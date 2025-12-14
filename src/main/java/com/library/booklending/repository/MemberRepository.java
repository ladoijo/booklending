package com.library.booklending.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.booklending.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Modifying
    @Query("""
        UPDATE Member AS member
        SET member.isDeleted = :isDelete
        WHERE member.id = :id
    """)
    void updateDeleteFlagById(Long id, boolean isDelete);
}
