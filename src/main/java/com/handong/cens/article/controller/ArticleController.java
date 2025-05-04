package com.handong.cens.article.controller;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.handong.cens.article.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/save")
    public void saveArticles() {
        try {
            articleService.saveArticles();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticles(@RequestParam(value = "code", required = false) Integer code) {
        if (code == null) {
            return ResponseEntity.ok().body(articleService.getAllArticles());
        } else {
            return ResponseEntity.ok().body(articleService.getArticlesByCategoryCode(code));
        }
    }
}
