module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.google.gson;
    requires playwright;
    requires json.simple;
    requires org.jsoup;
    requires edu.stanford.nlp.corenlp;
    requires com.opencsv;

    opens app.view to javafx.fxml;
    exports app;
    exports controller;
    opens controller to javafx.fxml;
}