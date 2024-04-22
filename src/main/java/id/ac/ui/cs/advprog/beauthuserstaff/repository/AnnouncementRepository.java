package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class AnnouncementRepository {
    private List<Announcement> announcements = new ArrayList<>();


    public Announcement addAnnouncement(Announcement announcement){
        announcements.add(announcement);
        return announcement;
    }

    public Announcement getAnnouncement(String id){
        for (Announcement savedAnnouncement : announcements){
            if (savedAnnouncement.getId().equals(id)){
                return savedAnnouncement;
            }
        }
        throw new IllegalArgumentException();
    }

    public void deleteAnnouncement(String id){
        try{
            announcements.removeIf(announcement -> announcement.getId().equals(id));
        } catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    public Iterator<Announcement> getAllAnnouncements(){
        return announcements.iterator();
    }
}
