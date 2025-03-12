package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.MoreContestListActivity;
import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Adapter.ContestFilterAdapter;
import com.fantafeat.Adapter.ContestHeaderAdapter;
import com.fantafeat.Adapter.MoreContestFilterAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.OfferModel;
import com.fantafeat.Model.PassModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MyRecyclerScroll;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ContestListInnerFragment extends BaseFragment {

    RecyclerView contest_list, match_header_filter;
    private List<ContestModel> contestModelList;
    private List<ContestModel> contestFilterModelList;
    private List<OfferModel> offerModelList;
    public ContestHeaderAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView contest_filter_title1, contest_filter_title2;
    private ContestListActivity contestListActivity;
    private LinearLayout contest_filter_layout1, contest_filter_layout2, create_team;
    public String total_team_count;
    public CardView f1card, f2card;
    private long diff;
    private JSONObject team_count, join_count;
    private ContestFilterAdapter filterAdapter;
    private ImageView imgFilter,btnUnFillFilter;
    private String lineupCount = "", selectedContestId = "";
    private BottomSheetDialog bottomSheetDialog = null;
    private ArrayList<EventModel> listTop;
    private long lastUpdateTime=0;

    public ContestListInnerFragment() {
        //  super();
        // this.contestListActivity = contestListActivity;
    }

    public static ContestListInnerFragment newInstance(String selectedContestId) {
        ContestListInnerFragment myFragment = new ContestListInnerFragment();
        Bundle args = new Bundle();
        args.putString("selectedContestId", selectedContestId);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedContestId = getArguments().getString("selectedContestId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contest_list_inner, container, false);
        initFragment(view);
        preferences.setPref("join_contest", false);
        //getContestsDelay();
        //getContests();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isApiCall=false;

        if (BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            if (!ConstantUtil.play_store_app) {
                isApiCall=true;
            }
        }
        else {
            isApiCall=true;
        }
        if (isApiCall){
            /*if (mSocket!=null){
                if (!mSocket.connected())
                    mSocket.connect();
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("en", "tradingData");
                    LogUtil.e("resp","tradingData: "+obj);
                    mSocket.emit("req", obj);
                    listener();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }*/
        }

        initContestList();
        if (preferences.getPrefBoolean("join_contest")) {
            if (((ContestListActivity) mContext).bottomSheetDialog!=null && ((ContestListActivity) mContext).bottomSheetDialog.isShowing()){
                ((ContestListActivity) mContext).bottomSheetDialog.dismiss();
            }
            preferences.setPref("join_contest", false);
            String contest_pass_data=preferences.getPrefString("contest_pass_data");
            if (TextUtils.isEmpty(contest_pass_data)){
                contest_pass_data="";
            }
            Log.e(TAG, "onResume: ");
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            String model = gson.toJson(((ContestListActivity) mContext).list);
            intent.putExtra("model", contest_pass_data);//model
            preferences.setPref("contest_pass_data", "");
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void initControl(View view) {

       // mSocket=MyApp.getInstance().getSocketInstance();

        contest_list = view.findViewById(R.id.contest_main_list);
        swipeRefreshLayout = view.findViewById(R.id.contest_refresh);
        create_team = view.findViewById(R.id.create_team);
        contest_filter_title1 = view.findViewById(R.id.contest_filter_title1);
        contest_filter_title2 = view.findViewById(R.id.contest_filter_title2);
        contest_filter_layout1 = view.findViewById(R.id.contest_filter_layout1);
        contest_filter_layout2 = view.findViewById(R.id.contest_filter_layout2);
        imgFilter = view.findViewById(R.id.imgFilter);
        f1card = view.findViewById(R.id.f1card);
        f2card = view.findViewById(R.id.f2card);
        btnUnFillFilter = view.findViewById(R.id.btnUnFillFilter);
        // match_header_filter = view.findViewById(R.id.match_header_filter);
        listTop = new ArrayList<>();


    }

    private void initContestList() {
        contestModelList = new ArrayList<>();
        contestFilterModelList = new ArrayList<>();
        offerModelList = new ArrayList<>();

        adapter = new ContestHeaderAdapter(mContext, contestModelList, ContestListInnerFragment.this, gson);
        contest_list.setLayoutManager(new LinearLayoutManager(mContext));
        contest_list.setAdapter(adapter);

        getContests();
    }

    @Override
    public void initClick() {

        contest_list.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                create_team.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                int fabMargin = getResources().getDimensionPixelSize(R.dimen.top_bottom_margin);
                int fabMargin1 = getResources().getDimensionPixelSize(R.dimen.create_team_bottom_margin);
                create_team.animate().translationY(create_team.getHeight() + fabMargin1).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if ((System.currentTimeMillis()-lastUpdateTime)>=ConstantUtil.Refresh_delay) {
                ConstantUtil.isTimeOverShow = false;
                getContests();
            }else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        create_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 0);
                startActivity(intent);
                /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new CricketSelectPlayerFragment(),
                        ((HomeActivity) mContext).fragmentTag(13),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT
                );*/
            }
        });

        contest_filter_title1.setOnClickListener(v -> {
            contest_filter_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            contest_filter_layout2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));

            contest_filter_title1.setTextColor(getResources().getColor(R.color.white_pure));
            contest_filter_title2.setTextColor(getResources().getColor(R.color.font_color));

            //int pos=-1;
            for (int i = 0; i < contestFilterModelList.size(); i++) {
                if (contest_filter_title1.getText().toString().equalsIgnoreCase(contestFilterModelList.get(i).getTitle())) {
                    scrollToContest(i);
                    break;
                }
            }
            //scrollToContest(contestModelList.size()-1);
        });

        contest_filter_title2.setOnClickListener(v -> {
            contest_filter_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            contest_filter_layout1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));
            contest_filter_title2.setTextColor(getResources().getColor(R.color.white_pure));
            contest_filter_title1.setTextColor(getResources().getColor(R.color.font_color));

            for (int i = 0; i < contestFilterModelList.size(); i++) {
                if (contest_filter_title2.getText().toString().equalsIgnoreCase(contestFilterModelList.get(i).getTitle())) {
                    scrollToContest(i);
                    break;
                }
            }
            //scrollToContest(contestModelList.size()-2);
        });

        imgFilter.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                showFilterSheet();
            }
        });

        btnUnFillFilter.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, MoreContestListActivity.class)
                    .putExtra("league_id","0")
                    .putExtra("is_unfill",true)
            );
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (mSocket!=null) {
            mSocket.off("res");
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (mSocket!=null) {
            mSocket.off("res");
        }*/
    }

    private void showFilterSheet() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contest_filter_dialog, null);
        //ChipGroup chipsFilter = view.findViewById(R.id.chipsFilter);
        RecyclerView recyclerFilter = view.findViewById(R.id.recyclerFilter);

        contest_filter_layout2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));
        contest_filter_layout1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));
        contest_filter_title1.setTextColor(getResources().getColor(R.color.font_color));
        contest_filter_title2.setTextColor(getResources().getColor(R.color.font_color));

        /*filterAdapter = new ContestFilterAdapter(mContext, contestModelList, ContestListInnerFragment.this);
        match_header_filter.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        match_header_filter.setAdapter(filterAdapter);*/

        bottomSheetDialog = new BottomSheetDialog(mContext);
        //  bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }

        recyclerFilter.setLayoutManager(new GridLayoutManager(mContext, 2));
        MoreContestFilterAdapter adapter = new MoreContestFilterAdapter(contestFilterModelList, mContext, (position -> {
            if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
            }
            scrollToContest(position);
        }));
        recyclerFilter.setAdapter(adapter);

        /*for (int i =0 ;i<contestModelList.size();i++) {
            ContestModel modal = contestModelList.get(i);

            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
            mChip.setText(modal.getTitle());
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            final int pos=i;
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
           // mChip.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 2));
            mChip.setOnCheckedChangeListener((compoundButton, b) -> {
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                scrollToContest(pos);
            });
            chipsFilter.addView(mChip);
        }*/
    }

    public void getContests() {
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //LogUtil.e(TAG, "onSuccessResult Param: " + jsonObject + "\nurl: " + ApiManager.contestListV3);
        boolean show_dialog = swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing();
        HttpRestClient.postJSON(mContext, show_dialog, ApiManager.contestListV3, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult Contest: " + responseBody);
                lastUpdateTime=System.currentTimeMillis();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                offerModelList.clear();
                listTop.clear();

                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                  /*  List<ContestModel.ContestDatum> list = preferences.getFavList();
                    if (list!=null && list.size()>0){
                        ContestModel favConModal=new ContestModel();
                        favConModal.setId("0");
                        favConModal.setImage("fav");
                        favConModal.setIs_view_all("no");
                        favConModal.setTitle("Favorite Contest");
                        favConModal.setSubTitle("Ready for one more match?");
                        favConModal.setContestData(new ArrayList<>());
                        contestModelList.add(favConModal);
                        contestFilterModelList.add(favConModal);
                    }*/

                    contestModelList.clear();
                    contestFilterModelList.clear();

                    for (i = 0; i < array.length(); i++) {

                        try {
                            JSONObject data = array.getJSONObject(i);

                            ContestModel contestModel = gson.fromJson(data.toString(), ContestModel.class);

                            for (int j = 0; j < contestModel.getContestData().size(); j++) {

                                /*if (ConstantUtil.is_game_test)
                                    contestModel.getContestData().get(j).setDefault_bb_coins("95");*/
                                JSONObject contest = data.optJSONArray("contest_data").optJSONObject(j);

                                if (contest.optString("entry_fee").equalsIgnoreCase("49")) {
                                    LogUtil.e(TAG, "qwerty_cnt=> " + contest);
                                }
                                ContestModel.ContestDatum cModal = contestModel.getContestData().get(j);
                                LogUtil.e(TAG, "CONTESt=>" + contest);


                                ArrayList<PassModel> passList = new ArrayList<>();
                                if (contest.has("my_pass_data")) {
                                    JSONArray jsonArray = contest.optJSONArray("my_pass_data");
                                    LogUtil.e(TAG, "JJSon=>" + jsonArray);
                                    if (jsonArray != null) {
                                        for (int k = 0; k < jsonArray.length(); k++) {
                                            PassModel passModel = gson.fromJson(jsonArray.optJSONObject(k).toString(), PassModel.class);
                                            passList.add(passModel);
                                        }
                                    }
                                }
                                cModal.setPassModelArrayList(passList);

                                if (TextUtils.isEmpty(cModal.getOffer_date_text())) {
                                    if (cModal.getIs_pass().equalsIgnoreCase("yes") && cModal.getPassModelArrayList().size() > 0) {
                                        PassModel passModel = cModal.getPassModelArrayList().get(0);
                                        int countLeft = Integer.parseInt(passModel.getNoOfEntry()) - Integer.parseInt(passModel.getTotalJoinSpot());
                                        if (countLeft > 0) {
                                            JSONArray array1 = new JSONArray();
                                            for (int k = 0; k < Integer.parseInt(cModal.getMaxJoinTeam()); k++) {
                                                JSONObject object = new JSONObject();
                                                object.put("used_bonus", "");
                                                object.put("team_no", (k + 1) + "");
                                                object.put("discount_entry_fee", cModal.getEntryFee());
                                                array1.put(object);
                                            }
                                            if (array1.length() > 0) {
                                                cModal.setOffer_date_text(array1.toString());
                                            } else {
                                                cModal.setOffer_date_text("");
                                            }

                                        } else {
                                            cModal.setOffer_date_text("");
                                        }
                                    }
                                }

                                if (!TextUtils.isEmpty(cModal.getOffer_date_text())) {
                                    ArrayList<NewOfferModal> newOfferModals = new ArrayList<>();
                                    ArrayList<NewOfferModal> temnewOfferModals = new ArrayList<>();

                                    for (int k = 0; k < CustomUtil.convertInt(cModal.getMaxJoinTeam()); k++) {
                                        NewOfferModal newOfferModal = new NewOfferModal();
                                        newOfferModal.setDiscount_entry_fee("");
                                        newOfferModal.setEntry_fee(cModal.getEntryFee());
                                        newOfferModal.setTeam_no((k + 1) + "");
                                        newOfferModal.setUsed_bonus(cModal.getDefaultBonus());
                                        newOfferModals.add(newOfferModal);
                                        temnewOfferModals.add(newOfferModal);

                                    }
                                    cModal.setNewOfferList(newOfferModals);

                                    JSONArray offerDateArr = new JSONArray(cModal.getOffer_date_text());

                                    ArrayList<NewOfferModal> newOfferTempModals = new ArrayList<>();
                                    for (int k = 0; k < offerDateArr.length(); k++) {
                                        JSONObject obj = offerDateArr.getJSONObject(k);
                                        NewOfferModal mdl = gson.fromJson(obj.toString(), NewOfferModal.class);
                                        if (mdl != null && TextUtils.isEmpty(mdl.getDiscount_entry_fee())) {
                                            mdl.setEntry_fee(cModal.getEntryFee());
                                        }
                                        newOfferTempModals.add(mdl);
                                    }

                                    if (cModal.getPassModelArrayList().size() > 0) {
                                        PassModel passModel = cModal.getPassModelArrayList().get(0);
                                        int countLeft = Integer.parseInt(passModel.getNoOfEntry()) - Integer.parseInt(passModel.getTotalJoinSpot());
                                        for (int k = 0; k < newOfferTempModals.size(); k++) {
                                            NewOfferModal newOfferModal = newOfferTempModals.get(k);
                                            if (k <= countLeft - 1) {
                                                newOfferModal.setDiscount_entry_fee("0");
                                                newOfferModal.setPassId(passModel.getPassId());
                                            }
                                        }
                                    }

                                    cModal.setNewOfferTempList(newOfferTempModals);

                                    for (NewOfferModal newOfferModal : cModal.getNewOfferList()) {
                                        for (NewOfferModal temp : cModal.getNewOfferTempList()) {
                                            if (CustomUtil.convertInt(temp.getTeam_no()) == CustomUtil.convertInt(newOfferModal.getTeam_no())) {
                                                newOfferModal.setDiscount_entry_fee(temp.getDiscount_entry_fee());
                                                newOfferModal.setPassId(temp.getPassId());
                                                if (TextUtils.isEmpty(temp.getUsed_bonus())) {
                                                    temp.setUsed_bonus("0");
                                                }
                                                if (CustomUtil.convertInt(temp.getUsed_bonus()) > 0) {
                                                    newOfferModal.setUsed_bonus(temp.getUsed_bonus());
                                                }
                                            }
                                        }
                                    }

                                    if (CustomUtil.convertInt(cModal.getMyJoinedTeam()) > 0) {
                                        if (temnewOfferModals.size() > 0) {
                                            temnewOfferModals.subList(0, CustomUtil.convertInt(cModal.getMyJoinedTeam())).clear();
                                            cModal.setNewOfferRemovedList(temnewOfferModals);
                                        }
                                    } else {
                                        cModal.setNewOfferRemovedList(temnewOfferModals);
                                    }

                                    if (cModal.getPassModelArrayList().size() > 0) {
                                        PassModel passModel = cModal.getPassModelArrayList().get(0);
                                        int countLeft = Integer.parseInt(passModel.getNoOfEntry()) - Integer.parseInt(passModel.getTotalJoinSpot());
                                        for (int k = 0; k < temnewOfferModals.size(); k++) {
                                            NewOfferModal newOfferModal = temnewOfferModals.get(k);
                                            if (k <= countLeft - 1) {
                                                newOfferModal.setDiscount_entry_fee("0");
                                                newOfferModal.setPassId(passModel.getPassId());
                                            }
                                        }
                                        cModal.setNewOfferList(temnewOfferModals);
                                    }
                                } else {
                                    cModal.setNewOfferList(new ArrayList<>());
                                    cModal.setNewOfferRemovedList(new ArrayList<>());
                                    cModal.setNewOfferTempList(new ArrayList<>());
                                }
                            }

                            contestModelList.add(contestModel);
                            contestFilterModelList.add(contestModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONObject matchJSON = responseBody.optJSONObject("match_data");
                    if (matchJSON.length() > 0) {
                        MatchModel matchModel = preferences.getMatchModel();
                        matchModel.setMatchStartDate(matchJSON.optString("match_start_date"));
                        matchModel.setSafeMatchStartTime(matchJSON.optString("safe_match_start_time"));
                        matchModel.setRegularMatchStartTime(matchJSON.optString("regular_match_start_time"));
                        matchModel.setMatchType(matchJSON.optString("match_type"));
                        matchModel.setTeamAXi(matchJSON.optString("team_a_xi"));
                        matchModel.setTeamBXi(matchJSON.optString("team_b_xi"));
                        matchModel.setMatchDesc(matchJSON.optString("match_desc"));
                        // matchModel.setConDisplayType(contestType);
                        MyApp.getMyPreferences().setMatchModel(matchModel);
                        preferences = MyApp.getMyPreferences();
                        ((ContestListActivity) mContext).setTimer();
                    }

                    try {
                        JSONArray team_count_temp = responseBody.optJSONArray("team_count");
                        LogUtil.e(TAG, "onSuccessResult team_count_temp: " + responseBody.optJSONArray("team_count"));
                        if (team_count_temp != null && team_count_temp.length() > 0) {
                            team_count = team_count_temp.getJSONObject(0);
                            preferences.setPref("total_team", CustomUtil.convertInt(team_count.optString("total_team")));
                            if (preferences.getPlayerModel() != null && team_count != null &&
                                    !team_count.optString("total_team").equals("") &&
                                    preferences.getPlayerModel().size() != CustomUtil.convertInt(team_count.optString("total_team"))) {

                                getTempTeamData();
                            }
                        } else {
                            preferences.setPref("total_team", 0);
                        }
                        JSONArray join_count_temp = responseBody.optJSONArray("join_count");
                        if (join_count_temp != null && join_count_temp.length() > 0) {
                            join_count = join_count_temp.getJSONObject(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                 /*   if (!lineupCount.equalsIgnoreCase(matchJSON.optString("lineup_count"))) {
                        getTeamData();
                    }else {
                        setData();
                    }*/
                    if (lineupCount.equalsIgnoreCase(matchJSON.optString("lineup_count"))) {
                        if (preferences.getPlayerModel() == null) {
                            getTempTeamData();
                        }
                    } else {
                        getTeamData();
                    }
                    lineupCount = matchJSON.optString("lineup_count");

                    setData();

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTeamData() {
        final ArrayList<PlayerModel> playerModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.MATCH_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: TAG" + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    for (i = 0; i < array.length(); i++) {
                        try {
                            PlayerModel playerModel = gson.fromJson(array.getJSONObject(i).toString(), PlayerModel.class);
                            playerModels.add(playerModel);
                            preferences.setPlayerList(playerModels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    getTempTeamData();
                    // if (preferences.getPlayerModel() == null) {
                    //setData();
                    // }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTempTeamData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: TAG1" + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    getTempTeamDetailData(responseBody.optJSONArray("data"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getTempTeamDetailData(final JSONArray data) {
        final List<PlayerListModel> playerListModels = new ArrayList<>();
        ((ContestListActivity)mContext).playerListModels = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_DETAIL_LIST: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");
                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0, lineup_count = 0, cr = 0;
                        float credit_point = 0;
                        JSONObject main = data.optJSONObject(j);

                        PlayerListModel playerListModel = new PlayerListModel();
                        playerListModel.setId(main.optString("id"));
                        playerListModel.setTempTeamName(main.optString("temp_team_name"));
                        playerListModel.setMatchId(main.optString("match_id"));
                        playerListModel.setContestDisplayTypeId(main.optString("contest_display_type_id"));
                        playerListModel.setUserId(main.optString("user_id"));
                        playerListModel.setTotalPoint(main.optString("total_point"));

                        List<PlayerModel> playerDetails = new ArrayList<>();
                        for (i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);

                            PlayerModel playerDetail = gson.fromJson(object.toString(), PlayerModel.class);

                            if (playerDetail.getTempTeamId().equals(playerListModel.getId())) {

                                credit_point += CustomUtil.convertFloat(playerDetail.getPlayerRank());
                                if (preferences.getMatchModel().getSportId().equals("1")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "wk":
                                            wk += 1;
                                            break;
                                        case "bat":
                                            bat += 1;
                                            break;
                                        case "ar":
                                            ar += 1;
                                            break;
                                        case "bowl":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("2")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "mid":
                                            ar += 1;
                                            break;
                                        case "for":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("4")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "pg":
                                            wk += 1;
                                            break;
                                        case "sg":
                                            bat += 1;
                                            break;
                                        case "sf":
                                            ar += 1;
                                            break;
                                        case "pf":
                                            bowl += 1;
                                            break;
                                        case "cr":
                                            cr += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("3")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "of":
                                            wk += 1;
                                            break;
                                        case "if":
                                            bat += 1;
                                            break;
                                        case "pit":
                                            ar += 1;
                                            break;
                                        case "cat":
                                            bowl += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("6")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "gk":
                                            wk += 1;
                                            break;
                                        case "def":
                                            bat += 1;
                                            break;
                                        case "fwd":
                                            ar += 1;
                                            break;
                                    }
                                }
                                if (preferences.getMatchModel().getSportId().equals("7")) {
                                    switch (playerDetail.getPlayerType().toLowerCase()) {
                                        case "def":
                                            wk += 1;
                                            break;
                                        case "ar":
                                            bat += 1;
                                            break;
                                        case "raid":
                                            ar += 1;
                                            break;
                                    }
                                }
                               /* switch (playerDetail.getPlayerType().toLowerCase()) {
                                    case "wk":
                                        wk += 1;
                                        break;
                                    case "bat":
                                        bat += 1;
                                        break;
                                    case "ar":
                                        ar += 1;
                                        break;
                                    case "bowl":
                                        bowl += 1;
                                        break;
                                }*/
                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam1())) {
                                    team1 += 1;
                                }
                                if (playerDetail.getTeamId().equals(preferences.getMatchModel().getTeam2())) {
                                    team2 += 1;
                                }
                                if (playerDetail.getIsCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setC_player_name(playerDetail.getPlayerName());
                                    playerListModel.setC_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setC_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setC_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setC_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setC_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setC_player_type(playerDetail.getPlayerType());
                                    playerListModel.setCTeam_id(playerDetail.getTeamId());
                                }
                                if (playerDetail.getIsWiseCaptain().equalsIgnoreCase("yes")) {
                                    playerListModel.setVc_player_name(playerDetail.getPlayerName());
                                    playerListModel.setVc_player_sname(playerDetail.getPlayerSname());
                                    playerListModel.setVc_player_avg_point(playerDetail.getPlayerAvgPoint());
                                    playerListModel.setVc_player_rank(playerDetail.getPlayerRank());
                                    playerListModel.setVc_player_img(playerDetail.getPlayerImage());
                                    playerListModel.setVc_player_xi(playerDetail.getPlayingXi());
                                    playerListModel.setVc_player_type(playerDetail.getPlayerType());
                                    playerListModel.setVCTeam_id(playerDetail.getTeamId());
                                }

                                if (preferences.getPlayerList() != null) {
                                    for (PlayerModel playerModel : preferences.getPlayerList()) {
                                        if (playerModel.getPlayerId().equalsIgnoreCase(playerDetail.getPlayerId())) {
                                            playerDetail.setPlayingXi(playerModel.getPlayingXi());
                                            playerDetail.setOther_text(playerModel.getOther_text());
                                            playerDetail.setOther_text2(playerModel.getOther_text2());
                                            break;
                                        }
                                    }
                                }

                                if (preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") ||
                                        preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")) {

                                    if (playerDetail.getPlayingXi().toLowerCase().equals("no") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")) {
                                        lineup_count++;
                                        LogUtil.e(TAG, "onSuccessResult: ABC " + playerDetail.getPlayerName());
                                    }
                                }
                                playerDetails.add(playerDetail);
                            }
                        }
                        playerListModel.setPlayerDetailList(playerDetails);
                        playerListModel.setTeam1_count("" + team1);
                        playerListModel.setTeam2_count("" + team2);
                        playerListModel.setCreditTotal("" + credit_point);
                        playerListModel.setWk_count("" + wk);
                        playerListModel.setBat_count("" + bat);
                        playerListModel.setAr_count("" + ar);
                        playerListModel.setBowl_count("" + bowl);
                        playerListModel.setCr_count("" + cr);
                        playerListModel.setIsJoined("No");
                        playerListModel.setIsSelected("No");
                        LogUtil.e(TAG, "onSuccessResult: " + lineup_count);
                        playerListModel.setLineup_count("" + lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());
                        playerListModels.add(playerListModel);
                    }
                    preferences.setPlayerModel(playerListModels);
                    ((ContestListActivity) mContext).playerListModels.addAll(playerListModels);
                    //joinDirectContest();

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public class prizePoolUp implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Integer.compare(CustomUtil.convertInt(o1.getDistributeAmount()), CustomUtil.convertInt(o2.getDistributeAmount()));
        }
    }

    public class EntryFeeDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getEntryFee()), CustomUtil.convertFloat(o2.getEntryFee()));
        }
    }

    private void setData() {
        boolean isSameConAvail = false;

        //List<ContestModel.ContestDatum> list = preferences.getFavList();
        ArrayList<String> favArr = new ArrayList<>(Arrays.asList(preferences.getUserModel().getFavoriteLeague().split(",")));

        if (favArr != null && favArr.size() > 0) {

            ContestModel favConModal = new ContestModel();
            favConModal.setId("0");
            favConModal.setImage("fav");
            favConModal.setIs_view_all("no");
            favConModal.setTitle("Favorite Contest");
            favConModal.setSubTitle("Ready for one more match?");
            favConModal.setContestData(new ArrayList<>());
            contestModelList.add(0, favConModal);
            contestFilterModelList.add(0, favConModal);

            for (ContestModel model : CustomUtil.emptyIfNull(contestModelList)) {
                for (String fContest : favArr) {
                    for (int j = 0; j < model.getContestData().size(); j++) {
                        ContestModel.ContestDatum contest = model.getContestData().get(j);
                        if (fContest.trim().equalsIgnoreCase(contest.getConTplId().trim())) {
                            isSameConAvail = true;
                            List<ContestModel.ContestDatum> addContest = contestModelList.get(0).getContestData();
                            contest.setContestFavorite(true);
                            addContest.add(contest);
                        }
                    }
                }
            }
        }

        if (!isSameConAvail) {
            if (contestModelList.get(0).getTitle().equalsIgnoreCase("Favorite Contest")) {
                contestModelList.remove(0);
                contestFilterModelList.remove(0);
            }
        }

        if (join_count != null && !join_count.optString("join_contest_count").equals("")) {
            ((ContestListActivity) mContext).join_count = join_count;
            ((ContestListActivity) mContext).tabLayout.getTabAt(1).setText("My Contests (" + join_count.optString("join_contest_count") + ")");
        } else {
            ((ContestListActivity) mContext).join_count = null;
            ((ContestListActivity) mContext).tabLayout.getTabAt(1).setText("My Contest");
        }

        if (team_count != null && !team_count.optString("total_team").equals("")) {
            ((ContestListActivity) mContext).team_count = team_count;
            ((ContestListActivity) mContext).tabLayout.getTabAt(2).setText("My Teams (" + team_count.optString("total_team") + ")");
        } else {
            ((ContestListActivity) mContext).team_count = null;
            ((ContestListActivity) mContext).tabLayout.getTabAt(2).setText("My Team");
        }

        contestModelList.add(new ContestModel(1));

        adapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(selectedContestId)) {
            for (int i = 0; i < contestModelList.size(); i++) {
                ContestModel cont = contestModelList.get(i);
                for (ContestModel.ContestDatum contest : CustomUtil.emptyIfNull(cont.getContestData())) {
                    if (selectedContestId.equals(contest.getId())) {
                        selectedContestId = "";
                        ((ContestListActivity) mContext).linkAndJoin(contest, i);
                    }
                }
            }
        }

    }

    public void scrollToContest(int adapterPosition) {
        if (listTop.size() > 0) {
            adapterPosition = adapterPosition + 1;
        }
        RecyclerView.LayoutManager layoutManager = contest_list.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            // Scroll to item and make it the first visible item of the list.
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(adapterPosition, 0);
        } else {
            contest_list.smoothScrollToPosition(adapterPosition);
        }
    }

}