package com.dsv.tourism.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsv.tourism.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class NoDataFragment extends Fragment {

    private CircularProgressView circularProgressView;


    public NoDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_no_data, container, false);


        // get the progress view indicator and set it as visible
        circularProgressView = (CircularProgressView) getActivity().findViewById(R.id.progress_view);
        circularProgressView.setVisibility(View.GONE);

        return v;
    }

}
