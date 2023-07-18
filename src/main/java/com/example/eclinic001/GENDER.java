package com.example.eclinic001;

public enum GENDER {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String gender;

    GENDER(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
