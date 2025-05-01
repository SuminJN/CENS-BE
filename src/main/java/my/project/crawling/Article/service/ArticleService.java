package my.project.crawling.Article.service;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import my.project.crawling.Article.entity.Article;
import my.project.crawling.Article.repository.ArticleRepository;
import my.project.crawling.commons.entity.Category;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public void saveArticles() throws IOException {

        // {100: 정치, 101: 경제, 102: 사회, 103: 생활/문화, 104: 세계, 105: IT/과학}
        Category category = Category.IT_SCIENCE;

        String url = "https://news.naver.com/section/" + category.getCode(); // 크롤링 할 사이트 URL
        String headlineNewsSelector = "li.sa_item._SECTION_HEADLINE:not(.is_blind) a.sa_text_title._NLOG_IMPRESSION"; // 뉴스 제목 및 링크 선택자
        String dateSelector = "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME";
        String articleSelector = "article#dic_area"; // 뉴스 본문 선택자
        Document doc = null;

        // 지정된 URL로 연결하여 HTML 문서를 가져옴
        doc = Jsoup.connect(url).get();

        // 지정된 선택자를 사용하여 뉴스 제목 및 링크 요소를 선택
        Elements titles = doc.select(headlineNewsSelector);

        // 선택된 뉴스의 개수를 출력
        System.out.println(titles.size() + "개의 " + category.getDescription() + " 헤드라인 뉴스가 있습니다. \n");

        // 각 뉴스 제목 요소를 순회하며 처리
        for (Element element : titles) {

            // 뉴스 링크와 제목을 출력
            String articleLink = element.attr("href"); // 뉴스 링크

            System.out.println(articleLink);
            System.out.println("제목: " + element.text()); // 뉴스 제목

            // 뉴스 링크로 연결하여 날짜를 가져옴
            Elements date = Jsoup.connect(articleLink).get().select(dateSelector);

            // 뉴스 날짜를 출력
            System.out.print("기사 날짜: ");
            System.out.println(date.text()); // 뉴스 날짜

            // 뉴스 링크로 연결하여 본문을 가져옴
            Elements articles = Jsoup.connect(articleLink).get().select(articleSelector);

            // 뉴스 본문을 출력
            System.out.print("기사 본문: ");
            System.out.println(articles.text()); // 뉴스 본문

            // 뉴스 간 구분을 위해 빈 줄 출력
            System.out.println();

            // Article 객체 생성 및 저장
            Article article = Article.builder()
                    .title(element.text())
                    .content(articles.text())
                    .date(date.text())
                    .category(category.getDescription())
                    .build();

            // Article 객체를 데이터베이스에 저장
            articleRepository.save(article);
        }
    }
}
