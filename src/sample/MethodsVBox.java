package sample;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;

public class MethodsVBox extends VBox {

    private MethodChart methodChart;
    Slider slider = new Slider(1, 10, 1);
    Label label = new Label();
    AnchorPane canvas = new AnchorPane();

    public MethodsVBox(String methodName, MethodChart methodChart){
        this.methodChart = methodChart;
        canvas.setMaxWidth(Double.MAX_VALUE);
        canvas.setMinWidth(methodChart.getWidth());
        canvas.setPadding(new Insets(0, 50, 0, 50));

        setSliderSettings(slider, methodChart, canvas);
        setLabelSettings(label, methodName);


        setVgrow(label, Priority.ALWAYS);
        setVgrow(canvas, Priority.ALWAYS);
        getChildren().add(slider);
        getChildren().add(label);
        getChildren().add(canvas);
    }

    private void setSliderSettings(Slider slider, MethodChart methodChart, AnchorPane canvas) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            methodChart.setWidthMultiplier(newValue.doubleValue());
            setMinWidth(methodChart.getWidth());
            methodChart.drawChart(canvas);
        });
    }

    private void setLabelSettings(Label label, String methodName) {
        label.setText(methodName);
        label.setAlignment(CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
    }

    public void update(){
        methodChart.drawChart(canvas);
    }

}
