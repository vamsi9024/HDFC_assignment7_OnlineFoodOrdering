package com.chef.restaurant;

public interface PriceWarObserver {
    void onCompetitorPriceDrop(String dishName, double oldPrice, double newPrice, Restaurant source);
}
