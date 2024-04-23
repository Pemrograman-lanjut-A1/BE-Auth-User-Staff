package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController.AnnouncementController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementServiceImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    void testCreateAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
        announcementController.createAnnouncement( "{\"content\":\"hello\"}");
        List<Announcement> resultList = announcementService.getAllAnnouncements();
        assertEquals(1, resultList.size());

        Announcement resultAnnouncement = resultList.getFirst();
        assertEquals("hello", resultAnnouncement.getContent());
    }

    @Test
    void testDeleteAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
        announcementController.createAnnouncement("{\"content\":\"hello\"}");
        List<Announcement> tempList = announcementService.getAllAnnouncements();
        assertEquals(1, tempList.size());

        Announcement tempAnnouncement = tempList.getFirst();
        String tempId = tempAnnouncement.getId();

        announcementController.deleteAnnouncement(tempId);
        List<Announcement> result = announcementService.getAllAnnouncements();
        assertTrue(result.isEmpty());
    }


    @Test
    void testGetAllAnnouncements() throws JsonProcessingException, JSONException, org.springframework.boot.configurationprocessor.json.JSONException {
        announcementController.createAnnouncement("{\"content\":\"hello\"}");
        announcementController.createAnnouncement("{\"content\":\"world\"}");
        String announcements = announcementController.getAllAnnouncements();
        JSONArray jsonArray = new JSONArray(announcements);
        JSONObject announcementJson1 = jsonArray.getJSONObject(0);
        JSONObject announcementJson2 = jsonArray.getJSONObject(1);
        assertEquals("hello", announcementJson1.getString("content"));
        assertEquals("world", announcementJson2.getString("content"));
    }
}
