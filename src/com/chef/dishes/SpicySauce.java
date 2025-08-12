package com.chef.dishes;

public class SpicySauce extends AddOnDecorator {
    public SpicySauce(Dish inner) { super(inner); }
    @Override public double getPrice() { return inner.getPrice() + 15; }
    @Override public String describe() { return inner.describe() + " + SpicySauce"; }
}
