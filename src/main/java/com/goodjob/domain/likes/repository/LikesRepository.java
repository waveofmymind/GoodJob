package com.goodjob.domain.likes.repository;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}
