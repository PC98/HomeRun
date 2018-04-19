package com.example.android.homerun.model;

/**
 * An enum for Shelter age categories
 */
public enum AgeCategories {
    FAMILIES_WITH_NEWBORNS ("Families with newborns"), CHILDREN ("Children"),
    YOUNG_ADULTS ("Young adults"), ANYONE ("Anyone");

    private final String type;

    AgeCategories(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
