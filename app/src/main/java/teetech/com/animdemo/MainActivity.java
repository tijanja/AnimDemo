package teetech.com.animdemo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final HashMap<ImageView, PictureData> mPicturesData = new HashMap<>();
    ImageView alvyOne,alvyTwo;
    String PACKAGE__ = "teetech.com.animdemo";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ColorMatrix grayScale = new ColorMatrix();
        grayScale.setSaturation(0);

        ColorMatrixColorFilter grayScaleFilter = new ColorMatrixColorFilter(grayScale);

        GridLayout mGridLayout;
        mGridLayout = (GridLayout) findViewById(R.id.gridlayout);
        mGridLayout.setColumnCount(3);
        mGridLayout.setUseDefaultMargins(true);

        Resources resources = getResources();
        ArrayList<PictureData> pictures = BitmapUtils.loadPhotos(resources);
        for (int i = 0; i < pictures.size(); ++i)
        {
            PictureData pictureData = pictures.get(i);
            BitmapDrawable thumbnailDrawable = new BitmapDrawable(resources, pictureData.thumbnail);
            thumbnailDrawable.setColorFilter(grayScaleFilter);
            ImageView imageView = new ImageView(this);
            imageView.setOnClickListener(this);
            imageView.setImageDrawable(thumbnailDrawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mPicturesData.put(imageView, pictureData);
            mGridLayout.addView(imageView);
        }





    }


    @Override
    public void onClick(View v)
    {

        int[] screenLocation = new int[2];
        v.getLocationOnScreen(screenLocation);
        //ImageView image = (ImageView)v;

        PictureData info = mPicturesData.get(v);
        Intent i = new Intent(this,DisplayActivity.class);
        i.putExtra(PACKAGE__+".locationX",screenLocation[0]);
        i.putExtra(PACKAGE__+".locationY",screenLocation[1]);
        i.putExtra(PACKAGE__+".width",v.getWidth());
        i.putExtra(PACKAGE__ + ".height", v.getHeight());
        i.putExtra(PACKAGE__ + ".resourceId", info.resourceId);

        //Toast.makeText(this,screenLocation[1]+"",Toast.LENGTH_LONG).show();

        startActivity(i);
        overridePendingTransition(0,0);
    }
}
