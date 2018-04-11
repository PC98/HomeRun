package com.example.android.homerun.model;


import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;

public class UtilityMethods {

    // From https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    private static final String EMAIL_REGEX = "^[\\p{L}0-9_+&*-]+(?:\\."+
            "[\\p{L}0-9_+&*-]+)*@" +
            "(?:[\\p{L}0-9-]+\\.)+[\\p{L}]{2,7}$";

    // Similar to above, except no @
    private static final String USERNAME_REGEX = "^[\\p{L}0-9_+&*-]+(?:\\."+
            "[\\p{L}0-9_+&*-]+)*$";

    // No whitespaces and at-least 6 characters:
    private static final String PASSWORD_REGEX = "^(?=\\S+$).{6,}$";

    // Letters from any language and some special characters:
    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";

    /**
     * Checks if the user inputted email is valid using EMAIL_REGEX
     *
     * @param email CharSequence representing an email
     * @return true of false indicating if email is valid
     */
    public static boolean isEmailValid(CharSequence email) {
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        return (email != null) && pat.matcher(email).matches();
    }

    /**
     * Checks if the user inputted username is valid using USERNAME_REGEX
     *
     * @param username CharSequence representing a username
     * @return true of false indicating if username is valid
     */
    public static boolean isUsernameValid(CharSequence username) {
        Pattern pat = Pattern.compile(USERNAME_REGEX);
        return (username != null) && pat.matcher(username).matches();
    }

    /**
     * Checks if the user inputted password is valid using PASSWORD_REGEX
     *
     * @param password CharSequence representing a password
     * @return true of false indicating if password is valid
     */
    public static boolean isPasswordValid(CharSequence password) {
        Pattern pat = Pattern.compile(PASSWORD_REGEX);
        return (password != null) && pat.matcher(password).matches();
    }

    /**
     * Checks if the user inputted name is valid using NAME_REGEX
     *
     * @param name CharSequence representing a name
     * @return true of false indicating if name is valid
     */
    public static boolean isNameValid(CharSequence name) {
        Pattern pat = Pattern.compile(NAME_REGEX);
        return (name != null) && pat.matcher(name).matches();
    }

    /**
     * Creates a new shelter database on Firebase
     *
     * @param inputStream the input stream from which Shelter data is to be parsed
     * @param shelterMap a map of Shelter objects using ID as key that needs to be populated
     */
    public static void createShelterDatabase(InputStream inputStream, Map<String,
            Shelter> shelterMap) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            reader.readLine(); // Skip the first line
            String csvLine = reader.readLine();
            while (csvLine != null) {
                String[] row = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim().replaceAll("^\"|\"$", "");
                }

                Shelter shelter = new Shelter(row[0], row[1], row[2].isEmpty() ? null :
                        Integer.parseInt(row[2]), row[3].isEmpty() ? null :
                        Integer.parseInt(row[3]), row[4], Double.parseDouble(row[5]),
                        Double.parseDouble(row[6]), row[7], row[8], row[9],
                        AgeCategories.valueOf(row[10]), GenderCategories.valueOf(row[11]));
                shelterMap.put(shelter.getId(), shelter);
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.DATABASE_SHELTERS)
                        .child(shelter.getId())
                        .setValue(shelter);

                csvLine = reader.readLine();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }

    /**
     * Updates either currentIndividualCapacity OR currentFamilyCapacity of a Shelter, both locally
     * and on Firebase (if possible). Either the currentIndividualCapacity or the
     * currentFamilyCapacity parameter is null - this is done so that only one attribute of Shelter
     * is updated in a single call and Firebase doesn't register multiple onChildChanged() callbacks
     * in DashboardActivity.java
     *
     * @param shelter the Shelter object that needs to be modified
     * @param currentIndividualCapacity the new individual capacity of shelter, else null
     * @param currentFamilyCapacity the new family capacity of shelter, else null
     */
    public static void updateShelter(Shelter shelter, Integer currentIndividualCapacity,
                                     Integer currentFamilyCapacity) {
        if (currentFamilyCapacity == null) {
            shelter.setCurrentIndividualCapacity(currentIndividualCapacity);

            try {
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.DATABASE_SHELTERS)
                        .child(shelter.getId())
                        .child(FirebaseConstants.DATABASE_CUR_INDIVIDUAL_CAPACITY)
                        .setValue(shelter.getCurrentIndividualCapacity());
            } catch (Exception e) {
                Log.e("Firebase", e.getMessage());
            }


        } else {
            assert currentIndividualCapacity == null;

            shelter.setCurrentFamilyCapacity(currentFamilyCapacity);

            try {
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.DATABASE_SHELTERS)
                        .child(shelter.getId())
                        .child(FirebaseConstants.DATABASE_CUR_FAMILY_CAPACITY)
                        .setValue(shelter.getCurrentFamilyCapacity());
            } catch (Exception e) {
                Log.e("Firebase", e.getMessage());
            }
        }
    }

    /**
     * Updates claimedShelterId and claimedSpots of a User, both locally and on Firebase.
     *
     * @param user the User object that needs to be modified
     * @param claimedShelterId the ID of the newly claimed shelter by user
     * @param claimedSpots a String description of the the newly claimed shelter spots by user
     */
    public static void updateUser(User user, String claimedShelterId, String claimedSpots) {

        user.setClaimedShelterId(claimedShelterId);
        user.setClaimedSpots(claimedSpots);

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DATABASE_USERS)
                .child(user.getId())
                .child(FirebaseConstants.DATABASE_CLAIMED_SHELTER_ID)
                .setValue(user.getClaimedShelterId());

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DATABASE_USERS)
                .child(user.getId())
                .child(FirebaseConstants.DATABASE_CLAIMED_SPOTS)
                .setValue(user.getClaimedSpots());
    }
}
