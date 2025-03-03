package com.fantafeat.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeamPreviewFragment extends BaseFragment {

    List<PlayerModel> playerModelList;
    private LinearLayout wicketLayout,batsmanLayout,batsmanLayout2,allRounderLayout,bowlerLayout,bowlerLayout2;
    private ImageView back_btn;

    public TeamPreviewFragment(List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_preview, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        back_btn = (ImageView) view.findViewById(R.id.back_icon);

        wicketLayout = (LinearLayout) view.findViewById(R.id.wicketLayout);
        batsmanLayout = (LinearLayout) view.findViewById(R.id.batsmanLayout);
        batsmanLayout2 = (LinearLayout) view.findViewById(R.id.batsmanLayout2);
        allRounderLayout = (LinearLayout) view.findViewById(R.id.allRounderLayout);
        bowlerLayout = (LinearLayout) view.findViewById(R.id.bowlerLayout);
        bowlerLayout2 = (LinearLayout) view.findViewById(R.id.bowlerLayout2);

        Map<String, ArrayList<PlayerModel>> hash = new HashMap<>();
        for(PlayerModel mp : CustomUtil.emptyIfNull(playerModelList)){
            if(hash.containsKey(mp.getTeamId())){
                hash.get(mp.getTeamId()).add(mp);
            }
            else{
                ArrayList<PlayerModel> matchPlayers = new ArrayList<>();
                matchPlayers.add(mp);
                hash.put(mp.getTeamId(),matchPlayers);
            }
        }

        playerModelList = new ArrayList<>();
        if(hash.containsKey(preferences.getMatchModel().getTeam1())) {
            playerModelList.addAll(Objects.requireNonNull(hash.get(preferences.getMatchModel().getTeam1())));
        } if(hash.containsKey(preferences.getMatchModel().getTeam2())){
            playerModelList.addAll(Objects.requireNonNull(hash.get(preferences.getMatchModel().getTeam2())));
        }

        init();
    }

    @Override
    public void initClick() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentUtil().removeFragment(getActivity(),
                        R.id.home_fragment_container,
                        TeamPreviewFragment.this,
                        FragmentUtil.ANIMATION_TYPE.SLIDE_UP_TO_DOWN);
            }
        });
    }

    private void init() {

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int Team1Count=0,Team2Count=0;
        int wk = 0,bat=0,ball=0,allRounder=0;
        for(PlayerModel mpm :  CustomUtil.emptyIfNull(playerModelList)){
            switch (mpm.getPlayerType()) {
                case "wk":
                    wk += 1;
                    break;
                case "bat":
                    bat += 1;
                    break;
                case "ar":
                    allRounder += 1;
                    break;
                case "bowl":
                    ball += 1;
                    break;
            }
        }
        int wkTemp= 0,batTemp=0,ballTemp=0,allRounderTemp=0;
        for(final PlayerModel mpm :  CustomUtil.emptyIfNull(playerModelList)){

            final View addView = layoutInflater.inflate(R.layout.row_team_preview, null);
            addView.setLayoutParams(param);

            TextView playerName = (TextView) addView.findViewById(R.id.player_name);
            TextView playerPoint = (TextView) addView.findViewById(R.id.player_point);
            ImageView imageView = (ImageView) addView.findViewById(R.id.player_img);
            TextView txtCVC = (TextView) addView.findViewById(R.id.txtCVC);
           // ImageView isLeader = (ImageView) addView.findViewById(R.id.isLeader);

            /*addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PlayerInfoActivity.class);
                    intent.putExtra("player", mpm);
                    mContext.startActivity(intent);
                }
            });*/

            if(mpm.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                Team1Count += 1;
                playerName.setBackgroundResource(R.drawable.white_round_fill);
                txtCVC.setBackgroundResource(R.drawable.white_circle);
                playerName.setTextColor(getResources().getColor(R.color.font_color));
                txtCVC.setTextColor(getResources().getColor(R.color.font_color));

                CustomUtil.loadImageWithGlide(mContext,imageView,ApiManager.DOCUMENTS,mpm.getPlayerImage(),R.drawable.ic_team1_tshirt);
                /*if(mpm.getPlayerImage() != null &&
                        !mpm.getPlayerImage().equals("")) {
                    Glide.with(mContext)
                            .load(ApiManager.IMGURL + mpm.getPlayerImage())
                            .centerCrop()
                            .error(R.drawable.ic_team1_tshirt)
                            .placeholder(R.drawable.ic_team1_tshirt)
                            .into(imageView);
                }else {
                    imageView.setImageResource(R.drawable.ic_team1_tshirt);
                }*/

            }if(mpm.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                Team2Count += 1;
                playerName.setBackgroundResource(R.drawable.black_round_fill);
                txtCVC.setBackgroundResource(R.drawable.black_circle);
                playerName.setTextColor( getResources().getColor(R.color.white_font));
                txtCVC.setTextColor(getResources().getColor(R.color.pureWhite));

                CustomUtil.loadImageWithGlide(mContext,imageView,ApiManager.DOCUMENTS,mpm.getPlayerImage(),R.drawable.ic_team2_tshirt);
              /*  if(mpm.getPlayerImage() != null &&
                        !mpm.getPlayerImage().equals("")) {
                    Glide.with(mContext)
                            .load(ApiManager.IMGURL + mpm.getPlayerImage())
                            .centerCrop()
                            .error(R.drawable.ic_team2_tshirt)
                            .placeholder(R.drawable.ic_team2_tshirt)
                            .into(imageView);
                }else {
                    imageView.setImageResource(R.drawable.ic_team2_tshirt);
                }*/
            }




            if (mpm.getIsCaptain().equalsIgnoreCase("Yes")) {
                txtCVC.setVisibility(View.VISIBLE);
                //isLeader.setImageResource(R.drawable.c);
                txtCVC.setText("C");
            } else if (mpm.getIsWiseCaptain().equalsIgnoreCase("Yes")) {
                txtCVC.setVisibility(View.VISIBLE);
                //isLeader.setImageResource(R.drawable.vc);
                txtCVC.setText("VC");
            }else{
                txtCVC.setVisibility(View.GONE);
            }

            playerName.setText(mpm.getPlayerSname());
            playerPoint.setText("Cr : " + mpm.getPlayerRank());

            switch (mpm.getPlayerType()) {
                case "wk":
                    wicketLayout.addView(addView);
                    break;
                case "bat":
                    batTemp += 1;
                    if(bat>=5){
                        if(bat==5){
                            if(batTemp <= 3){
                                batsmanLayout.addView(addView);
                            }else{
                                batsmanLayout2.addView(addView);
                            }
                        }else if(bat == 6){
                            if(batTemp <= 4){
                                batsmanLayout.addView(addView);
                            }else{
                                batsmanLayout2.addView(addView);
                            }
                        }else{
                            batsmanLayout.addView(addView);
                        }
                    }else{
                        batsmanLayout.addView(addView);
                    }
                    break;
                case "ar":
                    allRounderLayout.addView(addView);
                    break;
                case "bowl":
                    ballTemp += 1;
                    if(ball>=5){
                        if(ball==5 ){
                            if(ballTemp <= 3){
                                bowlerLayout.addView(addView);
                            }else{
                                bowlerLayout2.addView(addView);
                            }
                        }else if(ball == 6){
                            if(ballTemp <= 4){
                                bowlerLayout.addView(addView);
                            }else{
                                bowlerLayout2.addView(addView);
                            }
                        }else{
                            bowlerLayout.addView(addView);
                        }
                    }else{
                        bowlerLayout.addView(addView);
                    }
                    break;
            }
        }

        //prv_team1_name.setText(preferences.getMatchModel().getTeam1Sname() + " ("+Team1Count+")");
        //prv_team2_name.setText(preferences.getMatchModel().getTeam2Sname() + " ("+Team2Count+")");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playerModelList = null;
    }
}