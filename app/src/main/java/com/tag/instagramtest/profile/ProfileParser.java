package com.tag.instagramtest.profile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 09/02/2017.
 */

public class ProfileParser {

    public List<ProfileValue> parse(JSONObject jsonObject) {

        List<ProfileValue> profileList = new ArrayList<>();

        ProfileValue profileValue;


        try {

            JSONArray postsArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < postsArray.length(); i++) {

                JSONObject posts = postsArray.getJSONObject(i);

                profileValue = new ProfileValue();


                JSONObject userObj = posts.getJSONObject("user");
                String full_name = userObj.getString("full_name");
                profileValue.setFull_name(full_name);
                Log.d("full_name", full_name);


                String profilePicture = userObj.getString("profile_picture");
                profileValue.setProfile_picture(profilePicture);
                Log.d("profilePicture", profilePicture);


                // get a thumbnail image for two column gallery view
                JSONObject thumbImageObject = posts.getJSONObject("images");
                JSONObject thumbImageURL = thumbImageObject.getJSONObject("low_resolution");
                String thumbImage = thumbImageURL.getString("url");
                profileValue.setThumbImage(thumbImage);
                Log.d("thumbImage", thumbImage);


                // get a thumbnail image for two column gallery view
                JSONObject fullImageObject = posts.getJSONObject("images");
                JSONObject fullImageURL = fullImageObject.getJSONObject("standard_resolution");
                String fullImage = fullImageURL.getString("url");
                profileValue.setFullSizeImage(fullImage);
                Log.d("fullImage", fullImage);


                //  Get Like Count
                JSONObject likeObject = posts.getJSONObject("likes");
                int likeCount = likeObject.getInt("count");
                profileValue.setLikes(likeCount);
                Log.d("likes", ((String.valueOf(likeCount))));

                //  Get Like Count
                JSONObject CaptionObject = posts.getJSONObject("caption");
                String captionText = CaptionObject.getString("text");
                profileValue.setCaptionText(captionText);
                Log.d("captionText", captionText);

                //Created Time
                String createdTime = CaptionObject.getString("created_time");
                long foo = Long.parseLong(createdTime)*1000;
                Date date = new Date(foo);
                DateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy");
                profileValue.setCreated_time(formatter.format(date));




                Log.d("createdTime", createdTime);


                profileList.add(profileValue);

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return profileList;

    }



    }

