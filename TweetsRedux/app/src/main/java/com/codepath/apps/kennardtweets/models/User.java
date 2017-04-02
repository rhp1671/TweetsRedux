package com.codepath.apps.kennardtweets.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raprasad on 3/24/17.
 */

public class User implements Parcelable{

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followersCount;

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    private int followingsCount;

    public String getName() {
        return "@"+name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl.replace("_normal", "");
    }

    public static User fromJSON(JSONObject json) {
        User user = null;
        if (json != null) {
        try {
                user = new User();
                user.name = json.getString("name");
                user.uid = json.getLong("id");
                user.screenName = json.getString("screen_name");
                user.profileImageUrl = json.getString("profile_image_url");
                user.tagline = json.getString("description");
                user.followersCount = json.getInt("followers_count");
                user.followingsCount = json.getInt("friends_count");

            } catch (JSONException e) {
                Log.d("DEBUG", e.getMessage());
            }
        }
        return user;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(uid);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeString(tagline);
        dest.writeInt(followersCount);
        dest.writeInt(followingsCount);
    }

    public User(Parcel source){

        name=source.readString();
        uid=source.readLong();
        screenName=source.readString();
        profileImageUrl=source.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
    };

    public User(){}

        @Override
    public int describeContents() {
        return 0;
    }

}






