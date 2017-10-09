package bcitdaltond.application;

import org.junit.Test;

import bcitdaltond.application.mydbexample.DataStorageImp;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dataStorage_isWorking() throws Exception {
        DataStorageImp db = new DataStorageImp();
        db.saveState("Testing");

        assertEquals("Testing", db.getState());
    }
}