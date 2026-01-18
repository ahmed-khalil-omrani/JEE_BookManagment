package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.entities.BorrowingRecord;

import java.util.List;

public interface BorrowingRecordService {
    public BorrowingRecord borrowBook(Long bookId,Long userId);
    public BorrowingRecord returnBook(Long recordId);
    public List<BorrowingRecord> getActiveRecord();
    public List<BorrowingRecord> getUserActiveRecord(Long id);
    public List<BorrowingRecord>getUserRecord(Long id);

}
