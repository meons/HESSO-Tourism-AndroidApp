package com.dsv.tourism.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.fragments.AnsweredQuizFragment;
import com.dsv.tourism.fragments.OfficeFragment;
import com.dsv.tourism.fragments.QuizFragment;
import com.dsv.tourism.model.Office;
import com.dsv.tourism.model.Quiz;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OfficeFragment.OnFragmentInteractionListener,
        AnsweredQuizFragment.OnFragmentInteractionListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private FragmentManager mSupportFragmentManager;
    private Toolbar mToolbar;
    private ImageButton mResetSession;
    private TextView mTouristReference;

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private  ActionBarDrawerToggle drawerToggle;
    private int mSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First launch tutorial slider
        launchTutorialAtFirstStart();

        // get toolbar and set it as action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mSupportFragmentManager = getSupportFragmentManager();

        mDrawer= (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  //* host Activity
                mDrawerLayout,         //* DrawerLayout object
                R.string.drawer_open,  //* "open drawer" description
                R.string.drawer_close  //* "close drawer" description
        ) {
            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //default it set first item as selected
        mSelectedId = savedInstanceState == null ? R.id.navigation_item_quizzes: savedInstanceState.getInt("SELECTED_ID");
        selectItem(mSelectedId);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // new tourist reference, stored in shared preferences
        createTouristReference();

        // Reset Session
        mResetSession = (ImageButton) findViewById(R.id.image_button_reset_session);
        mResetSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTouristSession();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case R.id.navigation_item_quizzes:
                fragment = new OfficeFragment();
                break;
            case R.id.navigation_item_answered_quizzes:
                fragment = new AnsweredQuizFragment();
                break;
            case R.id.navigation_sub_item_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.navigation_sub_item_options:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        if (fragment != null) {
            mSupportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        //getSupportFragmentManager().popBackStack();
        mSupportFragmentManager.popBackStack();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        selectItem(mSelectedId);

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // turn on the Navigation Drawer image;
        // this is called in the LowerLevelFragments
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Office o) {
        // The user select an Office from the OfficeFragment

        // We don't need to capture a new fragment because ww only create a one pane layout
        // If the frag is not available, we're in the one-pane layout and must swap frags...

        // Create fragment and give it an argument for the selected article
        QuizFragment newFragment = new QuizFragment();
        Bundle args = new Bundle();

        args.putInt(QuizFragment.ARG_OFFICE_ID, o.getmId());
        newFragment.setArguments(args);
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.main_content, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onAnsweredQuizSelected(Quiz q) {
        Toast.makeText(getApplicationContext(), "Quiz "+q.getmName(), Toast.LENGTH_SHORT).show();
    }

    private void launchTutorialAtFirstStart() {
        // First launch tutorial slider
        //SharedPreferences pref = getSharedPreferences(getString(R.string.preference_file_tutorial), Context.MODE_PRIVATE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (!pref.getBoolean("appFirstLaunch", false) || pref.getBoolean(getString(R.string.pref_tutorial_key), false)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("appFirstLaunch", true);
            editor.putBoolean(getString(R.string.pref_tutorial_key), false);
            //editor.commit();
            editor.apply();
            Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Create a new tourist reference, store it in shared preferences
     * and update reference in the drawer header
     */
    private void createTouristReference() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (!pref.getBoolean("touristReferenceCreated", false)) {

            char[] chars = "ABCDEFGHIJKLMNPQRSTUVWXYZ".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("touristReferenceCreated", true);
            editor.putString(getString(R.string.preference_tourist_reference), sb.toString());
            editor.apply();
        }

        // retrieve from shared preferences
        mTouristReference = (TextView) findViewById(R.id.text_view_reference);
        mTouristReference.setText(pref.getString(getString(R.string.preference_tourist_reference), "NA"));
    }

    /**
     * Restart a new user session
     */
    private void resetTouristSession() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("appFirstLaunch", false);
        editor.putBoolean("touristReferenceCreated", false);
        editor.apply();

        // restart the main activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
