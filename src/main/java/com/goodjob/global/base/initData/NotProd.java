package com.goodjob.global.base.initData;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.member.entity.Member;
import com.goodjob.domain.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@Profile("local")
public class NotProd {

    @Bean
    CommandLineRunner initData(MemberRepository memberRepository, ArticleRepository articleRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member member1 = Member
                        .builder()
                        .account("test-account")
                        .password("1234")
                        .nickname("test")
                        .build();

                memberRepository.save(member1);


                Article article1 = Article
                        .builder()
                        .createDate(LocalDateTime.now())
                        .modifiedDate(LocalDateTime.now())
                        .title("테스트1")
                        .content("내용테스트1")
                        .likeCount(0L)
                        .viewCount(0L)
                        .build();

                articleRepository.save(article1);
            }
        };
    }
}
