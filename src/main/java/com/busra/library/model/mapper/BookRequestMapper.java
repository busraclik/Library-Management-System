package com.busra.library.model.mapper;

import com.busra.library.model.dto.BookRequestDTO;
import com.busra.library.model.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {
    BookRequestDTO bookToBookRequestDTO(Book book);
    Book bookRequestDtoToBook(BookRequestDTO bookRequestDTO);
}
