package controller;

import crawler.Article;
import data_mining.Search;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import app.Main;
import history.HistorySearchFileManager;


public class SearchContentController {
	private static final int RESULTS_PER_PAGE = 20;
    private List<Article> searchResult;
    private int currentPage = 0;
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private VBox articleBox;

    @FXML
    private ComboBox<String> searchCriteriaComboBox;

    @FXML
    private TextField urlInput;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Search searchEngine;
    
    @FXML
    private AdvancedSearch advancedSearchEngine;
    
    @FXML
    private Button previousButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private TextField totalArticleResults;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private ComboBox<String> comboBoxAdvanced1;
    
    @FXML
    private ComboBox<String> comboBoxAdvanced2;
    
    @FXML
    private ComboBox<String> comboBoxAdvanced3;
    
    @FXML
    private TextField textFieldAdvanced1;
    
    @FXML
    private TextField textFieldAdvanced2;
    
    @FXML
    private TextField textFieldAdvanced3;
    
    @FXML
    private TextField pageNumberInput;
    
    @FXML
    private TextField totalPagesText;
    
    @FXML
    private Button searchPageButton;
    private String searchKeyword;
    @FXML
    private void searchArticles() {
        // Lấy tiêu chí tìm kiếm từ ComboBox
        String selectedCriteria = searchCriteriaComboBox.getValue();
        if (selectedCriteria != null && !selectedCriteria.isEmpty()) {
        	searchKeyword = urlInput.getText().trim();
            List<Article> result = null;
            switch (selectedCriteria) {
                case "All":
                    String queryAll = urlInput.getText().trim();
                    result = searchEngine.searchArticleByAll(queryAll);
                    break;
                case "Author":
                    String queryAuthor = urlInput.getText().trim();
                    result = searchEngine.searchArticleByAuthor(queryAuthor);
                    break;
                case "Title":
                    String queryTitle = urlInput.getText().trim();
                    result = searchEngine.searchArticleByTitle(queryTitle);
                    break;
                case "Content":
                    String queryContent = urlInput.getText().trim();
                    result = searchEngine.searchArticleByContent(queryContent);
                    break;
                case "Hashtag":
                    String queryHashtag = urlInput.getText().trim();
                    result = searchEngine.searchArticleByHashtag(queryHashtag);
                    break;
                case "PublishDate":
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();
                    result = searchEngine.searchArticleByDate(startDate, endDate);
                    break;
                case "UMC":{
                    List<Pair<String,String>> queries = new ArrayList<>();
                    String criteria1 = comboBoxAdvanced1.getValue();
                    String query1 = textFieldAdvanced1.getText().trim();
                    if (criteria1 != null && !criteria1.isEmpty() && query1 != null && !query1.isEmpty()) {
                        queries.add(new Pair<>(criteria1, query1));
                    }

                    String criteria2 = comboBoxAdvanced2.getValue();
                    String query2 = textFieldAdvanced2.getText().trim();
                    if (criteria2 != null && !criteria2.isEmpty() && query2 != null && !query2.isEmpty()) {
                        queries.add(new Pair<>(criteria2, query2));
                    }

                    String criteria3 = comboBoxAdvanced3.getValue();
                    String query3 = textFieldAdvanced3.getText().trim();
                    if (criteria3 != null && !criteria3.isEmpty() && query3 != null && !query3.isEmpty()) {
                        queries.add(new Pair<>(criteria3, query3));
                    }

                    result = advancedSearchEngine.searchArticleUnionMultipleCriterions(queries);
                    break;
                }
                case "IMC":{
                    List<Pair<String,String>> queries = new ArrayList<>();
                    String criteria1 = comboBoxAdvanced1.getValue();
                    String query1 = textFieldAdvanced1.getText().trim();
                    if (criteria1 != null && !criteria1.isEmpty() && query1 != null && !query1.isEmpty()) {
                        queries.add(new Pair<>(criteria1, query1));
                    }

                    String criteria2 = comboBoxAdvanced2.getValue();
                    String query2 = textFieldAdvanced2.getText().trim();
                    if (criteria2 != null && !criteria2.isEmpty() && query2 != null && !query2.isEmpty()) {
                        queries.add(new Pair<>(criteria2, query2));
                    }

                    String criteria3 = comboBoxAdvanced3.getValue();
                    String query3 = textFieldAdvanced3.getText().trim();
                    if (criteria3 != null && !criteria3.isEmpty() && query3 != null && !query3.isEmpty()) {
                        queries.add(new Pair<>(criteria3, query3));
                    }

                    result = advancedSearchEngine.searchArticleIntersectMutipleCriterions(queries);
                    break;
                }
            }
            searchResult = result;
            currentPage = 0;
            displaySearchResult(result);
        }
    }
    private void displaySearchResult(List<Article> result) {
    	articleBox.getChildren().clear();
        int startIndex = currentPage * RESULTS_PER_PAGE;
        int endIndex = Math.min(startIndex + RESULTS_PER_PAGE, result.size());
        totalArticleResults.setText(result.size() + "");

        int totalPages = (int) Math.ceil((double) result.size() / RESULTS_PER_PAGE);
        totalPagesText.setText(String.valueOf(totalPages));
        
        if (result != null && !result.isEmpty()) {
            for (int i = startIndex ; i < endIndex ; i++) {
                Article article = result.get(i);
                TextField articleHeader = new TextField( article.getTitle());
                articleHeader.setEditable(false);
                articleHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                articleHeader.getStyleClass().add("red-text");
                articleHeader.setOnMouseClicked(event -> {
                    showArticleDetails(article);
                });

                TextArea articleSummary = new TextArea(article.getSummary());
                articleSummary.setEditable(false);
                articleSummary.setWrapText(true);
                articleSummary.setPrefHeight(100);

                articleBox.getChildren().addAll(articleHeader, articleSummary);
            }
        } else {
            TextArea noResultsText = new TextArea("No results found.");
            noResultsText.setEditable(false);
            articleBox.getChildren().add(noResultsText);
        }
    }
    @FXML
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            displaySearchResult(searchResult);
        }
    }

    @FXML
    private void nextPage() {
        int totalPages = (int) Math.ceil((double) searchResult.size() / RESULTS_PER_PAGE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            displaySearchResult(searchResult);
        }
    }
    HistorySearchFileManager historySearchFileManager = new HistorySearchFileManager();
    private void showArticleDetails(Article article) {
        historySearchFileManager.writeJson(article);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ArticleDetail.fxml"));
            BorderPane articleDetailContent = loader.load();
            ArticleDetailController controller = loader.getController();
            controller.setSearchKeyword(searchKeyword);
            controller.setArticle( article);
            Main.changeCenterContent(articleDetailContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        searchEngine = new Search();
        advancedSearchEngine = new AdvancedSearch();
   //     List<Article> articles = searchEngine.getArticles();
   //     displaySearchResult(articles);
        searchCriteriaComboBox.getItems().addAll("All", "ID", "Author", "Title","Content", "Hashtag", "PublishDate","UMC", "IMC");
        searchCriteriaComboBox.setValue("All");
        comboBoxAdvanced1.getItems().addAll("ID","Author","Title","Content","Hashtag");
        comboBoxAdvanced2.getItems().addAll("ID","Author","Title","Content","Hashtag");
        comboBoxAdvanced3.getItems().addAll("ID","Author","Title","Content","Hashtag");
        urlInput.setVisible(true);
        startDatePicker.setVisible(false);
        endDatePicker.setVisible(false);
        comboBoxAdvanced1.setVisible(false);
        comboBoxAdvanced2.setVisible(false);
        comboBoxAdvanced3.setVisible(false);
        textFieldAdvanced1.setVisible(false);
        textFieldAdvanced2.setVisible(false);
        textFieldAdvanced3.setVisible(false);
        
        searchCriteriaComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
        	if (newVal.equals("PublishDate")) {
        		urlInput.setVisible(false);
        		startDatePicker.setVisible(true);
        		endDatePicker.setVisible(true);
        		comboBoxAdvanced1.setVisible(false);
                comboBoxAdvanced2.setVisible(false);
                comboBoxAdvanced3.setVisible(false);
                textFieldAdvanced1.setVisible(false);
                textFieldAdvanced2.setVisible(false);
                textFieldAdvanced3.setVisible(false);
        	}
        	else if (newVal.equals("UMC") || newVal.equals("IMC")){
        		comboBoxAdvanced1.setVisible(true);
                comboBoxAdvanced2.setVisible(true);
                comboBoxAdvanced3.setVisible(true);
                textFieldAdvanced1.setVisible(true);
                textFieldAdvanced2.setVisible(true);
                textFieldAdvanced3.setVisible(true);
                urlInput.setVisible(false);
                startDatePicker.setVisible(false);
                endDatePicker.setVisible(false);
        	}
        	else {
        		urlInput.setVisible(true);
                startDatePicker.setVisible(false);
                endDatePicker.setVisible(false);
                comboBoxAdvanced1.setVisible(false);
                comboBoxAdvanced2.setVisible(false);
                comboBoxAdvanced3.setVisible(false);
                textFieldAdvanced1.setVisible(false);
                textFieldAdvanced2.setVisible(false);
                textFieldAdvanced3.setVisible(false);
        	}
        });
    }
    
    @FXML
    private void searchPage() {
        try {
            int requestedPage = Integer.parseInt(pageNumberInput.getText());
            int totalPages = (int) Math.ceil((double) searchResult.size() / RESULTS_PER_PAGE);
            if (requestedPage > 0 && requestedPage <= totalPages) {
                currentPage = requestedPage - 1;
                displaySearchResult(searchResult);
            } else {
 
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid page number.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid page number.");
            alert.showAndWait();
        }
    }

}
