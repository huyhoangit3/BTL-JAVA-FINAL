package com.nhom7.controller;

import com.nhom7.Program;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class InfoFormController {
    @FXML
    public TextArea content;
    @FXML
    public AnchorPane ap1;
    private Program program;
    private String filePath;

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setTextArea(String text) {
        this.content.clear();
        this.content.setText(text);
        this.content.setStyle("-fx-font-size: 18");
        this.content.setEditable(false);
    }

    public void writeToFile() {
        Stage stage = (Stage) ap1.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Save file");
        fc.setInitialDirectory(new File(System.getProperty("user.home"),
                "datasets"));
        File file = fc.showSaveDialog(stage);
        if (file != null) {
            program.writeToFile(file.getAbsolutePath());
            this.filePath = file.getAbsolutePath();
        }
    }

    public void draw() {
        System.out.println(this.filePath);
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("python3 src/com/nhom7/python_scripts/Main.py " + this.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void closeInfoForm(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}
