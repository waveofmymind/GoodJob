package com.goodjob.core.domain.subComment.repository;

import com.goodjob.core.domain.subComment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
