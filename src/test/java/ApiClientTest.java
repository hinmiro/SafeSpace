import org.junit.jupiter.api.Test;
import org.junit.Before;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import services.ApiClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ApiClientTest {


    @Test
    public void postLogin() throws IOException, InterruptedException {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockResponse.body()).thenReturn("{\"token\": \"QWEASD123\"}");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        String response = ApiClient.postLogin("https://0e3c0758-5535-4c7c-a3b6-b958095f551f.mock.pstmn.io/login", "{\"username\": \"user\", \"password\": \"pass\"}");

        assertEquals("{\"username\": \"käyttäjä\", \"contactinfo\": \"email here\"}", response);


    }
}
