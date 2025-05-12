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
                    .nameSurname("Librarian User")
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
            String[][] sampleBooks = {
                    {"The Silent Patient", "Alex Michaelides", "9781250301697", "Thriller"},
                    {"Educated", "Tara Westover", "9780399590504", "Memoir"},
                    {"The Midnight Library", "Matt Haig", "9780525559474", "Fantasy"},
                    {"Becoming", "Michelle Obama", "9781524763138", "Biography"},
                    {"Where the Crawdads Sing", "Delia Owens", "9780735219106", "Mystery"},
                    {"Atomic Habits", "James Clear", "9780735211292", "Self-help"},
                    {"Sapiens", "Yuval Noah Harari", "9780062316097", "History"},
                    {"The Alchemist", "Paulo Coelho", "9780061122415", "Fiction"},
                    {"The Subtle Art", "Mark Manson", "9780062457714", "Self-help"},
                    {"Normal People", "Sally Rooney", "9780571334650", "Drama"}
            };

            for (int i = 0; i < sampleBooks.length; i++) {
                Book book = Book.builder()
                        .title(sampleBooks[i][0])
                        .author(sampleBooks[i][1])
                        .isbn(sampleBooks[i][2])
                        .genre(sampleBooks[i][3])
                        .publishedDate(LocalDateTime.now().minusYears(2 + i))
                        .available(true)
                        .build();
                bookRepository.save(book);
            }
        }


        if (borrowRepository.count() == 0) {
            User user = userRepository.findByUsername("patronuser").orElseThrow();
            Book book = bookRepository.findAll().get(3);

            book.setAvailable(false);
            bookRepository.save(book);

            Borrow borrow = Borrow.builder()
                    .user(user)
                    .book(book)
                    .borrowDate(LocalDate.from(LocalDateTime.now().minusDays(20)))
                    .dueDate(LocalDate.from(LocalDateTime.now().minusDays(6)))
                    .returned(false)
                    .build();

            borrowRepository.save(borrow);
        }


    }
}