package controller;

import javafx.scene.image.Image;
import model.*;
import org.junit.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ControllerForViewTest {
    ControllerForView controller;
    SoftwareModel mockApp;

    @Before
    public void setUp() throws Exception {
        controller = ControllerForView.getInstance();
        mockApp = mock(SoftwareModel.class);

        Field appField = Controller.class.getDeclaredField("app");
        appField.setAccessible(true);
        appField.set(controller, mockApp);
    }

    @After
    public void tearDown() {
        controller = null;
        mockApp = null;
    }

    @Test
    public void testGetInstanceSingleton() {
        ControllerForView instance1 = ControllerForView.getInstance();
        ControllerForView instance2 = ControllerForView.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        UserModel mockUser = new UserModel("testUser", 1, "testDate");
        when(mockApp.login("username", "password")).thenReturn(mockUser);

        UserModel result = controller.login("username", "password");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(mockApp).login("username", "password");
    }

    @Test
    public void testRegisterSuccessful() throws Exception {
        UserModel mockUser = new UserModel("newUser", 1, "testDate");
        when(mockApp.postRegister("newUser", "password")).thenReturn(mockUser);

        UserModel result = controller.register("newUser", "password");

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        verify(mockApp).postRegister("newUser", "password");
    }

    @Test
    public void testUpdateProfile() throws Exception {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockApp.postPicture(mockFile, "/profile")).thenReturn("profilePictureId");
        when(mockApp.updateUser("username", "password", "bio", "profilePictureId")).thenReturn(true);

        boolean result = controller.updateProfile("username", "password", "bio", mockFile);

        assertTrue(result);
        verify(mockApp).postPicture(mockFile, "/profile");
        verify(mockApp).updateUser("username", "password", "bio", "profilePictureId");
    }

    @Test
    public void testSendPost() throws Exception {
        when(mockApp.createNewPost("testPost")).thenReturn(true);

        boolean result = controller.sendPost("testPost");

        assertTrue(result);
        verify(mockApp).createNewPost("testPost");
    }

    @Test
    public void testSendPostWithImage() throws Exception {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockApp.postPicture(mockFile, "/post")).thenReturn("imageId");
        when(mockApp.createNewPostWithImage("testPost", "imageId")).thenReturn(true);

        boolean result = controller.sendPostWithImage("testPost", mockFile);

        assertTrue(result);
        verify(mockApp).postPicture(mockFile, "/post");
        verify(mockApp).createNewPostWithImage("testPost", "imageId");
    }

    @Test
    public void testGetProfilePicture() throws Exception {
        Image mockImage = mock(Image.class);
        when(mockApp.getProfileImage(1)).thenReturn(mockImage);

        Image result = controller.getProfilePicture(1);

        assertEquals(mockImage, result);
        verify(mockApp).getProfileImage(1);
    }

    @Test
    public void testGetPostPicture() throws Exception {
        Image mockImage = mock(Image.class);
        when(mockApp.getPostImage("1")).thenReturn(mockImage);

        Image result = controller.getPostPicture("1");

        assertEquals(mockImage, result);
        verify(mockApp).getPostImage("1");
    }

    @Test
    public void testGetUserById() throws Exception {
        UserModel mockUser = new UserModel("sara", 1, "2024-11-15");
        when(mockApp.getUserById(1)).thenReturn(mockUser);

        UserModel result = controller.getUserById(1);

        assertEquals(mockUser, result);
        verify(mockApp).getUserById(1);
    }

    @Test
    public void testGetUserByName() throws Exception {
        UserModel mockUser = new UserModel("sara", 1, "2024-11-15");
        when(mockApp.getUserByName("sara")).thenReturn(mockUser);

        UserModel result = controller.getUserByName("sara");

        assertEquals(mockUser, result);
        verify(mockApp).getUserByName("sara");
    }

    @Test
    public void testGetPostCommentsById() throws Exception {
        ArrayList<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment(1, 1, "sara", "Nice post!"));
        mockComments.add(new Comment(2, 2, "sara2", "Great content!"));

        when(mockApp.getCommentsByPostId("1")).thenReturn(mockComments);

        ArrayList<Comment> result = controller.getPostCommentsById("1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Nice post!", result.get(0).getCommentContent());
        assertEquals("Great content!", result.get(1).getCommentContent());

        verify(mockApp).getCommentsByPostId("1");
    }

    @Test
    public void testAddComment() {
        doNothing().when(mockApp).postComment("saran kommentti", "1");

        controller.addComment("saran kommentti", 1);
        verify(mockApp).postComment("saran kommentti", "1");
    }

    @Test
    public void testSendMessage() throws IOException, InterruptedException {
        Messages mockMessage = new Messages("type", "saran viesti", "2024-11-15");
        when(mockApp.sendMessage(mockMessage)).thenReturn(true);

        boolean result = controller.sendMessage(mockMessage);

        assertTrue(result);
        verify(mockApp).sendMessage(mockMessage);
    }

    @Test
    public void testAddFriend() {
        when(mockApp.addFriend(1, 2)).thenReturn(true);

        boolean result = controller.addFriend(1, 2);

        assertTrue(result);
        verify(mockApp).addFriend(1, 2);
    }

    @Test
    public void testRemoveFriend() throws IOException, InterruptedException {
        when(mockApp.removeFriend(1)).thenReturn(true);

        boolean result = controller.removeFriend(1);

        assertTrue(result);
        verify(mockApp).removeFriend(1);
    }

    @Test
    public void testIsFriend() throws IOException, InterruptedException {
        when(mockApp.isFriend(1, 2)).thenReturn(true);

        boolean result = controller.isFriend(1, 2);

        assertTrue(result);
        verify(mockApp).isFriend(1, 2);
    }

    @Test
    public void testGetUserPostsOwnProfile() throws Exception {
        UserModel mockUser = new UserModel("testUser", 1, "testDate");
        mockUser.setPosts(new ArrayList<>(List.of(1, 2)));
        SessionManager.getInstance().setLoggedUser(mockUser);

        UserModel updatedUser = new UserModel("testUser", 1, "testDate");
        updatedUser.setPosts(new ArrayList<>(List.of(1, 2)));
        when(mockApp.getUserById(1)).thenReturn(updatedUser);

        Post post1 = new Post(1, 1, "testUser", "Post 1 content", "imageId", "2024-11-17", 0, 0);
        Post post2 = new Post(2, 1, "testUser", "Post 2 content", "imageId", "2024-11-17", 0, 0);
        when(mockApp.getPostById("1")).thenReturn(post1);
        when(mockApp.getPostById("2")).thenReturn(post2);

        List<Post> result = controller.getUserPostsOwnProfile();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Post 1 content", result.get(0).getPostContent());
        assertEquals("Post 2 content", result.get(1).getPostContent());

        verify(mockApp).getUserById(1);
        verify(mockApp).getPostById("1");
        verify(mockApp).getPostById("2");
    }

    @Test
    public void testGetMessages() throws Exception {
        List<Conversation> mockConversations = List.of(
                new Conversation(new UserModel("user1", 1, "2024-11-17")),
                new Conversation(new UserModel("user2", 2, "2024-11-17"))
        );

        when(mockApp.getMessages()).thenReturn(mockConversations);

        List<Conversation> result = controller.getMessages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getWithUser().getUsername());
        assertEquals("user2", result.get(1).getWithUser().getUsername());
        verify(mockApp).getMessages();
    }

    @Test
    public void testGetCurrentConversation() throws Exception {
        Conversation mockConversation = new Conversation(new UserModel("sara", 1, "2024-11-15"));
        List<Conversation> mockConversations = List.of(
                new Conversation(new UserModel("user1", 2, "2024-11-17")),
                new Conversation(new UserModel("user2", 3, "2024-11-17")),
                mockConversation
        );

        when(mockApp.getAllConversations()).thenReturn(mockConversations);

        Conversation result = controller.getCurrentConversation(1);

        assertNotNull(result);
        assertEquals(mockConversation, result);
        verify(mockApp).getAllConversations();
    }
}
