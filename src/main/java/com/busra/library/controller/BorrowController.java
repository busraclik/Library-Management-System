package com.busra.library.controller;

import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import com.busra.library.model.enums.Role;
import com.busra.library.service.BorrowService;
import com.busra.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasAuthority('PATRON')")
    @PostMapping
    public ResponseEntity<String> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        String result = borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('PATRON')")
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long userId, @RequestParam Long bookId) {
        String result = borrowService.returnBook(userId, bookId);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyAuthority('LIBRARIAN', 'PATRON')")
    @GetMapping("/history")
    public ResponseEntity<List<BorrowDTO>> getUserBorrowingHistory(
            @RequestParam Long userId,
            Authentication authentication
    ) {
        String currentUsername = authentication.getName();

        // Kullanıcıyı veritabanından çekiyoruz
        User currentUser = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Eğer kullanıcı PATRON ise sadece kendi geçmişini görüntüleyebilir
        if (currentUser.getRole().equals(Role.PATRON) && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

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
