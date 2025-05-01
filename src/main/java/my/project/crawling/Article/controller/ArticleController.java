package my.project.crawling.Article.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.project.crawling.Article.service.ArticleService;
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
