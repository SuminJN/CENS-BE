package com.handong.cens.article.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.handong.cens.article.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public void saveArticles() {
        try {
            articleService.saveArticles();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
