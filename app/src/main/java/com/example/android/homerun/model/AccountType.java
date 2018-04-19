package com.example.android.homerun.model;

/**
 * An enum for User account types
 */
public enum AccountType {
    USER ("User"), ADMIN ("Admin");

    private final String type;

    AccountType(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
