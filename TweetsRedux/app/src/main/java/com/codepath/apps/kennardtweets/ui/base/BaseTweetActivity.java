package com.codepath.apps.kennardtweets.ui.base;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.kennardtweets.R;
import com.codepath.apps.kennardtweets.utilities.Utils;

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
        //TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
       // tvTitle.setText(R.string.title);
        getSupportActionBar().setTitle(getResources().getString(R.string.title));
    }

    protected abstract int getLayoutID();

    protected void showToolbarIcon(boolean bShow){
        if (bShow){
            getSupportActionBar().setIcon(R.drawable.ic_bar_bird);
        } else {
            getSupportActionBar().setIcon(
                    new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        }
    }

}
