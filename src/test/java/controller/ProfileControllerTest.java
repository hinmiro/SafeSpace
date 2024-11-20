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

public class ProfileControllerTest extends ApplicationTest {
    private Button profileButton;
    private Button homeButton;
    private ImageView profileImageView;
    private Label usernameLabel;
    private Button settingsProfileID;
    private Label registeredLabel;
    private Label bioLabel;
    private Label followersCountLabel;
    private Label followingCountLabel;
    private ListView<Post> feedListView;

    @Before
    public void initializeSessionManager() {
        SessionManager.setLanguage(Language.EN);
        SessionManager.getInstance().getLoggedUser().setUsername("sara");
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        profileButton = lookup("#profileButton").queryAs(Button.class);
        homeButton = lookup("#homeButton").queryAs(Button.class);
        profileImageView = lookup("#profileImageView").queryAs(ImageView.class);
        usernameLabel = lookup("#usernameLabel").queryAs(Label.class);
        settingsProfileID = lookup("#settingsProfileID").queryAs(Button.class);
        registeredLabel = lookup("#registeredLabel").queryAs(Label.class);
        bioLabel = lookup("#bioLabel").queryAs(Label.class);
        followersCountLabel = lookup("#followersCountLabel").queryAs(Label.class);
        followingCountLabel = lookup("#followingCountLabel").queryAs(Label.class);
        feedListView = lookup("#feedListView").queryAs(ListView.class);
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#profileButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#homeButton", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#profileImageView", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#usernameLabel", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#settingsProfileID", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#registeredLabel", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#bioLabel", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#followersCountLabel", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#followingCountLabel", NodeMatchers.isVisible());
        sleep(1000);
        verifyThat("#feedListView", NodeMatchers.isVisible());
        sleep(1000);
    }
}
