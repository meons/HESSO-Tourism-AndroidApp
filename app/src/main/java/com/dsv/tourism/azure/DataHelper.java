package com.dsv.tourism.azure;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.OfficeAdapter;
import com.dsv.tourism.model.Answer;
import com.dsv.tourism.model.Office;
import com.dsv.tourism.model.Participation;
import com.dsv.tourism.model.Question;
import com.dsv.tourism.model.Quiz;
import com.dsv.tourism.model.Result;
import com.dsv.tourism.model.Tourist;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.Query;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by Vince on 25.08.2015.
 */
public class DataHelper {

    private static AzureServiceHelper mAzureServiceHelper = null;

    /**
     * Microsoft Azure mobile service client
     */
    private static MobileServiceClient mClient;

    private DataHelper() {}

    public static void init(Context context) {

        if (mAzureServiceHelper == null){
            mAzureServiceHelper = new AzureServiceHelper(context);
        }
    }

    /**
     * Get all tourist offices
     *
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static MobileServiceList<Office> getOffices() throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Office> mOfficeTable = mClient.getTable("office", Office.class);
        MobileServiceList<Office> result = null;
            //final MobileServiceList<Office> result = mOfficeTable.where().field("complete").eq(false).execute().get();
            result = mOfficeTable.execute().get();

        return result;
    }

    /**
     * Get a tourist by his reference
     *
     * @param reference
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static Tourist getTouristByReference(String reference) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Tourist> mTouristTable = mClient.getTable("tourist", Tourist.class);
        MobileServiceList<Tourist> tourists = mTouristTable.where().field("reference").eq(reference).top(1).execute().get();

        if(null == tourists || tourists.isEmpty()) {
            return null;
        }

        return tourists.get(0);
    }

    public static void addTourist(Tourist t) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Tourist> mTouristTable  = mClient.getTable("tourist", Tourist.class);
        mTouristTable.insert(t).get();
    }

    /**
     * Get a list of quizzes by tourist reference
     *
     * @param reference
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ArrayList<Quiz> getQuizzesByTouristReference(String reference) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Tourist> mTouristTable = mClient.getTable("tourist", Tourist.class);
        MobileServiceList<Tourist> tourists = mTouristTable.where().field("reference").eq(reference).top(1).execute().get();
        Tourist t = tourists.get(0);

        MobileServiceTable<Participation> mParticipationTable = mClient.getTable("participation", Participation.class);
        MobileServiceList<Participation> participation = mParticipationTable.where().field("tourist_id").eq(t.getmId()).execute().get();

        ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
        MobileServiceTable<Quiz> mQuizTable = mClient.getTable("quiz", Quiz.class);
        for (Participation p : participation) {
            MobileServiceList<Quiz> quiz = mQuizTable.where().field("id").eq(p.getmQuizId()).top(1).execute().get();
            quizzes.add(quiz.get(0));
        }

        return quizzes;
    }

    /**
     * Get all quizzes from an office
     *
     * @param id
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static MobileServiceList<Quiz> getQuizzesByOfficeId(Integer id) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Quiz> mOfficeTable = mClient.getTable("quiz", Quiz.class);
        MobileServiceList<Quiz> quizzes = mOfficeTable.where().field("office_id").eq(id).execute().get();

        return quizzes;
    }

    public static Question getQuestionByQuizId(Integer id) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Question> mOfficeTable = mClient.getTable("question", Question.class);
        MobileServiceList<Question> questions = mOfficeTable.where().field("quiz_id").eq(id).top(1).execute().get();

        return questions.get(0);
    }

    public static Question getQuestionById(Integer id) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Question> mOfficeTable = mClient.getTable("question", Question.class);
        MobileServiceList<Question> questions = mOfficeTable.where().field("id").eq(id).top(1).execute().get();

        return questions.get(0);
    }

    public static void addParticipation(Participation p) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Participation> mResultTable  = mClient.getTable("participation", Participation.class);
        mResultTable.insert(p).get();
    }

    public static void addResult(Result r) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Result> mResultTable  = mClient.getTable("result", Result.class);
        mResultTable.insert(r).get();
    }

    /**
     * Get all quizzes from an office
     *
     * @param id
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static MobileServiceList<Answer> getAnswersByQuestionId(Integer id) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Answer> mOfficeTable = mClient.getTable("answer", Answer.class);
        MobileServiceList<Answer> answers = mOfficeTable.where().field("question_id").eq(id).execute().get();
        //result = mOfficeTable.execute().get();

        return answers;
    }

    private static class AzureServiceHelper {
        private Context context;

        public AzureServiceHelper(Context context) {
            this.context = context;
            this.getClient();
        }

        private MobileServiceClient getClient() {
            try {
                mClient = new MobileServiceClient(
                        "https://mobile-tourism.azure-mobile.net/",
                        "zebzFMYsdHnqQwVokGWaemXzWXSwFR40",
                        context
                );

                return mClient;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
