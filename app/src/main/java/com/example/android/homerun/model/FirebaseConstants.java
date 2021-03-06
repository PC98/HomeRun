package com.example.android.homerun.model;

/**
 * Constants that relate to the Firebase database
 */
public class FirebaseConstants {
    public static final String DATABASE_USERS = "users";
    public static final String DATABASE_SHELTERS = "shelters";
    static final String DATABASE_CUR_FAMILY_CAPACITY = "currentFamilyCapacity";
    static final String DATABASE_CUR_INDIVIDUAL_CAPACITY = "currentIndividualCapacity";
    static final String DATABASE_CLAIMED_SHELTER_ID = "claimedShelterId";
    static final String DATABASE_CLAIMED_SPOTS = "claimedSpots";
    static final String DATABASE_INCORRECT_LOGIN_ATTEMPTS = "incorrectLoginAttempts";
    static final String DATABASE_LOCKOUT_DATE = "lockoutDate";
    public static final String DATABASE_USERNAME = "username";

    public static final String EMAIL_DOMAIN = "@homerun.com";
}
