package com.example.rubankfx;


import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.DatePicker;

public class HelloController {
    public Text NameText;
    public TextField Hengyi;
    public DatePicker DatePicker;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(Hengyi.getText());
    }
}