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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceImplTest {

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowMapper borrowMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_Success() {
        Long userId = 1L;
        Long bookId = 2L;
        User user = new User();
        Book book = Book.builder().available(true).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowRepository.existsByUserAndReturnedFalse(user)).thenReturn(false);

        String result = borrowService.borrowBook(userId, bookId);

        assertEquals("Book borrowed successfully!", result);
        assertFalse(book.isAvailable());
        verify(borrowRepository).save(any(Borrow.class));
        verify(bookRepository).save(book);
    }

    @Test
    void borrowBook_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> borrowService.borrowBook(1L, 2L));
    }

    @Test
    void borrowBook_BookNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> borrowService.borrowBook(1L, 2L));
    }

    @Test
    void borrowBook_OverdueRestriction() {
        User user = new User();
        Book book = Book.builder().available(true).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.existsByUserAndReturnedFalse(user)).thenReturn(true);

        assertThrows(BorrowRestrictionException.class,
                () -> borrowService.borrowBook(1L, 2L));
    }

    @Test
    void borrowBook_BookNotAvailable() {
        User user = new User();
        Book book = Book.builder().available(false).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.existsByUserAndReturnedFalse(user)).thenReturn(false);

        String result = borrowService.borrowBook(1L, 2L);
        assertEquals("Book is not available!", result);
    }

    @Test
    void returnBook_Success() {
        User user = new User();
        Book book = Book.builder().available(false).build();
        Borrow borrow = Borrow.builder().returned(false).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.findByUserAndBookAndReturnedFalse(user, book))
                .thenReturn(Optional.of(borrow));

        String result = borrowService.returnBook(1L, 2L);

        assertEquals("Book returned successfully!", result);
        assertTrue(borrow.isReturned());
        assertTrue(book.isAvailable());
    }

    @Test
    void returnBook_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        String result = borrowService.returnBook(1L, 2L);
        assertEquals("User not found!", result);
    }

    @Test
    void returnBook_BookNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        String result = borrowService.returnBook(1L, 2L);
        assertEquals("Book not found!", result);
    }

    @Test
    void returnBook_NotBorrowed() {
        User user = new User();
        Book book = new Book();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowRepository.findByUserAndBookAndReturnedFalse(user, book)).thenReturn(Optional.empty());

        String result = borrowService.returnBook(1L, 2L);
        assertEquals("This book was not borrowed by the user!", result);
    }

    @Test
    void getUserBorrowHistory_ReturnsList() {
        User user = new User();
        Borrow borrow = new Borrow();
        BorrowDTO dto = new BorrowDTO();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(borrowRepository.findByUser(user)).thenReturn(List.of(borrow));
        when(borrowMapper.borrowToBorrowDTO(borrow)).thenReturn(dto);

        List<BorrowDTO> result = borrowService.getUserBorrowHistory(1L);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getUserBorrowHistory_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> borrowService.getUserBorrowHistory(1L));
    }

    @Test
    void getOverdueBooks_ReturnsList() {
        Borrow borrow = new Borrow();
        BorrowDTO dto = new BorrowDTO();

        when(borrowRepository.findByDueDateBeforeAndReturnedFalse(any()))
                .thenReturn(List.of(borrow));
        when(borrowMapper.borrowToBorrowDTO(borrow)).thenReturn(dto);

        List<BorrowDTO> result = borrowService.getOverdueBooks();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}
