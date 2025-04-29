package com.busra.library.repository;


import com.busra.library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
    Book findByAuthor(String author);
    Book findByIsbn(String isbn);
    Book findByGenre(String genre);
}
