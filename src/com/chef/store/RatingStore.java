package com.chef.store;

import java.util.*;

public class RatingStore {
    private final Map<String, List<Integer>> ratings = new HashMap<>();

    private String key(String r, String d) { return r + "|" + d; }

    public void rate(String restaurant, String dish, int stars) {
        if (stars < 1 || stars > 5) throw new IllegalArgumentException("1..5 only");
        ratings.computeIfAbsent(key(restaurant, dish), k -> new ArrayList<>()).add(stars);
    }

    public double avg(String restaurant, String dish) {
        var list = ratings.get(key(restaurant, dish));
        if (list == null || list.isEmpty()) return 0.0;
        return list.stream().mapToInt(i->i).average().orElse(0.0);
    }

    public static class Rated {
        public final String restaurant, dish; public final double avg;
        public Rated(String r, String d, double a){ restaurant=r; dish=d; avg=a; }
    }

    public Rated highestRated() {
        return ratings.entrySet().stream()
                .map(e -> {
                    double a = e.getValue().stream().mapToInt(i->i).average().orElse(0.0);
                    String[] parts = e.getKey().split("\\|",2);
                    return new Rated(parts[0], parts[1], a);
                })
                .max(Comparator.comparingDouble(r -> r.avg))
                .orElse(null);
    }
}
