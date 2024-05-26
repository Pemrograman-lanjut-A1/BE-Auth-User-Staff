package id.ac.ui.cs.advprog.beauthuserstaff.dto;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthResponseTest {

    @Test
    void testTokenGetterAndSetter() {
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        String expectedToken = "testToken";

        jwtAuthResponse.setToken(expectedToken);
        String actualToken = jwtAuthResponse.getToken();

        assertEquals(expectedToken, actualToken, "The token getter and setter should work correctly.");
    }

    @Test
    void testRefreshTokenGetterAndSetter() {
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        String expectedRefreshToken = "testRefreshToken";

        jwtAuthResponse.setRefreshToken(expectedRefreshToken);
        String actualRefreshToken = jwtAuthResponse.getRefreshToken();

        assertEquals(expectedRefreshToken, actualRefreshToken, "The refresh token getter and setter should work correctly.");
    }
}
