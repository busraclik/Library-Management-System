package com.busra.library.model.mapper;

import com.busra.library.model.dto.BookDTO;
import com.busra.library.model.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO bookToBookDTO(Book book);
    Book bookDtoToBook(BookDTO bookDTO);
}
