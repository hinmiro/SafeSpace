package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Language;
import model.SessionManager;
import model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

public class NewTextController extends ApplicationTest {
    private Button postButton;
    private Button closeButton;
    private ImageView imageView;
    private Button chooseImageButton;
    private TextArea captionTextArea;
}
