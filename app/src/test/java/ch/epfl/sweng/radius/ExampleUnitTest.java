package ch.epfl.sweng.radius;

import android.provider.ContactsContract;

import org.junit.Test;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.friends.FriendsTab;
import ch.epfl.sweng.radius.friends.RequestsTab;
import ch.epfl.sweng.radius.home.GroupTab;
import ch.epfl.sweng.radius.home.PeopleTab;
import ch.epfl.sweng.radius.home.TopicsTab;
import ch.epfl.sweng.radius.messages.MessageListActivity;
import ch.epfl.sweng.radius.messages.MessagesFragment;
import ch.epfl.sweng.radius.utils.MapUtility;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testOnDataChange(){
        Database.activateDebugMode();
        FriendsTab tab = new FriendsTab();
        tab.onLocationChange("HELLO");
        RequestsTab tab2 = new RequestsTab();
        tab2.onLocationChange("HELLO");

        GroupTab tab3 = new GroupTab();
        tab3.onLocationChange("HELO");

        PeopleTab tab4 = new PeopleTab();
        tab4.onLocationChange("HELO");

        TopicsTab tab5 = new TopicsTab();
        tab5.onLocationChange("HELO");
        MessagesFragment f0 = new MessagesFragment();
        f0.onUserChange("HELO");

        MapUtility m1 = MapUtility.getMapInstance();
        m1.onLocationChange("HELO");

    //    ((FakeFirebaseUtility) Database.getInstance()).printDBtoJSON();

    }
}