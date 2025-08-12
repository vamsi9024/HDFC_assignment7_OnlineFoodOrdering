package com.chef.services;

import com.chef.order.Bill;
import com.chef.order.Order;
import com.chef.order.OrderItem;
import com.chef.store.InMemoryDB;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final InMemoryDB db = InMemoryDB.get();

    public void record(Order order, Bill bill) {
        db.revenueByRestaurant.merge(order.getRestaurant().getName(), bill.total, Double::sum);
        for (OrderItem it : order.getItems()) {
            db.dishCount.merge(it.getDish().getName(), it.getQty(), Integer::sum);
        }
    }

    public LinkedHashMap<String,Integer> top3DishesOverall() {
        return db.dishCount.entrySet().stream()
                .sorted((a,b)->Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (a,b)->a, LinkedHashMap::new));
    }

    public Map.Entry<String,Double> restaurantWithHighestRevenue() {
        return db.revenueByRestaurant.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElse(null);
    }
}
