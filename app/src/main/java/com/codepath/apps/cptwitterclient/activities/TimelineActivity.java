package com.codepath.apps.cptwitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.adapters.TweetListAdapter;
import com.codepath.apps.cptwitterclient.helper.EndlessScrollListener;
import com.codepath.apps.cptwitterclient.models.Tweet;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {
    private final int REQUEST_CODE = 200;

    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private TweetListAdapter tweetAdapter;
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Set up views
        setUpViews();
        // Get timeline
        populateTimelineWithMaxId(Long.MAX_VALUE);
    }

    private void setUpViews() {
        // Get SwipeContainer
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // ListView
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        // Create arraylist (data source)
        tweets = new ArrayList<Tweet>();
        // Create Adapter
        tweetAdapter = new TweetListAdapter(this, tweets);
        // Hook adapter with list view
        lvTweets.setAdapter(tweetAdapter);
        // Add onItemClickListener to the ListView
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an Intent
                Intent imageIntent = new Intent(TimelineActivity.this, TweetDetailActivity.class);
                // Get the tweet
                Tweet tweet = tweets.get(position);
                // Pass image result into the Intent
                imageIntent.putExtra("tweet", tweet);
                // Start the activity
                startActivity(imageIntent);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("SCROLLING", "Page is " + page + "Total Items are "+ totalItemsCount + "Max ID " + Tweet.getMaxId() + " and Since Id" + Tweet.getSinceId());
                populateTimelineWithMaxId(Tweet.getMaxId());
            }
        });

        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimelineWithMaxId(Long.MAX_VALUE);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get timeline
        populateTimelineWithMaxId(Long.MAX_VALUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Refresh the Timeline
            populateTimelineWithMaxId(Long.MAX_VALUE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
            startActivity(i);


//            client.getCredentials(new JsonHttpResponseHandler() {
//                // Success
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    User user = User.fromJSON(response);
//                    Log.d("Debugging", response.toString());
//
//                    Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
//                    i.putExtra("user", user);
//                    startActivityForResult(i, REQUEST_CODE);
//                }
//
//                // Failure
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    Log.d("DebugError", errorResponse.toString());
//                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                }
//            });

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void populateTimelineWithMaxId(final long maxId) {

        if (maxId == Long.MAX_VALUE) {
            Tweet.setMaxId(Long.MAX_VALUE);
        }

        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG:", response.toString());
                // Clear tweetAdapter
                if (maxId == Long.MAX_VALUE) {
                    tweetAdapter.clear();
                }
                // Add new data to tweetAdapter
                tweetAdapter.addAll(Tweet.fromJSONArray(response));
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


}
