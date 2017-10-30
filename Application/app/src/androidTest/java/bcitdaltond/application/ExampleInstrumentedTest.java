package bcitdaltond.application;

import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import bcitdaltond.application.myDatabase.DBHelper;
import bcitdaltond.application.myDatabase.Image;
import bcitdaltond.application.myDatabase.User;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("bcitdaltond.application", appContext.getPackageName());
    }

    private DBHelper dbHelper;

    private void removeImages() {
        ArrayList<Image> images = dbHelper.getAllImages();
        for (int i = 0; i < images.size(); i++) {
            dbHelper.removeImage("" + images.get(i).getId());
        }
    }

    private void addImages() {
        for (int i = 10; i < 20; i++) {
            Image img = new Image(i, "uri" + i, "caption", "9-" + i + "-2017", "0.0,0.0");
            dbHelper.addImage(img);
        }
    }

    @Before
    public void setUp(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getInstrumentation().getUiAutomation().executeShellCommand(
//                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
//                            + " android.permission.READ_EXTERNAL_STORAGE");
//        }

        dbHelper = DBHelper.getInstance(InstrumentationRegistry.getTargetContext());
        removeImages();
        addImages();
    }

    @After
    public void finish() {
        removeImages();
        dbHelper.close();
    }


    @Test
    public void DBHelper_isWorking() throws Exception {
        //dbHelper = new DBHelper(context); //main.getApplicationContext()
        User a = new User(0, "bob", "bob123", "bob", "bobby");
        dbHelper.addUser(a);

        User b = dbHelper.getUser("1");
        //System.out.println(b.getFirstname() + "testing..");
        assertEquals("bob", b.getFirstname());
    }

    @Test
    public void DBHelper_filter() throws  Exception {
        ArrayList<Image> images = null;

        images = dbHelper.getAllImages();
        assertEquals(10, images.size());

        images = dbHelper.getDateImages("9-11-2017");
        Log.d("SIZE", "" + images.size());
        assertEquals(1, images.size());

        images = dbHelper.getDateImages("9-9-2017");
        assertEquals(0, images.size());
    }

    @Test
    public void DBHelper_remove() throws Exception {
        ArrayList<Image> images = null;

        images = dbHelper.getAllImages();
        assertEquals(10, images.size());

        for (int i = 0; i < images.size() - 7; i++) {
            Log.d("IMAGE ID: ", "" + images.get(i).getId());
            dbHelper.removeImage("" + images.get(i).getId());
        }

        //10 - 3 = 7
        images = dbHelper.getAllImages();
        assertEquals(7, images.size());
    }
}
