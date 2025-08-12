package com.chef.restaurant;

import com.chef.store.InMemoryDB;
import com.chef.dishes.BaseDish;

public class RestaurantRegistry {
    private final InMemoryDB db = InMemoryDB.get();

    public void register(Restaurant r) { db.restaurants.add(r); }

    public void onPriceUpdated(Restaurant r, String dishName, double oldPrice, double newPrice) {
        if (oldPrice <= 0) return;
        double drop = (oldPrice - newPrice) / oldPrice;
        if (drop > 0.15) {
            for (Restaurant other : db.restaurants) {
                other.onCompetitorPriceDrop(dishName, oldPrice, newPrice, r);
            }
        }
    }

    public Restaurant findByName(String name) {
        return db.restaurants.stream().filter(x -> x.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void checkBlock(Restaurant r) {
        if (r.getMenu().size() < 5) r.setBlocked(true);
        else r.setBlocked(false);
    }
}
