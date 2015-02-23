package com.codepath.apps.cptwitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Vikramjeet on 2/20/15.
 */
public class User implements Serializable {

    private String name;
    private long uid;
    private String screenName;
    private String profileImageURL;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageURL = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
