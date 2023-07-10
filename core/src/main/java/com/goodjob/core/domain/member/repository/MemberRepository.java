package com.goodjob.core.domain.member.repository;



import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.coin= :maxCoinCount WHERE m.coin BETWEEN 0 AND :maxCoinCount")
    void updateCoinForFreeMembers(@Param("maxCoinCount") int maxCoinCount);

}
