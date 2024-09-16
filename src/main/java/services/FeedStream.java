package services;

import model.SharedData;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FeedStream implements Runnable {

    private static String url = "http://localhost:8081/posts/events";
    private BlockingQueue<String> queue;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_SECONDS = 5;
    private WebClient webClient;

    public FeedStream(BlockingQueue<String> queue) {
        this.queue = queue;
        this.webClient = WebClient.create();
    }

    public void startListeningToSSE() {
        Flux<String> eventStream = webClient.get()
                .uri(url)
                .header("Accept", "text/event-stream")
                .retrieve()
                .bodyToFlux(String.class);

        eventStream.subscribe(
                line -> {
                    System.out.println("Read line: " + line); // Log each line read
                    if (!line.isEmpty() && !line.startsWith(":")) {
                        System.out.println("received line: " + line);
                        processEvent(line);
                    }
                },
                error -> {
                    error.printStackTrace();
                    retryConnection();
                }
        );
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