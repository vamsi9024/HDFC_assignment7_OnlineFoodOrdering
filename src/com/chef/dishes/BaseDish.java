package com.chef.dishes;

public class BaseDish extends Dish {
    private final String name;
    private double basePrice;

    public BaseDish(String name, double basePrice) { this.name = name; this.basePrice = basePrice; }

    public void setBasePrice(double p) { this.basePrice = p; }

    @Override public String getName() { return name; }
    @Override public double getBasePrice() { return basePrice; }
    @Override public double getPrice() { return basePrice; }
    @Override public String describe() { return name; }
}
