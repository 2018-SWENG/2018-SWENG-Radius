package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class GroupLocationTest {

    private MLocation testLocation;

    @Before
    public void setUp() {
        testLocation = new MLocation();
    }

    @Test
    public void testInitialState() {
        assertFalse(testLocation.isGroupLocation());
    }

    @Test
    public void testSetIsGroupLocation() {
        testLocation.setIsGroupLocation(true);
        assertTrue(testLocation.isGroupLocation());
        testLocation.setIsGroupLocation(false);
        assertFalse(testLocation.isGroupLocation());
    }


}
