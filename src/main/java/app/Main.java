package app;

import crawler.WebScrapper;
import data_mining.LoadArticle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.Stack;

public class Main extends Application {

    private static BorderPane root;
    private static Stack<Node> contentStack = new Stack<>();

    public static void pushContent(Node content) {
        contentStack.push(content);
    }

    public static Node popContent() {
        return contentStack.pop();
    }

    public static void changeCenterContent(Node newContent) {
        contentStack.push(root.getCenter());
        root.setCenter(newContent);
    }

    public static void backToPreviousContent() {
        if (!contentStack.isEmpty()) {
            Node previousContent = contentStack.pop();
            root.setCenter(previousContent);
        } else {
            System.out.println("No previous content!");
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("view/HomepageView.fxml"));
        root = mainLoader.load();
        
        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("view/SideBarOOP.fxml"));
        root.setLeft(sidebarLoader.load());
        
        LoadArticle loadArticle = new LoadArticle();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("APP");
        primaryStage.show();
    }

    public static void main(String[] args) {
//        WebScrapper.main(args);
        launch(args);
    }
}
