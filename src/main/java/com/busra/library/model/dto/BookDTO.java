package com.busra.library.model.dto;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
public class BookDTO {

    private String title;
    private String author;
    private String isbn;
    private LocalDateTime publishedDate;
    private String genre;
}
