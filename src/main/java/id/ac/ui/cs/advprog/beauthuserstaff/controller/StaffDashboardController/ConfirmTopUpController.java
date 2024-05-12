package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.ConfirmTopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class ConfirmTopUpController {

    @Autowired
    ConfirmTopUpService confirmTopUpService;

    @GetMapping("/get-all-waiting-top-ups")
    public String getAllWaitingTopUps(){
        return confirmTopUpService.getAllWaitingTopUps();
    }

    @PostMapping("/confirm-top-up")
    public String confirmTopUp(){
        return confirmTopUpService.confirmTopUp();
    }
}
