package com.inventory.librarymanagementsystem.entities;

import com.inventory.librarymanagementsystem.enums.BorrowStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="BorrowingRecords")
@NamedQueries({@NamedQuery(name="BorrowingRecords.findAll",query = "SELECT b FROM BorrowingRecord b ORDER BY b.borrowDate ")

})
public class BorrowingRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;



    @Column(name="borrowDate", nullable=false)
    private LocalDateTime borrowDate;


    @Column(name="dueDate", nullable=false)
    private LocalDate dueDate;


    @Column(name="returnDate")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private BorrowStatus status;

    public BorrowingRecord() {
        this.status = BorrowStatus.BORROWED;
        this.borrowDate = LocalDateTime.now();

    }

    public BorrowingRecord(User user, Book book, LocalDateTime borrowDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = BorrowStatus.BORROWED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                ", user=" + user.getName() +
                ", book=" + book.getTitle() +
                '}';
    }
}
