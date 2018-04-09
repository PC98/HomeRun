package com.example.android.homerun.model;

/**
 * Created by PC on 3/4/18.
 */

public enum FilterCategories {
    NAME ("Name"), GENDER ("Gender"), AGE("Age");

    private final String type;

    FilterCategories(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
