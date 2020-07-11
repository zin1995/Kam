package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class MethodChart {

    private String methodName;
    private Double[] methodData;
    private Double[] depthData;
    private int depthMultiplier;
    private Double widthMultiplier = 1.0;
    private Color color = Color.BLACK;

    public MethodChart(String methodName, Double[] methodData, Double[] depthData, int depthMultiplier) {
        this.methodName = methodName;
        this.methodData = methodData;
        this.depthData = depthData;
        this.depthMultiplier = depthMultiplier;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

    public void setWidthMultiplier(Double widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    public void drawChart(AnchorPane anchorPane) {
        Double lowerDepth = depthData[0];
        Double minXValue = getMinXValue();
        Double maxXValue = getMaxXValue();
        Double xDivider = (maxXValue - minXValue) / getWidth();
        if (minXValue == maxXValue) minXValue = 0.0;

        Double lastXPoint = (methodData[0] - minXValue) / xDivider;
        Double lastYPoint = 0.0;

        drawMouseCoordinate(anchorPane, lowerDepth, minXValue, xDivider);

        for (int i = 0; i < depthData.length; i++) {
            Double currentYPoint = (depthData[i] - lowerDepth) * depthMultiplier;
            Double currentXPoint = (methodData[i] - minXValue) / xDivider;
            if (currentXPoint < 0) currentXPoint = 0.0;

            if (i % 10 == 0) {
                Line line = new Line(0, currentYPoint, getWidth(), currentYPoint);
                line.setStrokeWidth(0.1);
                anchorPane.getChildren().add(line);
            }

            anchorPane.getChildren().add(new Line(lastXPoint, lastYPoint, currentXPoint, currentYPoint));
            lastXPoint = currentXPoint;
            lastYPoint = currentYPoint;

        }
    }

    private Double getMinXValue() {
        Double minXValue = Double.MAX_VALUE;

        for (Double xValue : methodData) {
            if (xValue < 0) continue;
            if (minXValue > xValue) minXValue = xValue;
        }

        return minXValue;
    }

    private Double getMaxXValue() {
        Double maxXValue = 0.0;

        for (Double xValue : methodData) {
            if (maxXValue < xValue) maxXValue = xValue;
        }

        return maxXValue;
    }

    public double getWidth() {
        return 150 * widthMultiplier;
    }


    private void drawMouseCoordinate(AnchorPane anchorPane, double lowerDepth,double minXValue, double xDivider){
        anchorPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                    anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                    anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                    anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                    anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                }
            }
        });

        anchorPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getY() / depthMultiplier > 0) {
                    if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                        anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                        anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                        anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                        anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1);
                    }
                    Line line1 = new Line(event.getX(), 0, event.getX(), event.getY());
                    line1.setStrokeWidth(0.1);
                    double xPoint = (methodData[(int)Math.round(event.getY()/depthMultiplier*10)] - minXValue) / xDivider;
                    Line line2 = new Line(xPoint, event.getY(), event.getX(), event.getY());
                    line2.setStrokeWidth(0.1);

                    anchorPane.getChildren().add(line1);
                    anchorPane.getChildren().add(line2);
                    anchorPane.getChildren().add(new Text(event.getX(), event.getY(), String.format("%.1f", event.getY() / depthMultiplier+lowerDepth)));
                    anchorPane.getChildren().add(new Text(event.getX(), event.getY()-10, methodData[(int)Math.round(event.getY()/depthMultiplier*10)]+""));
                }
            }
        });
    }

}
