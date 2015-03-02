package com.codepath.apps.cptwitterclient.models;

import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.network.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vikramjeet on 2/20/15.
 */

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    private static long maxId = /*Integer.MAX_VALUE;*/ Long.MAX_VALUE;
    private static long sinceId = 1;
    private static TwitterClient client = TwitterApplication.getRestClient();

    @Column(name = "body")
    private String body;
    @Column(name = "u_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; // Unique DB id for the tweet
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "retweet_count")
    private String retweetCount;
    @Column(name = "favorite_count")
    private String favoriteCount;
//    @Column(name = "favorited")
//    private Boolean isFavorited;

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

//    public Boolean isFavorited() {
//        return isFavorited();
//    }

    public Tweet() {
        super();
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");

            if (maxId > tweet.uid) {
                maxId = tweet.uid;
            }

            if (sinceId < tweet.uid) {
                sinceId = tweet.uid;
            }

            tweet.createdAt = getRelativeTimeAgo(json.getString("created_at"));
            tweet.user = User.findOrCreateFromJson(json.getJSONObject("user"));
            tweet.retweetCount = String.valueOf(json.getLong("retweet_count"));
            tweet.favoriteCount = String.valueOf(json.getLong("favorite_count"));
            tweet.save();
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
        return maxId - 1;
    }

    public static long getSinceId() {
        return sinceId;
    }

    // Return cursor for result set for all todo items
    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(Tweet.class).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(Tweet.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        return resultCursor;
    }

    public static List<Tweet> getTweetsFromDB() {
        List<Tweet> tweets = new Select()
                .from(Tweet.class)
                .orderBy("created_at DESC").execute();

        return tweets;
    }


}
