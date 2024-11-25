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

    @AfterClass
    public static void tearDown() {
        client = null;
        res = null;
        mockedApiClient.close();
    }
}