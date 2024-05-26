package id.ac.ui.cs.advprog.beauthuserstaff.config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTserviceimpl;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JWTserviceimpl jwtService;

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Claims claims;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
        lenient().when(userService.userDetailsService()).thenReturn(userDetailsService);
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        String token = "Bearer validToken";
        String username = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername("validToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        String token = "Bearer invalidToken";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUsername("invalidToken")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
    @Test
    void testFilterTokenWithInvalidToken() {
        String token = "Bearer invalidToken";

        when(jwtService.resolveToken("Bearer invalidToken")).thenReturn(null);

        String role = jwtAuthFilter.filterToken(token);
        assertNull(role);
    }
}
