package com.chef.users;

import com.chef.enums.Role;

public class RestaurantOwner extends User {
    public RestaurantOwner(String email, String name) { super(email, name, Role.RESTAURANT_OWNER); }
}
