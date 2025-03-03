package com.fantafeat.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Adapter.CommenteryAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.OnFragmentInteractionListener;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CommentaryFragment extends BaseFragment {

    private RecyclerView recyclerCommentary;
    private CommenteryAdapter adapter;
    private List<ScoreModel.Commentary> list;
    private SwipeRefreshLayout swipCom;
    //private AfterMatchActivity afterMatchActivity;
    private LinearLayout layNoData;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public CommentaryFragment() {
        //this.afterMatchActivity=afterMatchActivity;AfterMatchActivity afterMatchActivity
    }


    public static CommentaryFragment newInstance() {
        return new CommentaryFragment();//afterMatchActivity
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_commentary, container, false);
        initFragment(view);
        //setComData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setComData();
    }

    @Override
    public void initControl(View view) {
        recyclerCommentary=view.findViewById(R.id.recyclerCommentary);
        swipCom=view.findViewById(R.id.swipCom);

        layNoData=view.findViewById(R.id.layNoData);
    }

    public void setComData(){
        if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
            if (ConstantUtil.score_list!=null && ConstantUtil.score_list.size()>0){
                list=new ArrayList<>();
                for (int i=0;i< ConstantUtil.score_list.size();i++){
                    List<ScoreModel.Commentary> commentary = ConstantUtil.score_list.get(i).getCommentaries();

                    list.addAll(commentary);
                }

                Collections.reverse(list);

                LinearLayoutManager manager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
                recyclerCommentary.setLayoutManager(manager);
                adapter=new CommenteryAdapter(mContext,list,"commentary");
                recyclerCommentary.setAdapter(adapter);

                checkData(true);

            }else {
                getScoreCardCom();
            }
        }else {
            checkData(false);
        }
    }

    public void getScoreCardCom() {
        list=new ArrayList<>();
        ConstantUtil.score_list=new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e(TAG, "onSuccessResult param: " + jsonObject.toString() );

        boolean isLoader=true;
        if (swipCom.isRefreshing()){
            isLoader=false;
        }

        HttpRestClient.postJSONWithParam(mContext, isLoader, ApiManager.SCORE_CARD, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    swipCom.setRefreshing(false);
                    LogUtil.e(TAG, "onSuccessResult Score: " + responseBody.toString() );
                    if(responseBody.optBoolean("status")){

                        JSONObject data=responseBody.optJSONObject("data");
                        if (data!=null && data.has("innings")){
                            JSONArray innings=data.optJSONArray("innings");
                            if (innings!=null && innings.length()>0){
                                ConstantUtil.score_list.clear();

                                // preferences.setScore(scoreModelList);
                                for (int i=0;i<innings.length();i++){
                                    ScoreModel modal= gson.fromJson(innings.optJSONObject(i).toString(),ScoreModel.class);
                                    ConstantUtil.score_list.add(modal);
                                }

                                //afterMatchActivity.updateMainScore(ConstantUtil.score_list);
                                //updateMainScore(ConstantUtil.score_list);
                                if (mListener!=null){
                                    mListener.onFragmentAction(ConstantUtil.score_list);
                                }

                                checkData(true);
                            }else {
                                checkData(false);
                            }
                        }else {
                            checkData(false);
                        }
                    }else {
                        checkData(false);
                    }
                } catch (JsonSyntaxException e) {
                    checkData(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                swipCom.setRefreshing(false);
            }
        });
    }

    private void updateScoreApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id",preferences.getMatchModel().getId());
            jsonObject.put("user_id",preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.GET_MATCH_SCORE, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "matchScore: " + responseBody.toString() );
                if(responseBody.optBoolean("status")){
                    MatchModel matchModel = preferences.getMatchModel();
                    JSONObject data = responseBody.optJSONObject("data");
                    matchModel.setTeam1Inn1Score(data.optString("team_1_inn_1_score"));
                    matchModel.setTeam1Inn2Score(data.optString("team_1_inn_2_score"));
                    matchModel.setTeam2Inn1Score(data.optString("team_2_inn_1_score"));
                    matchModel.setTeam2Inn2Score(data.optString("team_2_inn_2_score"));
                    MyApp.getMyPreferences().setMatchModel(matchModel);
                    //((AfterMatchActivity)mContext).updateScore();

                    //updateScore();

                   /* if (preferences.getMatchModel().getMatchType().equalsIgnoreCase("test")) {
                        inning2_score_team1.setVisibility(View.VISIBLE);
                        inning2_score_team2.setVisibility(View.VISIBLE);

                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());
                    } else {
                        inning1_score_team1.setText(preferences.getMatchModel().getTeam1Inn1Score());
                        inning2_score_team1.setText(preferences.getMatchModel().getTeam1Inn2Score());
                        inning1_score_team2.setText(preferences.getMatchModel().getTeam2Inn1Score());
                        inning2_score_team2.setText(preferences.getMatchModel().getTeam2Inn2Score());

                        inning2_score_layout.setVisibility(View.GONE);
                        inning4_score_layout.setVisibility(View.GONE);
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkData(boolean isData){
        if (isData) {

            recyclerCommentary.setVisibility(View.VISIBLE);
            layNoData.setVisibility(View.GONE);

        }else {
            recyclerCommentary.setVisibility(View.GONE);
            layNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initClick() {
        swipCom.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ( preferences.getUpdateMaster().getIs_score_card_refresh().equalsIgnoreCase("no")){
                    list=new ArrayList<>();
                    ConstantUtil.score_list=new ArrayList<>();
                    getScoreCardCom();
                }else {
                    swipCom.setRefreshing(false);
                }

                /**/
            }
        });
    }
}