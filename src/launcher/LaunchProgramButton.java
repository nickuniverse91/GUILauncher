package launcher;

import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LaunchProgramButton extends Region {

    private double size;

    private Pane programButton = new Pane();
    private ImageView removeButton = new ImageView();

    private Rectangle rect;
    private ImageView programIcon = new ImageView();
    private Label programName = new Label();

    public LaunchProgramButton(String name, String path, double size) {
        super();

        this.size = size;

        rect = new Rectangle(size, size, Color.WHITE);
        setIcon(path);
        setName(name);

        programName.setFont(Font.font(14));
        programName.setAlignment(Pos.CENTER);
        setLabelPos();

        rect.setStroke(Color.DARKGRAY);
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStrokeWidth(3);

        removeButton.setFitWidth(24);
        removeButton.setFitHeight(24);
        removeButton.setLayoutX(size-12);
        removeButton.setLayoutY(-12);
        removeButton.setVisible(false);
        removeButton.setImage(new Image(getClass().getResourceAsStream("/close button.png")));

        programButton.getChildren().addAll(rect, programIcon, programName);

        this.getChildren().addAll(programButton, removeButton);

        programButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Runtime run = Runtime.getRuntime();
                try {
                    run.exec(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rect.setFill(Color.LIGHTGRAY);
                animateRemoveButton(true);
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rect.setFill(Color.WHITE);
                animateRemoveButton(false);
            }
        });

        removeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        removeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

    }

    public void animateRemoveButton(boolean grow) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), removeButton);
        removeButton.setVisible(true);
        if (grow) {
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
        } else {
            st.setFromX(1);
            st.setFromY(1);
            st.setToX(0);
            st.setToY(0);
        }
        st.play();
    }

    public void setIcon(String path) {
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(new File(path));
        ImageIcon imgIcon = (ImageIcon) icon;
        BufferedImage bi = new BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(), BufferedImage.TRANSLUCENT);
        Graphics g = bi.createGraphics();
        imgIcon.paintIcon(null, g, 0, 0);
        g.dispose();

        Image img = SwingFXUtils.toFXImage(bi, null);

        programIcon.setImage(img);
        programIcon.setFitWidth(64);
        programIcon.setFitHeight(64);
        setIconPos();
    }

    private void setIconPos() {
        programIcon.setLayoutX(size/2-32);
        programIcon.setLayoutY(size/2-32);
    }

    private void setLabelPos() {
        programName.setPrefWidth(size);
        programName.setLayoutY(size-25);
    }

    public void setName(String name) {
        programName.setText(name);
    }

    public String getName() {
        return programName.getText();
    }

    public void setRemoveButtonHandler(CustomEventHandler handler) {
        handler.setText(programName.getText());
        removeButton.setOnMouseClicked(handler);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        rect.setWidth(size);
        rect.setHeight(size);
        setIconPos();
        setLabelPos();
        removeButton.setLayoutX(size-12);
    }

    public void setNameVisibility(boolean visible) {
        programName.setVisible(visible);
    }

}
