package com.handong.cens.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleResponseDto {

    private Long articleId;
    private String title;
    private String content;
    private String date;
    private String category;
}
