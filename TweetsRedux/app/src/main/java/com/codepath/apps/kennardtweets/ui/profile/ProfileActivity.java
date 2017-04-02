package com.codepath.apps.kennardtweets.ui.profile;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetActivity;

public class ProfileActivity extends BaseTweetActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screenName = getIntent().getStringExtra("screenName");

        if (savedInstanceState == null) {
            ProfileHeaderFragment headerFragment = ProfileHeaderFragment.newInstance(screenName);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutHeader, headerFragment).commit();
            UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutProfile, userTimeLineFragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.profile));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutID(){return R.layout.activity_profile;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
