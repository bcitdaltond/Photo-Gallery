package bcitdaltond.application.myClasses;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by runej on 2017-10-02.
 */

public class CreateList {

    private String image_title;
    private Bitmap image_id;

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public Bitmap getImage_ID() {
        return image_id;
    }

    public void setImage_ID(Bitmap android_image_url) {
        this.image_id = android_image_url;
    }
}