package com.dsv.tourism.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dsv.tourism.R;
import com.dsv.tourism.activities.ResultActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class QuestionFeedbackFragment extends Fragment {

    private CircularProgressView circularProgressView;

    private static final String ARG_PARTICIPATION_ID = "com.dsv.PARTICIPATION_ID";
    private int mParticipationId;
    private Button mButtonSeeResult;
    private Button mButtonAnswerAnOtherQuiz;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param participationId The participation id.
     * @return A new instance of fragment TabResultFragment.
     */
    public static QuestionFeedbackFragment newInstance(int participationId) {
        QuestionFeedbackFragment fragment = new QuestionFeedbackFragment();

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

    public QuestionFeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParticipationId = getArguments().getInt(ARG_PARTICIPATION_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_feedback, container, false);


        mButtonSeeResult = (Button) v.findViewById(R.id.button_see_results);
        mButtonSeeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra(ResultActivity.ARG_PARTICIPATION_ID, mParticipationId);
                //intent.putExtra(ResultActivity.ARG_TOURIST_ID, q.getmTouristId());
                startActivity(intent);
                getActivity().finish();
            }
        });

        mButtonAnswerAnOtherQuiz = (Button) v.findViewById(R.id.button_answer_an_other_quiz);
        mButtonAnswerAnOtherQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }

}
