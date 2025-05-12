package com.busra.library.service.impl;

import com.busra.library.exception.BorrowRestrictionException;
import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import com.busra.library.model.mapper.BorrowMapper;
import com.busra.library.repository.BookRepository;
import com.busra.library.repository.BorrowRepository;
import com.busra.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BorrowServiceImplTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowMapper borrowMapper;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User();
        book = Book.builder().available(true).build();
    }

    @Test
    void borrowBook_OverdueRestriction() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.existsByUserAndReturnedFalseAndDueDateBefore(user, LocalDate.now())).thenReturn(true);

        assertThrows(BorrowRestrictionException.class,
                () -> borrowService.borrowBook(1L, 2L));
    }

    @Test
    void borrowBook_BookNotAvailable() {

        Book unavailableBook = Book.builder().available(false).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(unavailableBook));
        when(borrowRepository.existsByUserAndReturnedFalseAndDueDateBefore(user, LocalDate.now())).thenReturn(false);

        String result = borrowService.borrowBook(1L, 2L);

        assertEquals("Book is not available!", result);
    }

    @Test
    void borrowBook_Success() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.existsByUserAndReturnedFalseAndDueDateBefore(user, LocalDate.now())).thenReturn(false);

        String result = borrowService.borrowBook(1L, 2L);

        assertEquals("Book borrowed successfully!", result);
        verify(borrowRepository, times(1)).save(any(Borrow.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void returnBook_BookNotBorrowed() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        String result = borrowService.returnBook(1L, 2L);

        assertEquals("This book was not borrowed by the user!", result);
    }

    @Test
    void returnBook_Success() {
        Borrow borrow = Borrow.builder().user(user).book(book).returned(false).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.findByUserAndBookAndReturnedFalse(user, book)).thenReturn(Optional.of(borrow));

        String result = borrowService.returnBook(1L, 2L);

        assertEquals("Book returned successfully!", result);
        verify(borrowRepository, times(1)).save(borrow);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void getUserBorrowHistory_Success() {

        List<Borrow> borrows = new ArrayList<>();
        borrows.add(Borrow.builder().build());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(borrowRepository.findByUser(user)).thenReturn(borrows);

        List<BorrowDTO> borrowHistory = borrowService.getUserBorrowHistory(1L);

        assertNotNull(borrowHistory);
        assertFalse(borrowHistory.isEmpty());
    }

    @Test
    void getOverdueBooks_Success() {
        // Overdue books
        List<Borrow> overdueBooks = new ArrayList<>();
        overdueBooks.add(Borrow.builder().build());

        when(borrowRepository.findByDueDateBeforeAndReturnedFalse(any(LocalDate.class))).thenReturn(overdueBooks);

        List<BorrowDTO> overdueBooksList = borrowService.getOverdueBooks();

        assertNotNull(overdueBooksList);
        assertFalse(overdueBooksList.isEmpty());
    }
}
