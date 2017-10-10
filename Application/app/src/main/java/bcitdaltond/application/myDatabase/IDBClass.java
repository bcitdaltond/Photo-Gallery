package bcitdaltond.application.myDatabase;

import android.content.ContentValues;

/**
 * Created by runej on 2017-10-10.
 */

interface IDBClass {
    void setId(int id);
    int getId();
    ContentValues getContent();
}
