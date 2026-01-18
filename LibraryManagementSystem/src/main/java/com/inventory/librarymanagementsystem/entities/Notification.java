package com.inventory.librarymanagementsystem.entities;

import com.inventory.librarymanagementsystem.enums.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="Notifications")
public class Notification {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="message", nullable=false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable=false)
    private NotificationType type;

    @Column(name="isRead")
    private boolean isRead = false;

    @Column(name="createdDate")
    private LocalDateTime createdDate;

    public Notification() {
        createdDate=LocalDateTime.now();
        isRead = false;
    }
    public Notification(User user, String message, NotificationType type) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.createdDate = LocalDateTime.now();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "user=" + user +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", isRead=" + isRead +
                ", createdDate=" + createdDate +
                '}';
    }
}
