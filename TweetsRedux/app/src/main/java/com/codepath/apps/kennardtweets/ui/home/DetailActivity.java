package com.codepath.apps.kennardtweets.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetActivity;
import com.codepath.apps.kennardtweets.ui.profile.ProfileHeaderFragment;
import com.codepath.apps.kennardtweets.ui.profile.UserTimeLineFragment;

import static com.codepath.apps.kennardtweets.R.id.ivUser;

public class DetailActivity extends BaseTweetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showToolbarIcon(false);
         Tweet t  = getIntent().getParcelableExtra("tweet");
        if (savedInstanceState == null) {
            if (t != null) {
                ProfileHeaderFragment headerFragment = ProfileHeaderFragment.newInstance(t.getUser().getScreenName());
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutHeader, headerFragment).commit();
                TextView tvTweet = (TextView) findViewById(R.id.tweet);
                tvTweet.setText(t.getBody());
                ImageView  imageView = (ImageView) findViewById(R.id.imageTweet);
                Glide.with((getApplicationContext()))
                        .load(t.getMediaUrl()).into(imageView);
            }
        }

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
