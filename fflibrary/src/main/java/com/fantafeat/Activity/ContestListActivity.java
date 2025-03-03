package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.fantafeat.Adapter.ContestQuantityAdapter;
import com.fantafeat.Adapter.GroupListAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Fragment.ContestListInnerFragment;
import com.fantafeat.Fragment.MyMatchesFragment;
import com.fantafeat.Fragment.MyTeamFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.ContestQuantityModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.PassModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContestListActivity extends BaseActivity {

    public TabLayout tabLayout;
    public TextView txtAnounce, btnClassic,/*btnDuo,*/
            btnQuinto;
    public LinearLayout layXi, layClassic, layDuo, layQuinto;
    private Toolbar toolbar;
    public ImageView mToolBarBack, toolbar_wallet, imgInfo;
    private static ViewPager2 viewPager;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private TextView match_title, timer;
    private MatchModel selected_match;
    private long diff;

    private CountDownTimer countDownTimer;

    ContestListInnerFragment contestListInnerFragment;
    MyMatchesFragment myMatchesFragment;
    MyTeamFragment myTeamFragment;
    public BottomSheetDialog bottomSheetDialog = null;
    public ContestModel.ContestDatum list = new ContestModel.ContestDatum();
    public List<PlayerListModel> playerListModels = new ArrayList<>();
    public JSONObject team_count, join_count;
    float use_deposit = 0;
    float use_transfer = 0;
    float use_winning = 0;
    float use_donation_deposit = 0;
    float useBonus = 0;
    float useCoin = 0;
    float amtToAdd = 0;
    ContestModel.ContestDatum contestData;
    int mainPosition = -1;

    private ArrayList<GroupModel> duoList;
    private RecyclerView recyclerDuo;
    private LinearLayout layNoDataDuo;
    private LinearLayout ll_tabs;
    private RelativeLayout layTabs;
    private SwipeRefreshLayout swipeDuo;
    private String selectedTag = "1", selectedContestId = "";
   // private Socket mSocket = null;
    private String selectedState = "";
    private Spinner spinState;
    private ArrayList<String> cityName, cityId;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private String selectedGender = "Select Gender";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_list);
        Window window = ContestListActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));
        toolbar = findViewById(R.id.contest_list_toolbar);
        setSupportActionBar(toolbar);

      //  mSocket = MyApp.getInstance().getSocketInstance();

        match_title = findViewById(R.id.match_title);
        timer = findViewById(R.id.timer);
        mToolBarBack = findViewById(R.id.toolbar_back);
        toolbar_wallet = findViewById(R.id.toolbar_wallet);
        recyclerDuo = findViewById(R.id.recyclerDuo);
        layNoDataDuo = findViewById(R.id.layNoDataDuo);
        swipeDuo = findViewById(R.id.swipeDuo);
        layTabs = findViewById(R.id.layTabs);
        layXi = findViewById(R.id.layXi);
        imgInfo = findViewById(R.id.imgInfo);
        txtAnounce = findViewById(R.id.txtAnounce);
        txtAnounce.setSelected(true);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        btnClassic = findViewById(R.id.btnClassic);
        //btnDuo = findViewById(R.id.btnDuo);
        btnQuinto = findViewById(R.id.btnQuinto);

        layClassic = findViewById(R.id.layClassic);
        layDuo = findViewById(R.id.layDuo);
        ll_tabs = findViewById(R.id.ll_tabs);
        layQuinto = findViewById(R.id.layQuinto);

        initClick();
        if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            ll_tabs.setVisibility(View.GONE);
        }else{
            ll_tabs.setVisibility(View.VISIBLE);
        }

        if (preferences.getMatchModel() != null) {
            selected_match = preferences.getMatchModel();
        }

        if (selected_match.getMatchTitle() != null) {
            match_title.setText(selected_match.getTeam1Sname() + " vs " + selected_match.getTeam2Sname());
        }

        if (selected_match.getIs_fifer().equalsIgnoreCase("yes")/* ||
                selected_match.getIs_single().equalsIgnoreCase("yes")*/) {
            layTabs.setVisibility(View.VISIBLE);
        } else {
            layTabs.setVisibility(View.GONE);
        }

        setTimer();

        mFragmentTitleList.add("Contests");
        mFragmentTitleList.add("My Contest");
        mFragmentTitleList.add("My Team");

        viewPager.setAdapter(createContestAdapter());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(mFragmentTitleList.get(position));
                    viewPager.setCurrentItem(tab.getPosition(), false);
                }).attach();

        viewPager.setUserInputEnabled(ApiManager.isPagerSwipe);
        ConstantUtil.reduceDragSensitivity(viewPager);
        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        if (getIntent().hasExtra("link_contest_id")) {
            selectedContestId = getIntent().getStringExtra("link_contest_id");
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Log.e(TAG, "onTabSelected: 0");

                    if (contestListInnerFragment != null) {
                        //contestListInnerFragment.getContests();
                    }
                } else if (tab.getPosition() == 1) {
                    Log.e(TAG, "onTabSelected: 1");
                    if (myMatchesFragment != null) {
                        //myMatchesFragment.getData();
                    }
                } else if (tab.getPosition() == 2) {
                    Log.e(TAG, "onTabSelected: 2");
                    if (myTeamFragment != null) {
                        //myTeamFragment.getTempTeamData();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (TextUtils.isEmpty(preferences.getMatchModel().getTime_change_msg())) {
            layXi.setVisibility(View.GONE);
            //txtAnounce.setText("The 'Starting XI' is a tool to help you choose your squad, but we encourage that you conduct your own research before forming teams.");
        } else {
            layXi.setVisibility(View.VISIBLE);
            txtAnounce.setText(preferences.getMatchModel().getTime_change_msg());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow = false;

       /* Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->   getTempTeamData(), 400);*/


        if (selectedTag.equalsIgnoreCase("2")) {
            getDuoGroup();
        }
        if (selectedTag.equalsIgnoreCase("3")) {
            getDuoGroup();
        }

        /*if (mSocket != null) {
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj = new JSONObject();
                if (preferences.getUserModel() != null) {
                    obj.put("user_id", preferences.getUserModel().getId());
                    obj.put("title", "contestlist");
                }
                mSocket.emit("connect_user", obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void initClick() {

        mToolBarBack.setOnClickListener(view -> onBackPressed());

        toolbar_wallet.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AddDepositActivity.class);
            intent.putExtra("isJoin", true);
            intent.putExtra("depositAmt", "");
            startActivity(intent);
        });

        btnClassic.setOnClickListener(view -> {
            selectedTag = "1";
            btnClassic.setBackgroundResource(R.drawable.primary_fill_border);
            btnClassic.setTextColor(getResources().getColor(R.color.white_pure));
           /* btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*/
            btnQuinto.setBackgroundResource(R.drawable.transparent_view);
            btnQuinto.setTextColor(getResources().getColor(R.color.black_pure));

            layClassic.setVisibility(View.VISIBLE);
            layDuo.setVisibility(View.GONE);
            layQuinto.setVisibility(View.GONE);
            imgInfo.setVisibility(View.GONE);

        });

        /*btnDuo.setOnClickListener(view -> {
            selectedTag="2";
            btnClassic.setBackgroundResource(R.drawable.transparent_view);
            btnClassic.setTextColor(getResources().getColor(R.color.black));

            btnDuo.setBackgroundResource(R.drawable.primary_fill_border);
            btnDuo.setTextColor(getResources().getColor(R.color.white));
            btnQuinto.setBackgroundResource(R.drawable.transparent_view);
            btnQuinto.setTextColor(getResources().getColor(R.color.black));

            layClassic.setVisibility(View.GONE);
            layDuo.setVisibility(View.VISIBLE);
            imgInfo.setVisibility(View.VISIBLE);
            layQuinto.setVisibility(View.GONE);

            getDuoGroup();

        });*/

        btnQuinto.setOnClickListener(view -> {
            selectedTag = "3";

            btnClassic.setBackgroundResource(R.drawable.transparent_view);
            btnClassic.setTextColor(getResources().getColor(R.color.black_pure));

           /* btnDuo.setBackgroundResource(R.drawable.transparent_view);
            btnDuo.setTextColor(getResources().getColor(R.color.black));*/
            btnQuinto.setBackgroundResource(R.drawable.primary_fill_border);
            btnQuinto.setTextColor(getResources().getColor(R.color.white_pure));

            layClassic.setVisibility(View.GONE);
            layDuo.setVisibility(View.VISIBLE);
            imgInfo.setVisibility(View.VISIBLE);
            layQuinto.setVisibility(View.GONE);

            getDuoGroup();

        });

        swipeDuo.setOnRefreshListener(this::getDuoGroup);

        imgInfo.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                showInfoDialog();
            }
        });

    }

    private void showInfoDialog() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.ludo_tnc_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtTnc = view.findViewById(R.id.txtTnc);
        ImageView imgClose = view.findViewById(R.id.imgClose);

        if (selectedTag.equalsIgnoreCase("2")) {
            txtTitle.setText("Rules for Singles");
            txtTnc.setText("\n" +
                    "- Single Entry per Contest (1 Player Selection out of 5 Players)\n\n" +
                    "- Join Unlimited Contests with different Entry Fee\n\n" +
                    "- Refund of Entry Fee if your selected player is not in lineups\n");

        } else if (selectedTag.equalsIgnoreCase("3")) {
            txtTitle.setText("Rules for Fifer");
            txtTnc.setText("\n" +
                    "- Join with Different Players with Multiple Entries in a Contest\n\n" +
                    "- Unlimited Joining with Endless Prize Pool\n\n" +
                    "- Confirmed Contest\n\n" +
                    "- Refund of Entry Fee if your selected player is not in lineups\n");
        }

        imgClose.setOnClickListener(vi -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();

    }

    private void getDuoGroup() {
        duoList = new ArrayList<>();
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());//ConstantUtil.testMatchId
            jsonObject.put("display_type", selectedTag);
            jsonObject.put("user_id", preferences.getUserModel().getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("getDuoGroup", jsonObject.toString());

        boolean isProgress = true;
        if (swipeDuo.isRefreshing()) {
            isProgress = false;
        }

        HttpRestClient.postJSON(mContext, isProgress, ApiManager.MATCH_WISE_GROUP_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getDuoGroup: " + responseBody.toString());
                if (swipeDuo.isRefreshing()) {
                    swipeDuo.setRefreshing(false);
                }
                if (responseBody.optBoolean("status")) {

                    JSONArray data = responseBody.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        duoList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.optJSONObject(i);
                            GroupModel groupModel = gson.fromJson(obj.toString(), GroupModel.class);
                            groupModel.setType(selectedTag);
                            duoList.add(groupModel);
                        }
                        GroupListAdapter adapter = new GroupListAdapter(mContext, duoList, preferences.getMatchModel(), model -> {
                            if (selectedTag.equalsIgnoreCase("2")) {
                                startActivity(new Intent(mContext, SinglesContestActivity.class)
                                        .putExtra("group_model", model)
                                        .putExtra("is_match_after", false)
                                );
                            } else if (selectedTag.equalsIgnoreCase("3")) {
                                startActivity(new Intent(mContext, FiferContestActivity.class)
                                        .putExtra("group_model", model)
                                        .putExtra("is_match_after", false)
                                );
                            }
                        });
                        recyclerDuo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        recyclerDuo.setAdapter(adapter);
                    }
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                    LogUtil.e(TAG, "MATCH_WISE__GROUP_LIST:" + responseBody.optString("msg"));
                }

                checkDuoData();
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipeDuo.isRefreshing()) {
                    swipeDuo.setRefreshing(false);
                }
            }
        });
    }

    private void checkDuoData() {
        if (duoList.size() > 0) {
            recyclerDuo.setVisibility(View.VISIBLE);
            layNoDataDuo.setVisibility(View.GONE);
        } else {
            recyclerDuo.setVisibility(View.GONE);
            layNoDataDuo.setVisibility(View.VISIBLE);
        }
    }

    public void confirmTeam(ContestModel.ContestDatum contestData, int mainPosition) {

        if (playerListModels != null && playerListModels.size() > 0) {
            if (TextUtils.isEmpty(preferences.getUserModel().getEmailId())) {
                showBasicDetailDialog(contestData, mainPosition);
                return;
            }

            this.contestData = null;
            this.mainPosition = -1;

        /*use_deposit = use_transfer = use_winning = use_donation_deposit = useBonus = 0;

        final float[] Contest_fee = {CustomUtil.convertFloat(contestData.getEntryFee())};
        float orgContestEntry = CustomUtil.convertFloat(contestData.getEntryFee());
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());

        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        float usableBonus = 0;
       // String teamno="";


        if (contestData.getNewOfferRemovedList().size()>0){
            NewOfferModal modal=contestData.getNewOfferRemovedList().get(0);
            if (modal.getDiscount_entry_fee().equalsIgnoreCase("")){
                Contest_fee[0] =CustomUtil.convertFloat(contestData.getEntryFee());
                orgContestEntry =CustomUtil.convertFloat(contestData.getEntryFee());
            }else {
                Contest_fee[0] =CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                orgContestEntry =CustomUtil.convertFloat(modal.getDiscount_entry_fee());
            }
            usableBonus=CustomUtil.convertFloat(modal.getUsed_bonus());
           // teamno=modal.getTeam_no();
        }
        else {
            usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
            Contest_fee[0] =CustomUtil.convertFloat(contestData.getEntryFee());
            orgContestEntry =CustomUtil.convertFloat(contestData.getEntryFee());
        }

        useBonus = ((Contest_fee[0] * usableBonus) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (Contest_fee[0] - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
            Contest_fee[0] = Contest_fee[0] - useBonus;
        }

        if ((Contest_fee[0] - deposit) < 0) {
            use_deposit = Contest_fee[0];
        }
        else {
            use_deposit = deposit;
            //use_transfer = transfer_bal;
            use_winning = Contest_fee[0] - deposit;
        }

        LogUtil.e(TAG, "onClick: deposit: " + deposit +"\n useBonus:" + useBonus + "\n Winning:" + winning);
        LogUtil.e(TAG, "onClick: Total: " + (deposit + useBonus + winning)+"\n Contest_fee:" + Contest_fee[0]);*/

            if (!isValidForJoin(contestData, 1)) {//((deposit +  winning ) - Contest_fee[0]) < 0

                this.contestData = contestData;
                this.mainPosition = mainPosition;

                //float amt= Contest_fee[0] -(deposit + winning);
                double amt = Math.ceil(amtToAdd);

                if (amt < 1) {
                    amt = 1;
                }

                String patableAmt = CustomUtil.getFormater("0.00").format(amt);
                MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS, false);
                Intent intent = new Intent(mContext, AddDepositActivity.class);
                intent.putExtra("isJoin", true);
                intent.putExtra("depositAmt", patableAmt);
                intent.putExtra("contestData", gson.toJson(contestData));
                startActivity(intent);
            } else {
                TextView join_contest_fee, join_use_deposit, join_use_borrowed, join_use_rewards,
                        join_use_winning, join_user_pay;
                final CheckBox join_donation_select;
                TextView join_donation_text, join_contest;
                final EditText join_donation_price, edtConQty;
                final RecyclerView recyclerNoOfContest;

                ArrayList<ContestQuantityModel> contestQtyList = ConstantUtil.getContestEntryList();


                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_joining, null);

                join_contest = (TextView) view.findViewById(R.id.join_contest_btn);
                join_contest_fee = (TextView) view.findViewById(R.id.join_contest_fee);
                join_use_deposit = (TextView) view.findViewById(R.id.join_use_deposit);
                //join_use_borrowed = (TextView) view.findViewById(R.id.join_use_borrowed);
                join_use_rewards = (TextView) view.findViewById(R.id.join_use_rewards);
                join_use_winning = (TextView) view.findViewById(R.id.join_use_winning);
                join_user_pay = (TextView) view.findViewById(R.id.join_user_pay);
                recyclerNoOfContest = view.findViewById(R.id.recyclerNoOfContest);
                LinearLayout layMultiContest = view.findViewById(R.id.layMultiContest);
                LinearLayout layBonus = view.findViewById(R.id.layBonus);
                edtConQty = view.findViewById(R.id.edtConQty);
                edtConQty.setText("1");

                if (contestData.getConPlayerEntry().equalsIgnoreCase("Single")
                        && contestData.getAutoCreate().equalsIgnoreCase("yes")
                        && !(contestData.getEntryFee().equalsIgnoreCase("0") ||
                        contestData.getEntryFee().equalsIgnoreCase("0.0") ||
                        contestData.getEntryFee().equalsIgnoreCase("0.00"))) {

                    layMultiContest.setVisibility(View.VISIBLE);
                    recyclerNoOfContest.setLayoutManager(new GridLayoutManager(mContext, 6));
                    ContestQuantityAdapter adapter = new ContestQuantityAdapter(mContext, contestQtyList, new ContestQuantityAdapter.onQtyListener() {
                        @Override
                        public void onClick(ContestQuantityModel model) {
                            edtConQty.setText(model.getId());
                            edtConQty.setSelection(model.getId().length());
                        }
                    });
                    recyclerNoOfContest.setAdapter(adapter);
                    //float finalOrgContestEntry = orgContestEntry;
                    edtConQty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            for (int i = 0; i < contestQtyList.size(); i++) {
                                ContestQuantityModel model = contestQtyList.get(i);
                                if (s.toString().trim().equalsIgnoreCase(model.getId().trim())) {
                                    model.setSelected(true);
                                } else {
                                    model.setSelected(false);
                                }
                            }

                            adapter.notifyDataSetChanged();

                            if (!TextUtils.isEmpty(s.toString())) {
                                int qty = Integer.parseInt(s.toString());
                                if (qty > 500) {
                                    CustomUtil.showToast(mContext, "Maximum 500 Quantity allow");

                                    edtConQty.setText("500");
                                    edtConQty.setSelection(3);

                                    qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                    isValidForJoin(contestData, qty);

                                /*use_deposit=use_winning=useBonus=0;
                                Contest_fee[0]=CustomUtil.convertFloat(contestData.getEntryFee()) * qty;
                                // LogUtil.e(TAG, "onClick: total: "+ total);

                                float usableBonus = 0;
                                if (CustomUtil.convertInt(contestData.getMyJoinedTeam()) < CustomUtil.convertInt(contestData.getMaxTeamBonusUse())) {
                                    usableBonus = CustomUtil.convertFloat(contestData.getUseBonus());
                                }
                                else {
                                    usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                                }
                                useBonus = ((Contest_fee[0] * usableBonus) / 100);

                                if (useBonus > bonus) {
                                    useBonus = bonus;
                                }

                                if (Contest_fee[0] - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
                                    Contest_fee[0] = Contest_fee[0] - useBonus;
                                }

                                if ((Contest_fee[0] - deposit) < 0) {
                                    use_deposit = Contest_fee[0];
                                }
                                else {
                                    use_deposit = deposit;
                                    //use_transfer = transfer_bal;
                                    use_winning = Contest_fee[0] - deposit;
                                }*/
                                } else {
                                    qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                    isValidForJoin(contestData, qty);
                               /* use_deposit=use_winning=useBonus=0;
                                Contest_fee[0]=CustomUtil.convertFloat(contestData.getEntryFee()) * qty;
                                // LogUtil.e(TAG, "onClick: total: "+ total);

                                float usableBonus = 0;
                                if (CustomUtil.convertInt(contestData.getMyJoinedTeam()) < CustomUtil.convertInt(contestData.getMaxTeamBonusUse())) {
                                    usableBonus = CustomUtil.convertFloat(contestData.getUseBonus());
                                }
                                else {
                                    usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                                }
                                useBonus = ((Contest_fee[0] * usableBonus) / 100);

                                if (useBonus > bonus) {
                                    useBonus = bonus;
                                }

                                if (Contest_fee[0] - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
                                    Contest_fee[0] = Contest_fee[0] - useBonus;
                                }

                                if ((Contest_fee[0] - deposit) < 0) {
                                    use_deposit = Contest_fee[0];
                                }
                                else {
                                    use_deposit = deposit;
                                    //use_transfer = transfer_bal;
                                    use_winning = Contest_fee[0] - deposit;
                                }*/
                                }

                                LogUtil.e(TAG, "onClick: deposit_bal" + CustomUtil.getFormater("00.00").format(use_deposit) +
                                        "\n  Win_bal" + CustomUtil.getFormater("00.00").format(use_winning) +
                                        "\n  Donation useBonus" + CustomUtil.getFormater("00.00").format(useBonus));

                                join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
                                join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                                join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                                //join_use_coin.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useCoin));
                                join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));
                            } else {
                                join_use_deposit.setText(getResources().getString(R.string.rs) + "00.00");
                                join_use_winning.setText(getResources().getString(R.string.rs) + "00.00");
                                join_use_rewards.setText(getResources().getString(R.string.rs) + "00.00");
                                //join_use_coin.setText(getResources().getString(R.string.rs) + "00.00");
                                join_user_pay.setText(getResources().getString(R.string.rs) + "00.00");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                } else {
                    layMultiContest.setVisibility(View.GONE);
                }

                if (ConstantUtil.is_bonus_show) {
                    layBonus.setVisibility(View.VISIBLE);
                } else {
                    layBonus.setVisibility(View.GONE);
                }

                join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
                //join_use_borrowed.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_transfer));
                join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                // join_use_coin.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useCoin));
                // LogUtil.e("resval",useBonus+"   "+CustomUtil.getFormater("00.000").format(useBonus));
                join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contestData.getEntryFee())));// Contest_fee+ useBonus
                join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));// Contest_fee+ useBonus

                final float finalUseBonus = useBonus;
                join_contest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //join_contest.setEnabled(false);
                        if (MyApp.getClickStatus()) {
                        /*Float donation;
                        Boolean isDonation = false;*/

                            int qty = 0;
                            if (!TextUtils.isEmpty(edtConQty.getText().toString().trim())) {
                                qty = Integer.parseInt(edtConQty.getText().toString().trim());
                            }

                            //  float entry=(CustomUtil.convertFloat(contestData.getEntryFee()) * qty);

                        /*use_deposit = use_transfer = use_winning = use_donation_deposit = useBonus = 0;
                        float usableBonus = 0;

                        if (CustomUtil.convertInt(contestData.getMyJoinedTeam()) < CustomUtil.convertInt(contestData.getMaxTeamBonusUse())) {
                            usableBonus = CustomUtil.convertFloat(contestData.getUseBonus());
                        } else {
                            usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                        }
                        useBonus = ((entry * usableBonus) / 100);

                        if (useBonus > bonus) {
                            useBonus = bonus;
                        }

                        if (entry - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
                            entry = entry - useBonus;
                        }

                        if ((entry - deposit) < 0) {
                            use_deposit = entry;
                        }
                        else {
                            use_deposit = deposit;
                            //use_transfer = transfer_bal;
                            use_winning = entry - deposit;
                        }*/


                            if (!isValidForJoin(contestData, qty)) {//((deposit +  winning  ) - Contest_fee[0]) < 0
                                bottomSheetDialog.dismiss();
                                CustomUtil.showToast(mContext, "Insufficient Balance");
                                //float amt= Contest_fee[0] - (deposit + winning);
                                double amt = Math.ceil(amtToAdd);

                                if (amt < 1) {
                                    amt = 1;
                                }
                                contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                                String patableAmt = CustomUtil.getFormater("0.00").format(amt);
                                MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS, false);
                                Intent intent = new Intent(mContext, AddDepositActivity.class);
                                intent.putExtra("isJoin", true);
                                intent.putExtra("depositAmt", patableAmt);
                                intent.putExtra("contestData", gson.toJson(contestData));
                                startActivity(intent);
                                return;
                            }

                            SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date.before(MyApp.getCurrentDate())) {
                                Toast.makeText(mContext, "Time Up! Match Started", Toast.LENGTH_LONG).show();
                            }
                            else if (TextUtils.isEmpty(edtConQty.getText().toString().trim())) {
                                CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                            }
                            else if (qty <= 0) {
                                CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                            }
                            else if (qty > 500) {
                                CustomUtil.showToast(mContext, "Maximum 500 Quantity allow");
                            }
                            else {
                                bottomSheetDialog.dismiss();

                                contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                                joinContest(contestData, mainPosition);
                            }
                        }
                    }
                });

                bottomSheetDialog = new BottomSheetDialog(mContext);

                bottomSheetDialog.setContentView(view);

                if (bottomSheetDialog != null && !bottomSheetDialog.isShowing())
                    bottomSheetDialog.show();
            }
        } else {
            Gson gson = new Gson();
            String model = gson.toJson(contestData);
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            intent.putExtra("model", model);
            startActivity(intent);
        }


    }

    private boolean isValidForJoin(ContestModel.ContestDatum contestData, int qty) {
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus, usableBBCoins = 0;

        if (!TextUtils.isEmpty(contestData.getOffer_date_text())) {
            if (contestData.getNewOfferRemovedList().size() > 0) {
                NewOfferModal modal = contestData.getNewOfferRemovedList().get(0);
                if (modal.getDiscount_entry_fee().equalsIgnoreCase("")) {
                    finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
                } else {
                    finalEntryFee = CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                }
                usableBonus = CustomUtil.convertFloat(modal.getUsed_bonus());
            } else {
                usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
            }
        } else {
            usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
            finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
        }

        usableBBCoins = CustomUtil.convertFloat(contestData.getDefault_bb_coins());

        float usableBonus1 = 0;

        usableBonus1 = usableBonus;
        finalEntryFee = finalEntryFee * qty;

        useBonus = ((finalEntryFee * usableBonus1) / 100);
        useCoin = ((finalEntryFee * usableBBCoins) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (useCoin > bb_coin) {
            useCoin = bb_coin;
        }

        if (finalEntryFee - useBonus >= 0) {
            finalEntryFee = finalEntryFee - useBonus;
        }

        if (useCoin >= useBonus)
            useCoin = useCoin - useBonus;

        if (finalEntryFee - useCoin >= 0) {
            finalEntryFee = finalEntryFee - useCoin;
        }

        if ((finalEntryFee - deposit) < 0) {
            use_deposit = finalEntryFee;
        } else {
            use_deposit = deposit;
            use_winning = finalEntryFee - deposit;
        }

        float calReward = 0, calCoin = 0;

        if (bonus > 0) {
            if (useBonus <= bonus) {
                calReward = useBonus;
            } else if (useBonus > bonus) {
                calReward = bonus;
            } else {
                calReward = 0;
            }
        } else {
            calReward = 0;
        }

        if (bb_coin > 0) {
            if (useCoin <= bb_coin) {
                calCoin = useCoin;
            } else if (useCoin > bb_coin) {
                calCoin = bb_coin;
            } else {
                calCoin = 0;
            }
        } else {
            calCoin = 0;
        }

        float calBalance1 = deposit + winning + calReward + calCoin;
        float totalCharge = useBonus + use_deposit + use_winning + useCoin;

        amtToAdd = totalCharge - calBalance1;

        LogUtil.e("resp", "deposit:" + deposit + "\nwinning:" + winning + "\nbb_coin:" + bb_coin + "\nuse_deposit:" + use_deposit
                + "\nuse_transfer:" + use_transfer + "\nuse_winning:" + use_winning + "\nuse_bb_coin:" + useCoin + "\nuseBonus:" + useBonus);

        if (calBalance1 < totalCharge) {
            return false;
        }
        return true;
    }

    private void showBasicDetailDialog(ContestModel.ContestDatum contestData, int mainPosition) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        UserModel user = preferences.getUserModel();

        RelativeLayout layImage = view.findViewById(R.id.layImage);
        layImage.setVisibility(View.GONE);
        LinearLayout layName = view.findViewById(R.id.layName);
        layName.setVisibility(View.GONE);
        ImageView toolbar_back = view.findViewById(R.id.toolbar_back);
        //toolbar_back.setVisibility(View.GONE);
        toolbar_back.setImageResource(R.drawable.ic_close_otp);
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");

        EditText name_as_aadhar = view.findViewById(R.id.name_as_aadhar);
        EditText team_name = view.findViewById(R.id.edtTeamName);
        EditText mobile_number = view.findViewById(R.id.mobile_number);
        EditText email = view.findViewById(R.id.email);
        EditText dob = view.findViewById(R.id.dob);
        Spinner spinGender = view.findViewById(R.id.spinGender);
        Button confirm = view.findViewById(R.id.confirm);
        spinState = view.findViewById(R.id.spinState);

        name_as_aadhar.setText(user.getDisplayName());
        team_name.setText(user.getUserTeamName());
        mobile_number.setText(user.getPhoneNo());
        email.setText(user.getEmailId());
        dob.setText(user.getDob());

        selectedState = user.getStateId();

      /*  if (TextUtils.isEmpty(user.getEmailId())){
            email.setEnabled(true);
        }
        else {
            email.setEnabled(false);
        }*/

        team_name.setEnabled(user.getTeam_name_change_allow().equalsIgnoreCase("yes"));

        ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter(mContext, R.layout.spinner_text, genderList);
        spinGender.setAdapter(genderAdapter);
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedGender = genderList.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.YEAR, -18);

        date = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dob);
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        date.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

        dob.setOnClickListener(view1 -> date.show());

        confirm.setOnClickListener(view1 -> {
            String db = dob.getText().toString().trim();
            String strEmail = getEditText(email);
            String strName = getEditText(name_as_aadhar);
            String strTeam = getEditText(team_name);

            if (TextUtils.isEmpty(strName)) {
                CustomUtil.showToast(mContext, "Please Enter Name as on Aadhar Card.");
            } else if (TextUtils.isEmpty(strTeam)) {
                CustomUtil.showToast(mContext, "Please Enter Team Name.");
            } else if (strTeam.length() > 11) {
                CustomUtil.showToast(mContext, "Team name should be less than or equals to 11 characters.");
            } else if (TextUtils.isEmpty(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Email.");
            } else if (!isValidEmail(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Valid Email.");
            } else if (selectedState.equalsIgnoreCase("0")) {
                CustomUtil.showToast(mContext, "Please Select State.");
            } else if (TextUtils.isEmpty(db)) {
                CustomUtil.showToast(mContext, "Please Enter Date of Birth.");
            } else {
                callFirstApi(strName, strEmail, strTeam, db, bottomSheetDialog, contestData, mainPosition);
            }

        });

        toolbar_back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        switch (user.getGender()) {
            case "Male":
                spinGender.setSelection(1);
                break;
            case "Female":
                spinGender.setSelection(2);
                break;
            case "Other":
                spinGender.setSelection(3);
                break;
            default:
                spinGender.setSelection(0);
                break;
        }

        getStateData();

        bottomSheetDialog.show();
    }

    private void callFirstApi(String name, String email, String team, String dob, BottomSheetDialog bottomSheetDialog,
                              ContestModel.ContestDatum contestData, int mainPosition) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email);
            jsonObject.put("display_name", name);
            jsonObject.put("state_id", selectedState);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("dob", dob);
            jsonObject.put("user_team_name", team);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(email);
                    userModel.setDisplayName(name);
                    userModel.setStateId(selectedState);
                    userModel.setGender(selectedGender);
                    userModel.setDob(dob);
                    userModel.setUserTeamName(team);
                    preferences.setUserModel(userModel);
                    MyApp.getMyPreferences().setUserModel(userModel);

                    bottomSheetDialog.dismiss();

                    confirmTeam(contestData, mainPosition);

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                    LogUtil.e(TAG, "AUTHV3UpdateUserDetails contest join");
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateLabel(EditText dob) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void getStateData() {
        HttpRestClient.postData(mContext, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                    cityName = new ArrayList<String>();
                    cityName.add("Select state");
                    cityId = new ArrayList<String>();
                    cityId.add("0");
                    ArrayList<StateModal> stateModals = new ArrayList<>();
                    int pos = 0;
                    if (responseBody.optBoolean("status")) {
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            String id = obj.optString("id");
                            if (selectedState.equals(id)) {
                                pos = i + 1;
                            }
                            cityId.add(id);
                            cityName.add(name);
                            StateModal stateModal = gson.fromJson(obj.toString(), StateModal.class);
                            stateModals.add(stateModal);
                        }
                        preferences.setStateList(stateModals);
                    }
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState = cityId.get(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spinState.setSelection(pos);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }

        });
    }

    public void linkAndJoin(ContestModel.ContestDatum list, int position) {
        if (preferences.getPlayerModel() != null && preferences.getPlayerModel().size() > 0) {
            this.list = list;

            if (CustomUtil.convertInt(list.getMaxJoinTeam()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                CustomUtil.showTopSneakError(mContext, "Max " + list.getMaxJoinTeam() + " team(s) allowed");
            }
            else if (CustomUtil.convertInt(preferences.getUpdateMaster().getMaxTeamCount()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                CustomUtil.showTopSneakError(mContext, "Max " + preferences.getUpdateMaster().getMaxTeamCount() + " team(s) can be created");
            }
            else if (preferences.getPlayerModel() != null &&
                    CustomUtil.convertInt(list.getMyJoinedTeam()) >= preferences.getPlayerModel().size()) {
                //MyApp.getMyPreferences().setPref(PrefConstant.IS_CONTEST_JOIN,true);
                Gson gson = new Gson();
                Type menu = new TypeToken<ContestModel.ContestDatum>() {
                }.getType();

                String json = gson.toJson(list, menu);
                LogUtil.e("resp", "selected contest json: " + json);

                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra("data", json);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 2);
                startActivity(intent);
            }
            else {
                int teamCnt = 0;
                if (((ContestListActivity) mContext).team_count != null) {
                    teamCnt = CustomUtil.convertInt(team_count.optString("total_team"));
                }
                LogUtil.d("colDara", list.getJoinedTeamTempTeamId() + "   " + teamCnt);
                if ((TextUtils.isEmpty(list.getJoinedTeamTempTeamId()) || list.getJoinedTeamTempTeamId().equals("0")) &&
                        teamCnt == 1) {
                    confirmTeam(list, position);
                } else {
                    Gson gson = new Gson();
                    String model = gson.toJson(list);
                    Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                    intent.putExtra("model", model);
                    startActivity(intent);
                }
            }
        } else {
            Gson gson = new Gson();
            Type menu = new TypeToken<ContestModel.ContestDatum>() {
            }.getType();

            String json = gson.toJson(list, menu);
            LogUtil.e("resp", "selected contest json: " + json);

            Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
            intent.putExtra("data", json);
            intent.putExtra(PrefConstant.TEAMCREATETYPE, 2);
            startActivity(intent);
        }

    }

    private void joinContest(ContestModel.ContestDatum contestData, int mainPosition) {

        if (playerListModels != null && playerListModels.size() > 0) {
            PlayerListModel playerListModel = playerListModels.get(0);

            DecimalFormat format = CustomUtil.getFormater("0.0000");

            final JSONObject jsonObject = new JSONObject();
            final JSONObject childObj = new JSONObject();
            final JSONArray array = new JSONArray();

            try {
                //jsonObject.put("temp_team_id", playerListModel.getId());
                childObj.put("pass_id", "");
                jsonObject.put("pass_id", "");
                jsonObject.put("match_id", preferences.getMatchModel().getId());
                jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
                jsonObject.put("user_id", preferences.getUserModel().getId());
                jsonObject.put("contest_id", contestData.getId());
                if (TextUtils.isEmpty(contestData.getJoin_con_qty()) || contestData.getJoin_con_qty().equalsIgnoreCase("0"))
                    contestData.setJoin_con_qty("1");
                jsonObject.put("join_con_qty", contestData.getJoin_con_qty());
                // jsonObject.put("team_name", playerListModel.getTempTeamName());
                if (use_deposit > 0) {
                    use_deposit = use_deposit / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (useBonus > 0) {
                    useBonus = useBonus / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (use_winning > 0) {
                    use_winning = use_winning / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (useCoin > 0) {
                    useCoin = useCoin / Integer.parseInt(contestData.getJoin_con_qty());
                }
                jsonObject.put("deposit_bal", format.format(use_deposit));//92.85
                jsonObject.put("bonus_bal", format.format(useBonus));//6
                jsonObject.put("win_bal", format.format(use_winning));//21.15
                jsonObject.put("ff_coins_bal", format.format(useCoin));
                //jsonObject.put("transfer_bal", format.format(use_transfer));
                jsonObject.put("donation_bal", format.format(use_donation_deposit));

                jsonObject.put("applied_pass_count", "0");
                childObj.put("team_no", "1");

                childObj.put("deposit_bal", format.format(use_deposit));
                childObj.put("win_bal", format.format(use_winning));
                childObj.put("bonus_bal", format.format(useBonus));
                childObj.put("ff_coins_bal", format.format(useCoin));
                //childObj.put("transfer_bal", format.format(use_transfer));
                childObj.put("donation_bal", format.format(use_donation_deposit));
                childObj.put("team_name", playerListModel.getTempTeamName());
                childObj.put("temp_team_id", playerListModel.getId());

                if (contestData.getIs_pass() != null && contestData.getIs_pass().equalsIgnoreCase("yes")) {
                    if (contestData.getPassModelArrayList().size() > 0) {
                        LogUtil.e(TAG, "teamSelectJoin=" + contestData.getPassModelArrayList().size());
                        PassModel model = contestData.getPassModelArrayList().get(0);
                        int count = Integer.parseInt(model.getNoOfEntry()) - Integer.parseInt(model.getTotalJoinSpot());
                        if (count >= 1) {
                            childObj.put("pass_id", model.getPassId());
                            jsonObject.put("pass_id", model.getPassId());
                            jsonObject.put("applied_pass_count", "1");

                            JSONObject passData = new JSONObject();
                            passData.put("id", model.getId());
                            passData.put("no_of_entry", model.getNoOfEntry());
                            passData.put("pass_id", model.getPassId());
                            passData.put("total_join_spot", model.getTotalJoinSpot());
                            jsonObject.put("my_pass_data", passData);
                            LogUtil.e(TAG, "pass_data=>" + passData);
                        }
                    }
                }
                array.put(childObj);
                jsonObject.put("team_array", array);


            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtil.d("resp", jsonObject.toString());

            HttpRestClient.postJSON(mContext, true, ApiManager.JOIN_CONTEST2MultiJoin, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "onSuccessResultciontes: " + responseBody.toString());

                    if (responseBody.optBoolean("status")) {

                        //refreshArray(playerListModel.getId());

                        UserModel user = preferences.getUserModel();

                        if (use_deposit > 0) {
                            use_deposit = use_deposit * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (useBonus > 0) {
                            useBonus = useBonus * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (use_winning > 0) {
                            use_winning = use_winning * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (useCoin > 0) {
                            useCoin = useCoin * Integer.parseInt(contestData.getJoin_con_qty());
                        }

                        float deposit_bal = CustomUtil.convertFloat(user.getDepositBal()) - use_deposit;
                        float coin_bal = CustomUtil.convertFloat(user.getFf_coin()) - useCoin;
                        //float transfer_bal = CustomUtil.convertFloat(user.getTransferBal()) - use_transfer;
                        float bonus_bal = CustomUtil.convertFloat(user.getBonusBal()) - useBonus;
                        float winning_bal = CustomUtil.convertFloat(user.getWinBal()) - use_winning;

                        user.setDepositBal(String.valueOf(deposit_bal));
                        //user.setTransferBal(String.valueOf(transfer_bal));
                        user.setBonusBal(String.valueOf(bonus_bal));
                        user.setWinBal(String.valueOf(winning_bal));
                        user.setFf_coin(String.valueOf(coin_bal));

                        float total;
                        //CustomUtil.convertFloat(user.getTransferBal()) +
                        /* +
                                    CustomUtil.convertFloat(user.getBonusBal())*/
                        if (ConstantUtil.is_bonus_show) {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) +
                                    CustomUtil.convertFloat(user.getBonusBal());

                        } else {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }
                        user.setTotal_balance(total);


                        preferences.setUserModel(user);
                        CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                        LogUtil.e(TAG, "JOIN_CONTEST2MultiJoinSucess:contest join");
                        if (contestListInnerFragment != null) {
                            contestListInnerFragment.getContests();
                        }


                    } else {
                        String message = responseBody.optString("msg");
                        CustomUtil.showTopSneakWarning(mContext, message);
                        LogUtil.e(TAG, "JOIN_CONTEST2MultiJoin_failed:contest join failed");

                        if (responseBody.has("new_con_id")) {
                            final String new_con_id = responseBody.optString("new_con_id");
                            if (new_con_id != null && !new_con_id.equals("null") && !new_con_id.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ContestListActivity.this);
                                builder.setTitle("The Contest is already full!!");//Almost there! That League filled up
                                builder.setMessage("Don't worry, we have same contest for you! join this contest.");//No worries, join this League instead! it is exactly the same type
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        contestData.setId(new_con_id);
                                        joinContest(contestData, mainPosition);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                // AlertDialog alertDialog = builder.create();
                                builder.setCancelable(false);
                                builder.show();
                            } else {
                                CustomUtil.showTopSneakError(mContext, "Please join another contest.");//Almost there! The League already filled Join any other League.
                            }
                        } else {
                            CustomUtil.showTopSneakError(mContext, message);
                            LogUtil.e(TAG, "joinmultiple2con:contest join");
                        }
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {
                    LogUtil.e(TAG, "fail=>" + responseBody);
                }
            });
        }
        else {
            Gson gson = new Gson();
            String model = gson.toJson(contestData);
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            intent.putExtra("model", model);
            startActivity(intent);
        }

    }

    public void followUnFollowAction(ContestModel.ContestDatum object) {
        List<ContestModel.ContestDatum> list = preferences.getFavList();
        if (list != null && list.size() > 0) {
            if (object.getContestFavorite()) {
                int pos = -1;
                for (int i = 0; i < list.size(); i++) {
                    ContestModel.ContestDatum contest = list.get(i);
                    if (
                            object.getConDisplayType().equals(contest.getConDisplayType()) &&
                                    object.getConTplId().equals(contest.getConTplId()) &&
                                    object.getConTypeId().equals(contest.getConTypeId()) &&
                                    object.getConTypeName().equals(contest.getConTypeName()) &&
                                    object.getDistributeAmount().equals(contest.getDistributeAmount()) &&
                                    object.getEntryFee().equals(contest.getEntryFee())
                    ) {
                        pos = i;
                    }
                }
                if (pos != -1) {
                    list.remove(pos);
                    preferences.setFavList(list);
                }
                CustomUtil.showTopSneakSuccess(mContext, "Contest Unfavorite successfully");
            } else {
                boolean isFavAdd = false;
                for (int i = 0; i < list.size(); i++) {
                    ContestModel.ContestDatum contest = list.get(i);
                    if (object.getConDisplayType().equals(contest.getConDisplayType()) &&
                            object.getConTplId().equals(contest.getConTplId()) &&
                            object.getConTypeId().equals(contest.getConTypeId()) &&
                            object.getConTypeName().equals(contest.getConTypeName()) &&
                            object.getDistributeAmount().equals(contest.getDistributeAmount()) &&
                            object.getEntryFee().equals(contest.getEntryFee())
                    ) {
                        isFavAdd = true;
                    }
                }
                if (!isFavAdd) {
                    list.add(object);
                    preferences.setFavList(list);
                    CustomUtil.showTopSneakSuccess(mContext, "Contest favorite successfully");
                } else {
                    CustomUtil.showTopSneakSuccess(mContext, "Contest is already favorite");
                }

            }

        } else {
            List<ContestModel.ContestDatum> list1 = new ArrayList<>();
            list1.add(object);
            preferences.setFavList(list1);
            CustomUtil.showTopSneakSuccess(mContext, "Contest favorite successfully");
        }

        if (contestListInnerFragment != null) {
            contestListInnerFragment.getContests();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1002) {
            if (contestData != null && mainPosition != -1) {
                if (data.hasExtra("isDirectJoin") && data.getBooleanExtra("isDirectJoin", false)) {
                    confirmTeam(contestData, mainPosition);
                }
            }
        }
    }

    private RecyclerView.Adapter createContestAdapter() {
        ContestListViewPagerAdapter adapter = new ContestListViewPagerAdapter(this, mContext);
        return adapter;
    }

    public void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            selected_match = preferences.getMatchModel();

            date = format.parse(selected_match.getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            Log.e(TAG, "onBindViewHolder: " + diff);
        } catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        /*if (matchModel.getMatchStatus().equals("Pending")) {*/
        countDownTimer = new CountDownTimer(diff, 1000) {
            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = millisUntilFinished / daysInMilli;
                millisUntilFinished = millisUntilFinished % daysInMilli;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String diff = "";
                if (elapsedDays > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedDays) + "d " + CustomUtil.getFormater("00").format(elapsedHours) + "h Left";
                } else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedHours) + "h " + CustomUtil.getFormater("00").format(elapsedMinutes) + "m";
                } else {
                    diff = CustomUtil.getFormater("00").format(elapsedMinutes) + "m " + CustomUtil.getFormater("00").format(elapsedSeconds) + "s";
                }
                timer.setText(diff);
            }

            public void onFinish() {

                timesOver();
                // Do something on finish
            }
        }.start();
        /*} else {
            holder.match_remain_time.setTextSize(14);
            holder.match_remain_time.setText(List.getMatchStatus());
        }*/
    }

    public void timesOver() {
        Button btn_ok;

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(view1 -> {
            ConstantUtil.isTimeOverShow = false;
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                    R.id.fragment_container,
                    new HomeFragment(),
                    ((HomeActivity) mContext).fragmentTag(0),
                    FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);*/
        });
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow && !((Activity) mContext).isFinishing()) {
            ConstantUtil.isTimeOverShow = true;
            bottomSheetDialog.show();
        }
    }

    public class ContestListViewPagerAdapter extends FragmentStateAdapter {
        Context mContext;

        public ContestListViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Context mContext) {
            super(fragmentActivity);
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                contestListInnerFragment = ContestListInnerFragment.newInstance(selectedContestId);
                return contestListInnerFragment;
            } else if (position == 1) {
                return MyMatchesFragment.newInstance();
            } else if (position == 2) {
                return MyTeamFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }

    }

}