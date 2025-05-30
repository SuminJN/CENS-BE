package com.handong.cens.article.controller;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Article", description = "뉴스 기사 관련 API")
public class ArticleController {

    private final ArticleService articleService;

//    // 뉴스 기사 크롤링
//    @Hidden
//    @GetMapping("/save")
//    public ResponseEntity<String> saveArticles() {
//        articleService.saveArticles();
//        return ResponseEntity.ok("SAVE OK");
//    }
//
//    // 전체 뉴스 삭제
//    @Hidden
//    @GetMapping("/clear")
//    public ResponseEntity<String> clearArticles() {
//        articleService.clearArticles();
//        return ResponseEntity.ok("CLEAR OK");
//    }

    @Operation(summary = "뉴스 목록 조회", description = "카테고리 코드가 없으면 전체 기사, 있으면 해당 카테고리의 기사만 반환합니다.")
    @Parameters({
            @Parameter(
                    name = "code",
                    description = """
                            카테고리 코드:
                            - 100: 정치
                            - 101: 경제
                            - 102: 사회
                            - 103: 생활/문화
                            - 104: 세계
                            - 105: IT/과학
                            """,
                    example = "101"
            )
    })
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticles(@RequestParam(value = "code", required = false) Integer code) {
        if (code == null) {
            return ResponseEntity.ok().body(articleService.getAllArticles());
        } else {
            return ResponseEntity.ok().body(articleService.getArticlesByCategoryCode(code));
        }
    }

    @Operation(summary = "뉴스 요약 조회", description = "기사 ID를 기반으로 해당 뉴스의 요약을 반환합니다.")
    @GetMapping("/summary/{articleId}")
    public ResponseEntity<String> getSummary(@PathVariable Long articleId) {
        String summary = articleService.getSummary(articleId);
        return ResponseEntity.ok().body(summary);
    }
}
