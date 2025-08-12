package com.chef.services;

import com.chef.order.Bill;
import com.chef.order.Order;

public class OrderService {
    public Bill generateBill(Order order) { return Bill.from(order); }
}
