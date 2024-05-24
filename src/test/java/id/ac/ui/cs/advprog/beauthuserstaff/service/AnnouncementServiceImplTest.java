package id.ac.ui.cs.advprog.beauthuserstaff.service;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import id.ac.ui.cs.advprog.beauthuserstaff.repository.AnnouncementRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.AnnouncementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AnnouncementServiceImplTest {


    @InjectMocks
    AnnouncementServiceImpl announcementService;


    List<Announcement> announcements;

    @Mock
    AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        announcements = new ArrayList<>();
        announcementService = new AnnouncementServiceImpl();

        ReflectionTestUtils.setField(announcementService, "announcementRepository", announcementRepository);

        Announcement announcement1 = new AnnouncementBuilder().id("id-1").content("Selamat pagi").build();
        announcements.add(announcement1);
        Announcement announcement2 = new AnnouncementBuilder().id("id-2").content("Selamat pagi").build();
        announcements.add(announcement2);
    }

    @Test
    void testCreateAnnouncement(){


        Announcement announcement = announcements.get(1);


        when(announcementRepository.addAnnouncement(any(Announcement.class))).thenReturn(announcement);

        Announcement createdAnnouncement = announcementService.createAnnouncement(announcement);

        assertNotNull(createdAnnouncement);
        assertEquals(announcement.getId(), createdAnnouncement.getId());
        assertEquals(announcement.getContent(), createdAnnouncement.getContent());
    }

    @Test
    void testDeleteAnnouncement(){
        Announcement announcement = announcements.get(1);
        String id = announcement.getId();
        announcementService.deleteAnnouncement(id);

        verify(announcementRepository, times(1)).deleteAnnouncement(id);
    }

    @Test
    void testGetAllAnnouncements() {
        List<Announcement> expectedAnnouncements = new ArrayList<>();
        expectedAnnouncements.add(new AnnouncementBuilder().content("hello").build());
        expectedAnnouncements.add(new AnnouncementBuilder().content("hello").build());
        expectedAnnouncements.add(new AnnouncementBuilder().content("hello").build());
        when(announcementRepository.getAllAnnouncements()).thenReturn(expectedAnnouncements);

        List<Announcement> foundAnnouncements = announcementService.getAllAnnouncements();

        assertNotNull(foundAnnouncements);
        assertEquals(expectedAnnouncements.size(), foundAnnouncements.size());
        assertEquals(expectedAnnouncements, foundAnnouncements);
    }
}
