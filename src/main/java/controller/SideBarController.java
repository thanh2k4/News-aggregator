package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SideBarController {

    @FXML
    private Button mainButton;
    @FXML
    private Button aboutBlockchainButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button trendButton;
    @FXML
    private Button aboutUsButton;

    @FXML
    void mainSwitch(ActionEvent event) {
        loadContent("/app/view/MainScreen.fxml", event);
        resetButtonStyles();
        mainButton.getStyleClass().add("buttonSelected");
    }

    @FXML
    void aboutblockchainSwitch(ActionEvent event) {
        loadContent("/app/view/AboutBlockchain.fxml", event);
        resetButtonStyles();
        aboutBlockchainButton.getStyleClass().add("buttonSelected");
    }

    @FXML
    void searchSwitch(ActionEvent event) {
        loadContent("/app/view/SearchContent.fxml", event);
        resetButtonStyles();
        searchButton.getStyleClass().add("buttonSelected");
    }

    @FXML
    void historySwitch(ActionEvent event) {
        loadContent("/app/view/SearchHistoryContent.fxml", event);
        resetButtonStyles();
        historyButton.getStyleClass().add("buttonSelected");
    }

    @FXML
    void trendSwitch(ActionEvent event) {
        loadContent("/app/view/Statistic.fxml", event);
        resetButtonStyles();
        trendButton.getStyleClass().add("buttonSelected");
    }

    @FXML
    void aboutusSwitch(ActionEvent event) {
        loadContent("/app/view/AboutUs.fxml", event);
        resetButtonStyles();
        aboutUsButton.getStyleClass().add("buttonSelected");
    }

    private void loadContent(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane content = loader.load();
            Node sourceNode = (Node) event.getSource();
            Scene scene = sourceNode.getScene();
            Parent root = scene.getRoot();
            if (root instanceof BorderPane) {
                BorderPane borderPane = (BorderPane) root;
                borderPane.setCenter(null); 
                borderPane.setCenter(content);
            } else {
                System.out.println("Root is not an instance of BorderPane!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void resetButtonStyles() {
        mainButton.getStyleClass().remove("buttonSelected");
        aboutBlockchainButton.getStyleClass().remove("buttonSelected");
        searchButton.getStyleClass().remove("buttonSelected");
        historyButton.getStyleClass().remove("buttonSelected");
        trendButton.getStyleClass().remove("buttonSelected");
        aboutUsButton.getStyleClass().remove("buttonSelected");
    }
}
