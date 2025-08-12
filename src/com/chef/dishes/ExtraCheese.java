package com.chef.dishes;

public class ExtraCheese extends AddOnDecorator {
    public ExtraCheese(Dish inner) { super(inner); }
    @Override public double getPrice() { return inner.getPrice() + 30; }
    @Override public String describe() { return inner.describe() + " + ExtraCheese"; }
}
