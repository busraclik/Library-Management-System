package com.busra.library.service.impl;

import com.busra.library.model.entity.Book;
import com.busra.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReactiveBookAvailabilityService {

    private final BookRepository bookRepository;

    public ReactiveBookAvailabilityService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<Book> updateBookAvailability(Long id, boolean available) {
        return Mono.justOrEmpty(bookRepository.findById(id)) // Optional -> Mono
                .flatMap(book -> {

                    book.setAvailable(available);

                    return Mono.fromCallable(() -> bookRepository.save(book))
                            .doOnTerminate(() -> {
                                log.info("Save operation completed");
                            });
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Book not found with ID: " + id)))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error updating availability", e)));
    }


}
