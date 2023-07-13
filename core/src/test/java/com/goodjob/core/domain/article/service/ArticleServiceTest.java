//package com.goodjob.core.domain.article.service;
//
//import com.goodjob.core.domain.article.dto.request.ArticleRequestDto;
//import com.goodjob.core.domain.article.entity.Article;
//import com.goodjob.core.domain.article.mapper.ArticleMapper;
//import com.goodjob.core.domain.article.repository.ArticleRepository;
//import com.goodjob.core.domain.file.service.FileService;
//import com.goodjob.core.domain.hashTag.service.HashTagService;
//import com.goodjob.core.domain.member.entity.Member;
//import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
//import com.goodjob.core.domain.mentoring.entity.Mentoring;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static com.goodjob.core.domain.member.constant.Membership.FREE;
//import static com.goodjob.core.domain.member.constant.ProviderType.GOODJOB;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//public class ArticleServiceTest {
//    @InjectMocks
//    private ArticleService articleService;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @Mock
//    private ArticleMapper articleMapper;
//
//    @Mock
//    private HashTagService hashTagService;
//
//    @Mock
//    private FileService fileService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(articleService);
//    }
//
//    private Member singUpMember() {
//        return Member.builder()
//                .username("test")
//                .password("1234")
//                .nickname("tester")
//                .email("test@naver.com")
//                .membership(FREE)
//                .providerType(GOODJOB)
//                .build();
//    }
//
//    private Article createArticle() {
//        Member member = singUpMember();
//        return Article.builder()
//                .id(1L)
//                .member(member)
//                .title("title")
//                .content("content")
//                .isDeleted(false)
//                .category(1)
//                .build();
//    }
//
//    private ArticleRequestDto createMentoringRequestDto() {
//        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
//        articleRequestDto.setTitle("title");
//        articleRequestDto.setContent("content");
//
//        return articleRequestDto;
//    }
////
////
////        @Test
////    @DisplayName("게시글 생성")
////    void t001() {
////        // GIVEN
////        ArticleRequestDto articleRequestDto = createMentoringRequestDto();
////        Member member = singUpMember();
////
////        // WHEN
////        RsData<Mentoring> mentoringRsData = mentoringService.createMentoring(member, mentoringRequestDto);
////
////        // THEN
////        assertThat(mentoringRsData.getResultCode()).isEqualTo("S-1");
////        assertThat(mentoringRsData.getData().getTitle()).isEqualTo("title test");
////        assertThat(mentoringRsData.getData().getMember().getUsername()).isEqualTo("test");
////    }
//}
