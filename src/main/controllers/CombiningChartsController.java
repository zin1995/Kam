package main.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import main.ContentAnchor.MethodChart;
import javafx.fxml.FXML;

import java.util.Map;

public class CombiningChartsController {

    @FXML
    ListView listView1;
    @FXML
    ListView listView2;
    @FXML
    Button buttonLeft;
    @FXML
    Button buttonRight;

    private ComboBox<MethodChart> comboBox;


    @FXML
    public void initialize() {
        buttonLeft.setOnAction(event -> {
            MethodChart methodChart = (MethodChart) listView2.getSelectionModel().getSelectedItem();
            if (methodChart != null) {
                comboBox.getItems().add(methodChart);
                listView1.getItems().add(methodChart);
                listView2.getItems().remove(methodChart);
            }
        });
        buttonRight.setOnAction(event -> {
            MethodChart methodChart = (MethodChart) listView1.getSelectionModel().getSelectedItem();
            if (methodChart!=null) {
                if (methodChart != listView1.getItems().get(0)) {
                    listView2.getItems().add(methodChart);
                    comboBox.getItems().remove(methodChart);
                    listView1.getItems().remove(methodChart);
                }
            }

        });
    }

    public void setMap(Map<String, MethodChart> map) {
        for (MethodChart methodChart : map.values()) {
            if (!comboBox.getItems().contains(methodChart)) listView2.getItems().add(methodChart);
        }
    }

    public void setComboBox(ComboBox<MethodChart> comboBox) {
        this.comboBox = comboBox;
        listView1.getItems().addAll(comboBox.getItems());
    }


}
