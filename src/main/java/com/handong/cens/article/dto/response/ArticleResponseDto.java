package com.handong.cens.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleResponseDto {

    private Long articleId;

    private String articleStatus; // 활성화 or 비활성화

    private String category;

    private String title;

    private String content;

    private String date;

    private String originalUrl;

    private String summary;
}
