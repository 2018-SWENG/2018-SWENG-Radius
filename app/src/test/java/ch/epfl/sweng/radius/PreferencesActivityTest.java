package ch.epfl.sweng.radius;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;

public class PreferencesActivityTest {

    private PreferencesActivity test;

    @Before
    public void setUp() throws Exception {
        Database.activateDebugMode();
        test = new PreferencesActivity();
        }

    @Test
    public void deleteUser() {
        PreferencesActivity.deleteUser();
        restoreCurrentUser();
    }

    private void restoreCurrentUser() {

        User currentUSer = new User("testUser1");
        currentUSer.addChat("testUser2", "10");
        currentUSer.addChat("testUser3", "11");
        currentUSer.addChat("testUser5", "12");
        currentUSer.addFriendRequest(new User("testUser5"));
        ArrayList<String> blockedUser = new ArrayList<>();
        blockedUser.add("testUser3");currentUSer.setBlockedUsers(blockedUser);

        currentUSer.addFriendRequest(new User("testUser3"));

        Database.getInstance().writeInstanceObj(currentUSer, Database.Tables.USERS);

        MLocation currentLoc = new MLocation("testUser1");
        currentLoc.setUrlProfilePhoto("./app/src/androidTest/java/ch/epfl/sweng/radius/utils/default.png");
        currentLoc.setTitle("testUser1");
        currentLoc.setRadius(30000); currentLoc.setMessage("Being tested on");
        currentLoc.setInterests("Tests, mostly");

        Database.getInstance().writeInstanceObj(currentLoc, Database.Tables.LOCATIONS);

    }

}