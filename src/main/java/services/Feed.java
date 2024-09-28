package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Like;
import model.Post;
import model.SessionManager;
import model.SharedData;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Feed implements Runnable {

    private static String url = "http://10.120.32.76:8080/api/v1/events";
    private BlockingQueue<Post> queue;
    private Gson gson;


    public Feed() {
        gson = new Gson();
    }

    public void startListeningToSSE() {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoggedUser().getJwt())
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofLines())
                .thenApply(HttpResponse::body)
                .thenAccept(lines -> {
                    StringBuilder eventBuilder = new StringBuilder();
                    lines.forEach(line -> {
                        System.out.println("Received line: " + line);
                        eventBuilder.append(line).append("\n");
                        if (line.isEmpty()) {
                            processEvent(eventBuilder.toString());
                            eventBuilder.setLength(0);
                        }
                    });
                })
                .exceptionally(e -> {
                    System.err.println("Error in SSE connection: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                })
                .join();
    }

    private void processEvent(String event) {
        System.out.println(event);
        String[] lines = event.split("\n");
        if (lines.length > 1) {
            String eventType = lines[0].substring(6).trim();
            String eventDataLine = lines[1].substring(5).trim();

            if (eventType.equals("new_post")) {
                Post post = gson.fromJson(eventDataLine, Post.class);
                SharedData.getInstance().addEvent(post);
            }

            if (eventType.equals("like_added")) {
                System.out.println("like in feed");
                Like like = gson.fromJson(eventDataLine, Like.class);
                SharedData.getInstance().addLike(like);
            }

            if (eventType.equals("remove_like")) {
                System.out.println("remove like: " + eventDataLine);
            }
        }
    }

    public void updateLikeCount(Like like) {
        for (Post post : SharedData.getInstance().getPosts()) {
            if (post.getPostID() == like.getPostId()) {
                post.setLikeCount(post.getLikeCount() + 1);
                break;
            }
        }
    }


    @Override
    public void run() {
        startListeningToSSE();
    }
}
