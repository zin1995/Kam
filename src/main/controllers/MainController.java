package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.LasParser;


import java.io.*;


public class MainController {

    @FXML
    VBox root;
    @FXML
    MenuItem openFile;
    @FXML
    Menu methodsMenu;
    @FXML
    Menu editMenu;

    @FXML
    public void initialize() {

    }

    @FXML
    private void openFile(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".las", "*.las"),
                new FileChooser.ExtensionFilter("All", "*.*"));
        chooser.setInitialDirectory(new File("D:\\Study\\������� ��� ���������"));
        File file = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());

        if (file != null) {
            if (root.getChildren().size() > 1) root.getChildren().remove(1);

            addContent(file);
        }
    }

    private void addContent(File file) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/content.fxml"));
            root.getChildren().add(loader.load());
            ContentController contentController = loader.getController();

            LasParser lasParser = new LasParser(file);
            contentController.setParser(lasParser);
            updateMethodMenu(lasParser, contentController);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void updateMethodMenu(LasParser lasParser, ContentController contentController) {
        for (String s : lasParser.getFullMethodsNames()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(s.trim());
            radioMenuItem.setSelected(true);
            methodsMenu.getItems().add(radioMenuItem);
            radioMenuItem.setOnAction(event -> {
                if (radioMenuItem.isSelected()) {
                    contentController.restorePanel(s);
                } else contentController.deletePanel(s);
            });
        }

        if (lasParser.getStitchedMethodsData().size() > 0) {
            Menu stitchedMethodsMenu = new Menu("������ ������");
            editMenu.getItems().add(1, stitchedMethodsMenu);
            for (String s : lasParser.getStitchedMethodsData().keySet()) {
                RadioMenuItem radioMenuItem = new RadioMenuItem(s.trim());
                stitchedMethodsMenu.getItems().add(radioMenuItem);
                radioMenuItem.setOnAction(event -> {
                    if (radioMenuItem.isSelected()) {
                        contentController.restorePanel(s);
                    } else contentController.deletePanel(s);
                });
            }
        }
    }

}
