package com.busra.library.service;

import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.Borrow;

import java.util.List;

public interface BorrowService {
    String borrowBook(Long userId, Long bookId);

    String returnBook(Long userId, Long bookId);

    List<BorrowDTO> getUserBorrowHistory(Long userId);

    List<BorrowDTO> getOverdueBooks();
}
