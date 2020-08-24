package main.updatable;

import javafx.collections.ListChangeListener;
import javafx.stage.Modality;
import main.ContentAnchor.MethodChart;
import main.controllers.CombiningChartsController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.controllers.ContentController;

import java.io.IOException;
import static javafx.scene.layout.HBox.setHgrow;


public class MethodsVBox extends VBox implements Updatable {
    private HBox hBox = new HBox();
    private Button button = new Button("+");
    private ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    private ComboBox<MethodChart> comboBox = new ComboBox<>();
    private Slider slider = new Slider(1, 5, 1);
    private AnchorPane canvas = new AnchorPane();
    private ScrollPane scrollPane = new ScrollPane();
    private ContentController contentController;

    public MethodsVBox(MethodChart methodChart, ContentController contentController) {
        this.contentController = contentController;

        setHBoxSettings();
        setButtonSettings();
        setComboBoxSettings(methodChart);
        setColorPickerSettings();
        setCanvasSettings();
        setSliderSettings();

        setScrollSettings(contentController.getVScroll());


        getChildren().add(hBox);
        getChildren().add(slider);
        getChildren().add(scrollPane);
    }

    private void setHBoxSettings(){
        hBox.getChildren().add(button);
        hBox.getChildren().add(colorPicker);
        hBox.getChildren().add(comboBox);
    }


    private void setButtonSettings(){
        button.setMinWidth(24);
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/combiningCharts.fxml"));
                Stage stage = loader.load();
                CombiningChartsController combiningChartsController = loader.getController();
                combiningChartsController.setComboBox(comboBox);
                combiningChartsController.setMap(contentController.getChartMap());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setColorPickerSettings(){
        colorPicker.setMaxWidth(28);
        colorPicker.setMinWidth(28);
        colorPicker.setOnAction(event -> {
            comboBox.getValue().setColor(colorPicker.getValue());
            update();
        });
    }

    private void setComboBoxSettings(MethodChart methodChart) {
        comboBox.getItems().add(methodChart);
        comboBox.setValue(methodChart);
        setVgrow(comboBox, Priority.ALWAYS);
        setHgrow(comboBox, Priority.ALWAYS);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getItems().addListener((ListChangeListener<MethodChart>) c -> update());
        comboBox.setOnAction(event -> {
            update();
            colorPicker.setValue(comboBox.getValue().getColor());
        });
    }

    private void setCanvasSettings() {
        setVgrow(canvas, Priority.ALWAYS);
        canvas.setStyle("-fx-background-insets: 0");
    }

    private void setSliderSettings() {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            comboBox.getValue().setWidthMultiplier(newValue.doubleValue());
            setMinWidth(comboBox.getValue().getWidth());
            setMaxWidth(comboBox.getValue().getWidth());
            scrollPane.setMinWidth(comboBox.getValue().getWidth());
            scrollPane.setMaxWidth(comboBox.getValue().getWidth());
            update();
        });
    }

    private void setScrollSettings(ScrollBar vScroll) {
        setVgrow(scrollPane, Priority.ALWAYS);
        setMargin(scrollPane, new Insets(10, 0, 0, 0));
        scrollPane.setMinWidth(comboBox.getValue().getWidth());
        scrollPane.setMaxWidth(comboBox.getValue().getWidth());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-insets: 0");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(canvas);
        scrollPane.setVmax(vScroll.getMax());
        scrollPane.setVmin(vScroll.getMin());
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }


    public void update() {
        MethodChart currentMethodChart = comboBox.getValue();
        canvas.getChildren().clear();
        for(MethodChart methodChart: comboBox.getItems()){
            if(!methodChart.equals(currentMethodChart)){
                methodChart.drawChart(canvas);
            }
        }
        currentMethodChart.drawChart(canvas);
    }

}
