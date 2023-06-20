package com.goodjob.core.domain.likes.repository;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.comment.entity.Comment;
import com.goodjob.core.domain.likes.entity.Likes;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.subComment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByArticleAndMember(Article article, Member member);

    Optional<Likes> findByCommentAndMember(Comment comment, Member member);

    Optional<Likes> findBySubCommentAndMember(SubComment subComment, Member member);
}
