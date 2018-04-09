package com.example.android.homerun.model;

public class User {
    private String name;
    private String username;
    private String password;
    private AccountType accountType;
    private String id;

    private String claimedShelterId;
    private String claimedSpots;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String username, String password, AccountType accountType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getName() {return name;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}

    public String getId() {return id;}
    public String getClaimedShelterId() {return claimedShelterId;}
    public String getClaimedSpots() {return claimedSpots;}
    public AccountType getAccountType() {return accountType;}

    public void setClaimedShelterId(String s) {this.claimedShelterId = s;}
    public void setClaimedSpots(String s) {this.claimedSpots = s;}
    public void setId(String id) {this.id = id;}
}
