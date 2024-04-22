package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class AnnouncementRepositoryTest {
    AnnouncementRepository announcementRepository;

    List<Announcement> announcements;
    @BeforeEach
    void setUp(){
        announcementRepository = new AnnouncementRepository();

        announcements = new ArrayList<>();

        Announcement announcement1 = new Announcement("id-1", "Selamat pagi");
        Announcement announcement2 = new Announcement("id-2", "Selamat siang");

        announcements.add(announcement1);
        announcements.add(announcement2);
    }

    @Test
    void testAddAndGetAnnouncement(){
        Announcement announcement = announcements.get(0);
        Announcement result = announcementRepository.addAnnouncement(announcement);

        Announcement findResult = announcementRepository.getAnnouncement(announcements.get(0).getId());
        assertEquals(announcement.getId(), result.getId());
        assertEquals(announcement.getContent(), result.getContent());
        assertEquals(announcement.getId(), findResult.getId());
        assertEquals(announcement.getContent(), findResult.getContent());
    }

    @Test
    void testGetAnnouncementIfInvalidId(){
        Announcement announcement = announcements.get(0);
        Announcement result = announcementRepository.addAnnouncement(announcement);

        assertThrows(IllegalArgumentException.class, () -> {
            Announcement findResult = announcementRepository.getAnnouncement((announcements.get(1).getId()));
        });

    }

    @Test
    void testDeleteAnnouncement(){
        Announcement announcement = announcements.get(0);
        announcementRepository.addAnnouncement(announcement);
        announcementRepository.deleteAnnouncement(announcement.getId());
        Announcement foundAnnouncement = announcementRepository.getAnnouncement(announcement.getId());
        assertNull(foundAnnouncement);
    }

    @Test
    void testDeleteAnnouncementIfEmpty(){
        Announcement announcement = announcements.get(0);


        assertThrows(IllegalArgumentException.class, () -> {
            announcementRepository.deleteAnnouncement(announcement.getId());
        });
    }

    @Test
    void testGetAllAnnouncementsIfEmpty(){
        Iterator<Announcement> announcementIterator = announcementRepository.getAnnouncements();
        assertFalse(announcementIterator.hasNext());
    }

    @Test
    void testGetAllAnnouncementsIfMoreThanOne(){
        int i = 0;
        for (Announcement announcement : announcements){
            announcementRepository.addAnnouncement(announcement);
            i += 1;
        }

        Iterator<Announcement> announcementIterator = announcementRepository.getAllAnnouncements();
        assertTrue(announcementIterator.hasNext());
        Announcement savedAnnouncement = announcementIterator.next();
        assertEquals(announcements.getFirst().getId(), savedAnnouncement.getId());
        savedAnnouncement = announcementIterator.next();
        assertEquals(announcements.get(1).getId(), savedAnnouncement.getId());
        assertFalse(announcementIterator.hasNext());
    }
}
