package services;

import com.google.gson.Gson;
import model.Post;
import model.SharedData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Feed implements Runnable {

    private static String url = "http://localhost:8081/api/v1/events";
    private BlockingQueue<Post> queue;
    private Gson gson;


    public Feed() {
        gson = new Gson();
    }

    public void startListeningToSSE() {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofLines())
                .thenApply(HttpResponse::body)
                .thenAccept(lines -> {
                    lines.forEach(line -> {
                        if (line.startsWith("data:")) {
                            String event = line.substring(5).trim();
                            processEvent(event);
                        }
                    });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                })
                .join();
    }

    private void processEvent(String event) {
        Pattern pattern = Pattern.compile("data:(\\{.*})");
        Post post = gson.fromJson(event, Post.class);
        SharedData.getInstance().addEvent(post);
    }


    @Override
    public void run() {
        startListeningToSSE();
    }
}
