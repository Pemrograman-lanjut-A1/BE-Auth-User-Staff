package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import org.springframework.stereotype.Service;

@Service
public class ConfirmTopUpServiceImpl implements ConfirmTopUpService{
    @Override
    public String getAllWaitingTopUps() {
        return "get all waiting top ups";
    }

    @Override
    public String confirmTopUp(){
        return "confirm top up";
    }
}
