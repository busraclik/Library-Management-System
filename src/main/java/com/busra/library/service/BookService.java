package com.busra.library.service;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.dto.BookRequestDTO;
import com.busra.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDTO> getAllBooks(Pageable pageable);

    Page<BookDTO> searchBookByTitle(String title, int page, int size);

    Page<BookDTO> searchBookByAuthor(String author, int page, int size);

    Page<BookDTO> searchBookByIsbn(String isbn, int page, int size);

    Page<BookDTO> searchBookByGenre(String genre, int page, int size);

    BookDTO createNewBook(BookRequestDTO bookRequestDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);

    BookDTO getBookById(Long id);
}
