package bcitdaltond.application.myDatabase;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by runej on 2017-10-03.
 */

public class Image {
    private int id;
    private String uri;
    private String caption;
    private String description;
    private String date;
    //private String category;

    /**
     * The current user of the app. A driver.
     * @param uri Directory for the image on the phone
     * @param caption Title for the image
     * @param description About the camera
     * @param date When the photo was taken
     */
    public Image(int id,
                String uri,
                String caption,
                String description,
                String date
    ) {
        this.id = id;
        this.uri = uri;
        this.caption = caption;
        this.description = description;
        this.date = date;
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
        this.description = c.getString(3);
        this.date = c.getString(4);
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public ContentValues getContent() {
        ContentValues values = new ContentValues();
        values.put("uri", this.getUri());
        values.put("caption", this.getCaption());
        values.put("description", this.getDescription());
        values.put("date", this.getDate());
        return values;
    }

}
