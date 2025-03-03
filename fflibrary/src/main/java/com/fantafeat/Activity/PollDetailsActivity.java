package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantafeat.Adapter.PollDetailAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class PollDetailsActivity extends BaseActivity {

    private EventModel eventModel;
    private boolean isLiveEvent;
    private RecyclerView recyclerPoll;
    private LinearLayout layDesc,tabMyPoll,tabOverview,layOverview,layMyPoll;
    private ImageView back_img,imgShare;
    private TextView txtEndDate,txtMatchedVal,txtConTitle,txtDesc,txtOp1,txtOp2,txtOp3,txtOp4,txtOp5,txtMyPoll,txtOverview,txtDescription,txtSouceAgency,
            txtStartDate,txtEndDate1;
    private CircleImageView imgConImage;
    private View viewMyPoll,viewOverview;
    private String selectedTag="1"; //1=Mypoll, 2=overview

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_details);

        eventModel=(EventModel) getIntent().getSerializableExtra("eventModel");
        isLiveEvent=getIntent().getBooleanExtra("isLiveEvent",false);

        recyclerPoll=findViewById(R.id.recyclerPoll);
        txtEndDate=findViewById(R.id.txtEndDate);
        txtMatchedVal=findViewById(R.id.txtMatchedVal);
        imgConImage=findViewById(R.id.imgConImage);
        txtConTitle=findViewById(R.id.txtConTitle);
        txtDesc=findViewById(R.id.txtDesc);
        txtOp1=findViewById(R.id.txtOp1);
        txtOp2=findViewById(R.id.txtOp2);
        txtOp3=findViewById(R.id.txtOp3);
        txtOp4=findViewById(R.id.txtOp4);
        txtOp5=findViewById(R.id.txtOp5);
        layDesc=findViewById(R.id.layDesc);
        back_img=findViewById(R.id.back_img);
        tabMyPoll=findViewById(R.id.tabMyPoll);
        txtMyPoll=findViewById(R.id.txtMyPoll);
        viewMyPoll=findViewById(R.id.viewMyPoll);
        tabOverview=findViewById(R.id.tabOverview);
        txtOverview=findViewById(R.id.txtOverview);
        viewOverview=findViewById(R.id.viewOverview);
        layOverview=findViewById(R.id.layOverview);
        layMyPoll=findViewById(R.id.layMyPoll);
        txtDescription=findViewById(R.id.txtDescription);
        txtSouceAgency=findViewById(R.id.txtSouceAgency);
        txtStartDate=findViewById(R.id.txtStartDate);
        txtEndDate1=findViewById(R.id.txtEndDate1);
        imgShare=findViewById(R.id.imgShare);

        recyclerPoll.setLayoutManager(new LinearLayoutManager(mContext));

        initData();

        getDetailApi();

        back_img.setOnClickListener(view -> {
            finish();
        });

        imgShare.setOnClickListener(view -> {
            shareContest(eventModel);
        });

    }

    private void showConfirmPollContestDialog(EventModel model) {

        if (!isLiveEvent)
            return;

        //use_deposit=use_transfer=use_winning=0;

        final float[] entryFee = {CustomUtil.convertFloat(model.getEntryFee())};
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
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
            DecimalFormat format = CustomUtil.getFormater("#.##");
            String rs=mContext.getResources().getString(R.string.rs);
            UserModel userModel=preferences.getUserModel();

            float totalWalletBalance=CustomUtil.convertFloat(userModel.getDepositBal())+
                    CustomUtil.convertFloat(userModel.getWinBal())+CustomUtil.convertFloat(userModel.getTransferBal());

            //View view = LayoutInflater.from(mContext).inflate(R.layout.opinion_confirm_dialog, null);

            Dialog dialog=new Dialog(mContext);
            dialog.setContentView(R.layout.poll_join_dialog);

            final int[] qty = {0};
            final int[] price = {0};

            TextView txtEndDate=dialog.findViewById(R.id.txtEndDate);

            TextView txtOp1=dialog.findViewById(R.id.txtOp1);
            TextView txtOp2=dialog.findViewById(R.id.txtOp2);
            TextView txtOp3=dialog.findViewById(R.id.txtOp3);
            TextView txtOp4=dialog.findViewById(R.id.txtOp4);
            TextView txtOp5=dialog.findViewById(R.id.txtOp5);

            RelativeLayout layOp1=dialog.findViewById(R.id.layOp1);
            RelativeLayout layOp2=dialog.findViewById(R.id.layOp2);
            RelativeLayout layOp3=dialog.findViewById(R.id.layOp3);
            RelativeLayout layOp4=dialog.findViewById(R.id.layOp4);
            RelativeLayout layOp5=dialog.findViewById(R.id.layOp5);

            ProgressBar progressOp1=dialog.findViewById(R.id.progressOp1);
            ProgressBar progressOp2=dialog.findViewById(R.id.progressOp2);
            ProgressBar progressOp3=dialog.findViewById(R.id.progressOp3);
            ProgressBar progressOp4=dialog.findViewById(R.id.progressOp4);
            ProgressBar progressOp5=dialog.findViewById(R.id.progressOp5);
            //TextView txtPrice=dialog.findViewById(R.id.txtPrice);
            TextView txtConDesc=dialog.findViewById(R.id.txtConDesc);
            SeekBar seekBarQty=dialog.findViewById(R.id.seekQty);

            TextView txtBtn10=dialog.findViewById(R.id.txtBtn10);
            TextView txtBtn50=dialog.findViewById(R.id.txtBtn50);
            TextView txtBtn100=dialog.findViewById(R.id.txtBtn100);
            TextView txtBtn500=dialog.findViewById(R.id.txtBtn500);
            TextView txtBtn750=dialog.findViewById(R.id.txtBtn750);

            TextView txtTrades=dialog.findViewById(R.id.txtTrades);
            TextView txtConTitle=dialog.findViewById(R.id.txtConTitle);
            TextView txtInvest=dialog.findViewById(R.id.txtInvest);
            TextView txtGet=dialog.findViewById(R.id.txtGet);
            EditText edtQty=dialog.findViewById(R.id.edtQty);
            TextView join_contest_btn=dialog.findViewById(R.id.join_contest_btn);
            ImageView imgConImage=dialog.findViewById(R.id.imgConImage);

            txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
            txtConTitle.setText(model.getConDesc());
            txtConDesc.setText(model.getConSubDesc());

            if (TextUtils.isEmpty(model.getTotalTrades()) || model.getTotalTrades().equalsIgnoreCase("null"))
                txtTrades.setText("0 Vote");
            else
                txtTrades.setText(model.getTotalTrades()+" Votes");

            if (!TextUtils.isEmpty(model.getConImage())){
                CustomUtil.loadImageWithGlide(mContext,imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
            }

            if (TextUtils.isEmpty(model.getOption1())){
                layOp1.setVisibility(View.GONE);
            }else {
                layOp1.setVisibility(View.VISIBLE);
                txtOp1.setText(model.getOption1());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption2())){
                layOp2.setVisibility(View.GONE);
            }else {
                layOp2.setVisibility(View.VISIBLE);
                txtOp2.setText(model.getOption2());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption3())){
                layOp3.setVisibility(View.GONE);
            }else {
                layOp3.setVisibility(View.VISIBLE);
                txtOp3.setText(model.getOption3());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption4())){
                layOp4.setVisibility(View.GONE);
            }else {
                layOp4.setVisibility(View.VISIBLE);
                txtOp4.setText(model.getOption4());// + " | "+rs+model.getEntryFee()
            }

            if (TextUtils.isEmpty(model.getOption5())){
                layOp5.setVisibility(View.GONE);
            }else {
                layOp5.setVisibility(View.VISIBLE);
                txtOp5.setText(model.getOption5());// + " | "+rs+model.getEntryFee()
            }

            layOp1.setOnClickListener(view -> {
                model.setOptionValue(model.getOption1());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp2.setOnClickListener(view -> {
                model.setOptionValue(model.getOption2());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp3.setOnClickListener(view -> {
                model.setOptionValue(model.getOption3());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp4.setOnClickListener(view -> {
                model.setOptionValue(model.getOption4());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            layOp5.setOnClickListener(view -> {
                model.setOptionValue(model.getOption5());
                progressOp1.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp2.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp3.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp4.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_light));
                progressOp5.setProgressDrawable(getResources().getDrawable(R.drawable.poll_option_progress_bg_dark));

                CalculatePollPercentage(model,qty[0],dialog);
            });

            if (model.getOptionValue().equalsIgnoreCase(model.getOption1())){
                layOp1.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption2())){
                layOp2.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption3())){
                layOp3.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption4())){
                layOp4.performClick();
            }
            else if (model.getOptionValue().equalsIgnoreCase(model.getOption5())){
                layOp5.performClick();
            }

            qty[0]=10;
            price[0]= (int) entryFee[0];

            CalculatePollPercentage(model,qty[0],dialog);

            //txtPrice.setText(rs+price[0]);
            txtInvest.setText(rs+price[0]);

            model.setCon_join_qty(qty[0]+"");
            model.setSelectedPrice(price[0]+"");

            seekBarQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    qty[0] = progress;

                    edtQty.setText(qty[0]+"");
                    model.setCon_join_qty(qty[0]+"");

                   /* float getValue=qty[0]*10;
                    txtGet.setText(getValue+"");*/

                    float investValue=price[0]*qty[0];
                    txtInvest.setText(rs+investValue+"");

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

                    CalculatePollPercentage(model,qty[0],dialog);

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
                    hideKeyboard((PollDetailsActivity) mContext);
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
                    if (TextUtils.isEmpty(charSequence)){
                        qty[0] = 1;
                        //edtQty.setText("1");
                        // edtQty.setSelection(1);
                    }else {
                        qty[0] = CustomUtil.convertInt(charSequence.toString());
                        edtQty.setSelection(charSequence.length());
                    }

                    if (qty[0]<=0){
                        qty[0]=1;
                    }else if (qty[0]>1000){
                        qty[0]=1000;
                    }

                    seekBarQty.setProgress(qty[0]);
                }

                @Override
                public void afterTextChanged(Editable editable) {

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

            seekBarQty.setProgress(qty[0]);
            edtQty.setText(qty[0]+"");

            join_contest_btn.setOnClickListener(view -> {
                dialog.dismiss();
                if (join_contest_btn.getText().toString().trim().equalsIgnoreCase("Confirm")){
                    if (TextUtils.isEmpty(model.getCon_join_qty()) ){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    if (CustomUtil.convertInt(model.getCon_join_qty())<=0){
                        CustomUtil.showTopSneakWarning(mContext,"Select contest quantity");
                        return;
                    }
                    dialog.dismiss();
                    joinPollContest(model);
                }
                else {

                    double amt=Math.ceil((price[0]*qty[0])-totalWalletBalance);
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

                    /*float leftAmt=(totalWalletBalance-price[0])+1;
                    startActivity(new Intent(mContext,AddDepositActivity.class)
                            .putExtra("depositAmt",((int)leftAmt)+""));*/
                }

            });

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
        }
    }

    private void CalculatePollPercentage(EventModel model, int qty,Dialog dialog){
        if (qty==0){
            return;
        }
        DecimalFormat format = CustomUtil.getFormater("#.##");
        String rs=mContext.getResources().getString(R.string.rs);

        float entryFee = CustomUtil.convertFloat(model.getEntryFee());

        TextView txtOp1=dialog.findViewById(R.id.txtOp1Per);
        TextView txtOp2=dialog.findViewById(R.id.txtOp2Per);
        TextView txtOp3=dialog.findViewById(R.id.txtOp3Per);
        TextView txtOp4=dialog.findViewById(R.id.txtOp4Per);
        TextView txtOp5=dialog.findViewById(R.id.txtOp5Per);

        ProgressBar progressOp1=dialog.findViewById(R.id.progressOp1);
        ProgressBar progressOp2=dialog.findViewById(R.id.progressOp2);
        ProgressBar progressOp3=dialog.findViewById(R.id.progressOp3);
        ProgressBar progressOp4=dialog.findViewById(R.id.progressOp4);
        ProgressBar progressOp5=dialog.findViewById(R.id.progressOp5);

        TextView txtGet=dialog.findViewById(R.id.txtGet);

        float op1cnt=CustomUtil.convertFloat(model.getOption_1_cnt());
        float op2cnt=CustomUtil.convertFloat(model.getOption_2_cnt());
        float op3cnt=CustomUtil.convertFloat(model.getOption_3_cnt());
        float op4cnt=CustomUtil.convertFloat(model.getOption_4_cnt());
        float op5cnt=CustomUtil.convertFloat(model.getOption_5_cnt());
        float totalTrade=CustomUtil.convertFloat(model.getTotalTrades())+qty;

        float op1per=(op1cnt*100)/totalTrade;
        float op2per=(op2cnt*100)/totalTrade;
        float op3per=(op3cnt*100)/totalTrade;
        float op4per=(op4cnt*100)/totalTrade;
        float op5per=(op5cnt*100)/totalTrade;

        float entryBeforeCommission=0;
        float winnercnt=0;

        if (model.getOptionValue().equalsIgnoreCase(model.getOption1())){
            op1per=((op1cnt+qty)*100)/totalTrade;

            winnercnt=op1cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption2())){
            op2per=((op2cnt+qty)*100)/totalTrade;

            winnercnt=op2cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption3())){
            op3per=((op3cnt+qty)*100)/totalTrade;

            winnercnt=op3cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption4())){
            op4per=((op4cnt+qty)*100)/totalTrade;

            winnercnt=op4cnt+qty;
        }
        else if (model.getOptionValue().equalsIgnoreCase(model.getOption5())){
            op5per=((op5cnt+qty)*100)/totalTrade;

            winnercnt=op5cnt+qty;
        }
        float totalEEntryFee=entryFee*totalTrade;
        // LogUtil.e("resp","totalEEntryFee: "+totalEEntryFee);

        entryBeforeCommission=totalEEntryFee-(winnercnt*entryFee);

        // LogUtil.e("resp","entryBeforeCommission: "+entryBeforeCommission);

        float entryAfterCommission=entryBeforeCommission-(entryBeforeCommission*CustomUtil.convertFloat(model.getCommission())/100);

        //LogUtil.e("resp","entryAfterCommission: "+entryAfterCommission);

        float winningAmt=entryAfterCommission+(winnercnt*entryFee);
        float winningAmtPerQty=winningAmt/qty;

        float winningPerQtySelected=winningAmt/winnercnt;
        float myAproxWinAmt=winningPerQtySelected*qty;

        // LogUtil.e("resp","winningAmt: "+winningAmt);
        //LogUtil.e("resp","winningAmtPerQty: "+winningAmtPerQty);

        progressOp1.setProgress((int)op1per);
        progressOp2.setProgress((int)op2per);
        progressOp3.setProgress((int)op3per);
        progressOp4.setProgress((int)op4per);
        progressOp5.setProgress((int)op5per);

        txtGet.setText(rs+format.format(myAproxWinAmt));

        txtOp1.setText(format.format(op1per)+"%");
        txtOp2.setText(format.format(op2per)+"%");
        txtOp3.setText(format.format(op3per)+"%");
        txtOp4.setText(format.format(op4per)+"%");
        txtOp5.setText(format.format(op5per)+"%");
    }

    private void getDetailApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("comp_id", ConstantUtil.COMPANY_ID);
            jsonObject.put("contest_id", eventModel.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "getDetailApi : " + jsonObject.toString());
        HttpRestClient.postJSONNormal(mContext, true, ApiManager.myJoinPollContestListDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "getDetailApi : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray data=responseBody.optJSONArray("data");
                    PollDetailAdapter adapter=new PollDetailAdapter(mContext,data);
                    recyclerPoll.setAdapter(adapter);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                //preferences.setUserModel(new UserModel());
            }
        });
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
            LogUtil.e("resp","param: "+jsonObject+" \n url: "+ApiManager.joinPollContest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSONNormal(mContext, true, ApiManager.joinPollContest, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                LogUtil.e(TAG, "joinPollContest : " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    // CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    getUserDetails(model);
                }else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}
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
        Button btnShare=dialog.findViewById(R.id.btnShare);
        Button btnMore=dialog.findViewById(R.id.btnMore);
        ImageView imgCorrect=dialog.findViewById(R.id.imgCorrect);
        TextView txtTitle=dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Poll Order placed!");

//        Glide.with(mContext).asGif().load(R.raw.ic_correct).into(imgCorrect);

        txtOrderQty.setText(model.getCon_join_qty());
        txtOrderPrice.setText(rs+model.getSelectedPrice());
        float total = CustomUtil.convertFloat(model.getSelectedPrice()) * CustomUtil.convertFloat(model.getCon_join_qty());
        txtOrderTotal.setText(rs+total+"");

        btnMore.setOnClickListener(view -> {
            dialog.dismiss();
            getDetailApi();
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
        String url= ConstantUtil.LINK_POLL_URL+eventModel.getId();;

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

    private void initData() {

        String rs=getResources().getString(R.string.rs);

        if (TextUtils.isEmpty(eventModel.getOption1())){
            txtOp1.setVisibility(View.GONE);
        }
        else {
            txtOp1.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(eventModel.getOption2())){
            txtOp2.setVisibility(View.GONE);
        }
        else {
            txtOp2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(eventModel.getOption3())){
            txtOp3.setVisibility(View.GONE);
        }
        else {
            txtOp3.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(eventModel.getOption4())){
            txtOp4.setVisibility(View.GONE);
        }
        else {
            txtOp4.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(eventModel.getOption5())){
            txtOp5.setVisibility(View.GONE);
        }
        else {
            txtOp5.setVisibility(View.VISIBLE);
        }

        txtOp1.setText(eventModel.getOption1()+" | "+eventModel.getOption_1_cnt()+" Votes");
        txtOp2.setText(eventModel.getOption2()+" | "+eventModel.getOption_2_cnt()+" Votes");
        txtOp3.setText(eventModel.getOption3()+" | "+eventModel.getOption_3_cnt()+" Votes");
        txtOp4.setText(eventModel.getOption4()+" | "+eventModel.getOption_4_cnt()+" Votes");
        txtOp5.setText(eventModel.getOption5()+" | "+eventModel.getOption_5_cnt()+" Votes");

        txtDescription.setText(eventModel.getCon_overview());
        txtSouceAgency.setText(eventModel.getSource_agency());
        txtEndDate1.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm aa"));
        txtStartDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConStartTime(),"MMM dd hh:mm aa"));

        if (isLiveEvent){
            txtEndDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm aa"));

            if (TextUtils.isEmpty(eventModel.getConSubDesc())){
                layDesc.setVisibility(View.GONE);
            }else {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(eventModel.getConSubDesc());
            }
        }
        else {
            txtEndDate.setText(CustomUtil.dateConvertWithFormat(eventModel.getConEndTime(),"MMM dd hh:mm aa"));

            layDesc.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(eventModel.getMy_total_trades_count())){
            txtMatchedVal.setText(eventModel.getMy_total_trades_count()+"/"+eventModel.getTotalTrades());
        }else {
            txtMatchedVal.setText(eventModel.getTotalTrades());
        }

        txtConTitle.setText(eventModel.getConDesc());

        if (!TextUtils.isEmpty(eventModel.getConImage())){
            CustomUtil.loadImageWithGlide(this,imgConImage, MyApp.imageBase + MyApp.document,eventModel.getConImage(),R.drawable.event_placeholder);
        }
        else
            imgConImage.setImageResource(R.drawable.event_placeholder);

        if (eventModel.getOption1().equalsIgnoreCase(eventModel.getConWinning())){
            txtOp1.setBackgroundResource(R.drawable.opinio_yes);
            txtOp2.setBackgroundResource(R.drawable.gray_round_border);
            txtOp3.setBackgroundResource(R.drawable.gray_round_border);
            txtOp4.setBackgroundResource(R.drawable.gray_round_border);
            txtOp5.setBackgroundResource(R.drawable.gray_round_border);
        }
        else if (eventModel.getOption2().equalsIgnoreCase(eventModel.getConWinning())){
            txtOp2.setBackgroundResource(R.drawable.opinio_yes);
            txtOp1.setBackgroundResource(R.drawable.gray_round_border);
            txtOp3.setBackgroundResource(R.drawable.gray_round_border);
            txtOp4.setBackgroundResource(R.drawable.gray_round_border);
            txtOp5.setBackgroundResource(R.drawable.gray_round_border);
        }
        else if (eventModel.getOption3().equalsIgnoreCase(eventModel.getConWinning())){
            txtOp3.setBackgroundResource(R.drawable.opinio_yes);
            txtOp1.setBackgroundResource(R.drawable.gray_round_border);
            txtOp2.setBackgroundResource(R.drawable.gray_round_border);
            txtOp4.setBackgroundResource(R.drawable.gray_round_border);
            txtOp5.setBackgroundResource(R.drawable.gray_round_border);
        }
        else if (eventModel.getOption4().equalsIgnoreCase(eventModel.getConWinning())){
            txtOp4.setBackgroundResource(R.drawable.opinio_yes);
            txtOp1.setBackgroundResource(R.drawable.gray_round_border);
            txtOp2.setBackgroundResource(R.drawable.gray_round_border);
            txtOp3.setBackgroundResource(R.drawable.gray_round_border);
            txtOp5.setBackgroundResource(R.drawable.gray_round_border);
        }
        else if (eventModel.getOption5().equalsIgnoreCase(eventModel.getConWinning())){
            txtOp5.setBackgroundResource(R.drawable.opinio_yes);
            txtOp1.setBackgroundResource(R.drawable.gray_round_border);
            txtOp2.setBackgroundResource(R.drawable.gray_round_border);
            txtOp3.setBackgroundResource(R.drawable.gray_round_border);
            txtOp4.setBackgroundResource(R.drawable.gray_round_border);
        }

        txtOp1.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption1());
            showConfirmPollContestDialog(eventModel);
        });

        txtOp2.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption2());
            showConfirmPollContestDialog(eventModel);
        });

        txtOp3.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption3());
            showConfirmPollContestDialog(eventModel);
        });

        txtOp4.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption4());
            showConfirmPollContestDialog(eventModel);
        });

        txtOp5.setOnClickListener(view -> {
            eventModel.setOptionValue(eventModel.getOption5());
            showConfirmPollContestDialog(eventModel);
        });

        Typeface normalTypeFace = ResourcesCompat.getFont(this, R.font.roboto_regular);
        Typeface boldTypeFace = ResourcesCompat.getFont(this, R.font.roboto_bold);

        tabMyPoll.setOnClickListener(view -> {
            selectedTag="1";
            txtMyPoll.setTextColor(getResources().getColor(R.color.dark_blue));
            txtMyPoll.setTypeface(boldTypeFace);
            viewMyPoll.setVisibility(View.VISIBLE);

            txtOverview.setTextColor(getResources().getColor(R.color.gray_686868));
            txtOverview.setTypeface(normalTypeFace);
            viewOverview.setVisibility(View.GONE);

            layMyPoll.setVisibility(View.VISIBLE);
            layOverview.setVisibility(View.GONE);
        });

        tabOverview.setOnClickListener(view -> {
            selectedTag="2";
            txtOverview.setTextColor(getResources().getColor(R.color.dark_blue));
            txtOverview.setTypeface(boldTypeFace);
            viewOverview.setVisibility(View.VISIBLE);

            txtMyPoll.setTextColor(getResources().getColor(R.color.gray_686868));
            txtMyPoll.setTypeface(normalTypeFace);
            viewMyPoll.setVisibility(View.GONE);

            layMyPoll.setVisibility(View.GONE);
            layOverview.setVisibility(View.VISIBLE);
        });

    }

}