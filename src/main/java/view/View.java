package view;

import controller.LoginController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import services.ApiClient;

import java.io.IOException;

public class View extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        showLogin();
    }

    public void showLogin() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            LoginController loginController = fxmlLoader.getController();
            loginController.setMainView(this);

            Scene scene = new Scene(root, 360, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Page");
            primaryStage.setResizable(false);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/kuvat/safespacelogo.png")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMain() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = fxmlLoader.load();

            MainController mainController = fxmlLoader.getController();
            mainController.setMainView(this);

            Scene scene = new Scene(root, 360, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Page");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
