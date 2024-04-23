package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(@RequestBody String jsonContent) throws JSONException {
        System.out.println(jsonContent);
        JSONObject jsonObject = new JSONObject(jsonContent);
        String content = jsonObject.getString("content");

        Announcement newAnnouncement = new Announcement(null, content);
        announcementService.createAnnouncement(newAnnouncement);
        return "200";
    }

    @PostMapping("/delete-announcement")
    public String deleteAnnouncement(String id){
        announcementService.deleteAnnouncement(id);
        return "200";
    }

    @GetMapping("/get-all-announcements")
    public String getAllAnnouncements() throws JsonProcessingException, JSONException {
        List<Announcement> announcementList = announcementService.getAllAnnouncements();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(announcementList);
    }


}
