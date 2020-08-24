package main.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.*;
import main.ContentAnchor.Lithology;
import main.ContentAnchor.MethodChart;
import main.updatable.LithologyVBox;
import main.updatable.MethodsVBox;
import main.updatable.Updatable;


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
    @FXML
    Button lithology;


    private LasParser lasParser;
    private HashMap<String, MethodsVBox> methodsPane = new HashMap<>();
    private HashMap<String, LithologyVBox> lithologyPane = new HashMap<>();
    private ArrayList<Lithology> lithologyList = new ArrayList<>();
    private HashMap<String, MethodChart> chartMap = new HashMap<>();
    private int depthMultiplier = 2;


    public void setParser(LasParser lasParser) {
        this.lasParser = lasParser;
        for (Map.Entry<String, double[]> pair : lasParser.getMethodsData().entrySet()) {
            addMethodPane(pair.getKey(), pair.getValue());
            showMethodPane(pair.getKey());
        }
        for (Map.Entry<String, double[]> pair : lasParser.getStitchedMethodsData().entrySet()) {
            addMethodPane(pair.getKey(), pair.getValue());
        }
        drawAllPanelsContent();
    }

    public HashMap<String, MethodChart> getChartMap() {
        return chartMap;
    }

    public ScrollBar getVScroll() {
        return vScroll;
    }

    @FXML
    public void initialize() {
        lithology.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/createLithology.fxml"));
            try {
                Stage stage = loader.load();
                stage.show();
                LithologyController lithologyController = loader.getController();
                lithologyController.setContentController(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        vScroll.setMax(depthScroll.getVmax());
        vScroll.setMin(depthScroll.getVmin());
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> {
            depthScroll.setVvalue(newValue.doubleValue());
        });
        depthScroll.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));

        depthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            depthMultiplier = newValue.intValue();
            for (MethodChart methodChart : chartMap.values()) {
                methodChart.setDepthMultiplier(depthMultiplier);
            }

            for(Lithology lithology: lithologyList){
                lithology.setDepthMultiplier(depthMultiplier);
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

    public void addLithologyPane(String lithologyName, Lithology lithology){
        LithologyVBox lithologyVBox = new LithologyVBox(lithology, vScroll);
        lithologyList.add(lithology);
        splitPane.getItems().add(lithologyVBox);
    }

    private void addMethodPane(String methodName, double[] methodData) {
        MethodChart methodChart = new MethodChart(methodName, methodData, lasParser.getDeptData());
        chartMap.put(methodName, methodChart);

        MethodsVBox methodsVBox = new MethodsVBox(methodChart, this);
        methodsPane.put(methodName, methodsVBox);
    }

    private void showMethodPane(String methodName) {
        splitPane.getItems().add(methodsPane.get(methodName));
    }


    private void drawContentsDepthPanel() {
        depthPane.getChildren().clear();
        double[] depthData = lasParser.getDeptData();

        for (int i = 0; i < depthData.length; i++) {
            double currentYPoint = i * depthMultiplier + 10;

            Line line = new Line(0, currentYPoint, 2, currentYPoint);
            line.setStrokeWidth(0.4);
            depthPane.getChildren().add(line);

            if (i % 5 == 0) {
                depthPane.getChildren().add(new Line(0, currentYPoint, 10, currentYPoint));
                Line line2 = new Line(0, currentYPoint, depthPane.getMaxWidth(), currentYPoint);
                line2.setStrokeWidth(0.1);
                depthPane.getChildren().add(line2);
                if (depthMultiplier >=2) {
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
                }
                if (i % 10 == 0 && depthMultiplier<2)
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
            }
        }
    }


    private void drawAllPanelsContent() {
        drawContentsDepthPanel();
        for (Node node : splitPane.getItems()) {
            ((Updatable) node).update();
        }
    }
}
