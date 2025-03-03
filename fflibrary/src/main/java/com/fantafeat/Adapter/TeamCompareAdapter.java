package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class TeamCompareAdapter extends RecyclerView.Adapter<TeamCompareAdapter.Holder> {

    Context mContext;
    List<PlayerModel> left;
    List<PlayerModel> right;
    MyPreferences preferences;

    public TeamCompareAdapter(Context mContext, List<PlayerModel> left, List<PlayerModel> right) {
        this.mContext = mContext;
        this.left = left;
        this.right = right;
        preferences = MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.row_compare_player, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PlayerModel leftList = left.get(holder.getAdapterPosition());
        PlayerModel rightList = right.get(holder.getAdapterPosition());

        holder.txtPlayerName.setText(leftList.getPlayerSname());
        holder.txtOppPlayerName.setText(rightList.getPlayerSname());

        //Log.e(TAG, "onBindViewHolder: "+left.get(position).getPlayerPoint()+"  "+right.get(position).getPlayerPoint());
        holder.txtScore.setText(leftList.getTotalPoints());
        holder.txtOppScore.setText(rightList.getTotalPoints());

        if(leftList.getIsCaptain().equalsIgnoreCase("yes")){
            holder.left_team_cvc.setText("C");
            //holder.left_team_cvc.setImageDrawable(mContext.getResources().getDrawable(R.drawable.c));
        }else if(leftList.getIsWiseCaptain().equalsIgnoreCase("yes")){
            holder.left_team_cvc.setText("VC");
            //holder.left_team_cvc.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vc));
        }else{
            holder.left_team_cvc.setVisibility(View.GONE);
        }

        if(rightList.getIsCaptain().equalsIgnoreCase("yes")){
            //holder.right_team_cvc.setImageDrawable(mContext.getResources().getDrawable(R.drawable.c));
            holder.right_team_cvc.setText("C");
        }else if(rightList.getIsWiseCaptain().equalsIgnoreCase("yes")){
            //holder.right_team_cvc.setImageDrawable(mContext.getResources().getDrawable(R.drawable.vc));
            holder.right_team_cvc.setText("VC");
        }else{
            holder.right_team_cvc.setVisibility(View.GONE);
        }

        String leftTeamName = "";
        if (leftList.getTeamId().equalsIgnoreCase(preferences.getMatchModel().getTeam1())) {
            leftTeamName = preferences.getMatchModel().getTeam1Sname();
        } else if (leftList.getTeamId().equalsIgnoreCase(preferences.getMatchModel().getTeam2())) {
            leftTeamName = preferences.getMatchModel().getTeam2Sname();
        }

        String rightTeamName = "";
        if (rightList.getTeamId().equalsIgnoreCase(preferences.getMatchModel().getTeam1())) {
            rightTeamName = preferences.getMatchModel().getTeam1Sname();
        } else if (rightList.getTeamId().equalsIgnoreCase(preferences.getMatchModel().getTeam2())) {
            rightTeamName = preferences.getMatchModel().getTeam2Sname();
        }

        holder.type_left.setText(leftTeamName + " | " + leftList.getPlayerType());
        holder.type_right.setText(rightTeamName + " | " + rightList.getPlayerType());

        String sportId=preferences.getMatchModel().getSportId();
        if (sportId.equals("1")){

            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.ic_team1_tshirt);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.ic_team1_tshirt)
                            .placeholder(R.drawable.ic_team1_tshirt)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.ic_team2_tshirt);
                 /*   Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.ic_team2_tshirt)
                            .placeholder(R.drawable.ic_team2_tshirt)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.ic_team1_tshirt)
                            .placeholder(R.drawable.ic_team1_tshirt)
                            .error(R.drawable.ic_team1_tshirt)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.ic_team2_tshirt)
                            .placeholder(R.drawable.ic_team2_tshirt)
                            .error(R.drawable.ic_team2_tshirt)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.ic_team1_tshirt);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.ic_team1_tshirt)
                            .placeholder(R.drawable.ic_team1_tshirt)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.ic_team2_tshirt);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.ic_team2_tshirt)
                            .placeholder(R.drawable.ic_team2_tshirt)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.ic_team1_tshirt)
                            .placeholder(R.drawable.ic_team1_tshirt)
                            .error(R.drawable.ic_team1_tshirt)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.ic_team2_tshirt)
                            .placeholder(R.drawable.ic_team2_tshirt)
                            .error(R.drawable.ic_team2_tshirt)
                            .into(holder.imgOppUserProfile);
                }
            }
        }
        else if (sportId.equalsIgnoreCase("2")){
            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.football_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.football_player1)
                            .placeholder(R.drawable.football_player1)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.football_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.football_player2)
                            .placeholder(R.drawable.football_player2)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.football_player1)
                            .placeholder(R.drawable.football_player1)
                            .error(R.drawable.football_player1)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.football_player2)
                            .placeholder(R.drawable.football_player2)
                            .error(R.drawable.football_player2)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.football_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.football_player1)
                            .placeholder(R.drawable.football_player1)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.football_player2);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.football_player2)
                            .placeholder(R.drawable.football_player2)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.football_player1)
                            .placeholder(R.drawable.football_player1)
                            .error(R.drawable.football_player1)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.football_player2)
                            .placeholder(R.drawable.football_player2)
                            .error(R.drawable.football_player2)
                            .into(holder.imgOppUserProfile);
                }
            }
        }
        else if (sportId.equalsIgnoreCase("4")){
            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.basketball_team1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),R.drawable.basketball_team2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .error(R.drawable.basketball_team1)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .error(R.drawable.basketball_team2)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.basketball_team1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,rightList.getPlayerImage(),R.drawable.basketball_team2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .error(R.drawable.basketball_team1)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .error(R.drawable.basketball_team2)
                            .into(holder.imgOppUserProfile);
                }
            }
        }
        else if (sportId.equals("3")){
            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),
                            R.drawable.baseball_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,leftList.getPlayerImage(),
                            R.drawable.baseball_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.baseball_player1)
                            .placeholder(R.drawable.baseball_player1)
                            .error(R.drawable.baseball_player1)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.baseball_player2)
                            .placeholder(R.drawable.baseball_player2)
                            .error(R.drawable.baseball_player2)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.baseball_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.baseball_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.baseball_player1)
                            .placeholder(R.drawable.baseball_player1)
                            .error(R.drawable.baseball_player1)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.baseball_player2)
                            .placeholder(R.drawable.baseball_player2)
                            .error(R.drawable.baseball_player2)
                            .into(holder.imgOppUserProfile);
                }
            }
        }
        else if (sportId.equals("6")){
            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,
                            leftList.getPlayerImage(),R.drawable.handball_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,
                            leftList.getPlayerImage(),R.drawable.handball_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.handball_player1)
                            .placeholder(R.drawable.handball_player1)
                            .error(R.drawable.handball_player1)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.handball_player2)
                            .placeholder(R.drawable.handball_player2)
                            .error(R.drawable.handball_player2)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.handball_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.handball_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.handball_player1)
                            .placeholder(R.drawable.handball_player1)
                            .error(R.drawable.handball_player1)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.handball_player2)
                            .placeholder(R.drawable.handball_player2)
                            .error(R.drawable.handball_player2)
                            .into(holder.imgOppUserProfile);
                }
            }
        }
        else if (sportId.equals("7")){
            if (leftList.getPlayerImage() != null && !leftList.getPlayerImage().equals("")) {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,
                            leftList.getPlayerImage(),R.drawable.kabaddi_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgUSerProfile);*/
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgUSerProfile,ApiManager.DOCUMENTS,
                            leftList.getPlayerImage(),R.drawable.kabaddi_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + leftList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgUSerProfile);*/
                }

            }
            else {
                if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.kabaddi_player1)
                            .placeholder(R.drawable.kabaddi_player1)
                            .error(R.drawable.kabaddi_player1)
                            .into(holder.imgUSerProfile);
                }else if (leftList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.kabaddi_player2)
                            .placeholder(R.drawable.kabaddi_player2)
                            .error(R.drawable.kabaddi_player2)
                            .into(holder.imgUSerProfile);
                }

            }

            if (rightList.getPlayerImage() != null && !rightList.getPlayerImage().equals("")) {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.kabaddi_player1);
                    /*Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team1)
                            .placeholder(R.drawable.basketball_team1)
                            .into(holder.imgOppUserProfile);*/
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    CustomUtil.loadImageWithGlide(mContext,holder.imgOppUserProfile,ApiManager.DOCUMENTS,
                            rightList.getPlayerImage(),R.drawable.kabaddi_player2);
                   /* Glide.with(mContext)
                            .load(ApiManager.DOCUMENTS + rightList.getPlayerImage())
                            .error(R.drawable.basketball_team2)
                            .placeholder(R.drawable.basketball_team2)
                            .into(holder.imgOppUserProfile);*/
                }

            }
            else {
                if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                    Glide.with(mContext)
                            .load(R.drawable.kabaddi_player1)
                            .placeholder(R.drawable.kabaddi_player1)
                            .error(R.drawable.kabaddi_player1)
                            .into(holder.imgOppUserProfile);
                }else if (rightList.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                    Glide.with(mContext)
                            .load(R.drawable.kabaddi_player2)
                            .placeholder(R.drawable.kabaddi_player2)
                            .error(R.drawable.kabaddi_player2)
                            .into(holder.imgOppUserProfile);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return left.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView imgUSerProfile;
        private TextView txtPlayerName;
        private TextView txtScore;
        private TextView txtOppScore, type_right, type_left;
        private TextView txtOppPlayerName;
        private ImageView imgOppUserProfile;
        private LinearLayout linearLayout;
        private TextView left_team_cvc,right_team_cvc;

        public Holder(View itemView) {
            super(itemView);

            imgUSerProfile = itemView.findViewById(R.id.imgUSerProfile);
            txtPlayerName = (TextView) itemView.findViewById(R.id.txtPlayerName);
            txtScore = (TextView) itemView.findViewById(R.id.txtScore);
            txtOppScore = (TextView) itemView.findViewById(R.id.txtOppScore);
            txtOppPlayerName = (TextView) itemView.findViewById(R.id.txtOppPlayerName);
            imgOppUserProfile =  itemView.findViewById(R.id.imgOppUserProfile);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.LinearClick);

            type_right = itemView.findViewById(R.id.type_right);
            type_left = itemView.findViewById(R.id.type_left);
            right_team_cvc = itemView.findViewById(R.id.right_team_cvc);
            left_team_cvc = itemView.findViewById(R.id.left_team_cvc);
        }
    }
}
