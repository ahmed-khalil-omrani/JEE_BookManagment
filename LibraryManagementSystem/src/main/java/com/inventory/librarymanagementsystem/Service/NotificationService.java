package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.entities.Notification;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    Notification sendNotification(User user, String message, NotificationType type);
    List<Notification> getAllNotificationsForUser(Long userId);
}
