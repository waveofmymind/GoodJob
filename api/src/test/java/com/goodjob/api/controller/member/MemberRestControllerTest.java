package com.goodjob.api.controller.member;

import com.goodjob.member.entity.Member;
import com.goodjob.member.repository.MemberRepository;
import com.goodjob.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder()
                .username("test")
                .password("1234")
                .nickname("tester")
                .email("test@test.com")
                .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원가입 - 아이디 확인 성공")
    void checkDuplicateUsernameSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/username")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "tester1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateUsername"))
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("사용 가능한 아이디입니다."));
    }

    @Test
    @DisplayName("회원가입 - 아이디 확인 실패 - 글자제한")
    void checkDuplicateUsernameFail_InvalidUsernameLength() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/username")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "tes")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateUsername"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("4자 이상 입력하세요."));
    }

    @Test
    @DisplayName("회원가입 - 아이디 확인 실패 - 아이디중복")
    void checkDuplicateUsernameFail_DuplicateUsername() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/username")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("username", "test")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateUsername"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("이미 사용중인 아이디입니다."));
    }

    @Test
    @DisplayName("회원가입 - 닉네임 확인 성공")
    void checkDuplicateNicknameSuccess() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/nickname")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "tester1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateNickname"))
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").value("사용 가능한 닉네임입니다."));
    }

    @Test
    @DisplayName("회원가입 - 닉네임 확인 실패 - 글자제한")
    void checkDuplicateUsernameFail_InvalidNicknameLength() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/nickname")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "t")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateNickname"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("2자 이상 입력하세요."));
    }

    @Test
    @DisplayName("회원가입 - 닉네임 확인 실패 - 글자제한")
    void checkDuplicateUsernameFail_DuplicateNickname() throws Exception {
        // WHEN
        ResultActions resultActions = mockMvc
                .perform(post("/member/join/valid/nickname")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("nickname", "tester")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberRestController.class))
                .andExpect(handler().methodName("checkDuplicateNickname"))
                .andExpect(jsonPath("$.resultCode").value("F-1"))
                .andExpect(jsonPath("$.msg").value("이미 사용중인 닉네임입니다."));
    }
}