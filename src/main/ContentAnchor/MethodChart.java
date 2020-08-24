package main.ContentAnchor;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import java.util.HashMap;

public class MethodChart {

    private String methodName;
    private double[] methodData;
    private double[] depthData;
    private int depthMultiplier = 2;
    private Double widthMultiplier = 1.0;
    private Color color = Color.BLACK;
    private HashMap<String, boolean[]> lithologyData = new HashMap<>();
    private double maxXValue;
    private double minXValue;

    public MethodChart(String methodName, double[] methodData, double[] depthData) {
        this.methodName = methodName;
        this.methodData = methodData;
        this.depthData = depthData;
        maxXValue = calculateMaxValue();
        minXValue = calculateMinValue();
    }

    public HashMap<String, boolean[]> getLithologyData() {
        return lithologyData;
    }

    public int getDepthLength() {
        return depthData.length;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

    public void setWidthMultiplier(double widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    public void drawChart(AnchorPane anchorPane) {
        if (minXValue == maxXValue) minXValue = 0.0;
        double xDivider = (maxXValue - minXValue) / (150 * widthMultiplier);

        drawMouseCoordinate(anchorPane, xDivider);

        double lastXPoint = (methodData[0] - minXValue) / xDivider + 10;
        double lastYPoint = 0;
        for (int i = 0; i < depthData.length; i++) {
            double currentYPoint = i * depthMultiplier;
            double currentXPoint = (methodData[i] - minXValue) / xDivider + 10;

            if (i % 5 == 0) {
                Line line = new Line(0, currentYPoint, getWidth(), currentYPoint);
                line.setStrokeWidth(0.1);
                anchorPane.getChildren().add(line);
            }

            Line line = new Line(lastXPoint, lastYPoint, currentXPoint, currentYPoint);
            line.setStroke(color);
            anchorPane.getChildren().add(line);
            lastXPoint = currentXPoint;
            lastYPoint = currentYPoint;
        }
    }


    private double calculateMinValue() {
        double minXValue = Double.MAX_VALUE;
        for (Double xValue : methodData) {
            if (xValue < 0) continue;
            if (minXValue > xValue) minXValue = xValue;
        }
        return minXValue;
    }

    private double calculateMaxValue() {
        double maxXValue = 0.0;
        for (Double xValue : methodData) {
            if (maxXValue < xValue) maxXValue = xValue;
        }
        return maxXValue;
    }

    public double getWidth() {
        return 150 * widthMultiplier + 20;
    }


    private void drawMouseCoordinate(AnchorPane anchorPane, double xDivider) {
        anchorPane.setOnMouseExited(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }
        });

        anchorPane.setOnMouseMoved(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }

            if (event.getY() / depthMultiplier > 0 && event.getY() / depthMultiplier < depthData.length - 1) {
                Line line1 = new Line(event.getX(), 0, event.getX(), event.getY());
                line1.setStrokeWidth(0.1);
                double xPoint = (methodData[(int) Math.round(event.getY() / depthMultiplier)] - minXValue) / xDivider + 10;
                Line line2 = new Line(xPoint, event.getY(), event.getX(), event.getY());
                line2.setStrokeWidth(0.1);

                anchorPane.getChildren().add(line1);
                anchorPane.getChildren().add(line2);
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 1, String.format("%.1f", event.getY() / depthMultiplier)));
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 10, methodData[(int) Math.round(event.getY() / depthMultiplier)] + ""));
            }
        });
    }

    public void calculateLithologyData(String[] lithologyInterval) {
        boolean[] lithologyDataArray = new boolean[methodData.length];
        double start = Double.parseDouble(lithologyInterval[0]);
        double end = Double.parseDouble(lithologyInterval[1]);
        for (int i = 0; i < methodData.length; i++) {
            if (methodData[i] >= start && methodData[i] < end) {
                lithologyDataArray[i] = true;
            }
            if (methodData[i] < start || methodData[i] >= end) {
                lithologyDataArray[i] = false;
            }
        }
        lithologyData.put(lithologyInterval[2], lithologyDataArray);
    }


    @Override
    public String toString() {
        return methodName;
    }

}
