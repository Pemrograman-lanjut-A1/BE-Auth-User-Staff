package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private static final String MESSAGE_KEY = "message";
    private static final String STATUS_KEY = "status";
    private ResponseHandler() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE_KEY, message);
        map.put(STATUS_KEY, status.value());
        map.put("data", responseObj);

        if (status == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(map);
        } else {
            return ResponseEntity.status(status).body(map);
        }
    }

    public static void generateLogoutResponse(String token, Map<String, Object> response) {
        if (token == null) {
            response.put(MESSAGE_KEY, "Token tidak ditemukan.");
            response.put(STATUS_KEY, HttpStatus.BAD_REQUEST);
        } else {
            response.put(MESSAGE_KEY, "Berhasil logout");
            response.put(STATUS_KEY, HttpStatus.ACCEPTED);
        }
    }
}
