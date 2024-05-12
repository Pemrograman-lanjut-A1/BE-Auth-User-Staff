package id.ac.ui.cs.advprog.beauthuserstaff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.AuthServiceImpl;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTservice jwTservice;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signInSuccess() throws ExecutionException, JsonProcessingException, InterruptedException {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password");

        User user = new User();
        user.setType(UserType.REGULAR);
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));

        String mockToken = "mockedToken";
        CompletableFuture<String> mockTokenFuture = CompletableFuture.completedFuture(mockToken);
        when(jwTservice.generateToken(any(UserDetails.class))).thenReturn(mockTokenFuture);

        ResponseEntity<Object> responseEntity = authService.signIn(signInRequest).join();

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    void signInInvalidCredentials() throws ExecutionException, JsonProcessingException, InterruptedException {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("wrongpassword");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = authService.signIn(signInRequest).join();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void signInUserNotFound() throws ExecutionException, JsonProcessingException, InterruptedException {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("nonexistent@example.com");
        signInRequest.setPassword("wrongpassword");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = authService.signIn(signInRequest).join();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void signUp_InvalidPassword_BadRequestResponse() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("weak");

        CompletableFuture<ResponseEntity<Object>> futureResponse = authService.signUp(signUpRequest);
        ResponseEntity<Object> response = futureResponse.get();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void signUp_DuplicateEmail_BadRequestResponse() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("existing@example.com");
        signUpRequest.setPassword("Password123!");

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        CompletableFuture<ResponseEntity<Object>> futureResponse = authService.signUp(signUpRequest);
        ResponseEntity<Object> response = futureResponse.get();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void signIn_NonexistentEmail_BadRequestResponse() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("nonexistent@example.com");
        signInRequest.setPassword("Password123!");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        CompletableFuture<ResponseEntity<Object>> futureResponse = authService.signIn(signInRequest);
        ResponseEntity<Object> response = futureResponse.get();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    void refreshToken_ValidRequest_SuccessfulResponse() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("valid_token");

        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .type(UserType.REGULAR)
                .build();

        when(jwTservice.extractUsername(refreshTokenRequest.getToken())).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwTservice.isTokenValid(refreshTokenRequest.getToken(), user)).thenReturn(true);
        CompletableFuture<String> futureToken = CompletableFuture.completedFuture("new_token");
        when(jwTservice.generateToken(user)).thenReturn(futureToken);

        // Wait for the CompletableFuture to complete
        futureToken.join();

        JwtAuthResponse expectedResponse = new JwtAuthResponse();
        expectedResponse.setToken("new_token");
        expectedResponse.setRefreshToken(refreshTokenRequest.getToken());

        JwtAuthResponse response = authService.refreshToken(refreshTokenRequest);

        assertEquals(expectedResponse, response);
    }

    @Test
    void refreshToken_InvalidToken_NullResponse() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("invalid_token");

        when(jwTservice.extractUsername(refreshTokenRequest.getToken())).thenThrow(new RuntimeException());

        JwtAuthResponse response = authService.refreshToken(refreshTokenRequest);

        assertNull(response);
    }


    @Test
    void refreshToken_ValidTokenAndUser_ValidResponse() throws Exception {
        String email = "user@example.com";
        String token = "valid_token";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(token);

        when(jwTservice.extractUsername(refreshTokenRequest.getToken())).thenReturn(email);

        User user = User.builder().email(email).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CompletableFuture<String> tokenFuture = CompletableFuture.completedFuture(token);
        when(jwTservice.generateToken(user)).thenReturn(tokenFuture);

        when(jwTservice.isTokenValid(refreshTokenRequest.getToken(), user)).thenReturn(true);

        JwtAuthResponse response = authService.refreshToken(refreshTokenRequest);

        assertNotNull(response);
    }

}

