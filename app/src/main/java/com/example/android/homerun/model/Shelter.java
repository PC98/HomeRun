package com.example.android.homerun.model;

import com.google.firebase.database.FirebaseDatabase;

/**
 * A class to represent a shelter
 */
public class Shelter {
    private String id;
    private String name;
    private String restrictions;
    private double longitude;
    private double latitude;
    private String address;
    private String specialNotes;
    private String phoneNumber;
    private AgeCategories ageCategory;
    private GenderCategories genderCategory;
    private Integer originalFamilyCapacity;
    private Integer originalIndividualCapacity;
    private Integer currentFamilyCapacity;
    private Integer currentIndividualCapacity;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public Shelter() {}

    /**
     * Getter for id
     *
     * @return this Shelter's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for name
     *
     * @return this Shelter's name
     */
    public String getName() {
        return name;
    }


    /**
     * Getter for currentIndividualCapacity
     *
     * @return this Shelter's current individual capacity
     */
    public Integer getCurrentIndividualCapacity() {
        return currentIndividualCapacity;
    }

    /**
     * Getter for currentFamilyCapacity
     *
     * @return this Shelter's current family capacity
     */
    public Integer getCurrentFamilyCapacity() {
        return currentFamilyCapacity;
    }

    /**
     * Getter for originalIndividualCapacity
     *
     * @return this Shelter's original individual capacity
     */
    public Integer getOriginalIndividualCapacity() {
        return originalIndividualCapacity;
    }

    /**
     * Getter for originalFamilyCapacity
     *
     * @return this Shelter's original family capacity
     */
    public Integer getOriginalFamilyCapacity() {
        return originalFamilyCapacity;
    }

    /**
     * Getter for restrictions
     *
     * @return this Shelter's restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Getter for longitude
     *
     * @return this Shelter's GPS longitude position
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for latitude
     *
     * @return this Shelter's GPS latitude position
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for address
     *
     * @return this Shelter's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for specialNotes
     *
     * @return this Shelter's special notes
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Getter for phoneNumber
     *
     * @return this Shelter's contact phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter for ageCategory
     *
     * @return this Shelter's age range as a AgeCategories type
     */
    public AgeCategories getAgeCategory() { return ageCategory; }

    /**
     * Getter for genderCategory
     *
     * @return the gender category that this Shelter caters to as a GenderCategories type
     */
    public GenderCategories getGenderCategory() { return genderCategory; }

    /**
     * Setter for currentIndividualCapacity
     *
     * @param i an integer value representing the new individual capacity for this Shelter
     */
    public void setCurrentIndividualCapacity(Integer i) {this.currentIndividualCapacity = i;}

    /**
     * Calls the setter for currentIndividualCapacity and update the value on Firebase as well
     *
     * @param i an integer value representing the new individual capacity for this Shelter
     */
    public void firebaseSetCurrentIndividualCapacity(Integer i) {
        setCurrentIndividualCapacity(i);

        FirebaseDatabase.getInstance().getReference(FirebaseConstants.DATABASE_SHELTERS)
                .child(id)
                .child(FirebaseConstants.DATABASE_CUR_INDIVIDUAL_CAPACITY)
                .setValue(currentIndividualCapacity);
    }

    /**
     * Setter for currentFamilyCapacity
     *
     * @param i an integer value representing the new family capacity for this Shelter
     */
    public void setCurrentFamilyCapacity(Integer i) {this.currentFamilyCapacity = i;}

    /**
     * Calls the setter for currentFamilyCapacity and updates the value on Firebase as well
     *
     * @param i an integer value representing the new family capacity for this Shelter
     */
    public void firebaseSetCurrentFamilyCapacity(Integer i) {
        setCurrentFamilyCapacity(i);

        FirebaseDatabase.getInstance().getReference(FirebaseConstants.DATABASE_SHELTERS).child(id)
                .child(FirebaseConstants.DATABASE_CUR_FAMILY_CAPACITY)
                .setValue(currentFamilyCapacity);
    }


    /**
     * Returns both the individual and family capacity of this Shelter as a String
     *
     * @return A String description of capacities
     */
    public String getCapacityString() {
        String capacityString = "";
        if (currentIndividualCapacity != null) {
            capacityString += currentIndividualCapacity + " individual spots";

            if (currentFamilyCapacity != null) {
                capacityString += ", ";
            }
        }
        if (currentFamilyCapacity != null) {
            capacityString += currentFamilyCapacity + " family spots";
        }
        if ((currentIndividualCapacity == null) && (currentFamilyCapacity == null)) {
            capacityString += "N/A";
        }
        return capacityString;
    }

    /**
     * Parameterized constructor required for creating a Shelter object from given data
     *
     * @param id id
     * @param name name
     * @param originalIndividualCapacity originalIndividualCapacity
     * @param originalFamilyCapacity originalFamilyCapacity
     * @param restrictions restrictions
     * @param longitude longitude
     * @param latitude latitude
     * @param address address
     * @param specialNotes specialNotes
     * @param phoneNumber phoneNumber
     * @param ageCategory ageCategory
     * @param genderCategory genderCategory
     */
    public Shelter(String id, String name, Integer originalIndividualCapacity,
                   Integer originalFamilyCapacity, String restrictions, double longitude,
                   double latitude, String address, String specialNotes, String phoneNumber,
                   AgeCategories ageCategory, GenderCategories genderCategory) {
        this.id = id;
        this.name = name;
        this.originalIndividualCapacity = originalIndividualCapacity;
        this.originalFamilyCapacity = originalFamilyCapacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.specialNotes = specialNotes;
        this.phoneNumber = phoneNumber;
        this.ageCategory = ageCategory;
        this.genderCategory = genderCategory;

        this.currentIndividualCapacity = originalIndividualCapacity;
        this.currentFamilyCapacity = originalFamilyCapacity;
    }
}
