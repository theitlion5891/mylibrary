package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fantafeat.Activity.EventDetailsActivity;
import com.fantafeat.Activity.OpinionDetailsActivity;
import com.fantafeat.Activity.PollDetailsActivity;
import com.fantafeat.Adapter.EventAdapter;
import com.fantafeat.Adapter.StocksAdapter;
import com.fantafeat.Adapter.StocksLiveMatchAdapter;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.StocksLiveMatchModel;
import com.fantafeat.Model.StocksModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveEventFragment extends BaseFragment {

    public LiveEventFragment() {}
    private static final String ARG_PARAM1 = "param1";

    private String selectedTag;//1=Trades, 2=Opinion, 3=poll
    private RecyclerView recyclerLive,recyclerTradeBoard;
    private SwipeRefreshLayout swipe;
    private LinearLayout nodata;
    private int limit=100,offset=0;
    boolean isGetData, isApiCall;
    private ArrayList<EventModel> list;
    private EventAdapter adapter;
    private Gson gson;
    public static LiveEventFragment newInstance(String param1) {
        LiveEventFragment fragment = new LiveEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedTag = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_live_event, container, false);
        initFragment(view);
        return view;

    }

    public void changeTags(String selectedTag){
        this.selectedTag=selectedTag;
        LogUtil.e("resp",selectedTag);

        list.clear();
        adapter.notifyDataSetChanged();

        if (selectedTag.equalsIgnoreCase("1")){
           // getTradeEventData();
            getTradeBoardData();
        }else if (selectedTag.equalsIgnoreCase("2")) {
            getOpinionEventData();
        } else if (selectedTag.equalsIgnoreCase("3")) {
            getPollEventData();
        }
        /*else if (selectedTag.equalsIgnoreCase("4")){
            //getStocksLiveMatchData();
        }*/
    }

    private void getPollEventData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
            LogUtil.e("TAG", "getPollEventData : " + jsonObject +"  url: "+ ApiManager.myJoinPollContestList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isApiCall = false;

        boolean isLoader= swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinPollContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getPollEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj=data.optJSONObject(i);
                        EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                        list.add(model);
                    }

                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size()<=0){
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    }else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    adapter.updateTag(selectedTag);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void initControl(View view) {
        recyclerLive=view.findViewById(R.id.recyclerLive);
        recyclerTradeBoard=view.findViewById(R.id.recyclerTradeBoard);
        nodata=view.findViewById(R.id.nodata);
        swipe=view.findViewById(R.id.swipe);

        isGetData = false;
        isApiCall = false;

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerLive.setLayoutManager(linearLayoutManager);


        gson=new Gson();

        list=new ArrayList<>();

        adapter=new EventAdapter(getActivity(),list, (model,tag) -> {
            if (tag.equalsIgnoreCase("exit")){
                //exitApi(model);
            }else {
                if (selectedTag.equalsIgnoreCase("3")){
                    startActivity(new Intent(getActivity(), PollDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", true)
                    );
                }
                else if (selectedTag.equalsIgnoreCase("2")){
                    startActivity(new Intent(getActivity(), OpinionDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", true)
                    );
                }
                else if (selectedTag.equalsIgnoreCase("1")) {
                    startActivity(new Intent(getActivity(), EventDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", true)
                    );
                }
            }
        },true,selectedTag);
        recyclerLive.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            if (selectedTag.equalsIgnoreCase("1")){
                //getTradeEventData();
                getTradeBoardData();
            }
            else if (selectedTag.equalsIgnoreCase("2")) {
                getOpinionEventData();
            }
            else if (selectedTag.equalsIgnoreCase("3")) {
                getPollEventData();
            }
        });


        recyclerLive.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1
                        && isGetData && isApiCall) {

                    if (selectedTag.equalsIgnoreCase("1")){
                        //getTradeEventData();
                        getTradeBoardData();
                    }else if (selectedTag.equalsIgnoreCase("2")) {
                        getOpinionEventData();
                    } else if (selectedTag.equalsIgnoreCase("3")) {
                        getPollEventData();
                    }
                }
            }
        });
        //getStocksLiveMatchData();
        if (selectedTag.equalsIgnoreCase("1")){
            getTradeBoardData();
            //getTradeBoardData();
        }else if (selectedTag.equalsIgnoreCase("2")) {
            getOpinionEventData();
        } else if (selectedTag.equalsIgnoreCase("3")) {
            getPollEventData();
        }/*else if (selectedTag.equalsIgnoreCase("4")){
            getStocksLiveMatchData();
        }*/

    }

    @Override
    public void initClick() {

    }


    private void exitApi(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isLoader=true;

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.MY_OPINION_CONTEST_LIST_EXIT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e("TAG", "getEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /*list.clear();
        if (selectedTag.equalsIgnoreCase("1")){
            getTradeEventData();
        }else if (selectedTag.equalsIgnoreCase("2")) {
            getOpinionEventData();
        } else if (selectedTag.equalsIgnoreCase("3")) {
            getPollEventData();
        }*/
    }

    private void getStocksLiveMatchData(){
        StocksLiveMatchModel[] stockList = new StocksLiveMatchModel[]{
                new StocksLiveMatchModel("Gujarat"),
                new StocksLiveMatchModel("Delhi"),
                new StocksLiveMatchModel("Mumbai"),
                new StocksLiveMatchModel("Chennai")
        };

        StocksLiveMatchAdapter adapter = new StocksLiveMatchAdapter(mContext,stockList,selectedTag);
        recyclerLive.setHasFixedSize(true);
        recyclerLive.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerLive.setAdapter(adapter);
        if (stockList.length>=0){
            nodata.setVisibility(View.GONE);
            recyclerLive.setVisibility(View.VISIBLE);
        }else {
            nodata.setVisibility(View.VISIBLE);
            recyclerLive.setVisibility(View.GONE);
        }

        adapter.updateTag(selectedTag);
        adapter.notifyDataSetChanged();
    }

    private void getTradeBoardData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        }catch (JSONException e){
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, false, ApiManager.liveMyJoinTradeBoardContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "getTradeBoardData : " + responseBody.toString()+" \n"+ApiManager.myJoinTradesContestListLive);
                if (responseBody.optBoolean("status")){
                    list.clear();
                    JSONArray data=responseBody.optJSONArray("data");
                    ArrayList<EventModel> listTradeBoard=new ArrayList<>();
                    if (data!=null && data.length()>0){
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.optJSONObject(i);
                            EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                            listTradeBoard.add(model);
                        }
                        EventModel eventModel = new EventModel();
                        eventModel.setConfirmTradeList(listTradeBoard);
                        eventModel.setType(0);
                        list.add(0,eventModel);
                        //adapter.notifyDataSetChanged();
                    }else {
                        LogUtil.e("EERROR","Tradeboard error");
                    }

                }
                getTradeEventData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("TRADEBOARD FAILED",responseBody);
            }
        });
    }

    private void getTradeEventData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isApiCall = false;

        boolean isLoader= swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinTradesContestListLive, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getEventDatalivee : " + responseBody.toString()+" \n"+ApiManager.myJoinTradesContestListLive);
                if (responseBody.optBoolean("status")) {
                    LogUtil.e("st123","090");
                    JSONArray data=responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj=data.optJSONObject(i);
                        EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                        model.setType(1);

                        list.add(model);
                    }
                    LogUtil.e("asd234",data.length());
                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size()<=0){
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    }else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    //adapter.updateTag(selectedTag);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    private void getOpinionEventData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isApiCall = false;

        boolean isLoader= swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinOpinionContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getPredictionEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj=data.optJSONObject(i);
                        EventModel model=gson.fromJson(obj.toString(),EventModel.class);
                        list.add(model);
                    }

                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size()<=0){
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    }else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    adapter.updateTag(selectedTag);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
            }
        });
    }
}