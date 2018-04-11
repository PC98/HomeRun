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

    /**
     * Unit tests for isEmailValid(String email) in UtilityMethods.java. Ensures that only valid
     * emails are used when interacting with the application
     * @author Jeffrey Jacob
     */
    @Test
    public void isEmailValid() {
        //Test if null
        assertEquals(false, UtilityMethods.isEmailValid(null));
        //Test if missing domain
        assertEquals(false, UtilityMethods.isEmailValid("test@gmail"));
        //Test if missing @ sign
        assertEquals(false, UtilityMethods.isEmailValid("testgmail.com"));
        //Test if not an email
        assertEquals(false, UtilityMethods.isEmailValid("notAnEmail"));
        //Test valid email
        assertEquals(true, UtilityMethods.isEmailValid("test@email.com"));
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
}
