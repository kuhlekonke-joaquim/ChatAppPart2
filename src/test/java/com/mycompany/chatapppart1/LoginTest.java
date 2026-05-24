package com.mycompany.chatapppart1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LoginTest {

    @Test
    public void testLoginUserSuccess() {
        Login login = new Login();
        login.registerUser("ab_1", "Pass123!", "+27831234567");
        boolean result = login.loginUser("ab_1", "Pass123!");
        assertTrue(result);
    }

    @Test
    public void testLoginUserFail() {
        Login login = new Login();
        login.registerUser("ab_1", "Pass123!", "+27831234567");
        boolean result = login.loginUser("wrong", "wrong");
        assertFalse(result);
    }

    @Test
    public void testReturnLoginStatusSuccess() {
        Login login = new Login();
        login.registerUser("ab_1", "Pass123!", "+2783212345");
        boolean status = login.loginUser("ab_1", "Pass123!");
        String message = login.returnLoginStatus(status);
        assertEquals("Welcome ab_1, it is great to see you again.", message);
    }

    @Test
    public void testReturnLoginStatusFail() {
        Login login = new Login();
        login.registerUser("ab_1", "Pass123!", "+2783212345");
        boolean status = login.loginUser("wrong", "wrong");
        String message = login.returnLoginStatus(status);
        assertEquals("Username or password incorrect, please try again.", message);
    }
}
