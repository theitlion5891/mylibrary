package com.fantafeat.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AffiliationActivity extends BaseActivity {
    private TextView aff_start_date, aff_end_date,  aff_deposit, aff_winning,
            aff_amount, txtSubmit,txtGetDetail,btnToday,btn1w,btn1m,btn3m,aff_entry,aff_joinedMatches,aff_join_team,aff_join_contest;//aff_match, aff_join
    private String fromDate;
    private DatePickerDialog fromDatePicker;
    private DatePickerDialog.OnDateSetListener fromDatePickerListener;
    private Calendar fromDateCalendar;
    private ImageView toolbar_back,imgAffLeaderboard;
    private String toDate;
    private DatePickerDialog toDatePicker;
    private DatePickerDialog.OnDateSetListener toDatePickerListener;
    private Calendar toDateCalendar;
    //private LinearLayout aff_detail;
    private LinearLayout layFooter;
    private String totalBal="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_affiliation);
        aff_start_date = findViewById(R.id.aff_start_date);
        aff_end_date = findViewById(R.id.aff_end_date);
        //aff_match = findViewById(R.id.aff_match);
       // aff_join = findViewById(R.id.aff_join);
        aff_deposit = findViewById(R.id.aff_deposit);
        aff_winning = findViewById(R.id.aff_winning);
        aff_amount = findViewById(R.id.aff_amount);
        txtSubmit = findViewById(R.id.txtSubmit);
        txtGetDetail = findViewById(R.id.txtGetDetail);
        toolbar_back = findViewById(R.id.toolbar_back);
        layFooter = findViewById(R.id.layFooter);
        btnToday = findViewById(R.id.btnToday);
        btn1w = findViewById(R.id.btn1w);
        btn1m = findViewById(R.id.btn1m);
        btn3m = findViewById(R.id.btn3m);
        imgAffLeaderboard = findViewById(R.id.imgAffLeaderboard);
        aff_entry = findViewById(R.id.aff_entry);
        aff_joinedMatches = findViewById(R.id.aff_joinedMatches);
        aff_join_team = findViewById(R.id.aff_join_team);
        aff_join_contest = findViewById(R.id.aff_join_contest);


        fromDateCalendar = Calendar.getInstance();
        fromDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                fromDateCalendar.set(Calendar.YEAR, year);
                fromDateCalendar.set(Calendar.MONTH, monthOfYear);
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fromDate = fromDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (fromDateCalendar.get(Calendar.MONTH) + 1) + "-" + fromDateCalendar.get(Calendar.YEAR);
                aff_start_date.setText(fromDate);
                toDatePicker.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
            }

        };

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        toDateCalendar = Calendar.getInstance();
        toDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                toDateCalendar.set(Calendar.YEAR, year);
                toDateCalendar.set(Calendar.MONTH, monthOfYear);
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                toDate = toDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (toDateCalendar.get(Calendar.MONTH) + 1) + "-" + toDateCalendar.get(Calendar.YEAR);
                aff_end_date.setText(toDate);
            }

        };

        toDatePicker = new DatePickerDialog(mContext, toDatePickerListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePicker = new DatePickerDialog(mContext, fromDatePickerListener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH));


        initClick();
    }

    @Override
    public void initClick() {

        btnToday.setOnClickListener(view -> {
            calDate(0);
        });

        btn1w.setOnClickListener(view -> {
            calDate(7);
        });

        btn1m.setOnClickListener(view -> {
            calDate(30);
        });

        btn3m.setOnClickListener(view -> {
            calDate(90);
        });

        imgAffLeaderboard.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                Intent intent = new Intent(mContext, PromotorLeaderboardActivity.class);
                startActivity(intent);
            }
        });

        txtGetDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aff_start_date.getText().toString().equals("") || aff_end_date.getText().toString().equals("")) {
                    CustomUtil.showTopSneakError(mContext, "Please select proper dates");
                } else {
                    if (MyApp.getClickStatus()) {
                        Intent intent = new Intent(mContext, AffilationMatchList.class);//AffiliationDetailsActivity
                        intent.putExtra("start_date",aff_start_date.getText().toString());
                        intent.putExtra("end_date",aff_end_date.getText().toString());
                        intent.putExtra("totalBal",totalBal);
                        startActivity(intent);
                    }
                }
            }
        });

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (validate()) {
                        callApi();
                    }
                }
            }
        });

        aff_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                fromDatePicker.show();
            }
        });

        aff_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aff_end_date.getText().toString().toLowerCase().equals("to date")) {
                    CustomUtil.showTopSneakError(mContext, "Please select from date");
                } else {
                    toDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    toDatePicker.show();
                }
            }
        });
    }

    private void callApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("from_date", aff_start_date.getText().toString());
            jsonObject.put("to_date", aff_end_date.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onSuccessResult: " + jsonObject.toString());

        HttpRestClient.postJSON(mContext, true, ApiManager.MATCH_AFFILIATION, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    layFooter.setVisibility(View.VISIBLE);
                    try {
                        JSONObject data = new JSONObject(responseBody.optString("data"));
                        //aff_match.setText(data.optString("match_count"));
                        //aff_join.setText(data.optString("teams_count"));
                        float deposit = CustomUtil.convertFloat(data.optString("deposit_bal"));
                        float win = CustomUtil.convertFloat(data.optString("win_bal"));
                        float total_amount = CustomUtil.convertFloat(data.optString("final_comm_pramotor"));
                        float entry = CustomUtil.convertFloat(data.optString("total_entry_fee"));


                        aff_deposit.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(deposit));
                        aff_winning.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(win));
                        aff_entry.setText(mContext.getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(entry));
                        aff_joinedMatches.setText(CustomUtil.convertInt(data.optString("match_count"))+"");
                        aff_join_team.setText(CustomUtil.convertInt(data.optString("teams_count"))+"");

                        //float total_amount = CustomUtil.convertFloat(data.optString("deposit_bal")) + CustomUtil.convertFloat(data.optString("win_bal"));
                        aff_join_contest.setText(CustomUtil.convertInt(data.optString("joined_contest"))+"");
                        totalBal=mContext.getResources().getString(R.string.rs) +
                                CustomUtil.getFormater("00.00").format(total_amount);
                        aff_amount.setText(totalBal);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean validate() {
        if (aff_start_date.getText().toString().equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please select from date");
            return false;
        } else if (aff_end_date.getText().toString().equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please select to date");
            return false;
        }
        return true;
    }

    private void calDate(int days){

        try {
            Date cd = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = MyApp.changedFormat("dd-MM-yyyy");

            String cData = sdf.format(cd);

            aff_end_date.setText(cData);

            Calendar c = Calendar.getInstance(); // Get Calendar Instance

            c.setTime(sdf.parse(cData));

            if (days==30){
                c.add(Calendar.MONTH, -1);
            }else  if (days==90){
                c.add(Calendar.MONTH, -3);
            }else {
                c.add(Calendar.DAY_OF_WEEK, -days);
            }

            aff_start_date.setText(sdf.format(c.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}