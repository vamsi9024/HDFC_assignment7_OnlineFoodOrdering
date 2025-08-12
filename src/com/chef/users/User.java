package com.chef.users;

import com.chef.enums.Role;

public abstract class User {
    protected final String email;
    protected final String name;
    protected final Role role;
    protected User(String email, String name, Role role) {
        this.email = email; this.name = name; this.role = role;
    }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Role getRole() { return role; }
}
