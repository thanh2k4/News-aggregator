package data_mining;

import com.opencsv.bean.CsvToBeanBuilder;
import crawler.Article;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LoadArticle {
    private static final String FILE_PATH = "src/main/resources/data/articles.csv";

    private static List<Article> articleList;

    public static List<Article> getArticleList() {
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            List<Article> articleList = new CsvToBeanBuilder<Article>(fileReader)
                    .withType(Article.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  articleList;
    }
}
