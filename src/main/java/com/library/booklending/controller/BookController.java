package com.library.booklending.controller;

import com.library.booklending.constant.Endpoint;
import com.library.booklending.dto.ApiRespDto;
import com.library.booklending.dto.BookReqDto;
import com.library.booklending.dto.BookRespDto;
import com.library.booklending.service.BookService;
import com.library.booklending.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Books")
@RequiredArgsConstructor
public class BookController {

  private final BookService service;

  @Operation(summary = "List all books")
  @GetMapping(Endpoint.BOOKS_V1)
  public ResponseEntity<ApiRespDto<List<BookRespDto>>> findAll() {
    var data = service.findAll();
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Get a book by id")
  @GetMapping(Endpoint.BOOKS_BY_ID_V1)
  public ResponseEntity<ApiRespDto<BookRespDto>> findById(@PathVariable Long id) {
    var data = service.findById(id);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Create a new book")
  @PostMapping(Endpoint.BOOKS_V1)
  public ResponseEntity<ApiRespDto<BookRespDto>> save(@Valid @RequestBody BookReqDto reqDto) {
    var data = service.save(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Update a book")
  @PutMapping(Endpoint.BOOKS_V1)
  public ResponseEntity<ApiRespDto<BookRespDto>> update(@Valid @RequestBody BookReqDto reqDto) {
    var data = service.save(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Delete a book")
  @DeleteMapping(Endpoint.BOOKS_BY_ID_V1)
  public ResponseEntity<ApiRespDto<?>> deleteFlagById(@PathVariable Long id) {
    service.deleteFlagById(id);
    return ResponseUtil.ok();
  }
}