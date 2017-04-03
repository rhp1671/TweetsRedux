package com.codepath.apps.kennardtweets.ui.profile;

/**
 * Created by raprasad on 4/1/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.models.User;
import com.codepath.apps.kennardtweets.network.TwitterClient;
import com.codepath.apps.kennardtweets.ui.home.ComposeTweetDialogFragment;
import com.codepath.apps.kennardtweets.utilities.Utils;
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
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


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


    public static final int CLICK_FOLLOWING = 0;
    public static final int CLICK_FOLLOWERS = 1;
    public long mUserID;

    public interface ProfileHeaderClickListener {
        void onClick(long userID, int clickID, String userName);
    }

    private ProfileHeaderClickListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ProfileHeaderClickListener) context;
        } catch (Exception ex) {

        }
    }

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
        final String screenName = getArguments().getString("screenName");

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(mUserID, CLICK_FOLLOWERS, screenName);
            }
        });
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(mUserID, CLICK_FOLLOWERS, screenName);
            }
        });

        if (checkNetworkConnectivity()) {
            client.getUserProfile(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    User u = User.fromJSON(response);
                    if (u != null) {
                        mUserID = u.getUid();
                        tvUserName.setText(u.getScreenName());
                        Spanned following = Utils.fromHtml(getContext().getResources().getString(R.string.following, u.getFollowingsCount()));
                        tvFollowing.setText(following);
                        Spanned followers = Utils.fromHtml(getContext().getResources().getString(R.string.followers, u.getFollowersCount()));
                        tvFollowers.setText(followers);
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
        }
        return v;
    }

    protected boolean checkNetworkConnectivity(){

        boolean bReturn = true;

        if (!Utils.isNetworkAvailable(getContext())){
            bReturn = false;
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        return bReturn;
    }

}


