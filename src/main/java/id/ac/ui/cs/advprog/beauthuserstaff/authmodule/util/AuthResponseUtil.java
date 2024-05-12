package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.JWTservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AuthResponseUtil {
    public static ResponseEntity<Object> generateUserLoginResponse(User user, JWTservice jwTservice)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        if(user == null){
            return ResponseHandler.generateResponse("Maaf username atau password tidak sesuai",
                    HttpStatus.UNAUTHORIZED, new HashMap<>());
        }
        Map<String, Object> userData = new ObjectMapper().convertValue(user, Map.class);
        userData = removeUnusedResponse(userData);

        if (user.getType().toString().equals(UserType.REGULAR.name())){
            Map<String, Object> data = new HashMap<>();
            data.put("user", userData);
            data.put("userToken", jwTservice.generateToken(user).get());

            return ResponseHandler.generateResponse(
                    "Login berhasil", HttpStatus.ACCEPTED, data);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("Staff", userData);
        data.put("StaffToken", jwTservice.generateToken(user).get());

        return ResponseHandler.generateResponse(
                "Login sebagai Staff berhasil", HttpStatus.ACCEPTED, data);
    }

    public static ResponseEntity<Object> generateUserSignUpResponse(User user, JWTservice jwTservice)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        Map<String, Object> userData = new ObjectMapper().convertValue(user, Map.class);
        userData = removeUnusedResponse(userData);

        if (user.getType().toString().equals(UserType.REGULAR.name())){
            Map<String, Object> data = new HashMap<>();
            data.put("user", userData);
            data.put("userToken", jwTservice.generateToken(user).get());

            return ResponseHandler.generateResponse(
                    "Sign Up berhasil", HttpStatus.CREATED, data);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("Staff", userData);
        data.put("StaffToken", jwTservice.generateToken(user).get());

        return ResponseHandler.generateResponse(
                "Sign Up sebagai Staff berhasil", HttpStatus.CREATED, data);
    }

    public static Map<String, Object> removeUnusedResponse(Map<String, Object> userData){
        userData.remove("enabled");
        userData.remove("accountNonExpired");
        userData.remove("accountNonLocked");
        userData.remove("credentialsNonExpired");
        userData.remove("authorities");
        userData.remove("userid");
        userData.remove("type");

        return userData;
    }
}
