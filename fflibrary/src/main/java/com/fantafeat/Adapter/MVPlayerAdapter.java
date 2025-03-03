package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.List;


public class MVPlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GroupModel.PlayersDatum> groupPlayerList;
    private MatchModel matchModel;
    private SelectPlayerListener listener;

    public MVPlayerAdapter(Context context, List<GroupModel.PlayersDatum> groupPlayerList, SelectPlayerListener listener) {
        this.context = context;
        this.groupPlayerList = groupPlayerList;
        this.listener = listener;
        matchModel= MyApp.getMyPreferences().getMatchModel();
    }

    @Override
    public int getItemViewType(int position) {
        if (groupPlayerList.get(position).isDisable()){
            return 1;
        }else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1){
            return new SelectedViewHolder(LayoutInflater.from(context).inflate(R.layout.selected_player_group,parent,false));
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_player_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder hol, int position) {
        GroupModel.PlayersDatum player=groupPlayerList.get(position);
        if (hol instanceof SelectedViewHolder){
            SelectedViewHolder holder=(SelectedViewHolder) hol;

            CustomUtil.loadImageWithGlide(context,holder.player_img, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.ic_team1_tshirt);

            holder.playerName.setText(player.getPlayerName().replace(" ","\n"));

            if (player.isChecked()){
                holder.teamReviewLayout.setBackgroundResource(R.drawable.selected_green_player);
              //  holder.imgSelectedChk.setImageResource(R.drawable.ic_checked_primary);
            }else if (player.isDisable()){
                holder.teamReviewLayout.setBackgroundResource(R.drawable.gray_border_selected);
               // holder.imgSelectedChk.setImageResource(R.drawable.ic_checked_gray);
            }else {
                holder.teamReviewLayout.setBackgroundResource(R.drawable.gray_border);
            }

            holder.player_type.setText(player.getPlayerType());

            if (matchModel.getTeamAXi().equalsIgnoreCase("Yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
                if (!TextUtils.isEmpty(player.getPlayingXi()) && !player.getPlayingXi().equalsIgnoreCase("Yes")) {
                    holder.inPlaying.setVisibility(View.VISIBLE);
                } else {
                    holder.inPlaying.setVisibility(View.GONE);
                }
            }

            holder.linearLayout3.setOnClickListener(v -> {
               // listener.onClick(player);
                if (!player.isDisable()){
                    if (!player.isDisable()){
                        for (int j=0;j< groupPlayerList.size();j++){
                            GroupModel.PlayersDatum player1= groupPlayerList.get(j);
                            player1.setChecked(false);
                        }
                        player.setChecked(true);
                        //confirmTeam(contestData,mainPosition,false);
                        notifyDataSetChanged();
                    }
                }
            });

            /*addView.setOnClickListener(view -> {
                //if (!player.getPlayerName().equalsIgnoreCase("Choose Player")){
                if (is_match_after){

                    PlayerModel mpm=new PlayerModel();
                    mpm.setCap_percent("0");
                    mpm.setId(player.getId());
                    mpm.setMatchId(player.getMatchId());
                    mpm.setPlayerId(player.getPlayerId());
                    mpm.setPlayerImage(player.getPlayerImage());
                    mpm.setPlayerName(player.getPlayerName());
                    mpm.setPlayerSname(player.getPlayerSname());
                    mpm.setPlayerType(player.getPlayerType());
                    mpm.setPlayerRank(player.getPlayerRank());
                    mpm.setPlayingXi(player.getPlayingXi());
                    mpm.setTotalPoints(player.getTotalPoints());
                    mpm.setTeamId(matchModel.getTeam1());

                    context.startActivity(new Intent(context, PlayerStateActivity.class)
                            .putExtra("playerModel",mpm)
                            .putExtra("league_id",matchModel.getLeagueId()));

                }
                else {
                    if (!player.isDisable()){
                        if (!player.isDisable()){
                            for (int j=0;j< groupPlayerList.size();j++){
                                PlayersDatum player1= groupPlayerList.get(j);
                                player1.setChecked(false);
                            }
                            player.setChecked(true);
                            //((SinglesContestActivity)context).confirmTeam(player,mainPosition,false);
                        }
                    }
                }
                // }
            });*/
        }
        else {
            ViewHolder holder=(ViewHolder) hol;

            CustomUtil.loadImageWithGlide(context,holder.player_img, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.ic_team1_tshirt);

            holder.playerName.setText(player.getPlayerName().replace(" ","\n"));

            if (player.isChecked()){
                holder.teamReviewLayout.setBackgroundResource(R.drawable.selected_green_player);
               // holder.imgSelectedChk.setImageResource(R.drawable.ic_checked_primary);
            }else if (player.isDisable()){
                holder.teamReviewLayout.setBackgroundResource(R.drawable.gray_border_selected);
               // holder.imgSelectedChk.setImageResource(R.drawable.ic_checked_gray);
            }else {
                holder.teamReviewLayout.setBackgroundResource(R.drawable.gray_border);
            }

            holder.player_type.setText(player.getPlayerType());

            if (matchModel.getTeamAXi().equalsIgnoreCase("Yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
                if (!player.getPlayingXi().equalsIgnoreCase("Yes")) {
                    holder.inPlaying.setVisibility(View.VISIBLE);
                } else {
                    holder.inPlaying.setVisibility(View.GONE);
                }
            }

            holder.linearLayout3.setOnClickListener(v -> {
                if (!player.isDisable()){
                    if (!player.isDisable()){
                        for (int j=0;j< groupPlayerList.size();j++){
                            GroupModel.PlayersDatum player1= groupPlayerList.get(j);
                            player1.setChecked(false);
                        }
                        player.setChecked(true);
                        //confirmTeam(contestData,mainPosition,false);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public interface SelectPlayerListener{
        public void onClick(GroupModel.PlayersDatum model);
    }

    @Override
    public int getItemCount() {
        return groupPlayerList.size();
    }

    class SelectedViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout teamReviewLayout;
       // ImageView imgSelectedChk ;
        LinearLayout linearLayout3 ;
        ImageView imgEdit ;
        TextView playerName ;
        TextView player_type ;
        ImageView player_img;
        ImageView inPlaying ;
        public SelectedViewHolder(@NonNull View addView) {
            super(addView);
            teamReviewLayout =  addView.findViewById(R.id.teamReviewLayout);
            //imgSelectedChk =  addView.findViewById(R.id.imgSelectedChk);
            imgEdit =  addView.findViewById(R.id.imgEdit);
            imgEdit.setVisibility(View.GONE);
            playerName =  addView.findViewById(R.id.player_name);
            playerName.setMaxLines(2);
            player_type = addView.findViewById(R.id.player_type);
            player_type.setSelected(true);
            inPlaying = addView.findViewById(R.id.inPlaying);
            player_img = addView.findViewById(R.id.player_img);
            linearLayout3 = addView.findViewById(R.id.linearLayout3);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout teamReviewLayout;
       // ImageView imgSelectedChk ;
        LinearLayout linearLayout3 ;
        ImageView imgEdit ;
        TextView playerName ;
        TextView player_type ;
        ImageView player_img;
        ImageView inPlaying ;

        public ViewHolder(@NonNull View addView) {
            super(addView);

            teamReviewLayout =  addView.findViewById(R.id.teamReviewLayout);
           // imgSelectedChk =  addView.findViewById(R.id.imgSelectedChk);
            imgEdit =  addView.findViewById(R.id.imgEdit);
            imgEdit.setVisibility(View.GONE);
            playerName =  addView.findViewById(R.id.player_name);
            playerName.setMaxLines(2);
            player_type = addView.findViewById(R.id.player_type);
            player_type.setSelected(true);
            inPlaying = addView.findViewById(R.id.inPlaying);
            player_img = addView.findViewById(R.id.player_img);
            linearLayout3 = addView.findViewById(R.id.linearLayout3);
        }
    }

}
