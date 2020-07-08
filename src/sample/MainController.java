package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class MainController {

    @FXML
    VBox root;
    @FXML
    MenuItem openFile;
    @FXML
    Menu methodsMenu;


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
        chooser.setInitialDirectory(new File("D:\\Study\\Примеры для камертона"));
        File file = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());
        try {
            root.getChildren().remove(1);
        } catch (Exception exception) {}

        showContent(file);
    }

    private void showContent(File file) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/content.fxml"));
            root.getChildren().add(loader.load());
            ContentController contentController = (ContentController) loader.getController();



            for (String s : getMethods(file)) {
                if (s.equals("DEPT")) continue;
                contentController.addMethodPane(s);
                updateMethodMenu(s, contentController);
            }
            contentController.setData(getData(file));

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private HashMap<String, Double[]> getData(File file) {
        ArrayList<String[]> list = new ArrayList<>();
        HashMap<String, Double[]> map = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            boolean flag = false;
            while (reader.ready()) {
                String s = reader.readLine().trim();
                if (flag) {
                    list.add(s.split("\\s+"));
                }
                if (s.startsWith("~A")) {
                    flag = true;
                }
            }

            int i = 0;
            for (String s : getMethods(file)) {
                map.put(s, new Double[list.size()]);
                for (int j = 0; j < list.size(); j++) {
                    map.get(s)[j] = Double.parseDouble(list.get(j)[i]);
                }
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private ArrayList<String> getMethods(File file) {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            boolean flag = false;
            while (reader.ready()) {
                String s = reader.readLine();
                if (s.startsWith("#")) continue;
                if (flag) {
                    if (s.matches("^~[^C].+")) break;
                    if(s.toLowerCase().matches("^dept.*")) s = "DEPT";
                    list.add(s.split("\\.")[0].trim());
                }
                if (s.startsWith("~C")) {
                    flag = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @FXML
    private void updateMethodMenu(String s, ContentController contentController) {
        RadioMenuItem radioMenuItem = new RadioMenuItem(s.trim());
        radioMenuItem.setSelected(true);
        methodsMenu.getItems().add(radioMenuItem);

        radioMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (radioMenuItem.isSelected()) {
                    contentController.restorePanel(s);
                } else contentController.deletePanel(s);
            }
        });
    }

}
