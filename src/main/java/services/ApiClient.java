package services;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static HttpClient client = HttpClient.newHttpClient();
    private static HttpResponse<String> res;
    private static final String url = "https://0e3c0758-5535-4c7c-a3b6-b958095f551f.mock.pstmn.io";

    public ApiClient(HttpClient client) {
        ApiClient.client = client;
    }


    public static String postLogin(String json) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        System.out.println(req);

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(res);
        return res.body();
    }

    public static String postRegister(String data) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(res);
        return res.body();
    }

}
