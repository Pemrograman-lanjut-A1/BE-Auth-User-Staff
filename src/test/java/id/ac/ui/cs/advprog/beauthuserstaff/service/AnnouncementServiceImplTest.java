package id.ac.ui.cs.advprog.beauthuserstaff.service;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AnnouncementServiceImplTest {


    AnnouncementServiceImpl announcementService;
    List<Announcement> announcements;

    AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp(){
        announcements = new ArrayList<>();
        announcementRepository = new AnnouncementRepository();
        announcementService = new AnnouncementServiceImpl();

        ReflectionTestUtils.setField(announcementService, "announcementRepository", announcementRepository);

        Announcement announcement1 = new Announcement("id-1", "Selamat pagi");
        announcements.add(announcement1);
        Announcement announcement2 = new Announcement("id-2", "Selamat siang");
        announcements.add(announcement2);
    }

    @Test
    void testCreateAnnouncement(){
        Announcement announcement = announcements.get(1);
        System.out.println(announcementRepository);
        announcementService.createAnnouncement(announcement);

        Iterator<Announcement> announcementIterator = announcementRepository.getAllAnnouncements();
        assertTrue(announcementIterator.hasNext());
        Announcement savedAnnouncement = announcementIterator.next();
        assertEquals(announcement.getId(), savedAnnouncement.getId());
        assertEquals(announcement.getContent(), savedAnnouncement.getContent());
    }

    @Test
    void testDeleteAnnouncement(){
        Announcement announcement = announcements.get(1);
        announcementService.createAnnouncement(announcement);
        announcementService.deleteAnnouncement(announcement.getId());

        Iterator<Announcement> announcementIterator = announcementRepository.getAllAnnouncements();
        assertFalse(announcementIterator.hasNext());
    }

    @Test
    void testGetAllAnnouncements() {
        for (Announcement announcement : announcements) {
            announcementService.createAnnouncement(announcement);
        }
        List<Announcement> announcementsResult = announcementService.getAllAnnouncements();

        for (Announcement announcement : announcements) {
            assertTrue(announcementsResult.contains(announcement));
        }
    }






}
