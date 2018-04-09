package com.example.android.homerun.model;
import java.io.Serializable;

/**
 * Created by PC on 2/25/18.
 */

public class Shelter implements Serializable {
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrentIndividualCapacity() {
        return currentIndividualCapacity;
    }

    public Integer getCurrentFamilyCapacity() {
        return currentFamilyCapacity;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AgeCategories getAgeCategory() { return ageCategory; }

    public GenderCategories getGenderCategory() { return genderCategory; }

    public void setCurrentIndividualCapacity(Integer i) {this.currentIndividualCapacity = i;}

    public void setCurrentFamilyCapacity(Integer i) {this.currentFamilyCapacity = i;}

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
