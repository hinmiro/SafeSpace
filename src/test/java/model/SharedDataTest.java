package model;

import org.junit.*;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import static org.junit.Assert.*;

public class SharedDataTest {
    private Locale locale;

    @Before
    public void setUp() {
        if (SessionManager.getInstance().getSelectedLanguage() == null) {
            SessionManager.getInstance().setLanguage(Language.EN);
        }
        locale = SessionManager.getInstance().getSelectedLanguage().getLocale();
    }

    @After
    public void tearDown() {
        locale = null;
    }

    @Test
    public void getInstance() {
        SharedData instance1 = SharedData.getInstance();
        SharedData instance2 = SharedData.getInstance();
        assertSame("Instances should be the same", instance1, instance2);
    }

    @Test
    public void addEvent() {
        SharedData.getInstance().addEvent(new Post(0, 0, "test", "test", null, "testingDay", 5, 5));
        BlockingQueue<Post> updatedEvents = SharedData.getInstance().getEventQueue();
        assertEquals("Event queue should contain 1 event", 1, updatedEvents.size());
    }

    @Test
    public void addLike() {
        SharedData.getInstance().addLike(new Like(0, 0));
        BlockingQueue<Like> updatedLikes = SharedData.getInstance().getLikeQueue();
        assertEquals("Like queue should contain 1 like", 1, updatedLikes.size());
    }

    @Test
    public void getRemovedLikeQueue() {
        BlockingQueue<Like> removedLikes = SharedData.getInstance().getRemovedLikeQueue();
        assertNotNull("Removed like queue should not be null", removedLikes);
    }

    @Test
    public void addRemoveLike() {
        BlockingQueue<Like> removedLikes = SharedData.getInstance().getRemovedLikeQueue();
        SharedData.getInstance().addRemoveLike(new Like(0, 0));
        assertEquals("Removed like queue should contain 1 like", 1, removedLikes.size());
    }

    @Test
    public void getEventQueue() {
        BlockingQueue<Post> events = SharedData.getInstance().getEventQueue();
        assertNotNull("Event queue should not be null", events);
    }

    @Test
    public void getLikeQueue() {
        BlockingQueue<Like> likes = SharedData.getInstance().getLikeQueue();
        assertNotNull("Like queue should not be null", likes);
    }

    @Test
    public void getPosts() {
        assertNotNull("Posts list should not be null", SharedData.getInstance().getPosts());
    }
}