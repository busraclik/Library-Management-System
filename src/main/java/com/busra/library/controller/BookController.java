package com.busra.library.controller;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping
//    public ResponseEntity<List<BookDTO>> getAllBooks(){
//        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id){
        return new ResponseEntity<BookDTO>(bookService.getBookById(id), HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title){
        return new ResponseEntity<>(
                bookService.getBookByTitle(title), HttpStatus.OK);
    }


    @GetMapping("/author/{author}")
    public ResponseEntity<BookDTO> getBookByAuthor(@PathVariable String author){
        return new ResponseEntity<>(
                bookService.getBookByAuthor(author), HttpStatus.OK);
    }


    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn){
        return new ResponseEntity<>(
                bookService.getBookByIsbn(isbn), HttpStatus.OK);
    }


    @GetMapping("/genre/{genre}")
    public ResponseEntity<BookDTO> getBookByGenre(@PathVariable String genre){
        return new ResponseEntity<>(
                bookService.getBookByGenre(genre), HttpStatus.OK);
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
