package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class MainControllerTest extends ApplicationTest {
    private Button newPostButton;
    private Button profileButton;
    private Button createPicPostButton;
    private Button createTextPostButton;
    private Button leaveMessageButton;
    private Button homeButton;
    private Button searchButton;
    private TextField usernameSearchField;
    private RadioButton friendsOption;
    private RadioButton allOption;
    private ListView<Post> feedListView;

    @Before
    public void initializeSessionManager() {
        SessionManager.getInstance().setLanguage(Language.EN);
        SessionManager.getInstance().setLoggedUser(new UserModel("sara", 115, "2024-09-27"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Before
    public void setUp() {
        newPostButton = lookup("#newPostButton").queryAs(Button.class);
        profileButton = lookup("#profileButton").queryAs(Button.class);
        createPicPostButton = lookup("#createPicPostButton").queryAs(Button.class);
        createTextPostButton = lookup("#createTextPostButton").queryAs(Button.class);
        leaveMessageButton = lookup("#leaveMessageButton").queryAs(Button.class);
        homeButton = lookup("#homeButton").queryAs(Button.class);
        searchButton = lookup("#searchButton").queryAs(Button.class);
        usernameSearchField = lookup("#usernameSearchField").queryAs(TextField.class);
        friendsOption = lookup("#friendsOption").queryAs(RadioButton.class);
        allOption = lookup("#allOption").queryAs(RadioButton.class);
        feedListView = lookup("#feedListView").queryAs(ListView.class);
    }

    @Test
    public void testFeedListView() {
        verifyThat("#feedListView", NodeMatchers.isVisible());
        assertThat(feedListView).isNotNull();
    }

    @Test
    public void testThatElementsExist() {
        verifyThat("#newPostButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#profileButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#createPicPostButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#createTextPostButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#leaveMessageButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#homeButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#searchButton", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#usernameSearchField", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#friendsOption", NodeMatchers.isVisible());
        sleep(500);
        verifyThat("#allOption", NodeMatchers.isVisible());
        sleep(500);
    }

    @Test
    public void testSearchButton() {
        verifyThat("#searchButton", NodeMatchers.isVisible());
        clickOn(usernameSearchField).write("test");
        verifyThat(usernameSearchField, NodeMatchers.isVisible());
        clickOn(searchButton);
        sleep(500);
        assertThat(searchButton).isNotNull();
    }

    @Test
    public void testFriendsOption() {
        verifyThat("#friendsOption", NodeMatchers.isVisible());
        clickOn(friendsOption);
        sleep(500);
        assertThat(friendsOption).isNotNull();
        assertThat(friendsOption.getText()).isEqualTo("Following");
    }

    @Test
    public void testAllOption() {
        verifyThat("#allOption", NodeMatchers.isVisible());
        clickOn(allOption);
        sleep(500);
        assertThat(allOption).isNotNull();
        assertThat(allOption.getText()).isEqualTo("All");
    }

    @Test
    public void testNewPostButton() {
        verifyThat("#newPostButton", NodeMatchers.isVisible());
        clickOn(newPostButton);
        sleep(500);
        clickOn(newPostButton);
        sleep(500);
        assertThat(newPostButton).isNotNull();
        assertThat(newPostButton.getText()).isEqualTo("+");
    }

    @Test
    public void testProfileButton() {
        verifyThat("#profileButton", NodeMatchers.isVisible());
        clickOn(profileButton);
        sleep(500);
        assertThat(lookup("#profileButton").queryAs(Button.class)).isNotNull();
        assertThat(lookup("#profileButton").queryAs(Button.class).getText()).isEqualTo("Profile");
    }

    @Test
    public void testCreatePicPostButton() {
        clickOn(newPostButton);
        sleep(500);
        verifyThat("#createPicPostButton", NodeMatchers.isVisible());
        clickOn(createPicPostButton);
        sleep(500);
        assertThat(createPicPostButton).isNotNull();
        assertThat(createPicPostButton.getText()).isEqualTo("Share an image");
    }

    @Test
    public void testCreateTextPostButton() {
        clickOn(newPostButton);
        sleep(500);
        verifyThat("#createTextPostButton", NodeMatchers.isVisible());
        clickOn(createTextPostButton);
        sleep(500);
        assertThat(createTextPostButton).isNotNull();
        assertThat(createTextPostButton.getText()).isEqualTo("Share your thoughts");
    }

    @Test
    public void testLeaveMessageButton() {
        verifyThat("#leaveMessageButton", NodeMatchers.isVisible());
        clickOn(leaveMessageButton);
        sleep(500);
        assertThat(leaveMessageButton).isNotNull();
    }

    @Test
    public void testHomeButton() {
        verifyThat("#homeButton", NodeMatchers.isVisible());
        clickOn(homeButton);
        sleep(500);
        assertThat(homeButton).isNotNull();
        assertThat(homeButton.getText()).isEqualTo("Home");
    }

}
