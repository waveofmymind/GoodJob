package com.goodjob.core.global.base.initData;

import com.goodjob.core.domain.article.entity.Article;
import com.goodjob.core.domain.article.repository.ArticleRepository;
import com.goodjob.core.domain.comment.repository.CommentRepository;
import com.goodjob.core.domain.member.dto.request.JoinRequestDto;
import com.goodjob.core.domain.member.entity.Member;
import com.goodjob.core.domain.member.service.MemberService;

import com.goodjob.core.domain.subComment.repository.SubCommentRepository;
import com.goodjob.resume.domain.Contents;
import com.goodjob.resume.domain.Prediction;
import com.goodjob.resume.domain.ServiceType;
import com.goodjob.resume.domain.Titles;
import com.goodjob.resume.ports.outs.PredictionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@Profile("local")
public class NotProd {

    @Bean
    CommandLineRunner initData(MemberService memberService, ArticleRepository articleRepository, PredictionRepository predictionRepository, CommentRepository commentRepository, SubCommentRepository subCommentRepository) {
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

                for (int i = 0; i <= 6; i++) {
                    Prediction prediction = Prediction.builder()
                            .serviceType(ServiceType.EXPECTED_QUESTION)
                            .titles(Titles.of(List.of("대주제와 소주제가 매끄럽게 이어지지 않습니다.", "프로젝트 경험을 더 자세히 작성해주세요.")))
                            .contents(Contents.of(List.of("모델 조언1", "모델 조언2")))
                            .memberId(1L).build();
                    predictionRepository.save(prediction);
                }


                for (int i = 1; i <= 200; i++) {
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
