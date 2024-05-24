package id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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

            System.out.println("masuk sini");
            return FORBIDDEN_MESSAGE;
        }

        JSONObject jsonObject = new JSONObject(jsonContent);
        AnnouncementBuilder announcementBuilder = new AnnouncementBuilder();
        String content = jsonObject.getString("content");
        announcementBuilder.content(content);
        if (jsonObject.has("tag")) {
            announcementBuilder.tag(jsonObject.getString("tag"));
        }
        if (jsonObject.has("title")) {
            announcementBuilder.title(jsonObject.getString("title"));
        }
        Announcement newAnnouncement = announcementBuilder.build();
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
    public String getAllAnnouncements(@RequestHeader(value = "Authorization") String token) throws JsonProcessingException {
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)) {
            return FORBIDDEN_MESSAGE;
        }
        List<Announcement> announcementList = announcementService.getAllAnnouncements();

        // Configure the ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

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
