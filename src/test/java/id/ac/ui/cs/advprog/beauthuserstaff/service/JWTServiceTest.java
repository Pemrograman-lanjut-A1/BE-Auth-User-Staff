package id.ac.ui.cs.advprog.beauthuserstaff.service;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.AuthService;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.AuthServiceImpl;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTservice;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTserviceimpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @InjectMocks
    private JWTserviceimpl jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthServiceImpl authService;


    @Value("${jwt.secret}")
    private String secretKey = "6o0fY3XZm6vcwmuOalTRZvMZmJ31DO2NyOSjJoj4XRwz7uGI8FAQ5kELHS+pmAD+i9idb7Sg8uigefSVAfwBXA==";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateTokenSuccess() throws ExecutionException, InterruptedException {

        String userId = "1";
        String userEmail = "test@exam.com";
        String userType = "REGULAR";
        String username = "test";
        String password = "password";

        UserDetails userDetails = new User(username, password, new ArrayList<>());

        id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User user = new id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User();
        user.setEmail(userEmail);
        user.setUserid(userId);
        user.setType(UserType.valueOf(userType));

        when(userRepository.findUserByUsername(userDetails.getUsername())).thenReturn(Optional.of(user));

        CompletableFuture<String> tokenFuture = jwtService.generateToken(userDetails);

        String token = tokenFuture.get();

        assertNotNull(token);
    }

    @Test
    void generateTokenUserNotFound() throws ExecutionException, InterruptedException {
        String username = "test";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        CompletableFuture<String> tokenFuture = jwtService.generateToken(userDetails);

        assertNull(tokenFuture.get());
    }

    @Test
    void generateRefreshTokenSuccess() throws ExecutionException, InterruptedException {
        String username = "test";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());
        Map<String, Object> extraClaims = new HashMap<>();

        CompletableFuture<String> refreshTokenFuture = jwtService.generateRefreshToken(extraClaims, userDetails);

        String refreshToken = refreshTokenFuture.get();

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

}

