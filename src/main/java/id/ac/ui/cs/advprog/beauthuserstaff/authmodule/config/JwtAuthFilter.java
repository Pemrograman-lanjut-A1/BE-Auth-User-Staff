package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTserviceimpl;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWTserviceimpl jwtService;
    private final UserService userService;
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(StringUtils.isEmpty(authHeader)
                || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);


        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);

            }
        }

        filterChain.doFilter(request, response);
    }

    public String filterToken(String token) {
        String accessToken = jwtService.resolveToken(token);

        if (accessToken == null) {
            return null;
        }

        Claims claims = jwtService.resolveClaims(token);

        if (claims != null && jwtService.validateClaims(claims)) {
            return claims.get("Role").toString();
        }
        return null;
    }
}
