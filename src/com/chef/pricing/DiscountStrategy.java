package com.chef.pricing;

public interface DiscountStrategy {
    double discountAmount(double subtotal);
    String label();
}
