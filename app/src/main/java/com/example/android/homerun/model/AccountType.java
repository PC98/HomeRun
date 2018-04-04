package com.example.android.homerun.model;

/**
 * Created by PC on 2/19/18.
 */

public enum AccountType {
    USER ("User"), ADMIN ("Admin");

    private String type;

    AccountType(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
