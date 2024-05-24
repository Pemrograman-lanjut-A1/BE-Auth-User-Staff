package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.model.Announcement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class AnnouncementRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Announcement addAnnouncement(Announcement announcement){
        entityManager.merge(announcement);
        return announcement;
    }

    @Transactional
    public Announcement getAnnouncement(String id){
        return entityManager.find(Announcement.class, id);
    }

    @Transactional
    public void deleteAnnouncement(String id) {
        try {
            entityManager.createQuery("DELETE FROM announcement a WHERE a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (EmptyResultDataAccessException e) {
        }
    }


    @Transactional
    public List<Announcement> getAllAnnouncements(){
        return entityManager.createQuery("SELECT a FROM announcement a ORDER BY a.creationTimestamp DESC", Announcement.class)
                .getResultList();
    }
}