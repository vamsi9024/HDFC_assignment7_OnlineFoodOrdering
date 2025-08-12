package com.chef.store;

import com.chef.order.Order;
import com.chef.restaurant.Restaurant;
import com.chef.users.Customer;

import java.util.*;

public class InMemoryDB {
    public final List<Restaurant> restaurants = new ArrayList<>();
    public final List<Customer> customers = new ArrayList<>();
    public final List<Order> orders = new ArrayList<>();
    public final Map<String, Integer> dishCount = new HashMap<>();
    public final Map<String, Double> revenueByRestaurant = new HashMap<>();

    private static final InMemoryDB INSTANCE = new InMemoryDB();
    private InMemoryDB() {}
    public static InMemoryDB get() { return INSTANCE; }
}
