package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class UserVisibilityTest {

    private User testUser;

    @Before
    public void setUp() {
        testUser = new User();
    }

    @Test
    public void testInitialVisibility() {
        assertTrue(testUser.isVisible());
    }

    @Test
    public void testSetVisibility() {
        testUser.setVisibility(false);
        assertFalse(testUser.isVisible());
        testUser.setVisibility(true);
        assertTrue(testUser.isVisible());
    }

}