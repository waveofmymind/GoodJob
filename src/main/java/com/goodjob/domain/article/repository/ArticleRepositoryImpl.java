package com.goodjob.domain.article.repository;

import com.beust.jcommander.internal.Nullable;
import com.goodjob.domain.article.dto.response.ArticleResponseDto;
import com.goodjob.domain.article.entity.Article;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.goodjob.domain.article.entity.QArticle.article;
import static com.goodjob.domain.likes.entity.QLikes.likes;
import static com.goodjob.domain.member.entity.QMember.member;
import static com.goodjob.domain.hashTag.entity.QHashTag.hashTag;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findQslBySortCode(int sortCode, String category, String kw) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();



        builder.and(article.isDeleted.eq(false));

        switch(sortCode) {
            case 2:
                orderSpecifiers.add(article.viewCount.desc());
                orderSpecifiers.add(article.id.desc());
                break;
            case 3:
                orderSpecifiers.add(article.likesList.size().desc());
                orderSpecifiers.add(article.id.desc());
                break;
            default:
                orderSpecifiers.add(article.id.desc());
                break;
        }
        switch(category) {
            case "내용":
                builder.or(article.content.contains(kw));
                break;
            case "제목+내용":
                builder.or(article.title.contains(kw));
                builder.or(article.content.contains(kw));
                break;
            case "글쓴이":
                builder.or(member.nickname.contains(kw));
                break;
            case "해시태그":
                builder.or(hashTag.keyword.content.toLowerCase().contains(kw));
            default:
                builder.or(article.title.contains(kw));
                break;
        }
        return jpaQueryFactory
                .selectFrom(article)
                .innerJoin(article.member, member)
                .leftJoin(article.hashTagList, hashTag).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    @Override
    public Optional<Article> findQslById(Long id) {
        Article articleTmp =  jpaQueryFactory.selectFrom(article)
                .leftJoin(article.hashTagList, hashTag).fetchJoin()
                .where(article.id.eq(id))
                .fetchOne();


        return Optional.ofNullable(articleTmp);
    }
}