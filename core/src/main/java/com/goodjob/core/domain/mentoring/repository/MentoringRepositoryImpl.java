package com.goodjob.core.domain.mentoring.repository;

import com.goodjob.core.domain.mentoring.entity.Mentoring;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.goodjob.core.domain.article.entity.QArticle.article;
import static com.goodjob.core.domain.mentoring.entity.QMentoring.mentoring;
import static com.goodjob.member.entity.QMember.member;


@RequiredArgsConstructor
public class MentoringRepositoryImpl implements MentoringRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Mentoring> findQslBySearch(String category, String kw) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        BooleanBuilder builder = new BooleanBuilder();

        orderSpecifiers.add(mentoring.id.desc());

        switch(category) {
            case "직무":
                builder.and(mentoring.job.contains(kw));
                break;
            case "경력":
                builder.and(mentoring.career.contains(kw));
                break;
            case "현직":
                builder.and(mentoring.currentJob.contains(kw));
                break;
            default:
                builder.and(mentoring.title.contains(kw));
                break;
        }
        return jpaQueryFactory
                .selectFrom(mentoring)
                .innerJoin(mentoring.member, member)
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }
}