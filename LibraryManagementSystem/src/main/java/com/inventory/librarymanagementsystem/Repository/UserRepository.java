package com.inventory.librarymanagementsystem.Repository;

import com.inventory.librarymanagementsystem.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.sql.ComparisonRestriction;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepository {
    @PersistenceContext(unitName = "LibraryPU")
    private EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;
    }
    public Optional<User> findByEmail(String email){
       try{ User user=em.createQuery("SELECT u FROM User u where u.email =:email",User.class)
                .setParameter("email",email)
                .getSingleResult();
        return Optional.of(user);}catch (Exception e){return Optional.empty();}

    }
    public List<User> findAll(){
        return em.createQuery("SELECT u FROM User u",User.class).getResultList();
    }

    public Optional<User> findById(Long userId) {
        return Optional.of(em.createQuery("SELECT u FROM User u WHERE id=:userId",User.class)
                .setParameter("userId",userId)
                .getSingleResult());
    }
}
