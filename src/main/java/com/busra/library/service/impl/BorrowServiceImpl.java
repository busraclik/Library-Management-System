package com.busra.library.service.impl;

import com.busra.library.model.entity.Book;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import com.busra.library.repository.BookRepository;
import com.busra.library.repository.BorrowRepository;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.BorrowService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public String borrowBook(Long userId, Long bookId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);

        if (user.isEmpty()) {
            return "User not found!";
        }
        if (book.isEmpty()) {
            return "Book not found!";
        }

        if (!book.get().isAvailable()) {
            return "Book is not available!";
        }

        Borrow borrow = Borrow.builder()
                .user(user.get())
                .book(book.get())
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14)) // 14 days for book
                .build();

        book.get().setAvailable(false); // book is unavailable

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

        // Ödünç alma kaydını bulmak için
        Optional<Borrow> borrow = borrowRepository.findByUserAndBookAndReturnedFalse(user.get(), book.get());

        if (borrow.isEmpty()) {
            return "This book was not borrowed by the user!";
        }

        // Kitap iade ediliyor
        Borrow currentBorrow = borrow.get();
        currentBorrow.setReturned(true);
        borrowRepository.save(currentBorrow);

        book.get().setAvailable(true); // Kitap tekrar mevcut
        bookRepository.save(book.get());

        return "Book returned successfully!";
    }


    public List<Borrow> getUserBorrowHistory(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return new ArrayList<>(); // User not found
        }

        return borrowRepository.findByUser(user.get());
    }

    public List<Borrow> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return borrowRepository.findByDueDateBeforeAndReturnedFalse(today);
    }

}
