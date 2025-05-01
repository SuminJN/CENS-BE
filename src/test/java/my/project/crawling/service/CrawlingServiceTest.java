package my.project.crawling.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class CrawlingServiceTest {

    @Autowired
    private SeleniumService crawlingService;

    @Test
    void crawl() throws InterruptedException {
        List<CrawlingInfo> crawlingInfos = crawlingService.getCrawlingInfos();
        log.info(crawlingInfos.toString());
    }
}