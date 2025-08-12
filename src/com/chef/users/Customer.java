package com.chef.users;

import com.chef.enums.Role;

public class Customer extends User {
    public Customer(String email, String name) { super(email, name, Role.CUSTOMER); }
}
