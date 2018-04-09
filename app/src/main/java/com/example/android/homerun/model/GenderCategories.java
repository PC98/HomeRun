package com.example.android.homerun.model;

/**
 * Created by PC on 3/4/18.
 */

public enum GenderCategories {
    MALE ("Male"), FEMALE ("Female"), ANYONE ("Anyone");

    private String type;

    GenderCategories(String s) {
        type = s;
    }

    public String toString() {
        return this.type;
    }
}
