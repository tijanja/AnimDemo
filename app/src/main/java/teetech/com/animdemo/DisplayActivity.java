package teetech.com.animdemo;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {

    private static final TimeInterpolator decelerator = new DecelerateInterpolator();
    private static final TimeInterpolator accelerator = new DecelerateInterpolator();
    private  static final int duration = 500;
    BitmapDrawable bitmapDrawable;
    ActionBar bar;
    private final ColorMatrix colorizerMatrix = new ColorMatrix();

    int  thumbNailTop, thumbNailLeft,resourceId,mTop,mLeft;
    int thumbNailWidth,thumbNailHeight;
    float widthScale,heightScale;
    ImageView alvy;
    FrameLayout layout;
    ColorDrawable bgColor;
    Toolbar toolbar;

    String PACKAGE__ = "teetech.com.animdemo";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setAlpha(0);
        //toolbar.setTranslationY(-300);


        alvy = (ImageView) findViewById(R.id.alvy);
        layout = (FrameLayout)findViewById(R.id.root_layout);

        /*ColorMatrix grayScale = new ColorMatrix();
        grayScale.setSaturation(0);
        ColorMatrixColorFilter grayScaleFilter = new ColorMatrixColorFilter(grayScale);*/

        Bundle extrals = getIntent().getExtras();
        resourceId = extrals.getInt(PACKAGE__+".resourceId");
        Bitmap bitmap = BitmapUtils.getBitmap(getResources(), resourceId);

        thumbNailWidth = extrals.getInt(PACKAGE__+".width");
        thumbNailHeight =extrals.getInt(PACKAGE__+".height");

        thumbNailTop = extrals.getInt(PACKAGE__+".locationY");
        thumbNailLeft = extrals.getInt(PACKAGE__+".locationX");



        bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
       // bitmapDrawable.setColorFilter(grayScaleFilter);
        alvy.setImageDrawable(bitmapDrawable);





        bgColor = new ColorDrawable(Color.WHITE);
        layout.setBackground(bgColor);
        //Toast.makeText(this,width+"",Toast.LENGTH_LONG).show();
        if(savedInstanceState==null)
        {
            ViewTreeObserver vto = alvy.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw()
                {
                    alvy.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];

                    alvy.getLocationOnScreen(screenLocation);
                    mLeft =  thumbNailLeft  - screenLocation[0];
                    mTop =  thumbNailTop - screenLocation[1];


                    widthScale = (float) thumbNailWidth / alvy.getWidth();
                    heightScale = (float) thumbNailHeight / alvy.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }

    }

    public void runEnterAnimation()
    {
        final long period = (long) (duration * 1);
        alvy.setPivotX(0);
        alvy.setPivotY(0);

        alvy.setScaleX(widthScale);
        alvy.setScaleY(heightScale);

        alvy.setTranslationX(mLeft);
        alvy.setTranslationY(mTop);

        alvy.animate().setDuration(period).scaleX(1).scaleY(1).translationX(0).translationY(0).setInterpolator(decelerator)
                .withEndAction(new Runnable() {

                    @Override
                    public void run()
                    {


                        alvy.setTranslationY(0);
                        alvy.animate().translationY((alvy.getHeight() - layout.getHeight()) / 2).scaleY(0.5f).setInterpolator(accelerator);
                        alvy.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        //toolbar.animate().setDuration(1000).translationY(0).alpha(0.5f);

                        


                    }
                });

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(bgColor,"alpha",0, 255);
        bgAnim.setDuration(period);
        bgAnim.start();


        ObjectAnimator colorizer = ObjectAnimator.ofFloat(DisplayActivity.this, "saturation", 0, 1);
        colorizer.setDuration(period*2);
        colorizer.start();


    }

    public void setSaturation(float value) {
        colorizerMatrix.setSaturation(value);
        ColorMatrixColorFilter colorizerFilter = new ColorMatrixColorFilter(colorizerMatrix);
        bitmapDrawable.setColorFilter(colorizerFilter);
    }

    public void runExitAnimation(final Runnable endAction)
    {
        final long period = (long) (duration * 1);

        // No need to set initial values for the reverse animation; the image is at the
        // starting size/location that we want to start from. Just animate to the
        // thumbnail size/location that we retrieved earlier

        // Caveat: configuration change invalidates thumbnail positions; just animate
        // the scale around the center. Also, fade it out since it won't match up with
        // whatever's actually in the center
        final boolean fadeOut;
        /*if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            mImageView.setPivotX(mImageView.getWidth() / 2);
            mImageView.setPivotY(mImageView.getHeight() / 2);
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }*/

        // First, slide/fade text out of the way
       /* mTextView.animate().translationY(-mTextView.getHeight()).alpha(0).
                setDuration(duration/2).setInterpolator(sAccelerator).
                withEndAction(new Runnable() {
                    public void run() {
                        // Animate image back to thumbnail size/location

                    }
                });*/
        alvy.setScaleType(ImageView.ScaleType.FIT_XY);
        alvy.animate().scaleY(1).translationY(180).translationX(0).setInterpolator(accelerator)
                .withEndAction(endAction);


        /*if (fadeOut) {
            alvy.animate().alpha(0);
        }*/
        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(bgColor, "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate the shadow of the image
       /* ObjectAnimator shadowAnim = ObjectAnimator.ofFloat(mShadowLayout,
                "shadowDepth", 1, 0);
        shadowAnim.setDuration(duration);
        shadowAnim.start();*/

        // Animate a color filter to take the image back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer =
                ObjectAnimator.ofFloat(DisplayActivity.this,
                        "saturation", 1, 0);
        colorizer.setDuration(period);
        colorizer.start();
    }

    @Override
    public void onBackPressed()
    {
        runExitAnimation(new Runnable() {

            @Override
            public void run()
            {
                alvy.animate().setDuration((long)duration*1).
                        scaleX(widthScale).scaleY(heightScale).
                        translationX(mLeft).translationY(mTop).setInterpolator(accelerator)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
