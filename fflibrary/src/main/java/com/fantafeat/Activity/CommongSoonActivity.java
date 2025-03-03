package com.fantafeat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommongSoonActivity extends BaseActivity {

   // private CircleImageView ludoDiced;
    private ImageView toolbar_back;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commong_soon);

        //ludoDiced=findViewById(R.id.ludoDiced);
        toolbar_back=findViewById(R.id.toolbar_back);

        /*ObjectAnimator rotate = ObjectAnimator.ofFloat(ludoDiced, "rotation", 360f, 0f);
        rotate.setDuration(5000);
        rotate.setRepeatCount(ObjectAnimator.INFINITE);
        rotate.start();*/

        toolbar_back.setOnClickListener(view -> {
            finish();
        });
    }
}