package sample;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import static javafx.geometry.Pos.CENTER;

public class ContentController {

    @FXML
    SplitPane splitPane;
    @FXML
    AnchorPane depthPane;
    @FXML
    Slider depthSlider;


    private HashMap<String, Node[]> methodsPane = new HashMap<>();
    private HashMap<String, MethodChart> chartMap = new HashMap<>();
    private Double[] deptData;
    private int depthMultiplier = 10;
    private Double XPointDivider = 1.0;

    public void setDeptData(Double[] deptData) {
        this.deptData = deptData;
        drawContentsDepthPanel();
    }


    @FXML
    public void initialize() {
        depthPane.setMinWidth(100);
        depthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                depthMultiplier = newValue.intValue();
                for(MethodChart methodChart: chartMap.values()){
                    methodChart.setDepthMultiplier(depthMultiplier);
                }
                drawAllPanelsContent();
            }
        });
    }

    public void deletePanel(String s){
        splitPane.getItems().remove(methodsPane.get(s)[0]);
    }

    public void restorePanel(String s){
        splitPane.getItems().add(methodsPane.get(s)[0]);
    }

    private void setSliderSettings(Slider slider, String methodName, VBox vBox, MethodChart methodChart){
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                XPointDivider = newValue.doubleValue();
                chartMap.get(methodName).setXPointDivider(XPointDivider);
                vBox.setMinWidth(methodChart.getWidth());
                drawContentsMethodPanel(methodName);
            }
        });
    }

    private void setLabelSettings(Label label){
        label.setAlignment(CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
    }


    public void addMethodPane(String methodName, Double[] methodData) {
        Node[] nodes = new Node[3];
        MethodChart methodChart = new MethodChart(methodName, methodData, deptData, depthMultiplier, XPointDivider);
        chartMap.put(methodName, methodChart);

        VBox vBox = new VBox();

        Slider slider = new Slider(1, 10, 1);
        setSliderSettings(slider, methodName, vBox, methodChart);

        Label label = new Label(methodName);
        setLabelSettings(label);

        AnchorPane canvas = new AnchorPane();

        vBox.setVgrow(label, Priority.ALWAYS);
        vBox.setVgrow(canvas, Priority.ALWAYS);
        vBox.getChildren().add(slider);
        vBox.getChildren().add(label);
        vBox.getChildren().add(canvas);

        nodes[0] = vBox;
        nodes[1] = slider;
        nodes[2] = canvas;
        methodsPane.put(methodName, nodes);
        splitPane.getItems().add(vBox);

        methodChart.drawChart(canvas);
    }


    private void drawContentsMethodPanel(String methodName) {
        AnchorPane anchorPane = (AnchorPane) methodsPane.get(methodName)[2];
        anchorPane.getChildren().clear();

        chartMap.get(methodName).drawChart(anchorPane);
    }

    private void drawContentsDepthPanel() {
        depthPane.getChildren().clear();
        Double lowerDepth = deptData[0];
        int i = 0;
        for (Double currentDepth : deptData) {
            Double currentYPoint = (currentDepth - lowerDepth) * depthMultiplier;

            //draw depth scale
            if (depthMultiplier >=10) {
                if(i==0 || i==10){
                    depthPane.getChildren().addAll(new Line(0, currentYPoint, 10, currentYPoint));
                    depthPane.getChildren().addAll(new Text(10, currentYPoint, currentDepth + ""));
                }
                if(i==5 || i == 15){
                    depthPane.getChildren().addAll(new Line(0, currentYPoint, 5, currentYPoint));
                }
            } else {
                if(i==0){
                    depthPane.getChildren().addAll(new Line(0, currentYPoint, 10, currentYPoint));
                    depthPane.getChildren().addAll(new Text(10, currentYPoint, currentDepth + ""));
                }
            }
            i++;
            if(i==20) i = 0;
        }
    }


    private void drawAllPanelsContent() {
        for (String methodName: methodsPane.keySet()) {
            drawContentsDepthPanel();
            drawContentsMethodPanel(methodName);
        }
    }

}
