package services;

import org.junit.*;
import java.io.IOException;
import java.net.http.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApiClientTest {
    public static HttpClient client;
    public static HttpResponse<String> res;
    private ApiClient apiClient;

    @BeforeClass
    public static void init() {
        client = mock(HttpClient.class);
        res = mock(HttpResponse.class);
    }

    @Before
    public void setUp() {
        apiClient = new ApiClient(client);
    }

    @AfterClass
    public static void tearDown() {
        client = null;
        res = null;
    }

    @Test
    public void postLoginTest() throws IOException, InterruptedException {
        String request = "{\"username\":\"testuser\",\"password\":\"password\"}";
        String expectation = "{\"jwt\": \"JWTTOKEN123\"}";

        when(res.body()).thenReturn(expectation);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(res);

        HttpResponse<String> response = apiClient.postLogin(request);

        String actualResponse = response.body();

        assertEquals(expectation, actualResponse);
    }

    @Test
    public void postRegisterTest() throws IOException, InterruptedException {
        String request = "{\"username\": \"tester\", \"password\": \"TEST\"}";
        String expectation = "{\"jwt\": \"JWT_TOKEN123\"}";

        when(res.body()).thenReturn(expectation);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(res);

        HttpResponse<String> response = apiClient.postRegister(request);

        String actualResponse = response.body();

        assertEquals(expectation, actualResponse);
    }
}