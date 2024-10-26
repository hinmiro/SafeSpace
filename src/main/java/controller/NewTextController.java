package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Language;
import model.ScreenUtil;
import model.SessionManager;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewTextController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private ResourceBundle alerts;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle fields;
    private Locale locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    private MainController mainController;

    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea textPostArea;
    @FXML
    private Label inspirationText;
    @FXML
    private ComboBox<String> languageBox;
    @FXML
    private Label needInspo;
    @FXML
    private Label whatOnMind;

    @FXML
    private void initialize() {
        languageBox.getItems().setAll(
                Language.EN.getDisplayName(),
                Language.FI.getDisplayName()
        );

        Language currentLanguage = SessionManager.getInstance().getSelectedLanguage();
        languageBox.setValue(currentLanguage == Language.FI ? Language.FI.getDisplayName() : Language.EN.getDisplayName());

        languageBox.setOnAction(event -> changeLanguage());
        updateLanguage();

        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handlePost());
        setRandomQuote();
    }

    private void updateTexts() {
        postButton.setText(buttons.getString("post"));
        needInspo.setText(labels.getString("needInspo"));
        whatOnMind.setText(labels.getString("whatOnMind"));
    }

    @FXML
    private void changeLanguage() {
        String selectedLanguage = languageBox.getValue();

        if (selectedLanguage.equals(Language.FI.getDisplayName())) {
            SessionManager.getInstance().setLanguage(Language.FI);
        } else {
            SessionManager.getInstance().setLanguage(Language.EN);
        }

        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        updateLanguage();
    }

    private void updateLanguage() {
        alerts = ResourceBundle.getBundle("Alerts", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        fields = ResourceBundle.getBundle("Fields", locale);
        updateTexts();
    }

    @FXML
    private void handlePost() {
        String postText = textPostArea.getText().trim();

        if (postText.isEmpty()) {
            showAlert(alerts.getString("post.empty"));
            return;
        }

        try {
            boolean res = controllerForView.sendPost(postText);
            if (res) {
                showAlert(alerts.getString("post.success"));
                handleClose();
            } else {
                showAlert(alerts.getString("post.error"));
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();

            Stage stage = (Stage) closeButton.getScene().getWindow();
            ResourceBundle pageTitle = ResourceBundle.getBundle("PageTitles", locale);
            stage.setTitle(pageTitle.getString("main"));
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setRandomQuote() {

        String[] inspirationsArray = new String[] {
                labels.getString("inspiration1"),
                labels.getString("inspiration2"),
                labels.getString("inspiration3"),
                labels.getString("inspiration4")
        };

        int randomIndex = (int) (Math.random() * inspirationsArray.length);
        inspirationText.setText(inspirationsArray[randomIndex]);
    }


    @FXML
    private void insertEmojiSmile() {
        insertEmoji("😊");
    }

    @FXML
    private void insertEmojiThumbsUp() {
        insertEmoji("👍");
    }

    @FXML
    private void insertEmojiLaugh() {
        insertEmoji("😂");
    }

    @FXML
    private void insertEmojiStar() {
        insertEmoji("🌟");
    }

    private void insertEmoji(String emoji) {
        int caretPosition = textPostArea.getCaretPosition();
        textPostArea.insertText(caretPosition, emoji);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alerts.getString("post.information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}