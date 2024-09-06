package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class NewPostController {

    private ControllerForView controllerForView;

    private MainController mainController;

    @FXML
    private Button chooseImageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea captionTextArea;
    @FXML
    private Button postButton;
    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleClose() {
        mainController.closeNewWindow();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setControllerForView(ControllerForView controller) {
        this.controllerForView = controller;
    }
}
