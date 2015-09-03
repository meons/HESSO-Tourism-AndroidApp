package com.dsv.tourism.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.fragments.TutorialBaseFragment;
import com.dsv.tourism.fragments.TutorialStepFiveFragment;
import com.dsv.tourism.fragments.TutorialStepFourFragment;
import com.dsv.tourism.fragments.TutorialStepThreeFragment;
import com.dsv.tourism.fragments.TutorialStepTwoFragment;
import com.dsv.tourism.ui.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Vince on 22.08.2015.
 */
public class ScreenSlidePagerActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES = 5;

    private int currentPosition;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private CustomViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private ImageButton mForwardButton;
    private ImageButton mBackButton;

    private Button mDoneButton;
    private EditText mEditTextAge;
    private RadioGroup mRadioGroupGender;
    private RadioButton mRadioButtonGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);
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

                mEditTextAge = (EditText)findViewById(R.id.edit_text_tutorial_age);
                mRadioGroupGender = (RadioGroup)findViewById(R.id.radio_group_gender);

                // Some ugly implemented tests on field...
                if (mEditTextAge == null || mEditTextAge.getText().equals("") || mEditTextAge.length() == 0 ) {
                    showSnakeBar(findViewById(R.id.pager), getString(R.string.tutorial_age_error));
                } else if(mRadioGroupGender.getCheckedRadioButtonId() == -1) {
                    showSnakeBar(findViewById(R.id.pager), getString(R.string.tutorial_gender_error));
                } else { // only close tutorial if fields are filled
                    // check date format
                    if (!isValidDate(mEditTextAge.getText().toString())) {
                        showSnakeBar(findViewById(R.id.pager), getString(R.string.tutorial_age_date_error));
                    } else {
                        // Save fileds on Shared Preferences
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ScreenSlidePagerActivity.this);
                        SharedPreferences.Editor editor = pref.edit();

                        // Save Birth date
                        editor.putString(getString(R.string.pref_age_key), mEditTextAge.getText().toString());

                        // Save gender
                        int selectedId = mRadioGroupGender.getCheckedRadioButtonId();
                        mRadioButtonGender = (RadioButton) findViewById(selectedId);
                        try {
                            editor.putInt(getString(R.string.pref_gender_key), Integer.parseInt(mRadioButtonGender.getTag().toString()));
                        } catch (NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }

                        editor.apply();
                        finish();
                    }
                }
            }
        });


        //Bind the title indicator to the adapter
        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);

        titleIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                // First slide
                if (position == 0) {
                    mBackButton.setVisibility(View.GONE);
                } else {
                    mBackButton.setVisibility(View.VISIBLE);
                }

                // Last slide
                if (position == NUM_PAGES - 1) {
                    mForwardButton.setVisibility(View.GONE);
                    mDoneButton.setVisibility(View.VISIBLE);
                } else {
                    mForwardButton.setVisibility(View.VISIBLE);
                    mDoneButton.setVisibility(View.GONE);
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
            switch (position) {
                case 0:
                    return new TutorialBaseFragment();
                case 1:
                    return new TutorialStepTwoFragment();
                case 2:
                    return new TutorialStepThreeFragment();
                case 3:
                    return new TutorialStepFourFragment();
                case 4:
                    return new TutorialStepFiveFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void showSnakeBar(View v, String message) {
        Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


    private static boolean isValidDate(String text) {
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
}
