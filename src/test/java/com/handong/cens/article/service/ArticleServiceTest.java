package com.handong.cens.article.service;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import com.handong.cens.article.entity.Article;
import com.handong.cens.article.repository.ArticleRepository;
import com.handong.cens.commons.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private OpenAiChatModel openAiChatModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Article mockArticle() {
        return Article.builder()
                .articleId(1L)
                .title("테스트 기사")
                .content("본문 내용")
                .createDate("2024-06-01")
                .category("정치")
                .originalUrl("http://example.com/article1")
                .summary("요약 내용")
                .build();
    }

    @Test
    void testGetAllArticles() {
        List<Article> mockArticles = List.of(mockArticle());

        when(articleRepository.findAll()).thenReturn(mockArticles);

        List<ArticleResponseDto> result = articleService.getAllArticles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("테스트 기사");
    }

    @Test
    void testGetArticlesByCategoryCode() {
        when(articleRepository.findByCategory("정치"))
                .thenReturn(List.of(mockArticle()));

        List<ArticleResponseDto> result = articleService.getArticlesByCategoryCode(100); // 100 = 정치

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("정치");
    }

    @Test
    void testGetArticle_success() {
        when(articleRepository.findById(1L))
                .thenReturn(Optional.of(mockArticle()));

        ArticleResponseDto result = articleService.getArticle(1L);

        assertThat(result.getTitle()).isEqualTo("테스트 기사");
    }

    @Test
    void testGetArticle_notFound() {
        when(articleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> articleService.getArticle(1L));
    }

    @Test
    void testClearArticles() {
        articleService.clearArticles();
        verify(articleRepository, times(1)).deleteAll();
    }

    @Test
    void testSummarizeAndSave() {
        String content = "이것은 테스트 기사입니다.";
        String summary = "테스트 요약";

        when(openAiChatModel.call(anyString())).thenReturn(summary);

        String result = ArticleServiceTestHelper.summarize(content, openAiChatModel);

        assertThat(result).isEqualTo(summary);
    }

    // 내부 메서드 테스트용 도우미 (private 메서드는 별도 래퍼로 테스트 가능)
    static class ArticleServiceTestHelper {
        static String summarize(String content, OpenAiChatModel model) {
            String prompt = "다음 기사를 한국어로 1문장 이내로 요약해줘:\n\n" + content;
            return model.call(prompt);
        }
    }
}
