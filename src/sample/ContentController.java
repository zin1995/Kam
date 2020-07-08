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
    private HashMap<String, Double[]> data;
    private Double lowerDepth;
    private int depthDivider = 10;

    public void setData(HashMap<String, Double[]> data) {
        this.data = data;
        lowerDepth = data.get("DEPT")[0];
        drawAllPanelsContent(depthDivider);
    }



    @FXML
    public void initialize() {
        depthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                depthDivider = newValue.intValue();
                drawAllPanelsContent(depthDivider);
            }
        });
    }

    public void deletePanel(String s){
        splitPane.getItems().remove(methodsPane.get(s)[2]);
    }

    public void restorePanel(String s){
        splitPane.getItems().add(methodsPane.get(s)[2]);
    }


    public void addMethodPane(String s) {
        Node[] nodes = new Node[3];

        VBox vBox = new VBox();
        Slider slider = new Slider(0.001, 1, 1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        Label label = new Label(s);


        AnchorPane canvas = new AnchorPane();
        canvas.setPrefWidth(100);
        vBox.getChildren().add(slider);
        vBox.getChildren().add(label);
        vBox.getChildren().add(canvas);
        vBox.setMinWidth(0);
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
    }

    private void drawContentsMethodPanel(int depthDivider, Double methodDivider, String methodName) {
        ((AnchorPane) methodsPane.get(methodName)[0]).getChildren().clear();

        Double minXValue = Double.MAX_VALUE;
        for(Double xValue:  data.get(methodName)){
            if(xValue<0) continue;
            if(minXValue>xValue) minXValue = xValue;
        }
        minXValue /=methodDivider;

        Double lastXPoint = data.get(methodName)[0] / methodDivider - minXValue;
        Double lastYPoint = 0.0;
        Double maxDivider = ((Slider) methodsPane.get(methodName)[1]).getMax();


        for (int i = 0; i < data.get(methodName).length; i++) {
            Double currentDepth = data.get("DEPT")[i];
            Double currentYPoint = (currentDepth - lowerDepth) * depthDivider;
            Double currentXPoint = data.get(methodName)[i] / methodDivider - minXValue;
            if(currentXPoint<0) currentXPoint = 0.0;


            //draw method graphs
            ((AnchorPane) methodsPane.get(methodName)[0]).getChildren().add(new Line(lastXPoint, lastYPoint, currentXPoint, currentYPoint));
            lastXPoint = currentXPoint;
            lastYPoint = currentYPoint;

            if (maxDivider < data.get(methodName)[i] / 100) {
                maxDivider = data.get(methodName)[i] / 100;
                ((Slider) methodsPane.get(methodName)[1]).setMax(maxDivider);
                ((Slider) methodsPane.get(methodName)[1]).setMajorTickUnit(maxDivider);
            }
        }
    }

    private void drawContentsDepthPanel(int depthDivider) {
        depthPane.getChildren().clear();
        int i = 0;
        for (Double currentDepth : data.get("DEPT")) {
            Double currentYPoint = (currentDepth - lowerDepth) * depthDivider;

            //draw depth scale
            if (depthDivider>=10) {
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


    private void drawAllPanelsContent(int depthDivider) {
        for (Map.Entry<String, Node[]> pair : methodsPane.entrySet()) {
            drawContentsDepthPanel(depthDivider);
            drawContentsMethodPanel(depthDivider, ((Slider) pair.getValue()[1]).getValue(), pair.getKey());

            ((Slider) pair.getValue()[1]).valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    drawContentsMethodPanel(depthDivider, newValue.doubleValue(), pair.getKey());
                }
            });
        }
    }

}
