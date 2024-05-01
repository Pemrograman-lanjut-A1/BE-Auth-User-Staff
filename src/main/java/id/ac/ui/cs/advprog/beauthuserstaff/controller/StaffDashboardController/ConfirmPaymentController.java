package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.ConfirmPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class ConfirmPaymentController {

    @Autowired
    ConfirmPaymentService confirmPaymentService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/get-all-waiting-payments")
    public String getAllWaitingPayments() {
        return confirmPaymentService.getAllWaitingPayments();
    }



}
