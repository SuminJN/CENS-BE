package com.handong.cens.article.service;

import java.io.IOException;
import java.util.List;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import com.handong.cens.commons.exception.CustomException;
import lombok.RequiredArgsConstructor;
import com.handong.cens.article.entity.Article;
import com.handong.cens.article.repository.ArticleRepository;
import com.handong.cens.commons.entity.Category;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.handong.cens.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public void saveArticles() {

        // {100: 정치, 101: 경제, 102: 사회, 103: 생활/문화, 104: 세계, 105: IT/과학}
        for (Category category : Category.values()) {

            String url = "https://news.naver.com/section/" + category.getCode(); // 크롤링 할 사이트 URL
            String headlineNewsSelector = "li.sa_item._SECTION_HEADLINE:not(.is_blind) a.sa_text_title._NLOG_IMPRESSION"; // 뉴스 제목 및 링크 선택자
            String dateSelector = "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME";
            String articleSelector = "article#dic_area"; // 뉴스 본문 선택자
            Document doc = null;

            // 지정된 URL로 연결하여 HTML 문서를 가져옴
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new CustomException(CRAWLING_PAGE_NOT_FOUND);
            }

            // 지정된 선택자를 사용하여 뉴스 제목 및 링크 요소를 선택
            Elements titles = doc.select(headlineNewsSelector);

            // 각 뉴스 제목 요소를 순회하며 처리
            for (Element element : titles) {

                // 뉴스 링크와 제목을 출력
                String articleLink = element.attr("href"); // 뉴스 링크

                String title = element.text();

                String content = null;
                try {
                    content = Jsoup.connect(articleLink).get().select(articleSelector).text();
                } catch (IOException e) {
                    throw new CustomException(ARTICLE_CONTENT_NOT_FOUND);
                }

                String date = null;
                try {
                    date = Jsoup.connect(articleLink).get().select(dateSelector).text();
                } catch (IOException e) {
                    throw new CustomException(ARTICLE_DATE_NOT_FOUND);
                }

                // Article 객체 생성 및 저장
                Article article = Article.builder()
                        .title(title)
                        .content(content)
                        .date(date)
                        .category(category.getDescription())
                        .originalUrl(articleLink)
                        .build();

                // Article 객체를 데이터베이스에 저장
                articleRepository.save(article);
            }
        }
    }

    public List<ArticleResponseDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(article -> ArticleResponseDto.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .date(article.getDate())
                        .category(article.getCategory())
                        .build())
                .toList();
    }

    public List<ArticleResponseDto> getArticlesByCategoryCode(int code) {
        List<Article> articles = articleRepository.findByCategory(getDescriptionByCode(code));

        return articles.stream()
                .map(article -> ArticleResponseDto.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .date(article.getDate())
                        .category(article.getCategory())
                        .build())
                .toList();
    }

    private String getDescriptionByCode(int code) {
        for (Category category : Category.values()) {
            if (category.getCode() == code) {
                return category.getDescription();
            }
        }
        throw new CustomException(MISSING_CATEGORY_CODE);
    }

    @Transactional
    public void clearArticles() {
        articleRepository.deleteAll();
    }
}
