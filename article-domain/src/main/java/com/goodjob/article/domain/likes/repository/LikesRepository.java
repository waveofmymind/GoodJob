package com.goodjob.article.domain.likes.repository;


import com.goodjob.article.domain.article.entity.Article;
import com.goodjob.article.domain.comment.entity.Comment;
import com.goodjob.article.domain.likes.entity.Likes;
import com.goodjob.article.domain.subComment.entity.SubComment;
import com.goodjob.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByArticleAndMember(Article article, Member member);

    Optional<Likes> findByCommentAndMember(Comment comment, Member member);

    Optional<Likes> findBySubCommentAndMember(SubComment subComment, Member member);
}
