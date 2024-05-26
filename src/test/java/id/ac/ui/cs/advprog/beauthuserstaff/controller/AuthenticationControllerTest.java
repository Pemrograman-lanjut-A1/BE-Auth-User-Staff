package id.ac.ui.cs.advprog.beauthuserstaff.controller;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.controller.AuthenticationController;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthenticationController authenticationController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = mock(AuthService.class);
        authenticationController = new AuthenticationController(authService);
    }

    @Test
    void signUp_shouldReturnResponseEntity() throws Exception {
        when(authService.signUp(any(SignUpRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("Sign up successful")));

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@exam.com");
        signUpRequest.setUsername("john doe");
        signUpRequest.setPassword("Password1!");

        CompletableFuture<ResponseEntity<Object>> response = authenticationController.signUp(signUpRequest);

        ResponseEntity<Object> responseEntity = response.get();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Sign up successful", responseEntity.getBody());
    }

    @Test
    void signUpStaff_shouldReturnResponseEntity() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("staff@test.com");
        signUpRequest.setUsername("staff_user");
        signUpRequest.setPassword("Password1!");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Staff sign up successful");
        when(authService.signUpStaff(signUpRequest)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = authenticationController.signUpStaff(signUpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Staff sign up successful", response.getBody());
    }

    @Test
    void signIn_shouldReturnResponseEntity() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test@exam.com");
        signInRequest.setPassword("Password1!");

        CompletableFuture<ResponseEntity<Object>> expectedResponse =
                CompletableFuture.completedFuture(ResponseEntity.ok("Sign in successful"));
        when(authService.signIn(signInRequest)).thenReturn(expectedResponse);

        CompletableFuture<ResponseEntity<Object>> response = authenticationController.signIn(signInRequest);

        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertEquals("Sign in successful", response.get().getBody());
    }

    @Test
    void signUp_shouldReturnErrorResponseWhenInvalidRequest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("john_doe");
        signUpRequest.setPassword("Password1!");

        CompletableFuture<ResponseEntity<Object>> expectedResponse =
                CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sign up request"));
        when(authService.signUp(signUpRequest)).thenReturn(expectedResponse);

        CompletableFuture<ResponseEntity<Object>> response = authenticationController.signUp(signUpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.get().getStatusCode());
        assertEquals("Invalid sign up request", response.get().getBody());
    }

    @Test
    void logout_shouldReturnSuccessResponse() {
        String token = "sample_token";

        ResponseEntity<?> response = authenticationController.logout(token);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Berhasil logout", ((Map<String, Object>) response.getBody()).get("message"));
    }


    @Test
    void refresh_shouldReturnJwtAuthResponse() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("refresh_token_here");

        JwtAuthResponse expectedResponse = new JwtAuthResponse();
        expectedResponse.setToken("refresh_token_here");
        expectedResponse.setRefreshToken("new_token");
        when(authService.refreshToken(refreshTokenRequest)).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthResponse> response = authenticationController.refresh(refreshTokenRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void refresh_shouldReturnUnauthorizedWhenJwtExceptionThrown() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenThrow(ExpiredJwtException.class);

        ResponseEntity<JwtAuthResponse> response = authenticationController.refresh(refreshTokenRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(authService, times(1)).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    void refresh_shouldReturnBadRequestWhenOtherExceptionThrown() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenThrow(RuntimeException.class);

        ResponseEntity<JwtAuthResponse> response = authenticationController.refresh(refreshTokenRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(authService, times(1)).refreshToken(any(RefreshTokenRequest.class));
    }

}

