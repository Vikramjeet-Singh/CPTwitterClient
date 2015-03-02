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
 * Created by Vikramjeet on 2/28/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        populateTimelineWithMaxId(null, Long.MAX_VALUE);
    }

    @Override
//    public void onResume() {
//        super.onResume();
//        // Get timeline
//        populateTimelineWithMaxId(null, Long.MAX_VALUE);
//    }

    public void populateTimelineWithMaxId(Long sinceId, final long maxId) {

        if (maxId == Long.MAX_VALUE) {
            Tweet.setMaxId(Long.MAX_VALUE);
        }

        long since_id = 1;

        if (sinceId != null) {
            since_id = sinceId;
        }

        client.getMentionsTimeline(since_id, maxId, new JsonHttpResponseHandler() {
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
