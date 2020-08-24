package main.updatable;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.ContentAnchor.Lithology;

public class LithologyVBox extends VBox implements Updatable {
    private ScrollPane scrollPane = new ScrollPane();
    private AnchorPane anchorPane = new AnchorPane();
    private Lithology lithology;

    public LithologyVBox(Lithology lithology, ScrollBar vScroll){
        this.lithology = lithology;
        getChildren().add(new Button());
        getChildren().add(new Slider());
        getChildren().add(scrollPane);

        setScrollSettings(vScroll);

        setMaxWidth(50);
        setMinWidth(50);

        anchorPane.setStyle("-fx-background-color: grey");
        lithology.drawLithology(anchorPane);
    }

    private void setScrollSettings(ScrollBar vScroll){
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        setMargin(scrollPane, new Insets(10, 0, 0, 0));
        setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));

        scrollPane.setContent(anchorPane);
    }

    @Override
    public void update() {
        anchorPane.getChildren().clear();
        lithology.drawLithology(anchorPane);
    }

}
