package com.chef.users;

import com.chef.enums.Role;

public class SystemAdmin extends User {
    public SystemAdmin(String email, String name) { super(email, name, Role.ADMIN); }
}
