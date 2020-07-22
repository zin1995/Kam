package main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import main.MethodChart;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.Map;

public class AddingStageController {

    @FXML
    ListView listView1;
    @FXML
    ListView listView2;
    @FXML
    Button buttonLeft;
    @FXML
    Button buttonRight;


    private ComboBox<MethodChart> comboBox;
    private Map<String, MethodChart> map;


    @FXML
    public void initialize() {
        buttonLeft.setOnAction(event -> {
            MethodChart methodChart = (MethodChart) listView2.getSelectionModel().getSelectedItem();
            comboBox.getItems().add(methodChart);
            listView1.getItems().add(methodChart);
            listView2.getItems().remove(methodChart);
        });
        buttonRight.setOnAction(event -> {
            MethodChart methodChart = (MethodChart) listView1.getSelectionModel().getSelectedItem();
            comboBox.getItems().remove(methodChart);
            listView1.getItems().remove(methodChart);
            listView2.getItems().add(methodChart);
        });
    }

    public void setMap(Map<String, MethodChart> map) {
        this.map = map;
        for (MethodChart methodChart : map.values()) {
            if (!comboBox.getItems().contains(methodChart)) listView2.getItems().add(methodChart);
        }
    }

    public void setComboBox(ComboBox<MethodChart> comboBox) {
        this.comboBox = comboBox;
        listView1.getItems().addAll(comboBox.getItems());
    }


}
