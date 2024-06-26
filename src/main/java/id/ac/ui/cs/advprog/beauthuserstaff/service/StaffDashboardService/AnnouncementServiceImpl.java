package id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    @Transactional
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.addAnnouncement(announcement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(String id) {
        announcementRepository.deleteAnnouncement(id);
    }

    @Override
    public List<Announcement> getAllAnnouncements() {

        List<Announcement> announcementList = announcementRepository.getAllAnnouncements();

        announcementList.sort(Comparator.comparing(Announcement::getCreationTimestamp).reversed());
        return announcementList;

    }
}