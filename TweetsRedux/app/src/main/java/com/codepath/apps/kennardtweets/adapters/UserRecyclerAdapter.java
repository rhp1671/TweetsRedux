package com.codepath.apps.kennardtweets.adapters;

/**
 * Created by raprasad on 4/2/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.models.User;
import com.codepath.apps.kennardtweets.ui.profile.ProfileActivity;
import com.codepath.apps.kennardtweets.utilities.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by raprasad on 3/25/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.ui.profile.ProfileActivity;
import com.codepath.apps.kennardtweets.utilities.Utils;
import com.codepath.apps.kennardtweets.models.Tweet;

import static com.codepath.apps.kennardtweets.R.string.tweet;
import static com.codepath.apps.kennardtweets.TwitterApp.getUser;
import static com.codepath.apps.kennardtweets.adapters.TimelineRecyclerAdapter.BASIC_TWEET;
import static com.codepath.apps.kennardtweets.adapters.TimelineRecyclerAdapter.PHOTO_TWEET;

/**
 * Created by raprasad on 3/24/17.
 */

public class UserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<User> mUsers;
    public static final String TAG = UserRecyclerAdapter.class.getName();


    public class BasicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivUser)
        ImageView imageView;
        @BindView(R.id.tvTagLine)
        TextView tvTagLine;
        @BindView(R.id.tvScreenUserName)
        TextView tvScreenUserName;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        private int mPosition;

        public BasicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User u = mUsers.get(mPosition);
                    if (u != null) {
                        String screenName = u.getScreenName();
                        if (screenName != null) {
                            Intent intent = new Intent(mContext, ProfileActivity.class);
                            intent.putExtra("screenName", screenName);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }

        public void renderUser(User u) {
            imageView.setImageResource(0);
            if (u != null) {
                tvScreenUserName.setText(u.getScreenName());
                tvUserName.setText(u.getName());
                tvTagLine.setText(u.getTagline());
                Glide.with(imageView.getContext())
                        .load(u.getProfileImageUrl()).into(imageView);
            }
        }
    }


    public UserRecyclerAdapter(Context context, ArrayList<User> users) {
        this.mUsers = users;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.mUsers != null ? this.mUsers.size() : 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
        return new BasicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            if (holder instanceof BasicViewHolder) {
                ((BasicViewHolder) holder).renderUser(mUsers.get(position));
                ((BasicViewHolder) holder).mPosition = position;
            }
        }
    }

    public ArrayList<User> add(ArrayList<User> user) {
        mUsers.addAll(user);
        notifyDataSetChanged();
        return mUsers;
    }

    public ArrayList<User> addTweet(User user) {
        mUsers.add(0, user);
        notifyDataSetChanged();
        return mUsers;
    }

    public ArrayList<User> swap(ArrayList<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
        return mUsers;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        User user = mUsers.get(position);
        return 0;
    }
}

