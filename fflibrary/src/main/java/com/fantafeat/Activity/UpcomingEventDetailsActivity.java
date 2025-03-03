package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantafeat.Model.AvailableQtyModel;
import com.fantafeat.Model.EventModel;
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
import com.fantafeat.util.MusicManager;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpcomingEventDetailsActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    private EventModel eventModel;
    private TextView txtYes,txtNo,txtEndDate,txtConTitle,txtDesc,txtTrades,txtTradesDialog,txtNewBadge,txtDescription,txtSouceAgency,txtStartDate,txtEndDate1,
            txtMaxQtyAtPrice,txtQtyAtPrice;
    private ImageView imgConImage;
    private ImageView imgShare,back_img;
    private LinearLayout layDesc,layMain/*,layTrade*/;
    private String selectedTag;
   // private Socket mSocket= null;
    private BottomSheetDialog dialog;
    private ArrayList<AvailableQtyModel> yesList=new ArrayList<>();
    private ArrayList<AvailableQtyModel> noList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_event_details);

        //mSocket=MyApp.getInstance().getSocketInstance();

        eventModel=(EventModel) getIntent().getSerializableExtra("eventModel");
        selectedTag=getIntent().getStringExtra("selectedTag");

        txtYes=findViewById(R.id.txtYes);
        txtNo=findViewById(R.id.txtNo);
        txtEndDate=findViewById(R.id.txtEndDate);
        imgConImage=findViewById(R.id.imgConImage);
        txtConTitle=findViewById(R.id.txtConTitle);
        txtDesc=findViewById(R.id.txtDesc);
        layDesc=findViewById(R.id.layDesc);
        txtTrades=findViewById(R.id.txtTrades);
        layMain=findViewById(R.id.layMain);
        txtNewBadge=findViewById(R.id.txtNewBadge);
        txtDescription=findViewById(R.id.txtDescription);
        txtSouceAgency=findViewById(R.id.txtSouceAgency);
        txtStartDate=findViewById(R.id.txtStartDate);
        txtEndDate1=findViewById(R.id.txtEndDate1);
        imgShare=findViewById(R.id.imgShare);
        back_img=findViewById(R.id.back_img);
        //layTrade=findViewById(R.id.layTrade);

        if (eventModel.isNewBadge()){
            txtNewBadge.setVisibility(View.VISIBLE);
        }else {
            txtNewBadge.setVisibility(View.GONE);
        }

        txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm a"));

        txtConTitle.setText(eventModel.getConDesc());
        if (TextUtils.isEmpty(eventModel.getConSubDesc())){
            layDesc.setVisibility(View.GONE);
        } else {
            layDesc.setVisibility(View.VISIBLE);
            txtDesc.setText(eventModel.getConSubDesc());
        }

        String rs=mContext.getResources().getString(R.string.rs);

        if (!TextUtils.isEmpty(eventModel.getConImage())) {
            CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,eventModel.getConImage(),R.drawable.event_placeholder);
        }else
            imgConImage.setImageResource(R.drawable.event_placeholder);

        txtYes.setText(eventModel.getOption1() + " | "+rs+eventModel.getOption1Price());
        txtNo.setText(eventModel.getOption2() + " | "+rs+eventModel.getOption2Price());
        txtTrades.setText(eventModel.getTotalTrades()+" Trades");

        txtDescription.setText(eventModel.getCon_overview());
        txtSouceAgency.setText(eventModel.getSource_agency());
        txtStartDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConStartTime(),"MMM dd yyyy hh:mm a"));
        txtEndDate1.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd yyyy hh:mm a"));

        txtYes.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption1());
            eventModel.setOptionEntryFee(eventModel.getOption1Price());
            getOpinionCnt();
        });

        txtNo.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption2());
            eventModel.setOptionEntryFee(eventModel.getOption2Price());
            getOpinionCnt();
        });

        imgShare.setOnClickListener(view -> {
            shareContest();
        });

        back_img.setOnClickListener(view -> {
            finish();
        });

    }

    private void shareContest() {
        String url= ConstantUtil.LINK_TRADING_URL+eventModel.getId();
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
                            shareShortLink(task.getResult().getShortLink());
                        else {
                            CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                        }
                    } else {
                        CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                    }
                });*/
    }

    private void shareShortLink(Uri link) {
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

    private void getOpinionCnt(){
        //

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
                    showConfirmContestDialog(data);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });

    }

    private void showConfirmContestDialog(JSONObject data) {

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(eventModel.getOptionEntryFee())};
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

        float calBalance=deposit + transfer_bal + winning + ff_coin;

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
                    CustomUtil.convertFloat(userModel.getFf_coin())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

           // tradingDialogId=model.getId();
            //dialogEventModel=model;

            dialog=new BottomSheetDialog(mContext);
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

            EditText edtQty=dialog.findViewById(R.id.edtQty);
            edtQty.setText("1");
            TextView txtPrice=dialog.findViewById(R.id.txtPrice);
            txtPrice.setText(rs+"1");
            TextView txtOptionYes=dialog.findViewById(R.id.txtOptionYes);
            TextView txtOptionNo=dialog.findViewById(R.id.txtOptionNo);
            txtQtyAtPrice=dialog.findViewById(R.id.txtQtyAtPrice);
            txtMaxQtyAtPrice=dialog.findViewById(R.id.txtMaxQtyAtPrice);
            TextView txtSelect=dialog.findViewById(R.id.txtSelect);

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);
            ImageView imgConImage=dialog.findViewById(R.id.imgConImage);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtDesc=dialog.findViewById(R.id.txtDesc);
            LinearLayout layDesc=dialog.findViewById(R.id.layDesc);
            txtTradesDialog=dialog.findViewById(R.id.txtTrades);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);
            TextView txtGet=dialog.findViewById(R.id.txtGet);
            TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtNewBadge=dialog.findViewById(R.id.txtNewBadge);
            TextView txtLblCommision=dialog.findViewById(R.id.txtLblCommision);
            TextView txtBtn10=dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50=dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100=dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500=dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750=dialog.findViewById(R.id.txtBtn750);

            txtLblCommision.setText("Fee of "+eventModel.getCommission()+"% on credit amount, 0% fee on loss.");

            txtEndDate.setText("Ends on "+CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm a"));

            if (eventModel.isNewBadge()){
                txtNewBadge.setVisibility(View.VISIBLE);
            }else {
                txtNewBadge.setVisibility(View.GONE);
            }

            txtConTitle.setText(eventModel.getConDesc());
            if (TextUtils.isEmpty(eventModel.getConSubDesc())){
                layDesc.setVisibility(View.GONE);
            }else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(eventModel.getConSubDesc());
            }

            if (!TextUtils.isEmpty(eventModel.getConImage())){
                CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,eventModel.getConImage(),R.drawable.event_placeholder);
            }

            txtTradesDialog.setText(eventModel.getTotalTrades()+" Trades");

            SeekBar seekBarPrice = dialog.findViewById(R.id.seekPrice);

            txtOptionYes.setOnClickListener(v->{
                eventModel.setOptionValue("Yes");
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                setOpinionValue(eventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+format.format(price[0]));
                }

            });

            txtOptionNo.setOnClickListener(v->{
                eventModel.setOptionValue("No");
                txtOptionYes.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setBackgroundResource(R.drawable.opinio_no);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.red));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.black_pure));

                setOpinionValue(eventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

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
                    price[0]=progress;

                    eventModel.setSelectedPrice(price[0]+"");

                    txtPrice.setText(rs+price[0]);

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");

                    setOpinionValue(eventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

                    if (totalWalletBalance<(qty[0]*price[0])){
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
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

            if (eventModel.getOptionValue().equalsIgnoreCase("yes")){
                txtOptionYes.setBackgroundResource(R.drawable.opinio_yes);
                txtOptionNo.setBackgroundResource(R.drawable.transparent_view);
                txtOptionNo.setTextColor(mContext.getResources().getColor(R.color.black_pure));
                txtOptionYes.setTextColor(mContext.getResources().getColor(R.color.green_pure));

                if (TextUtils.isEmpty(eventModel.getOption1Price())){
                    price[0]=1;
                    eventModel.setSelectedPrice("1");
                }else {
                    price[0]= (int)CustomUtil.convertFloat(eventModel.getOption1Price());
                    eventModel.setSelectedPrice(eventModel.getOption1Price());
                }

                if (yesList.size()>0){
                    txtMaxQtyAtPrice.setText(yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                            rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                }else {
                    txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                }
            }
            else {
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

                if (TextUtils.isEmpty(eventModel.getOption2Price())){
                    price[0]=1;
                    eventModel.setSelectedPrice("1");
                }else {
                    price[0]= (int)CustomUtil.convertFloat(eventModel.getOption2Price());
                    eventModel.setSelectedPrice(eventModel.getOption2Price());
                }
            }

            seekBarPrice.setProgress((int) price[0]);

            setOpinionValue(eventModel.getOptionValue(),price[0]+"",data,txtQtyAtPrice);

            SeekBar seekBarQty = dialog.findViewById(R.id.seekQty);

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] =progress;

                    edtQty.setText(qty[0]+"");
                    eventModel.setCon_join_qty(qty[0]+"");

                    float getValue=qty[0]*10;
                    txtGet.setText(rs+getValue+"");

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");
                    LogUtil.e("0price0",price[0]);
                    if (edtQty.hasFocus()){
                        edtQty.setSelection(String.valueOf(progress).length());
                    }

                    if (qty[0]==10){
                        txtBtn10.performClick();
                    }
                    else if (qty[0]==50){
                        txtBtn50.performClick();
                    }
                    else if (qty[0]==100){
                        txtBtn100.performClick();
                    }
                    else if (qty[0]==500){
                        txtBtn500.performClick();
                    }
                    else if (qty[0]==750){
                        txtBtn750.performClick();
                    }
                    else{
                        txtBtn10.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn50.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn100.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn500.setBackgroundResource(R.drawable.border_match_green);
                        txtBtn750.setBackgroundResource(R.drawable.border_match_green);
                    }

                    if (totalWalletBalance<(qty[0]*price[0])){
                        join_contest_btn.setBackgroundResource(R.drawable.red_bg_radius);
                        join_contest_btn.setText("Insufficient balance | Add Cash");
                    }else {
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
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard((Activity) mContext);
                    if (TextUtils.isEmpty(edtQty.getText().toString().trim()) ||
                            CustomUtil.convertInt(edtQty.getText().toString().trim())<=0){
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
                    if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                        //edtQty.setText("1");
                        //edtQty.setSelection(1);
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0]<=0){
                        qty[0]=1;
                    }
                    else if (qty[0]>1000){
                        qty[0]=1000;
                    }
                  /*  if (edtQty.hasFocus())
                        edtQty.setSelection(charSequence.length());*/

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {}

            });

            txtSelect.setOnClickListener(view1 -> {
                if (eventModel.getOptionValue().equalsIgnoreCase("yes")){
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
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    if (TextUtils.isEmpty(eventModel.getCon_join_qty()) ){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(eventModel.getCon_join_qty())<=0){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinOpinionContest(eventModel);
                }
                else {
                    if (totalWalletBalance<(qty[0]*price[0])){
                        double amt=Math.ceil((qty[0]*price[0]) - totalWalletBalance);
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
                }
            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
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
            LogUtil.e("resp","param: "+jsonObject+" \n url: "+ApiManager.joinTradesContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinTradesContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
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
        txtTitle.setText("Trade Order placed!");
        Button btnShare=dialog.findViewById(R.id.btnShare);
        Button btnMore=dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect=dialog.findViewById(R.id.imgCorrect);

       // Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);

        txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs+model.getSelectedPrice());
        float total=CustomUtil.convertFloat(model.getSelectedPrice())*CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs+total+"");

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btnShare.setOnClickListener(view -> {
            dialog.dismiss();

            shareContest();
        });

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        MusicManager.getInstance().playEventSuccess(mContext);
        dialog.show();
    }

    private void setOpinionValue(String option,String price,JSONObject data,TextView txtQtyAtPrice){

        JSONArray yesCount=data.optJSONArray("yesCount");
        JSONArray noCount=data.optJSONArray("noCount");

        txtQtyAtPrice.setText("0 quantities available at ₹"+price);

        if (option.equalsIgnoreCase("yes")){
            for (int i = 0; i < yesList.size(); i++) {
                AvailableQtyModel model=yesList.get(i);
                if (model.getJnAmount().equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at ₹"+price);
                }
            }
        }
        else {
            for (int i = 0; i < noList.size(); i++) {
                AvailableQtyModel model=noList.get(i);
                if (model.getJnAmount().equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(model.getTotalJoinCnt()+" quantities available at ₹"+price);
                }
                /*JSONObject yesObj=noCount.optJSONObject(i);
                if (yesObj.optString("jn_amount").equalsIgnoreCase(String.valueOf(price))){
                    txtQtyAtPrice.setText(yesObj.optString("total_join_cnt")+" quantities available at ₹"+price);
                }*/
            }
        }
    }

    private void listener() {
        /*if (mSocket!=null) {
            mSocket.off("res");
            mSocket.on("res", args -> {

                if (args!=null){
                    try {
                        Log.e("resp","LiveScore listener: "+args[0]);
                        JSONObject object=new JSONObject(args[0].toString());
                        if (object.optString("en").equalsIgnoreCase("trade_unmatch_count")){
                            Log.e("resp","trade_unmatch_count: "+args[0]);
                            if (eventModel!=null){
                                JSONArray data=object.optJSONArray("data");
                                String rs=mContext.getResources().getString(R.string.rs);
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

                                        if (!TextUtils.isEmpty(eventModel.getOptionValue())){

                                            if (eventModel.getOptionValue().equalsIgnoreCase("yes")){
                                                if (txtMaxQtyAtPrice!=null && dialog!=null && dialog.isShowing()){
                                                    if (yesList.size()>0){
                                                        txtMaxQtyAtPrice.setText( yesList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                                rs+String.format("%.2f",CustomUtil.convertFloat(yesList.get(0).getJnAmount())));
                                                    }else {
                                                        txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                    }
                                                }

                                            }
                                            else {
                                                if (txtMaxQtyAtPrice!=null && dialog!=null && dialog.isShowing()) {
                                                    if (noList.size()>0){
                                                        txtMaxQtyAtPrice.setText(noList.get(0).getTotalJoinCnt()+" quantities available at "+
                                                                rs+String.format("%.2f",CustomUtil.convertFloat(noList.get(0).getJnAmount())));
                                                    }else {
                                                        txtMaxQtyAtPrice.setText("0 quantities available at "+rs+"1");
                                                    }
                                                }
                                            }
                                        }

                                        if (txtQtyAtPrice!=null && dialog!=null && dialog.isShowing())
                                            setOpinionValue(eventModel.getOptionValue(),eventModel.getSelectedPrice()
                                                    ,obj,txtQtyAtPrice);
                                        *//*Collections.sort(yesList, new prizePoolUp());
                                        Collections.sort(noList, new prizePoolUp());*//*

                                        // qtyList.clear();
                                      *//*  if (eventModel.getOptionValue().equalsIgnoreCase("yes")){
                                            qtyList.addAll(yesList);
                                        }else {
                                            qtyList.addAll(noList);
                                        }*//*

                                        runOnUiThread(() -> {
                                            *//*if (qtyList.size()>0){
                                                txtNoQty.setVisibility(View.GONE);
                                                recyclerQty.setVisibility(View.VISIBLE);
                                            }else {
                                                txtNoQty.setVisibility(View.VISIBLE);
                                                recyclerQty.setVisibility(View.GONE);
                                            }
                                            if (adapter!=null && recyclerQty!=null){
                                                adapter.updateList(qtyList,eventModel.getOptionValue(),recyclerQty);
                                            }*//*

                                            eventModel.setTotalTrades(obj.optJSONObject("contest_data").optString("total_trades"));

                                            if (txtTrades!=null)
                                                txtTrades.setText(CustomUtil.coolFormat(mContext,obj.optJSONObject("contest_data").optString("total_trades"))+" Trades");

                                            if (txtTradesDialog!=null && dialog!=null && dialog.isShowing()) {
                                                txtTradesDialog.setText(CustomUtil.coolFormat(mContext,obj.optJSONObject("contest_data").optString("total_trades"))+" Trades");
                                            }
                                        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (mSocket!=null){
            mSocket.off("res");
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            listener();
        }*/
    }

}