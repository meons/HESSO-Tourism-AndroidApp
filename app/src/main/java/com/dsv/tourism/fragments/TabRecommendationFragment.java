package com.dsv.tourism.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.RecommendationAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.model.Recommendation;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabRecommendationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabRecommendationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabRecommendationFragment extends Fragment {


    private static final String ARG_PARTICIPATION_ID = "com.dsv.PARTICIPATION_ID";

    /**
     * A list of answers retrieve from Azure Mobile Service
     */
    private MobileServiceList<Recommendation> mMSLRecommendations;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mParticipationId;

    /**
     * Used to identify class when logging
     */
    private static final String TAG = TabRecommendationFragment.class.getName();

    /**
     * A list of question answers
     */
    private ArrayList<Recommendation> mRecommendations = new ArrayList<Recommendation>();

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private RecommendationAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param participationId Parameter 1.
     * @return A new instance of fragment TabRecommendationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabRecommendationFragment newInstance(int participationId) {
        TabRecommendationFragment fragment = new TabRecommendationFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        // Add parameters to the argument bundle
        args.putInt(ARG_PARTICIPATION_ID, participationId);
        fragment.setArguments(args);

        return fragment;
    }

    public TabRecommendationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.

        RecyclerView recList = (RecyclerView) getActivity().findViewById(R.id.recycler_view_recommendation);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParticipationId = getArguments().getInt(ARG_PARTICIPATION_ID);
        }

        // create the adapter for answers list
        mAdapter = new RecommendationAdapter(mRecommendations);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_recommendation, container, false);


        getRecommendationsFromTable();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTabRecommendationFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onTabRecommendationFragmentInteraction(Uri uri);
    }


    /**
     * Refresh the list with the items in the Mobile Service Table
     */
    private void getRecommendationsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mMSLRecommendations = DataHelper.getRecommendationsByParticipationId(mParticipationId);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (Recommendation r : mMSLRecommendations) {
                                mRecommendations.add(r);
                            }

                            mAdapter.notifyDataSetChanged();

                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, "Error: " + exception.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
