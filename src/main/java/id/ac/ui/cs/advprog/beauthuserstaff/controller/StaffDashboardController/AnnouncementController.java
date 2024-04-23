package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(@RequestBody String announcementMessage) throws JsonProcessingException, JSONException {
        //return announcementService.createAnnouncement();
        JSONObject json = new JSONObject(announcementMessage);
        String id = json.getString("id");
        String content = json.getString("content");
        Announcement newAnnouncement = new Announcement(id, content);
        announcementService.createAnnouncement(newAnnouncement);

        return "200";
    }

    @PostMapping("/delete-announcement")
    public String deleteAnnouncement(@RequestBody String idMessage) throws JSONException {
        JSONObject json = new JSONObject(idMessage);
        String id = json.getString("id");
        System.out.println(id);
        System.out.println("Masuk delete");
        announcementService.deleteAnnouncement(id);
        return null;
    }

    @GetMapping("/get-all-announcements")
    public String getAllAnnouncements() throws JsonProcessingException {
        List<Announcement> announcementList = announcementService.getAllAnnouncements();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(announcementList);
        return json;
    }


}

