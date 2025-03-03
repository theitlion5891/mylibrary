package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Adapter.NotificationAdapter;
import com.fantafeat.Model.NotificationModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends BaseFragment {

    RecyclerView notification_list;
    int offset, limit;
    RelativeLayout progressBar;
    LinearLayoutManager mLinearLayoutManager;
    boolean isGetData, isApiCall;
    List<NotificationModel> notificationModelList = new ArrayList<>();
    NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        HideBottomNavigationBar();
        initFragment(view);
        initToolBar(view, "Notifications", true);
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setStatusBarDark();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShowBottomNavigationBar();
    }
    @Override
    public void initControl(View view) {
        mLinearLayoutManager = new LinearLayoutManager(mContext);

        limit = 25;
        offset = 0;
        isGetData = false;
        notification_list = view.findViewById(R.id.notification_list);
        progressBar = view.findViewById(R.id.notification_level);
    }

    @Override
    public void initClick() {
        notification_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (mLinearLayoutManager != null &&
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == notificationModelList.size() - 1
                        && !isGetData) {
                    //Load more online data
                    LogUtil.e(TAG, "on Scroll");
                    if (notificationModelList.size() > 0) {
                        progressBar.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });
    }

    public void getData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.NOTIFICATION_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                progressBar.setVisibility(View.GONE);
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                JSONArray array = responseBody.optJSONArray("data");
                int i = 0;
                if (limit >= array.length()) {
                    isGetData = true;
                }
                for (i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    NotificationModel notificationModel = gson.fromJson(object.toString(), NotificationModel.class);
                    notificationModelList.add(notificationModel);
                }
                if (offset == 0) {
                    adapter = new NotificationAdapter(mContext, notificationModelList);
                    notification_list.setLayoutManager(mLinearLayoutManager);
                    notification_list.setAdapter(adapter);

                } else {
                    if (adapter != null) {
                        adapter.updateData(notificationModelList);
                    } else {
                        adapter = new NotificationAdapter(mContext, notificationModelList);
                        notification_list.setLayoutManager(mLinearLayoutManager);
                        notification_list.setAdapter(adapter);
                    }
                }
                offset += 25;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }
}