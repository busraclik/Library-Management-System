package com.busra.library.service;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookByTitle(String title);

    BookDTO getBookByAuthor(String author);

    BookDTO getBookByIsbn(String isbn);

    BookDTO getBookByGenre(String genre);

    BookDTO createNewBook(BookDTO bookDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);
}
