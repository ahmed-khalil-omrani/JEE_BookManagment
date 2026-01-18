package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.NotificationRepository;
import com.inventory.librarymanagementsystem.Repository.UserRepository;
import com.inventory.librarymanagementsystem.entities.Notification;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.NotificationType;
import jakarta.inject.Inject;

import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    @Inject
    private NotificationRepository notificationRepository;
    @Inject
    private UserRepository userRepository;
    @Override
    public Notification sendNotification(User user, String message, NotificationType type) {
        Long userId=user.getId();
        if(!userRepository.findById(userId).isPresent()){
            throw new IllegalArgumentException("this user dossn't exists");
        }
        Notification notification = new Notification(user, message, type);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotificationsForUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException(" user with id"+userId+"does not exist"));
        return notificationRepository.findByUserId(userId);
    }
}
