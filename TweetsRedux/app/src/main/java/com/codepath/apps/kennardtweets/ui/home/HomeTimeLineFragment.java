package com.codepath.apps.kennardtweets.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.network.TwitterClient;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.max;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by raprasad on 3/31/17.
 */

public class HomeTimeLineFragment extends BaseTweetFragment implements ComposeTweetDialogFragment.ComposeTweetDialogListener {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        setHasOptionsMenu(true);
        populateTimeline(0, 1);
    }

    @Override
    protected void populateTimeline(final long maxId, long sinceID) {
        if (checkNetworkConnectivity()) {

            client.getHomeTimeline(maxId, sinceID, new JsonHttpResponseHandler() {
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
                    //    fragmentListener.onLoading(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("debug", errorResponse.toString());
                    swipeContainer.setRefreshing(false);
                    // fragmentListener.onLoading(false);
                }
            });
        }
    }


    @Override
    protected void populateSearchTweets(final long maxId, long sinceID) {
        if (checkNetworkConnectivity()) {

            client.searchTweets(mQuery, maxId, sinceID, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        JSONArray arrayList = response.getJSONArray("statuses");
                        ArrayList a = Tweet.fromJSONArray(arrayList);
                        if (maxId == 0) {
                            mTweets = mArrayAdapter.swap(a);
                        } else {
                            mTweets = mArrayAdapter.add(a);
                        }
                        Log.d("debug", response.toString());
                        swipeContainer.setRefreshing(false);
                    } catch (JSONException e) {

                    }
                    //    fragmentListener.onLoading(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("debug", errorResponse.toString());
                    swipeContainer.setRefreshing(false);
                    // fragmentListener.onLoading(false);
                }
            });
        }
    }


    @Override
    public void onFinishEditDialog(Tweet result) {
        if (result != null) {
            mArrayAdapter.addTweet(result);
            linearLayoutManager.scrollToPosition(0);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);
        mMode = MODE_SEARCH_TWEETS;

        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mMode = MODE_ALL_TWEETS;
                mQuery = "";
                populateTimeline(0,1);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;  // Return true to expand action view
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                populateSearchTweets(0, 1);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
