package com.goodjob.core.global.base.initData;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.repository.ArticleRepository;
import com.goodjob.core.domain.comment.repository.CommentRepository;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;
import com.goodjob.core.domain.subComment.repository.SubCommentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Deprecated
public class NotProd {

    @Bean
    CommandLineRunner initData(MemberService memberService, ArticleRepository articleRepository, CommentRepository commentRepository, SubCommentRepository subCommentRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                JoinRequestDto joinRequestDto = new JoinRequestDto();
                joinRequestDto.setUsername("test");
                joinRequestDto.setPassword("1234");
                joinRequestDto.setNickname("tester");
                joinRequestDto.setEmail("test@naver.com");

                Member member1 = memberService.join(joinRequestDto).getData();



                for(int i = 1; i <= 200; i++) {
                    Article article = Article
                            .builder()
                            .member(member1)
                            .title("테스트%s".formatted(i))
                            .content("내용테스트%s".formatted(i))
                            .viewCount(0L)
                            .commentsCount(0L)
                            .build();
                    articleRepository.save(article);
                }
            }
        };
    }
}
