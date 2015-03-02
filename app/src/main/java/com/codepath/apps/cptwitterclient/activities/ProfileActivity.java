package com.codepath.apps.cptwitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.cptwitterclient.models.User;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    private User user;
    private TwitterClient client;
    private TextView tvUserName;
    private TextView tvDescription;
    private ImageView ivUserPhoto;
    private TextView tvTweetCount;
    private TextView tvFollowingCount;
    private TextView tvFollowerCount;
    private ImageView ivBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApplication.getRestClient();

        final Bundle savedState = savedInstanceState;
        // Get user from the activity
        user = (User) getIntent().getSerializableExtra("user");

        if (user == null) {
            client.getCredentials(new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    Log.d("Debugging", response.toString());
                    // Populate views
                    populateViews(savedState);
                }

                // Failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DebugError", errorResponse.toString());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            // Populate views
            populateViews(savedInstanceState);
        }

    }

    private void populateViews(Bundle savedInstanceState) {
        // Find views
        tvUserName = (TextView) findViewById(R.id.tvUsername);
        tvDescription = (TextView) findViewById(R.id.tvBody);
        ivUserPhoto = (ImageView) findViewById(R.id.ivProfileImage);
        tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
        tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
        tvFollowerCount = (TextView) findViewById(R.id.tvFollowersCount);
        // Populate information
        tvUserName.setText(user.getName());
        tvDescription.setText(user.getDescription());
        tvTweetCount.setText(user.getTweetCount().toString() + " Tweets");
        tvFollowerCount.setText(user.getFollowerCount().toString() + " Followers");
        tvFollowingCount.setText(user.getFollowingCount().toString() + " Following");
        // Clear user photo
        ivUserPhoto.setImageResource(android.R.color.transparent);
        // Populate user photo
        Picasso.with(getApplicationContext()).load(user.getProfileImageURL()).into(ivUserPhoto);

        // Get screen name
        getSupportActionBar().setTitle("@" + user.getScreenName());

        if (savedInstanceState == null) {
            // Get screen name
            String screenName = user.getScreenName();
            // Create user timeline fragment
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(screenName);
            // Display user fragment
            FragmentTransaction ft = getSupportFragmentManager() .beginTransaction();
            ft.replace(R.id.flContainer, fragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
