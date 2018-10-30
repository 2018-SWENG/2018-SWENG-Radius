package ch.epfl.sweng.radius.utilsTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;

import static org.junit.Assert.assertTrue;

public class BrowseProfilesUtilityTests {

    private BrowseProfilesUtility profileListener;
    private String profileOwner;

    @Before
    public void setUp() {
        profileOwner = "John Doe";
        profileListener = new BrowseProfilesUtility(profileOwner);
    }

    @Test
    public void testReportUser() {
        profileListener.reportUser("Language");
        profileListener.reportUser("Spam");
    }

    @Test
    public void testGetProfileOwner() {
        String profileOwnerName;
        profileOwnerName = profileListener.getProfileOwner();
        assertTrue(profileOwnerName.equals(profileOwner));
    }

    @After
    public void tearDown() {
        profileListener = null;
        profileOwner = null;
    }

}
