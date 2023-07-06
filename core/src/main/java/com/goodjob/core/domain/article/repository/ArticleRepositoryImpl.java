package com.goodjob.core.domain.article.repository;

import com.goodjob.core.domain.article.entity.Article;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.goodjob.core.domain.article.entity.QArticle.article;
import static com.goodjob.core.domain.hashTag.entity.QHashTag.hashTag;
import static com.goodjob.core.domain.member.entity.QMember.member;


@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findQslBySortCode(int id, int sortCode, String category, String kw) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(article.isDeleted.eq(false));

        switch(id) {
            case 1:
                builder.and(article.category.eq(1));
                break;
            case 2:
                builder.and(article.category.eq(2));
                break;
            case 3:
                builder.and(article.category.eq(3));
                break;
            case 4:
                builder.and(article.category.eq(4));
                break;
            case 5:
                builder.and(article.category.eq(5));
                break;
            case 6:
                builder.and(article.category.eq(6));
                break;
            case 7:
                builder.and(article.category.eq(7));
                break;
            case 8:
                builder.and(article.category.eq(8));
                break;
            default:
                break;
        }

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
                builder.and(article.content.contains(kw));
                break;
            case "제목+내용":
                builder.and(article.title.contains(kw));
                builder.or(article.content.contains(kw));
                break;
            case "글쓴이":
                builder.and(member.nickname.contains(kw));
                break;
            case "해시태그":
                builder.and(article.hashTagList.any().keyword.content.toLowerCase().contains(kw));
                break;
            default:
                builder.and(article.title.contains(kw));
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