package services;

import model.SharedData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Feed implements Runnable {

    private static String url = "http://localhost:8081/posts/events";
    private BlockingQueue<String> queue;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_SECONDS = 5;


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
                .thenAccept(lines -> lines.forEach(line -> {
                    if (!line.isEmpty() && !line.startsWith(":")) {
                        System.out.println("recived line: " + line);
                        processEvent(line);
                    }
                }))
                .exceptionally(e -> {
                    e.printStackTrace();
                    retryConnection();
                    return null;
                })
                .join();
    }

    private void processEvent(String event) {
        SharedData.getInstance().addEvent(event);
    }

    private void retryConnection() {
        for (int i = 1; i <= MAX_RETRIES; i++) {
            try {
                System.out.println("Retrying connection... Attempt " + i);
                TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
                startListeningToSSE();
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Retry interrupted");
                break;
            }
        }
    }

    @Override
    public void run() {
        startListeningToSSE();
    }
}
