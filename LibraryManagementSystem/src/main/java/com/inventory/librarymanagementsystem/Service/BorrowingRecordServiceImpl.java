package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.BookRepository;
import com.inventory.librarymanagementsystem.Repository.BorrowingRecordRepository;
import com.inventory.librarymanagementsystem.Repository.UserRepository;
import com.inventory.librarymanagementsystem.entities.Book;
import com.inventory.librarymanagementsystem.entities.BorrowingRecord;
import com.inventory.librarymanagementsystem.entities.User;
import com.inventory.librarymanagementsystem.enums.BorrowStatus;
import com.inventory.librarymanagementsystem.enums.NotificationType;
import jakarta.inject.Inject;
import jakarta.xml.bind.annotation.XmlInlineBinaryData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BorrowingRecordServiceImpl implements BorrowingRecordService {
    @Inject
    private BorrowingRecordRepository recordRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private BookRepository bookRepository;
    @Inject
    private NotificationService notificationService;

    @Override
    public BorrowingRecord borrowBook(Long bookId, Long userId) {
        Book book=bookRepository.findById(bookId);
        if (book == null) throw new IllegalArgumentException("Book not found");
        User user=userRepository.findById(userId).orElse(null);
        if (user == null) throw new IllegalArgumentException("User not found");
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available");
        }
        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDate dueDate = LocalDate.now().plusWeeks(2);
        BorrowingRecord record = new BorrowingRecord(user, book, borrowDate, dueDate);
        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.update(book);
        notificationService.sendNotification(user, "You have successfully borrowed '" + book.getTitle() + "'. Due date: " + dueDate, NotificationType.SUCCESS);

        return recordRepository.save(record);
    }

    @Override
    public BorrowingRecord returnBook(Long recordId) {
        BorrowingRecord record=recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        if(record.getStatus()== BorrowStatus.RETURNED){
            throw new IllegalStateException("Book already returned");
        }

        Book book=record.getBook();
        User user=record.getUser();
        if(record.getStatus()==BorrowStatus.BORROWED)
            notificationService.sendNotification(user, "You have successfully returned '" + book.getTitle() ,NotificationType.SUCCESS);
        else {
            LocalDate returnDate = LocalDate.now();
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
            long fineAmount = overdueDays * 2;
            notificationService.sendNotification(user, "You have returned '" + book.getTitle() + "' late. Please note you have a fine to pay a fine of "+fineAmount+"$"
                    , NotificationType.OVERDUE_ALERT);
        }
        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        book.setAvailableCopies(book.getAvailableCopies()+1);
        bookRepository.update(book);



        return recordRepository.save(record);
    }

    @Override
    public List<BorrowingRecord> getActiveRecord() {
        return recordRepository.findAllActiveRecords();
    }

    @Override
    public List<BorrowingRecord> getUserActiveRecord(Long id) {
        User user =userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException(" user with id"+id+"does not exist"));
        return recordRepository.findRecordByUserId(id);
    }

    @Override
    public List<BorrowingRecord> getUserRecord(Long id) {
        User user =userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException(" user with id"+id+"does not exist"));
        return recordRepository.findRecordByUserId(id);
    }
}
