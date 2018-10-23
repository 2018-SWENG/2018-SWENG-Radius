package ch.epfl.sweng.radius;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    private LoginActivity loginActivity;

    @Before
    public void setUp() throws Exception {
        loginActivity = loginActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch() {
        assertNotNull(loginActivity);
        getInstrumentation().waitForIdleSync();
        View testView = loginActivity.findViewById(R.id.loginLayout);
        assertNotNull(testView);
        testView = loginActivity.findViewById(R.id.googleButton);
        assertNotNull(testView);
        testView = loginActivity.findViewById(R.id.imageView);
        assertNotNull(testView);
    }

    /** Does not work, GoogleButton cannot be located in the UI hierarchy
    @Test
    public void testLogin() throws InterruptedException {
        //onData(anything()).inAdapterView(withId(R.id.googleButton)).perform(click());
        //onView(withId(R.id.googleButton)).perform(click());
        //Thread.sleep(10000);
    }
     **/

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }

}
