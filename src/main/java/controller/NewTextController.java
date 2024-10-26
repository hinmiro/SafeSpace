package controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ScreenUtil;
import model.SessionManager;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewTextController {

    private ControllerForView controllerForView = ControllerForView.getInstance();
    private MainController mainController;
    private Locale locale;
    private ResourceBundle titles;
    private ResourceBundle buttons;
    private ResourceBundle labels;
    private ResourceBundle alerts;

    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea textPostArea;
    @FXML
    private Label inspirationText;
    @FXML
    private Label inspirationLabel;
    @FXML
    public Label textPostLabel;


    @FXML
    private void initialize() {
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
        titles = ResourceBundle.getBundle("PageTitles", locale);
        buttons = ResourceBundle.getBundle("Buttons", locale);
        labels = ResourceBundle.getBundle("Labels", locale);
        alerts = ResourceBundle.getBundle("Alerts", locale);

        closeButton.setOnAction(event -> handleClose());
        postButton.setOnAction(event -> handlePost());
        setRandomQuote();

        inspirationText.setText(labels.getString("inspText"));
        inspirationLabel.setText(labels.getString("inspQuestion"));
        textPostLabel.setText(labels.getString("textPost"));
        postButton.setText(buttons.getString("postButton"));
        textPostArea.setPromptText(labels.getString("writeHere"));
    }

    @FXML
    private void handlePost() {
        String postText = textPostArea.getText().trim();

        if (postText.isEmpty()) {
            showAlert(alerts.getString("noTextPost"));
            return;
        }

        try {
            boolean res = controllerForView.sendPost(postText);
            if (res) {
                showAlert(alerts.getString("newPostSuccess"));
                handleClose();
            } else {
                showAlert(alerts.getString("sendPostErr"));
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
            stage.setTitle(titles.getString("mainPage"));
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
                labels.getString("randomQuote1"),
                labels.getString("randomQuote2"),
                labels.getString("randomQuote3"),
                labels.getString("randomQuote4")
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
        alert.setTitle(alerts.getString("info"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
