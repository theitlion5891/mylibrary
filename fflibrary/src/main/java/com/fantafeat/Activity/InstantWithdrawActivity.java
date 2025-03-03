package com.fantafeat.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.CustomUtil;

import java.text.DecimalFormat;

public class InstantWithdrawActivity extends BaseActivity {

    @Override
    public void initClick() {

    }

    public TextView total_winning_bal,toolbar_title,txtAmt,txtCharge,txtTotal;
    ImageView toolbar_back;
    EditText add_amount_edit;
    public float winning_bal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_instant_withdraw);

        initData();
    }

    private void initData() {
        txtAmt = findViewById(R.id.txtAmt);
        txtCharge = findViewById(R.id.txtCharge);
        txtTotal = findViewById(R.id.txtTotal);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_back = findViewById(R.id.toolbar_back);
        total_winning_bal = findViewById(R.id.total_winning_bal);
        add_amount_edit = findViewById(R.id.add_amount_edit);

        winning_bal = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());

        toolbar_title.setText("Instant Withdraw");
        total_winning_bal.setText(mContext.getResources().getString(R.string.rs) + new DecimalFormat("00.00").format(winning_bal));

        add_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    txtAmt.setText(getResources().getString(R.string.rs)+"0.0");
                    txtCharge.setText(getResources().getString(R.string.rs)+"0");
                    txtTotal.setText(getResources().getString(R.string.rs)+"0.0");
                    return;
                }
                float amount=CustomUtil.convertFloat(s.toString());
                txtAmt.setText(getResources().getString(R.string.rs)+amount);
                if (amount>0){
                    int charge=0;
                    if (amount<=1000){
                        charge=2;
                    }else if (amount<=10000){
                        charge=5;
                    }else if (amount>=10001){
                        charge=10;
                    }
                    float total=amount-charge;

                    txtCharge.setText("-"+getResources().getString(R.string.rs)+charge);
                    if (total<0){
                        txtTotal.setText("-"+getResources().getString(R.string.rs)+String.valueOf(total).replace("-",""));
                    }else {
                        txtTotal.setText(getResources().getString(R.string.rs)+total);
                    }

                }else {
                    txtAmt.setText(getResources().getString(R.string.rs)+"0.0");
                    txtCharge.setText(getResources().getString(R.string.rs)+"0");
                    txtTotal.setText(getResources().getString(R.string.rs)+"0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar_back.setOnClickListener(v->{
            finish();
        });
    }
}