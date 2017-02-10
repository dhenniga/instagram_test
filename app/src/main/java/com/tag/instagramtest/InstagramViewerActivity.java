package com.tag.instagramtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramViewerActivity extends AppCompatActivity {

    String EXTRA_FULL_IMAGE = "EXTRA_FULL_IMAGE";
    String EXTRA_CAPTION_TEXT = "EXTRA_CAPTION_TEXT";
    String EXTRA_CREATED_TIME = "EXTRA_CREATED_TIME";

    ImageView ivFullImage;
    TextView tvCaptionText, tvCreatedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_viewer);

        ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
        tvCaptionText = (TextView) findViewById(R.id.tvCaptionText);
        tvCreatedTime = (TextView) findViewById(R.id.tvCreatedTime);

        final Bundle extras = getIntent().getExtras();
        String fullImageURL = extras.getString(EXTRA_FULL_IMAGE);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        if (extras != null) {

            Picasso.with(this).load(fullImageURL).resize(displayMetrics.widthPixels, displayMetrics.widthPixels).centerCrop().into(ivFullImage);
            tvCaptionText.setText(extras.getString(EXTRA_CAPTION_TEXT));
            tvCreatedTime.setText(extras.getString(EXTRA_CREATED_TIME));

        }
    }
}
