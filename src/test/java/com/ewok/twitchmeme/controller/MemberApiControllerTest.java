package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.domain.member.Member;
import com.ewok.twitchmeme.domain.member.MemberRepository;
import com.ewok.twitchmeme.domain.member.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebApplicationContext context;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;
    private Member member;
    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        member = Member.builder()
                .picture("이미지")
                .role(Role.USER)
                .nickname("닉네임")
                .twitchId(999L)
                .build();
        memberRepository.save(member);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 회원정보조회() throws Exception {
        //given
        String url = "http://localhost" + port + "/api/v1/member";

        //when
        MvcResult result = mvc.perform(get(url).header("Accept-Charset", "UTF-8")).andExpect(status().isOk()).andReturn();
        HttpServletResponse response = result.getResponse();
        response.setCharacterEncoding("UTF-8"); //인코딩 설정해야 한글 안깨짐

        //then
        List list = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        LinkedHashMap map = (LinkedHashMap) list.get(0);
        assertThat(map.get("nickname")).isEqualTo("닉네임");

    }
}