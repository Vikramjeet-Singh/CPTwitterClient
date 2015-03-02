package com.codepath.apps.cptwitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Vikramjeet on 2/20/15.
 */
@Table(name = "User")
public class User extends Model implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "u_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageURL;
    @Column(name = "description")
    private String description;
    @Column(name = "follower_count")
    private long followerCount;
    @Column(name = "following_count")
    private long followingCount;
    @Column(name = "tweet_count")
    private long tweetCount;
    @Column(name = "background_profile_image_url")
    private String backgroundProfileImageURL;

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

    public String getDescription() {
        return description;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public Long getFollowingCount() {
        return followingCount;
    }

    public Long getTweetCount() {
        return tweetCount;
    }

    public String getBackgroundProfileImageURL() {
        return backgroundProfileImageURL;
    }

   public User() {
       super();
   }

    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageURL = json.getString("profile_image_url");
            user.description = json.getString("description");
            user.backgroundProfileImageURL = json.getString("profile_background_image_url");

            Long followers = json.getLong("followers_count");
            if (followers != null) {
                user.followerCount = followers;
            } else {
                user.followerCount = 0;
            }

            Long followings = json.getLong("friends_count");
            if (followings != null) {
                user.followingCount = followings;
            } else {
                user.followingCount = 0;
            }

            Long tweets = json.getLong("statuses_count");
            if (tweets != null) {
                user.tweetCount = tweets;
            } else {
                user.tweetCount = 0;
            }

//            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }


    public static User findOrCreateFromJson(JSONObject json) {

        long id = 0;
        try {
            id = json.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        User existingUser =
                new Select().from(User.class).where("u_id = ?", id).executeSingle();
        if (existingUser != null) {
            // found and return existing
            return existingUser;
        } else {
            // create and return new
            User user = User.fromJSON(json);
            user.save();
            return user;
        }
    }
}
