package sample;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    @FXML
    ScrollPane depthScroll;
    @FXML
    ScrollPane methodScroll;


    private HashMap<String, Node[]> methodsPane = new HashMap<>();
    private HashMap<String, MethodChart> chartMap = new HashMap<>();
    private Double[] depthData;
    private int depthMultiplier = 10;

    public void setDepthData(Double[] depthData) {
        this.depthData = depthData;
        drawContentsDepthPanel();
    }


    @FXML
    public void initialize() {

        methodScroll.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                depthScroll.setVvalue(newValue.doubleValue());
            }
        });
        depthScroll.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                methodScroll.setVvalue(newValue.doubleValue());
            }
        });

        depthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                depthMultiplier = newValue.intValue();
                for (MethodChart methodChart : chartMap.values()) {
                    methodChart.setDepthMultiplier(depthMultiplier);
                }
                drawAllPanelsContent();
            }
        });
    }

    public void deletePanel(String s) {
        splitPane.getItems().remove(methodsPane.get(s)[0]);
        double size = splitPane.getItems().size();
        for(int i = 0; i < size; i++){
            splitPane.setDividerPosition(i, 1/size);
        }
    }

    public void restorePanel(String s) {
        splitPane.getItems().add(methodsPane.get(s)[0]);
        double size = splitPane.getItems().size();
        for(int i = 0; i < size; i++){
            splitPane.setDividerPosition(i, 1/size);
        }
    }

    private void setSliderSettings(Slider slider, String methodName, VBox vBox, MethodChart methodChart) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chartMap.get(methodName).setXPointDivider(newValue.doubleValue());
                vBox.setMinWidth(methodChart.getWidth());
                vBox.setMaxWidth(methodChart.getWidth());
                drawContentsMethodPanel(methodName);
            }
        });
    }

    private void setLabelSettings(Label label) {
        label.setAlignment(CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
    }


    public void addMethodPane(String methodName, Double[] methodData) {
        Node[] nodes = new Node[3];
        MethodChart methodChart = new MethodChart(methodName, methodData, depthData, depthMultiplier);
        chartMap.put(methodName, methodChart);

        VBox vBox = new VBox();
        AnchorPane canvas = new AnchorPane();
        canvas.setMaxWidth(methodChart.getWidth());
        canvas.setMinWidth(methodChart.getWidth());

        Slider slider = new Slider(1, 10, 1);
        setSliderSettings(slider, methodName, vBox, methodChart);

        Label label = new Label(methodName);
        setLabelSettings(label);


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
        Double lowerDepth = depthData[0];


        for (int i = 0; i < depthData.length; i++) {
            Double currentYPoint = (depthData[i] - lowerDepth) * depthMultiplier;


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
        for (String methodName : methodsPane.keySet()) {
            drawContentsDepthPanel();
            drawContentsMethodPanel(methodName);
        }
    }

}
