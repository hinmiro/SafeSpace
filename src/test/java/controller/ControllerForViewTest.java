package controller;

import model.SoftwareModel;
import model.UserModel;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
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

}
