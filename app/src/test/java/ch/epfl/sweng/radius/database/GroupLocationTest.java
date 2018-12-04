package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class GroupLocationTest {

    private MLocation testLocation;

    @Before
    public void setUp() {
        testLocation = new MLocation("testLoc0");
    }

    @Test
    public void testInitialState() {
        assertFalse(testLocation.getLocationType() == 1);
    }

    @Test
    public void testSetIsGroupLocation() {
        testLocation.setLocationType(1);
        assertTrue(testLocation.getLocationType() == 1);
        testLocation.setLocationType(0);
        assertFalse(testLocation.getLocationType() == 1);
    }


}
