package com.codepath.apps.kennardtweets.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.adapters.TimelineRecyclerAdapter;
import com.codepath.apps.kennardtweets.models.Tweet;
import com.codepath.apps.kennardtweets.ui.helper.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by raprasad on 3/29/17.
 */

public class BaseTweetFragment extends Fragment {
    public static final String TAG = BaseTweetFragment.class.getName();
    protected @BindView(R.id.lvTweets)
    RecyclerView lvTweets;
    protected @BindView((R.id.swipeContainer))
    SwipeRefreshLayout swipeContainer;
    protected ArrayList<Tweet> mTweets;
    protected TimelineRecyclerAdapter mArrayAdapter;
    protected LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_tweet, container, false);
        ButterKnife.bind(this, v);
        lvTweets.setAdapter(mArrayAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        lvTweets.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        lvTweets.addItemDecoration(itemDecoration);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mTweets.size() < 1) {
                    populateTimeline(0, 1);
                } else {
                    populateTimeline(mTweets.get(mTweets.size() - 1).getUid() -1 , 0);
                }
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code     is needed to append new items to the bottom of the list
                final int itemCt = totalItemsCount;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        long maxId = mTweets.get(itemCt - 1).getUid() -1;
                        populateTimeline(maxId, 0);
                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        lvTweets.addOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTweets = new ArrayList<>();
        mArrayAdapter = new TimelineRecyclerAdapter(getActivity(), mTweets);
    }

    protected void populateTimeline(final long maxId, long sinceID){
    }
}
