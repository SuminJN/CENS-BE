package my.project.crawling.Article.repository;

import my.project.crawling.Article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
