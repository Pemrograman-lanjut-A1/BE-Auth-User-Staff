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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTservice jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private SignUpRequest validSignUpRequest;
    private SignUpRequest invalidSignUpRequest;
    private SignInRequest validSignInRequest;
    private SignInRequest invalidSignInRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validSignUpRequest = new SignUpRequest();
        validSignUpRequest.setEmail("test@email.com");
        validSignUpRequest.setUsername("testUsername");
        validSignUpRequest.setPassword("ValidPassword123!");

        invalidSignUpRequest = new SignUpRequest();
        invalidSignUpRequest.setEmail("test@email.com");
        invalidSignUpRequest.setUsername("testUsername");
        invalidSignUpRequest.setPassword("invalid");

        validSignInRequest = new SignInRequest();
        validSignInRequest.setEmail("test@email.com");
        validSignInRequest.setPassword("validPassword");

        invalidSignInRequest = new SignInRequest();
        invalidSignInRequest.setEmail("test@email.com");
        invalidSignInRequest.setPassword("invalidPassword");
    }

    @Test
    void signUp_Success() throws ExecutionException, InterruptedException {
        User user = User.builder()
                .email(validSignUpRequest.getEmail())
                .username(validSignUpRequest.getUsername())
                .build();
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(CompletableFuture.completedFuture("mockToken"));

        CompletableFuture<ResponseEntity<Object>> result = authService.signUp(validSignUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.get().getStatusCode());
    }


    @Test
    void signUp_InvalidPassword() throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<Object>> result = authService.signUp(invalidSignUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.get().getStatusCode());
    }

    @Test
    void signUp_EmailAlreadyInUse() throws ExecutionException, InterruptedException {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Email is already in used"));

        CompletableFuture<ResponseEntity<Object>> result = authService.signUp(validSignUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.get().getStatusCode());
    }

    @Test
    void signIn_Success() throws ExecutionException, InterruptedException {
        User user = User.builder()
                .email(validSignInRequest.getEmail())
                .type(UserType.REGULAR)
                .password(passwordEncoder.encode(validSignInRequest.getPassword()))
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(CompletableFuture.completedFuture("mockToken"));

        CompletableFuture<ResponseEntity<Object>> result = authService.signIn(validSignInRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.ACCEPTED, result.get().getStatusCode());
    }

    @Test
    void signIn_InvalidCredentials() throws ExecutionException, InterruptedException {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        CompletableFuture<ResponseEntity<Object>> result = authService.signIn(invalidSignInRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.get().getStatusCode());
    }

    @Test
    void signIn_UserNotFound() throws ExecutionException, InterruptedException {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        CompletableFuture<ResponseEntity<Object>> result = authService.signIn(validSignInRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.get().getStatusCode());
    }

    @Test
    void signUpStaff_Success()  {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("staff@email.com");
        signUpRequest.setUsername("staffUsername");
        signUpRequest.setPassword("ValidPassword123!");
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .type(UserType.STAFF)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(CompletableFuture.completedFuture("mockToken"));

        ResponseEntity<Object> result = authService.signUpStaff(signUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void signUpStaff_InvalidPassword() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("staff@email.com");
        signUpRequest.setUsername("staffUsername");
        signUpRequest.setPassword("invalid");

        ResponseEntity<Object> result = authService.signUpStaff(signUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void signUpStaff_EmailAlreadyInUse() throws JsonProcessingException, ExecutionException, InterruptedException {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("staff@email.com");
        signUpRequest.setUsername("staffUsername");
        signUpRequest.setPassword("ValidPassword123!");
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Email is already in used"));

        ResponseEntity<Object> result = authService.signUpStaff(signUpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void refreshToken_Success() {
        String token = "validRefreshToken";
        String email = "test@email.com";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(token);
        User user = User.builder().email(email).build();
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(CompletableFuture.completedFuture("newJwtToken"));

        JwtAuthResponse result = authService.refreshToken(refreshTokenRequest);

        assertNotNull(result);
        assertEquals("newJwtToken", result.getToken());
        assertEquals(token, result.getRefreshToken());
    }

    @Test
    void refreshToken_InvalidToken() {
        String token = "invalidRefreshToken";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(token);
        when(jwtService.extractUsername(token)).thenReturn(null);

        JwtAuthResponse result = authService.refreshToken(refreshTokenRequest);

        assertNull(result);
    }

    @Test
    void refreshToken_UserNotFound() {
        String token = "validRefreshToken";
        String email = "test@email.com";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        JwtAuthResponse result = authService.refreshToken(refreshTokenRequest);

        assertNull(result);
    }

    @Test
    void refreshToken_TokenNotValid() {
        String token = "validRefreshToken";
        String email = "test@email.com";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(token);
        User user = User.builder().email(email).build();
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(false);

        JwtAuthResponse result = authService.refreshToken(refreshTokenRequest);

        assertNull(result);
    }

}