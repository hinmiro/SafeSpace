package services;

import com.google.gson.Gson;
import model.*;
import java.net.*;
import java.net.http.*;
import java.util.logging.Logger;

public class Feed implements Runnable {

    private static String url = "http://10.120.32.76:8080/api/v1/events";
    private Gson gson;
    private static final Logger logger = Logger.getLogger(Feed.class.getName());


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
                        logger.info("Received line: " + line);
                        eventBuilder.append(line).append("\n");
                        if (line.isEmpty()) {
                            processEvent(eventBuilder.toString());
                            eventBuilder.setLength(0);
                        }
                    });
                })
                .exceptionally(e -> {
                    logger.info("Error in SSE connection: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                })
                .join();
    }

    private void processEvent(String event) {
        logger.info(event);
        String[] lines = event.split("\n");
        if (lines.length > 1) {
            String eventType = lines[0].substring(6).trim();
            String eventDataLine = lines[1].substring(5).trim();

            if (eventType.equals("new_post")) {
                Post post = gson.fromJson(eventDataLine, Post.class);
                SharedData.getInstance().addEvent(post);
            }

            if (eventType.equals("like_added")) {
                Like like = gson.fromJson(eventDataLine, Like.class);
                SharedData.getInstance().addLike(like);
            }

            if (eventType.equals("like_removed")) {
                Like like = gson.fromJson(eventDataLine, Like.class);
                SharedData.getInstance().addRemoveLike(like);
            }
        }
    }

    @Override
    public void run() {
        startListeningToSSE();
    }
}
