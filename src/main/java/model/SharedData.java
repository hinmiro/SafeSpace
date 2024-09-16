package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedData {
    private static SharedData instance;
    private List<String> events;
    private BlockingQueue<String> eventQueue;


    private SharedData() {
        events = new ArrayList<>();
        eventQueue = new LinkedBlockingQueue<>();
    }

    public static synchronized SharedData getInstance() {
        instance = instance == null ? new SharedData() : instance;
        return instance;
    }

    public List<String> getEvents() {
        return events;
    }

    public void addEvent(String event) {
        System.out.println(event);
        events.add(event);
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public String takeEvent() throws InterruptedException {
        return eventQueue.take();
    }

    public BlockingQueue<String> getEventQueue() {
        return eventQueue;
    }
}
