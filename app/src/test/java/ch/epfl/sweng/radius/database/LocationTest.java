package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class LocationTest {

    private MLocation testLocation;
    private MLocation testTopic;

    @Before
    public void setUp() {
        Database.activateDebugMode();
        testLocation = new MLocation("testLoc0");
        testTopic = new MLocation("testTopic");
        testTopic.setOwnerId(Database.getInstance().getCurrent_user_id());
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

    @Test
    public void testIsRemovableTopic() {
        assertFalse(testTopic.isRemovableTopic());
        testTopic.setLocationType(2);
        assertTrue(testTopic.isRemovableTopic());
    }

}
