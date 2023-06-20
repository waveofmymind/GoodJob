package com.goodjob.core.domain.comment.repository;

import com.goodjob.core.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
