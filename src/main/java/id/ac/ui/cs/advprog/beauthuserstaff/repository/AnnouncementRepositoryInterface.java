package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;

import java.util.Iterator;

public interface AnnouncementRepositoryInterface {

    public Announcement addAnnouncement(Announcement announcement);

    public Announcement getAnnouncement(String id);

    public void deleteAnnouncement(String id);

    public Iterator<Announcement> getAllAnnouncements();
}