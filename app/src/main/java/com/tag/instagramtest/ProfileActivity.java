package com.tag.instagramtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tag.instagramtest.listener.RecyclerClickListener;
import com.tag.instagramtest.listener.RecyclerTouchListener;
import com.tag.instagramtest.profile.ProfileAdapter;
import com.tag.instagramtest.profile.ProfileParser;
import com.tag.instagramtest.profile.ProfileValue;

import org.json.JSONObject;

import java.util.List;

public class ProfileActivity extends Activity {

    private static final String EXTRA_FULL_IMAGE = "EXTRA_FULL_IMAGE";
    private static final String EXTRA_CAPTION_TEXT = "EXTRA_CAPTION_TEXT";
    private static final String EXTRA_CREATED_TIME = "EXTRA_CREATED_TIME";
    private static final String EXTRA_PROFILE_PICTURE = "EXTRA_PROFILE_PICTURE";

    Button btnLogout;
    ImageView ivProfilePicture;
    TextView tvProfileFullName;
    InstagramApp mApp;

    private List<ProfileValue> profileList;
    private RecyclerView rvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        Picasso.with(this).load(mApp.getProfilePicture()).into(ivProfilePicture);
//        Log.d("profilePicture", mApp.getProfilePicture().toString());


        tvProfileFullName = (TextView) findViewById(R.id.tvProfileFullName);
        tvProfileFullName.setText(mApp.getName());

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        initViews();

        new JSONAsync().execute();

    }



    private void initViews() {


        rvProfile = (RecyclerView) findViewById(R.id.rvProfile);
        rvProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false));

        rvProfile.addOnItemTouchListener(new RecyclerTouchListener(ProfileActivity.this, rvProfile, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {

                instagramViewer(position);

            }
        }));

    }


    /**
     *   Logout dialog
     */
    private void logoutUser() {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setMessage("Logout from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    mApp.resetAccessToken();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            final AlertDialog alert = builder.create();
            alert.show();
    }




    /**
     *
     * @param position
     */
    private void instagramViewer(int position) {

        if (profileList != null) {

            Intent intent = new Intent(getApplicationContext(), InstagramViewerActivity.class);
            intent.putExtra(EXTRA_FULL_IMAGE, profileList.get(position).getFullSizeImage());
            intent.putExtra(EXTRA_CAPTION_TEXT, profileList.get(position).getCaptionText());
            intent.putExtra(EXTRA_CREATED_TIME, profileList.get(position).getCreated_time());
            Log.d(EXTRA_FULL_IMAGE, profileList.get(position).getFullSizeImage());
            Log.d(EXTRA_CAPTION_TEXT, profileList.get(position).getCaptionText());
            Log.d(EXTRA_CREATED_TIME, profileList.get(position).getCreated_time());
            startActivity(intent);

        }
    }




    class JSONAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd = ProgressDialog.show(ProfileActivity.this, null, "Loading Instagram Gallery...", true, false);

        }


        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = new JSONParser().getJSONFromUrlByGet(ApplicationData.USER_MEDIA_BASE_URL + mApp.getToken());
            profileList = new ProfileParser().parse(jsonObject);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            ProfileAdapter profileAdapter = new ProfileAdapter(getApplicationContext(), profileList);
            rvProfile.setAdapter(profileAdapter);
            pd.dismiss();
        }
    }


}
