package com.chef.pricing;

public class PercentageDiscount implements DiscountStrategy {
    private final double percent;
    public PercentageDiscount(double percent) { this.percent = percent; }
    @Override public double discountAmount(double subtotal) { return subtotal * (percent/100.0); }
    @Override public String label() { return percent + "% off"; }
}
