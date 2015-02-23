package com.codepath.apps.cptwitterclient.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Vikramjeet on 2/20/15.
 */
public class Tweet implements Serializable {

    private static long maxId = /*Integer.MAX_VALUE;*/ Long.MAX_VALUE;
    private static long sinceId = 1;

    private String body;
    private long uid; // Unique DB id for the tweet
    private User user;
    private String createdAt;
    private String retweetCount;
    private String favoriteCount;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = json.getString("text")/* + json.getString("source")*/;
            tweet.uid = json.getLong("id");

            if (maxId > tweet.uid) {
                maxId = tweet.uid;
            }

            tweet.createdAt = getRelativeTimeAgo(json.getString("created_at"));
            tweet.user = User.fromJSON(json.getJSONObject("user"));
            tweet.retweetCount = String.valueOf(json.getLong("retweet_count"));
            tweet.favoriteCount = String.valueOf(json.getLong("favorite_count"));
        } catch (JSONException e) {
            Log.d("Tweet", "Json parse exception");
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet =Tweet.fromJSON(tweetJSON);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    // Helper methods

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static void setMaxId(long id) {
        Log.d("TWEET", "Max id set is " + id);
        maxId = id;
    }

    public static long getMaxId() {
        return maxId;
    }

    public static long getSinceId() {
        return sinceId;
    }

}
