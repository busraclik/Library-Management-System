package com.busra.library.service.impl;

import com.busra.library.exception.BookNotFoundException;
import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.mapper.BookMapper;
import com.busra.library.repository.BookRepository;
import com.busra.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::bookToBookDTO);
    }


    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return bookMapper.bookToBookDTO(book);
    }

    public Page<BookDTO> searchBookByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        return books.map(bookMapper::bookToBookDTO);
    }

    @Override
    public Page<BookDTO> searchBookByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        return books.map(bookMapper::bookToBookDTO);
    }

    @Override
    public Page<BookDTO> searchBookByIsbn(String isbn, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
        return books.map(bookMapper::bookToBookDTO);
    }

    @Override
    public Page<BookDTO> searchBookByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findByGenreContainingIgnoreCase(genre, pageable);
        return books.map(bookMapper::bookToBookDTO);
    }


    @Override
    public BookDTO createNewBook(BookDTO bookDTO) {
        return saveAndReturnDTO(bookMapper.bookDtoToBook(bookDTO));
    }

    private BookDTO saveAndReturnDTO(Book book) {
        Book saveBook = bookRepository.save(book);
        return bookMapper.bookToBookDTO(saveBook);
    }


    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookMapper.bookDtoToBook(bookDTO);
        book.setId(id);
       return saveAndReturnDTO(book);
    }

    @Override
    public void deleteBookById(Long id) {
        //bookRepository.deleteById(id);
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }


}
