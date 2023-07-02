package com.goodjob.core.domain.comment.repository;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.member.id= :memberId ORDER BY c.createdDate DESC")
    List<Comment> findAllByMemberIdOrderByCreatedDateDesc(@Param("memberId") Long memberId);
}
