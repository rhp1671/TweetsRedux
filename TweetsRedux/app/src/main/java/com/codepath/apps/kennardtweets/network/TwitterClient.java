package com.codepath.apps.kennardtweets.network;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.codepath.apps.kennardtweets.ui.home.TimelineActivity.TAG;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "GvAcIJWxhmREMBHtWhp8xrNdT";       // Change this
    public static final String REST_CONSUMER_SECRET = "XJMNAgdfTmh7I75pIyrbL0DnKRPIr8VcuDCeSbknvtR2EXU2hw"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://kennardcb"; // Change this (here and in manifest)
    public static final String  POST_TWEET = "statuses/update.json";
    public static final String GET_TWEETS = "statuses/home_timeline.json";
    public static final String MENTION_TWEETS = "statuses/mentions_timeline.json";
    public static final String GET_USER = "account/verify_credentials.json";
    public static final String GET_USER_TIMELINE = "statuses/user_timeline.json";
    public static final String GET_USER_PROFILE = "users/show.json";
    public static final String GET_FOLLOWING = "friends/list.json";
    public static final String GET_FOLLOWERS = "followers/list.json";
    public static final String SEARCH_TWEETS="search/tweets.json";

    public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void getHomeTimeline(long maxID, long sinceID, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl(GET_TWEETS);
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (sinceID != 0) {
            params.put("since_id", sinceID);
        }
        if (maxID != 0) {
            params.put("max_id", maxID);
        }
        client.get(apiUrl, params, handler);
    }

    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(POST_TWEET);
        if (tweet != null && !tweet.isEmpty()){
            RequestParams params = new RequestParams();
            params.put("status", tweet);
            client.post(apiUrl, params, handler);
        }
    }

    public void getCurrentUser(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(GET_USER);
        RequestParams params = null;
        if (screenName != null) {
            params = new RequestParams();
            params.put("screen_name", screenName);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserProfile(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(GET_USER_PROFILE);
        RequestParams params = null;
        if (screenName != null) {
            params = new RequestParams();
            params.put("screen_name", screenName);
        }
        client.get(apiUrl, params, handler);
    }



    public void getMentionsTimeLine(long maxId, long sinceID, JsonHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl(MENTION_TWEETS);
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (sinceID != 0) {
            params.put("since_id", sinceID);
        }
        if (maxId != 0) {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUsersTimeLine(String screenName, long maxId, long sinceID, JsonHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl(GET_USER_TIMELINE);
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (screenName != null){
            params.put("screen_name", screenName);
        }
        if (sinceID != 0) {
            params.put("since_id", sinceID);
        }
        if (maxId != 0) {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }


    public void getFollowing(long userID,
                             JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl(GET_FOLLOWING);
        RequestParams params = new RequestParams();

            if (userID != 0) {
                params.put("user_id", userID);
            }
             params.put("count", 25);

        client.get(apiUrl, params, handler);
    }

    public void getFollowers(long userID,
                             JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl(GET_FOLLOWERS);
        RequestParams params = new RequestParams();

        if (userID != 0) {
            params.put("user_id", userID);
        }
        params.put("count", 25);

        client.get(apiUrl, params, handler);
    }

    public void searchTweets(String search, long maxId, long sinceID,
                             JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl(SEARCH_TWEETS);

        RequestParams params = new RequestParams();
       // params.put("count", 25);
        if (sinceID != 0) {
            params.put("since_id", sinceID);
        }
        if (maxId != 0) {
            params.put("max_id", maxId);
        }
        if (search != null && !search.isEmpty()) {
            String encoded = "";
            try{
                encoded = URLEncoder.encode(search,"UTF-8");
            } catch (UnsupportedEncodingException e){
                Log.d(TAG, e.getMessage());
            }

            params.put("q", encoded);
        }
        client.get(apiUrl, params, handler);
        }


}
