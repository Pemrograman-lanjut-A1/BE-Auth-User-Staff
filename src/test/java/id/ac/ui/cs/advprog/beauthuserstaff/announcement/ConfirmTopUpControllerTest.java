package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard.ConfirmTopUpController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class ConfirmTopUpControllerTest {

    @InjectMocks
    ConfirmTopUpController confirmTopUpController;


    @Mock
    RestTemplate restTemplate;

    @Mock
    JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    public void setUp() {
        confirmTopUpController = new ConfirmTopUpController();
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testConfirmTopUpSuccess() throws JSONException {

        when(jwtAuthFilter.filterToken("token")).thenReturn("STAFF");
        // Stubbing the restTemplate.exchange() method with exact arguments
        when(restTemplate.exchange(
                eq("http://34.142.213.219/topup/4e7deb2f-925b-4bbd-8833-14a8ef9cf918/confirm"), // URL
                eq(HttpMethod.PUT), // HTTP method
                any(), // Request entity
                eq(String.class) // Response type
        )).thenReturn(null);

        // Calling the method under test
        confirmTopUpController
                .confirmTopUp("token", "{\n    \"id\": \"4e7deb2f-925b-4bbd-8833-14a8ef9cf918\"\n}");

        // Verifying that the restTemplate.exchange() method was called with the expected arguments
        verify(restTemplate, times(1)).exchange(
                eq("http://34.142.213.219/topup/4e7deb2f-925b-4bbd-8833-14a8ef9cf918/confirm"),
                eq(HttpMethod.PUT),
                any(),
                eq(String.class)
        );
    }

    @Test
    void testConfirmTopUpForbidden() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn(null);
        ResponseEntity<String> result = confirmTopUpController
                .confirmTopUp("token", "{\n    \"id\": \"4e7deb2f-925b-4bbd-8833-14a8ef9cf918\"\n}");
        ResponseEntity<String> expectedResponse = ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                .body("You are not authorized to make this request");
        assertEquals(expectedResponse,result);
    }

    @Test
    void testConfirmTopUpWrongRole() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn("USER");
        ResponseEntity<String> result = confirmTopUpController
                .confirmTopUp("token", "{\n    \"id\": \"4e7deb2f-925b-4bbd-8833-14a8ef9cf918\"\n}");
        ResponseEntity<String> expectedResponse = ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                .body("You are not authorized to make this request");
        assertEquals(expectedResponse,result);
    }


    @Test
    void testGetAllWaitingTopUpsSuccess(){
        when(jwtAuthFilter.filterToken("token")).thenReturn("STAFF");
        when(restTemplate.exchange(
                eq("http://34.142.213.219/topup/waiting"), // URL
                eq(HttpMethod.GET), // HTTP method
                any(), // Request entity
                eq(String.class) // Response type
        )).thenReturn(null);

        // Calling the method under test
        confirmTopUpController
                .getAllWaitingTopUps("token");

        // Verifying that the restTemplate.exchange() method was called with the expected arguments
        verify(restTemplate, times(1)).exchange(
                eq("http://34.142.213.219/topup/waiting"), // URL
                eq(HttpMethod.GET),
                any(),
                eq(String.class)
        );
    }


    @Test
    void testGetAllWaitingTopUpsForbidden(){
        when(jwtAuthFilter.filterToken("token")).thenReturn(null);
        ResponseEntity<String> result = confirmTopUpController
                .getAllWaitingTopUps("token");
        ResponseEntity<String> expectedResponse = ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                .body("You are not authorized to make this request");
        assertEquals(expectedResponse,result);
    }

    @Test
    void testGetAllWaitingTopUpsWrongRole(){
        when(jwtAuthFilter.filterToken("token")).thenReturn("USER");
        ResponseEntity<String> result = confirmTopUpController
                .getAllWaitingTopUps("token");
        ResponseEntity<String> expectedResponse = ResponseEntity
                .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                .body("You are not authorized to make this request");
        assertEquals(expectedResponse,result);
    }
}