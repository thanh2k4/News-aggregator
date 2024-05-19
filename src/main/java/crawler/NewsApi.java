package crawler;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;


public class NewsApi {
    private final static String FILE_PATH = "src/main/resources/data/articles.csv";
    public static void main(String[] args) {
        String url = "https://newsapi.org/v2/everything?q=Blockchain&apiKey=10398e3c397d4c438a6c16e3f88785d1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Parse JSON
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            // Extract array from JSON
            JsonArray articles = jsonResponse.getAsJsonArray("articles");

            //Write to CSV
            writeArticlesToCsv(articles);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void writeArticlesToCsv(JsonArray articles) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            // Header row
            String[] header = {"Link", "Source", "Title", "Summary", "Content", "Date", "Author", "Type", "Tag", "Categories"};
            writer.writeNext(header);

            // Write article rows
            for (int i = 0; i < articles.size(); i++) {
                JsonObject article = articles.get(i).getAsJsonObject();

                String source = article.getAsJsonObject("source").get("name").getAsString();
                String author = article.has("author") && !article.get("author").isJsonNull() ? article.get("author").getAsString() : "";
                String title = article.has("title") && !article.get("title").isJsonNull() ? article.get("title").getAsString() : "";
                String description = article.has("description") && !article.get("description").isJsonNull() ? article.get("description").getAsString() : "";
                String url = article.has("url") && !article.get("url").isJsonNull() ? article.get("url").getAsString() : "";
                String publishedAt = article.has("publishedAt") && !article.get("publishedAt").isJsonNull() ? article.get("publishedAt").getAsString() : "";
                String content = article.has("content") && !article.get("content").isJsonNull() ? article.get("content").getAsString() : "";
                String type = "";
                String tag = "";
                String categories = "";
                String[] row = {url, source, title, description, content , publishedAt, author , type, tag, categories};
                writer.writeNext(row);
            }

            System.out.println("CSV file created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
