package com.goodjob.article.domain.subComment.repository;

import com.goodjob.article.domain.subComment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
