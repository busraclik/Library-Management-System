package com.busra.library.service.impl;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import com.busra.library.model.mapper.BookMapper;
import com.busra.library.repository.BookRepository;
import com.busra.library.service.BookService;
import org.springframework.data.domain.Page;
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

//    @Override
//    public List<BookDTO> getAllBooks() {
//        return bookRepository.findAll()
//                .stream()
//                .map(bookMapper::bookToBookDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::bookToBookDTO);
    }


    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return bookMapper.bookToBookDTO(book);
    }

    @Override
    public BookDTO getBookByTitle(String title) {
        return bookMapper.bookToBookDTO(bookRepository.findByTitle(title));
    }

    @Override
    public BookDTO getBookByAuthor(String author) {
        return bookMapper.bookToBookDTO(bookRepository.findByAuthor(author));
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) {
        return bookMapper.bookToBookDTO(bookRepository.findByIsbn(isbn));
    }

    @Override
    public BookDTO getBookByGenre(String genre) {
        return bookMapper.bookToBookDTO(bookRepository.findByGenre(genre));
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
        bookRepository.deleteById(id);
    }


}
