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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Vince on 24.08.2015.
 */
public class ResultRadarLabelAdapter extends ArrayAdapter<String> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public ResultRadarLabelAdapter(Context context, int layoutResourceId) {
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


        final String value = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(value);
        final TextView textView = (TextView) row.findViewById(R.id.row_label_name);
        textView.setText(value);


        return row;
    }
}