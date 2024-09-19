package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedData {
    private static SharedData instance;
    private BlockingQueue<Post> postQueue;
    private ArrayList<Post> posts;


    private SharedData() {
        postQueue = new LinkedBlockingQueue<>();
        posts = new ArrayList<>();
    }

    public static synchronized SharedData getInstance() {
        instance = instance == null ? new SharedData() : instance;
        return instance;
    }

    public void addEvent(Post event) {
        try {
            postQueue.put(event);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public Post takeEvent() throws InterruptedException {

        Post post = postQueue.take();
        posts.add(post);
        return post;
    }

    public BlockingQueue<Post> getEventQueue() {
        return postQueue;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

}
