package crawler;

import java.io.FileWriter;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import exception.CustomException;
import javafx.scene.control.Alert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewsApi {
    private final static String FILE_PATH = "src/main/resources/data/articles.csv";
    private final static String API_KEY = "10398e3c397d4c438a6c16e3f88785d1";
    public static void main(String[] args) throws CustomException {
        String[] queries = {"Blockchain%20Technology", "Cryptocurrencies", "Applications%20of%20Blockchain", "Blockchain%20Business", "Blockchain%20Security", "Blockchain%20Society"};
        for (String query : queries) {
            String url = "https://newsapi.org/v2/everything?q=" + query + "&apiKey=" + API_KEY + "&language=en";

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
                writeArticlesToCsv(articles, query, "NewsPost");
            } catch (IOException | InterruptedException e) {
                throw new CustomException("Please check your network connection. ");
            }
        }
    }
    private static void writeArticlesToCsv(JsonArray articles, String query, String method) {
        boolean fileExists = new java.io.File(FILE_PATH).exists();
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            // Header row
            if (!fileExists) {
                String[] header = {"Link", "Source", "Title", "Summary", "Content", "Date", "Author", "Type", "Tag", "Categories"};
                writer.writeNext(header);
            }

            // Write article rows
            for (int i = 0; i < articles.size(); i++) {
                JsonObject article = articles.get(i).getAsJsonObject();

                String source = article.getAsJsonObject("source").get("name").getAsString();
                String author = article.has("author") && !article.get("author").isJsonNull() ? article.get("author").getAsString() : "";
                String title = article.has("title") && !article.get("title").isJsonNull() ? article.get("title").getAsString() : "";
                String description = article.has("description") && !article.get("description").isJsonNull() ? article.get("description").getAsString() : "";
                String url = article.has("url") && !article.get("url").isJsonNull() ? article.get("url").getAsString() : "";
                String publishedAt = article.has("publishedAt") && !article.get("publishedAt").isJsonNull() ? article.get("publishedAt").getAsString() : "";
                String content = getDetails(url);
                if(content == ""){
                    content = article.has("content") && !article.get("content").isJsonNull() ? article.get("content").getAsString() : "";
                }
                String type = method;
                String tag = "";
                String categories = getCategoryFromQuery(query);
                String[] row = {url, source, title, description, content , publishedAt, author , type, tag, categories};
                writer.writeNext(row);
            }

            System.out.println("CSV file created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getDetails(String url) {
        try {
            //Connect to URL
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
            //Get paragraph elements
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
