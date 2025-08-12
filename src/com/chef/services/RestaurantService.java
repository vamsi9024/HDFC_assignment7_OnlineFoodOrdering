package com.chef.services;

import com.chef.dishes.BaseDish;
import com.chef.restaurant.Restaurant;
import com.chef.restaurant.RestaurantRegistry;
import com.chef.store.InMemoryDB;

public class RestaurantService {
    private final RestaurantRegistry registry;
    private final InMemoryDB db = InMemoryDB.get();

    public RestaurantService(RestaurantRegistry registry) { this.registry = registry; }

    public void registerRestaurant(Restaurant r) {
        registry.register(r);
        registry.checkBlock(r);
    }

    public void addDish(Restaurant r, BaseDish d) {
        r.addDish(d);
        registry.checkBlock(r);
    }

    public boolean updatePrice(Restaurant r, String dishName, double newPrice) {
        BaseDish base = r.getMenu().get(dishName);
        if (base == null) return false;
        double old = base.getBasePrice();
        base.setBasePrice(newPrice);
        registry.onPriceUpdated(r, dishName, old, newPrice); // observer callback
        return true;
    }

    public Restaurant findByName(String name) { return registry.findByName(name); }
}
