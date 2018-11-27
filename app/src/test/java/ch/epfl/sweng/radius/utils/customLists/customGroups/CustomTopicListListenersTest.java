package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.radius.utils.MapUtilityTest;
import ch.epfl.sweng.radius.utils.customLists.customTopics.CustomTopicListListeners;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

public class CustomTopicListListenersTest {
    private TextView mockedView = Mockito.mock(TextView.class);
    private CustomTopicListListeners test;

    @Before
    public void setUp() throws Exception {

//        Database.activateDebugMode();

        final ArgumentCaptor<View.OnClickListener> argument = ArgumentCaptor.forClass(View.OnClickListener.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(mockedView).setOnClickListener(argument.capture());
                View.OnClickListener answer = argument.getValue();
                answer.onClick(null);
                return null;
            }

        }).when(mockedView).setOnClickListener(argument.capture());

    }
    @After
    public void tearDown() throws Exception {
        test = new CustomTopicListListeners("TestTopic", "TestTopic", "TestTopic");
        test.setCustomOnClick(mockedView, MapUtilityTest.context);
    }

    @Test
    public void onBindViewHolder() {
    }
}
