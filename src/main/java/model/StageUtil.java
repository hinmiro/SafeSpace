package model;

import javafx.application.Platform;
import javafx.stage.Stage;

public class StageUtil {

    /**
     * Sets the close request handler for the given stage.
     * When the stage is closed, the application will exit.
     *
     * @param stage the stage to set the close request handler for
     */
    public static void setCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}