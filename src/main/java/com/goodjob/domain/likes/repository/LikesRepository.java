package com.goodjob.domain.likes.repository;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.comment.entity.Comment;
import com.goodjob.domain.likes.entity.Likes;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.subComment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByArticleAndMember(Article article, Member member);

    Optional<Likes> findByCommentAndMember(Comment comment, Member member);

    Optional<Likes> findBySubCommentAndMember(SubComment subComment, Member member);
}
