package com.busra.library.controller;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> createNewBook(@Valid @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.createNewBook(bookDTO), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id){
        return new ResponseEntity<BookDTO>(bookService.getBookById(id), HttpStatus.OK);
    }


    @GetMapping("/search/title")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    public ResponseEntity<Page<BookDTO>> searchBooksByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> result = bookService.searchBookByTitle(title, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/author")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    public ResponseEntity<Page<BookDTO>> searchBooksByAuthor(
            @RequestParam String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> result = bookService.searchBookByAuthor(author, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/isbn")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    public ResponseEntity<Page<BookDTO>> searchBooksByIsbn(
            @RequestParam String isbn,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> result = bookService.searchBookByIsbn(isbn, page, size);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/search/genre")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    public ResponseEntity<Page<BookDTO>> searchBooksByGenre(
            @RequestParam String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookDTO> result = bookService.searchBookByGenre(genre, page, size);
        return ResponseEntity.ok(result);
    }


    @PutMapping({"/{id}"})
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO){
        return new ResponseEntity<BookDTO>(
                bookService.updateBook(id, bookDTO), HttpStatus.OK);
    }


    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
