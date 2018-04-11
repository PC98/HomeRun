package com.example.android.homerun.model;

/**
 * An enum for categories that the list of shelters can be filtered by
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
