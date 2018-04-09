package com.example.android.homerun.model;

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
