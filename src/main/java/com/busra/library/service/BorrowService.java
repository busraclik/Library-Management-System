package com.busra.library.service;

import com.busra.library.model.entity.Borrow;

import java.util.List;

public interface BorrowService {
    String borrowBook(Long userId, Long bookId);

    String returnBook(Long userId, Long bookId);

    List<Borrow> getUserBorrowHistory(Long userId);

    List<Borrow> getOverdueBooks();
}
