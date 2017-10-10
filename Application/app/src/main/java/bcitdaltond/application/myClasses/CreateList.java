package bcitdaltond.application.myClasses;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by runej on 2017-10-02.
 */

public class CreateList {
    private int image_id;
    private String image_title;
    private Bitmap image_bitmap;

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_name) {
        this.image_title = image_name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public void setImage_bitmap(Bitmap image_bitmap) {
        this.image_bitmap = image_bitmap;
    }
}