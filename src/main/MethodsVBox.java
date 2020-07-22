package main;

import main.controllers.AddingStageController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


public class MethodsVBox extends VBox {
    private HBox hBox = new HBox();
    private Button button = new Button("1");
    private ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    private Slider slider = new Slider(1, 5, 1);
    private ComboBox<MethodChart> comboBox = new ComboBox<>();
    private AnchorPane canvas = new AnchorPane();
    private ScrollPane scrollPane = new ScrollPane();
    ContentController contentController;

    public MethodsVBox(MethodChart methodChart, ScrollBar vScroll, ContentController contentController) {
        this.contentController = contentController;
        comboBox.getItems().add(methodChart);
        comboBox.setValue(methodChart);
        methodChart.setColor(colorPicker.getValue());

        setCanvasSettings();
        setSliderSettings(slider);
        setScrollSettings(vScroll);
        setComboBoxSettings(comboBox);
        setColorPickerSettings();
        setButtonSettings(button);

        setMinWidth(methodChart.getWidth());
        setMaxWidth(methodChart.getWidth());
        setVgrow(comboBox, Priority.ALWAYS);
        setVgrow(canvas, Priority.ALWAYS);
        setVgrow(scrollPane, Priority.ALWAYS);
        setHgrow(comboBox, Priority.ALWAYS);
        setMargin(scrollPane, new Insets(10, 0, 0, 0));


        hBox.getChildren().add(button);
        hBox.getChildren().add(colorPicker);
        hBox.getChildren().add(comboBox);


        getChildren().add(hBox);
        getChildren().add(slider);
        getChildren().add(scrollPane);
    }

    public void addMethodChart(MethodChart methodChart){
        comboBox.getItems().add(methodChart);
        update();
    }

    private void setButtonSettings(Button button){
        button.setMaxWidth(20);
        button.setOnAction(event -> {
            Stage stage = new Stage();
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/addingStage.fxml"));
                root = loader.load();
                stage.setScene(new Scene(root, 300, 300));
                AddingStageController addingStageController = loader.getController();
                addingStageController.setComboBox(comboBox);
                addingStageController.setMap(contentController.getChartMap());
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

    private void setComboBoxSettings(ComboBox<MethodChart> comboBox) {
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setOnAction(event -> {
            update();
            colorPicker.setValue(comboBox.getValue().getColor());
        });
    }

    private void setCanvasSettings() {
        canvas.setStyle("-fx-background-insets: 0");
    }

    private void setSliderSettings(Slider slider) {
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
