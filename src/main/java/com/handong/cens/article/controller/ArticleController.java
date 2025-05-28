package com.handong.cens.article.controller;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.handong.cens.article.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticles(@RequestParam(value = "code", required = false) Integer code) {
        if (code == null) {
            return ResponseEntity.ok().body(articleService.getAllArticles());
        } else {
            return ResponseEntity.ok().body(articleService.getArticlesByCategoryCode(code));
        }
    }

    // 뉴스 기사 크롤링
    @Hidden
    @GetMapping("/save")
    public ResponseEntity<String> saveArticles() {
        articleService.saveArticles();
        return ResponseEntity.ok("SAVE OK");
    }

    // 전체 뉴스 삭제
    @Hidden
    @GetMapping("/clear")
    public ResponseEntity<String> clearArticles() {
        articleService.clearArticles();
        return ResponseEntity.ok("CLEAR OK");
    }

    @GetMapping("/summary/{articleId}")
    public ResponseEntity<String> getSummary(@PathVariable Long articleId) {
        String summary = articleService.getSummary(articleId);
        return ResponseEntity.ok().body(summary);
    }
}
