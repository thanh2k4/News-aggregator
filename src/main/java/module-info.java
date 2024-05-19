module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.google.gson;
    requires org.jsoup;
    requires com.opencsv;
    requires java.net.http;

    opens history to com.google.gson;
    opens view to javafx.fxml;
    exports app;

    exports controller;
    opens controller to javafx.fxml;
}