package teetech.com.animdemo;

/**
 * Created by aKI on 25/02/2016.
 */
import android.graphics.Bitmap;

class PictureData {
    final int resourceId;
    final String description;
    final Bitmap thumbnail;

    public PictureData(int resourceId, String description, Bitmap thumbnail) {
        this.resourceId = resourceId;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
