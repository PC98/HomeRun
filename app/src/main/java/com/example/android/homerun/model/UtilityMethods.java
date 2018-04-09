package com.example.android.homerun.model;


import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by PC on 2/20/18.
 */

public class UtilityMethods {

    // From https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    private static final String EMAIL_REGEX = "^[\\p{L}0-9_+&*-]+(?:\\."+
            "[\\p{L}0-9_+&*-]+)*@" +
            "(?:[\\p{L}0-9-]+\\.)+[\\p{L}]{2,7}$";

    // Similar to above
    private static final String USERNAME_REGEX = "^[\\p{L}0-9_+&*-]+(?:\\."+
            "[\\p{L}0-9_+&*-]+)*$";

    // No whitespaces and at-least 6 characters:
    private static final String PASSWORD_REGEX = "^(?=\\S+$).{6,}$";

    // Letters from any language and some special characters:
    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";

    public static boolean isEmailValid(String email) {
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        if (email == null)
            return false;

        return pat.matcher(email).matches();
    }

    public static boolean isUsernameValid(String username) {
        Pattern pat = Pattern.compile(USERNAME_REGEX);
        if (username == null)
            return false;

        return pat.matcher(username).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pat = Pattern.compile(PASSWORD_REGEX);
        if (password == null)
            return false;

        return pat.matcher(password).matches();
    }
    public static boolean isNameValid(String name) {
        Pattern pat = Pattern.compile(NAME_REGEX);
        if (name == null)
            return false;

        return pat.matcher(name).matches();
    }

    public static void createShelterDatabase(InputStream inputStream, HashMap<String, Shelter> sheterMap) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            reader.readLine(); // Skip the first line
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim().replaceAll("^\"|\"$", "");
                }

                Shelter shelter = new Shelter(row[0], row[1], row[2].isEmpty() ? null :
                        Integer.parseInt(row[2]), row[3].isEmpty() ? null :
                        Integer.parseInt(row[3]),
                        row[4], Double.parseDouble(row[5]), Double.parseDouble(row[6]), row[7],
                        row[8], row[9], AgeCategories.valueOf(row[10]), GenderCategories.valueOf(row[11]));
                sheterMap.put(shelter.getId(), shelter);
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.DATABASE_SHELTERS)
                        .child(shelter.getId())
                        .setValue(shelter);
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

    public static void updateShelter(Shelter shelter, Integer currentFamilyCapacity, Integer currentIndividualCapacity) {
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
