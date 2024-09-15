package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class NewTextController {

    private ControllerForView controllerForView;
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
    private void initialize() {
        closeButton.setOnAction(event -> handleClose());
        setRandomQuote();
    }

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setControllerForView(controllerForView);

            Stage stage = new Stage();
            stage.setTitle("Main Page");
            Scene scene = new Scene(root, 360, 800);
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
    private void boldText() {
        //applyStyleToText("-fx-font-weight", "bold");
    }

    @FXML
    private void italicText() {
        //applyStyleToText("-fx-font-style", "italic");
    }

    @FXML
    private void addList() {
        String currentText = textPostArea.getText();
        textPostArea.setText(currentText + "\n• ");
    }

    /*private void applyStyleToText(String styleProperty, String styleValue) {
        String selectedText = textPostArea.getSelectedText();
        if (!selectedText.isEmpty()) {
            String replacement = String.format("[style=%s:%s]%s[/style]", styleProperty, styleValue, selectedText);
            textPostArea.replaceSelection(replacement);
        }
    }*/

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setControllerForView(ControllerForView controllerForView) {
        this.controllerForView = controllerForView;
    }
}
