package services;

import controller.ControllerForView;
import model.SessionManager;
import model.UserModel;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class ApiClient {

    private static HttpClient client = HttpClient.newHttpClient();
    private static HttpResponse<String> res;
    private static final String url = "http://10.120.32.76:8080/api/v1";
    private static final String authUrl = "http://10.120.32.76:8080/auth";
    private static final String pictureUrl = "http://10.120.32.76:8080/api/v1/storage";
    private static final String profilePictureGet = "http://10.120.32.76/pictures/profile/";
    private static final String postPictureGet = "http://10.120.32.76/pictures/post/";

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
        return res;
    }

    public static HttpResponse<String> postPicture(File file, String endPoint) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        String boundary = UUID.randomUUID().toString();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(pictureUrl + endPoint))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Authorization", "Bearer " + user.getJwt())
                .POST(ofMimeMultipartData(file.toPath(), boundary))
                .build();
        System.out.println("request " + req.toString());

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    // For constructing multipart form data
    private static HttpRequest.BodyPublisher ofMimeMultipartData(Path filePath, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();

        byteArrays.add(("--" + boundary + "\r\n").getBytes());
        byteArrays.add(("Content-Disposition: form-data; name=\"file\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes());
        byteArrays.add(("Content-Type: " + Files.probeContentType(filePath) + "\r\n\r\n").getBytes());
        byteArrays.add(Files.readAllBytes(filePath));
        byteArrays.add(("\r\n--" + boundary + "--\r\n").getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
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
        System.out.println("BODY from updateUser(): " + res.body());

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
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
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

    public static HttpResponse<String>  removeFriend(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .DELETE().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> addFriend(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
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

    public static HttpResponse<String> getUserById(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> getMe() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/users/me"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("res body api" + res.body());
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

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

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

    public static HttpResponse<String> getPostById(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());

        return res;
    }

    // Images

    public static HttpResponse<byte[]> getProfileImg(int userId) throws IOException, InterruptedException {
        HttpResponse<byte[]> response = null;

        UserModel user = ControllerForView.getInstance().getUserById(userId);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(profilePictureGet + user.getProfilePictureUrl()))
                .header("Authorization", "Bearer " + user.getJwt())
                .GET()
                .build();

        response = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
        return response;
    }

    public static HttpResponse<byte[]> getPostImage(String id) throws IOException, InterruptedException {
        HttpResponse<byte[]> res = null;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(postPictureGet + id))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET()
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
        return res;
    }

    // Comments

    public static HttpResponse<String> getCommentsByPostId(String id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + id + "/comment"))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .header("Content-Type", "application/json")
                .GET()
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> postComment(String json, String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/post/" + postId + "/comment"))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    // messages

    public static HttpResponse<String> sendMessage(int toUserId, String messageContent) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        String formData = "toUserId=" + toUserId + "&messageContent=" + URLEncoder.encode(messageContent, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/message/send"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + user.getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    public static HttpResponse<String> getMessages() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/message"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .GET()
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }
}
