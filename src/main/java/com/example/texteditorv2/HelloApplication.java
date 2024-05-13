package com.example.texteditorv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CustomTextArea textEditor = new CustomTextArea();
        StackPane root = new StackPane();
        root.getChildren().add(textEditor);
        Scene scene = new Scene(root, 600, 400);  // Appropriate size for the editor


        stage.setTitle("Custom Text Editor with RichTextFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}