package com.dsv.tourism.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.DrawerAdapter;
import com.dsv.tourism.fragments.OfficeFragment;
import com.dsv.tourism.fragments.QuizFragment;
import com.dsv.tourism.model.Office;
import com.dsv.tourism.ui.Items;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OfficeFragment.OnFragmentInteractionListener {

    private String[] mDrawerTitles;
    private String[] mFooterTitles;
    private TypedArray mDrawerIcons;
    private ArrayList<Items> drawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private FragmentManager mSupportFragmentManager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First launch tutorial slider
        launchTutorialAtFirstStart();

        // new tourist reference, stored in shared preferences
        createTouristReference();

        // get toolbar and set it as action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mSupportFragmentManager = getSupportFragmentManager();

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
        mFooterTitles = getResources().getStringArray(R.array.footer_titles);
        mDrawerIcons = getResources().obtainTypedArray(R.array.drawer_icons);
        drawerItems = new ArrayList<Items>();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        for (int i = 0; i < mDrawerTitles.length; i++) {
            drawerItems.add(new Items(mDrawerTitles[i], mDrawerIcons.getResourceId(i, -(i + 1))));
        }

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                mToolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header,
                mDrawerList, false);

        final ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer,
                mDrawerList, false);

        // Give your Toolbar a subtitle!
        /* mToolbar.setSubtitle("Subtitle"); */

        mDrawerList.addHeaderView(header, null, false); // true = clickable
        mDrawerList.addFooterView(footer, null, false); // true = clickable

        //Set width of drawer
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        lp.width = calculateDrawerWidth();
        mDrawerList.setLayoutParams(lp);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter(getApplicationContext(), drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        FragmentTransaction tx = mSupportFragmentManager.beginTransaction();
        tx.replace(R.id.main_content, new OfficeFragment());
        tx.commit();
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
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                break;
            case 1:
                fragment = new OfficeFragment();
                break;
            case 2:
                fragment = new OfficeFragment();
                break;
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            //FragmentManager fragmentManager = getSupportFragmentManager();


            mSupportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        if (position != 0) {
            setTitle(mDrawerTitles[position - 1]);
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void footerClick(View view) {

        Fragment fragment = null;
        boolean settingsCheck = false;
        mDrawerLayout.closeDrawer(mDrawerList);

        if (view.getId() == R.id.footer_text1) {
            //Settings
            fragment = new OfficeFragment();
            mTitle = mDrawerTitles[0];
        } else if (view.getId() == R.id.footer_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            //mTitle = mDrawerTitles[1];
        }

        if (!settingsCheck && fragment != null) {
            // update the main content by replacing fragments
            //FragmentManager fragmentManager = getSupportFragmentManager();
            mSupportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (!settingsCheck && getFragmentManager().findFragmentByTag("preference") != null) {

                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("preference")).commit();
            }
        }
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

        Toast.makeText(getApplicationContext(), "msg msg 1", Toast.LENGTH_SHORT).show();

        //This method is called when the up button is pressed. Just the pop back stack.
        //getSupportFragmentManager().popBackStack();
        mSupportFragmentManager.popBackStack();
        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
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

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.main_content, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private int calculateDrawerWidth() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        Display display = getWindowManager().getDefaultDisplay();
        int width;
        int height;
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();  // deprecated
            height = display.getHeight();  // deprecated
        }
        return width - actionBarHeight;
    }

    private void launchTutorialAtFirstStart() {
        // First launch tutorial slider
        //SharedPreferences pref = getSharedPreferences(getString(R.string.preference_file_tutorial), Context.MODE_PRIVATE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //if (!pref.getBoolean("appFirstLaunch", false) || pref.getBoolean(getString(R.string.pref_tutorial_key), false)) {
        if (!pref.getBoolean("appFirstLaunch", false)) {
            SharedPreferences.Editor editor = pref.edit();
            //editor.putBoolean("appFirstLaunch", true);
            editor.putBoolean(getString(R.string.pref_tutorial_key), false);
            //editor.commit();
            editor.apply();
            Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
            startActivity(intent);
        }
    }

    private void createTouristReference() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


        if (!pref.getBoolean("touristReferenceCreated", false)) {

            char[] chars = "ABCDEFGHIJKLMNPQRSTUVWXYZ".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("touristReferenceCreated", true);
            editor.putString(getString(R.string.preference_tourist_reference), sb.toString());
            editor.apply();
        }
    }
}
