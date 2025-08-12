package com.chef.dishes;

public abstract class AddOnDecorator extends Dish {
    protected final Dish inner;
    protected AddOnDecorator(Dish inner) { this.inner = inner; }

    @Override public String getName() { return inner.getName(); }
    @Override public double getBasePrice() { return inner.getBasePrice(); }
}
