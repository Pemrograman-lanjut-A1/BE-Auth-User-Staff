package id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.ConfirmPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class ConfirmPaymentController {

    @Autowired
    ConfirmPaymentService confirmPaymentService;

    @GetMapping("/get-all-waiting-payments")
    public String getAllWaitingPayments(){
        return confirmPaymentService.getAllWaitingPayments();
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(){
        return confirmPaymentService.confirmPayment();
    }
}
