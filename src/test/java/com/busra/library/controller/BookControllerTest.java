package com.busra.library.controller;

import com.busra.library.exception.BookNotFoundException;
import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testCreateNewBook() throws Exception {
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .isbn("1234567890")
                .publishedDate(LocalDateTime.now())
                .genre("Fiction")
                .available(true)
                .build();

        when(bookService.createNewBook(any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content("{\"title\":\"Test Book\", \"author\":\"Test Author\", \"isbn\":\"1234567890\", \"publishedDate\":\"2025-01-01T00:00:00\", \"genre\":\"Fiction\", \"available\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .isbn("1234567890")
                .publishedDate(LocalDateTime.now())
                .genre("Fiction")
                .available(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDTO> bookPage = new PageImpl<>(List.of(bookDTO), pageable, 1);

        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Book"));
    }



    @Test
    public void testGetBookById() throws Exception {
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .isbn("1234567890")
                .publishedDate(LocalDateTime.now())
                .genre("Fiction")
                .available(true)
                .build();

        when(bookService.getBookById(1L)).thenReturn(bookDTO);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("Updated Book")
                .author("Updated Author")
                .isbn("0987654321")
                .publishedDate(LocalDateTime.now())
                .genre("Non-Fiction")
                .available(false)
                .build();

        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(put("/api/books/1")
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Book\", \"author\":\"Updated Author\", \"isbn\":\"0987654321\", \"publishedDate\":\"2025-01-01T00:00:00\", \"genre\":\"Non-Fiction\", \"available\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBookById(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBookById(1L);
    }




}
