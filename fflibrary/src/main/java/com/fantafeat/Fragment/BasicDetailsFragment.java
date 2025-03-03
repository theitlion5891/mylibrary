package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;

public class BasicDetailsFragment extends BaseFragment {

    public BasicDetailsFragment() {

    }

    @Override
    public void initControl(View view) {

    }

    @Override
    public void initClick() {

    }


    public static BasicDetailsFragment newInstance(String param1, String param2) {
        BasicDetailsFragment fragment = new BasicDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_details, container, false);
        return view;
    }


}