package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.JwtAuthResponse;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.RefreshTokenRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignInRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto.SignUpRequest;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.AuthService;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util.ResponseHandler;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:8080", " http://34.142.244.77", "https://fe-repo-inky.vercel.app"})
public class AuthenticationController  {
    private AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public CompletableFuture<ResponseEntity<Object>> signUp(@RequestBody SignUpRequest signUpRequest) throws JsonProcessingException {
        return authService.signUp(signUpRequest);
    }

    @PostMapping("/signup/staff")
    public ResponseEntity<Object> signUpStaff(@RequestBody SignUpRequest signUpRequest) throws JsonProcessingException {
        return authService.signUpStaff(signUpRequest);
    }

    @PostMapping("/signin")
    public CompletableFuture<ResponseEntity<Object>> signIn(@RequestBody SignInRequest signInRequest) throws JsonProcessingException, ExecutionException, InterruptedException {
        return authService.signIn(signInRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        try {
            JwtAuthResponse response = authService.refreshToken(refreshTokenRequest);
            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        ResponseHandler.generateLogoutResponse(token, response);
        return ResponseHandler.generateResponse((String) response.get("message"),
                (HttpStatus) response.get("status"), response);
    }

}
