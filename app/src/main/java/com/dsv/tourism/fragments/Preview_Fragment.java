package com.dsv.tourism.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dsv.tourism.R;
import com.dsv.tourism.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Daniel on 09.11.2014.
 */
public class Preview_Fragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    ArrayList<String> dialogItems;

    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preview, container, false);

        dialogItems = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            dialogItems.add("Item No. "+i);
        }

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(R.color.accent_color,
                R.color.accent_color_light,
                R.color.accent_color_dark,
                R.color.accent_color_light);

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    boolean isTaskRunning = true;

    @Override
    public void onRefresh() {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTaskRunning) {
                    swipeLayout.setRefreshing(false);
                } else {
                    handler.postDelayed(this, 1000);
                    isTaskRunning = false;
                }
            }
        }, 1000);

    }

}
