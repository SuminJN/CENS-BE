package com.handong.cens.commons.entity;

import lombok.Getter;

@Getter
public enum Category {

    // 100: 정치, 101: 경제, 102: 사회, 103: 생활/문화, 104: 세계, 105: IT/과학
    POLITICS(100, "정치"),
    ECONOMY(101, "경제"),
    SOCIETY(102, "사회"),
    CULTURE(103, "생활/문화"),
    WORLD(104, "세계"),
    IT_SCIENCE(105, "IT/과학");

    private final int code;
    private final String description;

    Category(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
