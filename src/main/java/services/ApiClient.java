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

    public static boolean isServerAvailable() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://10.120.32.76/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return res.statusCode() == 403;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static HttpResponse<String> postLogin(String json) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(authUrl + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println(res.body());
            return res;
        } catch (Exception e) {
            throw new InterruptedException();
        }
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

    public HttpResponse<String> newPost(String text, File file) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();
        postPicture(file);
        return null; // UNFINISHED NEED POST PICTURE ENDPOINT
    }

    public static HttpResponse<String> updateUser(String data) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/update"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("BODY: " + res.body());

        return res;
    }


    public static HttpResponse<String> sendLike() {
        return null;
    }


    public static int deleteUser() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/" + user.getUserId()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .DELETE().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.statusCode();
    }

    public static HttpResponse<String> getUserByName(String name) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/search?name=" + name))
                .header("Content-Type", "application/json")
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> getAllUsers() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static int removeFriend(String id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .DELETE().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.statusCode();
    }

    public static int addFriend(String id) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.statusCode();
    }

    public static HttpResponse<String> getAllFriends() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/friends"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> getUsernameByUserID(int userID) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/" + userID))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }


    // Posts

    public static HttpResponse<String> createPost(String json) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static int updatePost(String postId, String json) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.statusCode();
    }

    public static HttpResponse<String> getPosts() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> deletePost(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .DELETE().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> likePost(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId + "/like"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> removeLike(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId + "/remove"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }
}
