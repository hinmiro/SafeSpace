package controller;

import javafx.fxml.*;
import javafx.stage.*;
import model.*;
import java.io.*;
import javafx.scene.control.*;

/**
 * Controller class for handling language selection in the application.
 */
public class LanguageSelectionController {

    private Stage stage;

    @FXML private Button englishButton;
    @FXML private Button finnishButton;
    @FXML private Button japaneseButton;
    @FXML private Button russianButton;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the action when the English button is clicked.
     */
    @FXML
    private void handleEnglish() {
        changeLanguage(Language.EN);
    }

    /**
     * Handles the action when the Finnish button is clicked.
     */
    @FXML
    private void handleFinnish() {
        changeLanguage(Language.FI);
    }

    /**
     * Handles the action when the Japanese button is clicked.
     */
    @FXML
    private void handleJapanese() {
        changeLanguage(Language.JP);
    }

    /**
     * Handles the action when the Russian button is clicked.
     */
    @FXML
    private void handleRussian() {
        changeLanguage(Language.RU);
    }

    /**
     * Initializes the controller. Highlights the selected language button.
     */
    @FXML
    public void initialize() {
        highlightSelectedLanguage();
    }

    /**
     * Changes the application language and refreshes the UI.
     *
     * @param language the language to change to
     */
    private void changeLanguage(Language language) {
        SessionManager.getInstance().setLanguage(language);
        refreshUI();
        stage.close();
    }

    /**
     * Refreshes the user interface to reflect the selected language.
     */
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

    /**
     * Highlights the button corresponding to the currently selected language.
     */
    private void highlightSelectedLanguage() {
        Language selectedLanguage = SessionManager.getInstance().getSelectedLanguage();
        String selectedLanguageStyle = "menuLanguageButtonSelected";
        switch (selectedLanguage) {
            case EN:
                englishButton.getStyleClass().add(selectedLanguageStyle);
                break;
            case FI:
                finnishButton.getStyleClass().add(selectedLanguageStyle);
                break;
            case JP:
                japaneseButton.getStyleClass().add(selectedLanguageStyle);
                break;
            case RU:
                russianButton.getStyleClass().add(selectedLanguageStyle);
                break;
        }
    }
}