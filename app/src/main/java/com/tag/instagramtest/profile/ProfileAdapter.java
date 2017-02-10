package com.tag.instagramtest.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tag.instagramtest.R;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context mContext;
    private List<ProfileValue> profileList;
    private LayoutInflater inflater;
    public View profileView;

    public ProfileAdapter(Context context, List<ProfileValue> profileList) {
        this.profileList = profileList;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPostImage;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);

        }
    }



    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        profileView = inflater.inflate(R.layout.profile_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(profileView);
        return new ViewHolder(profileView);
    }



    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProfileValue profilePost = profileList.get(position);

        String imageURL = profilePost.getThumbImage();
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Picasso.with(mContext).load(imageURL).resize(displayMetrics.widthPixels/2, displayMetrics.widthPixels/2).centerCrop().into(holder.ivPostImage);

    }



    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return profileList == null ? 0 : profileList.size();
    }


}
