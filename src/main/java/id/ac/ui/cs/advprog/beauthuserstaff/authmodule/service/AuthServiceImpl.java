package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util.AuthResponseUtil;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util.ResponseHandler;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTservice jwtService;

    @Override
    @Async
    public CompletableFuture<ResponseEntity<Object>> signUp(SignUpRequest signUpRequest) {
        try {
            if (!isPasswordValid(signUpRequest.getPassword())) {
                return CompletableFuture.completedFuture(generateSignUpError());
            }
            String id = String.valueOf(UUID.randomUUID());
            User user = User.builder()
                    .email(signUpRequest.getEmail())
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .type(UserType.REGULAR)
                    .build();
            user.setUserid(id);
            userRepository.save(user);

            return CompletableFuture.completedFuture(generateUserSignUpResponse(user));
        } catch (DataIntegrityViolationException e) {
            return CompletableFuture.completedFuture(ResponseHandler.generateResponse(
                    "Email is already in used",
                    HttpStatus.BAD_REQUEST, null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(ResponseHandler.generateResponse(
                    "Failed to register user",
                    HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<Object>> signIn(SignInRequest signInRequest) throws JsonProcessingException, ExecutionException, InterruptedException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getEmail(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return CompletableFuture.completedFuture(generateSignInError());
        }
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElse(null);
        if (user == null) {
            return CompletableFuture.completedFuture(generateSignInError());
        }
        return CompletableFuture.completedFuture(generateUserLoginResponse(user));
    }

    public ResponseEntity<Object> signUpStaff(SignUpRequest signUpRequest) throws JsonProcessingException {
        try {
            if (!isPasswordValid(signUpRequest.getPassword())) {
                return generateSignUpError();
            }
            String id = String.valueOf(UUID.randomUUID());
            User user = User.builder()
                    .email(signUpRequest.getEmail())
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .type(UserType.STAFF)
                    .build();

            user.setUserid(id);
            userRepository.save(user);
            return generateUserSignUpResponse(user);
        }catch (DataIntegrityViolationException e) {
            return ResponseHandler.generateResponse(
                    "Email is already in used",
                    HttpStatus.BAD_REQUEST, null);
        }catch (Exception e) {
            return ResponseHandler.generateResponse(
                    "Failed to register user",
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ResponseEntity<Object> generateUserLoginResponse(User user) throws JsonProcessingException, ExecutionException, InterruptedException {
        return AuthResponseUtil.generateUserLoginResponse(user, jwtService);
    }

    public ResponseEntity<Object> generateUserSignUpResponse(User user) throws JsonProcessingException, ExecutionException, InterruptedException {
        return AuthResponseUtil.generateUserSignUpResponse(user, jwtService);
    }
    public ResponseEntity<Object> generateSignInError()throws JsonProcessingException{
        return ResponseHandler.generateResponse(
                "Invalid email or password",
                HttpStatus.UNAUTHORIZED, null);
    }
    public ResponseEntity<Object> generateSignUpError()throws JsonProcessingException{
        return ResponseHandler.generateResponse(
                "Password must be at least 8 characters long, " +
                        "contain at least one uppercase letter, one digit, and one special symbol",
                HttpStatus.BAD_REQUEST, null);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!*()%^&+=])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            String email = jwtService.extractUsername(refreshTokenRequest.getToken());

            if (email == null) {
                return null;
            }

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return null;
            }

            User user = userOptional.get();

            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
                CompletableFuture<String> jwtFuture = jwtService.generateToken(user);
                String jwt = jwtFuture.join();

                JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                jwtAuthResponse.setToken(jwt);
                jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());

                return jwtAuthResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

