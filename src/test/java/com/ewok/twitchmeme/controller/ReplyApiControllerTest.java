package com.ewok.twitchmeme.controller;

import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.domain.post.PostRepository;
import com.ewok.twitchmeme.domain.post.Reply;
import com.ewok.twitchmeme.domain.post.ReplyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReplyApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private MockMvc mvc;

    private Post post;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        post = Post.builder()
                .title("제목")
                .summary("요약")
                .content("내용")
                .broadcastId("aaa123")
                .build();

        postRepository.save(post);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 댓글_삭제() throws Exception {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        List<Reply> replies = new ArrayList<>();
        Reply savedReply = replyRepository.save(Reply.builder()
                .post(list.get(0))
                .remove('N')
                .replies(replies)
                .content("댓글내용")
                .build());

        Long deletedId = savedReply.getId();
        String url = "http://localhost:" + port + "/api/v1/reply/" + deletedId;

        //when
        mvc.perform(put(url)).andExpect(status().isOk());

        //then
        assertThat(replyRepository.findById(deletedId)).isEmpty();

    }

    @Test
    @WithMockUser(roles = "USER")
    void 댓글_내용_수정() throws Exception {
        //given
        List<Post> list = postRepository.findByBroadcastId("aaa123");
        Reply savedReply = replyRepository.save(Reply.builder()
                .post(list.get(0))
                .remove('N')
                .content("댓글내용")
                .build());

        Long updateId = savedReply.getId();

        String updateContent = "수정된 댓글 내용";
        String url = "http://localhost:" + port + "/api/v1/reply/" + updateId;

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updateContent))
                .andExpect(status().isOk());

        //then
        Reply updatedReply = replyRepository.findById(updateId).get();
        assertThat(updatedReply.getContent()).isEqualTo(updateContent);
    }
}