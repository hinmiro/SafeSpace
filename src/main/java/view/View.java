package view;

import controller.LoginController;
import controller.ProfileController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.Language;
import model.ScreenUtil;
import model.SessionManager;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class View extends Application {

    private Stage primaryStage;
    private Locale locale;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        showLogin();
    }

    public void showLogin() throws Exception {
        try {
            if (SessionManager.getInstance().getSelectedLanguage() == null) {
                SessionManager.getInstance().setLanguage(Language.EN);
            }
            locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

            locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
            ResourceBundle titles = ResourceBundle.getBundle("PageTitles", locale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            LoginController loginController = fxmlLoader.getController();
            loginController.setMainView(this);

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            primaryStage.setScene(scene);
            primaryStage.setTitle(titles.getString("login"));
            primaryStage.setResizable(false);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/kuvat/safespacelogo.png")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProfile() throws Exception {
        try {
            if (SessionManager.getInstance().getSelectedLanguage() == null) {
                SessionManager.getInstance().setLanguage(Language.EN);
            }
            locale = SessionManager.getInstance().getSelectedLanguage().getLocale();

            locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
            ResourceBundle titles = ResourceBundle.getBundle("PageTitles", locale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = fxmlLoader.load();

            ProfileController profileController = fxmlLoader.getController();
            profileController.setMainView(this);

            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            primaryStage.setScene(scene);
            primaryStage.setTitle(titles.getString("profile"));
            primaryStage.setResizable(false);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/kuvat/safespacelogo.png")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {return primaryStage;}

    public static void main(String[] args) {
        launch(args);
    }
}
