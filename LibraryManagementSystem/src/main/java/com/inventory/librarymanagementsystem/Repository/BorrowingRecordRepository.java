package com.inventory.librarymanagementsystem.Repository;

import com.inventory.librarymanagementsystem.entities.BorrowingRecord;
import com.inventory.librarymanagementsystem.enums.BorrowStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.inventory.librarymanagementsystem.enums.BorrowStatus.BORROWED;
import static com.inventory.librarymanagementsystem.enums.BorrowStatus.OVERDUE;

@Stateless
public class BorrowingRecordRepository {

    @PersistenceContext(unitName = "LibraryPU")
    private EntityManager em;

    public BorrowingRecord save(BorrowingRecord record){
        if(record.getId()==null){
            em.persist(record);
        }
        else{
            record=em.merge(record);
        }return record;
    }

    public Optional<BorrowingRecord> findById(Long id) {
        return Optional.ofNullable(em.find(BorrowingRecord.class, id));
    }
    public List<BorrowingRecord>findAllActiveRecords(){
        return em.createQuery("SELECT r FROM BorrowingRecord r WHERE r.status=:status",BorrowingRecord.class)
                .setParameter("status",BORROWED)
                .getResultList();
    }
    public List<BorrowingRecord>findActiveByUserId(Long id){
        return em.createQuery("SELECT r FROM BorrowingRecord r WHERE r.status=:status AND r.user.id =: userId",BorrowingRecord.class)
                .setParameter("status",BORROWED)
                .setParameter("userId",id)
                .getResultList();
    }
    public List<BorrowingRecord> findOVERDUEByUserId(Long id){
        return em.createQuery("SELECT r FROM BorrowingRecord r WHERE r.status=:status AND r.user.id =: userId",BorrowingRecord.class)
                .setParameter("status",OVERDUE)
                .setParameter("userId",id)
                .getResultList();
    }
    public List<BorrowingRecord> findRecordByUserId(Long id){
        return em.createQuery("SELECT r FROM BorrowingRecord r WHERE r.user.id =: userId",BorrowingRecord.class)
                .setParameter("userId",id)
                .getResultList();
    }

    public List<BorrowingRecord> findOverudeRecords(LocalDate today){
        return em.createQuery("SELECT b FROM BorrowingRecord b WHERE b.status = :status AND b.dueDate < :today", BorrowingRecord.class)
                .setParameter("status", BORROWED)
                .setParameter("today", today)
                .getResultList();
    }
}
