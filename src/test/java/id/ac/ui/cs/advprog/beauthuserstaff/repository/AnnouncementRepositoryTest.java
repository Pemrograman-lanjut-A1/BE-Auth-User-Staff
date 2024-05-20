package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import id.ac.ui.cs.advprog.beauthuserstaff.model.AnnouncementBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AnnouncementRepositoryTest {

    @InjectMocks
    AnnouncementRepository announcementRepository;

    @Mock
    EntityManager entityManager;


    List<Announcement> announcements;
    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);
        announcementRepository = new AnnouncementRepository();
        announcements = new ArrayList<>();
        

        ReflectionTestUtils.setField(announcementRepository, "entityManager", entityManager);

        Announcement announcement1 = new AnnouncementBuilder().id("id-1").content("Selamat pagi").build();
        Announcement announcement2 = new AnnouncementBuilder().id("id-2").content("Selamat pagi").build();

        announcements.add(announcement1);
        announcements.add(announcement2);
    }

    @Test
    void testAddAndGetAnnouncement(){
        Announcement announcement = announcements.getFirst();
        when(entityManager.find(eq(Announcement.class), any())).thenReturn(announcement);
        Announcement result = announcementRepository.addAnnouncement(announcement);

        Announcement findResult = announcementRepository.getAnnouncement(announcements.get(0).getId());
        assertEquals(announcement.getId(), result.getId());
        assertEquals(announcement.getContent(), result.getContent());
        assertEquals(announcement.getId(), findResult.getId());
        assertEquals(announcement.getContent(), findResult.getContent());
        verify(entityManager, times(1)).find(eq(Announcement.class), any());
    }

    @Test
    void testGetAnnouncementIfInvalidId(){
        when(entityManager.find(eq(Announcement.class), any())).thenReturn(null);
        assertNull(announcementRepository.getAnnouncement("123"));
    }

    @Test
    void testDeleteAnnouncement(){
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        doReturn(query).when(query).setParameter(anyString(), any());
        when(query.executeUpdate()).thenReturn(1);

        announcementRepository.deleteAnnouncement("123");

        verify(entityManager, Mockito.times(1)).createQuery(anyString());
        verify(query, Mockito.times(1)).setParameter(anyString(), any());
        verify(query, Mockito.times(1)).executeUpdate();
//        Announcement announcement = announcements.get(0);
//        announcementRepository.addAnnouncement(announcement);
//        announcementRepository.deleteAnnouncement(announcement.getId());
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            Announcement foundAnnouncement = announcementRepository.getAnnouncement(announcement.getId());
//        });
    }


    @Test
    void testGetAllAnnouncementsIfEmpty(){
        List<Announcement> expectedAnnouncements = Collections.emptyList();
        TypedQuery<Announcement> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Announcement.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedAnnouncements);

        List<Announcement> foundTopUps = announcementRepository.getAllAnnouncements();

        assertEquals(expectedAnnouncements, foundTopUps);
        verify(entityManager, Mockito.times(1)).createQuery(anyString(), eq(Announcement.class));
        verify(typedQuery, Mockito.times(1)).getResultList();
    }
}
