package com.fantafeat.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;


public class VollyballHomeFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String sport_id="",linkData="";

    private NestedScrollView scroll;

    public VollyballHomeFragment() {
      //  this.sport_id = sport_id;
    }

    public static VollyballHomeFragment newInstance(String id,String linkData) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("linkData", linkData);
        VollyballHomeFragment f = new VollyballHomeFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sport_id = getArguments().getString("id");
            linkData = getArguments().getString("linkData");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vollyball_home, container, false);
    }

    @Override
    public void initControl(View view) {

    }

    @Override
    public void initClick() {

    }
}