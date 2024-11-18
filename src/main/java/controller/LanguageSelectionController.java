package controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Language;
import model.SessionManager;

public class LanguageSelectionController {

    private Stage stage;

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
        // Refresh the UI to reflect the new language
        refreshUI();
        stage.close();
    }

    private void refreshUI() {
        // Assuming you have a method in your main controller to refresh the UI
        MainController mainController = SessionManager.getInstance().getMainController();
        mainController.updateLanguage();


    }
}