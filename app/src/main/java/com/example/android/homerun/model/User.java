package com.example.android.homerun.model;

public class User {
    private String name;
    private String username;
    private String password;
    private AccountType accountType;
    private String id;

    private String claimedShelterId;
    private String claimedSpots;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public User() {}

    /**
     * Parameterized constructor to create a new User object with the given data
     *
     * @param name name
     * @param username username
     * @param password password
     * @param accountType accountType
     */
    public User(String name, String username, String password, AccountType accountType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    /**
     * Getter for name
     *
     * @return this User's name
     */
    public String getName() {return name;}

    /**
     * Getter for username
     *
     * @return this User's username
     */
    public String getUsername() {return username;}

    /**
     * Getter for password
     *
     * @return this User's password
     */
    public String getPassword() {return password;}

    /**
     * Getter for id
     *
     * @return this User's ID
     */
    public String getId() {return id;}

    /**
     * Getter for claimedShelterId
     *
     * @return this User's claimed shelter id
     */
    public String getClaimedShelterId() {return claimedShelterId;}

    /**
     * Getter for claimedSpots
     *
     * @return a String representation of this User's claimed shelter spots
     */
    public String getClaimedSpots() {return claimedSpots;}

    /**
     * Getter for accountType
     *
     * @return this User's account type as an AccountType type
     */
    public AccountType getAccountType() {return accountType;}


    /**
     * Setter for claimedShelterId
     *
     * @param s String representing the newly claimed shelter's ID
     */
    public void setClaimedShelterId(String s) {this.claimedShelterId = s;}

    /**
     * Setter for claimedSpots
     *
     * @param s a String representation of this User's newly claimed shelter spots
     */
    public void setClaimedSpots(String s) {this.claimedSpots = s;}

    /**
     * Setter for id (called so that the id generated by Firebase can be assigned to this User)
     *
     * @param id this User's ID
     */
    public void setId(String id) {this.id = id;}
}
