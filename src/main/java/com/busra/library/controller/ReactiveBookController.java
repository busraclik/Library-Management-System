package com.busra.library.controller;

import com.busra.library.model.entity.Book;
import com.busra.library.service.impl.ReactiveBookAvailabilityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactive/books")
public class ReactiveBookController {

    private final ReactiveBookAvailabilityService reactiveBookAvailabilityService;

    public ReactiveBookController(ReactiveBookAvailabilityService reactiveBookAvailabilityService) {
        this.reactiveBookAvailabilityService = reactiveBookAvailabilityService;
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public Mono<ResponseEntity<Book>> updateAvailability(@PathVariable Long id,
                                                         @RequestParam boolean available) {
        return reactiveBookAvailabilityService.updateBookAvailability(id, available)
                .map(book -> ResponseEntity.ok(book))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
