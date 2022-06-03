package com.example.hospital_2.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PATIENT, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
