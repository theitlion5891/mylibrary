package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.TransactionListAdapter;
import com.fantafeat.Adapter.WithdrawListAdapter;
import com.fantafeat.Model.TransactionModel;
import com.fantafeat.Model.WithdrawListModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WithdrawListActivity extends BaseActivity {

    private ImageView toolbar_back;
    private TextView toolbar_title,txtNoData;
    private SwipeRefreshLayout pull_transaction;
    private int offset, limit;
    private boolean isApiCall, isGetData;
    private LinearLayout scroll_loading;
    private RecyclerView transaction_full_list;
    private ArrayList<WithdrawListModel> list;
    private WithdrawListAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_list);

        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Withdraw History");
        toolbar_back=findViewById(R.id.toolbar_back);
        pull_transaction=findViewById(R.id.pull_transaction);
        scroll_loading=findViewById(R.id.scroll_loading);
        transaction_full_list=findViewById(R.id.transaction_full_list);
        txtNoData=findViewById(R.id.txtNoData);

        list=new ArrayList();

        offset = 0;
        limit = 25;
        isApiCall = true;
        isGetData = true;

        layoutManager=new LinearLayoutManager(mContext);
        transaction_full_list.setLayoutManager(layoutManager);

        getData();

        toolbar_back.setOnClickListener(v -> {
            finish();
        });

        transaction_full_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 1
                        && isGetData && isApiCall) {
                    scroll_loading.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });

        pull_transaction.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<>();
                offset = 0;
                isGetData = true;
                getData();
            }
        });

    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            /*if (ConstantUtil.is_game_test)
                jsonObject.put("user_id","670944" );//preferences.getUserModel().getId()
            else*/
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isProgress=true;
        if (pull_transaction != null && pull_transaction.isRefreshing()) {
            isProgress=false;
        }

        isApiCall = false;
        HttpRestClient.postJSON(mContext,isProgress , ApiManager.withdrawListWithTds, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "TRANSACTION_LIST: " + responseBody.toString());
                checkPull();
                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    if (limit > array.length()) {
                        isGetData = false;
                    }
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        WithdrawListModel transactionModel = gson.fromJson(object.toString(), WithdrawListModel.class);
                        list.add(transactionModel);
                    }

                    if (offset == 0) {
                        adapter=new WithdrawListAdapter(mContext,list);
                        transaction_full_list.setAdapter(adapter);
                    } else {
                        if (adapter != null) {
                            adapter.updateData(list);
                        }else{
                            adapter=new WithdrawListAdapter(mContext,list);
                            transaction_full_list.setAdapter(adapter);
                        }
                    }
                    offset += array.length();
                }

                if (list.size()>0){
                    txtNoData.setVisibility(View.GONE);
                    transaction_full_list.setVisibility(View.VISIBLE);
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    transaction_full_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                checkPull();
                if (list.size()>0){
                    txtNoData.setVisibility(View.GONE);
                    transaction_full_list.setVisibility(View.VISIBLE);
                }else {
                    txtNoData.setVisibility(View.VISIBLE);
                    transaction_full_list.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void initClick() {

    }

    private void checkPull() {
        if (pull_transaction != null && pull_transaction.isRefreshing()) {
            pull_transaction.setRefreshing(false);
        }
        isApiCall = true;
        scroll_loading.setVisibility(View.GONE);
    }

}