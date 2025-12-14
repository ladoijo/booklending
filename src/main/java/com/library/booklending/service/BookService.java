package com.library.booklending.service;

import com.library.booklending.dto.BookReqDto;
import com.library.booklending.dto.BookRespDto;
import com.library.booklending.entity.Book;
import com.library.booklending.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository repository;

  public List<BookRespDto> findAll() {
    var books = repository.findAll();
    var dtos = new ArrayList<BookRespDto>();
    for (var book : books) {
      dtos.add(new BookRespDto(book));
    }
    return dtos;
  }

  public BookRespDto findById(Long id) {
    var book = repository.findById(id).orElse(null);
    return book == null ? null : new BookRespDto(book);
  }

  public boolean existsById(Long id) {
    return repository.existsById(id);
  }

  public BookRespDto save(BookReqDto reqDto) {
    var book = new Book(reqDto);
    book = repository.save(book);
    return new BookRespDto(book);
  }

  @Transactional
  public void deleteFlagById(Long id) {
    repository.updateDeleteFlagById(id, true);
  }

  public int decrementAvailableCopies(Long id) {
    return repository.decrementAvailableCopies(id);
  }

  public int incrementAvailableCopies(Long id) {
    return repository.incrementAvailableCopies(id);
  }
}
