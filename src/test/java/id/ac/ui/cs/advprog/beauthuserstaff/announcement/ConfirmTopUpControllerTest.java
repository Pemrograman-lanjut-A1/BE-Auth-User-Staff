package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController.ConfirmTopUpController;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.ConfirmTopUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ConfirmTopUpControllerTest {

    @InjectMocks
    ConfirmTopUpController confirmTopUpController;

    @Mock
    ConfirmTopUpService confirmTopUpService;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        confirmTopUpController = new ConfirmTopUpController();
        MockitoAnnotations.initMocks(this); // Initialize mocks
    }

    @Test
    public void testConfirmTopUp() throws JSONException {
        // Stubbing the restTemplate.exchange() method with exact arguments
        when(restTemplate.exchange(
                eq("http://localhost:8081/topup/4e7deb2f-925b-4bbd-8833-14a8ef9cf918/confirm"), // URL
                eq(HttpMethod.PUT), // HTTP method
                any(), // Request entity
                eq(String.class) // Response type
        )).thenReturn(null);

        // Calling the method under test
        ResponseEntity<String> responseEntity = confirmTopUpController
                .confirmTopUp("{\n    \"id\": \"4e7deb2f-925b-4bbd-8833-14a8ef9cf918\"\n}");

        // Verifying that the restTemplate.exchange() method was called with the expected arguments
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8081/topup/4e7deb2f-925b-4bbd-8833-14a8ef9cf918/confirm"),
                eq(HttpMethod.PUT),
                any(),
                eq(String.class)
        );
    }


    @Test
    public void testGetAllWaitingTopUps(){
        when(restTemplate.exchange(
                eq("http://localhost:8081/topup/waiting"), // URL
                eq(HttpMethod.GET), // HTTP method
                any(), // Request entity
                eq(String.class) // Response type
        )).thenReturn(null);

        // Calling the method under test
        ResponseEntity<?> responseEntity = confirmTopUpController
                .getAllWaitingTopUps();

        // Verifying that the restTemplate.exchange() method was called with the expected arguments
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8081/topup/waiting"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class)
        );
    }
}