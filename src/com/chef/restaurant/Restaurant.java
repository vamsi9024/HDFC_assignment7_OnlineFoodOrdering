package com.chef.restaurant;

import com.chef.dishes.BaseDish;
import com.chef.pricing.DiscountStrategy;
import com.chef.pricing.NoDiscount;

import java.util.*;

public class Restaurant implements PriceWarObserver {
    private final String name;
    private final Object owner;
    private final Map<String, BaseDish> menu = new LinkedHashMap<>();
    private boolean blocked = false;
    private DiscountStrategy strategy = new NoDiscount();

    public Restaurant(String name, Object owner) { this.name = name; this.owner = owner; }

    public String getName() { return name; }
    public Map<String, BaseDish> getMenu() { return menu; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean b) { this.blocked = b; }
    public DiscountStrategy getDiscountStrategy() { return strategy; }
    public void setDiscountStrategy(DiscountStrategy s) { this.strategy = s; }

    public void addDish(BaseDish d) { menu.put(d.getName(), d); }
    public void removeDish(String name) { menu.remove(name); }

    @Override
    public void onCompetitorPriceDrop(String dishName, double oldPrice, double newPrice, Restaurant source) {
        BaseDish me = menu.get(dishName);
        if (me == null || this == source) return;
        double cur = me.getBasePrice();
        double reduced = cur - (cur * 0.05);
        me.setBasePrice(Math.max(1, reduced));
        System.out.println("[" + this.name + "] reacted to price war on " + dishName + " -> new price ₹" + me.getBasePrice());
    }
}
