package id.ac.ui.cs.advprog.beauthuserstaff.util;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTservice;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util.AuthResponseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthResponseUtilTest {

    @Test
    void generateUserLoginResponse_RegularUser_ShouldReturnAcceptedResponse() throws ExecutionException, InterruptedException {
        User user = new User();
        user.setType(UserType.REGULAR);
        JWTservice jwtService = mock(JWTservice.class);
        CompletableFuture<String> futureToken = CompletableFuture.completedFuture("sample_token");
        when(jwtService.generateToken(user)).thenReturn(futureToken);

        ResponseEntity<Object> response = AuthResponseUtil.generateUserLoginResponse(user, jwtService);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Map<String, Object> responseData = (Map<String, Object>) response.getBody();
        assertEquals("Login berhasil", responseData.get("message"));
        assertEquals("sample_token", ((Map<String, Object>) responseData.get("data")).get("userToken"));

    }

    @Test
    void generateUserSignUpResponse_StaffUser_ShouldReturnCreatedResponse() throws ExecutionException, InterruptedException {
        User user = new User();
        user.setType(UserType.STAFF);
        JWTservice jwtService = mock(JWTservice.class);
        CompletableFuture<String> futureToken = CompletableFuture.completedFuture("sample_token");
        when(jwtService.generateToken(user)).thenReturn(futureToken);

        ResponseEntity<Object> response = AuthResponseUtil.generateUserSignUpResponse(user, jwtService);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseData = (Map<String, Object>) response.getBody();
        System.out.println(responseData);
        assertEquals("Sign Up sebagai Staff berhasil", responseData.get("message"));
        assertEquals("sample_token", ((Map<String, Object>) responseData.get("data")).get("StaffToken"));
    }

    @Test
    void removeUnusedResponse_ShouldRemoveUnusedFields() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John Doe");
        userData.put("enabled", true);
        userData.put("accountNonExpired", true);
        userData.put("accountNonLocked", true);
        userData.put("credentialsNonExpired", true);
        userData.put("authorities", "ROLE_USER");
        userData.put("userid", 123);
        userData.put("type", UserType.REGULAR);

        Map<String, Object> cleanedData = AuthResponseUtil.removeUnusedResponse(userData);

        assertEquals(1, cleanedData.size());
        assertEquals("John Doe", cleanedData.get("name"));
    }


    @Test
    void generateUserLoginResponse_NullUser_ShouldReturnUnauthorizedResponse() throws ExecutionException, InterruptedException {
        User user = null;
        JWTservice jwtService = mock(JWTservice.class);

        ResponseEntity<Object> response = AuthResponseUtil.generateUserLoginResponse(user, jwtService);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseData = (Map<String, Object>) response.getBody();
        assertEquals("Maaf username atau password tidak sesuai", responseData.get("message"));
    }

}

