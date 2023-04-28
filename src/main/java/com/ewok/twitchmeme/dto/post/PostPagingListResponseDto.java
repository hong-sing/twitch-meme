package com.ewok.twitchmeme.dto.post;

import com.ewok.twitchmeme.domain.post.Post;
import com.ewok.twitchmeme.dto.PostResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostPagingListResponseDto {

    private List<PostResponseDto> postsList;    //조회된 데이터
    private Long totalPage; //전체 페이지 수
    private Long totalCount;    //전체 게시글 수

    @Builder
    public PostPagingListResponseDto(List<Post> postsList, Long totalPage, Long totalCount) {
        this.postsList = postsList.stream().map(PostResponseDto::new).collect(Collectors.toList());
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }

}
