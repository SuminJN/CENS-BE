package com.handong.cens.article.service;

import java.io.IOException;
import java.util.List;

import com.handong.cens.article.dto.response.ArticleResponseDto;
import com.handong.cens.article.entity.ArticleStatus;
import com.handong.cens.commons.exception.CustomException;
import lombok.RequiredArgsConstructor;
import com.handong.cens.article.entity.Article;
import com.handong.cens.article.repository.ArticleRepository;
import com.handong.cens.commons.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.handong.cens.commons.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final OpenAiChatModel openAiChatModel;

    @Transactional
    public void saveArticles() {

        int count = 0; // 크롤링한 뉴스 개수

        // {100: 정치, 101: 경제, 102: 사회, 103: 생활/문화, 104: 세계, 105: IT/과학}
        for (Category category : Category.values()) {

            String url = "https://news.naver.com/section/" + category.getCode(); // 크롤링 할 사이트 URL
            String headlineNewsSelector = "li.sa_item._SECTION_HEADLINE:not(.is_blind) a.sa_text_title._NLOG_IMPRESSION"; // 뉴스 제목 및 링크 선택자
            String dateSelector = "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME";
            String modifiedDateSelector = "span.media_end_head_info_datestamp_time._ARTICLE_MODIFY_DATE_TIME";
            String articleSelector = "article#dic_area"; // 뉴스 본문 선택자
            Document doc = null;

            // 지정된 URL로 연결하여 HTML 문서를 가져옴
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
//                throw new CustomException(CRAWLING_PAGE_NOT_FOUND);
                log.info("크롤링할 페이지를 찾을 수 없습니다. URL: {}", url);
                continue; // 페이지를 찾을 수 없으면 다음 카테고리로 넘어감
            }

            // 지정된 선택자를 사용하여 뉴스 제목 및 링크 요소를 선택
            Elements titles = doc.select(headlineNewsSelector);

            // 각 뉴스 제목 요소를 순회하며 처리
            for (Element element : titles) {

                boolean isEnabled = true;

                // 뉴스 링크와 제목을 출력
                String articleLink = element.attr("href"); // 뉴스 링크

                String title = element.text();
                title = title.replace("\"", "");

                String content = null;
                try {
                    content = Jsoup.connect(articleLink).get().select(articleSelector).text();
                    content = content.replace("\"", "");
                } catch (IOException e) {
//                    throw new CustomException(ARTICLE_CONTENT_NOT_FOUND);
                    log.info("기사 본문을 찾을 수 없습니다. 기사 링크: {}", articleLink);
                    content = "본문 내용 없음"; // 본문 내용이 없을 경우 기본값 설정
                    isEnabled = false;
                }

                String date = null;
                try {
                    date = Jsoup.connect(articleLink).get().select(dateSelector).text();
                } catch (IOException e) {
//                    throw new CustomException(ARTICLE_DATE_NOT_FOUND);
                    log.info("기사 날짜를 찾을 수 없습니다. 기사 링크: {}", articleLink);
                }

                // 수정된 날짜가 있는 경우, 수정된 날짜를 가져옴
                String modifiedDate = null;
                try {
                    modifiedDate = Jsoup.connect(articleLink).get().select(modifiedDateSelector).text();
                } catch (IOException e) {
                    log.info("기사 수정 날짜를 찾을 수 없습니다. 기사 링크: {}", articleLink);
                }

                // Article 객체 생성 및 저장
                Article article = Article.builder()
                        .articleStatus(isEnabled ? ArticleStatus.Enabled : ArticleStatus.Disabled)
                        .title(title)
                        .content(content)
                        .createDate(date)
                        .modifiedDate(modifiedDate)
                        .category(category.getDescription())
                        .originalUrl(articleLink)
//                        .summary("뉴스 요약 테스트 중 입니다.") // 요약은 나중에 OpenAI API로 처리
                        .summary(summarizeAndSave(content))
                        .build();

                // Article 객체를 데이터베이스에 저장
                articleRepository.save(article);

                log.info("{} - Article saved: {}",  ++count, article.getTitle());
            }
        }
    }

    public String summarizeAndSave(String content) {
        String prompt = "다음 기사를 한국어로 1문장 이내로 요약해줘:\n\n" + content;

        return openAiChatModel.call(prompt);
    }

    @Transactional
    public String getSummary(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ARTICLE_NOT_FOUND));

        String prompt = "다음 기사를 한국어로 1문장 이내로 요약해줘:\n\n"
                + article.getContent().substring(0, Math.min(article.getContent().length(), 500));

        String response = openAiChatModel.call(prompt);

        // 요약된 내용을 데이터베이스에 저장
        article.setSummary(response);

        return response;
    }

    public List<ArticleResponseDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(article -> ArticleResponseDto.builder()
                        .articleId(article.getArticleId())
                        .articleStatus(article.getArticleStatus().name())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .createDate(article.getCreateDate())
                        .modifiedDate(article.getModifiedDate())
                        .category(article.getCategory())
                        .originalUrl(article.getOriginalUrl())
                        .summary(article.getSummary())
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
                        .createDate(article.getCreateDate())
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
