package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;

public class MethodsVBox extends VBox {

    private MethodChart methodChart;
    private Slider slider = new Slider(1, 10, 1);
    private Label label = new Label();
    private AnchorPane canvas = new AnchorPane();
    private ScrollPane scrollPane = new ScrollPane();

    public MethodsVBox(String methodName, MethodChart methodChart, ScrollBar vScroll) {
        this.methodChart = methodChart;
        setMinWidth(methodChart.getWidth());
        setMaxWidth(methodChart.getWidth());

        setCanvasSettings();
        setSliderSettings(slider, methodChart, canvas);
        setLabelSettings(label, methodName);
        setScrollSettings(vScroll);

        setVgrow(label, Priority.ALWAYS);
        setVgrow(canvas, Priority.ALWAYS);
        setMargin(scrollPane, new Insets(10, 0, 0, 0));
        setMargin(canvas, new Insets(10, 0, 0, 0));

        getChildren().add(label);
        getChildren().add(slider);
        getChildren().add(scrollPane);
    }

    private void setCanvasSettings(){
        canvas.setStyle("-fx-background-insets: 0");
    }

    private void setSliderSettings(Slider slider, MethodChart methodChart, AnchorPane canvas) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            methodChart.setWidthMultiplier(newValue.doubleValue());
            scrollPane.setMinWidth(methodChart.getWidth());
            scrollPane.setMaxWidth(methodChart.getWidth());
            setMinWidth(methodChart.getWidth());
            setMaxWidth(methodChart.getWidth());
            methodChart.drawChart(canvas);
        });
    }

    private void setScrollSettings(ScrollBar vScroll){
        scrollPane.setMinWidth(methodChart.getWidth());
        scrollPane.setMaxWidth(methodChart.getWidth());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-insets: 0");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(canvas);
        scrollPane.setVmax(vScroll.getMax());
        scrollPane.setVmin(vScroll.getMin());
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }

    private void setLabelSettings(Label label, String methodName) {
        label.setText(methodName);
        label.setAlignment(CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
    }


    public void update() {
        methodChart.drawChart(canvas);
    }

}
