package com.dsv.tourism.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dsv.tourism.R;
import com.dsv.tourism.model.Quiz;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vince on 24.08.2015.
 */
public class AnsweredQuizAdapter extends ArrayAdapter<Quiz> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public AnsweredQuizAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        final Quiz quiz = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(quiz);
        final TextView textView = (TextView) row.findViewById(R.id.row_quiz_name);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.FRANCE);

        final TextView textViewDate = (TextView) row.findViewById(R.id.row_quiz_answered_date);
        textView.setText(quiz.getmName());
        textViewDate.setText(sdf.format(quiz.getmAnsweredDate()));

        return row;
    }
}