package com.chef.order;

import com.chef.enums.OrderStatus;
import com.chef.restaurant.Restaurant;
import com.chef.users.Customer;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Customer customer;
    private final Restaurant restaurant;
    private OrderStatus status = OrderStatus.CREATED;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(Customer c, Restaurant r) { this.customer = c; this.restaurant = r; }

    public void addItem(OrderItem item) { items.add(item); }
    public List<OrderItem> getItems() { return items; }
    public Restaurant getRestaurant() { return restaurant; }
    public Customer getCustomer() { return customer; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus s) { this.status = s; }
}
