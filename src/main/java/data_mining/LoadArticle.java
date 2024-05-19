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
        List<Article> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;
            reader.readNext();
            int id = 0;
            while (( nextLine = reader.readNext()) != null){
                Article article = new Article();
                article.setId(id);
                article.setLink(nextLine[0]);
                article.setSource(nextLine[1]);
                article.setTitle(nextLine[2]);
                article.setSummary(nextLine[3]);
                article.setContent(nextLine[4]);
                article.setDate( LocalDate.parse(nextLine[5].substring(0,10) , DateTimeFormatter.ofPattern("yyyy-MM-dd")) );
                article.setAuthor(nextLine[6]);
                article.setType(nextLine[7]);
                article.setTags(nextLine[8]);
                article.setCategories(nextLine[9]);
                result.add(article);
                id++;
            }
            articleList = result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return  articleList;
    }
}
