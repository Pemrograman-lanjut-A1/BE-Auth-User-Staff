package id.ac.ui.cs.advprog.beauthuserstaff.dto;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRequestTest {

    @Test
    void testTokenGetterAndSetter() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        String expectedToken = "testToken";

        refreshTokenRequest.setToken(expectedToken);
        String actualToken = refreshTokenRequest.getToken();

        assertEquals(expectedToken, actualToken, "The token getter and setter should work correctly.");
    }
}
