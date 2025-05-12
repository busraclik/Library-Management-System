package com.busra.library.controller;

import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.User;
import com.busra.library.service.BorrowService;
import com.busra.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

public class BorrowControllerTest {

    @InjectMocks
    private BorrowController borrowController;

    @Mock
    private BorrowService borrowService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void borrowBook_ShouldReturnSuccess() {
        // Arrange
        Long bookId = 2L;
        String expectedResponse = "Book borrowed successfully";

        User mockUser = new User();
        mockUser.setId(1L);

        when(borrowService.borrowBook(mockUser.getId(), bookId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = borrowController.borrowBook(bookId, mockUser);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void returnBook_ShouldReturnSuccess() {
        // Arrange
        Long bookId = 2L;
        String expectedResponse = "Book returned successfully";

        User mockUser = new User();
        mockUser.setId(1L);

        when(borrowService.returnBook(mockUser.getId(), bookId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = borrowController.returnBook(bookId, mockUser);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getOwnBorrowingHistory_ShouldReturnSuccess() {
        // Arrange
        Long userId = 1L;
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(userId);

        List<BorrowDTO> borrowDTOList = Collections.singletonList(mock(BorrowDTO.class));
        when(borrowService.getUserBorrowHistory(userId)).thenReturn(borrowDTOList);

        // Act
        ResponseEntity<List<BorrowDTO>> response = borrowController.getOwnBorrowingHistory(currentUser);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(borrowDTOList, response.getBody());
    }


    @Test
    void getOverdueBooks_ShouldReturnSuccess() {
        // Arrange
        List<BorrowDTO> overdueBooks = Collections.singletonList(mock(BorrowDTO.class));
        when(borrowService.getOverdueBooks()).thenReturn(overdueBooks);

        // Act
        ResponseEntity<List<BorrowDTO>> response = borrowController.getOverdueBooks();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(overdueBooks, response.getBody());
    }
}
