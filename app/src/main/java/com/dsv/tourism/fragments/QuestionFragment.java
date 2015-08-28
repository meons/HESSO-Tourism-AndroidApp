package com.dsv.tourism.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.AnswerAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.model.Answer;
import com.dsv.tourism.model.Question;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class QuestionFragment extends Fragment {

    /**
     * A list of question answers
     */
    private ArrayList<Answer> mAnswers = new ArrayList<Answer>();

    /**
     * A list of answers retrieve from Azure Mobile Service
     */
    private MobileServiceList<Answer> mMSLAnswers;

    /**
     * A list of questions retrieve from Azure Mobile Service
     */
    private MobileServiceList<Question> mMSLQuestion;

    /**
     * The current question
     */
    private Question mQuestion;

    private OnFragmentInteractionListener mListener;
    private Integer mQuizId;
    private Integer mQuestionId;
    private Integer mNextQuestionId;


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private AnswerAdapter mAdapter;

    /**
     * Textview containing the answers question
     */
    private TextView mTextViewQuestion;

    /**
     * Set the select office id to -1 for non existing office. Used later to get current office
     * for example on phone rotate
     */
    int mCurrentNextQuestionId = -1;
    int mCurrentNextQuestionIdTest;

    /**
     * The argument passed to this fragments. Can be call from it's fragment activity,
     * so must be public. Used to have always the good argument name
     */
    public final static String ARG_QUIZ_ID = "quiz_id";
    public final static String ARG_NEXT_QUESTION_ID = "next_question_id";

    /**
     * Used to identify class when logging
     */
    private static final String TAG = QuestionFragment.class.getName();


    public static QuestionFragment newInstance(Integer nextQuestionId) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NEXT_QUESTION_ID, nextQuestionId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNextQuestionId = getArguments().getInt(ARG_NEXT_QUESTION_ID, -1);
        }

        // init azure service
        DataHelper.init(getActivity());

        // create the adapter for answers list
        mAdapter = new AnswerAdapter(mAnswers);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentNextQuestionId = savedInstanceState.getInt(ARG_NEXT_QUESTION_ID);
        }

        View view = inflater.inflate(R.layout.fragment_question, container, false);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" "); // Don't remove this ! used to hide default activity text

        //for crate home button
        if(null != toolbar) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        mTextViewQuestion = (TextView) getActivity().findViewById(R.id.question);

        RecyclerView recList = (RecyclerView) getActivity().findViewById(R.id.answer_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(mAdapter);
    }

    public void updateQuestion() {
        Bundle args = getArguments();
        if (args != null) {
            if(mNextQuestionId != -1) {
                // next questions, selected by next question id
                refreshQuestionFromTable(mNextQuestionId);
                mCurrentNextQuestionId = mNextQuestionId;
            } else {
                // first question, selected by quiz id
                refreshQuestionByQuizId(args.getInt(ARG_QUIZ_ID));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get data from Azure mobile service and update the listview by adding rows to adapter
        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_NEXT_QUESTION_ID, mCurrentNextQuestionId);
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
        public void onFragmentInteraction(Answer a, Question q);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // add a listener on the RecyclerView adapter
        mAdapter.SetOnItemClickListener(new AnswerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(mAnswers.get(position), mQuestion);
                }
            }
        });
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     *
     * @param quizId  The selected question ID
     */
    private void refreshQuestionByQuizId(Integer quizId) {

        mQuizId = quizId;

        //final Integer id = questionId;

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e(TAG, "Getting Answers for quiz ID : "+mQuizId);

                    mMSLQuestion = DataHelper.getQuestionByQuizId(mQuizId);
                    mQuestion = mMSLQuestion.get(0);

                    Log.e(TAG, "Question is : " + mQuestion.getmText());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewQuestion.setText(mQuestion.getmText());

                            refreshAnswerListFromTable(mQuestion.getmId());
                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, "Error: " + exception.getMessage());
                }
                return null;
            }
        }.execute();
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     *
     * @param questionId  The selected question ID
     */
    private void refreshQuestionFromTable(Integer questionId) {

        mQuestionId = questionId;

        //final Integer id = questionId;

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e(TAG, "Getting Answers for question : "+mQuestionId);

                    mMSLQuestion = DataHelper.getQuestionById(mQuestionId);
                    mQuestion = mMSLQuestion.get(0);

                    Log.e(TAG, "Question is : " + mQuestion.getmText());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewQuestion.setText(mQuestion.getmText());

                            refreshAnswerListFromTable(mQuestion.getmId());
                        }
                    });
                } catch (Exception exception) {
                    Log.e(TAG, "Error: " + exception.getMessage());
                }
                return null;
            }
        }.execute();
    }


    /**
     * Refresh the list with the items in the Mobile Service Table
     *
     * @param questionId  The selected question ID
     */
    private void refreshAnswerListFromTable(Integer questionId) {
        final Integer id = questionId;

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.e(TAG, "Getting Answers for question : "+id);
                    mMSLAnswers = DataHelper.getAnswersByQuestionId(id);
                    Log.e(TAG, "Answers length is : " + mMSLAnswers.size());

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            for (Answer a : mMSLAnswers) {
                                mAnswers.add(a);
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
