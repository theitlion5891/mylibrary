package com.fantafeat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fantafeat.Adapter.TransactionListAdapter;
import com.fantafeat.Model.TransactionModel;
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

public class TransactionListFragment extends BaseFragment {

    private RecyclerView transaction_full_list;
    private TransactionListAdapter transactionListAdapter;
    private List<TransactionModel> transactionModelList = new ArrayList<>();
    private int offset, limit;
    private boolean isApiCall, isGetData;
    private SwipeRefreshLayout pull_transaction;
    private TextView btnWithdrawHistory,toolbar_title;
    private LinearLayout scroll_loading;
    private ImageView toolbar_back;
    private Button btnDown;
    private LinearLayoutManager linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        initFragment(view);
        //initToolBar(view, "Transaction", true);
        getData();
        return view;
    }

    @Override
    public void initControl(View view) {
        offset = 0;
        limit = 25;
        isApiCall = true;
        isGetData = true;

        linearLayout = new LinearLayoutManager(mContext);
        transaction_full_list = view.findViewById(R.id.transaction_full_list);
        scroll_loading = view.findViewById(R.id.scroll_loading);
        pull_transaction = view.findViewById(R.id.pull_transaction);
        btnWithdrawHistory = view.findViewById(R.id.btnWithdrawHistory);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Transaction");
        toolbar_back = view.findViewById(R.id.toolbar_back);

    }

    @Override
    public void initClick() {

        pull_transaction.setOnRefreshListener(() -> {
            transactionModelList = new ArrayList<>();
            offset = 0;
            isGetData = true;
            getData();
        });

        toolbar_back.setOnClickListener(v -> {
            RemoveFragment();
        });

        btnWithdrawHistory.setOnClickListener(v -> {

        });

        transaction_full_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayout != null &&
                        linearLayout.findLastCompletelyVisibleItemPosition() == transactionModelList.size() - 1
                        && isGetData && isApiCall) {

                    scroll_loading.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isApiCall = false;
        HttpRestClient.postJSON(mContext, true, ApiManager.TRANSACTION_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult123: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    checkPull();
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    if (limit > array.length()) {
                        isGetData = false;
                    }
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        TransactionModel transactionModel = gson.fromJson(object.toString(), TransactionModel.class);
                        transactionModelList.add(transactionModel);
                    }

                    if (offset == 0) {
                        transactionListAdapter = new TransactionListAdapter(mContext, transactionModelList);
                        transaction_full_list.setLayoutManager(linearLayout);
                        transaction_full_list.addItemDecoration(new DividerItemDecoration(mContext, 0));
                         transaction_full_list.setAdapter(transactionListAdapter);

                    } else {
                        if (transactionListAdapter != null) {
                            transactionListAdapter.updateData(transactionModelList);
                        }else{
                            transactionListAdapter = new TransactionListAdapter(mContext, transactionModelList);
                            transaction_full_list.setLayoutManager(linearLayout);
                            transaction_full_list.addItemDecoration(new DividerItemDecoration(mContext, 0));
                            transaction_full_list.setAdapter(transactionListAdapter);
                        }
                    }
                    offset += array.length();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkPull() {
        if (pull_transaction != null && pull_transaction.isRefreshing()) {
            pull_transaction.setRefreshing(false);
        }
        isApiCall = true;
        scroll_loading.setVisibility(View.GONE);
    }
}