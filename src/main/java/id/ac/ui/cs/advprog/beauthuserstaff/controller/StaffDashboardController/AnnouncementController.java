package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/create-announcement")
    public String createAnnouncement(){
        return announcementService.createAnnouncement();
    }

    @PostMapping("/delete-announcement")
    public String deleteAnnouncement(){
        return announcementService.deleteAnnouncement();
    }

    @GetMapping("/get-all-announcements")
    public String getAllAnnouncements(){
        return announcementService.getAllAnnouncements();
    }


}
