package com.example.android.homerun.model;

/**
 * An enum for Shelter gender categories
 */
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
