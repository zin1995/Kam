package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import main.ContentAnchor.Lithology;
import main.ContentAnchor.MethodChart;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class LithologyController {
    @FXML
    VBox firstVBox;
    @FXML
    VBox secondVBox;
    @FXML
    ComboBox<MethodChart> comboBox1;
    @FXML
    ComboBox<MethodChart> comboBox2;
    @FXML
    ComboBox<MethodChart> comboBox3;
    @FXML
    Stage stage;


    private ArrayList<HBox> firstPaneHBox = new ArrayList<>();
    private ArrayList<HBox> secondPaneHBox = new ArrayList<>();
    private ContentController contentController;

    @FXML
    public void initialize() {
        addLithologyVariable(firstVBox, createTextField());
        addLithologyVariable2(secondVBox, createTextField(), createTextField());
    }

    @FXML
    private void OkAction(ActionEvent actionEvent) {
        if (comboBox1.getValue() != null) {
            for (HBox hBox : firstPaneHBox) {
                TextField tf1 = (TextField) hBox.getChildren().get(0);
                TextField tf2 = (TextField) hBox.getChildren().get(1);
                ComboBox<String> lithologyComboBox = (ComboBox<String>) hBox.getChildren().get(3);
                if (tf1.getText() != null && tf2.getText() != null && lithologyComboBox.getValue() != null) {
                    String[] lithologyInterval = new String[3];
                    lithologyInterval[0] = tf1.getText();
                    lithologyInterval[1] = tf2.getText();
                    lithologyInterval[2] = lithologyComboBox.getValue();
                    comboBox1.getValue().calculateLithologyData(lithologyInterval);
                }
            }
            Lithology lithology = new Lithology(calculateLithologyIntervals(comboBox1.getValue().getLithologyData()), comboBox1.getValue().getDepthLength());
            contentController.addLithologyPane(comboBox1.getValue().toString(), lithology);
        }

        if (comboBox2.getValue() != null && comboBox3.getValue() != null) {
            for (HBox hBox : secondPaneHBox) {
                TextField tf1 = (TextField) hBox.getChildren().get(1);
                TextField tf2 = (TextField) hBox.getChildren().get(2);
                TextField tf3 = (TextField) hBox.getChildren().get(5);
                TextField tf4 = (TextField) hBox.getChildren().get(6);
                ComboBox<String> lithologyComboBox = (ComboBox<String>) hBox.getChildren().get(8);
                if (tf1.getText() != null && tf2.getText() != null && tf3.getText() != null && tf4.getText() != null && lithologyComboBox.getValue() != null) {
                    String[] lithologyInterval = new String[3];
                    lithologyInterval[0] = tf1.getText();
                    lithologyInterval[1] = tf2.getText();
                    lithologyInterval[2] = lithologyComboBox.getValue();
                    comboBox2.getValue().calculateLithologyData(lithologyInterval);
                    String[] lithologyInterval2 = new String[3];
                    lithologyInterval2[0] = tf3.getText();
                    lithologyInterval2[1] = tf4.getText();
                    lithologyInterval2[2] = lithologyComboBox.getValue();
                    comboBox3.getValue().calculateLithologyData(lithologyInterval2);
                }
            }
            Lithology lithology = new Lithology(calculateLithologyIntervals(comboBox2.getValue().getLithologyData(), comboBox3.getValue().getLithologyData()), comboBox3.getValue().getDepthLength());
            contentController.addLithologyPane(comboBox2.getValue().toString() + "+" + comboBox3.getValue().toString(), lithology);
        }
        stage.close();

    }

    private HashMap<String, ArrayList<int[]>> calculateLithologyIntervals(HashMap<String, boolean[]>... lithologyData) {
        HashMap<String, ArrayList<int[]>> lithologyIntervals = new HashMap();

        if (lithologyData.length == 1) {
            HashMap<String, boolean[]> map = lithologyData[0];
            for (Map.Entry<String, boolean[]> pair : map.entrySet()) {
                ArrayList<int[]> intervals = new ArrayList<>();
                boolean flag = false;
                boolean[] dataArray = pair.getValue();
                for (int i = 0; i < dataArray.length; i++) {
                    if (dataArray[i] && !flag) {
                        int[] interval = new int[2];
                        interval[0] = i;
                        interval[1] = dataArray.length;
                        intervals.add(interval);
                        flag = true;
                    }
                    if (!dataArray[i] && flag) {
                        intervals.get(intervals.size() - 1)[1] = i;
                        flag = false;
                    }
                }
                lithologyIntervals.put(pair.getKey(), intervals);
            }
        }

        if (lithologyData.length == 2) {
            for (String methodName : lithologyData[0].keySet()) {
                ArrayList<int[]> intervals = new ArrayList<>();
                boolean flag = false;
                boolean[] dataArray = lithologyData[0].get(methodName);
                boolean[] dataArray2 = lithologyData[1].get(methodName);
                for (int i = 0; i < dataArray.length; i++) {
                    if (dataArray[i] && dataArray2[i] && !flag) {
                        int[] interval = new int[2];
                        interval[0] = i;
                        interval[1] = dataArray.length;
                        intervals.add(interval);
                        flag = true;
                    }
                    if ((!dataArray[i] || !dataArray2[i]) && flag) {
                        intervals.get(intervals.size() - 1)[1] = i;
                        flag = false;
                    }
                }
                lithologyIntervals.put(methodName, intervals);
            }
        }

        return lithologyIntervals;
    }


    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter(), 0.0, change -> {
            Pattern DIGIT_PATTERN = Pattern.compile("\\d*\\.*\\d*");
            return DIGIT_PATTERN.matcher(change.getText()).matches() ? change : null;
        }));
        return textField;
    }

    private ComboBox<String> createLithologyComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setMaxWidth(200);
        File[] files = new File("src/resources/image").listFiles();
        for (File file : files) {
            comboBox.getItems().add(file.getName());
        }
        return comboBox;
    }


    public void setContentController(ContentController contentController) {
        this.contentController = contentController;
        comboBox1.getItems().addAll(contentController.getChartMap().values());
        comboBox1.setOnAction(event -> {
            comboBox2.setValue(null);
            comboBox3.setValue(null);
            secondPaneHBox.clear();
            secondVBox.getChildren().clear();
            addLithologyVariable2(secondVBox, createTextField(), createTextField());
        });
        comboBox2.getItems().addAll(contentController.getChartMap().values());
        comboBox2.setOnAction(event -> {
            comboBox1.setValue(null);
            firstPaneHBox.clear();
            firstVBox.getChildren().clear();
            addLithologyVariable(firstVBox, createTextField());
        });
        comboBox3.getItems().addAll(contentController.getChartMap().values());
        comboBox3.setOnAction(event -> {
            comboBox1.setValue(null);
            firstPaneHBox.clear();
            firstVBox.getChildren().clear();
            addLithologyVariable(firstVBox, createTextField());
        });
    }

    private void addLithologyVariable(VBox vBox, TextField tf) {
        HBox hBox = new HBox();

        TextField textField = createTextField();
        hBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(tf.getText());
        hBox.getChildren().add(textField);

        TextField textField2 = createTextField();
        hBox.setHgrow(textField2, Priority.ALWAYS);
        textField2.setText("999999");
        hBox.getChildren().add(textField2);
        hBox.getChildren().add(new Separator(Orientation.VERTICAL));

        ComboBox<String> comboBox = createLithologyComboBox();
        hBox.setHgrow(comboBox, Priority.ALWAYS);
        hBox.getChildren().add(comboBox);

        Button button = new Button("+");
        button.setMinWidth(24);
        button.setOnAction(event -> addLithologyVariable(vBox, textField2));
        hBox.getChildren().add(button);

        vBox.getChildren().add(hBox);
        firstPaneHBox.add(hBox);
    }

    private void addLithologyVariable2(VBox vBox, TextField tf, TextField tf2) {
        HBox hBox = new HBox();

        Label labK1 = new Label("K1");
        labK1.setMinWidth(Region.USE_PREF_SIZE);
        hBox.getChildren().add(labK1);

        TextField textField = createTextField();
        hBox.setHgrow(textField, Priority.ALWAYS);
        textField.setText(tf.getText());
        hBox.getChildren().add(textField);

        TextField textField2 = createTextField();
        hBox.setHgrow(textField2, Priority.ALWAYS);
        textField2.setText("999999");
        hBox.getChildren().add(textField2);
        hBox.getChildren().add(new Separator(Orientation.VERTICAL));

        Label labK2 = new Label("K2");
        labK2.setMinWidth(Region.USE_PREF_SIZE);
        hBox.getChildren().add(labK2);

        TextField textField3 = createTextField();
        hBox.setHgrow(textField3, Priority.ALWAYS);
        textField3.setText(tf2.getText());
        hBox.getChildren().add(textField3);

        TextField textField4 = createTextField();
        hBox.setHgrow(textField4, Priority.ALWAYS);
        textField4.setText("999999");
        hBox.getChildren().add(textField4);
        hBox.getChildren().add(new Separator(Orientation.VERTICAL));

        ComboBox<String> comboBox = createLithologyComboBox();
        hBox.setHgrow(comboBox, Priority.ALWAYS);
        hBox.getChildren().add(comboBox);

        Button button = new Button("+");
        button.setMinWidth(24);
        button.setOnAction(event -> addLithologyVariable2(vBox, textField2, textField4));
        hBox.getChildren().add(button);

        vBox.getChildren().add(hBox);
        secondPaneHBox.add(hBox);
    }

    public void CloseAction(ActionEvent actionEvent) {
        stage.close();
    }
}
