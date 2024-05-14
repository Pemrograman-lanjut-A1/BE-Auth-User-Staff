package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    private static final String MESSAGE_KEY = "message";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something Wrong With Server";
    private static final String EXPIRED_JWT_MESSAGE = "JWT token has expired";
    private static final String INVALID_JWT_MESSAGE = "Invalid JWT token";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to make this request";
    private static final String ERROR_KEY_MESSAGE = "Error";
    private static final String TOP_UP_ID_MESSAGE = "Top-up with ID ";
    private static final String NOT_FOUND_MESSAGE = " not found.";

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(@RequestHeader(value = "Authorization") String token, @RequestBody String jsonContent) throws JSONException {
        Map<String, Object> response = new HashMap<>();
        String role = null;

        System.out.println("Sebelum try");

        try {
            System.out.println("Setelah try");
            role = jwtAuthFilter.filterToken(token);
        }catch (Exception e){
            handleJwtException(e);
        }


        System.out.println((role));
        if (role == null || !(role.equals("STAFF"))){
            return "403";
        }

        System.out.println(jsonContent);
        JSONObject jsonObject = new JSONObject(jsonContent);
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        Announcement newAnnouncement = new AnnouncementBuilder().content(content).tag(tag).build();
        announcementService.createAnnouncement(newAnnouncement);
        return "200";
    }

    @PostMapping("/delete-announcement")
    public String deleteAnnouncement(@RequestBody String jsonId) throws JSONException{
        System.out.println(jsonId);
        JSONObject jsonObject = new JSONObject(jsonId);
        String id = jsonObject.getString("id");
        announcementService.deleteAnnouncement(id);
        return "200";
    }

    @GetMapping("/get-all-announcements")
    public String getAllAnnouncements() throws JsonProcessingException, JSONException {
        List<Announcement> announcementList = announcementService.getAllAnnouncements();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(announcementList);
    }


    private Map<String, Object> handleJwtException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.FORBIDDEN.value());
        response.put(MESSAGE_KEY, e instanceof ExpiredJwtException ? EXPIRED_JWT_MESSAGE : INVALID_JWT_MESSAGE);
        return response;
    }
}
