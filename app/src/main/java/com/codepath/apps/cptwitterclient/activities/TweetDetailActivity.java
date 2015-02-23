package com.codepath.apps.cptwitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.models.Tweet;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.squareup.picasso.Picasso;

public class TweetDetailActivity extends ActionBarActivity {

    private ImageView ivUserPhoto;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvTweetDetail;
    private TextView tvTimestamp;
    private TextView tvTweetCount;
    private TextView tvFavoritesCount;
    private ImageButton btnReply;
    private TwitterClient client = TwitterApplication.getRestClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // Get tweet from intent
        final Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        // Find views
        tvUserName = (TextView) findViewById(R.id.tvTweetDetailUserName);
        tvScreenName = (TextView) findViewById(R.id.tvTweetDetailScreenName);
        tvTweetDetail = (TextView) findViewById(R.id.tvTweetDetail);
        ivUserPhoto = (ImageView) findViewById(R.id.ivTweetDetailUserPhoto);
        tvTimestamp = (TextView) findViewById(R.id.tvTweetDetailTimeStamp);
        tvTweetCount = (TextView) findViewById(R.id.tvTweetDetailRetweet);
        tvFavoritesCount = (TextView) findViewById(R.id.tvTweetDetailFavorites);
        btnReply = (ImageButton) findViewById(R.id.btnTweetDetailReply);

        // Fill in the values
        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvTweetDetail.setText(Html.fromHtml(tweet.getBody()));
        tvTimestamp.setText(tweet.getCreatedAt());
        tvTweetCount.setText(tweet.getRetweetCount());
        tvFavoritesCount.setText(tweet.getFavoriteCount());

        btnReply.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(TweetDetailActivity.this, ComposeTweetActivity.class);
                                            i.putExtra("parentId", tweet.getUid());
                                            i.putExtra("parentUsername", tweet.getUser().getScreenName());
                                            startActivity(i);
                                        }
                                    });

        // Load the photo
        ivUserPhoto.setImageResource(android.R.color.transparent);

        Picasso.with(getApplicationContext())
                .load(tweet.getUser().getProfileImageURL())
                .placeholder(R.drawable.photo_placeholder)
                .into(ivUserPhoto);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_detail, menu);
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
