package com.example.android.homerun.model;

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
