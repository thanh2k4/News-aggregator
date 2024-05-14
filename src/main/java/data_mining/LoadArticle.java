package data_mining;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import crawler.Article;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadArticle {
    private static final String FILE_PATH = "src/main/resources/data/articles.csv";

    private static List<Article> articleList = new ArrayList<>();

    public static List<Article> getArticleList() {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;
            reader.readNext();
            int id = 0;
            while (( nextLine = reader.readNext()) != null){
                Article article = new Article();
                article.setId(id);
                article.setTitle(nextLine[0]);
                article.setLink(nextLine[1]);
                article.setSource(nextLine[2]);
                article.setType(nextLine[3]);
                article.setSummary(nextLine[4]);
                article.setContent(nextLine[5]);
                article.setDate( LocalDate.parse(nextLine[6] , DateTimeFormatter.ofPattern("yyyy-MM-dd")) );
                article.setTags(nextLine[7]);
                article.setAuthor(nextLine[8]);
                article.setCategories(nextLine[9]);
                articleList.add(article);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return  articleList;
    }
}
