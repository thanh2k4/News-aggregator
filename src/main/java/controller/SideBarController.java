package controller;

import crawler.NewsApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


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
    private Button reloadDataButton;

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
    public void reloadData(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Confirm Reload Data");

        // Create label
        Label label = new Label("Are you sure to reload data?");

        // Create buttons
        Button yesButton = new Button("Yes");
        Button cancelButton = new Button("Cancel");
        yesButton.setMinWidth(80);
        yesButton.setPrefWidth(80);
        yesButton.setMaxWidth(80);

        cancelButton.setMinWidth(80);
        cancelButton.setPrefWidth(80);
        cancelButton.setMaxWidth(80);

        yesButton.setOnAction(e -> {
            String[] args = new String[0];
            NewsApi.main(args);
            System.out.println("Yes clicked!");
            popupStage.close();
        });
        cancelButton.setOnAction(e -> popupStage.close());

        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(cancelButton, yesButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMargin( cancelButton ,new Insets(0, 30, 0, 0));
        hBox.setMargin( yesButton ,new Insets(0, 0, 0, 30));
        vBox.getChildren().addAll(label, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));

        popupStage.setScene(new Scene(vBox, 300, 100));
        popupStage.showAndWait();
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
