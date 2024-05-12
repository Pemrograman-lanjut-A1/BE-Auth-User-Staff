package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public interface JWTservice {

    CompletableFuture<String> generateToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);

    CompletableFuture<String> generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

}
