package com.dsv.tourism.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsv.tourism.fragments.TabRecommendationFragment;
import com.dsv.tourism.fragments.TabResultFragment;

/**
 * Created by Vince on 03.09.2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private int mParticipationId;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, int mParticipationId) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mParticipationId = mParticipationId;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabRecommendationFragment tab1 = TabRecommendationFragment.newInstance(mParticipationId);
                return tab1;
            case 1:
                TabResultFragment tab2 = TabResultFragment.newInstance(mParticipationId);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
