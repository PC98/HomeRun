package com.example.android.homerun.model;

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

    public Shelter() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    /**
     * returns shelter ID as a string
     * @return Shelter ID
     */
    public String getId() {
        return id;
    }

    /**
     * returns shelter name as a string
     * @return Shelter name
     */
    public String getName() {
        return name;
    }


    /**
     * returns shelter's current capacity for individuals as an integer
     * @return Shelter current individual capacity
     */
    public Integer getCurrentIndividualCapacity() {
        return currentIndividualCapacity;
    }

    /**
     * returns shelter's current capacity for families as an integer
     * @return Shelter current family capacity
     */
    public Integer getCurrentFamilyCapacity() {
        return currentFamilyCapacity;
    }

    /**
     * returns shelter's original capacity for individuals as an integer
     * @return Shelter original individual capacity
     */
    public Integer getOriginalIndividualCapacity() {
        return originalIndividualCapacity;
    }

    /**
     * returns shelter's original capacity for families as an integer
     * @return Shelter original family capacity
     */
    public Integer getOriginalFamilyCapacity() {
        return originalFamilyCapacity;
    }

    /**
     * returns shelter's restrictions as a string
     * @return Shelter's Restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }

    /**
     * returns shelter's GPS longitude position as a double
     * @return Shelter's GPS longitude position
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * returns shelter's GPS latitude position as a double
     * @return Shelter's GPS latitude position
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * returns shelter's address as a string
     * @return Shelter's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * returns shelter's special notes as a string
     * @return Shelter's special notes
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * returns shelter's contact phone number as a string
     * @return Shelter's contact phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * returns shelter's age range as an ageCategory
     * @return Shelter's age range
     */
    public AgeCategories getAgeCategory() { return ageCategory; }

    /**
     * returns the gender category of which th shelter caters too as a genderCategory object
     * @return Shelter's genderCategory
     */
    public GenderCategories getGenderCategory() { return genderCategory; }

    /**
     * changes the value of the Shelter's individual capacity based on the user
     * inputted integer
     * @parameter an integer value representing a new individual capacity
     */
    public void setCurrentIndividualCapacity(Integer i) {this.currentIndividualCapacity = i;}

    /**
     * changes the value of the Shelter's family capacity based on the user
     * inputted integer
     * @parameter an integer value representing a new family capacity
     */
    public void setCurrentFamilyCapacity(Integer i) {this.currentFamilyCapacity = i;}

    /**
     * returns both the individual and family capacity of the shelter
     * as a string
     * @return Shelter's capacity
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
