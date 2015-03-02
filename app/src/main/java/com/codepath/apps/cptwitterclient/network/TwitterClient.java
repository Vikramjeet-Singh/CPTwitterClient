package com.codepath.apps.cptwitterclient.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "TOb8Hruy7iO3LmZqTe92bVwWM";
	public static final String REST_CONSUMER_SECRET = "JTJHZZozqcJeRWomZG1qglWXDEaxa4go0xnC1r7NUH48B0tY3z";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long sinceId, long maxID, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("statuses/home_timeline.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("count", 50);
            params.put("since_id", 1);

            if (maxID != Long.MAX_VALUE) {
                params.put("max_id", maxID);
            }

            // Execute the request
            getClient().get(apiUrl, params, handler);
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getMentionsTimeline(long sinceId, long maxID, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("statuses/mentions_timeline.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("count", 50);
            params.put("since_id", 1);

            if (maxID != Long.MAX_VALUE) {
                params.put("max_id", maxID);
            }

            // Execute the request
            getClient().get(apiUrl, params, handler);
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserTimeline(String screenName, long sinceId, long maxID, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("statuses/user_timeline.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("count", 50);
            params.put("since_id", 1);
            params.put("screen_name", screenName);

            if (maxID != Long.MAX_VALUE) {
                params.put("max_id", maxID);
            }

            // Execute the request
            getClient().get(apiUrl, params, handler);
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void composeTweet(String tweet, long parentId, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("statuses/update.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("status", tweet);
            if (parentId != 0) {
                params.put("in_reply_to_status_id", parentId);
            }
            // Execute the request
            getClient().post(apiUrl, params, handler);
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCredentials(AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("account/verify_credentials.json");
            // Execute te request
            getClient().get(apiUrl, handler);
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT);
        }
    }

    public Boolean addFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("favorites/create.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("id", tweetId);
            // Execute te request
            getClient().get(apiUrl, handler);
            return true;
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT);
            return false;
        }
    }

    public Boolean removeFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        if (isNetworkAvailable()) {
            // Get API
            String apiUrl = getApiUrl("favorites/destroy.json");
            // Create parameter list
            RequestParams params = new RequestParams();
            params.put("id", tweetId);
            // Execute te request
            getClient().get(apiUrl, handler);
            return true;
        } else {
            Toast.makeText(context, "Network not available. Please check your network connection", Toast.LENGTH_SHORT);
            return false;
        }

    }



    // Check if Internet is available
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}