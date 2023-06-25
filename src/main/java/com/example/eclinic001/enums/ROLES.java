package com.example.eclinic001.enums;

public enum ROLES {
    USER("USER"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    ADMIN("ADMIN");

    private String authorities;

    ROLES(String authorities) {
        this.authorities = authorities;
    }

    public String getAuthorities() {
        return authorities;
    }
}
