package com.busra.library.repository;


import com.busra.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;


public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
    Book findByTitle(String title);
    Book findByAuthor(String author);
    Book findByIsbn(String isbn);
    Book findByGenre(String genre);
}
