package com.fantafeat.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantafeat.Activity.AddDepositActivity;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.MoreContestListActivity;
import com.fantafeat.Activity.SinglesContestActivity;
import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Adapter.ContestFilterAdapter;
import com.fantafeat.Adapter.ContestHeaderAdapter;
import com.fantafeat.Adapter.GroupListAdapter;
import com.fantafeat.Adapter.MoreContestFilterAdapter;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.AvailableQtyModel;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.OfferModel;
import com.fantafeat.Model.PassModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MusicManager;
import com.fantafeat.util.MyRecyclerScroll;
import com.fantafeat.util.PrefConstant;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public CardView f1card, f2card, cardMvp;
    private long diff;
    private JSONObject team_count, join_count;
    private ContestFilterAdapter filterAdapter;
    private ImageView imgFilter,btnUnFillFilter;
    private String lineupCount = "", selectedContestId = "";
    private BottomSheetDialog bottomSheetDialog = null;
    private ArrayList<EventModel> listTop;

   // private Socket mSocket= null;
    private String tradingDialogId="";
    private EventModel dialogEventModel;
    private Dialog dialog;
    private TextView txtMaxQtyAtPrice,txtQtyAtPrice,txtTrades;
    private ArrayList<AvailableQtyModel> yesList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> noList=new ArrayList<>();
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

    /*private void listener() {
        if (mSocket!=null){
            mSocket.off("res");
            mSocket.on("res", args -> {
                //Log.e("resp","LiveScore: "+args[0]);
                if (args!=null){
                    //Log.e("resp","LiveScore: "+args[0]);
                    try {
                        JSONObject object=new JSONObject(args[0].toString());
                        if (object!=null){
                            if (object.optString("en").equalsIgnoreCase("trade_unmatch_count")){
                                // Log.e("resp","trade_unmatch_count: "+args[0]);
                                if (!TextUtils.isEmpty(tradingDialogId) && dialogEventModel!=null && dialog!=null && dialog.isShowing()){
                                    JSONArray data=object.optJSONArray("data");
                                    if (data!=null){
                                        String rs=mContext.getResources().getString(R.string.rs);
                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject obj=data.optJSONObject(i);
                                            String contest_id=obj.optString("contest_id");
                                            if (contest_id.equalsIgnoreCase(tradingDialogId)){
                                                JSONArray yesCount=obj.optJSONArray("yesCount");
                                                JSONArray noCount=obj.optJSONArray("noCount");
                                                yesList.clear();
                                                noList.clear();
                                                for (int j = 0; j < yesCount.length(); j++) {
                                                    JSONObject yesObj=yesCount.optJSONObject(j);
                                                    AvailableQtyModel model1=gson.fromJson(yesObj.toString(),AvailableQtyModel.class);
                                                    yesList.add(model1);
                                                }

                                                for (int j = 0; j < noCount.length(); j++) {
                                                    JSONObject noObj=noCount.optJSONObject(j);
                                                    AvailableQtyModel model1=gson.fromJson(noObj.toString(),AvailableQtyModel.class);
                                                    noList.add(model1);
                                                }

                                                if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                                                    if (txtMaxQtyAtPrice!=null){
                                                        if (yesList.size()>0){
                                                            txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                                    rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                                                        }else {
                                                            txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                        }
                                                    }
                                                }
                                                else {
                                                    if (txtMaxQtyAtPrice!=null) {
                                                        if (noList.size()>0){
                                                            txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                                    rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                                                        }else {
                                                            txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                        }
                                                    }
                                                }
                                                if (txtQtyAtPrice!=null)
                                                    setOpinionValue(dialogEventModel.getOptionValue(),dialogEventModel.getSelectedPrice()
                                                            ,obj,txtQtyAtPrice);

                                                //Collections.sort(yesList, new TradingUp());
                                                //Collections.sort(noList, new TradingUp());

                                                ((Activity)mContext).runOnUiThread(() -> {
                                                    if (txtTrades!=null)
                                                        txtTrades.setText(CustomUtil.coolFormat(mContext,obj.optJSONObject("contest_data").optString("total_trades"))+" Trades");

                                                    if (adapter!=null && contestModelList!=null){
                                                        //ArrayList<EventModel> arrayList=tradingAdapter.getList();
                                                        for (int j = 0; j < contestModelList.size(); j++) {
                                                            ContestModel contestModel=contestModelList.get(j);
                                                            if (contestModel.getType()==2){
                                                                for (int k = 0; k < contestModel.getListTop().size(); k++) {
                                                                    EventModel model=contestModel.getListTop().get(k);
                                                                    if (!TextUtils.isEmpty(model.getId())){
                                                                        if (model.getId().equalsIgnoreCase(contest_id)){
                                                                            model.setTotalTrades(obj.optJSONObject("contest_data").optString("total_trades"));
                                                                            adapter.notifyItemChanged(0);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                });

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }*/

    public void getContestsDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getContests();
            }
        }, 300);
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
        cardMvp = view.findViewById(R.id.cardMvp);
        btnUnFillFilter = view.findViewById(R.id.btnUnFillFilter);
        // match_header_filter = view.findViewById(R.id.match_header_filter);
        listTop = new ArrayList<>();
        if (preferences.getMatchModel().getIs_single().equalsIgnoreCase("yes")) {
            cardMvp.setVisibility(View.VISIBLE);
        } else {
            cardMvp.setVisibility(View.GONE);
        }

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
                cardMvp.animate().translationX(0).setInterpolator(new DecelerateInterpolator(2)).start();
                create_team.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                int fabMargin = getResources().getDimensionPixelSize(R.dimen.top_bottom_margin);
                int fabMargin1 = getResources().getDimensionPixelSize(R.dimen.create_team_bottom_margin);
                cardMvp.animate().translationX(cardMvp.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
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

        cardMvp.setOnClickListener(v -> {
            getDuoGroup();
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

    private void getDuoGroup() {
        ArrayList<GroupModel> duoList = new ArrayList<>();
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());//ConstantUtil.testMatchId
            jsonObject.put("display_type", "2");
            jsonObject.put("user_id", preferences.getUserModel().getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("getDuoGroup", jsonObject.toString());

        HttpRestClient.postJSON(mContext, true, ApiManager.MATCH_WISE_GROUP_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getDuoGroup: " + responseBody.toString());

                if (responseBody.optBoolean("status")) {

                    JSONArray data = responseBody.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        duoList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.optJSONObject(i);
                            GroupModel groupModel = gson.fromJson(obj.toString(), GroupModel.class);
                            groupModel.setType("2");
                            duoList.add(groupModel);
                        }

                        if (duoList.size() > 0) {
                            startActivity(new Intent(mContext, SinglesContestActivity.class)
                                    .putExtra("group_model", duoList.get(0))
                                    .putExtra("is_match_after", false)
                            );
                        }
                    }
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }

            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
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
       /* contestModelList = new ArrayList<>();
        contestFilterModelList = new ArrayList<>();
        offerModelList = new ArrayList<>();*/
        JSONObject jsonObject = new JSONObject();
        try {
            //Log.e(TAG, "getContests: "+preferences.getMatchData().getId()+"   " +preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onSuccessResult Param: " + jsonObject + "\nurl: " + ApiManager.contestListV3);
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

    private void getTopList() {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("match_id", preferences.getMatchModel().getUniqueId());//  45454
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getTopList : " + jsonObject.toString());

        HttpRestClient.postJSONNormal(mContext, false, ApiManager.TRADING_CONTEST_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getTopList : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data = responseBody.optJSONArray("data");
                    //JSONArray tags=responseBody.optJSONArray("tags");

                    if (data != null && data.length() > 0) {
                        int posT = -1;
                        for (int i = 0; i < contestModelList.size(); i++) {
                            if (contestModelList.get(i).getType() == 2 &&
                                    contestModelList.get(i).getImage().equalsIgnoreCase("top")) {
                                posT = i;
                            }
                        }

                        if (posT != -1) {
                            contestModelList.remove(posT);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.optJSONObject(i);
                            EventModel model = gson.fromJson(obj.toString(), EventModel.class);
                            listTop.add(model);
                        }

                        ContestModel modal = new ContestModel();
                        modal.setImage("top");
                        modal.setId("-1");
                        modal.setIs_view_all("yes");
                        modal.setType(2);
                        modal.setTitle("TradeX");
                        modal.setSubTitle("What's your thought on this?");
                        modal.setListTop(listTop);
                        contestModelList.add(0, modal);

                        //adapter.notifyItemChanged(0);
                    }

                }
                adapter.notifyDataSetChanged();
                //updateAdapter();
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

        /*for (ContestModel model : CustomUtil.emptyIfNull(contestModelList)) {
            for (ContestModel.ContestDatum contest : CustomUtil.emptyIfNull(model.getContestData())) {
                contest.setIsOffer("No");
                contest.setO_entryFee(contest.getEntryFee());
                contest.setO_useBonus(contest.getUseBonus());

                for (OfferModel offerModel : CustomUtil.emptyIfNull(offerModelList)) {


                    if (contest.getId().equals(offerModel.getContestId())) {
                        contest.setOfferModel(offerModel);
                        contest.setOfferText(offerModel.getOfferText());

                        if (offerModel.getIsBonus().equalsIgnoreCase("Yes")) {
                            int joinedTeam = CustomUtil.convertInt(contest.getMyJoinedTeam());
                            if (joinedTeam >= CustomUtil.convertInt(offerModel.getMinTeam()) && joinedTeam < CustomUtil.convertInt(offerModel.getMaxTeam())) {
                                if (offerModel.getOfferType().equalsIgnoreCase("per")) {
                                    if (CustomUtil.convertFloat(contest.getMaxTeamBonusUse()) > joinedTeam) {
                                        if (CustomUtil.convertFloat(contest.getUseBonus()) < CustomUtil.convertFloat(offerModel.getOfferPrice())) {
                                            contest.setUseBonus(offerModel.getOfferPrice());

                                        } else {
                                            contest.setUseBonus(contest.getUseBonus());
                                        }
                                    } else {
                                        if (CustomUtil.convertFloat(contest.getDefaultBonus()) < CustomUtil.convertFloat(offerModel.getOfferPrice())) {
                                            contest.setUseBonus(offerModel.getOfferPrice());
                                           // contest.setOfferText(offerModel.getOfferText());
                                            contest.setMaxTeamBonusUse(offerModel.getMaxTeam());
                                        } else {
                                            contest.setUseBonus(contest.getDefaultBonus());
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            int joinedTeam = CustomUtil.convertInt(contest.getMyJoinedTeam());
                            if (joinedTeam >= CustomUtil.convertInt(offerModel.getMinTeam()) && joinedTeam < CustomUtil.convertInt(offerModel.getMaxTeam())) {
                                contest.setIsOffer("Yes");
                                float offerPrice = 0;
                                float currentPrice = CustomUtil.convertFloat(contest.getEntryFee());
                                if (offerModel.getOfferType().equalsIgnoreCase("per")) {
                                    float pricePer = CustomUtil.convertFloat(offerModel.getOfferPrice());
                                    offerPrice = currentPrice - (currentPrice * pricePer) / 100;
                                } else if (offerModel.getOfferType().equalsIgnoreCase("flat")) {
                                    float price = CustomUtil.convertFloat(offerModel.getOfferPrice());
                                    offerPrice = currentPrice - price;
                                }
                                //contest.setOfferText(offerModel.getOfferText());

                                if (offerPrice == 0) {
                                    contest.setEntryFee(CustomUtil.getFormater("0").format(offerPrice));
                                } else {
                                    if (offerPrice % 1 == 0) {
                                        contest.setEntryFee(CustomUtil.getFormater("0").format(offerPrice));
                                    } else {
                                        contest.setEntryFee(CustomUtil.getFormater("0.0").format(offerPrice));
                                    }
                                }

                                if (currentPrice == 0) {
                                    contest.setOfferPrice(CustomUtil.getFormater("0").format(currentPrice));
                                } else {
                                    if (currentPrice % 1 == 0) {
                                        contest.setOfferPrice(CustomUtil.getFormater("0").format(currentPrice));
                                    } else {
                                        contest.setOfferPrice(CustomUtil.getFormater("0.0").format(currentPrice));
                                    }
                                }
                            }
                        }
                        //break;
                    }
                }
            }
        }*/

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

       /* if (adapter != null) {
            adapter.updateList(contestModelList);
        } else {
            adapter = new ContestHeaderAdapter(mContext, contestModelList, ContestListInnerFragment.this, gson);
            contest_list.setLayoutManager(new LinearLayoutManager(mContext));
            contest_list.setAdapter(adapter);
        }*/

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

        if (!BuildConfig.APPLICATION_ID.equalsIgnoreCase(ConstantUtil.PLAY_STORE)){
            getTopList();
        }



        /*if (preferences.getPlayerModel()!=null && preferences.getPlayerModel().size()==0){
            getTempTeamData();
        }else if (preferences.getPlayerModel()!=null && preferences.getPlayerModel().size()>0){
            if (team_count!=null && !team_count.optString("total_team").equals("")
                    && preferences.getPlayerModel().size() != CustomUtil.convertInt(team_count.optString("total_team"))){
                getTempTeamData();
            } else if (!preferences.getPlayerModel().get(0).getMatchId().equalsIgnoreCase(preferences.getMatchModel().getId())) {
                getTempTeamData();
            }
        }*/

        /*if (preferences.getPlayerModel() != null) {
            joinDirectContest();
        }*/
    }

    public void getOpinionCnt(EventModel model) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getOpinionCnt : " + jsonObject);
        HttpRestClient.postJSONNormal(mContext, true, ApiManager.tradesContestListDetailsCount, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getOpinionCnt : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    showConfirmContestDialog(model, data);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });

    }

    private void showConfirmContestDialog(EventModel model, JSONObject data) {


        final float[] entryFee = {CustomUtil.convertFloat(model.getOptionEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        final float ff_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        /*if ((entryFee[0] - deposit) < 0) {
            use_deposit = entryFee[0];
        }
        else if ((entryFee[0] - (deposit + transfer_bal)) < 0) {
            use_deposit = deposit;
            use_transfer = entryFee[0] - deposit;
        }
        else {
            use_deposit = deposit;
            use_transfer = transfer_bal;
            use_winning = entryFee[0] - (deposit + transfer_bal);
        }*/

        float calBalance = deposit + transfer_bal + winning + ff_coin;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext, "Insufficient Balance");
            double amt = Math.ceil(entryFee[0] - calBalance);
            if (amt < 1) {
                amt = amt + 1;
            }
            int intamt = (int) amt;
            if (intamt < amt) {
                intamt += 1;
            }
            String payableAmt = String.valueOf(intamt);
            startActivity(new Intent(mContext, AddDepositActivity.class)
                    .putExtra("depositAmt", payableAmt));
        }
        else {

            tradingDialogId=model.getId();
            dialogEventModel=model;

            DecimalFormat format = CustomUtil.getFormater("00.00");
            String rs = mContext.getResources().getString(R.string.rs);
            UserModel userModel = preferences.getUserModel();

            float totalWalletBalance = CustomUtil.convertFloat(userModel.getDepositBal()) +
                    CustomUtil.convertFloat(userModel.getFf_coin()) +
                    CustomUtil.convertFloat(userModel.getWinBal()) + CustomUtil.convertFloat(userModel.getTransferBal());

            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

            dialog = new BottomSheetDialog(mContext);
            dialog.setContentView(R.layout.trading_confirm_dialog);

            final int[] qty = {0};
            final int[] price = {0};

            yesList.clear();
            noList.clear();

            JSONArray yesCount=data.optJSONArray("yesCount");
            JSONArray noCount=data.optJSONArray("noCount");

            for (int i = 0; i < yesCount.length(); i++) {
                JSONObject object=yesCount.optJSONObject(i);
                AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
                if (CustomUtil.convertFloat(model1.getTotalJoinCnt()) > 0)
                    yesList.add(model1);
            }

            for (int i = 0; i < noCount.length(); i++) {
                JSONObject object=noCount.optJSONObject(i);
                AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
                if (CustomUtil.convertFloat(model1.getTotalJoinCnt()) > 0)
                    noList.add(model1);
            }

            EditText edtQty = dialog.findViewById(R.id.edtQty);
            edtQty.setText("1");
            TextView txtPrice = dialog.findViewById(R.id.txtPrice);
            txtPrice.setText(rs + "1");
            TextView txtOptionYes = dialog.findViewById(R.id.txtOptionYes);
            TextView txtOptionNo = dialog.findViewById(R.id.txtOptionNo);
            txtQtyAtPrice = dialog.findViewById(R.id.txtQtyAtPrice);
            txtMaxQtyAtPrice = dialog.findViewById(R.id.txtMaxQtyAtPrice);
            TextView txtSelect = dialog.findViewById(R.id.txtSelect);

            TextView txtEndDate = dialog.findViewById(R.id.txtEndDate);
            ImageView imgConImage = dialog.findViewById(R.id.imgConImage);
            TextView txtConTitle = dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc = dialog.findViewById(R.id.txtDesc);
            LinearLayout layDesc = dialog.findViewById(R.id.layDesc);
            txtTrades = dialog.findViewById(R.id.txtTrades);
            TextView join_contest_btn = dialog.findViewById(R.id.join_contest_btn);
            TextView txtGet = dialog.findViewById(R.id.txtGet);
            TextView txtInvest = dialog.findViewById(R.id.txtInvest);
            TextView txtNewBadge = dialog.findViewById(R.id.txtNewBadge);
            TextView txtLblCommision = dialog.findViewById(R.id.txtLblCommision);
            TextView txtBtn10 = dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50 = dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100 = dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500 = dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750 = dialog.findViewById(R.id.txtBtn750);

            txtLblCommision.setText("Fee of " + model.getCommission() + "% on credit amount, 0% fee on loss.");

            txtEndDate.setText("Ends on " + CustomUtil.dateConvertWithFormat(model.getConEndTime(), "MMM dd hh:mm a"));

            if (model.isNewBadge()) {
                txtNewBadge.setVisibility(View.VISIBLE);
            } else {
                txtNewBadge.setVisibility(View.GONE);
            }

            txtConTitle.setText(model.getConDesc());
            if (TextUtils.isEmpty(model.getConSubDesc())) {
                layDesc.setVisibility(View.GONE);
            } else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(model.getConSubDesc());
            }

            if (!TextUtils.isEmpty(model.getConImage())) {
                CustomUtil.loadImageWithGlide(mContext, imgConImage, MyApp.imageBase + MyApp.document, model.getConImage(), R.drawable.event_placeholder);
            }

            txtTrades.setText(model.getTotalTrades() + " Trades");

            SeekBar seekBarPrice = dialog.findViewById(R.id.seekPrice);

            txtOptionYes.setOnClickListener(v -> {
                model.setOptionValue("Yes");
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            txtOptionNo.setOnClickListener(v -> {
                model.setOptionValue("No");
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);
                if (noList.size()>0){
                    txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    price[0] = progress;

                    model.setSelectedPrice(price[0] + "");

                    txtPrice.setText(rs + price[0]);

                    float investValue = price[0] * qty[0];
                    txtInvest.setText(rs + investValue + "");

                    setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

                    if (totalWalletBalance < (qty[0] * price[0])) {
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    } else {
                        join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_contest_btn.setText("Confirm");
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            if (model.getOptionValue().equalsIgnoreCase("yes")) {
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                if (TextUtils.isEmpty(model.getOption1Price())) {
                    price[0] = 1;
                    model.setSelectedPrice("1");
                } else {
                    price[0] = (int) CustomUtil.convertFloat(model.getOption1Price());
                    model.setSelectedPrice(model.getOption1Price());
                }

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }
            } else {
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                if (noList.size()>0){
                    txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }

                if (TextUtils.isEmpty(model.getOption2Price())) {
                    price[0] = 1;
                    model.setSelectedPrice("1");
                } else {
                    price[0] = (int) CustomUtil.convertFloat(model.getOption2Price());
                    model.setSelectedPrice(model.getOption2Price());
                }
            }

            seekBarPrice.setProgress(price[0]);

            setOpinionValue(model.getOptionValue(), price[0] + "", data, txtQtyAtPrice);

            SeekBar seekBarQty = dialog.findViewById(R.id.seekQty);

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] = progress;

                    edtQty.setText(qty[0] + "");
                    model.setCon_join_qty(qty[0] + "");

                    float getValue = qty[0] * 10;
                    txtGet.setText(rs + getValue + "");

                    float investValue = price[0] * qty[0];
                    txtInvest.setText(rs + investValue + "");

                    if (edtQty.hasFocus()) {
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (qty[0] == 10) {
                        txtBtn10.performClick();
                    } else if (qty[0] == 50) {
                        txtBtn50.performClick();
                    } else if (qty[0] == 100) {
                        txtBtn100.performClick();
                    } else if (qty[0] == 500) {
                        txtBtn500.performClick();
                    } else if (qty[0] == 750) {
                        txtBtn750.performClick();
                    } else {
                        txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn750.setBackgroundResource(R.drawable.border_match_green);
                    }

                    if (totalWalletBalance < (qty[0] * price[0])) {
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    } else {
                        join_contest_btn.setBackgroundResource(R.drawable.btn_green);
                        join_contest_btn.setText("Confirm");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            edtQty.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard((Activity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim()) <= 0) {
                        edtQty.setText("1");
                        edtQty.setSelection(1);
                    }
                    return false;
                }
                return false;
            });

            edtQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    /*if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                    }*/
                    if (TextUtils.isEmpty(charSequence)) {
                        qty[0] = 1;
                        //edtQty.setText("1");
                        //edtQty.setSelection(1);
                    } else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0] <= 0) {
                        qty[0] = 1;
                    } else if (qty[0] > 1000) {
                        qty[0] = 1000;
                    }
                  /*  if (edtQty.hasFocus())
                        edtQty.setSelection(charSequence.length());*/

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }

            });

            txtSelect.setOnClickListener(view1 -> {
                if (dialogEventModel.getOptionValue().equalsIgnoreCase("yes")){
                    if (yesList.size()>0){
                        price[0]=CustomUtil.convertInt(yesList.get(0).getJnAmount());
                    }else {
                        price[0]=1;
                    }
                    seekBarPrice.setProgress((int)price[0]);
                }else {
                    if (noList.size()>0){
                        price[0]=CustomUtil.convertInt(noList.get(0).getJnAmount());
                    }else {
                        price[0]=1;
                    }
                    seekBarPrice.setProgress((int)price[0]);
                }
            });

            txtBtn10.setOnClickListener(view1 -> {
                edtQty.setText("10");

                txtBtn10.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn50.setOnClickListener(view1 -> {
                edtQty.setText("50");

                txtBtn50.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn100.setOnClickListener(view1 -> {
                edtQty.setText("100");

                txtBtn100.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn500.setOnClickListener(view1 -> {
                edtQty.setText("500");

                txtBtn500.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                txtBtn750.setBackgroundResource(R.drawable.border_match_green);
            });

            txtBtn750.setOnClickListener(view1 -> {
                edtQty.setText("750");

                txtBtn750.setBackgroundResource(R.drawable.opinio_yes);
                txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                txtBtn10.setBackgroundResource(R.drawable.border_match_green);
            });

            seekBarQty.setProgress(10);

            join_contest_btn.setOnClickListener(v -> {
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")) {
                    if (TextUtils.isEmpty(model.getCon_join_qty())) {
                        CustomUtil.showTopSneakWarning(mContext, "Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(model.getCon_join_qty()) <= 0) {
                        CustomUtil.showTopSneakWarning(mContext, "Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinOpinionContest(model);
                } else {
                    if (totalWalletBalance < (qty[0] * price[0])) {
                        double amt = Math.ceil((qty[0] * price[0]) - totalWalletBalance);
                        if (amt < 1) {
                            amt = amt + 1;
                        }
                        int intamt = (int) amt;
                        if (intamt < amt) {
                            intamt += 1;
                        }
                        String payableAmt = String.valueOf(intamt);

                        startActivity(new Intent(mContext, AddDepositActivity.class)
                                .putExtra("depositAmt", payableAmt));
                    }
                }
            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

    private void setOpinionValue(String option, String price, JSONObject data, TextView txtQtyAtPrice) {

        txtQtyAtPrice.setText("0 quantities available at ₹" + price);

        if (option.equalsIgnoreCase("yes")) {
            for (int i = 0; i < yesList.size(); i++) {
                //JSONObject yesObj = yesCount.optJSONObject(i);
                AvailableQtyModel yesObj=yesList.get(i);
                if (yesObj.getJnAmount().equalsIgnoreCase(String.valueOf(price))) {
                    txtQtyAtPrice.setText(yesObj.getTotalJoinCnt() + " quantities available at ₹" + price);
                }
            }
        } else {
            for (int i = 0; i < noList.size(); i++) {
                AvailableQtyModel yesObj=noList.get(i);
                if (yesObj.getJnAmount().equalsIgnoreCase(String.valueOf(price))) {
                    txtQtyAtPrice.setText(yesObj.getTotalJoinCnt() + " quantities available at ₹" + price);
                }
            }
        }
    }

    private void joinOpinionContest(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
            jsonObject.put("entry_fee", model.getSelectedPrice());
            jsonObject.put("option_value", model.getOptionValue());
            jsonObject.put("con_join_qty", model.getCon_join_qty());
            LogUtil.e("resp", "param: " + jsonObject + " \n url: " + ApiManager.joinTradesContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinTradesContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    getUserData(model);
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getUserData(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.USER_DETAIL, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "USER USER_DETAIL: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    //if (responseBody.optString("code").equals("200")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);

                    float total;
                    if (ConstantUtil.is_bonus_show) {
                        total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());

                    } else {
                        total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                                + CustomUtil.convertFloat(userModel.getTransferBal());

                    }
                    /*float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                            + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());*/
                    userModel.setTotal_balance(total);

                    preferences.setUserModel(userModel);

                    showOrderSuccessDialog(model);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void showOrderSuccessDialog(EventModel model) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.order_success_dialog, null);

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.order_success_dialog);

        String rs = mContext.getResources().getString(R.string.rs);

        TextView txtOrderQty = dialog.findViewById(R.id.txtOrderQty);
        TextView txtOrderPrice = dialog.findViewById(R.id.txtOrderPrice);
        TextView txtOrderTotal = dialog.findViewById(R.id.txtOrderTotal);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        Button btnShare = dialog.findViewById(R.id.btnShare);
        Button btnMore = dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect = dialog.findViewById(R.id.imgCorrect);

        txtTitle.setText("Trade Order placed!");

        //Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);

        txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs + model.getSelectedPrice());
        float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs + total + "");

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
            getTopList();
            //getData();
        });

        btnShare.setOnClickListener(view -> {
            dialog.dismiss();

            shareContest(model);
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        MusicManager.getInstance().playEventSuccess(mContext);

        dialog.show();
    }

    private void shareContest(EventModel eventModel) {
        String url = ConstantUtil.LINK_TRADING_URL + eventModel.getId();
        ;

        /*FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix(ConstantUtil.LINK_PREFIX_URL)
                .setIosParameters(new DynamicLink.IosParameters.Builder(ConstantUtil.FF_IOS_APP_BUNDLE).setAppStoreId(ConstantUtil.FF_IOS_APP_ID)
                        .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK)).build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK))
                        .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getShortLink() != null)
                            shareShortLink(task.getResult().getShortLink(), eventModel);
                        else {
                            CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                        }
                    } else {
                        CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                    }
                });*/
    }

    private void shareShortLink(Uri link, EventModel eventModel) {
        String content =//ConstantUtil.LINK_URL+"\n\n"+
                "*" + eventModel.getConDesc() + "* \uD83C\uDFC6" +
                        "\n\nwhat do you think will happen ❓\n\n" +
                        "\uD83D\uDCC8Trade on *Fantafeat*.\n\n" +
                        "\uD83D\uDCB8Earning is simple here.\n\n" +
                        "Deadline⏳ : " + CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(), "dd MMM, yyyy hh:mm a") +
                        "\n\nTap below link for join:\uD83D\uDCF2" +
                        "\n" + link.toString().trim()/*+MyApp.getMyPreferences().getMatchModel().getId()+"/"+list.getId()*/;

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
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