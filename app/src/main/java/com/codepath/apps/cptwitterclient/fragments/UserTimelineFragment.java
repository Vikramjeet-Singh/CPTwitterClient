package com.codepath.apps.cptwitterclient.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.models.Tweet;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vikramjeet on 3/1/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimelineWithMaxId(null, Long.MAX_VALUE);
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimelineWithMaxId(Long sinceId, final long maxId) {

        if (maxId == Long.MAX_VALUE) {
            Tweet.setMaxId(Long.MAX_VALUE);
        }

        long since_id = 1;

        if (sinceId != null) {
            since_id = sinceId;
        }

        String name = getArguments().getString("screen_name");
        client.getUserTimeline(name, since_id,maxId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG:", response.toString());
                // Clear tweetAdapter
                if (maxId == Long.MAX_VALUE) {
                    clearList();
                }
                // Add new data to tweetAdapter
                addAll(Tweet.fromJSONArray(response));
                // Now we call setRefreshing(false) to signal refresh has finished
                stopRefreshing();

            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                stopRefreshing();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
