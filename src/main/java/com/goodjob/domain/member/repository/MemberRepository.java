package com.goodjob.domain.member.repository;

import com.goodjob.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String username);
}
