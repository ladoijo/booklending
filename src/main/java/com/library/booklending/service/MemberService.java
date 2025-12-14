package com.library.booklending.service;

import com.library.booklending.dto.MemberReqDto;
import com.library.booklending.dto.MemberRespDto;
import com.library.booklending.entity.Member;
import com.library.booklending.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository repository;

  public List<MemberRespDto> findAll() {
    var members = repository.findAll();
    var dtos = new ArrayList<MemberRespDto>();
    for (var member : members) {
      dtos.add(new MemberRespDto(member));
    }
    return dtos;
  }

  public MemberRespDto findById(Long id) {
    var member = repository.findById(id).orElse(null);
    return member == null ? null : new MemberRespDto(member);
  }

  public boolean existsById(Long id) {
    return repository.existsById(id);
  }

  public MemberRespDto save(MemberReqDto reqDto) {
    var member = new Member(reqDto);
    member = repository.save(member);
    return new MemberRespDto(member);
  }

  @Transactional
  public void deleteFlagById(Long id) {
    repository.updateDeleteFlagById(id, true);
  }
}
