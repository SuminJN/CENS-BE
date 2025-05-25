package com.handong.cens.article.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatus {
    Enabled("활성화"),
    Disabled("비활성화");

    private final String description;
}
