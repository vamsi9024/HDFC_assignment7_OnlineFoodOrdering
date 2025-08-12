package com.chef.dishes;

public class ExtraChicken extends AddOnDecorator {
    public ExtraChicken(Dish inner) { super(inner); }
    @Override public double getPrice() { return inner.getPrice() + 60; }
    @Override public String describe() { return inner.describe() + " + ExtraChicken"; }
}
