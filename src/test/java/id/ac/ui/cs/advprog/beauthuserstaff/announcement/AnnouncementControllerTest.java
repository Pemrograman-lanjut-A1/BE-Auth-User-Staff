package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard.AnnouncementController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AnnouncementControllerTest {

    @InjectMocks
    AnnouncementController announcementController;

    @Mock
    AnnouncementService announcementService;

    @Mock
    JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp(){
        announcementController = new AnnouncementController();

        ReflectionTestUtils.setField(announcementController, "announcementService", announcementService);
        ReflectionTestUtils.setField(announcementController, "jwtAuthFilter", jwtAuthFilter);
    }

    @Test
    void testCreateAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {

        when(jwtAuthFilter.filterToken("token")).thenReturn("STAFF");



        Announcement announcement = new Announcement("1", "{\"content\":\"hello\", \"tag\": \"TagTest\" }",null);
        when(announcementService.createAnnouncement(any())).thenReturn(announcement);
        announcementController.createAnnouncement( "token","{\"content\":\"hello\", \"tag\": \"TagTest\" }");
        verify(announcementService, times(1)).createAnnouncement(any());
    }

    @Test
    void testDeleteAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("STAFF");
        announcementController.deleteAnnouncement("token","{\"id\":\"" + "1" + "\"}");
        verify(announcementService, times(1)).deleteAnnouncement(any());
    }


    @Test
    void testGetAllAnnouncements() throws JsonProcessingException{
        Announcement announcement1 = new Announcement();
        Announcement announcement2 = new Announcement();

        List<Announcement> announcementList = new ArrayList<>();

        announcementList.add(announcement1);
        announcementList.add(announcement2);

        when(announcementService.getAllAnnouncements()).thenReturn(announcementList);
        when(jwtAuthFilter.filterToken("token")).thenReturn("STAFF");

        announcementController.getAllAnnouncements("token");
        verify(jwtAuthFilter, times(1)).filterToken("token");
        verify(announcementService, times(1)).getAllAnnouncements();
    }
}
