package id.ac.ui.cs.advprog.beauthuserstaff.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnouncementTest {
    private Announcement announcement;

    @BeforeEach
    void setUp(){
        this.announcement = new AnnouncementBuilder().id("eb558e9f-1c39-460e-8860-71af6af63bd6").content(
                                          "Selamat pagi").tag("TagTest").build();
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

