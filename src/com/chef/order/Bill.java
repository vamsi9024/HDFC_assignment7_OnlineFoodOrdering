package com.chef.order;

import com.chef.pricing.DiscountStrategy;
import com.chef.restaurant.Restaurant;

public class Bill {
    public final double base;
    public final double discount;
    public final double gst; // 5%
    public final double total;
    public final String discountLabel;

    public Bill(double base, double discount, double gst, double total, String discountLabel) {
        this.base = base; this.discount = discount; this.gst = gst; this.total = total; this.discountLabel = discountLabel;
    }

    public void print() {
        System.out.println("\n----- BILL -----");
        System.out.println("Base: ₹" + String.format("%.2f", base));
        System.out.println("Discount (" + discountLabel + "): -₹" + String.format("%.2f", discount));
        System.out.println("GST 5%: ₹" + String.format("%.2f", gst));
        System.out.println("TOTAL: ₹" + String.format("%.2f", total));
        System.out.println("----------------\n");
    }

    public static Bill from(Order order) {
        double base = order.getItems().stream().mapToDouble(OrderItem::lineTotal).sum();
        Restaurant r = order.getRestaurant();
        DiscountStrategy ds = r.getDiscountStrategy();
        double discount = ds.discountAmount(base);
        double taxedBase = Math.max(0, base - discount);
        double gst = taxedBase * 0.05;
        double total = taxedBase + gst;
        return new Bill(base, discount, gst, total, ds.label());
    }
}
