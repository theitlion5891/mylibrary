package com.fantafeat.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class TransferFragment extends BaseFragment {

    TextView total_transferable_balance, deposit_balance, winning_balance;
    EditText user_amount, friend_phone_number;
    Button transfer_btn;
    float deposit_bal = 0, winning_bal = 0, transferable_bal = 0;
    float deposit_send = 0, winnig_send = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        initFragment(view);
        initToolBar(view, "Transfer", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        total_transferable_balance = view.findViewById(R.id.total_transferable_balance);
        deposit_balance = view.findViewById(R.id.deposit_balance);
        winning_balance = view.findViewById(R.id.winning_balance);
        user_amount = view.findViewById(R.id.user_amount);
        friend_phone_number = view.findViewById(R.id.friend_phone_number);
        transfer_btn = view.findViewById(R.id.transfer_btn);

        deposit_bal = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        transferable_bal = deposit_bal + winning_bal;

        total_transferable_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(transferable_bal));
        deposit_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(deposit_bal));
        winning_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(winning_bal));
    }

    @Override
    public void initClick() {
        transfer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApp.getClickStatus()) {
                    if (validate()) {
                        calculate_deduction();

                        builder.setMessage("Are you sure you want to transfer money?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callAPI();
                                dialog.dismiss();
                                /*try {
                                    jsonObject.remove("contest_id");
                                    jsonObject.put("contest_id", new_con_id);
                                    joinContest(new_con_id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                        //callAPI();
                    }
                }
            }
        });

        friend_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (friend_phone_number.getText().toString().length() == 10) {
                    hideKeyboard((Activity) mContext);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callAPI() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("amount", getEditText(user_amount));
            jsonObject.put("oppo_phone_no", getEditText(friend_phone_number));
            jsonObject.put("deposit_bal", String.valueOf(deposit_send));
            jsonObject.put("win_bal", String.valueOf(winnig_send));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.TRANSFER_BAL, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                    UserModel userModel = preferences.getUserModel();
                    userModel.setDepositBal(String.valueOf(CustomUtil.convertFloat(preferences.getUserModel().getDepositBal()) - deposit_send));
                    userModel.setWinBal(String.valueOf(CustomUtil.convertFloat(preferences.getUserModel().getWinBal()) - winnig_send));
                    preferences.setUserModel(userModel);

                    deposit_bal = deposit_bal - deposit_send;
                    winning_bal = winning_bal - winnig_send;
                    deposit_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(deposit_bal));
                    winning_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(winning_bal));
                    transferable_bal = deposit_bal + winning_bal;
                    user_amount.setText("");
                    friend_phone_number.setText("");
                    transfer_btn.setText("Transfer More");
                    total_transferable_balance.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(transferable_bal));

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private boolean validate() {
        if (user_amount.getText().toString().trim().equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please enter amount");
            return false;
        } else if (user_amount.getText().toString().trim().length() >= 5) {
            CustomUtil.showTopSneakError(mContext, "You can transfer upto ₹99,999 amount.");
            return false;
        } else if (CustomUtil.convertFloat(user_amount.getText().toString().trim()) <= 0) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid amount");
            return false;
        } else if (friend_phone_number.getText().toString().trim().length() == 0) {
            CustomUtil.showTopSneakError(mContext, "Please enter phone number");
            return false;
        } else if (friend_phone_number.getText().toString().trim().length() != 10) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid phone number");
            return false;
        } else if (CustomUtil.convertFloat(user_amount.getText().toString().trim()) > (CustomUtil.convertFloat(preferences.getUserModel().getDepositBal())
                + CustomUtil.convertFloat(preferences.getUserModel().getWinBal()))) {
            CustomUtil.showTopSneakError(mContext, "You entered higher amount then available balance!");
            return false;
        }else if (friend_phone_number.getText().toString().trim().equals(preferences.getUserModel().getPhoneNo())) {
            CustomUtil.showTopSneakError(mContext, "You can't transfer money into your own account.");
            return false;
        }
        return true;
    }

    private void calculate_deduction() {
        if (CustomUtil.convertFloat(user_amount.getText().toString().trim()) <= CustomUtil.convertFloat(preferences.getUserModel().getDepositBal())) {
            deposit_send = CustomUtil.convertFloat(user_amount.getText().toString().trim());
            winnig_send = 0;
        } else {
            deposit_send = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
            winnig_send = CustomUtil.convertFloat(user_amount.getText().toString().trim()) - CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        }
    }
}