package com.busra.library.config;

import com.busra.library.model.entity.Book;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import com.busra.library.model.enums.Role;
import com.busra.library.repository.BookRepository;
import com.busra.library.repository.BorrowRepository;
import com.busra.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final BorrowRepository borrowRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User librarian = User.builder()
                    .nameSurname("Librarian Celik")
                    .username("librarianuser")
                    .password(passwordEncoder.encode("password123"))
                    .email("librarian@example.com")
                    .role(Role.LIBRARIAN)
                    .build();
            userRepository.save(librarian);

            User patron = User.builder()
                    .nameSurname("Patron User")
                    .username("patronuser")
                    .password(passwordEncoder.encode("password123"))
                    .email("patron@example.com")
                    .role(Role.PATRON)
                    .build();
            userRepository.save(patron);
        }

        if (bookRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                Book book = Book.builder()
                        .title("Book " + i)
                        .author("Author " + i)
                        .isbn("ISBN00" + i)
                        .genre("Genre" + i)
                        .publishedDate(LocalDateTime.now().minusYears(i))
                        .available(true)
                        .build();
                bookRepository.save(book);
            }
        }


        if (borrowRepository.count() == 0) {
            User user = userRepository.findByUsername("patronuser").orElseThrow();
            Book book = bookRepository.findAll().get(0); // ilk kitabı al

            Borrow borrow = Borrow.builder()
                    .user(user)
                    .book(book)
                    .borrowDate(LocalDate.from(LocalDateTime.now().minusDays(20)))
                    .dueDate(LocalDate.from(LocalDateTime.now().minusDays(6))) // geç kalınmış tarih
                    .returned(false)
                    .build();

            borrowRepository.save(borrow);

        }
    }
}