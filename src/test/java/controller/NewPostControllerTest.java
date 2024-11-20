package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Language;
import model.Post;
import model.SessionManager;
import model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

public class NewPostControllerTest extends ApplicationTest {
    private Button postButton;
    private Button closeButton;
    private ImageView imageView;
    private Button chooseImageButton;
    private TextArea captionTextArea;

    @Before
    public void initializeSessionManager() {
        SessionManager.setLanguage(Language.EN);
        SessionManager.getInstance().setLoggedUser(new UserModel("sara", 115, "2024-09-27"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newPost.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        postButton = lookup("#postButton").queryAs(Button.class);
        closeButton = lookup("#closeButton").queryAs(Button.class);
        imageView = lookup("#imageView").queryAs(ImageView.class);
        chooseImageButton = lookup("#chooseImageButton").queryAs(Button.class);
        captionTextArea = lookup("#captionTextArea").queryAs(TextArea.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#postButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#closeButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#imageView", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#chooseImageButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#captionTextArea", NodeMatchers.isVisible());
        sleep(1000);
    }

    @Test
    public void testPostButtonSuccess() {
        verifyThat("#captionTextArea", NodeMatchers.isVisible());
        verifyThat("#chooseImageButton", NodeMatchers.isVisible());
        clickOn(chooseImageButton);

        clickOn(captionTextArea).write("This is a test post");
        assertThat(captionTextArea.getText()).isEqualTo("This is a test post");
    }

}