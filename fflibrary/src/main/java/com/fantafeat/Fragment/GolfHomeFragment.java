package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;

public class GolfHomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_golf_home, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {

    }

    @Override
    public void initClick() {

    }
}