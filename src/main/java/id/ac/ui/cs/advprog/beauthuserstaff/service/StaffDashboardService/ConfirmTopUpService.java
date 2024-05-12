package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public interface ConfirmTopUpService {
    public String getAllWaitingTopUps();

    public String confirmTopUp();
}