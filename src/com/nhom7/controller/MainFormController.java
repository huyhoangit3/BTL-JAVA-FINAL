package com.nhom7.controller;

import com.nhom7.Program;
import com.nhom7.Sensor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    // method will be invoke when random button clicked.
    public void random() {
        // get data from text field
        try {
            int quantity = Integer.parseInt(this.quantity.getText());
            int length = Integer.parseInt(this.length.getText());
            int width = Integer.parseInt(this.width.getText());
            Sensor.setRadius(Integer.parseInt(this.radius.getText()));

            program = new Program();
            program.setNumberOfSensor(quantity + 1);
            program.setLengthOfArea(length);
            program.setWidthOfArea(width);

            program.randSensors();
            program.loadData();
            callOtherStage();
            this.stage.show();
            printInfo();
            // pass program variable to infoFormController form.
            infoFormController.setProgram(program);
            // set data for combobox.
            infoFormController.setDataComboBox(getActiveSensors());
            infoFormController.setNumberOfActiveSensor("Active sensor: " + (getActiveSensors().size() + 1)
                    + "/" + program.getNumberOfSensor());
        } catch (Exception e) {
            System.err.println("Dữ liệu nhập vào không hợp lệ!");
        }
    }

    public void callOtherStage() {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().
                getResource("..\\view\\infoForm.fxml")));
        try {
            Parent root = loader.load();
            infoFormController = loader.getController();
            this.stage = new Stage();
            this.stage.setScene(new Scene(root, 669, 621));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set text for text area.
    public void printInfo() {
        stage.show();
        StringBuilder result = new StringBuilder();
        result.append("DANH SÁCH CÁC SENSOR ĐÃ RANDOM LÀ:\n");
        result.append(program.printSensors(program.getSensors()));
        result.append("**************************************\n");
        result.append("ĐƯỜNG ĐI NGẮN NHẤT SINK SENSOR ĐẾN TẤT CẢ CÁC\nSENSOR " +
                "TRONG MẠNG CÓ THỂ KẾT NỐI VỚI NÓ\n");
        for (int i = 1; i < program.getNumberOfSensor(); i++) {
            List<Integer> path = program.getSensors().get(i).getShortestPath();
            if (path != null) {
                result.append("Đường đi từ sink sensor đến sensor ").append(i).append(" là: ");
                path.forEach(x -> result.append(x).append(" "));
                result.append("\n");
            }
        }
        infoFormController.setTextArea(result.toString());
    }

    // method will be invoke when read from file button clicked.
    public void readFromFile() {
        Stage stage = (Stage) ap.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a data set");
        fc.setInitialDirectory(new File(System.getProperty("user.dir"), "src\\com\\nhom7\\datasets"));
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
            infoFormController.setNumberOfActiveSensor("Active sensor: " + (getActiveSensors().size() + 1)
                    + "/" + program.getNumberOfSensor());
        }
    }
    // get list of sensor that have path to sink sensor.
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
