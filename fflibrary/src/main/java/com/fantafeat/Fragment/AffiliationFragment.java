package com.fantafeat.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

public class AffiliationFragment extends BaseFragment {

    private TextView aff_start_date, aff_end_date, aff_match, aff_join, aff_deposit, aff_winning, aff_amount, aff_get_data;
    private String fromDate;
    private DatePickerDialog fromDatePicker;
    private DatePickerDialog.OnDateSetListener fromDatePickerListener;
    private Calendar fromDateCalendar;
    private String toDate;
    private DatePickerDialog toDatePicker;
    private DatePickerDialog.OnDateSetListener toDatePickerListener;
    private Calendar toDateCalendar;
    private LinearLayout aff_detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_affiliation, container, false);
        initFragment(view);
        initToolBar(view, "Affiliation Program", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        aff_start_date = view.findViewById(R.id.aff_start_date);
        aff_end_date = view.findViewById(R.id.aff_end_date);
        aff_match = view.findViewById(R.id.aff_match);
        aff_join = view.findViewById(R.id.aff_join);
        aff_deposit = view.findViewById(R.id.aff_deposit);
        aff_winning = view.findViewById(R.id.aff_winning);
        aff_amount = view.findViewById(R.id.aff_amount);
        aff_get_data = view.findViewById(R.id.aff_get_data);
        aff_detail = view.findViewById(R.id.aff_detail);


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

        toDatePicker = new DatePickerDialog(getContext(), toDatePickerListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePicker = new DatePickerDialog(getContext(), fromDatePickerListener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void initClick() {
        aff_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aff_start_date.getText().toString().equals("") || aff_end_date.getText().toString().equals("")) {
                    CustomUtil.showTopSneakError(mContext, "Please select proper dates");
                } else {
                    if (MyApp.getClickStatus()) {
                        new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.home_fragment_container,
                                new AffiliationDetailFragment(aff_start_date.getText().toString(), aff_end_date.getText().toString()),
                                ((HomeActivity) mContext).fragmentTag(19),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);
                    }
                }
            }
        });

        aff_get_data.setOnClickListener(new View.OnClickListener() {
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
            jsonObject.put("ref_code", preferences.getUserModel().getRefNo());
            jsonObject.put("from_date", aff_start_date.getText().toString());
            jsonObject.put("to_date", aff_end_date.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AFFILIATION, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    try {
                        JSONObject data = new JSONObject(responseBody.optString("data"));
                        aff_match.setText(data.optString("match_count"));
                        aff_join.setText(data.optString("teams_count"));
                        float deposit = CustomUtil.convertFloat(data.optString("deposit_bal"));
                        float win = CustomUtil.convertFloat(data.optString("win_bal"));
                        float total_amount = CustomUtil.convertFloat(data.optString("final_comm_pramotor"));

                        aff_deposit.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(deposit));
                        aff_winning.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(win));

                        //float total_amount = CustomUtil.convertFloat(data.optString("deposit_bal")) + CustomUtil.convertFloat(data.optString("win_bal"));
                        aff_amount.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(total_amount));

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
}