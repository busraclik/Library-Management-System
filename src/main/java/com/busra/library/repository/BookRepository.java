package com.busra.library.repository;


import com.busra.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;


public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
    //Book findByTitle(String title);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    //Book findByAuthor(String author);
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    //Book findByIsbn(String isbn);
    Page<Book> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);
    //Book findByGenre(String genre);
    Page<Book> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
}
