package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.BorrowingRecordRepository;
import com.inventory.librarymanagementsystem.entities.BorrowingRecord;
import com.inventory.librarymanagementsystem.enums.BorrowStatus;
import com.inventory.librarymanagementsystem.enums.NotificationType;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@Singleton
@Startup
public class OverdueCheckScheduler {
    @Inject
    private BorrowingRecordRepository borrowingRepository;

    @Inject
    private NotificationService notificationService;
    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void checkOverdueBooks() {
        LocalDate today = LocalDate.now();
        List<BorrowingRecord> overdueRecords = borrowingRepository.findOverudeRecords(today);

        for (BorrowingRecord record : overdueRecords) {
            record.setStatus(BorrowStatus.OVERDUE);
            borrowingRepository.save(record);
            String message = "URGENT: Your book '" + record.getBook().getTitle() + "' was due on " + record.getDueDate() + ". Please return it immediately.";
            notificationService.sendNotification(record.getUser(), message, NotificationType.OVERDUE_ALERT);
        }
    }
}
