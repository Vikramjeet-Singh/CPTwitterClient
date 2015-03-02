package com.codepath.apps.cptwitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.activities.TweetDetailActivity;
import com.codepath.apps.cptwitterclient.adapters.TweetListAdapter;
import com.codepath.apps.cptwitterclient.helper.EndlessScrollListener;
import com.codepath.apps.cptwitterclient.models.Tweet;

import java.util.ArrayList;

/**
 * Created by Vikramjeet on 2/28/15.
 */
public abstract class TweetsListFragment extends Fragment {

    private final int REQUEST_CODE = 200;

    private SwipeRefreshLayout swipeContainer;
//    private TwitterClient client;
    private TweetListAdapter tweetAdapter;
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;

    // creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create arraylist (data source)
        tweets = new ArrayList<Tweet>();
        // Create Adapter
        tweetAdapter = new TweetListAdapter(getActivity(), tweets);
    }

    // inflation event
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // Set up views
        // ListView
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);        // Get timeline

        // Hook adapter with list view
        lvTweets.setAdapter(tweetAdapter);

        // Add onItemClickListener to the ListView
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an Intent
                Intent imageIntent = new Intent(getActivity(), TweetDetailActivity.class);
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
                Log.d("SCROLLING", "Page is " + page + "Total Items are " + totalItemsCount + "Max ID " + Tweet.getMaxId() + " and Since Id" + Tweet.getSinceId());
                populateTimelineWithMaxId(Tweet.getSinceId(), Tweet.getMaxId());
            }
        });


        // Get SwipeContainer
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimelineWithMaxId(null, Long.MAX_VALUE);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    protected void addAll(ArrayList<Tweet> tweets) {
        tweetAdapter.addAll(tweets);
    }

    protected void clearList() {
        tweetAdapter.clear();
    }

    protected void stopRefreshing() {
        swipeContainer.setRefreshing(false);
    }

    public abstract void populateTimelineWithMaxId(Long sinceId, long maxId);

}
