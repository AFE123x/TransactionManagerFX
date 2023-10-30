package com.example.rubankfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionManagerMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setWidth(637);
        stage.setHeight(720);
        stage.setResizable(false);
        stage.setTitle("Transaction Manager");
        FXMLLoader fxmlLoader = new FXMLLoader(TransactionManagerMain.class.getResource("TransactionManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720); // Match the scene size with the stage size
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}