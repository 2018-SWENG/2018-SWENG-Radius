package ch.epfl.sweng.radius.utilsTests;

import org.junit.After;
import org.junit.Test;

import ch.epfl.sweng.radius.utils.AppData;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppDataTest {

    @Test
    public void testDefaultAppData() {
        assertFalse(AppData.INSTANCE.isTestModeOn());
    }

    @Test
    public void testSetTestMode() {
        assertFalse(AppData.INSTANCE.isTestModeOn());
        AppData.INSTANCE.setTestMode(true);
        assertTrue(AppData.INSTANCE.isTestModeOn());
    }

    @After
    public void tearDown() {
        AppData.INSTANCE.setTestMode(false);
    }
}
