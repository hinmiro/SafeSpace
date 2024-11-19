package model;

import javafx.application.Platform;
import javafx.stage.Stage;

public class StageUtil {
    public static void setCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
