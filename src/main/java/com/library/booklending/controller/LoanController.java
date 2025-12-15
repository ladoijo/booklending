package com.library.booklending.controller;

import com.library.booklending.constant.Endpoint;
import com.library.booklending.dto.ApiRespDto;
import com.library.booklending.dto.BorrowBookReqDto;
import com.library.booklending.dto.BorrowBookRespDto;
import com.library.booklending.usecase.LoanUseCase;
import com.library.booklending.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Loans")
@RequiredArgsConstructor
public class LoanController {

  private final LoanUseCase useCase;

  @Operation(summary = "Create a new book loan")
  @PostMapping(Endpoint.LOANS_V1)
  public ResponseEntity<ApiRespDto<BorrowBookRespDto>> borrowBook(
      @Valid @RequestBody BorrowBookReqDto reqDto) {
    var data = useCase.borrowBook(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Return a book loan")
  @PostMapping(Endpoint.LOAN_RETURN_V1)
  public ResponseEntity<ApiRespDto<BorrowBookRespDto>> returnBook(@PathVariable Long id) {
    var data = useCase.returnBook(id);
    return ResponseUtil.okWithData(data);
  }
}