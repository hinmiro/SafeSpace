package services;


import model.SessionManager;
import model.UserModel;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static HttpClient client = HttpClient.newHttpClient();
    private static HttpResponse<String> res;
    private static final String url = "http://10.120.32.76:8080/api/v1";
    private static final String authUrl = "http://10.120.32.76:8080/auth";
    private static final String pictureUrl = "http://10.120.32.76:8080/";

    public ApiClient(HttpClient client) {
        ApiClient.client = client;
    }


    public static HttpResponse<String> postLogin(String json) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(authUrl + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> postRegister(String data) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(authUrl + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("code: " + res.statusCode());
        System.out.println("body: " + res.body());
        return res;
    }

    public static HttpResponse<String> postPicture(File file) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(pictureUrl))
                .header("Content-Type", "multipart/form-data")
                .header("Authorization", "Bearer " + user.getJwt())
                .POST(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("body: " + res.body());
        System.out.println("code: " + res.statusCode());
        return res;
    }

}
