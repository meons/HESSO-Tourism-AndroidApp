package com.dsv.tourism.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dsv.tourism.R;
import com.dsv.tourism.activities.ResultActivity;
import com.dsv.tourism.adapter.AnsweredQuizAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.model.Participation;
import com.dsv.tourism.model.Quiz;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class AnsweredQuizFragment extends Fragment implements AbsListView.OnItemClickListener {

    private ArrayList<Quiz> quizzes;
    private MobileServiceList<Participation> participation;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private AnsweredQuizAdapter mAdapter;

    private SharedPreferences mSharedPreference;

    private CircularProgressView circularProgressView;

    /**
     * Used to identify class when logging
     */
    private static final String TAG = OfficeFragment.class.getName();

    // TODO: Rename and change types of parameters
    public static AnsweredQuizFragment newInstance(String param1, String param2) {
        AnsweredQuizFragment fragment = new AnsweredQuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnsweredQuizFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init azure service
        DataHelper.init(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // create the adapter for offices list
        mAdapter = new AnsweredQuizAdapter(getActivity(), R.layout.row_list_answered_quiz);

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answered_quiz, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        // get the progress view indicator and set it as visible
        circularProgressView = (CircularProgressView) getActivity().findViewById(R.id.progress_view);
        circularProgressView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        circularProgressView.setVisibility(View.VISIBLE);
        refreshAnsweredQuizListFromTable();
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
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {

            Quiz q = quizzes.get(position);

            Log.i(TAG, "Crate activity with param : " + q.getmParticipationId());
            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra(ResultActivity.ARG_PARTICIPATION_ID, q.getmParticipationId());
            //intent.putExtra(ResultActivity.ARG_TOURIST_ID, q.getmTouristId());
            startActivity(intent);
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onAnsweredQuizSelected(quizzes.get(position));
        }
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
        public void onAnsweredQuizSelected(Quiz q);
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */
    private void refreshAnsweredQuizListFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    String touristReference = mSharedPreference.getString(getString(R.string.preference_tourist_reference), "NA");
                    quizzes = DataHelper.getQuizzesByTouristReference(touristReference);

                    if(null != quizzes) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.clear();

                                for (Quiz q : quizzes) {
                                    mAdapter.add(q);
                                }

                                circularProgressView.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        NoDataFragment nextFrag= new NoDataFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, nextFrag)
                                .addToBackStack(null)
                                .commit();
                    }
                } catch (Exception exception) {
                    Log.e(TAG, "Error: " + exception.getMessage());
                }
                return null;
            }
        }.execute();

        mAdapter.clear();
    }
}
