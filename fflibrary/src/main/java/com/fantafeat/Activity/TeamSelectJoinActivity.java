package com.fantafeat.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.fantafeat.Adapter.LeagueJoinAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.ContestQuantityModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.OfferModel;
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
import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TeamSelectJoinActivity extends BaseActivity {

    private RecyclerView join_team_selection_list;
    private ImageView back_img;
    private LeagueJoinAdapter adapter;
    private TextView join_contest;
    public LinearLayout team_join_confirm_btn;
    private TextView team_create;
    public int selectPosition;
    public ContestModel.ContestDatum contestData=new ContestModel.ContestDatum();
    private BottomSheetDialog bottomSheetDialog = null;
    List<PlayerListModel> playerListModelList;
    String[] selectedTeams;
    private long diff;
    private CountDownTimer countDownTimer;
    private LinearLayout laySelAll;
    private MaterialCheckBox team_all_select;

    private BottomSheetDialog bottomSheetDialog2 = null;
    public boolean isAllCheck=false;
    List<String> selectedTeamList=new ArrayList<>();
    float use_deposit = 0, use_transfer = 0, use_winning = 0, use_donation_deposit = 0, useBonus = 0,useCoin=0,amtToAdd=0;
    public String selectedCnt="";

    private String selectedState="";
    private Spinner spinState;
    private ArrayList<String> cityName,cityId;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private String selectedGender="Select Gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_select_join);

        Window window = TeamSelectJoinActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        Intent intent = getIntent();
        String model = intent.getStringExtra("model");
        LogUtil.d("contestData",model+" contestData123");
        contestData = gson.fromJson(model,ContestModel.ContestDatum.class);

        selectPosition = -1;

        join_team_selection_list = findViewById(R.id.join_team_selection_list);
        team_create = findViewById(R.id.team_create);
        back_img = findViewById(R.id.toolbar_back);
        laySelAll = findViewById(R.id.laySelAll);
        if (ApiManager.IS_MULTIJOIN){
            if (CustomUtil.convertInt(contestData.getMaxJoinTeam()) == 1){
                laySelAll.setVisibility(View.GONE);
            }else {
                laySelAll.setVisibility(View.VISIBLE);
            }
        }
        else {
            laySelAll.setVisibility(View.GONE);
        }
        team_all_select = findViewById(R.id.team_all_select);
        team_join_confirm_btn = findViewById(R.id.team_join_confirm_btn);

        if (contestData.getJoinedTeamTempTeamId()!=null){
            selectedTeams = contestData.getJoinedTeamTempTeamId().split(",");
            selectedTeamList=new ArrayList<>(Arrays.asList(selectedTeams));
        }

      //  Log.d("teamId",contestData.getJoinedTeamTempTeamId()+" ");

        initClick();
        setTimer();
        getTempTeamData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;
    }

    public void initClick() {

        back_img.setOnClickListener(view -> onBackPressed());

        team_create.setOnClickListener(v->{
            Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
            intent.putExtra(PrefConstant.TEAMCREATETYPE, 0);
            startActivity(intent);
        });

        team_all_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAllCheck=isChecked;
        });
        //final boolean[] isChecked = {false};
        team_all_select.setOnClickListener(v -> {
            if (playerListModelList!=null){
                if (playerListModelList.size()>0){
                    int count=1,inCnt=0;
                    for (int i=0;i<playerListModelList.size();i++){
                        PlayerListModel model=playerListModelList.get(i);
                        if (selectedTeamList.size()>0){
                            if (!selectedTeamList.contains(model.getId().trim())){
                                if (count<=CustomUtil.convertInt(contestData.getMaxJoinTeam()) ){
                                    model.setChecked(isAllCheck);
                                    inCnt+=1;
                                }
                            }
                        }else {
                            if (count<=CustomUtil.convertInt(contestData.getMaxJoinTeam()) ){
                                model.setChecked(isAllCheck);
                                inCnt+=1;
                            }
                        }
                        count+=1;
                    }
                    LogUtil.e("resp"," "+inCnt);
                    adapter.notifyDataSetChanged();
                    if (isAllCheck){
                        team_join_confirm_btn.setVisibility(View.VISIBLE);
                    }else {
                        team_join_confirm_btn.setVisibility(View.GONE);
                    }
                    isAllCheck = !isAllCheck;
                }
            }
        });

        laySelAll.setOnClickListener(v->{
            team_all_select.performClick();
        });

        team_join_confirm_btn.setOnClickListener(view -> {
            if(MyApp.getClickStatus()){
                if (playerListModelList!=null) {
                    if (playerListModelList.size() > 0) {
                        LogUtil.e("debg", "Myapp");
                        if (ApiManager.IS_MULTIJOIN) {
                            LogUtil.e("debg", "Multi");
                            if (CustomUtil.convertInt(contestData.getMaxJoinTeam()) > 1) {
                                LogUtil.e("debg", ">1");
                                int selCnt = 0;
                                for (int i = 0; i < playerListModelList.size(); i++) {
                                    PlayerListModel model = playerListModelList.get(i);
                                    if (model.isChecked()) {
                                        selCnt += 1;
                                    }
                                }
                                if (selCnt > 0) {//selectPosition != -1

                                    int remainTeam = CustomUtil.convertInt(contestData.getMaxJoinTeam()) - CustomUtil.convertInt(contestData.getMyJoinedTeam());
                                    if (selCnt > remainTeam) {
                                        CustomUtil.showTopSneakWarning(mContext, "Please select maximum " + remainTeam + " teams");
                                    } else {
                                        try {
                                            makeArray();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    //LogUtil.e("debg","mkarr else");
                                    CustomUtil.showTopSneakWarning(mContext, "Please select your team");
                                }
                            } else {
                                if (selectPosition != -1) {
                                    LogUtil.e("debg", "conf1");
                                    confirmTeam();
                                } else {
                                    CustomUtil.showTopSneakError(mContext, "Please select your team");
                                }
                            }
                        }
                        else {
                            if (selectPosition != -1) {
                                LogUtil.e("debg", "conf2");
                                confirmTeam();
                            } else {
                                CustomUtil.showTopSneakError(mContext, "Please select your team");
                            }
                        }
                    }
                }
            }
        });

    }

    private void makeArray() throws JSONException {

      /*  if (TextUtils.isEmpty(preferences.getUserModel().getEmailId())){
            showBasicDetailDialog("makeArray");
            return;
        }*/

        /*NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat format = (DecimalFormat) nf;
        format.setRoundingMode(RoundingMode.HALF_UP);
        format.applyPattern("00.00");*/

        DecimalFormat format=CustomUtil.getFormater("00.0000");
       // format.setRoundingMode(RoundingMode.HALF_UP);

       //DecimalFormat format=new DecimalFormat("00.00");
        //format.setRoundingMode(RoundingMode.HALF_UP);

        float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        float coins = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());
       // float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        //final float entryFee = CustomUtil.convertFloat(contestData.getO_entryFee());
        //float orgContestEntry = CustomUtil.convertFloat(contestData.getEntryFee());

        JSONArray param=new JSONArray();
        JSONObject mainObj=new JSONObject();
        JSONObject passDataObj=new JSONObject();

        int selectedTeamCnt=0;

        int myJoinCnt=CustomUtil.convertInt(contestData.getMyJoinedTeam());
        LogUtil.e("compa",myJoinCnt+"   myJoinCnt");
        float deposit_bal=0;
        float totalCharge=0;
        float bonus_bal=0;
        float coin_bal=0;
       // float transfer_total=0;
        float win_bal=0;
        float useRewar=0;


        ArrayList<PlayerListModel> tempListModal=new ArrayList<>();

        for (int i=0;i<playerListModelList.size();i++) {
            PlayerListModel model = playerListModelList.get(i);
            if (model.isChecked()){
                tempListModal.add(model);
            }
        }
        int passCount = 0;
        String passID="";
        for (int i=0;i<tempListModal.size();i++) {
            PlayerListModel model = tempListModal.get(i);

            selectedTeamCnt+=1;
            int teamno = myJoinCnt+1;

            LogUtil.e("compa",teamno+"   teamno");

            use_deposit =0;  use_winning =0; useBonus = 0 ; useCoin=0;//use_donation_deposit =0;use_transfer =0;
            JSONObject jsonObject=new JSONObject();

            //ContestModel.ContestDatum contestData=getOfferedContestData(myJoinCnt);

            // LogUtil.e("resp","usebonus: "+contestData.getUseBonus());

            float Contest_fee = CustomUtil.convertFloat(contestData.getEntryFee());

            float usableBonus = 0;
            float usableFFCoins = 0;

            if (contestData.getNewOfferRemovedList().size()>0){
                NewOfferModal modal=contestData.getNewOfferRemovedList().get(i);
                if (modal.getDiscount_entry_fee().equalsIgnoreCase("")){
                    Contest_fee=CustomUtil.convertFloat(contestData.getEntryFee());
                }else {
                    Contest_fee=CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                    //  LogUtil.e("compa",Contest_fee+"   Contest_fee");
                }
                usableBonus=CustomUtil.convertFloat(modal.getUsed_bonus());
                //teamno=modal.getTeam_no();
            }
            else {
                usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
                Contest_fee=CustomUtil.convertFloat(contestData.getEntryFee());
            }

            LogUtil.e("compa",Contest_fee+"   Contest_fee");

            usableFFCoins=CustomUtil.convertFloat(contestData.getDefault_bb_coins());

            useBonus = ((Contest_fee * usableBonus) / 100);

            useCoin = ((Contest_fee * usableFFCoins) / 100);

            if (useBonus > bonus) {
                useBonus = bonus;
            }

            if (useCoin > coins) {
                useCoin = coins;
            }

            if (Contest_fee - useBonus >= 0) {
                Contest_fee = Contest_fee - useBonus;
            }

            if (useCoin>=useBonus)
                useCoin=useCoin-useBonus;

            if (Contest_fee - useCoin >= 0) {
                Contest_fee = Contest_fee - useCoin;
            }

            if ((Contest_fee - deposit) < 0) {
                use_deposit = Contest_fee;
            } else {
                use_deposit = deposit;
                //use_transfer = transfer_bal;
                use_winning = Contest_fee - (deposit );
            }

            deposit=deposit-use_deposit;
            bonus=bonus-useBonus;
            winning=winning-use_winning;
            coins=coins-useCoin;

            useRewar+=useBonus;
            //   transfer_bal=transfer_bal-use_transfer;
            totalCharge+=(useBonus+use_deposit+use_winning+useCoin);//use_transfer+

            LogUtil.e("resp","bonus: "+useBonus+"  "+useRewar);

            jsonObject.put("deposit_bal", format.format(use_deposit));
            jsonObject.put("bonus_bal", format.format(useBonus));
            jsonObject.put("win_bal", format.format(use_winning));
            jsonObject.put("ff_coins_bal", format.format(useCoin));
            //     jsonObject.put("transfer_bal", format.format(use_transfer));
            jsonObject.put("donation_bal", "0");
            jsonObject.put("team_name", model.getTempTeamName());
            jsonObject.put("temp_team_id", model.getId());
            jsonObject.put("team_no", teamno);
            jsonObject.put("pass_id", "");
            LogUtil.e(TAG,"TEAMNO="+teamno);

            if (contestData.getIs_pass()!=null && contestData.getIs_pass().equalsIgnoreCase("yes")){
                if (contestData.getPassModelArrayList().size()>0){
                    PassModel model1 = contestData.getPassModelArrayList().get(0);
                    int leftCount = Integer.parseInt(model1.getNoOfEntry())-Integer.parseInt(model1.getTotalJoinSpot());
                    if (i<=leftCount-1){
                        LogUtil.e(TAG,"IIIIII=>"+leftCount);
                        passID= model1.getPassId();
                        LogUtil.e(TAG,"PASS_id"+passID);
                        jsonObject.put("pass_id",model1.getPassId());
                        passCount++;
                        passDataObj.put("id",model1.getId());
                        passDataObj.put("no_of_entry",model1.getNoOfEntry());
                        passDataObj.put("pass_id",model1.getPassId());
                        passDataObj.put("total_join_spot",model1.getTotalJoinSpot());

                    }
                }
            }

            deposit_bal+=use_deposit;
            // donation_bal+=use_donation_deposit;
            bonus_bal+=useBonus;
            coin_bal+=useCoin;
            //transfer_total+=use_transfer;
            win_bal+=use_winning;

            param.put(jsonObject);
            myJoinCnt+=1;
            // LogUtil.e("resp","param: "+jsonObject);
            // LogUtil.e("resp","count: "+myJoinCnt);

            // myJoinCnt+=1;


        }
        mainObj.put("pass_id",passID);
        mainObj.put("applied_pass_count",String.valueOf(passCount));
        mainObj.put("my_pass_data",passDataObj);
        mainObj.put("bonus_bal",format.format(bonus_bal));
        mainObj.put("ff_coins_bal",format.format(coin_bal));
        mainObj.put("contest_id",contestData.getId());
        mainObj.put("con_display_type",preferences.getMatchModel().getConDisplayType());
        mainObj.put("deposit_bal",format.format(deposit_bal));
        mainObj.put("donation_bal","0");
        mainObj.put("match_id",preferences.getMatchModel().getId());
        //mainObj.put("transfer_bal",format.format(transfer_total));
        mainObj.put("user_id",preferences.getUserModel().getId());
        mainObj.put("win_bal",format.format(win_bal));

        mainObj.put("team_array",param);

        LogUtil.e(TAG,"param123: "+mainObj);
        LogUtil.e(TAG,"param1my_pass_data: "+passDataObj);

        float calReward=0,calCoin=0,bbcoin=CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        if (CustomUtil.convertFloat(preferences.getUserModel().getBonusBal())>0){
            if (useRewar<=CustomUtil.convertFloat(preferences.getUserModel().getBonusBal())){
                calReward=useRewar;
            }else if (useRewar>CustomUtil.convertFloat(preferences.getUserModel().getBonusBal())){
                calReward=CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
            }else {
                calReward=0;
            }
        }
        else {
            calReward=0;
        }

        if (bbcoin>0){
            if (coin_bal<=bbcoin){
                calCoin=coin_bal;
            }else if (coin_bal>bbcoin){
                calCoin=bbcoin;
            }else {
                calCoin=0;
            }
        }
        else {
            calCoin=0;
        }

        float depos=CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        //float trans=CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        float won=CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //float coin1=CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float myTotalCal = depos +  won + calReward + calCoin;//trans +
        LogUtil.e("debg","myTotalCal  "+myTotalCal+"  "+totalCharge);
        if (myTotalCal < totalCharge) {
            //CustomUtil.showTopSneakWarning(mContext, "Insufficient Balance");
           /* double amt=totalCharge-myTotalCal;//Math.ceil(totalCharge-myTotalCal);
            if (amt<1){
                amt=1;
            }

            int intamt=(int) amt;

            if (intamt<amt){
                intamt+=1;
            }

            LogUtil.d("payamt",intamt+" ");
            String payableAmt=format.format(intamt);//String.valueOf(amt);*/

            float amt=totalCharge-myTotalCal;

            if (amt<1){
                amt=1;
            }
            /*LogUtil.e("debg","amt if "+amt);
            String patableAmt=CustomUtil.getFormater("0.00").format(amt);
            LogUtil.e("debg","amt patableAm1 "+patableAmt);*/
        //    String patableAmt=String.valueOf(Math.round(totalCharge-(deposit + useBonus + winning)));
            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
            CustomUtil.showTopSneakError(mContext,mContext.getResources().getString(R.string.not_enough_balance));
           /* Intent intent = new Intent(TeamSelectJoinActivity.this,AddDepositActivity.class);
            intent.putExtra("isJoin",true);
            intent.putExtra("depositAmt",patableAmt);
            intent.putExtra("contestData",gson.toJson(contestData));
            startActivity(intent);*/
           // LogUtil.e("debg","amt patableAmt2 "+patableAmt);
            return;
        }

        TextView join_contest_fee, join_use_deposit, join_use_borrowed, join_use_rewards,
                join_use_winning, join_user_pay;
        final CheckBox join_donation_select;
        TextView join_donation_text,txtTeamBackup,txtCntLabel;
        final EditText join_donation_price;
        final LinearLayout layTeamCnt;

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_joining, null);

        join_contest = (TextView) view.findViewById(R.id.join_contest_btn);
        join_contest_fee = (TextView) view.findViewById(R.id.join_contest_fee);
        join_use_deposit = (TextView) view.findViewById(R.id.join_use_deposit);
        //join_use_borrowed = (TextView) view.findViewById(R.id.join_use_borrowed);
        join_use_rewards = (TextView) view.findViewById(R.id.join_use_rewards);
        join_use_winning = (TextView) view.findViewById(R.id.join_use_winning);
        join_user_pay = (TextView) view.findViewById(R.id.join_user_pay);
       // join_donation_select = (CheckBox) view.findViewById(R.id.join_donation_select);
       // join_donation_select.setVisibility(View.GONE);
       // join_donation_text = (TextView) view.findViewById(R.id.join_donation_text);
        //join_donation_text.setVisibility(View.GONE);
       // join_donation_price = (EditText) view.findViewById(R.id.join_donation_number);
        LinearLayout layBonus = view.findViewById(R.id.layBonus);
        layTeamCnt = view.findViewById(R.id.layTeamCnt);
        txtTeamBackup = view.findViewById(R.id.txtTeamBackup);
        txtCntLabel = view.findViewById(R.id.txtCntLabel);
        LinearLayout layMultiContest = view.findViewById(R.id.layMultiContest);
        layMultiContest.setVisibility(View.GONE);

        if (ConstantUtil.is_bonus_show){
            layBonus.setVisibility(View.VISIBLE);
        }else {
            layBonus.setVisibility(View.GONE);
        }
        /*  if (TextUtils.isEmpty(selectedCnt)){
            selectedCnt=selectedTeamCnt+"";
        }*/

        if (ApiManager.IS_BREAKUP_SHOW){
            if (selectedTeamCnt>1){
                layTeamCnt.setVisibility(View.VISIBLE);
                txtTeamBackup.setText( selectedTeamCnt+" teams");//selectedCnt
            }else {
                txtTeamBackup.setText( selectedTeamCnt+" teams");//selectedCnt
                layTeamCnt.setVisibility(View.GONE);
            }
        }
        else {
            layTeamCnt.setVisibility(View.GONE);
            txtTeamBackup.setVisibility(View.GONE);
        }

       // join_donation_price.setVisibility(View.GONE);

        //join_donation_price.setEnabled(false);
        DecimalFormat format1=CustomUtil.getFormater("00.00");

        join_use_deposit.setText(getResources().getString(R.string.rs) + format1.format((deposit_bal+coin_bal)));
       // join_use_borrowed.setText(getResources().getString(R.string.rs) + format.format(transfer_total));
        join_use_winning.setText(getResources().getString(R.string.rs) + format1.format(win_bal));
        join_use_rewards.setText(getResources().getString(R.string.rs) + format1.format(bonus_bal));
        if (TextUtils.isEmpty(contestData.getEntryFee())){
            join_contest_fee.setText(getResources().getString(R.string.rs) + contestData.getO_entryFee());
        }else {
            join_contest_fee.setText(getResources().getString(R.string.rs) + contestData.getEntryFee());
        }

        join_user_pay.setText(getResources().getString(R.string.rs) + format1.format((totalCharge)));

       /* txtTeamBackup.setOnClickListener(v->{
            TeamBreakup bottomSheetTeam = new TeamBreakup(mContext,mainObj,preferences);
            bottomSheetTeam.show(getSupportFragmentManager(),
                    TeamBreakup.TAG);
        });*/

        // final float finalUseBonus = useBonus;
        join_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //join_contest.setEnabled(false);
                if(MyApp.getClickStatus()) {
                    Float donation;
                    Boolean isDonation = false;
                    bottomSheetDialog.dismiss();

                    SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date.before(MyApp.getCurrentDate())) {
                        Toast.makeText(mContext, "Time Up! Match Started", Toast.LENGTH_LONG).show();
                    } else {
                        joinContest(mainObj);
                       /* if (!isDonation && join_donation_select.isChecked()) {
                            Toast.makeText(mContext, "Insufficient Balance For Donation", Toast.LENGTH_LONG).show();
                        } else {
                            //joinContest();
                            joinContest(mainObj);
                        }*/
                    }
                }
            }
        });

        bottomSheetDialog = new BottomSheetDialog(mContext);

        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        if (!bottomSheetDialog.isShowing())
            bottomSheetDialog.show();
    }

    private ContestModel.ContestDatum getOfferedContestData(int myJoinTeamCnt){


        DecimalFormat format = CustomUtil.getFormater("0");
       // format.setRoundingMode(RoundingMode.HALF_UP);


        DecimalFormat decFormat =  CustomUtil.getFormater("0.0");
       // decFormat.setRoundingMode(RoundingMode.HALF_UP);


        ContestModel.ContestDatum returnData=contestData;
        // LogUtil.e("resp","useb: "+contestData.getO_useBonus());

        if (contestData.getOfferModel()!=null){
            OfferModel offerModel=contestData.getOfferModel();
            if (offerModel.getIsBonus().equalsIgnoreCase("Yes")) {
                // int joinedTeam = CustomUtil.convertInt(myJoinTeamCnt);
                returnData.setEntryFee(contestData.getEntryFee());
                returnData.setDefaultBonus(contestData.getDefaultBonus());

                if (myJoinTeamCnt >= CustomUtil.convertInt(offerModel.getMinTeam()) && myJoinTeamCnt < CustomUtil.convertInt(offerModel.getMaxTeam())) {
                    if (offerModel.getOfferType().equalsIgnoreCase("per")) {
                        if (CustomUtil.convertFloat(contestData.getMaxTeamBonusUse()) > myJoinTeamCnt) {
                            returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                            if (CustomUtil.convertFloat(contestData.getO_useBonus()) < CustomUtil.convertFloat(offerModel.getOfferPrice())) {
                                returnData.setUseBonus(offerModel.getOfferPrice());
                                //      LogUtil.e("resp","if1: "+offerModel.getOfferPrice());
                                //  returnData.setOfferText(offerModel.getOfferText());
                                //     Log.e(TAG, "setData if if: "+offerModel.getOfferText() );
                            } /*else {
                                returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                                returnData.setUseBonus(contestData.getO_useBonus());
                            }*/
                        } else {
                            if (CustomUtil.convertFloat(contestData.getDefaultBonus()) < CustomUtil.convertFloat(offerModel.getOfferPrice())) {
                                returnData.setUseBonus(offerModel.getOfferPrice());
                                // LogUtil.e("resp","else1: "+offerModel.getOfferPrice());
                                //  returnData.setOfferText(offerModel.getOfferText());
                                returnData.setMaxTeamBonusUse(offerModel.getMaxTeam());
                                //       Log.e(TAG, "setData if else: "+offerModel.getOfferText() );
                            } /*else {
                                returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                                returnData.setUseBonus(contestData.getO_useBonus());
                            }*/
                        }
                    }
                }else {
                    returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                    returnData.setUseBonus(contestData.getO_useBonus());
                    // LogUtil.e("resp","else2: "+contestData.getO_useBonus());
                    returnData.setO_isOffer(false);
                }
            }
            else {
                //returnData.setEntryFee(contestData.getEntryFee());
                returnData.setUseBonus(contestData.getUseBonus());
                returnData.setDefaultBonus(contestData.getDefaultBonus());
                // int joinedTeam = CustomUtil.convertInt(contest.getMyJoinedTeam());
                if (myJoinTeamCnt >= CustomUtil.convertInt(offerModel.getMinTeam()) && myJoinTeamCnt < CustomUtil.convertInt(offerModel.getMaxTeam())) {
                    //returnData.setIsOffer("Yes");
                    returnData.setO_isOffer(true);
                    float offerPrice = 0;
                    float currentPrice = CustomUtil.convertFloat(contestData.getO_entryFee());
                    if (offerModel.getOfferType().equalsIgnoreCase("per")) {
                        float pricePer = CustomUtil.convertFloat(offerModel.getOfferPrice());
                        offerPrice = currentPrice - (currentPrice * pricePer) / 100;
                    } else if (offerModel.getOfferType().equalsIgnoreCase("flat")) {
                        float price = CustomUtil.convertFloat(offerModel.getOfferPrice());
                        offerPrice = currentPrice - price;
                    }
                    //returnData.setOfferText(offerModel.getOfferText());
                    // Log.e(TAG, "setData else: "+offerModel.getOfferText() );
                    if (offerPrice == 0) {
                        returnData.setEntryFee(format.format(offerPrice));
                    } else {
                        if (offerPrice % 1 == 0) {
                            returnData.setEntryFee(format.format(offerPrice));
                        } else {
                            returnData.setEntryFee(decFormat.format(offerPrice));
                        }
                    }
                    if (CustomUtil.convertFloat(contestData.getDefaultBonus())<offerPrice){
                        returnData.setMaxTeamBonusUse(offerModel.getMaxTeam());
                    }else {
                        returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                    }

                    if (currentPrice == 0) {
                        returnData.setOfferPrice(format.format(currentPrice));
                    } else {
                        if (currentPrice % 1 == 0) {
                            returnData.setOfferPrice(format.format(currentPrice));
                        } else {
                            returnData.setOfferPrice(decFormat.format(currentPrice));
                        }
                    }
                }else {
                    returnData.setO_isOffer(false);
                    returnData.setMaxTeamBonusUse(contestData.getMaxTeamBonusUse());
                    returnData.setEntryFee(contestData.getO_entryFee());
                }
            }
            return returnData;
        }

        return returnData;
    }

    public void singleCheck(int position,boolean isCheck){
        int chkCnt=0;
        for (int i=0;i<playerListModelList.size();i++) {
            PlayerListModel model = playerListModelList.get(i);
            if (model.isChecked() || model.getIsSelected().equalsIgnoreCase("yes")){
                chkCnt+=1;
            }
        }
       // int remain=(playerListModelList.size()-selectedTeamList.size()+1)-chkCnt;
        team_all_select.setChecked(playerListModelList.size()==chkCnt);//remain == 0
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
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
        final List<PlayerListModel> playerListModels = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.TEMP_TEAM_DETAIL_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "TEMP_TEAM_LIST: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    int i = 0, j = 0;
                    JSONArray array = responseBody.optJSONArray("data");
                    for (j = 0; j < data.length(); j++) {
                        int wk = 0, bat = 0, ar = 0, bowl = 0, team1 = 0, team2 = 0,lineup_count = 0,cr=0;
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
                                if (preferences.getMatchModel().getSportId().equals("1")){
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
                                if (preferences.getMatchModel().getSportId().equals("2")){
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
                                if (preferences.getMatchModel().getSportId().equals("3")){
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
                                if (preferences.getMatchModel().getSportId().equals("6")){
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
                                if (preferences.getMatchModel().getSportId().equals("7")){
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
                                if (preferences.getMatchModel().getSportId().equals("4")){
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

                                if(preferences.getPlayerList() != null){
                                    for(PlayerModel playerModel : preferences.getPlayerList()){
                                        if(playerModel.getPlayerId().equalsIgnoreCase(playerDetail.getPlayerId())){
                                            playerDetail.setPlayingXi(playerModel.getPlayingXi());
                                            playerDetail.setOther_text(playerModel.getOther_text());
                                            playerDetail.setOther_text2(playerModel.getOther_text2());
                                            break;
                                        }
                                    }
                                }
                                if(preferences.getMatchModel().getTeamAXi().toLowerCase().equals("yes") || preferences.getMatchModel().getTeamBXi().toLowerCase().equals("yes")){

                                    if(playerDetail.getPlayingXi().toLowerCase().equals("no") &&
                                            !playerDetail.getOther_text().equalsIgnoreCase("substitute")){
                                        lineup_count++;
                                        LogUtil.e(TAG, "onSuccessResult: ABC "+playerDetail.getPlayerName() );
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
                        LogUtil.e(TAG, "onSuccessResult: "+lineup_count );
                        playerListModel.setLineup_count(""+ lineup_count);
                        playerListModel.setTeam1_sname(preferences.getMatchModel().getTeam1Sname());
                        playerListModel.setTeam2_sname(preferences.getMatchModel().getTeam2Sname());
                        playerListModels.add(playerListModel);
                    }
                    preferences.setPlayerModel(playerListModels);
                    //joinDirectContest();
                    setData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void setData() {

        playerListModelList = new ArrayList<>();
        Map<String, List<PlayerListModel>> filterJoining = new HashMap<>();

        for (PlayerListModel list : CustomUtil.emptyIfNull(preferences.getPlayerModel())) {
            if (selectedTeams != null) {
                for (int j = 0; j < selectedTeams.length; j++) {
                    if (list.getId().equals(selectedTeams[j])) {
                        LogUtil.e(TAG, "initControl: selected list");
                        list.setIsSelected("Yes");
                    }
                }
            }
            if (filterJoining.containsKey(list.getIsSelected())) {
                List<PlayerListModel> tempList = filterJoining.get(list.getIsSelected());
                tempList.add(list);
                filterJoining.put(list.getIsSelected(), tempList);
            } else {
                List<PlayerListModel> tempList = new ArrayList<>();
                tempList.add(list);
                filterJoining.put(list.getIsSelected(), tempList);
            }
            playerListModelList.add(list);
        }

        if (filterJoining.containsKey("Yes") || filterJoining.containsKey("No")) {
            playerListModelList = new ArrayList<>();
            if (filterJoining.get("No") != null) {
                playerListModelList.addAll(Objects.requireNonNull(filterJoining.get("No")));
            }
            if (filterJoining.get("Yes") != null) {
                playerListModelList.addAll(Objects.requireNonNull(filterJoining.get("Yes")));
            }

        }

        adapter = new LeagueJoinAdapter(mContext, playerListModelList, TeamSelectJoinActivity.this,CustomUtil.convertInt(contestData.getMaxJoinTeam()));
        join_team_selection_list.setLayoutManager(new LinearLayoutManager(mContext));
        join_team_selection_list.setAdapter(adapter);

    }

    private void confirmTeam() {


        if (!isValidForJoin(contestData,1)) {//((deposit +  winning + useBonus) - Contest_fee[0]) < 0

            double amt=Math.ceil(amtToAdd);//Contest_fee[0]-(deposit + winning);

            if (amt<1){
                amt=1;
            }

            //String patableAmt=CustomUtil.getFormater("0.00").format(amt);
            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
            CustomUtil.showTopSneakError(mContext,mContext.getResources().getString(R.string.not_enough_balance));

        }
        else {
            TextView join_contest_fee, join_use_deposit, join_use_borrowed, join_use_rewards,
                    join_use_winning, join_user_pay;
            final CheckBox join_donation_select;
            TextView join_donation_text;
            final EditText join_donation_price,edtConQty;
            final RecyclerView recyclerNoOfContest;

            ArrayList<ContestQuantityModel> contestQtyList=ConstantUtil.getContestEntryList();

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

            LinearLayout layBonus = view.findViewById(R.id.layBonus);
            if (ConstantUtil.is_bonus_show){
                layBonus.setVisibility(View.VISIBLE);
            }else {
                layBonus.setVisibility(View.GONE);
            }

            if (contestData.getConPlayerEntry().equalsIgnoreCase("Single")
                    && contestData.getAutoCreate().equalsIgnoreCase("yes")
                    && !(contestData.getEntryFee().equalsIgnoreCase("0") ||
                    contestData.getEntryFee().equalsIgnoreCase("0.0")||
                    contestData.getEntryFee().equalsIgnoreCase("0.00"))) {
                layMultiContest.setVisibility(View.VISIBLE);

                recyclerNoOfContest.setLayoutManager(new GridLayoutManager(mContext,6));
                ContestQuantityAdapter adapter=new ContestQuantityAdapter(mContext, contestQtyList, new ContestQuantityAdapter.onQtyListener() {
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
                            ContestQuantityModel model=contestQtyList.get(i);
                            if (s.toString().trim().equalsIgnoreCase(model.getId().trim())){
                                model.setSelected(true);
                            }else {
                                model.setSelected(false);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (!TextUtils.isEmpty(s.toString())){
                            int qty=Integer.parseInt(s.toString());
                            if (qty>500){
                                CustomUtil.showToast(mContext,"Maximum 500 Quantity allow");

                                edtConQty.setText("500");
                                edtConQty.setSelection(3);

                                qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                isValidForJoin(contestData,qty);

                            }
                            else {
                                qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                isValidForJoin(contestData,qty);

                            }

                            LogUtil.e(TAG, "onClick: deposit_bal" + CustomUtil.getFormater("00.00").format(use_deposit) +
                                    "\n  Win_bal" + CustomUtil.getFormater("00.00").format(use_winning) +
                                    "\n  Donation useBonus" + CustomUtil.getFormater("00.00").format(useBonus));

                            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+useCoin)));
                            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_deposit+use_winning+useBonus+useCoin));
                        }
                        else {
                            join_use_deposit.setText(getResources().getString(R.string.rs) + "00.00");
                            join_use_winning.setText(getResources().getString(R.string.rs) + "00.00");
                            join_use_rewards.setText(getResources().getString(R.string.rs) + "00.00");
                            join_user_pay.setText(getResources().getString(R.string.rs) + "00.00");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }else {
                layMultiContest.setVisibility(View.GONE);
            }

            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+useCoin)));
            //join_use_borrowed.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_transfer));
            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
            join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contestData.getEntryFee())));// Contest_fee+ useBonus
            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+use_winning+useBonus+useCoin)));// Contest_fee+ useBonus

            final float finalUseBonus = useBonus;
            join_contest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //join_contest.setEnabled(false);
                    if(MyApp.getClickStatus()) {
                        /*Float donation;
                        Boolean isDonation = false;*/


                        LogUtil.e(TAG, "onClick: deposit_bal" + use_deposit + "\n Transfer " + use_transfer +
                                "\n bonus" + finalUseBonus + "\n  Win_bal" + use_winning +
                                "\n  Donation Deposit" + use_donation_deposit);
                        int qty=0;
                        if (!TextUtils.isEmpty(edtConQty.getText().toString().trim())){
                            qty=Integer.parseInt(edtConQty.getText().toString().trim());
                        }

                        if (!isValidForJoin(contestData,qty)){//((deposit +  winning + useBonus) - (CustomUtil.convertFloat(contestData.getEntryFee()) * qty)) < 0
                            bottomSheetDialog.dismiss();
                            //CustomUtil.showToast(mContext,"Insufficient Balance");
                            //float amt= (CustomUtil.convertFloat(contestData.getEntryFee()) * qty) - (deposit + winning);
                            double amt=Math.ceil(amtToAdd);

                            if (amt<1){
                                amt=1;
                            }
                            contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                            //String patableAmt=CustomUtil.getFormater("0.00").format(amt);
                            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
                            CustomUtil.showTopSneakError(mContext,mContext.getResources().getString(R.string.not_enough_balance));

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
                        } else if (TextUtils.isEmpty(edtConQty.getText().toString().trim())){
                            CustomUtil.showToast(mContext,"Select atleast 1 contest quantity");
                        }
                        else if (qty<=0){
                            CustomUtil.showToast(mContext,"Select atleast 1 contest quantity");
                        }
                        else if (qty>500){
                            CustomUtil.showToast(mContext,"Maximum 500 Quantity allow");
                        }
                        else {
                            bottomSheetDialog.dismiss();
                            contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                            joinContest();
                        }
                    }
                }
            });

            bottomSheetDialog = new BottomSheetDialog(mContext);

            bottomSheetDialog.setContentView(view);
            //((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
            if (bottomSheetDialog!=null && !bottomSheetDialog.isShowing())
                bottomSheetDialog.show();
        }
    }

    private boolean isValidForJoin(ContestModel.ContestDatum contestData,int qty){
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit  = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus , usableBBCoins = 0;

        if (!TextUtils.isEmpty(contestData.getOffer_date_text())){
            if (contestData.getNewOfferRemovedList().size()>0){
                NewOfferModal modal=contestData.getNewOfferRemovedList().get(0);
                if (modal.getDiscount_entry_fee().equalsIgnoreCase("")){
                    finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
                }else {
                    finalEntryFee =CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                }
                usableBonus=CustomUtil.convertFloat(modal.getUsed_bonus());
            }
            else {
                usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
                finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
            }
        }else {
            usableBonus=CustomUtil.convertFloat(contestData.getDefaultBonus());
            finalEntryFee =CustomUtil.convertFloat(contestData.getEntryFee());
        }

        usableBBCoins=CustomUtil.convertFloat(contestData.getDefault_bb_coins());

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

        if (useCoin>=useBonus)
            useCoin=useCoin-useBonus;

        if (finalEntryFee - useCoin >= 0) {
            finalEntryFee = finalEntryFee - useCoin;
        }

        if ((finalEntryFee - deposit) < 0) {
            use_deposit = finalEntryFee;
        }
        else {
            use_deposit = deposit;
            use_winning = finalEntryFee - deposit;
        }

        float calReward=0,calCoin=0;

        if (bonus>0){
            if (useBonus<=bonus){
                calReward=useBonus;
            }else if (useBonus>bonus){
                calReward=bonus;
            }else {
                calReward=0;
            }
        }
        else {
            calReward=0;
        }

        if (bb_coin>0){
            if (useCoin<=bb_coin){
                calCoin=useCoin;
            }else if (useCoin>bb_coin){
                calCoin=bb_coin;
            }else {
                calCoin=0;
            }
        }
        else {
            calCoin=0;
        }

        float calBalance1 = deposit +  winning + calReward + calCoin;
        float totalCharge=useBonus+use_deposit+use_winning+useCoin;

        amtToAdd = totalCharge-calBalance1;

        LogUtil.e("resp","deposit:"+deposit+"\nwinning:"+winning+"\nbb_coin:"+bb_coin+"\nuse_deposit:"+use_deposit
                +"\nuse_transfer:"+use_transfer+"\nuse_winning:"+use_winning+"\nuse_bb_coin:"+useCoin+"\nuseBonus:"+useBonus);

        if (calBalance1 < totalCharge) {
            return false;
        }
        return true;
    }

    private void joinContest() {
        joinContest(contestData.getId());
    }

    private void joinContest(String contest_id) {
        PlayerListModel playerListModel = playerListModelList.get(selectPosition);

        DecimalFormat format=CustomUtil.getFormater("0.0000");
      //  format.setRoundingMode(RoundingMode.HALF_UP);
        //temp_team_id,match_id,con_display_type,user_id,contest_id,team_name,deposit_bal,bonus_bal,win_bal,transfer_bal,donation_bal
        final JSONObject jsonObject = new JSONObject();
        final JSONObject childObj = new JSONObject();
        final JSONArray array = new JSONArray();

        try {
            //jsonObject.put("temp_team_id", playerListModel.getId());
            jsonObject.put("match_id", preferences.getMatchModel().getId());
            jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("contest_id", contest_id);
            if (TextUtils.isEmpty(contestData.getJoin_con_qty()) || contestData.getJoin_con_qty().equalsIgnoreCase("0"))
                contestData.setJoin_con_qty("1");
            jsonObject.put("join_con_qty", contestData.getJoin_con_qty());
            // jsonObject.put("team_name", playerListModel.getTempTeamName());
            if (use_deposit>0){
                use_deposit=use_deposit/Integer.parseInt(contestData.getJoin_con_qty());
            }
            if (useBonus>0){
                useBonus=useBonus/Integer.parseInt(contestData.getJoin_con_qty());
            }
            if (use_winning>0){
                use_winning=use_winning/Integer.parseInt(contestData.getJoin_con_qty());
            }
            if (useCoin>0){
                useCoin=useCoin/Integer.parseInt(contestData.getJoin_con_qty());
            }
            // jsonObject.put("team_name", playerListModel.getTempTeamName());
            jsonObject.put("ff_coins_bal", format.format(useCoin));
            jsonObject.put("deposit_bal",  format.format(use_deposit));//92.85
            jsonObject.put("bonus_bal", format.format(useBonus));//6
            jsonObject.put("win_bal", format.format(use_winning));//21.15
            jsonObject.put("transfer_bal", format.format(use_transfer));
            jsonObject.put("donation_bal", format.format(use_donation_deposit));

            jsonObject.put("pass_id", "");
            jsonObject.put("applied_pass_count", "0");
            //jsonObject.put("team_no", team_no);

            childObj.put("team_no", "1");
            childObj.put("pass_id", "");

            childObj.put("deposit_bal", format.format(use_deposit));
            childObj.put("ff_coins_bal", format.format(useCoin));
            childObj.put("bonus_bal", format.format(useBonus));
            childObj.put("win_bal", format.format(use_winning));
            childObj.put("transfer_bal", format.format(use_transfer));
            childObj.put("donation_bal", format.format(use_donation_deposit));
            childObj.put("team_name",  playerListModel.getTempTeamName());
            childObj.put("temp_team_id", playerListModel.getId());


            if (contestData.getIs_pass() != null && contestData.getIs_pass().equalsIgnoreCase("yes")){
                if (contestData.getPassModelArrayList().size()>0){
                    LogUtil.e(TAG,"teamSelectJoin="+contestData.getPassModelArrayList().size());
                    PassModel model = contestData.getPassModelArrayList().get(0);
                    int count=Integer.parseInt(model.getNoOfEntry())-Integer.parseInt(model.getTotalJoinSpot());
                    if (count>=1){
                        childObj.put("pass_id", model.getPassId());
                        jsonObject.put("pass_id", model.getPassId());
                        jsonObject.put("applied_pass_count", "1");

                        JSONObject passData = new JSONObject();
                        passData.put("id", model.getId());
                        passData.put("no_of_entry", model.getNoOfEntry());
                        passData.put("pass_id", model.getPassId());
                        passData.put("total_join_spot", model.getTotalJoinSpot());
                        jsonObject.put("my_pass_data",passData);
                        LogUtil.e(TAG,"pass_data=>"+passData);
                    }
                }
            }
            array.put(childObj);
            jsonObject.put("team_array",array);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("resp",jsonObject.toString());

        HttpRestClient.postJSON(mContext, true, ApiManager.JOIN_CONTEST2MultiJoin, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult:teamselect " + responseBody.toString());

                if (responseBody.optBoolean("status")) {

                    //refreshArray(playerListModel.getId());

                    UserModel user = preferences.getUserModel();

                    if (use_deposit>0){
                        use_deposit=use_deposit*Integer.parseInt(contestData.getJoin_con_qty());
                    }
                    if (useBonus>0){
                        useBonus=useBonus*Integer.parseInt(contestData.getJoin_con_qty());
                    }
                    if (use_winning>0){
                        use_winning=use_winning*Integer.parseInt(contestData.getJoin_con_qty());
                    }
                    if (useCoin>0){
                        useCoin=useCoin*Integer.parseInt(contestData.getJoin_con_qty());
                    }

                    float deposit_bal = CustomUtil.convertFloat(user.getDepositBal()) - use_deposit;
                   // float transfer_bal = CustomUtil.convertFloat(user.getTransferBal()) - use_transfer;
                    float bonus_bal = CustomUtil.convertFloat(user.getBonusBal()) - useBonus;
                    float coin_bal = CustomUtil.convertFloat(user.getFf_coin()) - useCoin;
                    float winning_bal = CustomUtil.convertFloat(user.getWinBal()) - use_winning;

                    user.setDepositBal(String.valueOf(deposit_bal));
                  //  user.setTransferBal(String.valueOf(transfer_bal));

                    user.setWinBal(String.valueOf(winning_bal));
                    user.setFf_coin(String.valueOf(coin_bal));

                    float total;
                    if (ConstantUtil.is_bonus_show){
                        user.setBonusBal(String.valueOf(bonus_bal));
                        total =  CustomUtil.convertFloat(user.getDepositBal()) +
                                CustomUtil.convertFloat(user.getWinBal()) +
                                //CustomUtil.convertFloat(user.getTransferBal()) +
                                CustomUtil.convertFloat(user.getFf_coin()) +
                                CustomUtil.convertFloat(user.getBonusBal());

                    }else {
                        total = CustomUtil.convertFloat(user.getDepositBal()) +
                                CustomUtil.convertFloat(user.getWinBal()) +
                                //CustomUtil.convertFloat(user.getTransferBal()) +
                                CustomUtil.convertFloat(user.getFf_coin());

                    }

                    user.setTotal_balance(total);

                    preferences.setUserModel(user);
                    LogUtil.e(TAG,"SingleJoin");
                    Toast.makeText(mContext,"Team Join Successfully",Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();


                    Intent intent=new Intent();
                    intent.putExtra("tempTeamId",playerListModel.getId());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                    //onBackPressed();
                }
                else {
                    String message = responseBody.optString("msg");
                    CustomUtil.showTopSneakWarning(mContext, message);
                    LogUtil.e(TAG,"MDHD1:"+message);

                    if (responseBody.has("new_con_id")) {
                        final String new_con_id = responseBody.optString("new_con_id");
                        if (new_con_id != null && !new_con_id.equals("null") && !new_con_id.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TeamSelectJoinActivity.this);
                            builder.setTitle("The Contest is already full!!");//Almost there! That League filled up
                            builder.setMessage("Don't worry, we have same contest for you! join this contest.");//No worries, join this League instead! it is exactly the same type
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        jsonObject.remove("contest_id");
                                        jsonObject.put("contest_id", new_con_id);
                                        joinContest(new_con_id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void joinContest(JSONObject param) {
        //JSONObject passData = new JSONObject();

        try {
            param.put("join_con_qty", "1");

            HttpRestClient.postJSON(mContext, true, ApiManager.JOIN_CONTEST2MultiJoin, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    if (responseBody.optBoolean("status")) {

                        UserModel user = preferences.getUserModel();

                        float deposit_bal = 0;//use_deposit;win:56.50, bon:104
                      //  float transfer_bal = 0;//use_transfer;
                        float bonus_bal = 0;//bonus_bal;
                        float winning_bal = 0;//bonus_bal;
                        float coin_bal = 0;//bonus_bal;
                        try {
                            deposit_bal = (float) (CustomUtil.convertFloat(user.getDepositBal()) -
                                    CustomUtil.convertFloat(param.getString("deposit_bal")));
                            coin_bal = (float) (CustomUtil.convertFloat(user.getFf_coin()) -
                                    CustomUtil.convertFloat(param.getString("ff_coins_bal")));
                            //transfer_bal = (float) (CustomUtil.convertFloat(user.getTransferBal()) - CustomUtil.convertFloat(param.getString("transfer_bal")));
                            bonus_bal = (float) (CustomUtil.convertFloat(user.getBonusBal()) -
                                    CustomUtil.convertFloat(param.getString("bonus_bal")));
                            winning_bal = (float) (CustomUtil.convertFloat(user.getWinBal()) -
                                    CustomUtil.convertFloat(param.getString("win_bal")));

                            LogUtil.e("param",winning_bal+" "+CustomUtil.convertFloat(user.getWinBal())+"  "+
                                    CustomUtil.convertFloat(param.getString("win_bal")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        user.setDepositBal(String.valueOf(deposit_bal));
                        user.setFf_coin(String.valueOf(coin_bal));
                    //    user.setTransferBal(String.valueOf(transfer_bal));
                        user.setWinBal(String.valueOf(winning_bal));

                        float total;
                        if (ConstantUtil.is_bonus_show){
                            user.setBonusBal(String.valueOf(bonus_bal));
                            total =  CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) +
                                    CustomUtil.convertFloat(user.getBonusBal());

                        }else {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }

                        user.setTotal_balance(total);

                        preferences.setUserModel(user);
                        LogUtil.e(TAG,"MultipleJoin");
                        Toast.makeText(mContext,"Teams Join Successfully",Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();

                        Intent intent=new Intent();
                        intent.putExtra("tempTeamId", param.optString("contest_id"));
                        setResult(Activity.RESULT_OK,intent);
                        finish();

                    }
                    else {
                        String message = responseBody.optString("msg");
                        CustomUtil.showTopSneakWarning(mContext, message);

                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
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
                //timer.setText(diff);
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

    private void timesOver(){
        if(bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
        TextView btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog2.dismiss();
                ConstantUtil.isTimeOverShow=false;
                Intent intent = new Intent(mContext,HomeActivity.class);
                startActivity(intent);
                /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                        R.id.fragment_container,
                        new HomeFragment(),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);*/
            }
        });
        bottomSheetDialog2 = new BottomSheetDialog(mContext);
        bottomSheetDialog2.setCancelable(false);
        bottomSheetDialog2.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog2.isShowing() && !ConstantUtil.isTimeOverShow){
            ConstantUtil.isTimeOverShow=true;
            bottomSheetDialog2.show();
        }

    }

    private void showBasicDetailDialog(String from) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        UserModel user=preferences.getUserModel();

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

        EditText name_as_aadhar=view.findViewById(R.id.name_as_aadhar);
        EditText team_name=view.findViewById(R.id.edtTeamName);
        EditText mobile_number=view.findViewById(R.id.mobile_number);
        EditText email=view.findViewById(R.id.email);
        EditText dob=view.findViewById(R.id.dob);
        Spinner spinGender=view.findViewById(R.id.spinGender);
        Button confirm=view.findViewById(R.id.confirm);
        spinState=view.findViewById(R.id.spinState);

        name_as_aadhar.setText(user.getDisplayName());
        team_name.setText(user.getUserTeamName());
        mobile_number.setText(user.getPhoneNo());
        email.setText(user.getEmailId());
        dob.setText(user.getDob());

        selectedState=user.getStateId();

        /*if (TextUtils.isEmpty(user.getEmailId())){
            email.setEnabled(true);
        }else {
            email.setEnabled(false);
        }*/

        team_name.setEnabled(user.getTeam_name_change_allow().equalsIgnoreCase("yes"));

        ArrayList<String> genderList=new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter=new ArrayAdapter(mContext,R.layout.spinner_text,genderList);
        spinGender.setAdapter(genderAdapter);
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedGender=genderList.get(pos);
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
            String db=dob.getText().toString().trim();
            String strEmail=getEditText(email);
            String strName=getEditText(name_as_aadhar);
            String strTeam=getEditText(team_name);

            if (TextUtils.isEmpty(strName)) {
                CustomUtil.showToast(mContext, "Please Enter Name as on Aadhar Card.");
            }
            else if (TextUtils.isEmpty(strTeam)) {
                CustomUtil.showToast(mContext, "Please Enter Team Name.");
            }
            else if (strTeam.length()>11) {
                CustomUtil.showToast(mContext, "Team name should be less than or equals to 11 characters.");
            }
            else if (TextUtils.isEmpty(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Email.");
            }
            else if (!isValidEmail(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Valid Email.");
            }
            else if (selectedState.equalsIgnoreCase("0")) {
                CustomUtil.showToast(mContext, "Please Select State.");
            }
            else if (TextUtils.isEmpty(db)) {
                CustomUtil.showToast(mContext, "Please Enter Date of Birth.");
            }
            else {
                callFirstApi(strName,strEmail,strTeam,db,bottomSheetDialog,from);
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

    private void callFirstApi(String name,String email,String team,String dob,BottomSheetDialog bottomSheetDialog,String from) {
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

                    if (from.equalsIgnoreCase("makeArray")){
                        try {
                            makeArray();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        confirmTeam();
                    }
                } else {
                    CustomUtil.showToast(mContext, responseBody.optString("msg"));
                    LogUtil.e(TAG,"MDHD3:"+responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateLabel( EditText dob) {
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
                    cityName=new ArrayList<String>();
                    cityName.add("Select state");
                    cityId=new ArrayList<String>();
                    cityId.add("0");
                    ArrayList<StateModal> stateModals=new ArrayList<>();
                    int pos=0;
                    if(responseBody.optBoolean("status")){
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            String id = obj.optString("id");
                            if (selectedState.equals(id)){
                                pos=i+1;
                            }
                            cityId.add(id);
                            cityName.add(name);
                            StateModal stateModal = gson.fromJson(obj.toString(), StateModal.class);
                            stateModals.add(stateModal);
                        }
                        preferences.setStateList(stateModals);
                    }
                    ArrayAdapter<String> stateAdapter=new ArrayAdapter<String>(mContext,R.layout.spinner_text,cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState=cityId.get(pos);
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
            public void onFailureResult(String responseBody, int code) {}

        });
    }

}