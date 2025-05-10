package com.busra.library.service.impl;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.mapper.BookMapper;
import com.busra.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample book for testing
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("12345");
        book.setGenre("Test Genre");

        // Sample BookDTO for testing
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setIsbn("12345");
        bookDTO.setGenre("Test Genre");
    }

    @Test
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> books = new PageImpl<>(Arrays.asList(book));
        when(bookRepository.findAll(pageable)).thenReturn(books);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchBookByTitle() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> books = new PageImpl<>(Arrays.asList(book));
        when(bookRepository.findByTitleContainingIgnoreCase("Test", pageable)).thenReturn(books);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByTitle("Test", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Test", pageable);
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }

    @Test
    void testSearchBookByAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> books = new PageImpl<>(Arrays.asList(book));
        when(bookRepository.findByAuthorContainingIgnoreCase("Test Author", pageable)).thenReturn(books);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByAuthor("Test Author", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("Test Author", pageable);
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }

    @Test
    void testCreateNewBook() {
        when(bookMapper.bookDtoToBook(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.createNewBook(bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }


    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookDtoToBook(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).bookToBookDTO(any(Book.class));
    }


    @Test
    void testDeleteBookById() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
