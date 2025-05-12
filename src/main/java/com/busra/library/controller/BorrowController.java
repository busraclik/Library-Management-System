package com.busra.library.controller;

import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.User;
import com.busra.library.model.enums.Role;
import com.busra.library.service.BorrowService;
import com.busra.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/borrows")
public class BorrowController {


    private final BorrowService borrowService;
    private final UserService userService;

    public BorrowController(BorrowService borrowService, UserService userService) {
        this.borrowService = borrowService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATRON')")
    public ResponseEntity<String> borrowBook(@RequestParam Long bookId,
                                             @AuthenticationPrincipal User userDetails) {
        Long userId = userDetails.getId(); // getUsername() değil, doğrudan ID
        return ResponseEntity.ok(borrowService.borrowBook(userId, bookId));
    }


    @PreAuthorize("hasAuthority('PATRON')")
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long bookId,
                                             @AuthenticationPrincipal User userDetails) {
        Long userId = userDetails.getId();  // User objesinden ID'yi alıyoruz
        String result = borrowService.returnBook(userId, bookId);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAuthority('PATRON')")
    @GetMapping("/users/me/borrow-history")
    public ResponseEntity<List<BorrowDTO>> getOwnBorrowingHistory(@AuthenticationPrincipal User userDetails) {
        List<BorrowDTO> borrowings = borrowService.getUserBorrowHistory(userDetails.getId());
        return ResponseEntity.ok(borrowings);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("/users/{userId}/borrow-history")
    public ResponseEntity<List<BorrowDTO>> getBorrowingHistoryForUser(@PathVariable Long userId) {
        List<BorrowDTO> borrowings = borrowService.getUserBorrowHistory(userId);
        return ResponseEntity.ok(borrowings);
    }


    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowDTO>> getOverdueBooks() {
        List<BorrowDTO> overdueBooks = borrowService.getOverdueBooks();
        return ResponseEntity.ok(overdueBooks);
    }


}
