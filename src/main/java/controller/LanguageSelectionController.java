package controller;

import javafx.fxml.*;
import javafx.stage.*;
import model.*;
import java.io.*;
import javafx.scene.control.*;

public class LanguageSelectionController {

    private Stage stage;

    @FXML private Button englishButton;
    @FXML private Button finnishButton;
    @FXML private Button japaneseButton;
    @FXML private Button russianButton;

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

    @FXML
    public void initialize() {
        highlightSelectedLanguage();
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
            loader.load();
            profileController = loader.getController();
            profileController.setMainController(SessionManager.getInstance().getMainController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void highlightSelectedLanguage() {
        Language selectedLanguage = SessionManager.getInstance().getSelectedLanguage();
        switch (selectedLanguage) {
            case EN:
                englishButton.getStyleClass().add("menuLanguageButtonSelected");
                break;
            case FI:
                finnishButton.getStyleClass().add("menuLanguageButtonSelected");
                break;
            case JP:
                japaneseButton.getStyleClass().add("menuLanguageButtonSelected");
                break;
            case RU:
                russianButton.getStyleClass().add("menuLanguageButtonSelected");
                break;
        }
    }
}