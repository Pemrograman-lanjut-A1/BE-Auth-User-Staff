package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;

import java.util.List;

public interface AnnouncementService {
    public Announcement createAnnouncement(Announcement announcement);

    public void deleteAnnouncement(String id);

    public List<Announcement> getAllAnnouncements();
}
