package com.busra.library.repository;

import com.busra.library.model.entity.Book;
import com.busra.library.model.entity.Borrow;
import com.busra.library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    Optional<Borrow> findByUserAndBookAndReturnedFalse(User user, Book book);

    List<Borrow> findByUser(User user);

    List<Borrow> findByDueDateBeforeAndReturnedFalse(LocalDate today);
}
