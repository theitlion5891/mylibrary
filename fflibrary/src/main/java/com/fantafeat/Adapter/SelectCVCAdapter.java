package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantafeat.Fragment.SelectCVCFragment;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class SelectCVCAdapter extends RecyclerView.Adapter<SelectCVCAdapter.SelectCVCHolder>{

    Context context;
    List<PlayerModel> playerModelList;
    MyPreferences preferences;
    SelectCVCFragment selectCVCFragment;

    public SelectCVCAdapter(Context context, List<PlayerModel> playerModelList, MyPreferences preferences, SelectCVCFragment selectCVCFragment){
        this.context = context;
        this.playerModelList = playerModelList;
        this.preferences = preferences;
        this.selectCVCFragment = selectCVCFragment;
    }
    @NonNull
    @Override
    public SelectCVCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectCVCHolder(LayoutInflater.from(context).inflate(R.layout.row_select_cvc,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectCVCHolder holder, int i) {
        final PlayerModel model = playerModelList.get(i);

        if (((SelectCVCFragment) selectCVCFragment).type_selected == 0 || (holder.getAdapterPosition() > 0 &&
                playerModelList.get(holder.getAdapterPosition() - 1).getPlayerType().equalsIgnoreCase(model.getPlayerType()))) {
            holder.selected_cvc_header.setVisibility(View.GONE);
        }
        else {
            holder.selected_cvc_header.setVisibility(View.VISIBLE);
            String playerType = "";
            if (preferences.getMatchModel().getSportId().equals("1")){
                if (model.getPlayerType().equalsIgnoreCase("WK")) {
                    playerType = "Wicket Keeper";
                } else if (model.getPlayerType().equalsIgnoreCase("AR")) {
                    playerType = "All Rounder";
                } else if (model.getPlayerType().equalsIgnoreCase("BAT")) {
                    playerType = "Batsman";
                } else if (model.getPlayerType().equalsIgnoreCase("BOWL")) {
                    playerType = "Bowler";
                }
            }
            else  if (preferences.getMatchModel().getSportId().equals("2")){
                if (model.getPlayerType().equalsIgnoreCase("GK")) {
                    playerType = "Goal Keeper";
                } else if (model.getPlayerType().equalsIgnoreCase("MID")) {
                    playerType = "Mid Fielder";
                } else if (model.getPlayerType().equalsIgnoreCase("DEF")) {
                    playerType = "Defender";
                } else if (model.getPlayerType().equalsIgnoreCase("FOR")) {
                    playerType = "Forward";
                }
            }
            else  if (preferences.getMatchModel().getSportId().equals("3")){
                if (model.getPlayerType().equalsIgnoreCase("OF")) {
                    playerType = "Outfielder";
                } else if (model.getPlayerType().equalsIgnoreCase("IF")) {
                    playerType = "Infielder";
                } else if (model.getPlayerType().equalsIgnoreCase("pit")) {
                    playerType = "Pitcher";
                } else if (model.getPlayerType().equalsIgnoreCase("car")) {
                    playerType = "Catcher";
                }
            }
            else if (preferences.getMatchModel().getSportId().equals("6")){
                if (model.getPlayerType().equalsIgnoreCase("GK")) {
                    playerType = "Goal Keeper";
                } else if (model.getPlayerType().equalsIgnoreCase("DEF")) {
                    playerType = "Defender";
                } else if (model.getPlayerType().equalsIgnoreCase("FWD")) {
                    playerType = "Forward";
                }
            }
            else if (preferences.getMatchModel().getSportId().equals("7")){
                if (model.getPlayerType().equalsIgnoreCase("DEF")) {
                    playerType = "Defender";
                } else if (model.getPlayerType().equalsIgnoreCase("AR")) {
                    playerType = "All Rounder";
                } else if (model.getPlayerType().equalsIgnoreCase("RAID")) {
                    playerType = "Raider";
                }
            }else  if (preferences.getMatchModel().getSportId().equals("4")){
                if (model.getPlayerType().equalsIgnoreCase("PG")) {
                    playerType = "Point Guard";
                } else if (model.getPlayerType().equalsIgnoreCase("SF")) {
                    playerType = "Small Forward";
                } else if (model.getPlayerType().equalsIgnoreCase("SG")) {
                    playerType = "Shooting Guard";
                } else if (model.getPlayerType().equalsIgnoreCase("PF")) {
                    playerType = "Power Forward";
                }else if (model.getPlayerType().equalsIgnoreCase("CR")) {
                    playerType = "Center";
                }
            }

            holder.selected_cvc_header.setText(playerType);
        }

        holder.player_points.setText(model.getPlayerAvgPoint());
        holder.pleayer_name.setText(model.getPlayerSname());
        holder.playerType.setText(model.getPlayerType());
        holder.playerCredit.setText("Credit: "+ CustomUtil.convertFloat(model.getPlayerRank()));
        /*if(Integer.parseInt(model.getTeamId())==1){
            holder.team_name.setText(preferences.getMatchModel().getTeam1Sname());
        }else{
            holder.team_name.setText(preferences.getMatchModel().getTeam2Sname());
        }*/

        if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
            holder.team_name.setBackground(context.getResources().getDrawable(R.drawable.team2_name_select_player));
            holder.team_name.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.team_name.setText(preferences.getMatchModel().getTeam1Sname());
        }else{
            holder.team_name.setBackground(context.getResources().getDrawable(R.drawable.team1_name_select_player));
            holder.team_name.setTextColor(context.getResources().getColor(R.color.pureWhite));
            holder.team_name.setText(preferences.getMatchModel().getTeam2Sname());
        }

        String sportId=preferences.getMatchModel().getSportId();

        if (preferences.getUpdateMaster().getIs_enable_selc_per().equalsIgnoreCase("yes") &&
                !TextUtils.isEmpty(model.getCap_percent())){
            holder.cSelected_by.setVisibility(View.VISIBLE);
            holder.cSelected_by.setText(model.getCap_percent()+"%");
        }else {
            holder.cSelected_by.setVisibility(View.GONE);
        }
        if (preferences.getUpdateMaster().getIs_enable_selc_per().equalsIgnoreCase("yes") &&
                !TextUtils.isEmpty(model.getWise_cap_percent())){
            holder.vcSelected_by.setVisibility(View.VISIBLE);
            holder.vcSelected_by.setText(model.getWise_cap_percent()+"%");
        }else {
            holder.vcSelected_by.setVisibility(View.GONE);
        }

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")) {
            if (model.getPlayingXi().equals("Yes")) {
                holder.team_xi_sign.setVisibility(View.GONE);
            }
            else if (model.getPlayingXi().equals("No")) {

                holder.team_xi_sign.setVisibility(View.VISIBLE);
                if (model.getOther_text().equalsIgnoreCase("substitute")){
                    holder.team_xi_sign.setImageResource(R.drawable.substitute_dot);
                }else
                    holder.team_xi_sign.setImageResource(R.drawable.nonplay);
            }
            else {
                holder.team_xi_sign.setVisibility(View.GONE);
            }
        }else {
            holder.team_xi_sign.setVisibility(View.GONE);
        }

        if (sportId.equals("1")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.ic_team1_tshirt);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (sportId.equals("2")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.football_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.football_player2);
            }
        }
        else if (sportId.equals("4")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.basketball_team1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.basketball_team2);
            }
        }
        else if (sportId.equals("3")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.baseball_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.baseball_player2);
            }
        }
        else if (sportId.equals("6")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.handball_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.handball_player2);
            }
        }
        else if (sportId.equals("7")){
            if(model.getTeamId().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.kabaddi_player1);
            }else if(model.getTeamId().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(context,holder.player_image,ApiManager.TEAM_IMG,model.getPlayerImage(),R.drawable.kabaddi_player2);
            }
        }

        if (model.getIsCaptain().toLowerCase().equals("yes")) {
            holder.captain_select.setText("2x");
            holder.captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
            holder.captain_select.setBackgroundResource(R.drawable.selected_cvc);
        }else {
            holder.captain_select.setText("C");
            holder.captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.captain_select.setBackgroundResource(R.drawable.unselected_cvc);
        }

        if (model.getIsWiseCaptain().toLowerCase().equals("yes")) {
            holder.vise_captain_select.setText("1.5x");
            holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
            holder.vise_captain_select.setBackgroundResource(R.drawable.selected_cvc);
        }else {
            holder.vise_captain_select.setText("VC");
            holder.vise_captain_select.setBackgroundResource(R.drawable.unselected_cvc);
            holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
        }

        /*if ((selectCVCFragment).c != holder.getAdapterPosition()) {
            holder.captain_select.setText("C");
            holder.captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.captain_select.setBackgroundResource(R.drawable.unselected_cvc);
        } else {
            holder.captain_select.setText("2x");
            holder.captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
            holder.captain_select.setBackgroundResource(R.drawable.selected_cvc);
        }

        if ((selectCVCFragment).vc != holder.getAdapterPosition()) {
            holder.vise_captain_select.setText("VC");
            holder.vise_captain_select.setBackgroundResource(R.drawable.unselected_cvc);
            holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
        } else {
            holder.vise_captain_select.setText("1.5x");
            holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
            holder.vise_captain_select.setBackgroundResource(R.drawable.selected_cvc);
        }*/

        /*holder.captain_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((selectCVCFragment).check_captain(model)){
                    notifyItemChanged(holder.getAdapterPosition());
                }
                */
        /*if(((SelectCVCFragment)fragment).check_captain(model)){

                }else{
                    holder.captain_select.setText("C");
                    holder.captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
                    holder.captain_select.setBackgroundResource(R.drawable.captain_button);
                    //holder.captain_select.getBackground().setTint(context.getResources().getColor(R.color.transparent));
                }*//*
            }
        });*/

        holder.captain_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model.getIsWiseCaptain().toLowerCase().equals("yes")) {
                    model.setIsWiseCaptain("no");
                    (selectCVCFragment).vcid = "";
                }

                for (int k=0;k<playerModelList.size();k++){
                    PlayerModel pModal=playerModelList.get(k);
                    if (pModal.getIsCaptain().toLowerCase().equals("yes")){
                        pModal.setIsCaptain("no");
                      //  notifyItemChanged(k);
                    }
                }
                (selectCVCFragment).cid = model.getId();
                (selectCVCFragment).c = i;
                model.setIsCaptain("yes");
               // notifyItemChanged(i);
                notifyDataSetChanged();
                /*PlayerModel pre = null;

                if (holder.getAdapterPosition() == (selectCVCFragment).vc) {
                    int temp = (selectCVCFragment).vc;
                    (selectCVCFragment).vc = -1;
                    notifyItemChanged(temp);

                }
                if ((selectCVCFragment).c != -1) {
                    notifyItemChanged((selectCVCFragment).c);
                    pre = playerModelList.get((selectCVCFragment).c);
                    playerModelList.remove(pre);
                    pre.setIsCaptain("No");
                    playerModelList.add((selectCVCFragment).c, pre);
                }


                playerModelList.remove(i);
                model.setIsCaptain("Yes");
                playerModelList.add(i, model);
                (selectCVCFragment).c = holder.getAdapterPosition();*/

                //holder.selected_c.setBackgroundResource(R.drawable.captain_selected);
                holder.captain_select.setText("2x");
                holder.captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
                holder.captain_select.setBackgroundResource(R.drawable.selected_cvc);

                ((SelectCVCFragment) selectCVCFragment).changeNextBg();
            }

        });

        holder.vise_captain_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model.getIsCaptain().toLowerCase().equals("yes")) {
                    model.setIsCaptain("no");
                    (selectCVCFragment).cid = "";
                }

                for (int k=0;k<playerModelList.size();k++){
                    PlayerModel pModal=playerModelList.get(k);
                    if (pModal.getIsWiseCaptain().toLowerCase().equals("yes")){
                        pModal.setIsWiseCaptain("no");
                      //  notifyItemChanged(k);
                    }

                }
                (selectCVCFragment).vcid = model.getId();
                model.setIsWiseCaptain("yes");
             //   notifyItemChanged(i);
                notifyDataSetChanged();
               /* PlayerModel pre = null;
                if (holder.getAdapterPosition() == (selectCVCFragment).c) {
                    int temp = (selectCVCFragment).c;
                    (selectCVCFragment).c = -1;
                    notifyItemChanged(temp);

                }
                if ((selectCVCFragment).vc != -1) {
                    notifyItemChanged((selectCVCFragment).vc);
                    pre = playerModelList.get((selectCVCFragment).vc);
                    playerModelList.remove(pre);
                    pre.setIsWiseCaptain("No");
                    playerModelList.add((selectCVCFragment).vc, pre);
                }

                playerModelList.remove(i);
                model.setIsWiseCaptain("Yes");
                playerModelList.add(i, model);
                (selectCVCFragment).vc = holder.getAdapterPosition();*/

                holder.vise_captain_select.setText("1.5x");
                holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.white_font));
                holder.vise_captain_select.setBackgroundResource(R.drawable.selected_cvc);

                ((SelectCVCFragment) selectCVCFragment).changeNextBg();
            }
        });

        /*holder.vise_captain_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((selectCVCFragment).check_vise_captain(model)){
                    notifyItemChanged(holder.getAdapterPosition());
                }

                *//*if(((SelectCVCFragment)fragment).check_vise_captain(model)){
                }else{
                    holder.vise_captain_select.setText("VC");
                    holder.vise_captain_select.setBackgroundResource(R.drawable.captain_button);
                    holder.vise_captain_select.setTextColor(context.getResources().getColor(R.color.font_color));
                    holder.vise_captain_select.getBackground().setTint(context.getResources().getColor(R.color.transparent));
                }*//*
            }
        });*/
    }

    public void updateSelectCVC(List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return playerModelList.size();
    }

    public class SelectCVCHolder extends RecyclerView.ViewHolder{

        TextView team_name,pleayer_name,player_points,captain_select,vise_captain_select,playerType,playerCredit,
                selected_cvc_header,cSelected_by,vcSelected_by;
        ImageView player_image,team_xi_sign;

        public SelectCVCHolder(@NonNull View itemView) {
            super(itemView);
            team_name = itemView.findViewById(R.id.team_name);
            pleayer_name = itemView.findViewById(R.id.pleayer_name);
            player_points = itemView.findViewById(R.id.player_points);
            captain_select = itemView.findViewById(R.id.captain_select);
            vise_captain_select = itemView.findViewById(R.id.vise_captain_select);
            player_image = itemView.findViewById(R.id.player_image);
            playerType = itemView.findViewById(R.id.playerType);
            playerCredit = itemView.findViewById(R.id.playerCredit);
            selected_cvc_header = itemView.findViewById(R.id.selected_cvc_header);
            cSelected_by = itemView.findViewById(R.id.cSelected_by);
            vcSelected_by = itemView.findViewById(R.id.vcSelected_by);
            team_xi_sign = itemView.findViewById(R.id.team_xi_sign);
        }
    }

    public void updateList(List<PlayerModel> playerModelList) {
        this.playerModelList = playerModelList;
        notifyDataSetChanged();
    }
}
