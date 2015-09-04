package com.dsv.tourism.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabResultFragment extends Fragment {

    // the fragment initialization parameter
    private static final String ARG_PARTICIPATION_ID = "com.dsv.PARTICIPATION_ID";
    private int mParticipationId;
    private HashMap<String, Integer> mResults;
    private RadarChart mChart;
    private Typeface tf;
    private ArrayList<String> xVals;
    private OnFragmentInteractionListener mListener;
    private CircularProgressView circularProgressView;

    /**
     * Used to identify class when logging
     */
    private static final String TAG = TabResultFragment.class.getName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param participationId The participation id.
     * @return A new instance of fragment TabResultFragment.
     */
    public static TabResultFragment newInstance(int participationId) {
        TabResultFragment fragment = new TabResultFragment();

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

    public TabResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParticipationId = getArguments().getInt(ARG_PARTICIPATION_ID);
        }

        DataHelper.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_result, container, false);

        // get the progress view indicator and set it as visible
        circularProgressView = (CircularProgressView) v.findViewById(R.id.progress_view_result);
        circularProgressView.setVisibility(View.VISIBLE);

        mChart = (RadarChart) v.findViewById(R.id.result_radar);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        mChart.setDescription("");
        mChart.setWebLineWidth(1.5f);
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebAlpha(100);
        mChart.setClickable(true);
        mChart.setRotationEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null) {
                    return;
                }

                Toast.makeText(getActivity(), xVals.get(e.getXIndex()) + " : " + e.getVal(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });

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

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTabResultFragmentInteraction(uri);
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
        public void onTabResultFragmentInteraction(Uri uri);
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

                    getActivity().runOnUiThread(new Runnable() {
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
