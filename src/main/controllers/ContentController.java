package main.controllers;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import main.LasParser;
import main.MethodChart;
import main.MethodsVBox;


public class ContentController {

    @FXML
    SplitPane splitPane;
    @FXML
    AnchorPane depthPane;
    @FXML
    Slider depthSlider;
    @FXML
    ScrollPane depthScroll;
    @FXML
    ScrollBar vScroll;



    private LasParser lasParser;
    private HashMap<String, MethodsVBox> methodsPane = new HashMap<>();
    private HashMap<String, MethodChart> chartMap = new HashMap<>();
    private int depthMultiplier = 10;


    public void setParser(LasParser lasParser) {
        this.lasParser = lasParser;
        for (Map.Entry<String, double[]> pair : lasParser.getMethodsData().entrySet()) {
            addMethodPane(pair.getKey(), pair.getValue());
            showMethodPane(pair.getKey());
        }
        for(Map.Entry<String, double[]> pair : lasParser.getStitchedMethodsData().entrySet()){
            addMethodPane(pair.getKey(), pair.getValue());
        }
        drawAllPanelsContent();
    }

    public HashMap<String, MethodChart> getChartMap() {
        return chartMap;
    }

    @FXML
    public void initialize() {
        vScroll.setMax(depthScroll.getVmax());
        vScroll.setMin(depthScroll.getVmin());
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> depthScroll.setVvalue(newValue.doubleValue()));
        depthScroll.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));

        depthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            depthMultiplier = newValue.intValue();
            for (MethodChart methodChart : chartMap.values()) {
                methodChart.setDepthMultiplier(depthMultiplier);
            }
            drawAllPanelsContent();
        });
    }

    public void deletePanel(String s) {
        splitPane.getItems().remove(methodsPane.get(s));
    }

    public void restorePanel(String s) {
        splitPane.getItems().add(methodsPane.get(s));
        methodsPane.get(s).update();
    }

    private void addMethodPane(String methodName, double[] methodData) {
        MethodChart methodChart = new MethodChart(methodName, methodData, lasParser.getDeptData(), lasParser.getDeptStep());
        chartMap.put(methodName, methodChart);

        MethodsVBox methodsVBox = new MethodsVBox(methodChart, vScroll, this);
        methodsPane.put(methodName, methodsVBox);
    }

    private void showMethodPane(String methodName){
        splitPane.getItems().add(methodsPane.get(methodName));
    }


    private void drawContentsDepthPanel() {
        depthPane.getChildren().clear();
        double[] depthData = lasParser.getDeptData();
        double lowerDepth = depthData[0];

        for (int i = 0; i < depthData.length; i++) {
            double currentYPoint = (depthData[i] - lowerDepth) * depthMultiplier+10;

            Line line = new Line(0, currentYPoint, 2, currentYPoint);
            line.setStrokeWidth(0.4);
            depthPane.getChildren().add(line);

            if (i % 5 == 0 && i % 10 != 0) {
                depthPane.getChildren().add(new Line(0, currentYPoint, 5, currentYPoint));
            }

            if (i % 10 == 0) {
                depthPane.getChildren().add(new Line(0, currentYPoint, 10, currentYPoint));
                Line line2 = new Line(0, currentYPoint, depthPane.getMaxWidth(), currentYPoint);
                line2.setStrokeWidth(0.1);
                depthPane.getChildren().add(line2);
                if (depthMultiplier > 7) {
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
                }
                if (i % 20 == 0 && depthMultiplier <= 7 && depthMultiplier > 2)
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
                if (i % 100 == 0 && depthMultiplier <= 2)
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
            }
        }
    }


    private void drawAllPanelsContent() {
        drawContentsDepthPanel();
        for (Node node : splitPane.getItems()) {
            ((MethodsVBox) node).update();
        }
    }



    //    public void deleteLithologyPanel(String s) {
//        splitPane.getItems().remove(methodsPane.get("Литология " + s));
//        double size = splitPane.getItems().size();
//        for (int i = 0; i < size; i++) {
//            splitPane.setDividerPosition(i, 1 / size);
//        }
//    }
//
//    public void restoreLithologyPanel(String s) {
//        double size = splitPane.getItems().size();
//        int j = 0;
//        for (int i = 0; i < size; i++) {
//            splitPane.setDividerPosition(i, 1 / size);
//            if (splitPane.getItems().get(i).equals(methodsPane.get(s))) j = i + 1;
//        }
//        splitPane.getItems().add(j, methodsPane.get("Литология " + s));
//    }


//    @FXML
//    private void drawLithology(String s) {
//        VBox vBox = new VBox();
//        SplitPane splitPane1 = new SplitPane();
//        splitPane1.setOrientation(Orientation.VERTICAL);
//        Label label = new Label("Литология " + s);
//        label.setMinHeight(45);
//        splitPane1.getItems().add(label);
//        vBox.getChildren().add(splitPane1);
//        chartMap.get(s).drawLithology(splitPane1);
//        methodsPane.put("Литология " + s, vBox);
//    }

}
