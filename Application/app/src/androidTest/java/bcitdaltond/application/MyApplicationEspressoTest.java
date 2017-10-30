package bcitdaltond.application;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import bcitdaltond.application.myActivities.GalleryActivity;
import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MyApplicationEspressoTest {

    private DBHelper dbHelper;

    private void removeImages() {
        ArrayList<Image> images = dbHelper.getAllImages();
        for (int i = 0; i < images.size(); i++) {
            dbHelper.removeImage("" + images.get(i).getId());
        }
    }

    private void addImages() {
        for (int i = 0; i < 10; i++) {
            Image img = new Image(i, "uri" + i, "caption", "10-" + i + "-2017", "0.0," + i + ".0");
            dbHelper.addImage(img);
        }
    }

    @Before
    public void before() {
        dbHelper = DBHelper.getInstance(InstrumentationRegistry.getTargetContext());
        removeImages();
        addImages();
    }

    @After
    public void after() {
        removeImages();
        dbHelper.close();
    }

    @Rule
    public ActivityTestRule<GalleryActivity> mActivityRule =
            new ActivityTestRule<>(GalleryActivity.class);
    @Test
    public void ensureTextChangesWork() {
        openDrawer(R.id.drawer_layout);
        onView(withText("Filter")).perform(click());
        onView(withText("Location")).perform(click());
        onView(withText("Apply")).perform(click());
        onView(withId(R.id.imagegallery)).check(new RecyclerViewItemCountAssertion(1));
    }
}

class RecyclerViewItemCountAssertion implements ViewAssertion {

    private final Matcher<Integer> matcher;

    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.matcher = is(expectedCount);
    }

    public RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), matcher);
    }

}