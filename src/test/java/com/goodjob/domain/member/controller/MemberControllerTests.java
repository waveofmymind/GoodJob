package com.goodjob.domain.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
class MemberControllerTests {
//    @Autowired
//    private MockMvc mvc;

    // TODO: 로그인 처리
//    @Test
//    @DisplayName("로그인 처리")
//    void t01() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(get("/member/login"))
//                .andDo(print());
//
//        // THEN
//    }
}