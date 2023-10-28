package com.example.rubankfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public ListView<String> AccountList;
    public ChoiceBox<String> AccountType;
    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] test = {"one","two","three","four"};
        AccountType.getItems().addAll(test);
    }
}