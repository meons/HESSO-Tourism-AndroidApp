package com.dsv.tourism.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dsv.tourism.R;
import com.dsv.tourism.adapter.OfficeAdapter;
import com.dsv.tourism.azure.DataHelper;
import com.dsv.tourism.model.Office;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfficeOLDFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfficeOLDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfficeOLDFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;

    /**
     * Adapter to sync the items list with the view
     */
    private OfficeAdapter mAdapter;

    private static final String TAG = OfficeOLDFragment.class.getName();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfficeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfficeOLDFragment newInstance(String param1, String param2) {
        OfficeOLDFragment fragment = new OfficeOLDFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OfficeOLDFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_office_old, container, false);

        // init azure service
        DataHelper.init(rootView.getContext());

        // Create an adapter to bind the items with the view
        mAdapter = new OfficeAdapter(rootView.getContext(), R.layout.row_list_office);
        ListView listViewOffice = (ListView) rootView.findViewById(R.id.listViewOffice);
        listViewOffice.setAdapter(mAdapter);
        listViewOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onOfficeListClicked(position);
            }
        });

        // refresh office list
        refreshOfficeListFromTable();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onOfficeListClicked(int position) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position);
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
        public void onFragmentInteraction(int position);
    }


    /**
     * Refresh the list with the items in the Mobile Service Table
     */
    private void refreshOfficeListFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MobileServiceList<Office> result = DataHelper.getOffices();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (Office o : result) {
                                mAdapter.add(o);
                            }
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
