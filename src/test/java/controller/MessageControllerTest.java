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

public class MessageControllerTest extends ApplicationTest {
    private Button homeButton;
    private Button profileButton;
    private Button leaveMessageButton;
    UserModel user;

    @Before
    public void initializeSessionManager() {
        SessionManager.setLanguage(Language.EN);
        user = new UserModel("sara", 115, "2024-09-27");
        SessionManager.getInstance().setLoggedUser(user);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/messages.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        homeButton = lookup("#homeButton").queryAs(Button.class);
        profileButton = lookup("#profileButton").queryAs(Button.class);
        leaveMessageButton = lookup("#leaveMessageButton").queryAs(Button.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#homeButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#profileButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#leaveMessageButton", NodeMatchers.isVisible());
        sleep(1000);
    }

}
