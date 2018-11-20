package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
        testLocation.setisGroupLocation(true);
        assertTrue(testLocation.isGroupLocation());
        testLocation.setisGroupLocation(false);
        assertFalse(testLocation.isGroupLocation());
    }

}
