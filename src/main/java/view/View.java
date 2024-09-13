package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import services.ApiClient;

public class View extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        showLogin();
    }

    public void showLogin() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 350, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/kuvat/safespacelogo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
