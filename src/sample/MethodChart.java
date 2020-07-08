package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MethodChart {

    private String methodName;
    private Double[] methodData;
    private Double[] depthData;
    private int depthMultiplier;
    private Double XPointDivider;
    private Color color = Color.BLACK;

    public MethodChart(String methodName, Double[] methodData, Double[] depthData, int depthMultiplier, Double xPointDivider) {
        this.methodName = methodName;
        this.methodData = methodData;
        this.depthData = depthData;
        this.depthMultiplier = depthMultiplier;
        this.XPointDivider = xPointDivider;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

    public void setXPointDivider(Double XPointDivider) {
        this.XPointDivider = XPointDivider;
    }

    public void drawChart(AnchorPane anchorPane) {
        Double lowerDepth = depthData[0];
        Double minXValue = getMinXValue();
        Double maxXValue = getMaxXValue();
        if(minXValue==maxXValue) minXValue = 0.0;
        Double lastXPoint = (methodData[0] - minXValue) / ((maxXValue - minXValue) / (150*XPointDivider));
        Double lastYPoint = 0.0;


        for (int i = 0; i < depthData.length; i++) {
            Double currentDepth = depthData[i];
            Double currentYPoint = (currentDepth - lowerDepth) * depthMultiplier;
            Double currentXPoint = (methodData[i] - minXValue) / ((maxXValue - minXValue) / (150*XPointDivider));
            if (currentXPoint < 0) currentXPoint = 0.0;

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

    public double getWidth(){
        return 150*XPointDivider;
    }


}
