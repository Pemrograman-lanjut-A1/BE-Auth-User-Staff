package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{

    @Autowired
    private AnnouncementRepository announcementRepository;
    @Override
    public Announcement createAnnouncement(Announcement announcement) {
        announcementRepository.addAnnouncement(announcement);
        return announcement;
    }

    @Override
    public void deleteAnnouncement(String id) {
        announcementRepository.deleteAnnouncement(id);
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        Iterator<Announcement> announcementIterator = announcementRepository.getAllAnnouncements();
        List<Announcement> allAnnouncements = new ArrayList<>();
        announcementIterator.forEachRemaining(allAnnouncements::add);
        return allAnnouncements;
    }
}
