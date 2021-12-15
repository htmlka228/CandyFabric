package org.vaadin.example.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    FABRIC_USER,
    SHOP_USER,
    SUPPLIER_USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
