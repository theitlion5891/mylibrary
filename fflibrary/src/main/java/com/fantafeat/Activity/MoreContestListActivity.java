package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.fantafeat.Adapter.ContestFilterAdapter;
import com.fantafeat.Adapter.ContestListAdapter;
import com.fantafeat.Adapter.ContestQuantityAdapter;
import com.fantafeat.Fragment.ContestListInnerFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.ContestQuantityModel;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MoreContestListActivity extends BaseActivity {

    private ImageView toolbar_back, toolbar_wallet,btnUnFillFilter;
    private TextView match_title, timer;
    private MatchModel selected_match;
    private long diff;
    private CountDownTimer countDownTimer;
    private BottomSheetDialog bottomSheetDialog = null;

    private String league_id = "";
    private SwipeRefreshLayout contest_pull_refresh;
    private RecyclerView contest_main_list, recyclerFilter;
    private List<ContestModel> contestFilterModelList;
    public ContestModel.ContestDatum list = new ContestModel.ContestDatum();
    public List<PlayerListModel> playerListModels = new ArrayList<>();
    private boolean isEntryUp = true, isPool = false, isWinner = false, isSports = false;
    private int entryFilter=1,poolFilter=0,winnerFilter=0,sportFilter=0;
    private List<ContestModel.ContestDatum> contestMainModelList;
    private List<ContestModel.ContestDatum> contestTempMainModelList;
    private List<ContestModel> contestModelList;
    private String contestType = "1", lineupCount = "";

    private LinearLayout nodata,create_team;
    private ContestListAdapter adapterMain;
    private ContestFilterAdapter filterAdapter;
    public JSONObject team_count, join_count;
    float use_deposit = 0;
    float use_transfer = 0;
    float use_winning = 0;
    float use_donation_deposit = 0;
    float useBonus = 0;
    float useCoin = 0;
    float amtToAdd = 0;
    ContestModel.ContestDatum contestData;

    private String selectedState = "";
    private Spinner spinState;
    private ArrayList<String> cityName, cityId;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private String selectedGender = "Select Gender";
    private long lastUpdateTime=0;
    private boolean isUnfillEnable=false;
    private List<ContestModel.ContestDatum> contestFilteredList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_contest_list);

        toolbar_back = findViewById(R.id.toolbar_back);
        match_title = findViewById(R.id.match_title);
        timer = findViewById(R.id.timer);
        toolbar_wallet = findViewById(R.id.toolbar_wallet);
        toolbar_wallet.setVisibility(View.INVISIBLE);
        contest_pull_refresh = findViewById(R.id.contest_pull_refresh);
        contest_main_list = findViewById(R.id.contest_main_list);
        recyclerFilter = findViewById(R.id.recyclerFilter);
        nodata = findViewById(R.id.nodata);
        create_team = findViewById(R.id.create_team);
        btnUnFillFilter = findViewById(R.id.btnUnFillFilter);

        league_id = getIntent().getStringExtra("league_id");


        selected_match = preferences.getMatchModel();

        match_title.setText(selected_match.getTeam1Sname() + " vs " + selected_match.getTeam2Sname());

        contestFilterModelList = new ArrayList<>();
        contestMainModelList = new ArrayList<>();
        contestTempMainModelList = new ArrayList<>();
        contestModelList = new ArrayList<>();

        //setTimer();

        recyclerFilter.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        filterAdapter = new ContestFilterAdapter(mContext, contestFilterModelList);
        recyclerFilter.setAdapter(filterAdapter);

        adapterMain = new ContestListAdapter(mContext, contestMainModelList, gson, 0, false);
        contest_main_list.setLayoutManager(new LinearLayoutManager(mContext));
        contest_main_list.setAdapter(adapterMain);

        setFilterName();

        initClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getContestData();
        if (preferences.getPrefBoolean("join_contest")) {
            preferences.setPref("join_contest", false);
            Log.e(TAG, "onResume: ");
            String contest_pass_data=preferences.getPrefString("contest_pass_data");
            if (TextUtils.isEmpty(contest_pass_data)){
                contest_pass_data="";
            }
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            String model = gson.toJson(((MoreContestListActivity) mContext).list);
            intent.putExtra("model", contest_pass_data);//model
            preferences.setPref("contest_pass_data", "");
            mContext.startActivity(intent);
        }
    }

    private void setFilterName() {
        Log.e(TAG, "setFilterName: ");
        ContestModel model = new ContestModel();
        model.setId("1");
        model.setTitle("Entry ↑");
        model.setImage("");
        model.setContestData(null);
        model.setSubTitle("");
        contestFilterModelList.add(model);

        model = new ContestModel();
        model.setId("2");
        model.setTitle("Spots ↓");
        model.setImage("");
        model.setContestData(null);
        model.setSubTitle("");
        contestFilterModelList.add(model);

        model = new ContestModel();
        model.setId("3");
        model.setTitle("Prize pool ↓");
        model.setImage("");
        model.setContestData(null);
        model.setSubTitle("");
        contestFilterModelList.add(model);

        model = new ContestModel();
        model.setId("4");
        model.setTitle("%Winners ↓");
        model.setImage("");
        model.setContestData(null);
        model.setSubTitle("");
        contestFilterModelList.add(model);


    }

    private void selectFilter(ContestModel model,boolean isBoolChange) {

        //contestMainModelList.clear();
        //adapterMain.notifyDataSetChanged();
        LogUtil.e("resp","entryFilter: "+entryFilter);
        if (model.getId().equalsIgnoreCase("1")) {
            if (entryFilter==0) {//isEntryUp
                /*if (isBoolChange) {

                }*/
                entryFilter = 1;
                model.setTitle("Entry ↑");
                Collections.sort(contestMainModelList, new EntryFeeDown());

            }
            else if (entryFilter==1) {

                entryFilter = 0;
                model.setTitle("Entry ↓");
                Collections.sort(contestMainModelList, new EntryFeeUp());
            }
            /*else if (entryFilter==-1) {//isEntryUp
                if (isBoolChange) {
                    entryFilter = 1;
                    model.setTitle("Entry ↑");
                }
                Collections.sort(contestMainModelList, new EntryFeeDown());

            }*/
        }
        else if (model.getId().equalsIgnoreCase("2")) {
            if (sportFilter==0) {
                //if (isBoolChange) {
                    sportFilter = 1;
                    model.setTitle("Spots ↑");
                //}
                Collections.sort(contestMainModelList, new SportsDown());
            }
            else if (sportFilter==1){
                //if (isBoolChange) {
                    sportFilter = 0;
                    model.setTitle("Spots ↓");
                //}
                Collections.sort(contestMainModelList, new SportsUp());
            }
            /*else if (sportFilter==-1) {
                if (isBoolChange) {
                    sportFilter = 1;
                    model.setTitle("Spots ↑");
                }
                Collections.sort(contestMainModelList, new SportsDown());
            }*/
        }
        else if (model.getId().equalsIgnoreCase("3")) {
            if (poolFilter==0) {
                //if (isBoolChange) {
                    poolFilter = 1;
                    model.setTitle("Prize pool ↑");
                //}
                Collections.sort(contestMainModelList, new prizePoolDown());
            }
            else if (poolFilter==1) {
                //if (isBoolChange) {
                    poolFilter = 0;
                    model.setTitle("Prize pool ↓");
                //}
                Collections.sort(contestMainModelList, new prizePoolUp());
            }
         /*   else  if (poolFilter==-1) {
                if (isBoolChange) {
                    poolFilter = 1;
                    model.setTitle("Prize pool ↑");
                }
                Collections.sort(contestMainModelList, new prizePoolDown());
            }*/
        }
        else if (model.getId().equalsIgnoreCase("4")) {
            if (winnerFilter==0) {
                //if (isBoolChange) {
                    winnerFilter = 1;
                    model.setTitle("%Winners ↑");
                //}
                Collections.sort(contestMainModelList, new WinnerDown());
            }
            else if (winnerFilter==1) {
                //if (isBoolChange) {
                    winnerFilter = 0;
                    model.setTitle("%Winners ↓");
                //}
                Collections.sort(contestMainModelList, new WinnerUp());
            }
            /*else if (winnerFilter==-1) {
                if (isBoolChange) {
                    winnerFilter = 1;
                    model.setTitle("%Winners ↑");
                }
                Collections.sort(contestMainModelList, new WinnerDown());
            }*/
        }
        /*else if (model.getId().equalsIgnoreCase("9")){

        }*/

        adapterMain.notifyDataSetChanged();
        filterAdapter.notifyDataSetChanged();

        LogUtil.e("resp","entryFilter: "+entryFilter);

    }

    private void selectFilterWithoutChange(ContestModel model) {
        if (model.getId().equalsIgnoreCase("1")) {
            if (entryFilter==0) {//isEntryUp
                // entryFilter=1;
                Collections.sort(contestMainModelList, new EntryFeeUp());
                //model.setTitle("Entry ↑");

            }
            else if (entryFilter==1) {
                //entryFilter=0;
                Collections.sort(contestMainModelList, new EntryFeeDown());
                //model.setTitle("Entry ↓");
            }
               /* else if (entryFilter==-1) {//isEntryUp
                    entryFilter=1;
                    Collections.sort(contestMainModelList, new EntryFeeDown());
                    model.setTitle("Entry ↑");

                }*/
        }
        else if (model.getId().equalsIgnoreCase("2")) {
            if (sportFilter==0) {
                //sportFilter=1;
                Collections.sort(contestMainModelList, new SportsUp());
                //model.setTitle("Spots ↑");
            } else if (sportFilter==1){
                //sportFilter=0;
                Collections.sort(contestMainModelList, new SportsDown());
                // model.setTitle("Spots ↓");
            }
                /*else if (sportFilter==-1) {
                    sportFilter=1;
                    Collections.sort(contestMainModelList, new SportsDown());
                    model.setTitle("Spots ↑");
                }*/
        }
        else if (model.getId().equalsIgnoreCase("3")) {
            if (poolFilter==0) {
                //poolFilter=1;
                Collections.sort(contestMainModelList, new prizePoolUp());
                //model.setTitle("Prize pool ↑");
            }
            else if (poolFilter==1) {
                //poolFilter=0;
                Collections.sort(contestMainModelList, new prizePoolDown());
                //model.setTitle("Prize pool ↓");
            }
                /*else  if (poolFilter==-1) {
                    poolFilter=1;
                    Collections.sort(contestMainModelList, new prizePoolDown());
                    model.setTitle("Prize pool ↑");
                }*/
        }
        else if (model.getId().equalsIgnoreCase("4")) {
            if (winnerFilter==0) {
                //winnerFilter=1;
                Collections.sort(contestMainModelList, new WinnerUp());
                // model.setTitle("%Winners ↑");
            }
            else if (winnerFilter==1) {
                //winnerFilter=0;
                Collections.sort(contestMainModelList, new WinnerDown());
                //model.setTitle("%Winners ↓");
            }
                /*else if (winnerFilter==-1) {
                    winnerFilter=1;
                    Collections.sort(contestMainModelList, new WinnerDown());
                    model.setTitle("%Winners ↑");
                }*/
        }
        adapterMain.notifyDataSetChanged();
        checkData();
    }

    private void getContestData() {

        //  offerModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("con_type_id", league_id);
            LogUtil.e(TAG, "onSuccessResult: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isShow = true;
        if (contest_pull_refresh != null && contest_pull_refresh.isRefreshing()) {
            contest_main_list.getRecycledViewPool().clear();
            isShow = false;
        }

        HttpRestClient.postJSON(mContext, isShow, ApiManager.contestListWithTypeId, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                lastUpdateTime=System.currentTimeMillis();
                LogUtil.e("resp", "Contest More: " + responseBody);

                if (contest_pull_refresh != null && contest_pull_refresh.isRefreshing()) {
                    contest_pull_refresh.setRefreshing(false);
                }

                contestMainModelList.clear();
                contestTempMainModelList.clear();
                contestModelList.clear();
                contestFilteredList.clear();

                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;

                    for (i = 0; i < array.length(); i++) {
                        try {
                            JSONObject data = array.getJSONObject(i);
                            ContestModel contestModel = gson.fromJson(data.toString(), ContestModel.class);

                            for (int j = 0; j < contestModel.getContestData().size(); j++) {

                                JSONObject contest = data.optJSONArray("contest_data").optJSONObject(j);
                                ContestModel.ContestDatum cModal = contestModel.getContestData().get(j);
                               // LogUtil.e(TAG, "CCONTest" + contest);

                                ArrayList<PassModel> passList = new ArrayList<>();
                                if (contest.has("my_pass_data")) {
                                    JSONArray jsonArray = contest.optJSONArray("my_pass_data");
                                   // LogUtil.e(TAG, "JJSon=>" + jsonArray);
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

                                if (CustomUtil.convertFloat(cModal.getDistributeAmount())>0) {
                                    contestMainModelList.add(cModal);
                                    contestFilteredList.add(cModal);
                                }
                            }

                            //contestModelList.add(contestModel);

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
                        setTimer();
                    }

                    try {
                        JSONArray team_count_temp = responseBody.optJSONArray("team_count");
                        LogUtil.e(TAG, "onSuccessResult team_count_temp: " + team_count_temp);
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

                    if (lineupCount.equalsIgnoreCase(matchJSON.optString("lineup_count"))) {
                        if (preferences.getPlayerModel() == null) {
                            getTempTeamData();
                        }
                    }
                    else {
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
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
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
                    // if (preferences.getPlayerModel() == null) {
                    getTempTeamData();
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
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
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
        final List<PlayerListModel> playerListModel1 = new ArrayList<>();
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
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
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
                        playerListModel1.add(playerListModel);
                    }
                    preferences.setPlayerModel(playerListModel1);
                    playerListModels = playerListModel1;
                    //joinDirectContest();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
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

        getContestData();

    }

    private void setData() {

        List<ContestModel.ContestDatum> list = preferences.getFavList();
        if (list != null && list.size() > 0) {
            for (ContestModel.ContestDatum contest : CustomUtil.emptyIfNull(contestMainModelList)) {
                for (ContestModel.ContestDatum fContest : list) {
                    if (fContest.getConDisplayType().trim().equalsIgnoreCase(contest.getConDisplayType().trim()) &&
                            fContest.getConTplId().trim().equalsIgnoreCase(contest.getConTplId().trim()) &&
                            fContest.getConTypeId().trim().equalsIgnoreCase(contest.getConTypeId().trim()) &&
                            fContest.getConTypeName().trim().equalsIgnoreCase(contest.getConTypeName().trim()) &&
                            fContest.getDistributeAmount().trim().equalsIgnoreCase(contest.getDistributeAmount().trim()) &&
                            fContest.getEntryFee().trim().equalsIgnoreCase(contest.getEntryFee().trim())
                    ) {
                        contest.setContestFavorite(true);
                    }
                }
            }
        }

        adapterMain.updateList(contestMainModelList);

        filterAdapter.updateList(contestFilterModelList);

       /* if (entryFilter==0) //isEntryUp
            entryFilter=1;
        if (entryFilter==1) //isEntryUp
            entryFilter=-1;
        if (entryFilter==-1) //isEntryUp
            entryFilter=1;*/

        if (contestFilterModelList.size()>0) {
           // ContestModel model=contestFilterModelList.get(filterAdapter.selected);

            /*if (model.getId().equalsIgnoreCase("1")) {
                if (entryFilter==0) {//isEntryUp
                    entryFilter=1;
                    Collections.sort(contestMainModelList, new EntryFeeDown());
                    model.setTitle("Entry ↑");

                }
                else if (entryFilter==1) {
                    entryFilter=-1;
                    Collections.sort(contestMainModelList, new EntryFeeUp());
                    model.setTitle("Entry ↓");
                }
                else if (entryFilter==-1) {//isEntryUp
                    entryFilter=1;
                    Collections.sort(contestMainModelList, new EntryFeeDown());
                    model.setTitle("Entry ↑");

                }
            }
            else if (model.getId().equalsIgnoreCase("2")) {
                if (sportFilter==0) {
                    sportFilter=1;
                    Collections.sort(contestMainModelList, new SportsDown());
                    model.setTitle("Spots ↑");
                } else if (sportFilter==1){
                    sportFilter=-1;
                    Collections.sort(contestMainModelList, new SportsUp());
                    model.setTitle("Spots ↓");
                }else if (sportFilter==-1) {
                    sportFilter=1;
                    Collections.sort(contestMainModelList, new SportsDown());
                    model.setTitle("Spots ↑");
                }
            }
            else if (model.getId().equalsIgnoreCase("3")) {
                if (poolFilter==0) {
                    poolFilter=1;
                    Collections.sort(contestMainModelList, new prizePoolDown());
                    model.setTitle("Prize pool ↑");
                } else if (poolFilter==1) {
                    poolFilter=-1;
                    Collections.sort(contestMainModelList, new prizePoolUp());
                    model.setTitle("Prize pool ↓");
                }else  if (poolFilter==-1) {
                    poolFilter=1;
                    Collections.sort(contestMainModelList, new prizePoolDown());
                    model.setTitle("Prize pool ↑");
                }
            }
            else if (model.getId().equalsIgnoreCase("4")) {
                if (winnerFilter==0) {
                    winnerFilter=1;
                    Collections.sort(contestMainModelList, new WinnerDown());
                    model.setTitle("%Winners ↑");
                }
                else if (winnerFilter==1) {
                    winnerFilter=-1;
                    Collections.sort(contestMainModelList, new WinnerUp());
                    model.setTitle("%Winners ↓");
                }
                else if (winnerFilter==-1) {
                    winnerFilter=1;
                    Collections.sort(contestMainModelList, new WinnerDown());
                    model.setTitle("%Winners ↑");
                }
            }*/

            if (getIntent().hasExtra("is_unfill") || isUnfillEnable){
                isUnfillEnable=false;
                btnUnFillFilter.performClick();
            }
            else {
                ContestModel model=contestFilterModelList.get(filterAdapter.selected);
                selectFilterWithoutChange(model);
            }
            //selectFilter(model, false);
        }

        checkData();
    }

    public void confirmTeam(ContestModel.ContestDatum contestData) {

        if (TextUtils.isEmpty(preferences.getUserModel().getEmailId())) {
            showBasicDetailDialog(contestData);
            return;
        }

        this.contestData = null;
        //this.mainPosition=-1;

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
            //this.mainPosition=mainPosition;

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
            edtConQty = view.findViewById(R.id.edtConQty);
            edtConQty.setText("1");

            if (contestData.getConPlayerEntry().equalsIgnoreCase("Single") &&
                    contestData.getAutoCreate().equalsIgnoreCase("yes")
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
                            }

                            LogUtil.e(TAG, "onClick: deposit_bal" + CustomUtil.getFormater("00.00").format(use_deposit) +
                                    "\n  Win_bal" + CustomUtil.getFormater("00.00").format(use_winning) +
                                    "\n  Donation useBonus" + CustomUtil.getFormater("00.00").format(useBonus));

                            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
                            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));
                        } else {
                            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
                            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            } else {
                layMultiContest.setVisibility(View.GONE);
            }

            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
            //join_use_borrowed.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_transfer));
            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
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
                        } else if (TextUtils.isEmpty(edtConQty.getText().toString().trim())) {
                            CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                        } else if (qty <= 0) {
                            CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                        } else if (qty > 500) {
                            CustomUtil.showToast(mContext, "Maximum 500 Quantity allow");
                        } else {
                            bottomSheetDialog.dismiss();

                            contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                            joinContest(contestData);
                        }
                    }
                }
            });

            bottomSheetDialog = new BottomSheetDialog(mContext);

            bottomSheetDialog.setContentView(view);
            //((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
            if (bottomSheetDialog != null && !bottomSheetDialog.isShowing())
                bottomSheetDialog.show();
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
        LogUtil.e("resp", "usableBBCoins: " + usableBBCoins);

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

    private void showBasicDetailDialog(ContestModel.ContestDatum contestData) {
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
        toolbar_back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
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

        /*if (TextUtils.isEmpty(user.getEmailId())){
            email.setEnabled(true);
        }else {
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
                callFirstApi(strName, strEmail, strTeam, db, bottomSheetDialog, contestData);
            }

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
                              ContestModel.ContestDatum contestData) {
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

                    confirmTeam(contestData);

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                    LogUtil.e(TAG, "joinSucess");
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

    private void joinContest(ContestModel.ContestDatum contestData) {

        if (playerListModels != null && playerListModels.size() > 0) {
            PlayerListModel playerListModel = playerListModels.get(0);

            DecimalFormat format = CustomUtil.getFormater("0.0000");
            // format.setRoundingMode(RoundingMode.HALF_UP);
            //temp_team_id,match_id,con_display_type,user_id,contest_id,team_name,deposit_bal,bonus_bal,win_bal,transfer_bal,donation_bal
            final JSONObject jsonObject = new JSONObject();
            final JSONObject childObj = new JSONObject();
            final JSONArray array = new JSONArray();
            //JSONObject passData = new JSONObject();
            try {

                //jsonObject.put("temp_team_id", playerListModel.getId());
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
                jsonObject.put("transfer_bal", format.format(use_transfer));
                jsonObject.put("donation_bal", format.format(use_donation_deposit));
                jsonObject.put("pass_id", "");
                jsonObject.put("applied_pass_count", "0");
                childObj.put("team_no", "1");

                childObj.put("pass_id", "");
                childObj.put("deposit_bal", format.format(use_deposit));
                childObj.put("win_bal", format.format(use_winning));
                childObj.put("bonus_bal", format.format(useBonus));
                childObj.put("ff_coins_bal", format.format(useCoin));
                childObj.put("transfer_bal", format.format(use_transfer));
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


            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtil.d("resp", jsonObject.toString());

            HttpRestClient.postJSON(mContext, true, ApiManager.JOIN_CONTEST2MultiJoin, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "onSuccessResult:more " + responseBody.toString());

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
                        // float transfer_bal = CustomUtil.convertFloat(user.getTransferBal()) - use_transfer;
                        float bonus_bal = CustomUtil.convertFloat(user.getBonusBal()) - useBonus;
                        float winning_bal = CustomUtil.convertFloat(user.getWinBal()) - use_winning;
                        float coin_bal = CustomUtil.convertFloat(user.getFf_coin()) - useCoin;

                        user.setDepositBal(String.valueOf(deposit_bal));
                        //  user.setTransferBal(String.valueOf(transfer_bal));
                        user.setBonusBal(String.valueOf(bonus_bal));
                        user.setWinBal(String.valueOf(winning_bal));
                        user.setFf_coin(String.valueOf(coin_bal));

                        float total;
                        if (ConstantUtil.is_bonus_show) {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) +
                                    //  CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getBonusBal());

                        } else {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }
                      /*  float total = CustomUtil.convertFloat(user.getDepositBal()) +
                                CustomUtil.convertFloat(user.getWinBal()) +
                                CustomUtil.convertFloat(user.getFf_coin()) +
                                //  CustomUtil.convertFloat(user.getTransferBal()) +
                                CustomUtil.convertFloat(user.getBonusBal());*/

                        user.setTotal_balance(total);

                        preferences.setUserModel(user);
                        CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                        LogUtil.e(TAG, "MED_morecontest_SAFE=>contest join");
                        getContestData();
                    } else {
                        String message = responseBody.optString("msg");
                        CustomUtil.showTopSneakWarning(mContext, message);
                        LogUtil.e(TAG, "JOIN_CONTEST2MultiJoinMoreContest contest join");
                        if (responseBody.has("new_con_id")) {
                            final String new_con_id = responseBody.optString("new_con_id");
                            if (new_con_id != null && !new_con_id.equals("null") && !new_con_id.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MoreContestListActivity.this);
                                builder.setTitle("The Contest is already full!!");//Almost there! That League filled up
                                builder.setMessage("Don't worry, we have same contest for you! join this contest.");//No worries, join this League instead! it is exactly the same type
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        contestData.setId(new_con_id);
                                        joinContest(contestData);
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
                            LogUtil.e(TAG, "MEASAG=contest join");
                        }
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        } else {
            Gson gson = new Gson();
            String model = gson.toJson(contestData);
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            intent.putExtra("model", model);
            startActivity(intent);
        }

    }

    private void checkData() {
        if (contestMainModelList.size() > 0) {
           // contest_pull_refresh.setVisibility(View.VISIBLE);
            contest_main_list.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        } else {
           // contest_pull_refresh.setVisibility(View.GONE);
            contest_main_list.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void initClick() {
        toolbar_back.setOnClickListener(view -> {
            finish();
        });

        create_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                intent.putExtra(PrefConstant.TEAMCREATETYPE, 0);
                startActivity(intent);
            }
        });

        btnUnFillFilter.setOnClickListener(v -> {

            isUnfillEnable=!isUnfillEnable;

            if (isUnfillEnable){
                btnUnFillFilter.setImageResource(R.drawable.unfilled_contest);
            }
            else {
                btnUnFillFilter.setImageResource(R.drawable.fill_contest);
            }

            contestMainModelList.clear();
            if (isUnfillEnable){
                for (ContestModel.ContestDatum contest:contestFilteredList){
                    if (CustomUtil.convertFloat(contest.getJoinedTeams())>0){
                        contestMainModelList.add(contest);
                    }
                }
            }
            else {
                contestMainModelList.addAll(contestFilteredList);
            }

            ContestModel model=contestFilterModelList.get(filterAdapter.selected);

            selectFilterWithoutChange(model);

            checkData();

            //selectFilter(contestFilterModelList.get(filterAdapter.selected),false);

        });
       // contest_pull_refresh.setOnRefreshListener(this::getContestData);

        contest_pull_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ((System.currentTimeMillis()-lastUpdateTime)>=ConstantUtil.Refresh_delay) {
                    getContestData();
                }else {
                    contest_pull_refresh.setRefreshing(false);
                }
            }
        });
    }

    public class prizePoolUp implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getDistributeAmount()), CustomUtil.convertFloat(o1.getDistributeAmount()));
        }
    }

    public class prizePoolDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getDistributeAmount()), CustomUtil.convertFloat(o2.getDistributeAmount()));
        }
    }

    public class EntryFeeUp implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getEntryFee()), CustomUtil.convertFloat(o1.getEntryFee()));
        }
    }

    public class EntryFeeDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getEntryFee()), CustomUtil.convertFloat(o2.getEntryFee()));
        }
    }

    public class WinnerUp implements Comparator<ContestModel.ContestDatum> {

        //int totalWin1, totalWin2;

        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            int totalWin1 = Integer.parseInt(o1.getTotalWinner()) * 100 / Integer.parseInt(o1.getNoTeamJoin());

            int totalWin2 = Integer.parseInt(o2.getTotalWinner()) * 100 / Integer.parseInt(o2.getNoTeamJoin());

            return Integer.compare(totalWin2, totalWin1);
        }
    }

    public class WinnerDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            int totalWin1 = Integer.parseInt(o1.getTotalWinner()) * 100 / Integer.parseInt(o1.getNoTeamJoin());

            int totalWin2 = Integer.parseInt(o2.getTotalWinner()) * 100 / Integer.parseInt(o2.getNoTeamJoin());

            return Integer.compare(totalWin1, totalWin2);
        }
    }

    public class SportsDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getNoTeamJoin()), CustomUtil.convertFloat(o2.getNoTeamJoin()));
        }
    }

    public class SportsUp implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getNoTeamJoin()), CustomUtil.convertFloat(o1.getNoTeamJoin()));
        }
    }

    public class ContestFilterAdapter extends RecyclerView.Adapter<ContestFilterAdapter.ContestFilterHolder> {

        private Context mContext;
        private List<ContestModel> contestModelList;
        private static final String TAG = "ContestFilterAdapter";
        private int selected;

        public ContestFilterAdapter(Context mContext, List<ContestModel> contestModelList) {
            this.mContext = mContext;
            this.contestModelList = contestModelList;
            selected = 0;
        }

        public void updateList(List<ContestModel> contestModelList) {
            this.contestModelList = contestModelList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ContestFilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContestFilterHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contest_header_filter, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ContestFilterAdapter.ContestFilterHolder holder, int position) {
            holder.title.setText(contestModelList.get(position).getTitle());

            if (position == selected) {
                holder.contest_filter_layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.title.setTextColor(mContext.getResources().getColor(R.color.pureWhite));
            } else {
                holder.contest_filter_layout.setBackground(mContext.getResources().getDrawable(R.drawable.btn_contest_filter));
                holder.title.setTextColor(mContext.getResources().getColor(R.color.font_color));
            }

            holder.contest_filter_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selected);
                    selected = position;
                    notifyItemChanged(position);
                    selectFilter(contestModelList.get(position),true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return contestModelList.size();
        }

        protected class ContestFilterHolder extends RecyclerView.ViewHolder {
            TextView title;
            LinearLayout contest_filter_layout;

            public ContestFilterHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.contest_filter_title);
                contest_filter_layout = itemView.findViewById(R.id.contest_filter_layout);
            }
        }
    }

}