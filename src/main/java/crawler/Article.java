package crawler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Article {
    private int id;
    private String link;
    private String source;
    private String summary;
    private String title;
    private String content;
    private String date;
    private String author;
    private String tags;
    private String categories;
    
    //Getter Setter
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getTitle() { 
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Vector<String> getContent() {
        String[] contents = this.content.split("[.!?]");
        Vector<String> res = new Vector<>();
        Collections.addAll(res, contents);
        return res;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getDate() {
            return LocalDate.parse( date , DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getCategories() {
        return categories;
    }
    public void setCategories(String categories) {
        this.categories = categories;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Map<String, List<String>> getEntities() {
        Map<String, List<String>> entities = new HashMap<>();
        return entities;
    }
}
