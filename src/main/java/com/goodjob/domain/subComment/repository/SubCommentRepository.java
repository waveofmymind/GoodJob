package com.goodjob.domain.subComment.repository;

import com.goodjob.domain.subComment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
