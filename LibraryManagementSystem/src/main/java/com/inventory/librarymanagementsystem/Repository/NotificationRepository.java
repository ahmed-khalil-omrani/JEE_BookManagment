package com.inventory.librarymanagementsystem.Repository;

import com.inventory.librarymanagementsystem.entities.Notification;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Stateless
public class NotificationRepository {
    @PersistenceContext(unitName = "LibraryPU")
    private EntityManager em;
    public Notification save(Notification notification){
        if (notification.getId() == null) {
            em.persist(notification);
        } else {
            notification = em.merge(notification);
        }
        return notification;
    }
    public Optional<Notification> findById(Long id) {
        return Optional.of(em.createQuery("SELECT n FROM Notification n WHERE id=:id", Notification.class)
                .setParameter("id",id)
                .getSingleResult());
    }

    public List<Notification> findByUserId(Long userId) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdDate DESC", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
