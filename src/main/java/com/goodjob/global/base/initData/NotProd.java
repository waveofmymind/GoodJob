package com.goodjob.global.base.initData;

import com.goodjob.domain.article.entity.Article;
import com.goodjob.domain.article.repository.ArticleRepository;
import com.goodjob.domain.member.dto.request.JoinRequestDto;
import com.goodjob.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@Profile("local")
public class NotProd {

    @Bean
    CommandLineRunner initData(MemberService memberService, ArticleRepository articleRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                JoinRequestDto joinRequestDto = new JoinRequestDto();
                joinRequestDto.setUsername("test");
                joinRequestDto.setPassword("1234");
                joinRequestDto.setNickname("tester");
                joinRequestDto.setEmail("test@naver.com");

                memberService.join(joinRequestDto);


                for(int i = 1; i <= 200; i++) {
                    Article article = Article
                            .builder()
                            .title("테스트%s".formatted(i))
                            .content("내용테스트%s".formatted(i))
                            .viewCount(0L)
                            .build();
                    articleRepository.save(article);
                }
            }
        };
    }
}
