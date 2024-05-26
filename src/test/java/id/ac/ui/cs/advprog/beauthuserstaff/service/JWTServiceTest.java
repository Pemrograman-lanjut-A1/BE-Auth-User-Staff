package id.ac.ui.cs.advprog.beauthuserstaff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTserviceimpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @InjectMocks
    private JWTserviceimpl jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private String secretKey = "6o0fY3XZm6vcwmuOalTRZvMZmJ31DO2NyOSjJoj4XRwz7uGI8FAQ5kELHS+pmAD+i9idb7Sg8uigefSVAfwBXA==";
    private String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", encodedSecretKey);
        jwtService.init();
    }

    @Test
    void testGenerateToken() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUserId(String.valueOf(1L));
        user.setType(UserType.REGULAR);

        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        CompletableFuture<String> tokenFuture = jwtService.generateToken(userDetails);
        String token = tokenFuture.get();

        assertNotNull(token);
    }

    @Test
    void testGenerateRefreshToken() throws Exception {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("extra", "claim");

        when(userDetails.getUsername()).thenReturn("test@example.com");

        CompletableFuture<String> tokenFuture = jwtService.generateRefreshToken(extraClaims, userDetails);
        String token = tokenFuture.get();

        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = Jwts.builder().setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);
    }

    @Test
    void testIsTokenValid() {
        String token = Jwts.builder().setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        when(userDetails.getUsername()).thenReturn("test@example.com");

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testResolveToken() {
        String bearerToken = "Bearer test-token";
        String token = jwtService.resolveToken(bearerToken);
        assertEquals("test-token", token);
    }

    @Test
    void testValidateClaims() {
        Claims claims = new DefaultClaims();
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24));

        boolean isValid = jwtService.validateClaims(claims);
        assertTrue(isValid);
    }
}
