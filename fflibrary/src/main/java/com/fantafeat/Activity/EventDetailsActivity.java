package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fantafeat.Adapter.EventDetailAdapter;
import com.fantafeat.Model.AvailableQtyModel;
import com.fantafeat.Model.EventDetailModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventDetailsActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private EventModel eventModel;
    private TextView txtInvst,txtReturn,txtEndDate,txtConTitle,txtDesc,txtMatchedVal,txtUnMatched,txtMatched,txtCancelled,
            txtLblMatched,txtLblReturn,txtEventDetail,txtWinAmt,txtQtyAtPrice;
    private View viewUnMatched,viewMatched,viewCancelled,viewSaperator;
    private ImageView imgConImage;
    private RecyclerView recyclerEvent;
    private ImageView back_img;
    private LinearLayout layDesc,layMain,tabUnMatched,tabMatched,tabCancelled,layWinning;

    private String selectedTag="1";//1=unmatched or Refunded, 2=matched or Settled, 3=cancelled
    private ArrayList<EventDetailModel> list;
    private EventDetailAdapter adapter;
    private JSONObject data;
    private LinearLayout nodata;
    private SwipeRefreshLayout swipe;
    private boolean isLiveEvent;
    //private Socket mSocket= null;
    private EventDetailModel dialogDetailModel;
    private  Dialog dialog;
    private ArrayList<AvailableQtyModel> yesList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> noList=new ArrayList<>();
    private EventDetailModel modelEventDetail;;
    private int selectedPrice=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //mSocket=MyApp.getInstance().getSocketInstance();

        eventModel= (EventModel) getIntent().getSerializableExtra("eventModel");
        isLiveEvent=getIntent().getBooleanExtra("isLiveEvent",false);

        txtInvst=findViewById(R.id.txtInvst);
        txtReturn=findViewById(R.id.txtReturn);
        txtEndDate=findViewById(R.id.txtEndDate);
        imgConImage=findViewById(R.id.imgConImage);
        txtConTitle=findViewById(R.id.txtConTitle);
        txtDesc=findViewById(R.id.txtDesc);
        layDesc=findViewById(R.id.layDesc);
        txtMatchedVal=findViewById(R.id.txtMatchedVal);
        layMain=findViewById(R.id.layMain);
        back_img=findViewById(R.id.back_img);
        txtLblMatched=findViewById(R.id.txtLblMatched);
        viewSaperator=findViewById(R.id.viewSaperator);
        txtLblReturn=findViewById(R.id.txtLblReturn);

        tabUnMatched=findViewById(R.id.tabUnMatched);
        tabMatched=findViewById(R.id.tabMatched);
        tabCancelled=findViewById(R.id.tabCancelled);
        txtUnMatched=findViewById(R.id.txtUnMatched);
        viewUnMatched=findViewById(R.id.viewUnMatched);
        txtMatched=findViewById(R.id.txtMatched);
        viewMatched=findViewById(R.id.viewMatched);
        txtCancelled=findViewById(R.id.txtCancelled);
        viewCancelled=findViewById(R.id.viewCancelled);
        nodata=findViewById(R.id.nodata);
        recyclerEvent=findViewById(R.id.recyclerEvent);
        swipe=findViewById(R.id.swipe);
        txtEventDetail=findViewById(R.id.txtEventDetail);
        layWinning=findViewById(R.id.layWinning);
        txtWinAmt=findViewById(R.id.txtWinAmt);

        Typeface normalTypeFace = ResourcesCompat.getFont(this, R.font.roboto_regular);
        Typeface boldTypeFace = ResourcesCompat.getFont(this, R.font.roboto_bold);

        if (isLiveEvent){
            tabUnMatched.setVisibility(View.VISIBLE);

            txtUnMatched.setText("Unmatched");
            txtMatched.setText("Matched");
            txtCancelled.setText("Cancelled");

            selectedTag="1";
            txtUnMatched.setTextColor(getResources().getColor(R.color.blackPrimary));
            txtUnMatched.setTypeface(boldTypeFace);
            viewUnMatched.setVisibility(View.VISIBLE);

            txtMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtMatched.setTypeface(normalTypeFace);
            viewMatched.setVisibility(View.GONE);

            txtCancelled.setTextColor(getResources().getColor(R.color.gray_686868));
            txtCancelled.setTypeface(normalTypeFace);
            viewCancelled.setVisibility(View.GONE);

            txtEventDetail.setVisibility(View.VISIBLE);
        }
        else {
            txtEventDetail.setVisibility(View.GONE);

            tabUnMatched.setVisibility(View.GONE);

            txtCancelled.setText("Refunded");
            txtMatched.setText("Settled");

            selectedTag="2";
            txtMatched.setTextColor(getResources().getColor(R.color.blackPrimary));
            txtMatched.setTypeface(boldTypeFace);
            viewMatched.setVisibility(View.VISIBLE);

            txtUnMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtUnMatched.setTypeface(normalTypeFace);
            viewUnMatched.setVisibility(View.GONE);

            txtCancelled.setTextColor(getResources().getColor(R.color.gray_686868));
            txtCancelled.setTypeface(normalTypeFace);
            viewCancelled.setVisibility(View.GONE);
        }

        list=new ArrayList<>();

        recyclerEvent.setLayoutManager(new LinearLayoutManager(mContext));
        adapter=new EventDetailAdapter(mContext, list, this::getOpinionCnt,isLiveEvent);
        recyclerEvent.setAdapter(adapter);

        initData();

        initClick1();

        getData();
    }

    private void showActionDialog(EventDetailModel model,JSONObject data) {

        modelEventDetail=model;

        dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.event_action_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView txtActionTitle=dialog.findViewById(R.id.txtActionTitle);
        TextView txtActionSubTitle=dialog.findViewById(R.id.txtActionSubTitle);
        TextView txtPrice=dialog.findViewById(R.id.txtPrice);
        txtQtyAtPrice=dialog.findViewById(R.id.txtQtyAtPrice);
        TextView txtMaxQtyAvail=dialog.findViewById(R.id.txtMaxQtyAvail);
        TextView txtSelect=dialog.findViewById(R.id.txtSelect);
        EditText edtQty=dialog.findViewById(R.id.edtQty);
        SeekBar seekPrice=dialog.findViewById(R.id.seekPrice);
        Button btnSubmit=dialog.findViewById(R.id.btnSubmit);
        ImageView imgClose=dialog.findViewById(R.id.imgClose);
        LinearLayout layPrice=dialog.findViewById(R.id.layPrice);

        final int[] qty = {0};
        selectedPrice=1;
        String rs=mContext.getResources().getString(R.string.rs);

        yesList.clear();
        noList.clear();

        JSONArray yesCount=data.optJSONArray("yesCount");
        JSONArray noCount=data.optJSONArray("noCount");

        for (int i = 0; i < yesCount.length(); i++) {
            JSONObject object=yesCount.optJSONObject(i);
            AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
            if (CustomUtil.convertFloat(model1.getTotal_join_cnt_sell()) > 0)
                yesList.add(model1);
        }

        for (int i = 0; i < noCount.length(); i++) {
            JSONObject object=noCount.optJSONObject(i);
            AvailableQtyModel model1=gson.fromJson(object.toString(),AvailableQtyModel.class);
            if (CustomUtil.convertFloat(model1.getTotal_join_cnt_sell()) > 0)
                noList.add(model1);
        }

        seekPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedPrice=progress;

                //model.setSelectedPrice(price[0]+"");

                txtPrice.setText(rs+selectedPrice);

                /*float investValue=price[0]*qty[0];
                txtInvest.setText(investValue+"");*/

                setOpinionValue(model.getOption_value(),selectedPrice+"",data,txtQtyAtPrice,model.getTag());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        txtMaxQtyAvail.setText("You have "+model.getAvailCnt()+" available quantity");
        setOpinionValue(model.getOption_value(),selectedPrice+"",data,txtQtyAtPrice,model.getTag());

        edtQty.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                hideKeyboard((EventDetailsActivity) mContext);
                if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                        CustomUtil.convertInt(edtQty.getText().toString().trim())<=0){
                    edtQty.setText("1");
                    edtQty.setSelection(1);
                    qty[0]=1;
                }
                if (CustomUtil.convertInt(edtQty.getText().toString().trim())>CustomUtil.convertInt(model.getAvailCnt())){
                    qty[0] = CustomUtil.convertInt(model.getAvailCnt());
                }
                edtQty.setText(qty[0]+"");
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
                if (TextUtils.isEmpty(charSequence)){
                    qty[0] = 1;
                }else {
                    qty[0] = CustomUtil.convertInt(charSequence.toString());
                }
                if (qty[0]>CustomUtil.convertInt(model.getAvailCnt())){
                    qty[0] = CustomUtil.convertInt(model.getAvailCnt());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtSelect.setOnClickListener(view -> {
            edtQty.setText(model.getAvailCnt());
        });

        String title="",subTitle="";

        if (model.getTag().equalsIgnoreCase("1")){
            title="CANCEL";
            subTitle="Your entered quantity will be cancelled.";
            layPrice.setVisibility(View.GONE);
        }else if (model.getTag().equalsIgnoreCase("2")){
            title="Exit Trade";
            subTitle="You will exit from trade as per your selected quantity and prize.";
            layPrice.setVisibility(View.VISIBLE);
        }
        txtActionTitle.setText(title);
        txtActionSubTitle.setText(subTitle);

        imgClose.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btnSubmit.setOnClickListener(view -> {
            String qtys=edtQty.getText().toString().trim();
            String prices="";

            if (TextUtils.isEmpty(qtys)){
                CustomUtil.showToast(mContext,"Enter quantity");
                return;
            }

            if (CustomUtil.convertInt(qtys)<=0){
                CustomUtil.showToast(mContext,"Enter valid quantity");
                return;
            }

            if (CustomUtil.convertInt(qtys)>CustomUtil.convertInt(model.getAvailCnt())){
                CustomUtil.showToast(mContext,"Enter valid price");
                return;
            }

            if (model.getTag().equalsIgnoreCase("2")){
                prices=selectedPrice+"";

                if (TextUtils.isEmpty(prices)){
                    CustomUtil.showToast(mContext,"Enter price");
                    return;
                }

                if (CustomUtil.convertInt(prices)<=0){
                    CustomUtil.showToast(mContext,"Enter valid price");
                    return;
                }

            }
            //if (model.getTag().equalsIgnoreCase("1")){
            dialog.dismiss();
            submitAction(qtys,model,prices);
            //}
        });

        dialog.show();

    }

    private void setOpinionValue(String option,String price,JSONObject data,TextView txtQtyAtPrice,String tag){

        //JSONArray yesCount=data.optJSONArray("noCount");
        //JSONArray noCount=data.optJSONArray("yesCount");

        txtQtyAtPrice.setText("0 quantities available at "+selectedPrice);

        if (option.equalsIgnoreCase("yes")){
            for (int i = 0; i < noList.size(); i++) {
                AvailableQtyModel model=noList.get(i);
                //JSONObject yesObj=yesCount.optJSONObject(i);
                if (model.getJnEfAmount().equalsIgnoreCase(selectedPrice+"")){
                    if (tag.equalsIgnoreCase("2")){
                        txtQtyAtPrice.setText(model.getTotal_join_cnt_sell()+" quantities available at "+selectedPrice);
                    }
                    else {
                        txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at "+selectedPrice);
                    }
                }
            }
        }else {
            for (int i = 0; i < yesList.size(); i++) {
                AvailableQtyModel model=yesList.get(i);
                //JSONObject yesObj=noCount.optJSONObject(i);
                if (model.getJnEfAmount().equalsIgnoreCase(selectedPrice+"")){
                    if (tag.equalsIgnoreCase("2")){
                        txtQtyAtPrice.setText(model.getTotal_join_cnt_sell()+" quantities available at "+selectedPrice);
                    }
                    else {
                        txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at "+selectedPrice);
                    }
                }
            }
        }
    }

    private void submitAction(String qty,EventDetailModel model,String price) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", eventModel.getId());
            jsonObject.put("entry_fee",model.getJoin_fee());
            jsonObject.put("option_value",model.getOption_value());
            jsonObject.put("con_join_qty",qty);
            if (model.getTag().equalsIgnoreCase("2")){
                jsonObject.put("sell_amt",price);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url="";
        if (model.getTag().equalsIgnoreCase("1")){
            if (model.getIsExitedOrBuy().equalsIgnoreCase(""))
                url= ApiManager.myJoinTradesPendingContestCancel;
            else {
                try {
                    jsonObject.put("sell_amt",model.getSell_bal());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                url=ApiManager.myJoinTradesConfirmContestExitCancel;
            }
        }else {
            url=ApiManager.myJoinTradesConfirmContestExit;
        }
        LogUtil.e("TAG", "Param : " + jsonObject.toString()+"\nUrl: "+url);

        HttpRestClient.postJSONNormal(mContext, true, url, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e("TAG", "getData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    getData();
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }
        });
    }

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MyApp.getMyPreferences().getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", eventModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("TAG", "getData : " + jsonObject.toString());

        boolean isLoader=true;
        if (swipe!=null && swipe.isRefreshing()){
            isLoader=false;
        }

        HttpRestClient.postJSONNormal(mContext, isLoader, ApiManager.myJoinTradesContestListDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
                LogUtil.e("TAG", "getData : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    data=responseBody.optJSONObject("data");
                    //all_cnt_data=data.optJSONArray("all_cnt_data");

                    /*if (isLiveEvent){
                        tabUnMatched.performClick();
                    }else {
                        tabMatched.performClick();
                    }*/

                    displayData();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                if (swipe!=null && swipe.isRefreshing()){
                    swipe.setRefreshing(false);
                }
            }
        });
    }

    private void displayData() {
        list.clear();
        JSONArray all_cnt_data=data.optJSONArray("all_cnt_data");
        for (int i = 0; i < all_cnt_data.length(); i++) {
            JSONObject object=all_cnt_data.optJSONObject(i);
            String join_fee=object.optString("join_fee");
            String option_value=object.optString("option_value");
            String total_win_amount = object.optString("total_win_amount");
            String sell = object.optString("sell");
            String cnt = object.optString("cnt");
            String sold = object.optString("sold");
            // int cnt=object.optInt("cnt");
            int confirm=object.optInt("confirm");
            int pending=object.optInt("pending");
            int exit=object.optInt("_exit");
            // int sell=object.optInt("sell");
            // int sold=object.optInt("sold");

            EventDetailModel model=new EventDetailModel();
            model.setJoin_fee(join_fee);
            model.setOption_value(option_value);
            model.setIsExitedOrBuy("");
            model.setTag(selectedTag);
            model.setTotal_win_amount(total_win_amount);
            model.setSell_cnt(sell);
            model.setTotal_cnt(cnt);
            model.setSold_cnt(sold);
            model.setAvailCnt("0");

            if (selectedTag.equalsIgnoreCase("1")){
                if (pending>0){
                    model.setAvailCnt(pending+"");
                    model.setQuantity(pending+"");
                    list.add(model);
                }

            }
            if (selectedTag.equalsIgnoreCase("2")){
                if (confirm>0){
                    if (confirm==CustomUtil.convertInt(sold) || confirm==CustomUtil.convertInt(sell)){
                        model.setExitButton(false);
                       // model.setQuantity(confirm+"");//confirm
                    }else {
                        int availCnt=confirm-(
                                CustomUtil.convertInt(model.getSold_cnt())+
                                CustomUtil.convertInt(model.getSell_cnt()));
                        LogUtil.e("resp","ExitedQry: "+availCnt);
                        model.setAvailCnt(availCnt+"");
                        //model.setQuantity(availCnt+"");//confirm
                    }
                    if (isLiveEvent){
                        model.setQuantity(confirm+"");
                    }else {
                        int availCnt=confirm-(
                                CustomUtil.convertInt(model.getSold_cnt())+
                                        CustomUtil.convertInt(model.getSell_cnt()));
                        model.setQuantity(availCnt+"");
                    }

                    list.add(model);
                }
            }
            if (selectedTag.equalsIgnoreCase("3")){
                if (exit>0){
                    model.setQuantity(exit+"");
                    list.add(model);
                }
            }
        }

        if (selectedTag.equalsIgnoreCase("1")){
            JSONArray sellList=data.optJSONArray("sellList");
            for (int i = 0; i < sellList.length(); i++) {
                JSONObject object = sellList.optJSONObject(i);
                String join_fee = object.optString("join_fee");
                String option_value = object.optString("option_value");
                String sell_bal = object.optString("sell_bal");
                String total_win_amount = object.optString("total_win_amount");
                /*String total_win_amount = object.optString("total_win_amount");
                int cnt = object.optInt("cnt");
                int confirm = object.optInt("confirm");
                int pending = object.optInt("pending");
                int exit = object.optInt("_exit");
                int sold = object.optInt("sold");*/
                int sell = object.optInt("sell");
                String cnt = object.optString("cnt");

                EventDetailModel model=new EventDetailModel();
                model.setJoin_fee(join_fee);
                model.setSell_bal(sell_bal);
                model.setOption_value(option_value);
                model.setIsExitedOrBuy("Exit");
                model.setTag(selectedTag);
                model.setTotal_win_amount(total_win_amount);
                model.setTotal_cnt(cnt);
                model.setAvailCnt("0");

                if (sell>0){
                    model.setQuantity(sell+"");
                    model.setAvailCnt(sell+"");
                    list.add(model);
                }
            }
        }

        if (selectedTag.equalsIgnoreCase("2")){
            JSONArray soldList=data.optJSONArray("soldList");
            for (int i = 0; i < soldList.length(); i++) {
                JSONObject object = soldList.optJSONObject(i);
                String join_fee = object.optString("join_fee");
                String option_value = object.optString("option_value");
                String sell_bal = object.optString("sell_bal");
                String total_win_amount = object.optString("total_win_amount");
                /*int cnt = object.optInt("cnt");
                int confirm = object.optInt("confirm");
                int pending = object.optInt("pending");
                int exit = object.optInt("_exit");
                int sold = object.optInt("sold");*/
                int confirm = object.optInt("confirm");
                int sold = object.optInt("sold");
                String cnt = object.optString("cnt");

                EventDetailModel model=new EventDetailModel();
                model.setJoin_fee(join_fee);
                model.setSell_bal(sell_bal);
                model.setOption_value(option_value);
                model.setIsExitedOrBuy("Exit");
                model.setTag(selectedTag);
                model.setTotal_win_amount(total_win_amount);
                model.setTotal_cnt(cnt);
                model.setAvailCnt("0");

                if (sold>0){
                    model.setExitButton(false);
                    model.setQuantity(sold+"");
                    list.add(model);
                }
            }
        }

        adapter.updateItem(list);

        if (list.size()>0){
            nodata.setVisibility(View.GONE);
            recyclerEvent.setVisibility(View.VISIBLE);
        }else {
            nodata.setVisibility(View.VISIBLE);
            recyclerEvent.setVisibility(View.GONE);
        }

    }

    private void getOpinionCnt(EventDetailModel model){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", eventModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getOpinionCnt : " + jsonObject.toString());
        HttpRestClient.postJSONNormal(mContext, true, ApiManager.tradesContestListDetailsCount, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getOpinionCnt : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data=responseBody.optJSONObject("data");
                    showActionDialog(model,data);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });

    }

    private void initClick1() {

        Typeface normalTypeFace = ResourcesCompat.getFont(this, R.font.roboto_regular);
        Typeface boldTypeFace = ResourcesCompat.getFont(this, R.font.roboto_bold);

        tabUnMatched.setOnClickListener(view -> {
            selectedTag="1";
            txtUnMatched.setTextColor(getResources().getColor(R.color.blackPrimary));
            txtUnMatched.setTypeface(boldTypeFace);
            viewUnMatched.setVisibility(View.VISIBLE);

            txtMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtMatched.setTypeface(normalTypeFace);
            viewMatched.setVisibility(View.GONE);

            txtCancelled.setTextColor(getResources().getColor(R.color.gray_686868));
            txtCancelled.setTypeface(normalTypeFace);
            viewCancelled.setVisibility(View.GONE);

            displayData();
        });

        tabMatched.setOnClickListener(view -> {
            selectedTag="2";
            txtMatched.setTextColor(getResources().getColor(R.color.blackPrimary));
            txtMatched.setTypeface(boldTypeFace);
            viewMatched.setVisibility(View.VISIBLE);

            txtUnMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtUnMatched.setTypeface(normalTypeFace);
            viewUnMatched.setVisibility(View.GONE);

            txtCancelled.setTextColor(getResources().getColor(R.color.gray_686868));
            txtCancelled.setTypeface(normalTypeFace);
            viewCancelled.setVisibility(View.GONE);

            displayData();
        });

        tabCancelled.setOnClickListener(view -> {
            selectedTag="3";
            txtCancelled.setTextColor(getResources().getColor(R.color.blackPrimary));
            txtCancelled.setTypeface(boldTypeFace);
            viewCancelled.setVisibility(View.VISIBLE);

            txtUnMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtUnMatched.setTypeface(normalTypeFace);
            viewUnMatched.setVisibility(View.GONE);

            txtMatched.setTextColor(getResources().getColor(R.color.gray_686868));
            txtMatched.setTypeface(normalTypeFace);
            viewMatched.setVisibility(View.GONE);

            displayData();
        });

        txtEventDetail.setOnClickListener(view -> {
            startActivity(new Intent(mContext, UpcomingEventDetailsActivity.class)
                    .putExtra("eventModel",eventModel)
                    .putExtra("selectedTag","1"));
        });

        swipe.setOnRefreshListener(this::getData);

        back_img.setOnClickListener(view -> {
            finish();
        });

    }

    private void initData() {

        String rs=getResources().getString(R.string.rs);

        if (isLiveEvent){
            txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd HH:mm a"));

            txtMatchedVal.setText(eventModel.getMy_total_confirm_trades()+" / "+eventModel.getMy_total_trades_count());
            txtLblMatched.setText(" Matched");

            viewSaperator.setVisibility(View.GONE);
            if (TextUtils.isEmpty(eventModel.getConSubDesc())){
                layDesc.setVisibility(View.GONE);
            }else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(eventModel.getConSubDesc());
            }
            txtLblReturn.setText("  Current Returns ");

            txtLblReturn.setTextColor(this.getResources().getColor(R.color.dark_blue));
            txtReturn.setTextColor(this.getResources().getColor(R.color.dark_blue));

            txtLblReturn.setVisibility(View.GONE);
            txtReturn.setVisibility(View.GONE);

        }
        else {
            txtEndDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd HH:mm a"));
            txtMatchedVal.setText(eventModel.getMy_total_trades_count());
            txtLblMatched.setText(" Trades");
            layDesc.setVisibility(View.GONE);
            viewSaperator.setVisibility(View.VISIBLE);

            /*txtLblReturn.setText("  Winning ");
            if (CustomUtil.convertFloat(eventModel.getWinAmount())>0){
                txtLblReturn.setTextColor(this.getResources().getColor(R.color.green));
                txtReturn.setTextColor(this.getResources().getColor(R.color.green));
            }else {
                txtLblReturn.setTextColor(this.getResources().getColor(R.color.yellow_golden));
                txtReturn.setTextColor(this.getResources().getColor(R.color.yellow_golden));
            }*/

            if (CustomUtil.convertFloat(eventModel.getWinAmount())>0){
                layWinning.setVisibility(View.VISIBLE);
                txtWinAmt.setText("Won "+rs+eventModel.getWinAmount() );
            }else {
                layWinning.setVisibility(View.GONE);
            }

            txtLblReturn.setVisibility(View.GONE);
            txtReturn.setVisibility(View.GONE);
        }

        //txtMatchedVal.setText(eventModel.getMy_total_confirm_trades()+" / "+eventModel.getMy_total_trades_count());

        txtConTitle.setText(eventModel.getConDesc());

        if (!TextUtils.isEmpty(eventModel.getConImage())){
            CustomUtil.loadImageWithGlide(this,imgConImage, MyApp.imageBase + MyApp.document,eventModel.getConImage(),R.drawable.event_placeholder);
        }

        txtInvst.setText(rs+ eventModel.getInvestmentAmount() );
        txtReturn.setText(rs+eventModel.getWinAmount() );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLiveEvent){
         /*   if (mSocket!=null){
                if (!mSocket.connected())
                    mSocket.connect();
                listener();
            }*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (mSocket!=null){
            mSocket.off("res");
        }*/
    }

    private void listener() {
        /*if (mSocket!=null) {
            mSocket.off("res");
            mSocket.on("res", args -> {
                if (args!=null){
                    try {
                        Log.e("resp","trade_unmatch_count: "+args[0]);
                        JSONObject object=new JSONObject(args[0].toString());
                        if (object.optString("en").equalsIgnoreCase("trade_unmatch_count")){
                            if (eventModel!=null && dialog!=null && dialog.isShowing()){
                                JSONArray data=object.optJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj=data.optJSONObject(i);
                                    String contest_id=obj.optString("contest_id");
                                    if (contest_id.equalsIgnoreCase(eventModel.getId())){
                                        JSONArray yesCount=obj.optJSONArray("yesCount");
                                        JSONArray noCount=obj.optJSONArray("noCount");
                                        yesList.clear();
                                        noList.clear();
                                        for (int j = 0; j < yesCount.length(); j++) {
                                            JSONObject yesObj=yesCount.optJSONObject(j);
                                            AvailableQtyModel model1=gson.fromJson(yesObj.toString(),AvailableQtyModel.class);
                                            if (CustomUtil.convertFloat(model1.getTotal_join_cnt_sell()) > 0)
                                                yesList.add(model1);
                                        }

                                        for (int j = 0; j < noCount.length(); j++) {
                                            JSONObject noObj=noCount.optJSONObject(j);
                                            AvailableQtyModel model1=gson.fromJson(noObj.toString(),AvailableQtyModel.class);
                                            if (CustomUtil.convertFloat(model1.getTotal_join_cnt_sell()) > 0)
                                                noList.add(model1);
                                        }

                                        if (txtQtyAtPrice!=null && dialog!=null && dialog.isShowing()){
                                            if (modelEventDetail.getOption_value().equalsIgnoreCase("yes")){
                                                for (int j = 0; j < noList.size(); j++) {
                                                    AvailableQtyModel model=noList.get(j);
                                                    if (model.getJnEfAmount().equalsIgnoreCase(String.valueOf(selectedPrice))){
                                                        if (modelEventDetail.getTag().equalsIgnoreCase("2")){
                                                            txtQtyAtPrice.setText(model.getTotal_join_cnt_sell()+" quantities available at "+selectedPrice);
                                                        }
                                                        else {
                                                            txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at "+selectedPrice);
                                                        }
                                                    }
                                                }
                                            }else {
                                                for (int j = 0; j < yesList.size(); j++) {
                                                    AvailableQtyModel model=yesList.get(j);
                                                    //JSONObject yesObj=noCount.optJSONObject(i);
                                                    if (model.getJnEfAmount().equalsIgnoreCase(String.valueOf(selectedPrice))){
                                                        if (modelEventDetail.getTag().equalsIgnoreCase("2")){
                                                            txtQtyAtPrice.setText(model.getTotal_join_cnt_sell()+" quantities available at "+selectedPrice);
                                                        }
                                                        else {
                                                            txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at "+selectedPrice);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        //Collections.sort(yesList, new prizePoolUp());
                                        //Collections.sort(noList, new prizePoolUp());

                                    }
                                }
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }

}