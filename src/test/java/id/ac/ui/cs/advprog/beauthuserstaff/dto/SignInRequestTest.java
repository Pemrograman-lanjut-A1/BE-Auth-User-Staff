package id.ac.ui.cs.advprog.beauthuserstaff.dto;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignInRequestTest {

    @Test
    void testEmailGetterAndSetter() {
        SignInRequest signInRequest = new SignInRequest();
        String expectedEmail = "test@example.com";

        signInRequest.setEmail(expectedEmail);
        String actualEmail = signInRequest.getEmail();

        assertEquals(expectedEmail, actualEmail, "The email getter and setter should work correctly.");
    }

    @Test
    void testPasswordGetterAndSetter() {
        SignInRequest signInRequest = new SignInRequest();
        String expectedPassword = "securePassword";

        signInRequest.setPassword(expectedPassword);
        String actualPassword = signInRequest.getPassword();

        assertEquals(expectedPassword, actualPassword, "The password getter and setter should work correctly.");
    }
}
