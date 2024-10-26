package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SharedData;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewTextController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private SharedData language = SharedData.getInstance();
    private ResourceBundle bundle;
    private Locale currentLocale;
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
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handlePost());
        setRandomQuote();
    }

    @FXML
    private void changeLanguage() {
        if (languageBox.getValue().equals(bundle.getString("language.fi"))) {
            currentLocale = Locale.forLanguageTag("fi");
        } else {
            currentLocale = Locale.forLanguageTag("en");
        }

        SharedData.getInstance().setCurrentLocale(currentLocale);
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
        //updateTexts();
    }

    @FXML
    private void handlePost() {
        String postText = textPostArea.getText().trim();

        if (postText.isEmpty()) {
            showAlert("Please enter some text before posting.");
            return;
        }

        try {
            boolean res = controllerForView.sendPost(postText);
            if (res) {
                showAlert("New post sent!");
                handleClose();
            } else {
                showAlert("Spectacular error has occurred...");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        //closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();

            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setTitle("Main Page");
            Scene scene = new Scene(root, 360, ScreenUtil.getScaledHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setRandomQuote() {
        String[] inspirations = {
                "Share an insightful question that you’d like others to answer.",
                "Recommend something that has helped you feel inspired.",
                "Describe something you're grateful for today.",
                "Share a moment when you felt accomplished."
        };
        int randomIndex = (int) (Math.random() * inspirations.length);
        inspirationText.setText(inspirations[randomIndex]);
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
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
