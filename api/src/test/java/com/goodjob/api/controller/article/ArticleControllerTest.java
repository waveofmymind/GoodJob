//package com.goodjob.api.controller.article;
//
//import com.goodjob.api.controller.mentoring.MentoringController;
//import com.goodjob.core.domain.article.dto.request.ArticleRequestDto;
//import com.goodjob.core.domain.article.dto.response.ArticleResponseDto;
//import com.goodjob.core.domain.article.entity.Article;
//import com.goodjob.core.domain.article.mapper.ArticleMapper;
//import com.goodjob.core.domain.article.service.ArticleService;
//import com.goodjob.core.domain.file.dto.request.FileRequest;
//import com.goodjob.core.domain.file.service.FileService;
//import com.goodjob.core.domain.member.entity.Member;
//import com.goodjob.core.domain.mentoring.dto.request.MentoringRequestDto;
//import com.goodjob.core.domain.mentoring.entity.Mentoring;
//import com.goodjob.core.domain.mentoring.service.MentoringService;
//import com.goodjob.core.domain.s3.service.S3Service;
//import com.goodjob.core.global.rq.Rq;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import com.goodjob.common.rsData.RsData;
//
//import java.util.HashMap;
//
//import static com.goodjob.core.domain.member.constant.Membership.FREE;
//import static com.goodjob.core.domain.member.constant.ProviderType.GOODJOB;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ArticleControllerTest {
//    @InjectMocks
//    private ArticleController articleController;
//
//    @Mock
//    private ArticleService articleService;
//
//    @Mock
//    private S3Service s3Service;
//
//    @Mock
//    private Rq rq;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
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
//    @Test
//    @DisplayName("게시글 등록")
//    void t001() throws Exception {
//        // Given
//        Article article = createArticle();
//
//        RsData<Article> articleRsData = new RsData<>("S-1", "게시글을 성공적으로 생성하였습니다.", article);
//
//        doReturn(articleRsData).when(articleService).createArticle(isNull(), any(ArticleRequestDto.class));
//        doNothing().when(s3Service).uploadFile(any(Article.class), any(HashMap.class));
//
//        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
//
//        // When
//        ResultActions resultActions = mockMvc
//                .perform(multipart("/article/create")
//                        .file(mockFile)
//                        .param("title", "title")
//                        .param("content", "content")
//                        .param("category", "1")
//                )
//                .andDo(MockMvcResultHandlers.print());
//
//        // Then
//        resultActions
//                .andExpect(handler().handlerType(ArticleController.class))
//                .andExpect(handler().methodName("createArticle"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/article/detail/{id}"));
//    }
//
//    @Test
//    @DisplayName("게시글 수정")
//    void t002() throws Exception {
//        // Given
//        Article article = createArticle();
//
//        RsData<Article> articleRsData = new RsData<>("S-1", "게시글이 수정되었습니다.", article);
//
//        doReturn(articleRsData).when(articleService).updateArticle(isNull(), anyLong(), any(ArticleRequestDto.class));
//        doNothing().when(s3Service).uploadFile(any(Article.class), any(HashMap.class));
//        doNothing().when(s3Service).deleteFiles(any(Article.class), any(HashMap.class));
//        doReturn("redirect:/article/detail/1").when(rq).redirectWithMsg(any(String.class), any(RsData.class));
//
//        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
//
//        // When
//        ResultActions resultActions = mockMvc
//                .perform(multipart("/article/update/1")
//                        .file(mockFile)
//                        .param("title", "title change")
//                        .param("content", "content")
//                        .param("category", "1")
//                )
//                .andDo(MockMvcResultHandlers.print());
//
//        // Then
//        resultActions
//                .andExpect(handler().handlerType(ArticleController.class))
//                .andExpect(handler().methodName("updateArticle"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/article/detail/{id}"));
//    }
//
//    @Test
//    @DisplayName("게시글 삭제")
//    void t003() throws Exception {
//        // Given
//        Article article = createArticle();
//        RsData<Article> articleRsData = new RsData<>("S-1", "게시글이 삭제되었습니다.", article);
//
//        doReturn(articleRsData).when(articleService).deleteArticle(isNull(), anyLong());
//        doReturn("redirect:/article/list/0").when(rq).redirectWithMsg(any(String.class), any(RsData.class));
//
//        // When
//        ResultActions resultActions = mockMvc
//                .perform(get("/article/delete/1")
//                )
//                .andDo(MockMvcResultHandlers.print());
//
//        // Then
//        resultActions
//                .andExpect(handler().handlerType(ArticleController.class))
//                .andExpect(handler().methodName("deleteArticle"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/article/list/**"));
//    }
//}
