package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import io.jsonwebtoken.*;

@Primary
@Component
@Service
public class JWTserviceimpl implements JWTservice {

    @Value("${jwt.secret}")
    private String secretKey;
    private JwtParser jwtParser;
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Async
    public CompletableFuture<String> generateToken(UserDetails userDetails) {
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        User user = userOptional.get();
        String token = Jwts.builder().setSubject(user.getEmail())
                .claim("Id", user.getUserid())
                .claim("Role", user.getType().name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return CompletableFuture.completedFuture(token);
    }

    @Override
    @Async
    public CompletableFuture<String> generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String refreshToken = Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        return CompletableFuture.completedFuture(refreshToken);
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimResolvers){
        final Claims claims = extractAllClaims(token);
        return claimResolvers.apply(claims);
    }
    private Key getSignInKey(){
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}