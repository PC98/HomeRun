package com.example.android.homerun;

import com.example.android.homerun.model.AgeCategories;
import com.example.android.homerun.model.GenderCategories;
import com.example.android.homerun.model.Shelter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void shelterCapacityWithNoNullValues_isCorrect() {
        Shelter testShelter = createShelterForTesting("1", "TestShelter", 0,
                0, "", 0.0, 0.0, "",
                "No special notes", "1234561234",
                AgeCategories.ANYONE, GenderCategories.ANYONE);
        assertEquals("0 individual spots, 0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithNullFamilyValue_isCorrect() {
        Shelter testShelter = createShelterForTesting("1", "TestShelter", 0,
                null, "", 0.0, 0.0, "",
                "No special notes", "1234561234",
                AgeCategories.ANYONE, GenderCategories.ANYONE);
        assertEquals("0 individual spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithNullIndividualShelterValue_isCorrect() {
        Shelter testShelter = createShelterForTesting("1", "TestShelter", null,
                0, "", 0.0, 0.0, "",
                "No special notes", "1234561234",
                AgeCategories.ANYONE, GenderCategories.ANYONE);
        assertEquals("0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithBothNull_isCorrect() {
        Shelter testShelter = createShelterForTesting("1", "TestShelter", null,
                null, "", 0.0, 0.0, "",
                "No special notes", "1234561234",
                AgeCategories.ANYONE, GenderCategories.ANYONE);
        assertEquals("N/A", testShelter.getCapacityString());
    }

    /*
     * This method is used for any tests that need a Shelter. It creates a new
     * shelter each time with the given parameters.
     */
    private Shelter createShelterForTesting(String id,
                                            String name,
                                            Integer originalIndividualCapacity,
                                            Integer originalFamilyCapacity,
                                            String restrictions,
                                            double longitude,
                                            double latitude,
                                            String address,
                                            String specialNotes,
                                            String phoneNumber,
                                            AgeCategories ageCategory,
                                            GenderCategories genderCategory) {
        return (new Shelter(id,
                            name,
                            originalIndividualCapacity,
                            originalFamilyCapacity,
                            restrictions,
                            longitude,
                            latitude,
                            address,
                            specialNotes,
                            phoneNumber,
                            ageCategory,
                            genderCategory));
    }
    @Test
    public void PasswordIsNull() {
        Charsequence testPassword = null;
        assertEquals(false, isPasswordValid(testPassword));
    }
    @Test
    public void PasswordIsNotValid() {
        Charsequence testPassword = "pass";
        assertEquals(false, isPasswordValid(testPassword));
    }
    @Test
    public void PasswordIsValid() {
        Charsequence testPassword = "Lifeofpablo4";
        assertEquals(true, isPasswordValid(testPassword));
    }
}
