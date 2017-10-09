package bcitdaltond.application;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bcitdaltond.application.testActivities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MyApplicationEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button.
        onView(withId(R.id.edit_message))
                .perform(typeText("HELLO"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.textView)).check(matches(withText("HELLO")));
        //onView(withContentDescription("Navigate up")).perform(click());
    }
}