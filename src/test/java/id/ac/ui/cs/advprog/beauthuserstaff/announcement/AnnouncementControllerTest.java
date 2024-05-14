package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.controller.StaffDashboardController.AnnouncementController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementServiceImpl;
import jakarta.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class AnnouncementControllerTest {

    @InjectMocks
    AnnouncementController announcementController;

    @Mock
    AnnouncementService announcementService;

    @BeforeEach
    void setUp(){
        announcementController = new AnnouncementController();

        ReflectionTestUtils.setField(announcementController, "announcementService", announcementService);
    }

//    @Test
//    void testCreateAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
//        Announcement announcement = new Announcement("1", "{\"content\":\"hello\", \"tag\": \"TagTest\" }",null);
//        when(announcementService.createAnnouncement(any())).thenReturn(announcement);
//        announcementController.createAnnouncement( "{\"content\":\"hello\", \"tag\": \"TagTest\" }");
//        verify(announcementService, times(1)).createAnnouncement(any());
//    }

    @Test
    void testDeleteAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
        announcementController.deleteAnnouncement("{\"id\":\"" + "1" + "\"}");
        verify(announcementService, times(1)).deleteAnnouncement(any());
    }


    @Test
    void testGetAllAnnouncements() throws JsonProcessingException, JSONException, org.springframework.boot.configurationprocessor.json.JSONException {
        Announcement announcement1 = new Announcement();
        Announcement announcement2 = new Announcement();

        List<Announcement> announcementList = new ArrayList<>();

        announcementList.add(announcement1);
        announcementList.add(announcement2);

        when(announcementService.getAllAnnouncements()).thenReturn(announcementList);

        String result = announcementController.getAllAnnouncements();
        verify(announcementService, times(1)).getAllAnnouncements();
    }
}
