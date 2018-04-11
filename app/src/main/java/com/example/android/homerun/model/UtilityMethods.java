package com.example.android.homerun.model;


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
     * checks to make sure inputted email is valid
     * @return true of false indicating if an email is valid
     * @parameter an CharSequence representing an email
     */
    public static boolean isEmailValid(CharSequence email) {
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        return (email != null) && pat.matcher(email).matches();
    }

    /**
     * checks to make sure inputted username is valid
     * @return true of false indicating if a username is valid
     * @parameter an CharSequence representing a username
     */
    public static boolean isUsernameValid(CharSequence username) {
        Pattern pat = Pattern.compile(USERNAME_REGEX);
        return (username != null) && pat.matcher(username).matches();
    }

    /**
     * checks to make sure inputted password is valid
     * @return true of false indicating if a passoword is valid
     * @parameter an CharSequence representing a password
     */
    public static boolean isPasswordValid(CharSequence password) {
        Pattern pat = Pattern.compile(PASSWORD_REGEX);
        return (password != null) && pat.matcher(password).matches();
    }

    /**
     * checks to make sure inputted name is valid
     * @return true of false indicating if a name is valid
     * @parameter an CharSequence representing a name
     */
    public static boolean isNameValid(CharSequence name) {
        Pattern pat = Pattern.compile(NAME_REGEX);
        return (name != null) && pat.matcher(name).matches();
    }

    /**
     * Creates a new shelter database
     * @parameter an Input Stream
     * @parameter A Map of the shelters
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
     * Updates the capacities of a shelter
     * @parameter a shelter object representing a single shelter
     * @parameter the new family capacity
     * @parameter the new individual capacity
     */
    public static void updateShelter(Shelter shelter, Integer currentFamilyCapacity,
                                     Integer currentIndividualCapacity) {
        if (currentFamilyCapacity == null) {
            shelter.setCurrentIndividualCapacity(currentIndividualCapacity);

            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.DATABASE_SHELTERS)
                    .child(shelter.getId())
                    .child(FirebaseConstants.DATABASE_CUR_INDIVIDUAL_CAPACITY)
                    .setValue(shelter.getCurrentIndividualCapacity());

        } else {
            shelter.setCurrentFamilyCapacity(currentFamilyCapacity);

            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseConstants.DATABASE_SHELTERS)
                    .child(shelter.getId())
                    .child(FirebaseConstants.DATABASE_CUR_FAMILY_CAPACITY)
                    .setValue(shelter.getCurrentFamilyCapacity());
        }
    }

    /**
     * Updates the claimed shelter ID's and Claimed Spots of a user
     * @parameter a user object representing a single user
     * @parameter the new shelter ID
     * @parameter the new claimed spots
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
