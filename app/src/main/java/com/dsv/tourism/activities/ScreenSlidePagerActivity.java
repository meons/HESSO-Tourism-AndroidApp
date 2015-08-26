package com.dsv.tourism.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.dsv.tourism.R;
import com.dsv.tourism.fragments.TutorialBaseFragment;
import com.dsv.tourism.fragments.TutorialStepThreeFragment;
import com.dsv.tourism.fragments.TutorialStepTwoFragment;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Vince on 22.08.2015.
 */
public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private ImageButton mForwardButton;
    private ImageButton mBackButton;

    private Button mDoneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        /*
        Launcher tutorial back and forward buttons listener
         */
        mBackButton = (ImageButton) findViewById(R.id.arrow_backward);
        mForwardButton = (ImageButton) findViewById(R.id.arrow_forward);
        mDoneButton = (Button) findViewById(R.id.done);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Bind the title indicator to the adapter
        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);

        titleIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                if (position == NUM_PAGES - 1) {
                    mForwardButton.setVisibility(View.GONE);
                    mDoneButton.setVisibility(View.VISIBLE);
                } else {
                    mForwardButton.setVisibility(View.VISIBLE);
                    mDoneButton.setVisibility(View.GONE);
                }

                if (position == 0) {
                    mBackButton.setVisibility(View.GONE);
                } else {
                    mBackButton.setVisibility(View.VISIBLE);
                }
            }
        });

        titleIndicator.setViewPager(mPager);


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            //mPagerAdapter.getItemPosition(mPager.getCurrentItem());

            switch (position) {
                case 0:
                    return new TutorialBaseFragment();
                case 1:
                    return new TutorialStepTwoFragment();
                case 2:
                    return new TutorialStepThreeFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
