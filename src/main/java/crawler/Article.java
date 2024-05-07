package crawler;

import java.time.LocalDate;

public class Article {
    private String link;
    private String source;
    private String type;
    private String summary;
    private String title;
    private String content;
    private LocalDate date;
    private String author;
    private String tags;
    private String categories;
    private String keywords;
        
    public Article() {
        this.link = link;
        this.source = source;
        this.type = type;
        this.summary = summary;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
        this.tags = tags;
        this.categories = "";
        this.keywords = "";
    }
    
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
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
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
