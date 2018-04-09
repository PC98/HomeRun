package com.example.android.homerun;

import com.example.android.homerun.model.AgeCategories;
import com.example.android.homerun.model.GenderCategories;
import com.example.android.homerun.model.Shelter;
import com.example.android.homerun.model.UtilityMethods;

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
        Shelter testShelter = createShelterForTesting(0,
                0
        );
        assertEquals("0 individual spots, 0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithNullFamilyValue_isCorrect() {
        Shelter testShelter = createShelterForTesting(0,
                null
        );
        assertEquals("0 individual spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithNullIndividualShelterValue_isCorrect() {
        Shelter testShelter = createShelterForTesting(null,
                0
        );
        assertEquals("0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapcaityWithBothNull_isCorrect() {
        Shelter testShelter = createShelterForTesting(null,
                null
        );
        assertEquals("N/A", testShelter.getCapacityString());
    }

    // Animesh Fatehpuria
    @Test
    public void testIsNameValid() {
        // Test for empty string
        assertEquals(false, UtilityMethods.isNameValid(""));
        // Test for null string
        assertEquals(false, UtilityMethods.isNameValid(null));
        // Tests for strings that does not match NAME_REGEX
        assertEquals(false, UtilityMethods.isNameValid("animesh123"));
        assertEquals(false, UtilityMethods.isNameValid("animesh@"));
        assertEquals(false, UtilityMethods.isNameValid("animesh~"));
        assertEquals(false, UtilityMethods.isNameValid("animesh!"));
        assertEquals(false, UtilityMethods.isNameValid("animesh#"));
        assertEquals(false, UtilityMethods.isNameValid("animesh$"));
        assertEquals(false, UtilityMethods.isNameValid("animesh%"));
        assertEquals(false, UtilityMethods.isNameValid("animesh^"));
        assertEquals(false, UtilityMethods.isNameValid("animesh&"));
        assertEquals(false, UtilityMethods.isNameValid("animesh*"));
        assertEquals(false, UtilityMethods.isNameValid("animesh("));
        assertEquals(false, UtilityMethods.isNameValid("animesh_"));
        assertEquals(false, UtilityMethods.isNameValid("animesh)"));
        assertEquals(false, UtilityMethods.isNameValid("animesh+"));
        // Tests for valid strings
        assertEquals(true, UtilityMethods.isNameValid("animesh.fatehpuria"));
        assertEquals(true, UtilityMethods.isNameValid("animesh fatehpuria"));
        assertEquals(true, UtilityMethods.isNameValid(" animesh   fatehpuria "));
        assertEquals(true, UtilityMethods.isNameValid("animesh français"));
    }

    /*
     * This method is used for any tests that need a Shelter. It creates a new
     * shelter each time with the given parameters.
     */
    private Shelter createShelterForTesting(Integer originalIndividualCapacity,
                                            Integer originalFamilyCapacity) {
        return (new Shelter("1",
                "TestShelter",
                            originalIndividualCapacity,
                            originalFamilyCapacity,
                "",
                0.0,
                0.0,
                "",
                "No special notes",
                "1234561234",
                AgeCategories.ANYONE,
                GenderCategories.ANYONE));
    }
}
