package com.chef.pricing;

public class FlatDiscount implements DiscountStrategy {
    private final double amount;
    public FlatDiscount(double amount) { this.amount = amount; }
    @Override public double discountAmount(double subtotal) { return Math.min(amount, subtotal); }
    @Override public String label() { return "Flat ₹" + amount; }
}
