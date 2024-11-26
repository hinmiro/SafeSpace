package services;

import controller.ControllerForView;
import model.*;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Main connection between SafeSpace interface and server.
 */
public class ApiClient {

    private static HttpClient client = HttpClient.newHttpClient();
    private static HttpResponse<String> res;
    private static final String URL = "http://10.120.32.76:8080/api/v1";
    private static final String AUTHURL = "http://10.120.32.76:8080/auth";
    private static final String PICTUREURL = "http://10.120.32.76:8080/api/v1/storage";
    private static final String PROFILEPICTUREGET = "http://10.120.32.76/pictures/profile/";
    private static final String POSTPICTUREGET = "http://10.120.32.76/pictures/post/";
    private static final Logger logger = Logger.getLogger(ApiClient.class.getName());

    /**
     * Constructor to initialize the ApiClient with a custom HttpClient.
     *
     * @param client the HttpClient to be used for making requests
     */
    public ApiClient(HttpClient client) {
        ApiClient.client = client;
    }

    /**
     * Checks if the server is available.
     *
     * @return true if the server responds with status code 403, false otherwise
     */
    public static boolean isServerAvailable() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://10.120.32.76/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return res.statusCode() == 403;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a login request to the server.
     *
     * @param json the JSON string containing login credentials
     * @return the HttpResponse from the server
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> postLogin(String json) throws InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(AUTHURL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
            logger.info(res.body());
            return res;
        } catch (Exception e) {
            throw new InterruptedException();
        }
    }

    /**
     * Sends a registration request to the server.
     *
     * @param data the JSON string containing registration data
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> postRegister(String data) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(AUTHURL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Uploads a picture to the server.
     *
     * @param file     the file to be uploaded
     * @param endPoint the endpoint to which the file should be uploaded
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> postPicture(File file, String endPoint) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        String boundary = UUID.randomUUID().toString();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(PICTUREURL + endPoint))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Authorization", "Bearer " + user.getJwt())
                .POST(ofMimeMultipartData(file.toPath(), boundary))
                .build();
        logger.info("request " + req.toString());

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Constructs multipart form data for file upload.
     *
     * @param filePath the path to the file to be uploaded
     * @param boundary the boundary string for multipart data
     * @return the BodyPublisher for the multipart data
     * @throws IOException if an I/O error occurs
     */
    private static HttpRequest.BodyPublisher ofMimeMultipartData(Path filePath, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();

        byteArrays.add(("--" + boundary + "\r\n").getBytes());
        byteArrays.add(("Content-Disposition: form-data; name=\"file\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes());
        byteArrays.add(("Content-Type: " + Files.probeContentType(filePath) + "\r\n\r\n").getBytes());
        byteArrays.add(Files.readAllBytes(filePath));
        byteArrays.add(("\r\n--" + boundary + "--\r\n").getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    /**
     * Updates user information on the server.
     *
     * @param data the JSON string containing user data
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> updateUser(String data) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/update"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        logger.info("BODY from updateUser(): " + res.body());

        return res;
    }

    /**
     * Retrieves user information by username.
     *
     * @param name the username to search for
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getUserByName(String name) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/search?name=" + name))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Removes a friend by ID.
     *
     * @param id the ID of the friend to be removed
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> removeFriend(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .DELETE().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Adds a friend by ID.
     *
     * @param id the ID of the friend to be added
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> addFriend(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/friends/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Retrieves all friends of the logged-in user.
     *
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getAllFriends() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/friends"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param id the ID of the user to retrieve
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getUserById(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Retrieves information of the logged-in user.
     *
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getMe() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/users/me"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        logger.info("res body api" + res.body());
        return res;
    }

    // Posts

    /**
     * Creates a new post.
     *
     * @param json the JSON string containing post data
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> createPost(String json) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Retrieves all posts.
     *
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getPosts() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Likes a post by post ID.
     *
     * @param postId the ID of the post to like
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> likePost(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post/" + postId + "/like"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Removes a like from a post by post ID.
     *
     * @param postId the ID of the post to remove the like from
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> removeLike(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post/" + postId + "/remove"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .POST(HttpRequest.BodyPublishers.noBody()).build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Retrieves a post by post ID.
     *
     * @param postId the ID of the post to retrieve
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getPostById(String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post/" + postId))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET().build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());

        return res;
    }

    // Images

    /**
     * Retrieves the profile image of a user by user ID.
     *
     * @param userId the ID of the user whose profile image is to be retrieved
     * @return the HttpResponse containing the profile image as a byte array
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<byte[]> getProfileImg(int userId) throws IOException, InterruptedException {

        UserModel user = ControllerForView.getInstance().getUserById(userId);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(PROFILEPICTUREGET + user.getProfilePictureUrl()))
                .header("Authorization", "Bearer " + user.getJwt())
                .GET()
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofByteArray());
    }

    /**
     * Retrieves a post image by image ID.
     *
     * @param id the ID of the image to retrieve
     * @return the HttpResponse containing the post image as a byte array
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<byte[]> getPostImage(String id) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(POSTPICTUREGET + id))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .GET()
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofByteArray());
    }

    // Comments

    /**
     * Retrieves comments for a post by post ID.
     *
     * @param id the ID of the post to retrieve comments for
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> getCommentsByPostId(String id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post/" + id + "/comment"))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .header("Content-Type", "application/json")
                .GET()
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Posts a comment on a post.
     *
     * @param json   the JSON string containing comment data
     * @param postId the ID of the post to comment on
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    public static HttpResponse<String> postComment(String json, String postId) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/post/" + postId + "/comment"))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Sends a message to a specified user.
     *
     * @param receiverId     the ID of the user to receive the message
     * @param messageContent the content of the message to be sent
     * @return the HTTP response from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static HttpResponse<String> sendMessage(int receiverId, String messageContent) throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        String formData = "toUserId=" + receiverId + "&messageContent=" + URLEncoder.encode(messageContent, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/message/send"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + user.getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Retrieves all messages for the logged-in user.
     *
     * @return the HTTP response containing the messages
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static HttpResponse<String> getMessages() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/message"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .GET()
                .build();

        res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res;
    }

    /**
     * Retrieves all conversations for the logged-in user.
     *
     * @return the HTTP response containing the conversations
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static HttpResponse<String> getConversations() throws IOException, InterruptedException {
        UserModel user = SessionManager.getInstance().getLoggedUser();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/message/conversations"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + user.getJwt())
                .GET()
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}