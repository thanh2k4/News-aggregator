package controller;
import crawler.Article;
import data_mining.Search;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import data_mining.Search;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import app.Main;
import history.HistorySearchFileManager;
import history.HistorySearchModel;

public class HistoryController {

    @FXML
    private TableView<HistorySearchModel> historyTableView;

    @FXML
    private TableColumn<HistorySearchModel, Integer> idColumn;

    @FXML
    private TableColumn<HistorySearchModel, String> titleColumn;

    @FXML
    private TableColumn<HistorySearchModel, String> timestampColumn;
    
    @FXML
    private TableColumn<HistorySearchModel, Hyperlink> webNameColumn;
    
    @FXML
    private ComboBox<String> comboBoxSearch;
    
    @FXML
    private Button searchButton;
    private static HistorySearchFileManager historySearchFileManager;

    @FXML
    public void initialize() {
        historySearchFileManager = new HistorySearchFileManager();
        comboBoxSearch.getItems().addAll("Last 20 results","Last 50 results","Last 100 results");
        comboBoxSearch.setValue("Last 20 results");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        timestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp()));
        webNameColumn.setCellValueFactory(cellData -> {
            Hyperlink hyperlink = new Hyperlink(cellData.getValue().getWebName());
            hyperlink.setOnAction(event -> showWebsite(cellData.getValue().getWebName()));
            return new SimpleObjectProperty<>(hyperlink);
        });
        webNameColumn.setCellFactory(col -> {
            TableCell<HistorySearchModel, Hyperlink> cell = new TableCell<HistorySearchModel, Hyperlink>() {
                @Override
                public void updateItem(Hyperlink item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);
                        setGraphic(item);
                    }
                }
            };
            return cell;
        });
        searchButton.setOnAction(event -> loadHistoryData());
        historyTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                HistorySearchModel selectedHistory = historyTableView.getSelectionModel().getSelectedItem();
                if (selectedHistory != null) {
                	int id = selectedHistory.getId();
                	Article article = searchArticleByID(id);
                	showArticleDetails(article);
                }
            }
        });
    }
    @FXML
    private void loadHistoryData() {
        historySearchFileManager.loadJson();
        List<HistorySearchModel> historySearchList = historySearchFileManager.getHistorySearchModels();
        historyTableView.setItems(historySearchFileManager.getHistorySearchModels());
        String query = comboBoxSearch.getValue();
        int endIndex = 0;
        if (query.equals("Last 20 results")) {
                endIndex = Math.min(20, historySearchList.size());
        }
        if (query.equals("Last 50 results")) {
            endIndex = Math.min(50, historySearchList.size());
        }
        if (query.equals("Last 100 results")) {
            endIndex = Math.min(100, historySearchList.size());
        }
        
        historyTableView.setItems(FXCollections.observableArrayList(historySearchList.subList(0, endIndex)));
    }

    private Article searchArticleByID(int id) {
        Search search = new Search();
        return search.searchArticleByID(id);
    }

    private void showArticleDetails(Article article) {
    	historySearchFileManager.writeJson(article);
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ArticleDetail.fxml"));
            BorderPane articleDetailContent = loader.load();
            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);
            Main.changeCenterContent(articleDetailContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showWebsite(String url) {
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            if (!url.startsWith("www.")) {
                url = "www." + url;
            }
            url = "http://" + url;
        }
        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't open this link");
        }
    }
}
