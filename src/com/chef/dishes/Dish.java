package com.chef.dishes;

public abstract class Dish {
    public abstract String getName();
    public abstract double getBasePrice();  // price of the underlying base dish
    public abstract double getPrice();      // base + add-ons
    public abstract String describe();      // name + add-ons chain
}
