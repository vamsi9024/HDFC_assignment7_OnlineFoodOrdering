package com.chef.pricing;

public class NoDiscount implements DiscountStrategy {
    @Override public double discountAmount(double subtotal) { return 0; }
    @Override public String label() { return "No Discount"; }
}
