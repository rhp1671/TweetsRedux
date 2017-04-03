package com.codepath.apps.kennardtweets.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem    ;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.adapters.TimelineRecyclerAdapter;
import com.codepath.apps.kennardtweets.TwitterApp;
import com.codepath.apps.kennardtweets.ui.base.BaseTweetActivity;
import com.codepath.apps.kennardtweets.ui.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimelineActivity extends BaseTweetActivity {
    public static final String TAG = TimelineRecyclerAdapter.class.getName();
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;


    public class TweetPagerAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HomeTimeLineFragment();
            } else  if (position == 1){
                return new MentionsTimelineFragment();
            } else {
                return null;
            }

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public TweetPagerAdapter(FragmentManager fm) {
            super(fm);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        viewPager.setAdapter(new TweetPagerAdapter(getSupportFragmentManager()));
        pagerSlidingTabStrip.setViewPager(viewPager);
        showToolbarIcon(true);
    }


    public void onProfileView(MenuItem mi){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("screenName", TwitterApp.getUser().getScreenName());
        startActivity(intent);
    }

    public void onCompose(MenuItem mi){
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment composeTweetDialogFragment = ComposeTweetDialogFragment.newInstance(null);
        composeTweetDialogFragment.show(fm, "compose_tweet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_timeline;
    }
}
