package com.nhom7.controller;

import com.nhom7.Program;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InfoFormController {
    @FXML
    public TextArea content;
    @FXML
    public ComboBox<Integer> activeSensor;
    @FXML
    public AnchorPane ap1;
    @FXML
    private Label numberOfActiveSensor;
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

    public void setNumberOfActiveSensor(String text) {
        this.numberOfActiveSensor.setText(text);
    }

    public void setDataComboBox(List<Integer> activeSens) {
        this.activeSensor.setItems(FXCollections.observableList(activeSens));
    }

    // method will be invoke when write to file button clicked.
    public void writeToFile() {
        Stage stage = (Stage) ap1.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fc.setInitialFileName("*.txt");
        fc.setTitle("Save file");
        fc.setInitialDirectory(new File(System.getProperty("user.dir"),
                "src/com/nhom7/datasets"));
        File file = fc.showSaveDialog(stage);
        if (file != null) {
            program.writeToFile(file.getAbsolutePath());
            this.filePath = file.getAbsolutePath();
        }
    }

    // method will be invoke when draw button clicked.
    public void draw() {
        if (filePath != null) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("python3 src/com/nhom7/python_scripts/Main.py " + this.filePath + " 0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please write to file first!");
            alert.show();
        }

    }

    // get data from combobox.
    public void valueChange() {
        String index = activeSensor.getValue().toString();
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("python3 src/com/nhom7/python_scripts/Main.py " + this.filePath + " " + index);
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
