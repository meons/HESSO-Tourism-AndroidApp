package com.dsv.tourism.azure;


import android.content.Context;
import android.util.Log;

import com.dsv.tourism.model.Answer;
import com.dsv.tourism.model.Category;
import com.dsv.tourism.model.Office;
import com.dsv.tourism.model.Participation;
import com.dsv.tourism.model.Question;
import com.dsv.tourism.model.Quiz;
import com.dsv.tourism.model.Recommendation;
import com.dsv.tourism.model.RecommendationCriteria;
import com.dsv.tourism.model.Result;
import com.dsv.tourism.model.Tourist;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * Save a new tourist object
     *
     * @param t The tourist
     * @return the inserted tourist object
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static Tourist addTourist(Tourist t) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Tourist> mTouristTable  = mClient.getTable("tourist", Tourist.class);
        return mTouristTable.insert(t).get();
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

        if(null == tourists || tourists.isEmpty()) {
            return null;
        }

        Tourist t = tourists.get(0);

        MobileServiceTable<Participation> mParticipationTable = mClient.getTable("participation", Participation.class);
        MobileServiceList<Participation> participation = mParticipationTable.where().field("tourist_id").eq(t.getmId()).orderBy("created_at", QueryOrder.Descending).execute().get();

        ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
        MobileServiceTable<Quiz> mQuizTable = mClient.getTable("quiz", Quiz.class);
        for (Participation p : participation) {
            MobileServiceList<Quiz> quiz = mQuizTable.where().field("id").eq(p.getmQuizId()).top(1).execute().get();
            Quiz q = quiz.get(0);
            q.setmAnsweredDate(new Date(p.getmCreatedAt().getTime()));
            q.setmParticipationId(p.getmId());

            quizzes.add(q);
        }

        return quizzes;
    }

    /**
     * Get all recommendations for a participation
     *
     * @param participationId
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ArrayList<Recommendation> getRecommendationsByParticipationId(int participationId) throws MobileServiceException, ExecutionException, InterruptedException {

        // calculate answers score for a category
        MobileServiceTable<Result> mResultTable = mClient.getTable("result", Result.class);
        MobileServiceList<Result> results = mResultTable.where().field("participation_id").eq(participationId).execute().get();
        HashMap<Integer, Integer> scores = new HashMap<>();

        int score = 0;

        MobileServiceTable<Answer> mAnswerTable = mClient.getTable("answer", Answer.class);
        MobileServiceTable<Question> mQuestionTable = mClient.getTable("question", Question.class);
        MobileServiceTable<Category> mCategoryTable = mClient.getTable("category", Category.class);


        for (Result r : results) {
            MobileServiceList<Answer> answers = mAnswerTable.where().field("id").eq(r.getmAnswerId()).top(1).execute().get();
            Answer a = answers.get(0);

            MobileServiceList<Question> questions = mQuestionTable.where().field("id").eq(a.getmQuestionId()).top(1).execute().get();
            Question q = questions.get(0);

            MobileServiceList<Category> categories = mCategoryTable.where().field("id").eq(q.getmCategoryId()).top(1).execute().get();
            Category c = categories.get(0);

            score = a.getmScore();

            if (scores.get(c.getmId()) == null) {
                scores.put(c.getmId(), 0);
            }

            scores.put(c.getmId(), scores.get(c.getmId()) + score);
        }

        Log.i("sdsd", "Score list size is : " + scores.size());
        /*
            scores = scores par catégories

            cat | score
            -----------
            2   |   6
            5   |   5
            9   |   8

         */

        ArrayList<Recommendation> recommendationsList = new ArrayList<Recommendation>();

        MobileServiceTable<Recommendation> mRecommendationTable = mClient.getTable("recommendation", Recommendation.class);
        MobileServiceTable<RecommendationCriteria> mRecommendationCriteriaTable = mClient.getTable("recommendation_criteria", RecommendationCriteria.class);
        for(Map.Entry<Integer, Integer> entry : scores.entrySet()) {
            Integer categoriId = entry.getKey();
            Integer scoreValue = entry.getValue();


            MobileServiceList<Recommendation> recommendations = mRecommendationTable.where().field("category_id").eq(categoriId).top(1).execute().get();

            if(!recommendations.isEmpty()) {
                Recommendation r = recommendations.get(0);
                MobileServiceList<RecommendationCriteria> recommendationCriterias = mRecommendationCriteriaTable.where().field("recommendation_id").eq(r.getmId())
                        .and().field("threshold_max").gt(scoreValue)
                        .and().field("threshold_min").le(scoreValue)
                        .execute().get();

                RecommendationCriteria critera = recommendationCriterias.get(0);

                r.setmRecommendationCriteriaMessage(critera.getmMessage());
                recommendationsList.add(r);
            }
        }

        return recommendationsList;


        /*
        // get the participation
        MobileServiceTable<Participation> mParticipationTable = mClient.getTable("participation", Participation.class);
        MobileServiceList<Participation> participation = mParticipationTable.where().field("id").eq(participationId).top(1).execute().get();
        Participation p = participation.get(0);

        MobileServiceTable<Recommendation> mRecommendationTable = mClient.getTable("recommendation", Recommendation.class);
        MobileServiceList<Recommendation> recommendations = mRecommendationTable.where().field("quiz_id").eq(p.getmQuizId()).execute().get();

        return recommendations;
        */

    }

    /**
     * Get all results for a participation
     *
     * @param participationId
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static HashMap<String, Integer> getResultByParticipationId(int participationId) throws MobileServiceException, ExecutionException, InterruptedException {

        // get the participation
        /*
        MobileServiceTable<Participation> mParticipationTable = mClient.getTable("participation", Participation.class);
        MobileServiceList<Participation> participation = mParticipationTable.where().field("id").eq(participationId).top(1).execute().get();
        Participation p = participation.get(0);
        */

        MobileServiceTable<Result> mResultTable = mClient.getTable("result", Result.class);
        MobileServiceList<Result> results = mResultTable.where().field("participation_id").eq(participationId).execute().get();
        HashMap<String, Integer> scores = new HashMap<>();

        int score = 0;

        MobileServiceTable<Answer> mAnswerTable = mClient.getTable("answer", Answer.class);
        MobileServiceTable<Question> mQuestionTable = mClient.getTable("question", Question.class);
        MobileServiceTable<Category> mCategoryTable = mClient.getTable("category", Category.class);


        for (Result r : results) {
            MobileServiceList<Answer> answers = mAnswerTable.where().field("id").eq(r.getmAnswerId()).top(1).execute().get();
            Answer a = answers.get(0);

            MobileServiceList<Question> questions = mQuestionTable.where().field("id").eq(a.getmQuestionId()).top(1).execute().get();
            Question q = questions.get(0);

            MobileServiceList<Category> categories = mCategoryTable.where().field("id").eq(q.getmCategoryId()).top(1).execute().get();
            Category c = categories.get(0);

            score = a.getmScore();

            if (scores.get(c.getmName()) == null) {
                scores.put(c.getmName(), 0);
            }

            scores.put(c.getmName(), scores.get(c.getmName()) + score);
        }

        return scores;
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

    /**
     * Get questions for a quiz by his id
     *
     * @param id
     * @return
     * @throws MobileServiceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    public static Participation addParticipation(Participation p) throws MobileServiceException, ExecutionException, InterruptedException {
        MobileServiceTable<Participation> mResultTable  = mClient.getTable("participation", Participation.class);

        return mResultTable.insert(p).get();
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
