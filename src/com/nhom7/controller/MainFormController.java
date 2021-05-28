package com.nhom7.controller;

import com.nhom7.Program;
import com.nhom7.Sensor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainFormController {
    @FXML
    private TextField quantity, radius, length, width;
    @FXML
    private AnchorPane ap;
    private Program program;
    private Stage stage;
    private InfoFormController infoFormController;

    public void random() {
        int quantity = Integer.parseInt(this.quantity.getText());
        int length = Integer.parseInt(this.length.getText());
        int width = Integer.parseInt(this.width.getText());
        Sensor.setRadius(Integer.parseInt(this.radius.getText()));

        program = new Program();
        program.setNumberOfSensor(quantity);
        program.setLengthOfArea(length);
        program.setWidthOfArea(width);

        program.randSensors();
        program.loadData();
        callOtherStage();
        this.stage.show();
        printInfo();
        infoFormController.setProgram(program);
        infoFormController.setDataComboBox(getActiveSensors());
    }

    public void callOtherStage() {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().
                getResource("../view/infoForm.fxml")));
        try {
            Parent root = loader.load();
            infoFormController = loader.getController();
            this.stage = new Stage();
            this.stage.setScene(new Scene(root, 669, 567));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printInfo() {
        stage.show();
        StringBuilder result = new StringBuilder();
        result.append("DANH SACH CAC SENSOR DA RANDOM LA\n");
        result.append(program.printSensors(program.getSensors()));
        result.append("**************************************\n");
        result.append("DUONG DI NGAN NHAT TU SINK SENSOR DEN TAT CA CAC SENSOR " +
                "TRONG MANG CO THE GIAO TIEP VOI NO\n");
        for (int i = 1; i < program.getNumberOfSensor(); i++) {
            List<Integer> path = program.getSensors().get(i).getShortestPath();
            if (path != null) {
                result.append("Duong di tu sensor 0 den sensor ").append(i).append(" la: ");
                path.forEach(x -> result.append(x).append(" "));
                result.append("\n");
            }
        }
        infoFormController.setTextArea(result.toString());
    }

    public void readFromFile() {
        Stage stage = (Stage) ap.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a data set");
        fc.setInitialDirectory(new File(System.getProperty("user.home"), "datasets"));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            String fullPath = file.getAbsolutePath();
            program = new Program();
            program.readFromFile(file.getAbsolutePath());
            callOtherStage();
            program.loadData();
            stage.show();
            printInfo();
            infoFormController.setProgram(program);
            infoFormController.setFilePath(fullPath);
            infoFormController.setDataComboBox(getActiveSensors());
        }
    }
    public List<Integer> getActiveSensors() {
        List<Integer> activeSensors = new ArrayList<>();
        for (Sensor sensor : program.getSensors()) {
            if (sensor.getShortestPath() != null) {
                activeSensors.add(sensor.getIndex());
            }
        }
        activeSensors.remove(0);
        return activeSensors;
    }

    public void closeForm(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}
