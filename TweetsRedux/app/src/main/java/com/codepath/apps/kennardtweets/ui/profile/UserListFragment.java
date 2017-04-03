package com.codepath.apps.kennardtweets.ui.profile;

/**
 * Created by raprasad on 4/2/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.adapters.TimelineRecyclerAdapter;
import com.codepath.apps.kennardtweets.adapters.UserRecyclerAdapter;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.models.User;
import com.codepath.apps.kennardtweets.network.TwitterClient;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetFragment;
import com.codepath.apps.kennardtweets.ui.helper.EndlessRecyclerViewScrollListener;
import com.codepath.apps.kennardtweets.ui.home.ComposeTweetDialogFragment;
import com.codepath.apps.kennardtweets.utilities.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * Created by raprasad on 3/31/17.
 */


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by raprasad on 3/31/17.
 */

public class UserListFragment extends Fragment {
    private TwitterClient client;
    private static int MODE_FOLLOWERS;
    private static int MODE_FOLLOWING;

    public static final String TAG = BaseTweetFragment.class.getName();
    @BindView(R.id.lvTweets)
    RecyclerView lvUsers;
    protected
    @BindView((R.id.swipeContainer))
    SwipeRefreshLayout swipeContainer;
    protected ArrayList<User> musers;
    protected UserRecyclerAdapter mArrayAdapter;
    protected LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static UserListFragment newInstance(long userID, int mode) {
        UserListFragment fragmentUser = new UserListFragment();
        Bundle args = new Bundle();
        args.putLong("userID", userID);
        args.putInt("mode", mode);
        fragmentUser.setArguments(args);
        return fragmentUser;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_tweet, container, false);
        ButterKnife.bind(this, v);
        lvUsers.setAdapter(mArrayAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        lvUsers.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        lvUsers.addItemDecoration(itemDecoration);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (musers.size() < 1) {
                    populateList(0, 1);
                } else {
                    populateList(musers.get(musers.size() - 1).getUid() - 1, 0);
                }
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code     is needed to append new items to the bottom of the list
                final int itemCt = totalItemsCount;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        long maxId = musers.get(itemCt - 1).getUid() - 1;
                        populateList(maxId, 0);
                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        lvUsers.addOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        musers = new ArrayList<>();
        mArrayAdapter = new UserRecyclerAdapter(getActivity(), musers);
        populateList(0, 1);
    }

    protected void populateList(final long maxId, long sinceID) {
        if (checkNetworkConnectivity()) {
            long userID = getArguments().getLong("userID");
            int mode = getArguments().getInt("mode");
            if (mode == MODE_FOLLOWING) {
                client.getFollowing(userID, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        JSONArray users = response.optJSONArray("users");
                        ArrayList<User> a = User.fromJSONArray(users);


                        if (maxId == 0) {
                            musers = mArrayAdapter.swap(a);
                        } else {
                            musers = mArrayAdapter.add(a);
                        }
                        Log.d("debug", response.toString());
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("debug", errorResponse.toString());
                        swipeContainer.setRefreshing(false);
                    }
                });
            } else {
                client.getFollowers(userID, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        JSONArray users = response.optJSONArray("users");
                        ArrayList<User> a = User.fromJSONArray(users);
                        if (maxId == 0) {
                            musers = mArrayAdapter.swap(a);
                        } else {
                            musers = mArrayAdapter.add(a);
                        }
                        Log.d("debug", response.toString());
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("debug", errorResponse.toString());
                        swipeContainer.setRefreshing(false);
                    }
                });
            }
        }
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
