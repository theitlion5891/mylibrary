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
import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
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

public class ClosedEventFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String selectedTag;// 1=Trades, 2=Opinion, 3=poll
    private RecyclerView recyclerLive;
    private SwipeRefreshLayout swipe;
    private LinearLayout nodata;
    private int limit = 100, offset = 0;
    boolean isGetData, isApiCall;
    private ArrayList<EventModel> list;
    private EventAdapter adapter;
    private Gson gson;

    public ClosedEventFragment() {
    }

    public static ClosedEventFragment newInstance(String param1) {
        ClosedEventFragment fragment = new ClosedEventFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_closed_event, container, false);
        initFragment(view);
        return view;
    }

    public void changeTags(String selectedTag) {
        this.selectedTag = selectedTag;
        LogUtil.e("resp", selectedTag);

        list.clear();
        adapter.notifyDataSetChanged();

        if (selectedTag.equalsIgnoreCase("1")) {
            //getTradingEventData();
            getTradeBoardData();
        } else if (selectedTag.equalsIgnoreCase("2")) {
            getOpinionEventData();
        } else if (selectedTag.equalsIgnoreCase("3")) {
            getPollEventData();
        }
    }

    private void getPollEventData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("TAG", "getEventData param : " + jsonObject.toString() + "   " + ApiManager.myJoinPollContestListClose);
        isApiCall = false;
        boolean isLoader = swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinPollContestListClose, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.optJSONObject(i);
                        EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                        list.add(model);
                    }

                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size() <= 0) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    adapter.updateTag(selectedTag, list);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /*list.clear();
        adapter.notifyDataSetChanged();
        if (selectedTag.equalsIgnoreCase("1")){
            getTradingEventData();
        } else if (selectedTag.equalsIgnoreCase("2")) {
            getOpinionEventData();
        }
        else if (selectedTag.equalsIgnoreCase("3")) {
            getPollEventData();
        }*/
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

        LogUtil.e("TAG", "getEventData param : " + jsonObject.toString());
        isApiCall = false;
        boolean isLoader = swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinOpinionContestListClose, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.optJSONObject(i);
                        EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                        list.add(model);
                    }

                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size() <= 0) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    adapter.updateTag(selectedTag, list);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    private void getTradeBoardData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("TAG", "getTradeBoardCloseData param : " + jsonObject.toString());
        isApiCall = false;
        boolean isLoader = swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.completedMyJoinTradeBoardContestList, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getTradeBoardCloseData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    ArrayList<EventModel> listTradeBoard = new ArrayList<>();
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.optJSONObject(i);
                            EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                            listTradeBoard.add(model);
                        }
                        EventModel eventModel = new EventModel();
                        eventModel.setConfirmTradeList(listTradeBoard);
                        eventModel.setType(0);
                        list.add(0, eventModel);
                        //adapter.notifyDataSetChanged();
                    } else {
                        LogUtil.e("EERROR", "TradeboardClose error");
                    }
                }
                getTradingEventData();
            }


            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("TradeBoardClose Failed", responseBody);
            }
        });
    }

    private void getTradingEventData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("TAG", "getEventCloseData param : " + jsonObject.toString());
        isApiCall = false;
        boolean isLoader = swipe == null || !swipe.isRefreshing();

        HttpRestClient.postJSONNormal(getActivity(), isLoader, ApiManager.myJoinTradesContestListClose, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
                isApiCall = true;
                LogUtil.e("TAG", "getEventData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.optJSONObject(i);
                        EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                        model.setType(1);
                        list.add(model);
                    }

                    if (data.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += data.length();
                        isGetData = true;
                    }

                    if (list.size() <= 0) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerLive.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.GONE);
                        recyclerLive.setVisibility(View.VISIBLE);
                    }
                    adapter.updateTag(selectedTag, list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe != null && swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    private void initFragment(View view) {
        recyclerLive = view.findViewById(R.id.recyclerLive);
        nodata = view.findViewById(R.id.nodata);
        swipe = view.findViewById(R.id.swipe);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerLive.setLayoutManager(linearLayoutManager);

        isGetData = false;
        isApiCall = false;

        gson = new Gson();

        list = new ArrayList<>();

        adapter = new EventAdapter(getActivity(), list, (model, tag) -> {
            /*startActivity(new Intent(getActivity(), EventDetailsActivity.class)
                    .putExtra("eventModel",model)
                    .putExtra("isLiveEvent",false)
            );*/
            if (tag.equalsIgnoreCase("exit")) {
                //exitApi(model);
            } else {
                if (selectedTag.equalsIgnoreCase("3")) {
                    startActivity(new Intent(getActivity(), PollDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", false)
                    );
                } else if (selectedTag.equalsIgnoreCase("2")) {
                    startActivity(new Intent(getActivity(), OpinionDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", false)
                    );
                } else {
                    startActivity(new Intent(getActivity(), EventDetailsActivity.class)
                            .putExtra("eventModel", model)
                            .putExtra("isLiveEvent", false)
                    );
                }
            }
        }, false, selectedTag);
        recyclerLive.setAdapter(adapter);

        swipe.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            if (selectedTag.equalsIgnoreCase("1")) {
                getTradeBoardData();
                //getTradingEventData();
            } else if (selectedTag.equalsIgnoreCase("2")) {
                getOpinionEventData();
            } else if (selectedTag.equalsIgnoreCase("3")) {
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

                    if (selectedTag.equalsIgnoreCase("1")) {
                        getTradeBoardData();
                        //getTradingEventData();
                    } else if (selectedTag.equalsIgnoreCase("2")) {
                        getOpinionEventData();
                    } else if (selectedTag.equalsIgnoreCase("3")) {
                        getPollEventData();
                    }
                }
            }
        });

    }
}