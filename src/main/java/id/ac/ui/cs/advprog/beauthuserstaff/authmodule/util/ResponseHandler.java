package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        if (status == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(map);
        } else {
            return ResponseEntity.status(status).body(map);
        }
    }

    public static void generateLogoutResponse(String token, Map<String, Object> response) {
        if (token == null) {
            response.put("message", "Token tidak ditemukan.");
            response.put("status", HttpStatus.BAD_REQUEST);
        } else {
            response.put("message", "Berhasil logout");
            response.put("status", HttpStatus.ACCEPTED);
        }
    }
}
