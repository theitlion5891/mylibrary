package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Adapter.ContestListAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.OfferModel;
import com.fantafeat.Model.PassModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyMatchesFragment extends BaseFragment {

    List<ContestModel.ContestDatum> contestModelList;
    SwipeRefreshLayout join_refresh;
    private ContestListAdapter contestListAdapter;
    private RecyclerView joined_team_contest_list;
    ContestListActivity contestListActivity;
    LinearLayout layNoData,join_contest;
    //TextView ;
    ImageView imgPlace;
    private int offset, limit;
    private boolean isApiCall, isGetData;

    public MyMatchesFragment() {
      //  super();
        //this.contestListActivity = contestListActivity;
    }

    public static MyMatchesFragment newInstance() {

        return new MyMatchesFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;
        contestModelList=new ArrayList<>();
        offset = 0;
        limit = 1000;
        isApiCall = true;
        isGetData = true;
        getData();
        if (preferences.getPrefBoolean("join_contest")) {
            preferences.setPref("join_contest",false);
            Log.e(TAG, "onResume: " );
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            String model = gson.toJson(((ContestListActivity) mContext).list);
            intent.putExtra("model", model);
            mContext.startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_matches, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        joined_team_contest_list = view.findViewById(R.id.joined_team_contest_list);
        join_refresh = view.findViewById(R.id.join_refresh);
        //nodata = view.findViewById(R.id.nodata);
        join_contest = view.findViewById(R.id.join_contest);

        layNoData = view.findViewById(R.id.layNoData);
        imgPlace = view.findViewById(R.id.imgPlace);

        offset = 0;
        limit = 100;
        isApiCall = true;
        isGetData = true;

        contestModelList = new ArrayList<>();

        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        joined_team_contest_list.setLayoutManager(mLinearLayoutManager);

        contestListAdapter = new ContestListAdapter(mContext, contestModelList,gson,0,true);
        joined_team_contest_list.setAdapter(contestListAdapter);

        joined_team_contest_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLinearLayoutManager != null &&
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == contestModelList.size() - 1
                        && isGetData && isApiCall) {
                    getData();
                }
            }
        });

    }

    @Override
    public void initClick() {
        join_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConstantUtil.isTimeOverShow=false;


                getData();
            }
        });
        join_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ContestListActivity)mContext).tabLayout.selectTab(((ContestListActivity)mContext).tabLayout.getTabAt(0));
            }
        });
    }

    public void getData() {
       // contestModelList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtil.e("resp",jsonObject.toString());

        boolean showProgress = true;
        if (join_refresh != null && join_refresh.isRefreshing()) {
            //join_refresh.setRefreshing(false);
            showProgress = false;
        }
        HttpRestClient.postJSON(mContext, showProgress, ApiManager.JOIN_CONTEST_LIST_BY_ID, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (join_refresh != null && join_refresh.isRefreshing()) {
                    join_refresh.setRefreshing(false);
                    isGetData = false;
                    offset=0;
                }
                contestModelList.clear();
                contestListAdapter.updateData(contestModelList);

                ConstantUtil.isTimeOverShow=false;
                LogUtil.e(TAG, "onSuccessResult getData: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;

                    isApiCall = true;

                   // if(array.length()>0) {
                        //nodata.setVisibility(View.GONE);
                    join_refresh.setVisibility(View.VISIBLE);

                    for (i = 0; i < array.length(); i++) {
                        try {
                            JSONObject object = array.optJSONObject(i);
                            ContestModel.ContestDatum cModal = gson.fromJson(object.toString(), ContestModel.ContestDatum.class);
                            cModal.setIs_pass(object.optString("is_pass"));
                            LogUtil.e(TAG,"OOBBJJ="+object);

                            ArrayList<PassModel> passList=new ArrayList<>();
                            if (object.has("my_pass_data")){
                                JSONArray jsonArray=object.optJSONArray("my_pass_data");
                                if (jsonArray!=null){
                                    for (int a = 0;a<jsonArray.length();a++){
                                        PassModel model=gson.fromJson(jsonArray.optJSONObject(a).toString(),PassModel.class);
                                        passList.add(model);
                                    }
                                }
                            }
                            cModal.setPassModelArrayList(passList);

                            if (TextUtils.isEmpty(cModal.getOffer_date_text()) &&
                                    cModal.getIs_pass().equalsIgnoreCase("yes") && cModal.getPassModelArrayList().size()>0){
                                PassModel passContestModel=cModal.getPassModelArrayList().get(0);
                                int count=Integer.parseInt(passContestModel.getNoOfEntry())-Integer.parseInt(passContestModel.getTotalJoinSpot());
                                if (count>0){
                                    JSONArray arr=new JSONArray();
                                    for (int k=0;k<Integer.parseInt(cModal.getMaxJoinTeam());k++){
                                        JSONObject obj=new JSONObject();
                                        obj.put("team_no",(k+1)+"");
                                        obj.put("used_bonus","");
                                        obj.put("discount_entry_fee",cModal.getEntryFee());
                                        arr.put(obj);
                                    }
                                    if (arr.length()>0)
                                        cModal.setOffer_date_text(arr.toString());
                                    else
                                        cModal.setOffer_date_text("");
                                }else
                                    cModal.setOffer_date_text("");

                            }

                            if (!TextUtils.isEmpty(cModal.getOffer_date_text())){
                                ArrayList<NewOfferModal> newOfferModals=new ArrayList<>();
                                ArrayList<NewOfferModal> temnewOfferModals=new ArrayList<>();

                                for (int k=0;k<CustomUtil.convertInt(cModal.getMaxJoinTeam());k++){
                                    NewOfferModal newOfferModal=new NewOfferModal();
                                    newOfferModal.setDiscount_entry_fee("");
                                    newOfferModal.setEntry_fee(cModal.getEntryFee());
                                    newOfferModal.setTeam_no((k+1)+"");
                                    newOfferModal.setUsed_bonus(cModal.getDefaultBonus());
                                    newOfferModals.add(newOfferModal);
                                    temnewOfferModals.add(newOfferModal);

                                }
                                cModal.setNewOfferList(newOfferModals);

                                JSONArray offerDateArr=new JSONArray(cModal.getOffer_date_text());

                                ArrayList<NewOfferModal> newOfferTempModals=new ArrayList<>();
                                for (int k=0;k<offerDateArr.length();k++){
                                    JSONObject obj=offerDateArr.getJSONObject(k);
                                    NewOfferModal mdl=gson.fromJson(obj.toString(), NewOfferModal.class);
                                    if (mdl!=null && TextUtils.isEmpty(mdl.getDiscount_entry_fee())){
                                        mdl.setEntry_fee(cModal.getEntryFee());
                                    }
                                    newOfferTempModals.add(mdl);
                                }

                                    if (cModal.getPassModelArrayList().size()>0){
                                        PassModel passContestModel=cModal.getPassModelArrayList().get(0);
                                        int count=Integer.parseInt(passContestModel.getNoOfEntry())-Integer.parseInt(passContestModel.getTotalJoinSpot());
                                        for (int k=0;k<newOfferTempModals.size();k++){
                                            NewOfferModal newOfferModal = newOfferTempModals.get(k);
                                            if (k<=count-1){
                                                newOfferModal.setDiscount_entry_fee("0");
                                                newOfferModal.setPassId(passContestModel.getPassId());
                                            }
                                        }
                                    }

                                cModal.setNewOfferTempList(newOfferTempModals);

                                for (NewOfferModal newOfferModal:cModal.getNewOfferList()){
                                    for (NewOfferModal temp:cModal.getNewOfferTempList()){
                                        if (CustomUtil.convertInt(temp.getTeam_no())==CustomUtil.convertInt(newOfferModal.getTeam_no())){
                                            newOfferModal.setDiscount_entry_fee(temp.getDiscount_entry_fee());
                                            newOfferModal.setPassId(temp.getPassId());
                                            //LogUtil.e("compa",cModal.getUseBonus()+"  naxJoin");
                                            if (TextUtils.isEmpty(temp.getUsed_bonus())){
                                                temp.setUsed_bonus("0");
                                            }
                                            if (CustomUtil.convertInt(temp.getUsed_bonus())>0){
                                                newOfferModal.setUsed_bonus(temp.getUsed_bonus());
                                            }
                                        }
                                    }
                                }

                                if (CustomUtil.convertInt(cModal.getMyJoinedTeam())>0){
                                    if (temnewOfferModals.size()>0){
                                        temnewOfferModals.subList(0,CustomUtil.convertInt(cModal.getMyJoinedTeam())).clear();
                                        cModal.setNewOfferRemovedList(temnewOfferModals);
                                    }
                                }
                                else {
                                    //cModal.setNewOfferRemovedList(new ArrayList<>());
                                    cModal.setNewOfferRemovedList(temnewOfferModals);
                                }

                                    if (cModal.getPassModelArrayList().size()>0){
                                        PassModel passContestModel=cModal.getPassModelArrayList().get(0);
                                        int count=Integer.parseInt(passContestModel.getNoOfEntry())-Integer.parseInt(passContestModel.getTotalJoinSpot());
                                        for (int k=0;k<temnewOfferModals.size();k++){
                                            NewOfferModal newOfferModal = temnewOfferModals.get(k);
                                            if (k<=count-1){
                                                newOfferModal.setDiscount_entry_fee("0");
                                                newOfferModal.setPassId(passContestModel.getPassId());
                                            }
                                        }
                                        cModal.setNewOfferList(temnewOfferModals);
                                    }
                            }
                            else {
                                cModal.setNewOfferList(new ArrayList<>());
                                cModal.setNewOfferRemovedList(new ArrayList<>());
                                cModal.setNewOfferTempList(new ArrayList<>());
                            }
                            contestModelList.add(cModal);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    if (array.length()>0)
                        ((ContestListActivity)mContext).tabLayout.getTabAt(1).setText("My Contests ("+responseBody.optString("my_join_cnt")+")");

                    /*for (ContestModel.ContestDatum contest : CustomUtil.emptyIfNull(contestModelList)) {
                        contest.setIsOffer("No");
                        contest.setO_entryFee(contest.getEntryFee());
                        contest.setO_useBonus(contest.getUseBonus());
                        for (OfferModel offerModel : CustomUtil.emptyIfNull(preferences.getOfferModel())) {
                            if (contest.getId().equals(offerModel.getContestId())) {
                                contest.setOfferModel(offerModel);
                                contest.setOfferText(offerModel.getOfferText());

                                LogUtil.e("ofrData",contest.getOfferModel().getOfferPrice());
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
                                                    //  contest.setOfferText(offerModel.getOfferText());
                                                    contest.setMaxTeamBonusUse(offerModel.getMaxTeam());
                                                } else {
                                                    contest.setUseBonus(contest.getDefaultBonus());
                                                }
                                            }
                                        }
                                    }
                                } else {
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
                                        // contest.setOfferText(offerModel.getOfferText());

                                        if(offerPrice==0){
                                            contest.setEntryFee(new DecimalFormat("0").format(offerPrice));
                                        }else{
                                            if(offerPrice%1==0){
                                                contest.setEntryFee(new DecimalFormat("0").format(offerPrice));
                                            }else{
                                                contest.setEntryFee(new DecimalFormat("0.0").format(offerPrice));
                                            }

                                        }

                                        if(currentPrice==0){
                                            contest.setOfferPrice(new DecimalFormat("0").format(currentPrice));
                                        }else{
                                            if(currentPrice%1==0){
                                                contest.setOfferPrice(new DecimalFormat("0").format(currentPrice));
                                            }else{
                                                contest.setOfferPrice(new DecimalFormat("0.0").format(currentPrice));
                                            }

                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }*/

                    contestListAdapter.updateData(contestModelList);

                    if (contestModelList.size() < limit) {
                        isGetData = false;
                        offset = 0;
                    }
                    else {
                        if(array.length()>0) {
                            isGetData = true;
                            offset += limit;
                        }else {
                            isGetData = false;
                        }

                    }

                       /* if (limit > array.length()) {
                            isGetData = false;
                        }else{
                            if (array.length() > 0 && array.length()==limit){
                                offset += limit;
                                isGetData = true;
                            }
                        }*/

                    //}else {
                    checkData();
                        /*else{
                        nodata.setVisibility(View.VISIBLE);
                        join_refresh.setVisibility(View.GONE);
                    }*/
                   // }
                    //setData();
                }else {
                    checkData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (join_refresh != null && join_refresh.isRefreshing()) {
                    join_refresh.setRefreshing(false);
                }
            }
        });
    }

    private void checkData(){
        LogUtil.d("selSports",preferences.getMatchModel().getSportId()+" ");
        switch (preferences.getMatchModel().getSportId()+""){
            case "1":
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.cricket_placeholder);
                }
                break;
            case "2":
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.football_placeholder);
                }
                break;
            case "3":
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.baseball_placeholder);
                }
                break;
            case "4":
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.basketball_placeholder);
                }
                break;
            case "5":
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.vollyball_placeholder);
                }
                break;
            case "6"://6
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.handball_placeholder);

                }
                break;
            case "7"://6
                if (contestModelList.size()>0){
                    joined_team_contest_list.setVisibility(View.VISIBLE);
                    layNoData.setVisibility(View.GONE);
                }else {
                    joined_team_contest_list.setVisibility(View.GONE);
                    layNoData.setVisibility(View.VISIBLE);
                    imgPlace.setImageResource(R.drawable.kabaddi_placeholder);

                }
                break;
        }
    }

    private void setData() {
        /*LogUtil.e("ofrData",preferences.getOfferModel().size());
        for (ContestModel.ContestDatum contest : CustomUtil.emptyIfNull(contestModelList)) {
            contest.setIsOffer("No");
            contest.setO_entryFee(contest.getEntryFee());
            contest.setO_useBonus(contest.getUseBonus());
            for (OfferModel offerModel : CustomUtil.emptyIfNull(preferences.getOfferModel())) {
                if (contest.getId().equals(offerModel.getContestId())) {
                    contest.setOfferModel(offerModel);
                    contest.setOfferText(offerModel.getOfferText());

                    LogUtil.e("ofrData",contest.getOfferModel().getOfferPrice());
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
                                      //  contest.setOfferText(offerModel.getOfferText());
                                        contest.setMaxTeamBonusUse(offerModel.getMaxTeam());
                                    } else {
                                        contest.setUseBonus(contest.getDefaultBonus());
                                    }
                                }
                            }
                        }
                    } else {
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
                           // contest.setOfferText(offerModel.getOfferText());

                            if(offerPrice==0){
                                contest.setEntryFee(new DecimalFormat("0").format(offerPrice));
                            }else{
                                if(offerPrice%1==0){
                                    contest.setEntryFee(new DecimalFormat("0").format(offerPrice));
                                }else{
                                    contest.setEntryFee(new DecimalFormat("0.0").format(offerPrice));
                                }

                            }

                            if(currentPrice==0){
                                contest.setOfferPrice(new DecimalFormat("0").format(currentPrice));
                            }else{
                                if(currentPrice%1==0){
                                    contest.setOfferPrice(new DecimalFormat("0").format(currentPrice));
                                }else{
                                    contest.setOfferPrice(new DecimalFormat("0.0").format(currentPrice));
                                }

                            }
                        }
                    }
                    break;
                }
            }
        }*/
        /*if(contestModelList.size()>0){
            ((ContestListActivity)mContext).tabLayout.getTabAt(1).setText("My Contests ("+contestModelList.size()+")");
        }else{
            ((ContestListActivity)mContext).tabLayout.getTabAt(1).setText("My Contest");
        }*/

        contestListAdapter.updateData(contestModelList);

        Log.e(TAG, "setData: 1" );

        checkData();

    }

}