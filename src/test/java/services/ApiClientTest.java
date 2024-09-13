package services;

import org.junit.AfterClass;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiClientTest {
    public static HttpClient client;
    public static HttpResponse<String> res;
    private ApiClient apiClient;


    @BeforeAll
    public static void init() {
        client = mock(HttpClient.class);
        res = mock(HttpResponse.class);
    }

    @BeforeEach
    void setUp() {
        apiClient = new ApiClient(client);
    }

    @AfterClass
    public static void tearDown() {
        client = null;
        res = null;
    }

    @Test
    void postLogin() throws IOException, InterruptedException {
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
    void postRegister() throws IOException, InterruptedException {
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