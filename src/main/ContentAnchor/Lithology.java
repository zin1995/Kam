package main.ContentAnchor;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lithology {
    private HashMap<String, ArrayList<int[]>> intervals;
    private int depthMultiplier = 2;
    private int depthLength;

    public Lithology(HashMap<String, ArrayList<int[]>> intervals, int depthLength) {
        this.intervals = intervals;
        this.depthLength = depthLength;
    }

    public void drawLithology(AnchorPane anchorPane) {
        anchorPane.setMinHeight(depthLength*depthMultiplier);
        for (Map.Entry<String, ArrayList<int[]>> pair : intervals.entrySet()) {
            for (int[] i : pair.getValue()) {
                double minHeight = i[1] * depthMultiplier - i[0] * depthMultiplier;
                double topAnchor = (double) i[0] * depthMultiplier;
                double botAnchor = depthLength*depthMultiplier - topAnchor - minHeight;

                AnchorPane lithologyAnchor = new LithologyAnchor(minHeight, topAnchor, botAnchor, pair.getKey());

                anchorPane.getChildren().add(lithologyAnchor);
            }
        }
    }


    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

}
