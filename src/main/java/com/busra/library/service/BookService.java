package com.busra.library.service;

import com.busra.library.model.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    //List<BookDTO> getAllBooks();

    Page<BookDTO> getAllBooks(Pageable pageable);

    BookDTO getBookByTitle(String title);

    BookDTO getBookByAuthor(String author);

    BookDTO getBookByIsbn(String isbn);

    BookDTO getBookByGenre(String genre);

    BookDTO createNewBook(BookDTO bookDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);

    BookDTO getBookById(Long id);
}
