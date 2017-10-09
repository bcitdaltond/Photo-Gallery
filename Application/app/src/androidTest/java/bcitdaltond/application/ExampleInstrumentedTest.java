package bcitdaltond.application;

import android.content.Context;
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

    @Before
    public void setUp(){
        dbHelper = DBHelper.getInstance(InstrumentationRegistry.getTargetContext());
        for (int i = 10; i < 20; i++) {
            Image img = new Image(i, "uri" + i, "caption", "description", "9-" + i + "-2017");
            dbHelper.addImage(img);
        }

    }

    @After
    public void finish() {
        ArrayList<Image> images = dbHelper.getAllImages();
        for (int i = 0; i < images.size(); i++) {
            dbHelper.removeImage("" + images.get(i).getId());
        }
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
}
