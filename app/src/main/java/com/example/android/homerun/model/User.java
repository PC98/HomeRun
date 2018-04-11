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

    /**
     * returns user's name as a string
     * @return user's name
     */
    public String getName() {return name;}

    /**
     * returns user's username as a string
     * @return account username
     */
    public String getUsername() {return username;}

    /**
     * returns user's account password as a string
     * @return account password
     */
    public String getPassword() {return password;}

    /**
     * returns user's id as a string
     * @return account ID
     */
    public String getId() {return id;}

    /**
     * returns user's claimed shelter id as a string
     * @return claimed shelter id
     */
    public String getClaimedShelterId() {return claimedShelterId;}

    /**
     * returns user's claimed shelter spots as a string
     * @return claimed shelter spots
     */
    public String getClaimedSpots() {return claimedSpots;}

    /**
     * returns user's type of account
     * @return account type
     */
    public AccountType getAccountType() {return accountType;}


    /**
     * changes the value of the claimed shelter id capacity based on the user
     * inputted string
     * @parameter an string representing a new shelter ID
     */
    public void setClaimedShelterId(String s) {this.claimedShelterId = s;}

    /**
     * changes the value of the claimed shelter spots capacity based on the user
     * inputted string
     * @parameter an string representing a new shelter spot
     */
    public void setClaimedSpots(String s) {this.claimedSpots = s;}

    /**
     * changes the value of the account based on the user
     * inputted string
     * @parameter an string representing a new ID
     */
    public void setId(String id) {this.id = id;}
}
