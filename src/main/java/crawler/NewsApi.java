package crawler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewsApi {
    private static final String FILE_PATH = "src/main/resources/data/articles.json";
    private static final String API_KEY = "ddd7b958a4ec4a1f89f21ff76c7f5a6b";

    public static void main(String[] args) {
        String[] queries = {"Blockchain%20Technology", "Cryptocurrencies", "Applications%20of%20Blockchain", "Blockchain%20Business", "Blockchain%20Security", "Blockchain%20Society"};
        List<Article> allArticles = new ArrayList<>();
        int id = 0;
        for (String query : queries) {
            String url = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + API_KEY + "&language=en";

            try {
                JsonArray articles = fetchArticles(url);

                // Process each article and add to the list
                for (int i = 0; i < articles.size(); i++) {
                    JsonObject articleJson = articles.get(i).getAsJsonObject();
                    Article processedArticle = processArticle(articleJson, query);
                    processedArticle.setId(id);
                    allArticles.add(processedArticle);
                    id++;
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Please check your network connection.");
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

    private static void writeArticlesToJson(List<Article> allArticles) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .create();
        String jsonOutput = gson.toJson(allArticles, new TypeToken<List<Article>>() {}.getType());

        // Write JSON to file
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(jsonOutput);
        }

        System.out.println("JSON file updated successfully.");
    }

    public static class LocalDateSerializer implements JsonSerializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(date));
        }
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

    private static Article processArticle(JsonObject articleJson, String query) {
        Article article = new Article();
        article.setCategories(getCategoryFromQuery(query));

        JsonObject source = articleJson.getAsJsonObject("source");
        article.setSource(source.get("name").getAsString());
        article.setAuthor(articleJson.has("author") && !articleJson.get("author").isJsonNull() ? articleJson.get("author").getAsString() : "");
        article.setTitle(articleJson.has("title") && !articleJson.get("title").isJsonNull() ? articleJson.get("title").getAsString() : "");
        article.setSummary(articleJson.has("description") && !articleJson.get("description").isJsonNull() ? articleJson.get("description").getAsString() : "");
        article.setLink(articleJson.has("url") && !articleJson.get("url").isJsonNull() ? articleJson.get("url").getAsString() : "");
        article.setDate(articleJson.has("publishedAt") && !articleJson.get("publishedAt").isJsonNull() ? String.valueOf(LocalDate.parse(articleJson.get("publishedAt").getAsString(), DateTimeFormatter.ISO_DATE_TIME)) : null);
        article.setTags("blockchain");
        // Get content from article URL
        String content = getDetails(article.getLink());
        article.setContent(content.isEmpty() ? (articleJson.has("content") && !articleJson.get("content").isJsonNull() ? articleJson.get("content").getAsString() : "") : content);

        return article;
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
            System.err.println("Error fetching details from URL: " + e.getMessage());
            return "";
        }
    }

    private static String getCategoryFromQuery(String query) {
        return switch (query) {
            case "Blockchain%20Technology" -> "Blockchain Technology";
            case "Cryptocurrencies" -> "Cryptocurrencies";
            case "Applications%20of%20Blockchain" -> "Applications of Blockchain";
            case "Blockchain%20Business" -> "Blockchain Business";
            case "Blockchain%20Security" -> "Blockchain Security";
            case "Blockchain%20Society" -> "Blockchain Society";
            default -> "Other";
        };
    }
}
