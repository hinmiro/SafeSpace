package model;

import com.google.gson.Gson;
import org.junit.*;
import org.mockito.*;
import services.ApiClient;
import java.io.IOException;
import java.net.http.*;
import java.util.HashMap;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SoftwareModelTest {
    public static HttpClient client;
    public static HttpResponse<String> res;
    private static Gson gson;
    private SoftwareModel softwareModel;
    private static MockedStatic<ApiClient> mockedApiClient;

    @BeforeClass
    public static void init() {
        client = mock(HttpClient.class);
        res = mock(HttpResponse.class);
        gson = new Gson();
        mockedApiClient = Mockito.mockStatic(ApiClient.class);
    }

    @Before
    public void setUp() {
        softwareModel = new SoftwareModel();
    }

    @Test
    public void loginTest() throws InterruptedException {
        String username = "testuser";
        String password = "password";
        String responseBody = "{\"jwt\": \"JWTTOKEN123\", \"username\": \"testuser\"}";
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        when(res.body()).thenReturn(responseBody);
        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.postLogin(any(String.class))).thenReturn(res);

        UserModel user = softwareModel.login(username, password);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("JWTTOKEN123", user.getJwt());
    }

    @Test
    public void registerTest() throws IOException, InterruptedException {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", "tester");
        data.put("password", "testword");
        String responseBody = "{\"jwt\": \"JWTTOKEN123\", \"username\": \"tester\"}";
        String jsonData = gson.toJson(data);

        when(res.body()).thenReturn(responseBody);
        when(res.statusCode()).thenReturn(201);
        mockedApiClient.when(() -> ApiClient.postRegister(jsonData)).thenReturn(res);

        UserModel user = softwareModel.postRegister("tester", "testword");

        assertNotNull(user);
        assertEquals("tester", user.getUsername());
        assertEquals("JWTTOKEN123", user.getJwt());
    }

    @Test
    public void createNewPostTest() throws IOException, InterruptedException {
        HashMap<String, String> data = new HashMap<>();
        data.put("post_content", "TESTINGTEEEEEEST");
        String jsonData = gson.toJson(data);

        when(res.statusCode()).thenReturn(201);
        mockedApiClient.when(() -> ApiClient.createPost(jsonData)).thenReturn(res);

        boolean result = softwareModel.createNewPost("TESTINGTEEEEEEST");

        assertTrue(result);
    }

    @Test
    public void updateUserTest() throws IOException, InterruptedException {
        String username = "newUser";
        String password = "newPassword";
        String bio = "Updated bio";
        String profilePictureId = "12345";
        String responseBody = "{\"username\": \"newUser1\", \"bio\": \"Updated bio\"}";

        when(res.body()).thenReturn(responseBody);
        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.updateUser(any(String.class))).thenReturn(res);

        boolean result = softwareModel.updateUser(username, password, bio, profilePictureId);

        assertTrue(result);
        UserModel loggedUser = SessionManager.getInstance().getLoggedUser();
        assertNotNull(loggedUser);
        assertEquals("newUser1", loggedUser.getUsername());
        assertEquals("Updated bio", loggedUser.getBio());
    }

    @Test
    public void getUserByNameTest() throws IOException, InterruptedException {
        String username = "testuser";
        String responseBody = "{\"username\": \"testuser\"}";

        when(res.body()).thenReturn(responseBody);
        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.getUserByName(username)).thenReturn(res);

        UserModel user = softwareModel.getUserByName(username);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void likePostTest() throws IOException, InterruptedException {
        String postId = "1";

        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.likePost(postId)).thenReturn(res);

        boolean result = softwareModel.likePost(postId);

        assertTrue(result);
    }

    @Test
    public void removeLikeTest() throws IOException, InterruptedException {
        String postId = "1";

        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.removeLike(postId)).thenReturn(res);

        boolean result = softwareModel.removeLike(postId);

        assertTrue(result);
    }

    @Test
    public void removeFriendSuccessTest() throws IOException, InterruptedException {
        int friendId = 2;

        when(res.statusCode()).thenReturn(200);
        mockedApiClient.when(() -> ApiClient.removeFriend(friendId)).thenReturn(res);

        boolean result = softwareModel.removeFriend(friendId);

        assertTrue(result);
    }

    @AfterClass
    public static void tearDown() {
        client = null;
        res = null;
        mockedApiClient.close();
    }
}