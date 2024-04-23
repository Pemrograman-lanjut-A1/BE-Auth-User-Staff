package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(String content){
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
    public List<Announcement> getAllAnnouncements(){
        return(announcementService.getAllAnnouncements());
    }


}
