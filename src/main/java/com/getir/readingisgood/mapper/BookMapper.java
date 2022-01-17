package com.getir.readingisgood.mapper;

import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toModel(BookDTO bookDTO);

    BookDTO toDTO(Book book);

}
