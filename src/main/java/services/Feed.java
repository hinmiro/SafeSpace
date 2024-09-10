package services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;

public class Feed implements Runnable {

    private static String url = "http://localhost:8081/posts/";
    private BlockingQueue<String> queue;

    public Feed(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void startListeningToSSE() {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofLines())
                .thenApply(HttpResponse::body)
                .thenAccept(lines -> lines.forEach(this::processEvent))
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void processEvent(String event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startListeningToSSE();
    }
}
