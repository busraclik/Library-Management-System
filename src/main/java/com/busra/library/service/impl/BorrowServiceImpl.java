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
import java.util.ArrayList;
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
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("This user is not in the list");
        }
        if (book.isEmpty()) {
            throw new BookNotFoundException("This book is not in the list");
        }

        // overdue book control
        if (borrowRepository.existsByUserAndReturnedFalse(user.get())) {
            throw new BorrowRestrictionException("You have an overdue book." +
                    " Please return it before borrowing a new one.!");
        }

        if (!book.get().isAvailable()) {
            return "Book is not available!";
        }

        Borrow borrow = Borrow.builder()
                .user(user.get())
                .book(book.get())
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returned(false)
                .build();

        book.get().setAvailable(false);

        borrowRepository.save(borrow);
        bookRepository.save(book.get());

        return "Book borrowed successfully!";
    }


    public String returnBook(Long userId, Long bookId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);

        if (user.isEmpty()) {
            return "User not found!";
        }
        if (book.isEmpty()) {
            return "Book not found!";
        }

        Optional<Borrow> borrow = borrowRepository.findByUserAndBookAndReturnedFalse(user.get(), book.get());

        if (borrow.isEmpty()) {
            return "This book was not borrowed by the user!";
        }

        Borrow currentBorrow = borrow.get();
        currentBorrow.setReturned(true);
        borrowRepository.save(currentBorrow);

        book.get().setAvailable(true);
        bookRepository.save(book.get());

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
