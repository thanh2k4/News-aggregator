package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class WebScrapper {
    private static final String FILE_PATH = "src/main/resources/data/articles.csv";
    public static void main(String[] args) {
        try {
            // Kết nối tới trang web cần thu thập dữ liệu
            String url = "https://www.forbes.com/search/?q=blockchain&sh=13a5acc6279f";
            Document doc = Jsoup.connect(url).get();
            // Lấy tất cả các phần tử chứa bài viết
            Elements articleElements = doc.select("div.article");

            // Mở file CSV để lưu dữ liệu
            FileWriter csvWriter = new FileWriter(FILE_PATH);
            csvWriter.append("Title,Link,Source,Type,Summary,Content,Date,Tags,Author,Category\n");

            // Duyệt qua từng bài viết và ghi thông tin vào file CSV
            for (Element articleElement : articleElements) {
                Article article = new Article();
            // Không dùng được cho nhiều trang web khác nhau do cấu trúc html, cách đặt tên khác nhau
                article.setTitle(articleElement.select("h2 a").text());
                article.setLink(articleElement.select("h2 a").attr("href"));
                article.setSource("Forbes");
                article.setType("News Article");
                article.setSummary(articleElement.select("p").text());
                article.setDate(LocalDate.parse(articleElement.select("div.stream-item__date").text() , DateTimeFormatter.ofPattern("MMM d, yyyy")));
                article.setAuthor(articleElement.select("div.stream-item__contributor").text());
                article.setCategories("Blockchain");

                // Ghi thông tin vào file CSV
                csvWriter.append("\"").append(article.getTitle()).append("\",");
                csvWriter.append("\"").append(article.getLink()).append("\",");
                csvWriter.append("\"").append(article.getSource()).append("\",");
                csvWriter.append("\"").append(article.getType()).append("\",");
                csvWriter.append("\"").append(article.getSummary()).append("\",");
                csvWriter.append("\"").append( articleElement.select("div.stream-item__tag a").text() ).append("\","); // Không lấy nội dung ở đây
                csvWriter.append("\"").append(article.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))).append("\",");
                csvWriter.append("\"").append(article.getTags()).append("\",");
                csvWriter.append("\"").append(article.getAuthor()).append("\",");
                csvWriter.append("\"").append(article.getCategories()).append("\"\n");
            }

            // Đóng file CSV
            csvWriter.flush();
            csvWriter.close();

            System.out.println("Dữ liệu đã được thu thập và lưu vào file CSV.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
