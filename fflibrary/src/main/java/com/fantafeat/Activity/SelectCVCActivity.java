package com.fantafeat.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectCVCActivity extends BaseActivity {

    List<PlayerModel> playerModelList = new ArrayList<>();
    TextView points,type,match_title,timer;
    Button team_preview,save_team;
    RecyclerView select_cvc_list;
   // SelectCVCAdapter selectCVCAdapter;
    int is_captain = 0, is_vise_captain = 0;

    private MatchModel selected_match;
    private long diff;
    private CountDownTimer countDownTimer;
    private Toolbar toolbar;
    public ImageView mToolBarBack;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_c_v_c);
        Window window = SelectCVCActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        Intent intent = getIntent();
        String player_list = intent.getStringExtra("players");

        Type player = new TypeToken<List<PlayerModel>>() {
        }.getType();
        playerModelList = gson.fromJson(player_list, player);

        toolbar = findViewById(R.id.contest_list_toolbar);
        setSupportActionBar(toolbar);
        match_title = findViewById(R.id.match_title);
        timer = findViewById(R.id.timer);
        mToolBarBack = findViewById(R.id.toolbar_back);

        points = findViewById(R.id.points);
        type = findViewById(R.id.type);
        team_preview = findViewById(R.id.team_preview);
        save_team =findViewById(R.id.save_team);
        select_cvc_list =findViewById(R.id.select_cvc_list);

        if(preferences.getMatchModel()!=null){
            selected_match = preferences.getMatchModel();
        }

        if(selected_match!=null && selected_match.getMatchTitle()!=null){
            match_title.setText(selected_match.getTeam1Sname()+" vs "+selected_match.getTeam2Sname());
        }
        //setTimer();

        setData();
    }



    private void setData() {
       /* selectCVCAdapter = new SelectCVCAdapter(mContext,playerModelList,preferences,SelectCVCActivity.this );
        select_cvc_list.setLayoutManager(new LinearLayoutManager(mContext));
        select_cvc_list.setAdapter(selectCVCAdapter);*/
    }


}