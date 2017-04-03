package com.codepath.apps.kennardtweets.ui.home;

/**
 * Created by raprasad on 3/31/17.
 */

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.network.TwitterClient;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import android.support.annotation.Nullable;



/**
 * Created by raprasad on 3/31/17.
 */

public class MentionsTimelineFragment extends BaseTweetFragment {
    private TwitterClient client;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline(0,1);
    }

    @Override
    protected void populateTimeline(final long maxId, long sinceID) {
        if (checkNetworkConnectivity()) {
            client.getMentionsTimeLine(maxId, sinceID, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    ArrayList a = Tweet.fromJSONArray(response);
                    if (maxId == 0) {
                        mTweets = mArrayAdapter.swap(a);
                    } else {
                        mTweets = mArrayAdapter.add(a);
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

