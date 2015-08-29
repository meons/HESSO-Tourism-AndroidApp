package com.dsv.tourism.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dsv.tourism.R;
import com.dsv.tourism.listeners.OnTextChangeListener;
import com.dsv.tourism.ui.CustomViewPager;

import java.text.SimpleDateFormat;


public class TutorialStepFourFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tutorial_step_four, container, false);


        return rootView;
    }
}
