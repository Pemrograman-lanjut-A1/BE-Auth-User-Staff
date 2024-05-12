package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    @Override
    public String createAnnouncement() {
        return "create announcement";
    }

    @Override
    public String deleteAnnouncement() {
        return "delete announcement";
    }

    @Override
    public String getAllAnnouncements() {
        return "get all announcements";
    }
}
