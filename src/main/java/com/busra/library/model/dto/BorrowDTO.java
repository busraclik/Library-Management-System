package com.busra.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDTO {
    private UserResponseDTO user;
    private BookDTO book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    @Builder.Default
    private boolean returned = false;
}
