package com.goodjob.domain.article.repository;

import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.goodjob.domain.article.entity.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findQslBySortCode(int sortCode) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        switch(sortCode) {
            case 2:
                orderSpecifiers.add(article.viewCount.desc());
                orderSpecifiers.add(article.id.desc());
                break;
            case 3:
                orderSpecifiers.add(article.likesCount.desc());
                orderSpecifiers.add(article.id.desc());
                break;
            default:
                orderSpecifiers.add(article.id.desc());
                break;
        }

        return jpaQueryFactory
                .selectFrom(article)
                .where(article.isDeleted.eq(false))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

}