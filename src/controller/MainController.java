package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import launcher.CustomEventHandler;
import launcher.LaunchProgramButton;
import model.ProgramData;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private ArrayList<ProgramData> programs = new ArrayList<>();

    private double tileSizeRatio;

    @FXML
    private BorderPane mainWindowBorderPane;

    @FXML
    private CheckMenuItem showNameButton;

    @FXML
    private TilePane tilePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (InputStream input = new FileInputStream("resources/config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            tileSizeRatio = Double.parseDouble(prop.getProperty("tile_size"));
            showNameButton.setSelected(Boolean.parseBoolean(prop.getProperty("show_names")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            programs = DatabaseController.getTableData();
            createTiles();
        } catch (SQLException e) {
            handleDataBaseException(e);
        }
    }

    public ArrayList<ProgramData> getPrograms() {
        return programs;
    }

    private void createTiles() {
        tilePane.getChildren().clear();
        for (ProgramData d: programs) {
            if (d.getName() == null)
                continue;
            LaunchProgramButton lpb = new LaunchProgramButton(d.getName(), d.getPath(), tileSizeRatio * 150);
            lpb.setNameVisibility(showNameButton.isSelected());
            setRemoveHandler(lpb);

            tilePane.getChildren().add(lpb);
        }
    }

    public void addTile(ProgramData data) {
        programs.add(data);
        try {
            DatabaseController.saveTableData(programs);
            createTiles();
        } catch (SQLException e) {
            handleDataBaseException(e);
        }
    }

    public void setTileSizes(double sizeRatio) {
        for (Node n: tilePane.getChildren()) {
            LaunchProgramButton lpb = (LaunchProgramButton) n;
            lpb.setSize(sizeRatio * 150);
            tileSizeRatio = sizeRatio;
        }
    }

    private void showNames(boolean visible) {
        for (Node n: tilePane.getChildren()) {
            LaunchProgramButton lpb = (LaunchProgramButton) n;
            lpb.setNameVisibility(visible);
        }

        try  {
            InputStream input = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(input);
            input.close();

            FileOutputStream output = new FileOutputStream("resources/config.properties");
            prop.setProperty("show_names", Boolean.toString(visible));
            prop.store(output, null);
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleDataBaseException(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("DataBase Error");
        alert.setHeaderText("There was an error accessing the database");
        alert.setContentText(null);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        TextArea textArea = new TextArea(sw.toString());

        alert.getDialogPane().setExpandableContent(textArea);

        alert.showAndWait();
    }

    private void setRemoveHandler(LaunchProgramButton lpb) {

        lpb.setRemoveButtonHandler(new CustomEventHandler() {
            @Override
            public void handle(Event event) {
                for (int i = 0; i < programs.size(); i++) {
                    if (programs.get(i).getName().equals(getText())) {
                        programs.remove(i);
                        try {
                            DatabaseController.deleteEntry(getText());
                        } catch (SQLException e) {
                            handleDataBaseException(e);
                        }
                        break;
                    }
                }
                createTiles();
            }
        });

    }

    @FXML
    private void newButtonHandler(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewWindow.fxml"));
        Parent root = loader.load();

        NewWindowController nwc = loader.getController();
        nwc.setMainController(this);

        Stage newStage = new Stage();
        newStage.setTitle("New Program");

        newStage.setScene(new Scene(root, 383, 146));

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainWindowBorderPane.getScene().getWindow());
        newStage.setResizable(false);

        newStage.showAndWait();

    }

    @FXML
    private void closeButtonHandler(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void deleteAllButtonHandler(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete All?");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete all entries?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(yesButton)) {
            programs.clear();
            try {
                DatabaseController.deleteAll();
            } catch (SQLException e) {
                handleDataBaseException(e);
            }
            createTiles();
        }

    }

    @FXML
    private void tileSizeButtonHandler(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TileSize.fxml"));
        Parent root = loader.load();

        TileSizeController nwc = loader.getController();
        nwc.setMainController(this);

        Stage newStage = new Stage();
        newStage.setTitle("Tile Size");

        newStage.setScene(new Scene(root, 275, 80));

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(mainWindowBorderPane.getScene().getWindow());
        newStage.setResizable(false);

        newStage.showAndWait();

    }

    @FXML
    private void showNameHandler(ActionEvent actionEvent) {
        showNames(showNameButton.isSelected());
    }

}
