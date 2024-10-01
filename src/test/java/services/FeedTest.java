package services;

import com.google.gson.Gson;
import model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedTest {

    private static final String MOCK_URL = "http://mockurl/api/v1/events";
    private Feed feed;
    private BlockingQueue<Post> queue;
    private Gson gson;
    private HttpClient mockClient;

    @Before
    public void setUp() throws Exception {
        queue = new LinkedBlockingQueue<>();
        gson = new Gson();
        mockClient = mock(HttpClient.class);
        feed = new Feed();

        SessionManager mockSessionManager = mock(SessionManager.class);
        UserModel mockUserModel = mock(UserModel.class);
        when(mockSessionManager.getLoggedUser()).thenReturn(mockUserModel);
        when(mockUserModel.getJwt()).thenReturn("mockJwtToken");

        Field instanceField = SessionManager.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, mockSessionManager);
    }


    @Test
    public void run() {
        Feed spyFeed = Mockito.spy(feed);
        spyFeed.run();
        verify(spyFeed, times(1)).startListeningToSSE();
    }
}