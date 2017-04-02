package com.codepath.apps.kennardtweets.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.kennardtweets.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseTweetActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title));
        getSupportActionBar().setIcon(R.drawable.ic_bar_bird);
    }

    protected abstract int getLayoutID();
}
