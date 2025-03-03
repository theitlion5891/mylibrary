package com.fantafeat.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;

public class PromoteAppActivity extends BaseActivity {

    private TextView txtDetail;
    private Button btnConnect;
    private ImageView toolbar_back;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_promote_app);
        initData();
        iniClick();

    }

    protected void setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blackSecondary));
        }
    }

    private void initData() {
        btnConnect=findViewById(R.id.btnConnect);
        toolbar_back=findViewById(R.id.toolbar_back);
        txtDetail=findViewById(R.id.txtDetail);
        String text = "Are you a <font color=#B20017><b>Youtuber</b></font> or Having a <font color=#2193D0><b>Telegram</b></font> channel or want to promote <font color=#1D1D1D><b>Fantafeat</b></font>?";
        txtDetail.setText(Html.fromHtml(text));

    }

    public void iniClick() {
        btnConnect.setOnClickListener(view -> {
            startActivity(new Intent(this,PromoteAppFormActivity.class));
        });

        toolbar_back.setOnClickListener(view->{
            finish();
        });
    }

}