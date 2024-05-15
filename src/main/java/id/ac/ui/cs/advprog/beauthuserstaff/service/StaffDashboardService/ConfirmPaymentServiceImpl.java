package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import org.springframework.stereotype.Service;

@Service
public class ConfirmPaymentServiceImpl implements ConfirmPaymentService{
    @Override
    public String getAllWaitingPayments() {
        return "get all waiting payments";
    }

    @Override
    public String confirmPayment() {
        return "confirm payment";
    }
}
