package com.goodjob.domain.member.repository;

import com.goodjob.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String account);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);
}
