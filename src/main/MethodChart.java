package main;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class MethodChart {

    private String methodName;
    private double[] methodData;
    private double[] depthData;
    private double deptIncrement;
    private int depthMultiplier = 10;
    private Double widthMultiplier = 1.0;
    private Color color = Color.BLACK;

    public MethodChart(String methodName, double[] methodData, double[] depthData, double deptStep) {
        this.methodName = methodName;
        this.methodData = methodData;
        this.depthData = depthData;
        this.deptIncrement = 1 / deptStep;
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
        double lowerDepth = depthData[0];
        double minXValue = getMinXValue();
        double maxXValue = getMaxXValue();
        if (minXValue == maxXValue) minXValue = 0.0;
        double xDivider = (maxXValue - minXValue) / (150 * widthMultiplier);

        drawMouseCoordinate(anchorPane, lowerDepth, minXValue, xDivider);

        double lastXPoint = (methodData[0] - minXValue) / xDivider + 10;
        double lastYPoint = (depthData[0] - lowerDepth) * depthMultiplier;
        for (int i = 0; i < depthData.length; i++) {
            double currentYPoint = (depthData[i] - lowerDepth) * depthMultiplier;
            double currentXPoint = (methodData[i] - minXValue) / xDivider + 10;

            if (i % 10 == 0) {
                Line line = new Line(0, currentYPoint, getWidth(), currentYPoint);
                line.setStrokeWidth(0.1);
                anchorPane.getChildren().add(line);
            }

            Line line = new Line(lastXPoint, lastYPoint, currentXPoint, currentYPoint);
            line.setFill(color);
            line.setStroke(color);
            anchorPane.getChildren().add(line);
            lastXPoint = currentXPoint;
            lastYPoint = currentYPoint;
        }
    }


    private double getMinXValue() {
        double minXValue = Double.MAX_VALUE;
        for (Double xValue : methodData) {
            if (xValue < 0) continue;
            if (minXValue > xValue) minXValue = xValue;
        }
        return minXValue;
    }

    private double getMaxXValue() {
        double maxXValue = 0.0;
        for (Double xValue : methodData) {
            if (maxXValue < xValue) maxXValue = xValue;
        }
        return maxXValue;
    }

    public double getWidth() {
        return 150 * widthMultiplier + 20;
    }


    private void drawMouseCoordinate(AnchorPane anchorPane, double lowerDepth, double minXValue, double xDivider) {
        anchorPane.setOnMouseExited(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }
        });

        anchorPane.setOnMouseMoved(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }

            if (event.getY() / depthMultiplier > 0 && event.getY() / depthMultiplier * deptIncrement < depthData.length - 1) {
                Line line1 = new Line(event.getX(), 0, event.getX(), event.getY());
                line1.setStrokeWidth(0.1);
                double xPoint = (methodData[(int) Math.round(event.getY() / depthMultiplier * deptIncrement)] - minXValue) / xDivider + 10;
                Line line2 = new Line(xPoint, event.getY(), event.getX(), event.getY());
                line2.setStrokeWidth(0.1);

                anchorPane.getChildren().add(line1);
                anchorPane.getChildren().add(line2);
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 1, String.format("%.1f", event.getY() / depthMultiplier + lowerDepth)));
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 10, methodData[(int) Math.round(event.getY() / depthMultiplier * deptIncrement)] + ""));
            }
        });
    }

    @Override
    public String toString() {
        return methodName;
    }

    //    public void drawLithology(SplitPane splitPane){
//        double lev1 = 580.0;
//        double lev2 = 4.0;
//        double lev3 = 12.0;
//
//        double start = 0;
//        double end = 0;
//        boolean flag = false;
//        double lowerDepth = depthData[0];
//
//        for(int i = 0; i<methodData.length; i++){
//            if(methodData[i]>lev1 && !flag) {
//                AnchorPane anchorPane = new AnchorPane();
//                end = (depthData[i]-lowerDepth) * depthMultiplier;
//                anchorPane.setStyle("-fx-background-color: blue");
//                anchorPane.setMinHeight(end - start);
//                anchorPane.setMinWidth(20);
//                splitPane.getItems().add(anchorPane);
//                start = (depthData[i]-lowerDepth) * depthMultiplier;
//                flag = true;
//            }
//            if(methodData[i]<lev1 && flag) {
//                flag = false;
//                end = (depthData[i]-lowerDepth) * depthMultiplier;
//                AnchorPane anchorPane = new AnchorPane();
//                anchorPane.setStyle("-fx-background-color: red");
//                anchorPane.setMinHeight(end - start);
//                anchorPane.setMinWidth(20);
//                splitPane.getItems().add(anchorPane);
//            }
//        }
//        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setMinHeight((depthData[depthData.length-1]-lowerDepth)* depthMultiplier - end);
//        splitPane.getItems().add(anchorPane);
//    }

}
