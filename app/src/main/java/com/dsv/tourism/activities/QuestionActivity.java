package com.dsv.tourism.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dsv.tourism.R;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.fragments.QuestionFeedbackFragment;
import com.dsv.tourism.fragments.QuestionFragment;
import com.dsv.tourism.model.Answer;
import com.dsv.tourism.model.Participation;
import com.dsv.tourism.model.Question;
import com.dsv.tourism.model.Result;
import com.dsv.tourism.model.Tourist;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {

    /**
     * Contains all answers.
     */
    private HashMap<Integer, Integer> quizAnswers = new HashMap<Integer, Integer>();

    private int mQuizId;

    private Tourist mTourist;

    private SharedPreferences mSharedPreferences;

    private Participation mParticipation;
    /**
     * Used to identify class when logging
     */
    private static final String TAG = QuestionActivity.class.getName();

    public static final String ARG_RESULT_PARTICIPATION_ID = "arg_result_participation_id";

    /**
     * The argument passed to this fragments. Can be call from it's fragment activity,
     * so must be public. Used to have always the good argument name
     */
    public final static String ARG_QUIZ_ID = "quiz_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // get clicked quiz ID from quiz fragment
        Bundle b = getIntent().getExtras();
        if( null != b) {
            int quizId = b.getInt(ARG_QUIZ_ID);

            // Create fragment and give it an argument for the selected article
            Log.i("dssds", "Start from onCreate");
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle args = new Bundle();

            args.putInt(QuestionFragment.ARG_QUIZ_ID, quizId);
            questionFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.question_content, questionFragment);
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Answer a, Question q) {
        // add all answers to Hash Map
        quizAnswers.put(a.getmId(), q.getmQuizId());

        mQuizId = q.getmQuizId();

        // check if end of quiz ?
        if(null != a.getmNextQuestionId()) {

            Log.i("dssds", "Start from onFragmentInteraction");
            // Create fragment and give it an argument for the selected article
            Fragment questionFragment = QuestionFragment.newInstance(a.getmNextQuestionId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.question_content, questionFragment);
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        } else {
            // Next question is NULL, so that is quiz end. Now send results and display a message
            // to the user

            //Fragment questionFragment = new FragmentQuest
            insertResult(quizAnswers);

            //this.finish();
        }
    }

    /**
     * Insert results in Azure Mobile Service Database
     *
     * @param results
     *            The item to mark
     */
    public void insertResult(final HashMap<Integer, Integer> results) {

	    new AsyncTask<Void, Void, Void>() {

	        @Override
	        protected Void doInBackground(Void... params) {
	            try {
                    java.util.Date utilDate = new java.util.Date();

                    // get tourist or add it
                    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String touristReference = mSharedPreferences.getString(getString(R.string.preference_tourist_reference), "NA");
                    mTourist = DataHelper.getTouristByReference(touristReference);

                    // No tourist in database, save it !
                    if(null == mTourist) {
                        try {
                            Tourist t = new Tourist();
                            t.setmReference(touristReference);
                            t.setmCreationDate(new java.sql.Timestamp(utilDate.getTime()));

                            int touristGender = mSharedPreferences.getInt(getString(R.string.pref_gender_key), -1);
                            t.setmGender(touristGender);

                            String touristBirthDate = mSharedPreferences.getString(getString(R.string.pref_age_key), "1970-01-01");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
                            java.util.Date parsedDate = dateFormat.parse(touristBirthDate);
                            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                            t.setmBirthDate(timestamp);

                            mTourist = DataHelper.addTourist(t);
                        } catch (ParseException e) {
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                    }

                    // Add participation
                    Participation p = new Participation();
                    p.setmTouristId(mTourist.getmId());
                    p.setmQuizId(mQuizId);
                    p.setmCreatedAt(new java.sql.Timestamp(utilDate.getTime()));

                    // save participation
                    mParticipation = DataHelper.addParticipation(p);

                    // Add results
                    for (Map.Entry<Integer, Integer> r : results.entrySet()) {
                        Integer answerId = r.getKey();
                        Integer quizId = r.getValue();

                        Result result = new Result();
                        result.setmAnswerId(answerId);
                        result.setmParticipationId(mParticipation.getmId());

                        result.setmTouristId(mTourist.getmId());

                        // save result
                        DataHelper.addResult(result);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(QuestionActivity.ARG_RESULT_PARTICIPATION_ID, mParticipation.getmId());
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (Exception exception) {
                    Log.e(TAG, "Error: " + exception.getMessage());
	            }
	            return null;
	        }
	    }.execute();
    }
}
