package com.library.booklending.controller;

import com.library.booklending.constant.Endpoint;
import com.library.booklending.dto.ApiRespDto;
import com.library.booklending.dto.MemberReqDto;
import com.library.booklending.dto.MemberRespDto;
import com.library.booklending.service.MemberService;
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
@Tag(name = "Members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService service;

  @Operation(summary = "List all members")
  @GetMapping(Endpoint.MEMBERS_V1)
  public ResponseEntity<ApiRespDto<List<MemberRespDto>>> findAll() {
    var data = service.findAll();
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Get a member by id")
  @GetMapping(Endpoint.MEMBERS_BY_ID_V1)
  public ResponseEntity<ApiRespDto<MemberRespDto>> findById(@PathVariable Long id) {
    var data = service.findById(id);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Create a new member")
  @PostMapping(Endpoint.MEMBERS_V1)
  public ResponseEntity<ApiRespDto<MemberRespDto>> save(@Valid @RequestBody MemberReqDto reqDto) {
    var data = service.save(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Update a member")
  @PutMapping(Endpoint.MEMBERS_V1)
  public ResponseEntity<ApiRespDto<MemberRespDto>> update(@Valid @RequestBody MemberReqDto reqDto) {
    var data = service.save(reqDto);
    return ResponseUtil.okWithData(data);
  }

  @Operation(summary = "Delete a member")
  @DeleteMapping(Endpoint.MEMBERS_BY_ID_V1)
  public ResponseEntity<ApiRespDto<?>> deleteFlagById(@PathVariable Long id) {
    service.deleteFlagById(id);
    return ResponseUtil.ok();
  }
}