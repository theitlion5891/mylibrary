package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;

public class PrizePoolFragment extends BaseFragment {

    ContestModel.ContestDatum model;
    RecyclerView match_rank;
    MatchRankAdapter matchRank;
    String winning_tree;
    ImageView imgPlace;
    TextView txtPlace;
    LinearLayout layNoData,layHeader;

    public PrizePoolFragment() {
    }

    public static PrizePoolFragment newInstance(ContestModel.ContestDatum model) {
        PrizePoolFragment fragment = new PrizePoolFragment();
        Bundle args = new Bundle();
        args.putSerializable("contest_model", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            model=(ContestModel.ContestDatum) getArguments().getSerializable("contest_model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prize_pool, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        match_rank = view.findViewById(R.id.match_rank);
        layNoData = view.findViewById(R.id.layNoData);
        layHeader = view.findViewById(R.id.layHeader);
        imgPlace = view.findViewById(R.id.imgPlace);
        txtPlace = view.findViewById(R.id.txtPlace);

        winning_tree = model.getWinningTree();

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(winning_tree);
            LogUtil.e("resp","Tree: "+jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        matchRank = new MatchRankAdapter(mContext, jsonArray);
        match_rank.setAdapter(matchRank);
        match_rank.setLayoutManager(new LinearLayoutManager(mContext));

        checkData();
    }

    private void checkData(){
        //LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        if (model.getIsUnlimited().equalsIgnoreCase("Yes")){
            switch (preferences.getMatchModel().getSportId()+""){
                case "1":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                    break;
                case "2":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                    break;
                case "3":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                    break;
                case "4":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                    break;
                case "5":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                    break;
                case "6":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);
                    break;
                case "7":
                    layNoData.setVisibility(View.VISIBLE);
                    match_rank.setVisibility(View.GONE);
                    layHeader.setVisibility(View.GONE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);
                    break;
            }
        }
    }

    @Override
    public void initClick() {

    }
}