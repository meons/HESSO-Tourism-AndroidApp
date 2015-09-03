package com.dsv.tourism.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.azure.DataHelper;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private int mParticipationId;
    private HashMap<String, Integer> mResults;

    private RadarChart mChart;
    private Typeface tf;

    private ArrayList<String> xVals;


    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    private CircularProgressView circularProgressView;

    /**
     * The argument passed to this fragments. Can be call from it's fragment activity,
     * so must be public. Used to have always the good argument name
     */
    public final static String ARG_PARTICIPATION_ID = "participation_id";


    /**
     * Used to identify class when logging
     */
    private static final String TAG = ResultActivity.class.getName();

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

        // get the progress view indicator and set it as visible
        circularProgressView = (CircularProgressView) this.findViewById(R.id.progress_view);
        circularProgressView.setVisibility(View.GONE);


        // get clicked quiz ID from answered quiz fragment
        //Intent i = new Intent();
        Bundle b = getIntent().getExtras();
        if( null != b) {
            mParticipationId = b.getInt(ARG_PARTICIPATION_ID);
        }


        mChart = (RadarChart) findViewById(R.id.result_radar);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setDescription("");

        mChart.setWebLineWidth(1.5f);
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebAlpha(100);

        mChart.setClickable(true);


        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                                + ", DataSet index: " + dataSetIndex);

                Toast.makeText(getApplicationContext(), xVals.get(e.getXIndex()) + " : " + e.getVal(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });


        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        //mChart.setMarkerView(mv);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setTextSize(12f);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawLabels(false);
        xAxis.setEnabled(false);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(tf);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setStartAtZero(true);




        getResultsFromTable();

        /*
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

        */
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

    public void setData() {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        xVals = new ArrayList<String>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        int i = 0;
        for(Map.Entry<String, Integer> entry : mResults.entrySet()) {
            String categoryName = entry.getKey();
            Integer categoryScore = entry.getValue();

            // do what you have to do here
            // In your case, an other loop.
            yVals1.add(new Entry(categoryScore, i));
            xVals.add(categoryName);

            i++;
        }

        RadarDataSet set1 = new RadarDataSet(yVals1, getString(R.string.radar_data_set_name));

        set1.setColor(Color.rgb(255, 152, 0));
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);


        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(false);


        circularProgressView.setVisibility(View.VISIBLE);
        mChart.setData(data);


        //mChart.invalidate();
        //mChart.animateXY(3000, 3000);
        mChart.invalidate();
    }


    /**
     * Refresh the list with the items in the Mobile Service Table
     */
    private void getResultsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mResults = DataHelper.getResultByParticipationId(mParticipationId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();

                            // hide circular progress view
                            circularProgressView.setVisibility(View.GONE);
                            circularProgressView.resetAnimation();
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
