package com.example.android.homerun.model;

public enum GenderCategories {
    MALE ("Male"), FEMALE ("Female"), ANYONE ("Anyone");

    private final String type;

    GenderCategories(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
