package com.busra.library.service.impl;

import com.busra.library.exception.BookNotFoundException;
import com.busra.library.exception.BorrowRestrictionException;
import com.busra.library.exception.UserNotFoundException;
import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import com.busra.library.model.mapper.BorrowMapper;
import com.busra.library.repository.BookRepository;
import com.busra.library.repository.BorrowRepository;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.BorrowService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowMapper borrowMapper;

    public BorrowServiceImpl(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository, BorrowMapper borrowMapper) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowMapper = borrowMapper;
    }

    public String borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("This user is not in the list"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("This book is not in the list"));


        if (hasOverdueBooks(user)) {
            throw new BorrowRestrictionException("You have an overdue book. Please return it before borrowing a new one.");
        }

        if (!book.isAvailable()) {
            return "Book is not available!";
        }

        Borrow borrow = Borrow.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returned(false)
                .build();

        book.setAvailable(false);

        borrowRepository.save(borrow);
        bookRepository.save(book);

        return "Book borrowed successfully!";
    }

    // overdue book check
    private boolean hasOverdueBooks(User user) {
        return borrowRepository.existsByUserAndReturnedFalseAndDueDateBefore(user, LocalDate.now());
    }

    public String returnBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found!"));

        Optional<Borrow> borrow = borrowRepository.findByUserAndBookAndReturnedFalse(user, book);

        if (borrow.isEmpty()) {
            return "This book was not borrowed by the user!";
        }

        Borrow currentBorrow = borrow.get();
        currentBorrow.setReturned(true);
        borrowRepository.save(currentBorrow);

        book.setAvailable(true);
        bookRepository.save(book);

        return "Book returned successfully!";
    }

    public List<BorrowDTO> getUserBorrowHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return borrowRepository.findByUser(user)
                .stream()
                .map(borrowMapper::borrowToBorrowDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDTO> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return borrowRepository.findByDueDateBeforeAndReturnedFalse(today)
                .stream()
                .map(borrowMapper::borrowToBorrowDTO)
                .collect(Collectors.toList());
    }
}
