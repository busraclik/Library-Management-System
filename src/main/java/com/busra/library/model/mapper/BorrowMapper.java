package com.busra.library.model.mapper;

import com.busra.library.model.dto.BorrowDTO;
import com.busra.library.model.entity.Borrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowMapper {


    @Mapping(source = "user", target = "user")
    @Mapping(source = "book", target = "book")
    BorrowDTO borrowToBorrowDTO(Borrow borrow);


}


