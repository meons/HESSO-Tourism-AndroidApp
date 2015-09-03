package com.dsv.tourism.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.PagerAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.fragments.TabRecommendationFragment;
import com.dsv.tourism.fragments.TabResultFragment;

public class ResultActivity extends AppCompatActivity implements TabRecommendationFragment.OnFragmentInteractionListener, TabResultFragment.OnFragmentInteractionListener{

    private Toolbar mToolbar;
    private int mParticipationId;

    /**
     * The argument passed to this fragments. Can be call from it's fragment activity,
     * so must be public. Used to have always the good argument name
     */
    public final static String ARG_PARTICIPATION_ID = "participation_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        DataHelper.init(getApplicationContext());

        // get toolbar and set it as action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // get clicked quiz ID from answered quiz fragment
        //Intent i = new Intent();
        Bundle b = getIntent().getExtras();
        if( null != b) {
            Log.i("dsd", "Getting Activity param : " + b.getInt(ARG_PARTICIPATION_ID));
            mParticipationId = b.getInt(ARG_PARTICIPATION_ID);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_recommendation_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_result_name)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        Log.i("dsd", "Starting PagerAdapter with param : " + mParticipationId);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), mParticipationId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabRecommendationFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabResultFragmentInteraction(Uri uri) {

    }
}
