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
import java.util.logging.Logger;

/**
 * The View class extends Application and serves as the main entry point for the JavaFX application.
 */
public class View extends Application {

    private Stage primaryStage;
    private static final Logger logger = Logger.getLogger(View.class.getName());

    /**
     * The start method is called when the application is launched. It sets up the primary stage and shows the login view.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the login view
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        StageUtil.setCloseRequestHandler(stage);
        showLogin();
    }

    /**
     * Shows the login view. It loads the login FXML, sets the scene, and applies the theme and icon.
     */
    public void showLogin() {
        Locale locale;
        try {
            if (SessionManager.getInstance().getSelectedLanguage() == null) {
                SessionManager.getInstance().setLanguage(Language.EN);
            }

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
                logger.warning("Theme URL is null");
            }

            URL logoUrl = getClass().getResource("/kuvat/safespacelogo.png");
            if (logoUrl != null) {
                primaryStage.getIcons().add(new Image(logoUrl.toExternalForm()));
            } else {
                logger.warning("Logo URL is null");
            }

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}