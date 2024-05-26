package crawler;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewsApi {
    private final static String FILE_PATH = "src/main/resources/data/articles.json";
    private final static String API_KEY = "10398e3c397d4c438a6c16e3f88785d1";
    public static void main(String[] args) throws Exception {
        String HistoryFilePath = "src/main/resources/data/History.json";
        Gson gsonForHistory = new Gson();
        Object emptyList = Collections.emptyList();
        try (FileWriter fileWriter = new FileWriter(HistoryFilePath)) {
            gsonForHistory.toJson(emptyList, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] queries = {"Blockchain%20Technology", "Cryptocurrencies", "Applications%20of%20Blockchain", "Blockchain%20Business", "Blockchain%20Security", "Blockchain%20Society"};
        List<JsonObject> allArticles = new ArrayList<>();

        for (String query : queries) {
            String url = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + API_KEY + "&language=en";

            try {
                JsonArray articles = fetchArticles(url);
                //Process each article and add to the list
                for (int i = 0; i < articles.size(); i++) {
                    JsonObject article = articles.get(i).getAsJsonObject();
                    JsonObject processedArticle = processArticle(article, query);
                    allArticles.add(processedArticle);
                }
            } catch (IOException | InterruptedException e) {
                throw new Exception("Please check your network connection. ");
            }
        }
        try {
            // Write all articles to JSON file
            writeArticlesToJson(allArticles);
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeArticlesToJson(List<JsonObject> allArticles) throws IOException {
        Gson gson = new Gson();
        String jsonOutput = gson.toJson(allArticles);


        // Write JSON to file
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(jsonOutput);
        }

        System.out.println("JSON file created successfully.");
    }
    private static JsonArray fetchArticles(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Parse JSON
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

        // Extract array from JSON
        return jsonResponse.getAsJsonArray("articles");
    }
    private static JsonObject processArticle(JsonObject article, String query) {
        JsonObject processedArticle = new JsonObject();
        processedArticle.addProperty("category", getCategoryFromQuery(query));

        JsonObject source = article.getAsJsonObject("source");
        processedArticle.addProperty("source", source.get("name").getAsString());
        processedArticle.addProperty("author", article.has("author") && !article.get("author").isJsonNull() ? article.get("author").getAsString() : "");
        processedArticle.addProperty("title", article.has("title") && !article.get("title").isJsonNull() ? article.get("title").getAsString() : "");
        processedArticle.addProperty("summary", article.has("description") && !article.get("description").isJsonNull() ? article.get("description").getAsString() : "");
        processedArticle.addProperty("link", article.has("url") && !article.get("url").isJsonNull() ? article.get("url").getAsString() : "");
        processedArticle.addProperty("date", article.has("publishedAt") && !article.get("publishedAt").isJsonNull() ? article.get("publishedAt").getAsString() : "");
        processedArticle.addProperty("tags","blockchain");
        // Get content from article URL
        String content = getDetails(processedArticle.get("link").getAsString());
        if (content == "") {
            processedArticle.addProperty("content", article.has("content") && !article.get("content").isJsonNull() ? article.get("content").getAsString() : "");
        }
        processedArticle.addProperty("content", content);

        return processedArticle;
    }
    private static String getDetails(String url) {
        try {
            // Connect to URL
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
            // Get paragraph elements
            Elements paragraphs = doc.select("p");
            StringBuilder contentBuilder = new StringBuilder();
            for (Element paragraph : paragraphs) {
                contentBuilder.append(paragraph.text()).append("\n");
            }
            return contentBuilder.toString().trim();
        } catch (IllegalArgumentException e) {
            System.err.println("The supplied URL, '" + url + "', is malformed. Make sure it is an absolute URL, and starts with 'http://' or 'https://'.");
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static String getCategoryFromQuery(String query) {
        switch (query) {
            case "Blockchain%20Technology":
                return "Blockchain Technology";
            case "Cryptocurrencies":
                return "Cryptocurrencies";
            case "Applications%20of%20Blockchain":
                return "Applications of Blockchain";
            case "Blockchain%20Business":
                return "Blockchain Business";
            case "Blockchain%20Security":
                return "Blockchain Security";
            case "Blockchain%20Society":
                return "Blockchain Society";
            default:
                return "Other";
        }
    }
}
