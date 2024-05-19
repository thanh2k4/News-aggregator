package controller;

import crawler.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.Main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Color;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ArticleDetailController {

    @FXML
    private Text idText;

    @FXML
    private Hyperlink linkText;

    @FXML
    private Text typeText;

    @FXML
    private Text titleText;

    @FXML
    private Text summaryText;

    @FXML
    private TextFlow contentTextFlow;

    @FXML
    private Text authorText;

    @FXML
    private Text hashtagText;

    @FXML
    private Text categoryText;

    @FXML
    private Text publishDateText;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button highlightEntitiesButton;
    private Article currentArticle;
    private String searchKeyword;

    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
    }
    public void setArticle(Article article) {

        this.currentArticle = article;
        idText.setText(String.valueOf(article.getId()));
        linkText.setText(article.getLink());
        linkText.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(article.getLink()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        typeText.setText(article.getType());
        titleText.setText(article.getTitle());
        titleText.setFill(Color.RED);
        summaryText.setText(article.getSummary());

        if ( searchKeyword != null && !searchKeyword.isEmpty()) {
            highlightSearchKeywordInContent( article.getContent() );
        } else {
            displayContentWithoutHighlighting( article.getContent() );
        }
        String authors = article.getAuthor();
        StringBuilder authorBuilder = new StringBuilder(authors);
        authorText.setText(authors);

        String hashtags = article.getTags();
        hashtagText.setText(hashtags);

        String categories = article.getCategories();
        StringBuilder categoryBuilder = new StringBuilder(categories);
        categoryText.setText(categoryBuilder.toString());

        publishDateText.setText( article.getDate().toString());
    }
    private void displayContentWithoutHighlighting(Vector<String> htmlContent) {
        contentTextFlow.getChildren().clear();
        for (String paragraph : htmlContent) {
            String textContent = removeHtmlTags(paragraph);
            Text textNode = new Text(textContent + "\n");
            contentTextFlow.getChildren().add(textNode);
        }
    }
    private void highlightSearchKeywordInContent(Vector<String> htmlContent) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return;
        }
        contentTextFlow.getChildren().clear();
        Pattern pattern = Pattern.compile(Pattern.quote(searchKeyword), Pattern.CASE_INSENSITIVE);
        for (String paragraph : htmlContent) {
            String textContent = removeHtmlTags(paragraph);
            Matcher matcher = pattern.matcher(textContent);
            int lastEnd = 0;
            while (matcher.find()) {
                String beforeText = textContent.substring(lastEnd, matcher.start());
                Text before = new Text(beforeText);
                contentTextFlow.getChildren().add(before);

                String highlightedText = textContent.substring(matcher.start(), matcher.end());
                Text highlight = new Text(highlightedText);
                highlight.setFill(Color.BLUE);
                highlight.setFont(Font.font("Helvetica", FontWeight.BOLD, 12)); 
                contentTextFlow.getChildren().add(highlight);

                lastEnd = matcher.end();
            }
            String afterText = textContent.substring(lastEnd);
            Text after = new Text(afterText);
            contentTextFlow.getChildren().add(after);
        }
    }
   
    private boolean isHighlighted = false;
    

    @FXML
    private void handleHighlightEntities(ActionEvent event) {
        if (!isHighlighted) {
            highlightEntities(); 
            isHighlighted = true;
        } else {
            removeHighlight();
            isHighlighted = false; 
        }
    }

    private void highlightEntities() {
        contentTextFlow.getChildren().clear();
        Vector<String> htmlContent = currentArticle.getContent();
        Map<String, List<String>> entities = currentArticle.getEntities();

        int sentenceCount = 0; 
        for (String paragraph : htmlContent) {
            String textContent = removeHtmlTags(paragraph);
            String[] words = textContent.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                boolean isEntity = entities.values().stream().flatMap(List::stream).anyMatch(e -> e.equals(word));
                Text textNode = new Text(word);
                if (isEntity) {
                    textNode.setFill(Color.RED);
                    textNode.setFont(new Font(14));
                    Tooltip tooltip = new Tooltip("Entity Type: " + getEntityType(word, entities));
                    Tooltip.install(textNode, tooltip);
                }
                contentTextFlow.getChildren().add(textNode);
                if (word.endsWith(".")) {
                    sentenceCount++;
                    if (sentenceCount == 3) {
                        contentTextFlow.getChildren().add(new Text("\n"));
                        sentenceCount = 0;
                    }
                }
                if (i < words.length - 1) {
                    contentTextFlow.getChildren().add(new Text(" "));
                }
            }
            contentTextFlow.getChildren().add(new Text("\n"));
        }
    }

    private String getEntityType(String word, Map<String, List<String>> entities) {
        for (Map.Entry<String, List<String>> entry : entities.entrySet()) {
            if (entry.getValue().contains(word)) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }

    private void removeHighlight() {
        contentTextFlow.getChildren().clear();
        setArticle(currentArticle);
    }


    @FXML
    private void handleBackAction(ActionEvent event) {
        Main.backToPreviousContent();
    }
    private String removeHtmlTags(String html) {
        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(html);
        return matcher.replaceAll("");
    }
}
