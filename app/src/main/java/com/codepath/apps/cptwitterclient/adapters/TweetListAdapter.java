package com.codepath.apps.cptwitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.cptwitterclient.R;
import com.codepath.apps.cptwitterclient.TwitterApplication;
import com.codepath.apps.cptwitterclient.activities.ComposeTweetActivity;
import com.codepath.apps.cptwitterclient.activities.ProfileActivity;
import com.codepath.apps.cptwitterclient.models.Tweet;
import com.codepath.apps.cptwitterclient.network.TwitterClient;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vikramjeet on 2/20/15.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvTweetBody;
        TextView tvTimeStamp;
        TextView tvRetweet;
        TextView tvFavorites;
        ImageButton btnReply;
    }

    private TwitterClient client = TwitterApplication.getRestClient();
    private final int REQUEST_CODE = 200;

    public TweetListAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get tweet
        final Tweet tweet = getItem(position);
        //View look up cache stored in tag;
        ViewHolder viewHolder = null;
        if (convertView == null) {      //If recycled view is not available
            viewHolder = new ViewHolder();
            // create new convert view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            // Find subviews
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvTweetBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.tvRetweet = (TextView) convertView.findViewById(R.id.tvRetweet);
            viewHolder.tvFavorites = (TextView) convertView.findViewById(R.id.tvFavorites);
            viewHolder.btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ComposeTweetActivity.class);
//                i.putExtra("user", user);
                i.putExtra("parentId", tweet.getUid());
                i.putExtra("parentUsername", tweet.getUser().getScreenName());
                getContext().startActivity(i);
            }
        });

        //Populate Data
        viewHolder.tvUsername.setText(tweet.getUser().getScreenName());
        viewHolder.tvTweetBody.setText(tweet.getBody());
        viewHolder.tvTimeStamp.setText(tweet.getCreatedAt());
        viewHolder.tvRetweet.setText(tweet.getRetweetCount());
        viewHolder.tvFavorites.setText(tweet.getFavoriteCount());

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageURL()).into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Profile Activity
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", tweet.getUser());
                getContext().startActivity(i);
            }
        });
        // Return view
        return convertView;
    }
}
