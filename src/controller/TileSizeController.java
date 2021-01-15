package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class TileSizeController implements Initializable {

    private MainController mainController;

    @FXML
    private Slider slider;

    @FXML
    private Label sizeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (InputStream input = new FileInputStream("resources/config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            double tileSizeRatio = Double.parseDouble(prop.getProperty("tile_size"));

            sizeLabel.setText(String.format("%.3f", tileSizeRatio) + "x");
            slider.setValue(tileSizeRatio);

        } catch (IOException e) {
            e.printStackTrace();
        }

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double sizeRatio = slider.getValue();

                sizeLabel.setText(String.format("%.3f", sizeRatio) + "x");
                mainController.setTileSizes(sizeRatio);
                saveSize(sizeRatio);
            }
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void saveSize(double size) {
        try {

            InputStream input = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(input);
            input.close();

            OutputStream output = new FileOutputStream("resources/config.properties");
            prop.setProperty("tile_size", Double.toString(size));
            prop.store(output, null);
            output.close();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
