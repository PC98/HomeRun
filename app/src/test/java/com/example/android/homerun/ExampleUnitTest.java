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
    public void isEmailValidNullCheck() {
        assertEquals(false, UtilityMethods.isEmailValid(null));
    }

    @Test
    public void isEmailValidInvalidEmail_nocom() {
        String testEmail = "test@gmail";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmailValidInvalidEmail_noAt() {
        String testEmail = "testgmail.com";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmailValidInvalidEmail_notEmail() {
        String testEmail = "notAnEmail";
        assertEquals(false, UtilityMethods.isEmailValid(testEmail));
    }

    @Test
    public void isEmailValid_validEmail() {
        String testEmail = "test@email.com";
        assertEquals(true, UtilityMethods.isEmailValid(testEmail));
    }

    /**
     * Test isNameValid() in UtilityMethods.java
     *
     * @author Animesh Fatehpuria
     */
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

    /**
     * Test isUsernameValid() in UtilityMethods.java
     *
     * @author Prabhav Chawla
     */
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

    /**
     * Test isPasswordValid() in UtilityMethods.java
     *
     * @author Anand Chaturvedi
     */
    @Test
    public void test_isPasswordValid() {
        // password cannot be null
        assertEquals(false, UtilityMethods.isPasswordValid(null));
        // password has to be six characters minimum
        CharSequence testPassword = "pass";
        assertEquals(false, UtilityMethods.isPasswordValid(testPassword));
        // accepted password
        testPassword = "Lifeofpablo4";
        assertEquals(true, UtilityMethods.isPasswordValid(testPassword));
    }

    /**
     * Unit tests for updating shelter in utility methods.
     * @author Camille Atere-Roberts
     */

    @Test
    public void shelterUpdaterTest() {
        Shelter testShelter = createShelterForTesting(5,5);
        UtilityMethods tester = new UtilityMethods();
        tester.updateShelter(testShelter,10,10);
        assertEquals(10,testShelter.getCurrentIndividualCapacity());
        assertEquals(10,testShelter.getCurrentFamilyCapacity());

    }
    @Test
    public void shelterUpdaterNullInputTest() {
        Shelter testShelter = createShelterForTesting(5,5);
        UtilityMethods tester = new UtilityMethods();
        tester.updateShelter(testShelter,null,10);
        assertEquals(10,testShelter.getCurrentIndividualCapacity());
        assertEquals(null,testShelter.getCurrentFamilyCapacity());

    }
}
