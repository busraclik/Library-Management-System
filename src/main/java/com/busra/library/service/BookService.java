package com.busra.library.service;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    //List<BookDTO> getAllBooks();

    Page<BookDTO> getAllBooks(Pageable pageable);

    //BookDTO getBookByTitle(String title);
    //Page<Book> searchBooks(String title, int page, int size);
    Page<BookDTO> searchBookByTitle(String title, int page, int size);

    //BookDTO getBookByAuthor(String author);
    Page<Book> searchBookByAuthor(String author, int page, int size);

    //BookDTO getBookByIsbn(String isbn);
    Page<Book> searchBookByIsbn(String isbn, int page, int size);

    //BookDTO getBookByGenre(String genre);
    Page<Book> searchBookByGenre(String genre, int page, int size);

    BookDTO createNewBook(BookDTO bookDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);

    BookDTO getBookById(Long id);
}
