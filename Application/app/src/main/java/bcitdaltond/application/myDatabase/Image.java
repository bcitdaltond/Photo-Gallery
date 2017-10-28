package bcitdaltond.application.myDatabase;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by runej on 2017-10-03.
 */

public class Image implements IDBClass {
    private int id;
    private String uri;
    private String caption;
    private String date;
    private String location;
    //private String category;

    /**
     * The current user of the app. A driver.
     * @param uri Directory for the image on the phone
     * @param caption Title for the image
     * @param date When the photo was taken
     * @param location Location of where the photo was taken
     */
    public Image(int id,
                String uri,
                String caption,
                String date,
                 String location
    ) {
        this.id = id;
        this.uri = uri;
        this.caption = caption;
        this.date = date;
        this.location = location;
    }

    /**
     * Overloaded
     *
     * @param c
     */
    public Image(Cursor c) {
        this.id = c.getInt(0); //id
        this.uri = c.getString(1);
        this.caption = c.getString(2);
        this.date = c.getString(3);
        this.location = c.getString(4);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public String getUri() {
        return this.uri;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public ContentValues getContent() {
        ContentValues values = new ContentValues();
        values.put("uri", this.getUri());
        values.put("caption", this.getCaption());
        values.put("date", this.getDate());
        values.put("location", this.getLocation());
        return values;
    }

}
