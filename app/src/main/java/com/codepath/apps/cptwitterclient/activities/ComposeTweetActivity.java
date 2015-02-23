package com.codepath.apps.cptwitterclient.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.models.User;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends ActionBarActivity {

    private User user;
    private ImageView ivUserPhoto;
    private TextView tvName;
    private TextView tvScreenName;
    private EditText etTweet;
    private TwitterClient client;
    private MenuItem menuItem;
    private Long parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // show activity as PopUp
        showAsPopup(ComposeTweetActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        // Get shared preferences
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        // Get the client
        client = TwitterApplication.getRestClient(); // singleton client
        // Get user from intent
//        user = (User) getIntent().getSerializableExtra("user");

        // Get parentId if exists
        parentId = getIntent().getLongExtra("parentId", 0);
        String parentUser = getIntent().getStringExtra("parentUsername");
        // Find views
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivUserPhoto = (ImageView) findViewById(R.id.ivUserPhoto);
        etTweet = (EditText) findViewById(R.id.etTweet);
        if (parentId != 0) {
            etTweet.setText("@" + parentUser);
            etTweet.setSelection(etTweet.getText().length());
        }
        etTweet.addTextChangedListener(getTextChangedListener());

        // Main logic starts here
        String uName = pref.getString("username", "");
        String screenName = pref.getString("screen_name", "");
        String profileImageUrl = pref.getString("profile_image_url", "");

        if (uName.equals("") || screenName.equals("") || profileImageUrl.equals("")) {
            client.getCredentials( new JsonHttpResponseHandler() {
                                       // Success
                                       @Override
                                       public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                           User user = User.fromJSON(response);
                                           Log.d("Debugging", response.toString());

                                           SharedPreferences.Editor edit = pref.edit();
                                           edit.putString("username", user.getName());
                                           edit.putString("screen_name", user.getScreenName());
                                           edit.putString("user_profile_image_url", user.getProfileImageURL());
                                           edit.commit();

                                           // Populate information
                                           tvName.setText(user.getName());
                                           tvScreenName.setText(user.getScreenName());
                                           // Clear user photo
                                           ivUserPhoto.setImageResource(android.R.color.transparent);
                                           // Populate user photo
                                           Picasso.with(getApplicationContext()).load(user.getProfileImageURL()).into(ivUserPhoto);
                                       }

                                       // Failure
                                       @Override
                                       public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                           Log.d("DebugError", errorResponse.toString());
                                           super.onFailure(statusCode, headers, throwable, errorResponse);
                                       }
                                   });
        } else {
            // Populate information
            tvName.setText(uName);
            tvScreenName.setText(screenName);
            // Clear user photo
            ivUserPhoto.setImageResource(android.R.color.transparent);
            // Populate user photo
            Picasso.with(getApplicationContext()).load(profileImageUrl).into(ivUserPhoto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        // Get menu item
        menuItem = menu.findItem(R.id.miCharCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miTweet) {
            composeTweet(etTweet.getText().toString(), parentId);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void showAsPopup(Activity activity) {
        //To show activity as dialog and dim the background, you need to declare android:theme="@style/PopupTheme" on for the chosen activity on the manifest
        activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.height = 600; //fixed height
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        activity.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void composeTweet(String tweetStr, Long parentId) {
        client.composeTweet(tweetStr, parentId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TweetDebug", response.toString());
                setResult(RESULT_OK);
                ComposeTweetActivity.this.finish();
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TweetError", errorResponse.toString());
                setResult(RESULT_CANCELED);
                Toast.makeText(ComposeTweetActivity.this, "Could not compose tweet" + errorResponse.toString(), Toast.LENGTH_SHORT).show();
                ComposeTweetActivity.this.finish();
            }
        });
    }

    private TextWatcher getTextChangedListener() {
        TextWatcher tWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                menuItem.setTitle(Integer.toString(140 - s.toString().length()));
            }
        };

        return tWatcher;
    }
}
