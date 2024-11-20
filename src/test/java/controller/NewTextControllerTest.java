package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class NewTextControllerTest extends ApplicationTest {
    private Button postButton;
    private Button closeButton;
    private TextArea textPostArea;
    private Label inspirationText;

    @Before
    public void initializeSessionManager() {
        SessionManager.setLanguage(Language.EN);
        SessionManager.getInstance().setLoggedUser(new UserModel("sara", 115, "2024-09-27"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newText.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        postButton = lookup("#postButton").queryAs(Button.class);
        closeButton = lookup("#closeButton").queryAs(Button.class);
        textPostArea = lookup("#textPostArea").queryAs(TextArea.class);
        inspirationText = lookup("#inspirationText").queryAs(Label.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#postButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#closeButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#textPostArea", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#inspirationText", NodeMatchers.isVisible());
        sleep(500);
    }

    @Test
    public void testPostButtonSuccess() { // why not working?!
        clickOn("#textPostArea").write("This is a test post");
        clickOn("#postButton");
        sleep(500);
        assertThat(textPostArea.getText()).isEqualTo("This is a test post");
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("Post created successfully!");
    }

    @Test
    public void testCloseButton() {
        verifyThat("#closeButton", NodeMatchers.isVisible());
        clickOn(closeButton);
        sleep(500);
    }

    @Test
    public void testPostButtonFail() {
        clickOn("#postButton");
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("Please enter some text before posting.");
    }

    @Test
    public void testPostButtonError() {
        clickOn("#textPostArea").write("This is a test post");
        clickOn("#postButton");
        sleep(500);
        clickOn("#postButton");
        verifyThat(".alert", NodeMatchers.isVisible());
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertThat(dialogPane.getContentText()).isEqualTo("An error occurred while sending the post.");
    }

    @Test
    public void testInsertEmoji() {
        clickOn("#textPostArea").write("This is a test post");
        clickOn("#textPostArea").write("\uD83D\uDE00");
        assertThat(textPostArea.getText()).isEqualTo("This is a test post😀");
    }
}
