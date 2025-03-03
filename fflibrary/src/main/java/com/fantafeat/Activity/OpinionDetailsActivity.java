package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.Adapter.OpinionLeaderboardAdapter;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.OpinionLeaderboardModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.MusicManager;
import com.fantafeat.util.WinningTreeSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpinionDetailsActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private EventModel eventModel;
    private boolean isLiveEvent;
    private ImageView back_img,imgShare;
    private ImageView imgConImage;
    private ProgressBar contest_progress;
    private RecyclerView recyclerLeaderboard,recyclerWinning;
    private LinearLayout tabLeaderboard,tabOverview,tabWinning,layLeaderboard,layOverview,layWinning;
    private View viewLeaderboard,viewOverview,viewWinning;
    private TextView txtEndDate,txtMatched,txtLblMatched,txtConTitle,txtFirstPrice,txtWinPer,txtUpto,txtLeaderboard,txtOverview,txtWinning,txtDescription,
            txtSouceAgency,txtStartDate,txtEndDate1,match_rank_pool,contest_price_pool,txtGiveOpinion,contest_left_team,contest_total_team,txtAns;
    private String selectedTag="1",selectedOpinionTime="";//1=Leaderboard, 2=Overview, 3=Winning
    private int offset=0,limit=100;
    private OpinionLeaderboardAdapter opinionLeaderboardAdapter;

    private ArrayList<OpinionLeaderboardModel> leaderboardList;

    /*float  use_deposit = 0;
    float  use_transfer =  0;
    float  use_winning =  0;*/
    boolean isGetData, isApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_details);

        eventModel=(EventModel) getIntent().getSerializableExtra("eventModel");
        isLiveEvent=getIntent().getBooleanExtra("isLiveEvent",false);

        back_img=findViewById(R.id.back_img);
        txtEndDate=findViewById(R.id.txtEndDate);
        txtMatched=findViewById(R.id.txtMatched);
        txtLblMatched=findViewById(R.id.txtLblMatched);
        txtConTitle=findViewById(R.id.txtConTitle);
        txtFirstPrice=findViewById(R.id.txtFirstPrice);
        txtWinPer=findViewById(R.id.txtWinPer);
        txtUpto=findViewById(R.id.txtUpto);
        imgConImage=findViewById(R.id.imgConImage);
        tabLeaderboard=findViewById(R.id.tabLeaderboard);
        tabOverview=findViewById(R.id.tabOverview);
        tabWinning=findViewById(R.id.tabWinning);
        txtLeaderboard=findViewById(R.id.txtLeaderboard);
        txtOverview=findViewById(R.id.txtOverview);
        txtWinning=findViewById(R.id.txtWinning);
        viewLeaderboard=findViewById(R.id.viewLeaderboard);
        viewOverview=findViewById(R.id.viewOverview);
        viewWinning=findViewById(R.id.viewWinning);
        layLeaderboard=findViewById(R.id.layLeaderboard);
        layOverview=findViewById(R.id.layOverview);
        layWinning=findViewById(R.id.layWinning);
        recyclerLeaderboard=findViewById(R.id.recyclerLeaderboard);
        txtDescription=findViewById(R.id.txtDescription);
        txtSouceAgency=findViewById(R.id.txtSouceAgency);
        txtStartDate=findViewById(R.id.txtStartDate);
        txtEndDate1=findViewById(R.id.txtEndDate1);
        recyclerWinning=findViewById(R.id.recyclerWinning);
        match_rank_pool=findViewById(R.id.match_rank_pool);
        contest_price_pool=findViewById(R.id.contest_price_pool);
        txtGiveOpinion=findViewById(R.id.txtGiveOpinion);
        contest_left_team=findViewById(R.id.contest_left_team);
        contest_total_team=findViewById(R.id.contest_total_team);
        contest_progress=findViewById(R.id.contest_progress);
        imgShare=findViewById(R.id.imgShare);
        txtAns=findViewById(R.id.txtAns);

        isGetData = false;
        isApiCall = false;

        leaderboardList=new ArrayList<>();

        recyclerWinning.setLayoutManager(new LinearLayoutManager(mContext));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerLeaderboard.setLayoutManager(linearLayoutManager);
        opinionLeaderboardAdapter=new OpinionLeaderboardAdapter(mContext, leaderboardList, isLiveEvent);
        recyclerLeaderboard.setAdapter(opinionLeaderboardAdapter);

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(eventModel.getWinningTree());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MatchRankAdapter matchRank = new MatchRankAdapter(mContext, jsonArray);
        recyclerWinning.setHasFixedSize(true);
        recyclerWinning.setAdapter(matchRank);

        match_rank_pool.setText(CustomUtil.displayAmountFloat(mContext,eventModel.getDistributeAmount()));

        initClick1();
        initData();
        getLeaderboardData();

        if (!isLiveEvent){
            txtGiveOpinion.setVisibility(View.GONE);
        }else {
            txtGiveOpinion.setVisibility(View.VISIBLE);
        }

        back_img.setOnClickListener(view -> {
            finish();
        });

        txtGiveOpinion.setOnClickListener(view -> {
            showConfirmOpinionContestDialog(eventModel);
        });

        recyclerLeaderboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == leaderboardList.size() - 1
                        && isGetData && isApiCall) {
                    getLeaderboardData();
                }
            }
        });

        imgShare.setOnClickListener(view -> {
            shareContest(eventModel);
        });

    }

    private void showConfirmOpinionContestDialog(EventModel model) {

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(model.getEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());//95.15f;
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());//5010.39f;
        final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());

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

        float calBalance=deposit + transfer_bal + winning;

        if (calBalance < entryFee[0]) {
            CustomUtil.showToast(mContext,"Insufficient Balance");
            double amt=Math.ceil(entryFee[0] -calBalance);
            if (amt<1){
                amt=amt+1;
            }
            int intamt=(int) amt;
            if (intamt<amt){
                intamt+=1;
            }
            String payableAmt=String.valueOf(intamt);
            startActivity(new Intent(mContext,AddDepositActivity.class)
                    .putExtra("depositAmt",payableAmt));
        }
        else {
            DecimalFormat format = CustomUtil.getFormater("00.00");
            String rs=mContext.getResources().getString(R.string.rs);

            UserModel userModel=preferences.getUserModel();
            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.opinion_confirm_dialog);

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);
            TextView txtTrades=dialog.findViewById(R.id.txtTrades);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc=dialog.findViewById(R.id.txtDesc);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);

            TextView txtIntegerEntry=dialog.findViewById(R.id.txtIntegerEntry);
            EditText edtInteger=dialog.findViewById(R.id.edtInteger);

            LinearLayout layInteger=dialog.findViewById(R.id.layInteger);

            //TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtTimeEntry=dialog.findViewById(R.id.txtTimeEntry);
            EditText edtTime=dialog.findViewById(R.id.edtTime);
            LinearLayout layTime=dialog.findViewById(R.id.layTime);

            txtEndDate.setText("Ends on "+CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
            txtConTitle.setText(model.getConDesc());
            txtDesc.setText(model.getConSubDesc());
            txtTrades.setText(model.getTotalTrades()+" Trades");

            if (model.getCon_type().equalsIgnoreCase("Time")){
                layTime.setVisibility(View.VISIBLE);
                layInteger.setVisibility(View.GONE);

                txtTimeEntry.setText(rs+model.getEntryFee());

                edtTime.setOnClickListener(view -> {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            (view12, year, monthOfYear, dayOfMonth) -> {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                                        (view1, hourOfDay, minute) -> {
                                            Calendar cal = Calendar.getInstance();
                                            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                            cal.set(Calendar.MINUTE,minute);

                                            Format formatter,submitFormat;
                                            formatter = new SimpleDateFormat("h:mm a");
                                            edtTime.setText( year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth+" "+formatter.format(cal.getTime()));

                                            submitFormat=new SimpleDateFormat("HH:mm");
                                            selectedOpinionTime=year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth+" "+submitFormat.format(cal.getTime())+":00";

                                        }, mHour, mMinute, false);
                                timePickerDialog.show();

                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                });

                /*txtSubmitTime.setOnClickListener(view -> {
                    String value=edtTime.getText().toString().trim();
                    if (TextUtils.isEmpty(value)){
                        CustomUtil.showTopSneakWarning(mContext,"Enter Date and Time");
                        return;
                    }
                    model.setOptionValue(value);

                });*/

            }
            else if (model.getCon_type().equalsIgnoreCase("Integer")){
                layTime.setVisibility(View.GONE);
                layInteger.setVisibility(View.VISIBLE);

                txtIntegerEntry.setText(rs+model.getEntryFee());

               /* txtSubmitInteger.setOnClickListener(view -> {
                    String value=edtInteger.getText().toString().trim();
                    if (TextUtils.isEmpty(value)){
                        CustomUtil.showTopSneakWarning(context,"Enter Value");
                        return;
                    }
                    model.setOptionValue(value);
                    listener.onItemClick(model);
                });*/

            }

            model.setSelectedPrice(entryFee[0]+"");
            //txtInvest.setText(entryFee[0]+"");

            join_contest_btn.setOnClickListener(view -> {
                if (model.getCon_type().equalsIgnoreCase("Time")){
                    //String time=edtTime.getText().toString().trim();
                    if (TextUtils.isEmpty(selectedOpinionTime)){
                        CustomUtil.showTopSneakWarning(mContext,"Select Time");
                        return;
                    }
                    dialog.dismiss();
                    model.setOptionValue(selectedOpinionTime);
                    model.setCon_join_qty("1");
                    joinPollContest(model);

                }else if (model.getCon_type().equalsIgnoreCase("Integer")){
                    String integer=edtInteger.getText().toString().trim();
                    if (TextUtils.isEmpty(integer)){
                        CustomUtil.showTopSneakWarning(mContext,"Enter Value");
                        return;
                    }
                    dialog.dismiss();
                    model.setCon_join_qty("1");
                    model.setOptionValue(integer);
                    joinPollContest(model);
                }

            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        }
    }

    private void joinPollContest(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("user_team_name", preferences.getUserModel().getUserTeamName());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", model.getId());
            jsonObject.put("entry_fee", model.getEntryFee());
            jsonObject.put("option_value", model.getOptionValue());
            jsonObject.put("con_join_qty", model.getCon_join_qty());
            LogUtil.e("resp","param: "+jsonObject+" \n url: "+ ApiManager.joinPollContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinPollContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinPollContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    selectedOpinionTime="";
                    getUserDetails(model);
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

                //preferences.setUserModel(new UserModel());

            }
        });
    }

    private void getUserDetails(EventModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                    preferences.setUserModel(userModel);
                    showOrderSuccessDialog(model);
                }

            }
            @Override
            public void onFailureResult(String responseBody, int code) {}
        });
    }

    private void showOrderSuccessDialog(EventModel model) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.order_success_dialog, null);

        Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.order_success_dialog);

        String rs=mContext.getResources().getString(R.string.rs);

        TextView txtOrderQty=dialog.findViewById(R.id.txtOrderQty);
        TextView txtOrderPrice=dialog.findViewById(R.id.txtOrderPrice);
        TextView txtOrderTotal=dialog.findViewById(R.id.txtOrderTotal);
        TextView txtTitle=dialog.findViewById(R.id.txtTitle);
        Button btnShare=dialog.findViewById(R.id.btnShare);
        Button btnMore=dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect=dialog.findViewById(R.id.imgCorrect);

//        Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);

        txtTitle.setText("Opinion Order placed!");

        txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs+model.getSelectedPrice());
        float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs+total+"");

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();

            leaderboardList=new ArrayList<>();
            opinionLeaderboardAdapter.updateList(leaderboardList);
            offset=0;

            getLeaderboardData();
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
        String url= ConstantUtil.LINK_OPINION_URL+eventModel.getId();;

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
                        if (task.getResult().getShortLink()!=null)
                            shareShortLink(task.getResult().getShortLink(),eventModel);
                        else {
                            CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                        }
                    } else {
                        CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                    }
                });*/
    }

    private void shareShortLink(Uri link,EventModel eventModel) {
        String content=//ConstantUtil.LINK_URL+"\n\n"+
                "*"+eventModel.getConDesc()+"* \uD83C\uDFC6"+
                        "\n\nwhat do you think will happen ❓\n\n"+
                        "\uD83D\uDCC8Trade on *Fantafeat*.\n\n"+
                        "\uD83D\uDCB8Earning is simple here.\n\n"+
                        "Deadline⏳ : "+CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"dd MMM, yyyy hh:mm a")+
                        "\n\nTap below link for join:\uD83D\uDCF2" +
                        "\n"+link.toString().trim()/*+MyApp.getMyPreferences().getMatchModel().getId()+"/"+list.getId()*/;

        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }catch (ActivityNotFoundException e){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }
    }

    private void getLeaderboardData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", eventModel.getId());
            jsonObject.put("limit", limit);
            jsonObject.put("offset", offset);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("TAG", "getLeaderboardData : " + jsonObject.toString());
        boolean isLoader=true;
        isApiCall = false;

        HttpRestClient.postJSONNormal(mContext, isLoader, ApiManager.opinionContestDetailLeaderboard, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                isApiCall = true;
                LogUtil.e("TAG", "getLeaderboardData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data=responseBody.optJSONObject("data");
                    JSONArray my_join=data.optJSONArray("my_join");
                    JSONArray other_join=data.optJSONArray("other_join");

                    for (int i = 0; i < my_join.length(); i++) {
                        JSONObject obj=my_join.optJSONObject(i);
                        OpinionLeaderboardModel model=gson.fromJson(obj.toString(),OpinionLeaderboardModel.class);
                        model.setMyOpinion(true);
                        leaderboardList.add(model);
                    }

                    for (int i = 0; i < other_join.length(); i++) {
                        JSONObject obj=other_join.optJSONObject(i);
                        OpinionLeaderboardModel model=gson.fromJson(obj.toString(),OpinionLeaderboardModel.class);
                        model.setMyOpinion(false);
                        leaderboardList.add(model);
                    }

                    opinionLeaderboardAdapter.updateList(leaderboardList);

                    if (other_join.length() < limit) {
                        isGetData = false;
                        offset = 0;
                    } else {
                        offset += other_join.length();
                        isGetData = true;
                    }

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void initClick1() {

        Typeface normalTypeFace = ResourcesCompat.getFont(this, R.font.roboto_regular);
        Typeface boldTypeFace = ResourcesCompat.getFont(this, R.font.roboto_bold);

        tabLeaderboard.setOnClickListener(view -> {
            selectedTag="1";
            txtLeaderboard.setTextColor(getResources().getColor(R.color.dark_blue));
            txtLeaderboard.setTypeface(boldTypeFace);
            viewLeaderboard.setVisibility(View.VISIBLE);

            txtOverview.setTextColor(getResources().getColor(R.color.gray_686868));
            txtOverview.setTypeface(normalTypeFace);
            viewOverview.setVisibility(View.GONE);

            txtWinning.setTextColor(getResources().getColor(R.color.gray_686868));
            txtWinning.setTypeface(normalTypeFace);
            viewWinning.setVisibility(View.GONE);

            layLeaderboard.setVisibility(View.VISIBLE);
            layWinning.setVisibility(View.GONE);
            layOverview.setVisibility(View.GONE);
        });

        tabOverview.setOnClickListener(view -> {
            selectedTag="2";
            txtOverview.setTextColor(getResources().getColor(R.color.dark_blue));
            txtOverview.setTypeface(boldTypeFace);
            viewOverview.setVisibility(View.VISIBLE);

            txtLeaderboard.setTextColor(getResources().getColor(R.color.gray_686868));
            txtLeaderboard.setTypeface(normalTypeFace);
            viewLeaderboard.setVisibility(View.GONE);

            txtWinning.setTextColor(getResources().getColor(R.color.gray_686868));
            txtWinning.setTypeface(normalTypeFace);
            viewWinning.setVisibility(View.GONE);

            layLeaderboard.setVisibility(View.GONE);
            layWinning.setVisibility(View.GONE);
            layOverview.setVisibility(View.VISIBLE);
        });

        tabWinning.setOnClickListener(view -> {
            selectedTag="3";
            txtWinning.setTextColor(getResources().getColor(R.color.dark_blue));
            txtWinning.setTypeface(boldTypeFace);
            viewWinning.setVisibility(View.VISIBLE);

            txtLeaderboard.setTextColor(getResources().getColor(R.color.gray_686868));
            txtLeaderboard.setTypeface(normalTypeFace);
            viewLeaderboard.setVisibility(View.GONE);

            txtOverview.setTextColor(getResources().getColor(R.color.gray_686868));
            txtOverview.setTypeface(normalTypeFace);
            viewOverview.setVisibility(View.GONE);

            layLeaderboard.setVisibility(View.GONE);
            layWinning.setVisibility(View.VISIBLE);
            layOverview.setVisibility(View.GONE);
        });
    }

    private void initData() {
        String rs=mContext.getResources().getString(R.string.rs);

        txtEndDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm aa"));
        txtEndDate1.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm aa"));
        txtStartDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConStartTime(),"MMM dd hh:mm aa"));
        txtConTitle.setText(eventModel.getConDesc());
        txtMatched.setText(eventModel.getTotalTrades());
        txtDescription.setText(eventModel.getCon_overview());
        txtSouceAgency.setText(eventModel.getSource_agency());
        contest_price_pool.setText(CustomUtil.displayAmountFloat( mContext, eventModel.getDistributeAmount()));
        txtGiveOpinion.setText("Entry : "+rs+eventModel.getEntryFee());

        int totalSpots = CustomUtil.convertInt(eventModel.getNo_of_spot());
        int jointeam = CustomUtil.convertInt(eventModel.getTotalTrades());
        int totalWin=Integer.parseInt(eventModel.getNo_of_winners())*100/totalSpots;

        txtWinPer.setText(totalWin+"%");
        txtUpto.setText("Up to "+eventModel.getJoin_spot_limit());
        if (!TextUtils.isEmpty(eventModel.getConImage())){
            CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,eventModel.getConImage(),R.drawable.event_placeholder);
        }else
            imgConImage.setImageResource(R.drawable.event_placeholder);

        int left=totalSpots - jointeam;
        contest_left_team.setText(left+"");
        contest_total_team.setText(eventModel.getNo_of_spot());

        if (TextUtils.isEmpty(eventModel.getConWinning())){
            txtAns.setVisibility(View.GONE);
        }else {
            txtAns.setVisibility(View.VISIBLE);
            txtAns.setText("Answer is "+eventModel.getConWinning());
        }

        int progress = (jointeam * 100) / totalSpots;
        if (progress==0 && jointeam>0)
            progress+=1;

        contest_progress.setProgress(progress);

        try{
            JSONArray winningTree=new JSONArray(eventModel.getWinningTree());
            if (winningTree.length()>0)
                txtFirstPrice.setText(rs+winningTree.optJSONObject(0).optString("amount"));

        }catch (Exception e){
            e.printStackTrace();
        }

        txtFirstPrice.setOnClickListener(view -> {
            WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, eventModel.getDistributeAmount(), eventModel.getWinningTree()
                    ,txtFirstPrice.getText().toString());
            bottomSheetTeam.show(((OpinionDetailsActivity) mContext).getSupportFragmentManager(),
                    BottomSheetTeam.TAG);
            /*WinningTreeSheet bottomSheetTeam = new WinningTreeSheet( mContext, eventModel.getDistributeAmount(), eventModel.getWinningTree(),false);
            bottomSheetTeam.show(((OpinionDetailsActivity)  mContext).getSupportFragmentManager(),
                    BottomSheetTeam.TAG);*/
        });

    }
}