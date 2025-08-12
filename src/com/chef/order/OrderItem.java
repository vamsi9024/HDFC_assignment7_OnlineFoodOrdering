package com.chef.order;

import com.chef.dishes.Dish;

public class OrderItem {
    private final Dish dish;
    private final int qty;
    public OrderItem(Dish dish, int qty) { this.dish = dish; this.qty = qty; }
    public Dish getDish() { return dish; }
    public int getQty() { return qty; }
    public double lineTotal() { return dish.getPrice() * qty; }
}
