package com.codepath.apps.kennardtweets.ui.profile;

/**
 * Created by raprasad on 4/1/17.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.models.User;
import com.codepath.apps.kennardtweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;



/**
 * Created by raprasad on 3/31/17.
 */


import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;


/**
 * Created by raprasad on 3/31/17.
 */

public class ProfileHeaderFragment extends Fragment{
    private TwitterClient client;
    @BindView(R.id.tvScreenUserName)
    TextView tvUserName;
    @BindView(R.id.tvTagLine)
    TextView tvDescription;
    @BindView(R.id.ivUser)
    ImageView ivUser;
    @BindView(R.id.tvfollowingCount)
    TextView tvFollowing;
    @BindView(R.id.tvfriendCount)
    TextView tvFollowers;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
    }

    public static ProfileHeaderFragment newInstance(String screenName) {
        ProfileHeaderFragment fragmentUser = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        fragmentUser.setArguments(args);
        return fragmentUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_header_profile, container, false);
        ButterKnife.bind(this, v);
        String screenName = getArguments().getString("screenName");

        client.getUserProfile(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                User u = User.fromJSON(response);
                if (u != null){
                    tvUserName.setText(u.getScreenName());
                    tvFollowing.setText("Following " + u.getFollowingsCount());
                    tvFollowers.setText("Followers " + u.getFollowersCount());
                    tvDescription.setText(u.getTagline());
                    Glide.with((getActivity()))
                            .load(u.getProfileImageUrl()).into(ivUser);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        return v;
    }

}


