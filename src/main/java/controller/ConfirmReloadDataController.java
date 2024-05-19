package controller;

import crawler.NewsApi;
import exception.CustomException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Window;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;

public class ConfirmReloadDataController {

    @FXML
    private Button yesButton;

    @FXML
    private Button cancelButton;

    private Stage popupStage;

    @FXML
    public void initialize() {
        yesButton.setOnAction(this::handleYesButtonAction);
        cancelButton.setOnAction(this::handleCancelButtonAction);
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    private void handleYesButtonAction(ActionEvent event) {
        String[] args = new String[0];
        popupStage.close();

        Alert alertLoading = new Alert(Alert.AlertType.INFORMATION);
        alertLoading.setTitle("Reload Data");
        alertLoading.setHeaderText(null);
        alertLoading.setContentText("Loading...");
        alertLoading.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    NewsApi.main(args);
                } catch (CustomException e) {
                    // Trong trường hợp xảy ra lỗi, hiển thị cửa sổ thông báo lỗi
                    Platform.runLater(() -> {
                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setTitle("Error");
                        alertError.setHeaderText(null);
                        alertError.setContentText(e.getMessage());
                        alertError.showAndWait();
                        e.printStackTrace();
                    });
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
        task.setOnSucceeded(e -> alertLoading.close());
    }

    private void handleCancelButtonAction(ActionEvent event) {
        popupStage.close();
    }
}
