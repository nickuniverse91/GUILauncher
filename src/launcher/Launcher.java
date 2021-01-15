package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

class Main {

    public static void main(String[] args) {
        Launcher.main(args);
    }

}

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Launcher.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("GUI Launcher");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Tux.png")));
        primaryStage.setScene(new Scene(root, 640, 480));

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
