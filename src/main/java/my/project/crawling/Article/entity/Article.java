package my.project.crawling.Article.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article", schema = "crawling")
public class Article {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    private String category;

    private String title;

    @Column(length = 50000)
    private String content;

    private String date;

}
