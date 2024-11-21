package view;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.*;
import services.Theme;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class View extends Application {

    private Stage primaryStage;
    private Locale locale;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        StageUtil.setCloseRequestHandler(stage);
        showLogin();
    }

    public void showLogin() {
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

            URL themeUrl = getClass().getResource(Theme.getTheme());
            if (themeUrl != null) {
                scene.getStylesheets().add(themeUrl.toExternalForm());
            } else {
                System.err.println("Theme URL is null");
            }

            URL logoUrl = getClass().getResource("/kuvat/safespacelogo.png");
            if (logoUrl != null) {
                primaryStage.getIcons().add(new Image(logoUrl.toExternalForm()));
            } else {
                System.err.println("Logo URL is null");
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
