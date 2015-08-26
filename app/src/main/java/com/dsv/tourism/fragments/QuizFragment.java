package com.dsv.tourism.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.QuizAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.model.Quiz;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

public class QuizFragment extends Fragment {

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private QuizAdapter mAdapter;

    /**
     * A list of quizzes retrieve from Azure Mobile Service
     */
    private MobileServiceList<Quiz> quizzes;

    /**
     * The fragment's ListView holding quizzes
     */
    private AbsListView mListViewQuizzes;

    /**
     * Set the select office id to -1 for non existing office. Used later to get current office
     * for example on phone rotate
     */
    int mCurrentOfficeId = -1;

    /**
     * The argument passed to this fragments. Can be call from it's fragment activity,
     * so must be public. Used to have always the good argument name
     */
    public final static String ARG_OFFICE_ID = "office_id";

    /**
     * Used to identify class when logging
     */
    private static final String TAG = OfficeFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init azure service
        DataHelper.init(getActivity());

        // create the adapter for quizzes list
        mAdapter = new QuizAdapter(getActivity(), R.layout.row_list_quiz);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentOfficeId = savedInstanceState.getInt(ARG_OFFICE_ID);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateQuizzesList(args.getInt(ARG_OFFICE_ID));
        } else if (mCurrentOfficeId != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateQuizzesList(mCurrentOfficeId);
        }
    }

    public void updateQuizzesList(int officeId) {
        // Set the adapter to the quizzes list
        mListViewQuizzes = (AbsListView) getActivity().findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListViewQuizzes).setAdapter(mAdapter);

        // get data from Azure mobile service and update the listview by adding rows to adapter
        refreshQuizListFromTable(officeId);

        // set the office id to for current instance state
        mCurrentOfficeId = officeId;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_OFFICE_ID, mCurrentOfficeId);
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     *
     * @param officeId  The selected office ID
     */
    private void refreshQuizListFromTable(Integer officeId) {
        final Integer id = officeId;

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    quizzes = DataHelper.getQuizzesByOfficeId(id);

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (Quiz q : quizzes) {
                                mAdapter.add(q);
                            }
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
