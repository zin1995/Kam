package sample;

import java.util.HashMap;
import java.util.Map;

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
    Map<String, MethodChart> chartMap = new HashMap<>();
    private Double[] deptData;
    private int depthMultiplier = 10;
    private Double XPointDivider = 1.0;

    public void setDeptData(Double[] deptData) {
        this.deptData = deptData;
        drawContentsDepthPanel();
    }


    @FXML
    public void initialize() {
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
        splitPane.getItems().remove(methodsPane.get(s)[2]);
    }

    public void restorePanel(String s){
        splitPane.getItems().add(methodsPane.get(s)[2]);
    }


    public void addMethodPane(String s, Double[] methodData) {
        Node[] nodes = new Node[3];
        MethodChart methodChart = new MethodChart(s, methodData, deptData, depthMultiplier, XPointDivider);
        chartMap.put(s, methodChart);

        VBox vBox = new VBox();
        Slider slider = new Slider(0.001, 1, 1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                XPointDivider = newValue.doubleValue();
                chartMap.get(s).setXPointDivider(XPointDivider);
                drawContentsMethodPanel(s);
            }
        });
        setMaxSliderValue(slider, methodData);
        Label label = new Label(s);
        AnchorPane canvas = new AnchorPane();
        canvas.setPrefWidth(100);

        vBox.getChildren().add(slider);
        vBox.getChildren().add(label);
        vBox.getChildren().add(canvas);
        vBox.setMinWidth(200);
        vBox.setMaxWidth(200);
        label.setAlignment(CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        vBox.setVgrow(label, Priority.ALWAYS);
        vBox.setVgrow(canvas, Priority.ALWAYS);

        nodes[0] = canvas;
        nodes[1] = slider;
        nodes[2] = vBox;
        methodsPane.put(s, nodes);
        splitPane.getItems().add(vBox);
        methodChart.drawChart(canvas);
    }

    private void setMaxSliderValue(Slider slider, Double[] methodData){
        Double maxDivider = slider.getMax();

        for (int i = 0; i < methodData.length; i++) {
            if (maxDivider < methodData[i] / 100) {
                maxDivider = methodData[i] / 100;
                slider.setMax(maxDivider);
                slider.setMajorTickUnit(maxDivider);
            }
        }
    }

    private void drawContentsMethodPanel(String methodName) {
        AnchorPane anchorPane = (AnchorPane) methodsPane.get(methodName)[0];
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
