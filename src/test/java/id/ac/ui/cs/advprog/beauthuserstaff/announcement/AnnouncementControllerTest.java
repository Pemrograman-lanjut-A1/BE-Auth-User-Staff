package id.ac.ui.cs.advprog.beauthuserstaff.announcement;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard.AnnouncementController;
import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testCreateAnnouncementSuccess() throws org.springframework.boot.configurationprocessor.json.JSONException {

        when(jwtAuthFilter.filterToken("token")).thenReturn("STAFF");



        Announcement announcement = new Announcement("1", "{\"content\":\"hello\", \"tag\": \"TagTest\", \"title\": \"f\"  }",null, null);
        when(announcementService.createAnnouncement(any())).thenReturn(announcement);
        String res = announcementController.createAnnouncement( "token","{\"content\":\"hello\", \"tag\": \"TagTest\", \"title\": \"f\"  }");
        assertEquals("fdemo test", res);
        verify(announcementService, times(1)).createAnnouncement(any());
    }

    @Test
    void testCreateAnnouncementForbidden() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn(null);
        String result = announcementController.createAnnouncement( "token","{\"content\":\"hello\", \"tag\": \"TagTest\" }");
        assertEquals("You are not authorized to make this request",result);
    }

    @Test
    void testCreateAnnouncementWrongRole() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn("USER");
        String result = announcementController.createAnnouncement( "token","{\"content\":\"hello\", \"tag\": \"TagTest\" }");
        assertEquals("You are not authorized to make this request",result);
    }

    @Test
    void testDeleteAnnouncement() throws org.springframework.boot.configurationprocessor.json.JSONException {
        when(jwtAuthFilter.filterToken(anyString())).thenReturn("STAFF");
        announcementController.deleteAnnouncement("token","{\"id\":\"" + "1" + "\"}");
        verify(announcementService, times(1)).deleteAnnouncement(any());
    }

    @Test
    void testDeleteAnnouncementForbidden() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn(null);
        String result = announcementController.deleteAnnouncement("token","{\"id\":\"" + "1" + "\"}");
        assertEquals("You are not authorized to make this request", result);
    }

    @Test
    void testDeleteAnnouncementWrongRole() throws JSONException {
        when(jwtAuthFilter.filterToken("token")).thenReturn("USER");
        String result = announcementController.deleteAnnouncement("token","{\"id\":\"" + "1" + "\"}");
        assertEquals("You are not authorized to make this request", result);
    }


    @Test
    void testGetAllAnnouncements() throws JsonProcessingException{
        Announcement announcement1 = new AnnouncementBuilder().content("hello").build();
        Announcement announcement2 = new AnnouncementBuilder().content("hello").title("hello").build();
        Announcement announcement3 = new AnnouncementBuilder().content("hello").tag("hello").build();

        List<Announcement> announcementList = new ArrayList<>();

        announcementList.add(announcement1);
        announcementList.add(announcement2);
        announcementList.add(announcement3);

        when(announcementService.getAllAnnouncements()).thenReturn(announcementList);

        announcementController.getAllAnnouncements();
        verify(announcementService, times(1)).getAllAnnouncements();
    }

}
