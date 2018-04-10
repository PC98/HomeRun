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

    /**
     * Unit tests for getCapacityString() in Shelter.java.
     * @author Lauren
     */
    @Test
    public void shelterCapacityWithNoNullValues_isCorrect() {
        Shelter testShelter = createShelterForTesting(0,
                0
        );
        assertEquals("0 individual spots, 0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapacityWithNullFamilyValue_isCorrect() {
        Shelter testShelter = createShelterForTesting(0,
                null
        );
        assertEquals("0 individual spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapacityWithNullIndividualShelterValue_isCorrect() {
        Shelter testShelter = createShelterForTesting(null,
                0
        );
        assertEquals("0 family spots", testShelter.getCapacityString());
    }

    @Test
    public void shelterCapacityWithBothNull_isCorrect() {
        Shelter testShelter = createShelterForTesting(null,
                null
        );
        assertEquals("N/A", testShelter.getCapacityString());
    }

    @Test
    public void isEmalValidNullCheck() {
        assertEquals(false, UtilityMethods.isEmailValid(null));
    }

    @Test
    public void isEmalValidInvalidEmail_nocom() {
        String testEmail = "test@gmail";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmalValidInvalidEmail_noAt() {
        String testEmail = "testgmail.com";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmalValidInvalidEmail_notEmail() {
        String testEmail = "notAnEmail";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmalValid_validEmail() {
        String testEmail = "test@email.com";
        assertEquals(true, UtilityMethods.isEmailValid(testEmail));
    }
  
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

    @Test
    public void test_isUsernameValid() {
        // Can't have null or empty username
        assertEquals(false, UtilityMethods.isUsernameValid(null));
        assertEquals(false, UtilityMethods.isUsernameValid(""));

        // No spaces either
        assertEquals(false, UtilityMethods.isUsernameValid(" "));
        assertEquals(false, UtilityMethods.isUsernameValid("abc "));
        assertEquals(false, UtilityMethods.isUsernameValid(" abc"));
        assertEquals(false, UtilityMethods.isUsernameValid("a bc"));

        // No special characters (except email accepted characters)
        assertEquals(false, UtilityMethods.isUsernameValid("@"));
        assertEquals(false, UtilityMethods.isUsernameValid("$"));
        assertEquals(false, UtilityMethods.isUsernameValid("?"));
        assertEquals(false, UtilityMethods.isUsernameValid("!#%^&*"));

        // Allow alphanumeric and email accepted characters:
        assertEquals(true, UtilityMethods.isUsernameValid("pchawla"));
        assertEquals(true, UtilityMethods.isUsernameValid("pchawla8"));
        assertEquals(true, UtilityMethods.isUsernameValid("213adasd"));
        assertEquals(true, UtilityMethods.isUsernameValid("prabhav_chawla22"));
    }
}
