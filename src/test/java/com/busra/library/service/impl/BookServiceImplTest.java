package com.busra.library.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.busra.library.exception.BookNotFoundException;
import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.mapper.BookMapper;
import com.busra.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookServiceImpl bookService;

    Book book;
    BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Author Name")
                .isbn("123456789")
                .genre("Fiction")
                .build();

        bookDTO = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .author("Author Name")
                .isbn("123456789")
                .genre("Fiction")
                .build();
    }

    @Test
    void getAllBooks_shouldReturnPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.getAllBooks(pageable);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getBookById_shouldReturnBookDTO_whenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void getBookById_shouldThrowException_whenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void searchBookByTitle_shouldReturnMatchingBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findByTitleContainingIgnoreCase("Test", pageable)).thenReturn(bookPage);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByTitle("Test", 0, 5);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchBookByAuthor_shouldReturnMatchingBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findByAuthorContainingIgnoreCase("Author", pageable)).thenReturn(bookPage);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByAuthor("Author", 0, 5);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchBookByIsbn_shouldReturnMatchingBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findByIsbnContainingIgnoreCase("123", pageable)).thenReturn(bookPage);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByIsbn("123", 0, 5);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchBookByGenre_shouldReturnMatchingBooks() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findByGenreContainingIgnoreCase("Fiction", pageable)).thenReturn(bookPage);
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBookByGenre("Fiction", 0, 5);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void createNewBook_shouldSaveAndReturnBookDTO() {
        when(bookMapper.bookDtoToBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.createNewBook(bookDTO);

        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void updateBook_shouldUpdateAndReturnBookDTO() {
        when(bookMapper.bookDtoToBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void deleteBookById_shouldDelete_whenBookExists() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBookById(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBookById_shouldThrow_whenBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(1L));
    }
}
