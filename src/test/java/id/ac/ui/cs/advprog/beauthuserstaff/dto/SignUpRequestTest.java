package id.ac.ui.cs.advprog.beauthuserstaff.dto;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestTest {

    @Test
    void testEmailGetterAndSetter() {
        SignUpRequest signUpRequest = new SignUpRequest();
        String expectedEmail = "test@example.com";

        signUpRequest.setEmail(expectedEmail);
        String actualEmail = signUpRequest.getEmail();

        assertEquals(expectedEmail, actualEmail, "The email getter and setter should work correctly.");
    }

    @Test
    void testUsernameGetterAndSetter() {
        SignUpRequest signUpRequest = new SignUpRequest();
        String expectedUsername = "testUser";

        signUpRequest.setUsername(expectedUsername);
        String actualUsername = signUpRequest.getUsername();

        assertEquals(expectedUsername, actualUsername, "The username getter and setter should work correctly.");
    }

    @Test
    void testPasswordGetterAndSetter() {
        SignUpRequest signUpRequest = new SignUpRequest();
        String expectedPassword = "securePassword";

        signUpRequest.setPassword(expectedPassword);
        String actualPassword = signUpRequest.getPassword();

        assertEquals(expectedPassword, actualPassword, "The password getter and setter should work correctly.");
    }
}
