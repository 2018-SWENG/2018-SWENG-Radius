package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class LocationVisibilityTest {

    private MLocation testUser;

    @Before
    public void setUp() {
        testUser = new MLocation("testLoc0");
    }

    @Test
    public void testInitialVisibility() {
        assertTrue(testUser.getVisible());
    }

    @Test
    public void testSetVisibility() {
        testUser.setVisible(false);
        assertFalse(testUser.getVisible());
        testUser.setVisible(true);
        assertTrue(testUser.getVisible());
    }

}