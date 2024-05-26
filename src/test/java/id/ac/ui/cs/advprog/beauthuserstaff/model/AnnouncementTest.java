package id.ac.ui.cs.advprog.beauthuserstaff.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementTest {
    private Announcement announcement;

    @BeforeEach
    void setUp(){
        this.announcement = new AnnouncementBuilder().id("eb558e9f-1c39-460e-8860-71af6af63bd6").content(
                                          "Selamat pagi").tag("TagTest").title("TitleTest").build();
    }

    @Test
    void createAnnouncementNoContent(){
        AnnouncementBuilder announcementBuilder = new AnnouncementBuilder();
        assertThrows(IllegalArgumentException.class, announcementBuilder::build);
    }

    @Test
    void createAnnouncementDefault(){
        Announcement announcement1 = new Announcement();
        assertNull(announcement1.getId());
        assertNull(announcement1.getContent());
        assertNull(announcement1.getCreationTimestamp());
        assertNull(announcement1.getTag());
        assertNull(announcement1.getTitle());
    }

    @Test
    void testCreateTimeStamp(){
        assertNotNull(announcement.getCreationTimestamp());
    }

    @Test
    void testGetId(){
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", this.announcement.getId());
    }

    @Test
    void testGetContent(){
        assertEquals("Selamat pagi", this.announcement.getContent());
    }

    @Test
    void testGetTag(){
        assertEquals("TagTest", this.announcement.getTag());
    }

}

