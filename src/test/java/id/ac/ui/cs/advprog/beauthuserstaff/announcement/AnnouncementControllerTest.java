package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController.AnnouncementController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

public class AnnouncementControllerTest {
    AnnouncementController announcementController;
    AnnouncementService announcementService;
    AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp(){
        announcementController = new AnnouncementController();
        announcementService = new AnnouncementServiceImpl();
        announcementRepository = new AnnouncementRepository();

        ReflectionTestUtils.setField(announcementService, "announcementRepository", announcementRepository);
        ReflectionTestUtils.setField(announcementController, "announcementService", announcementService);
    }

    @Test
    void testCreateAnnouncement(){
        announcementController.createAnnouncement("id-1", "hello");
        Announcement result = announcementService.getAllAnnouncements().getFirst();
        assertEquals(result.getId(), "id-1");
        assertEquals(result.getContent(), "hello");
    }

    @Test
    void testDeleteAnnouncement(){
        announcementController.createAnnouncement("id-1", "hello");
        List<Announcement> temp = announcementService.getAllAnnouncements();
        assertEquals(temp.size(), 1);

        announcementController.deleteAnnouncement("id-1");
        List<Announcement> result = announcementService.getAllAnnouncements();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAnnouncements(){
        announcementController.getAllAnnouncements();
        verify(announcementService, times(1)).getAllAnnouncements();
    }
}
