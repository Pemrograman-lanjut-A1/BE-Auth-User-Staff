package id.ac.ui.cs.advprog.beauthuserstaff.util;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util.ResponseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseHandlerTest {

    @Test
    void generateResponse_ShouldReturnCorrectResponse() {
        String message = "Test message";
        HttpStatus status = HttpStatus.OK;
        Object responseObj = new Object();

        ResponseEntity<Object> response = ResponseHandler.generateResponse(message, status, responseObj);

        assertEquals(status, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(message, responseBody.get("message"));
        assertEquals(status.value(), responseBody.get("status"));
        assertEquals(responseObj, responseBody.get("data"));
    }

    @Test
    void generateLogoutResponse_NullToken_ShouldReturnBadRequestResponse() {
        String token = null;
        Map<String, Object> response = new HashMap<>();

        ResponseHandler.generateLogoutResponse(token, response);

        assertEquals("Token tidak ditemukan.", response.get("message"));
        assertEquals(HttpStatus.BAD_REQUEST, response.get("status"));
    }

    @Test
    void generateLogoutResponse_NonNullToken_ShouldReturnAcceptedResponse() {
        String token = "sample_token";
        Map<String, Object> response = new HashMap<>();

        ResponseHandler.generateLogoutResponse(token, response);

        assertEquals("Berhasil logout", response.get("message"));
        assertEquals(HttpStatus.ACCEPTED, response.get("status"));
    }
}

