package com.example.texteditorv2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class HelloController {
    @FXML
    private TextArea textArea;

    @FXML
    private Button saveBtn;

    @FXML
    private Button loadBtn;

    @FXML
    protected void onSaveButtonClick() {
        System.out.println("Save button clicked");
    }

    @FXML
    protected void onLoadButtonClick() {
        // Logic for load button click
        System.out.println("Load button clicked");
    }
}