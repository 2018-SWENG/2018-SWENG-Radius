package ch.epfl.sweng.radius;

import org.junit.Test;

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
        FriendsTab tab = new FriendsTab();
        tab.onDataChange("HELLO");

        RequestsTab tab2 = new RequestsTab();
        tab2.onDataChange("HELLO");

        GroupTab tab3 = new GroupTab();
        tab3.onDataChange("HELO");

        PeopleTab tab4 = new PeopleTab();
        tab4.onDataChange("HELO");

        TopicsTab tab5 = new TopicsTab();
        tab5.onDataChange("HELO");

        MessageListActivity a0 = new MessageListActivity();
        a0.onDataChange("HELO");

        MessagesFragment f0 = new MessagesFragment();
        f0.onDataChange("HELO");

        MapUtility m1 = new MapUtility(50000);
        m1.onDataChange("HELO");

    }
}