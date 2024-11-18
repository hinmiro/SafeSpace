package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Language;
import model.ScreenUtil;
import model.SessionManager;
import services.Theme;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class LanguageSelectionController {

    private Stage stage;
    @FXML
    private Button englishButton;

    @FXML
    private Button finnishButton;

    @FXML
    private Button japaneseButton;

    @FXML
    private Button russianButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleEnglish() {
        changeLanguage(Language.EN);
    }

    @FXML
    private void handleFinnish() {
        changeLanguage(Language.FI);
    }

    @FXML
    private void handleJapanese() {
        changeLanguage(Language.JP);
    }

    @FXML
    private void handleRussian() {
        changeLanguage(Language.RU);
    }

    private void changeLanguage(Language language) {
        SessionManager.getInstance().setLanguage(language);
        refreshUI();
        stage.close();
    }

    private void refreshUI() {
        ProfileController profileController = SessionManager.getInstance().getProfileController();
        profileController.updateLanguage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();
            profileController = loader.getController();
            profileController.setMainController(SessionManager.getInstance().getMainController());

            Stage stage = new Stage();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", SessionManager.getInstance().getSelectedLanguage().getLocale());
            stage.setTitle(pageTitle.getString("profile"));
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            scene.getStylesheets().add(getClass().getResource(Theme.getTheme()).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}}