package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public interface AuthService {
    CompletableFuture<ResponseEntity<Object>> signUp(SignUpRequest signUpRequest) throws JsonProcessingException;
    CompletableFuture<ResponseEntity<Object>> signIn(SignInRequest signInRequest) throws JsonProcessingException, ExecutionException, InterruptedException;
    ResponseEntity<Object> signUpStaff(SignUpRequest signUpRequest) throws JsonProcessingException;
    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    ResponseEntity<Object> generateUserLoginResponse(User user) throws JsonProcessingException, ExecutionException, InterruptedException;
    ResponseEntity<Object> generateUserSignUpResponse(User user) throws JsonProcessingException, ExecutionException, InterruptedException;
    ResponseEntity<Object> generateSignInError()throws JsonProcessingException;
    ResponseEntity<Object> generateSignUpError()throws JsonProcessingException;

}
