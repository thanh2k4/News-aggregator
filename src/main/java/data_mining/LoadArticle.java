package data_mining;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import crawler.Article;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoadArticle {
    private static final String FILE_PATH = "src/main/resources/data/articles.json";

    private static List<Article> articleList = new ArrayList<>();

    public static List<Article> getArticleList() {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(FILE_PATH)) {
                Type articleListType = new TypeToken<List<Article>>() {}.getType();
                List<Article> result = gson.fromJson(reader, articleListType);
                articleList = result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  articleList;
    }
}
