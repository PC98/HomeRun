package com.example.android.homerun.model;

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
