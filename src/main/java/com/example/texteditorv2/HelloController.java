package com.example.texteditorv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;

public class HelloController {
    private PieceTable pieceTable;
    @FXML
    private CustomTextArea textArea;

    @FXML
    private Button saveBtn;

    @FXML
    private Button loadBtn;

    @FXML
    public void initialize() {
        pieceTable = new PieceTable();
        textArea.setPieceTable(pieceTable);
    }

    @FXML
    protected void onSaveButtonClick() {
        // TODO Logic for save button click
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Text File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showSaveDialog(saveBtn.getScene().getWindow());

            if (selectedFile != null){
                FileHandling.writeFile(selectedFile.getPath(), pieceTable.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Save button clicked");
    }

    @FXML
    protected void onLoadButtonClick() {
        // Logic for load button click
        System.out.println("Load button clicked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(loadBtn.getScene().getWindow());

        if (selectedFile != null){
            try {
                String content = FileHandling.readFile(selectedFile.getPath());
                pieceTable.loadContent(content);
                textArea.replaceText(pieceTable.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}