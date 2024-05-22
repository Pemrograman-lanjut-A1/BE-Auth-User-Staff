package id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    private static final String AUTHENTICATED_KEY = "authenticated";
    private static final String NOT_AUTHENTICATED_KEY = "not authenticated";
    private static final String SUCCESS_MESSAGE = "Request Successful";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to make this request";


    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(@RequestHeader(value = "Authorization") String token, @RequestBody String jsonContent) throws JSONException {
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)){
            return FORBIDDEN_MESSAGE;
        }

        JSONObject jsonObject = new JSONObject(jsonContent);
        String content = jsonObject.getString("content");
        String tag = jsonObject.getString("tag");
        Announcement newAnnouncement = new AnnouncementBuilder().content(content).tag(tag).build();
        announcementService.createAnnouncement(newAnnouncement);
        return SUCCESS_MESSAGE;
    }

    @PostMapping("/delete-announcement")
    public String deleteAnnouncement(@RequestHeader(value = "Authorization") String token, @RequestBody String jsonId) throws JSONException{
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)){
            return FORBIDDEN_MESSAGE;
        }

        JSONObject jsonObject = new JSONObject(jsonId);
        String id = jsonObject.getString("id");
        announcementService.deleteAnnouncement(id);
        return SUCCESS_MESSAGE;
    }

    @GetMapping("/get-all-announcements")
    public String getAllAnnouncements(@RequestHeader(value = "Authorization") String token) throws JsonProcessingException{
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)){
            return FORBIDDEN_MESSAGE;
        }
        List<Announcement> announcementList = announcementService.getAllAnnouncements();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(announcementList);
    }

    private String authenticate(String token){
        String role = null;

        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (Exception ignored){}


        if (role == null || !(role.equals("STAFF"))){
            return NOT_AUTHENTICATED_KEY;
        }
        return AUTHENTICATED_KEY;
    }
}
