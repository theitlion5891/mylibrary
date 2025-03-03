package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Fragment.CricketSelectPlayerFragment;
import com.fantafeat.Fragment.LineupsSelectionFragment;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;

import java.util.List;

public class LineupTeam2Adapter extends RecyclerView.Adapter<LineupTeam2Adapter.ViewHolder> {

    private Context context;
    private String sportId;
    private List<PlayerModel> list;
    private boolean isPoint;
    private Team2PlayerClick listener;
    private Fragment fragment;
    private MyPreferences preferences;

    public LineupTeam2Adapter(Context context, List<PlayerModel> playerModelList,String sportId,boolean isPoint,Team2PlayerClick listener,Fragment fragment) {
        this.context = context;
        this.list = playerModelList;
        this.sportId = sportId;
        this.isPoint = isPoint;
        this.listener = listener;
        this.fragment = fragment;
        preferences= MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lineup_team2_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerModel model=list.get(position);

        MatchModel matchModel=preferences.getMatchModel();

        if (((LineupsSelectionFragment) fragment).checkSelect.get(model.getPlayerId()) == 0) {
            holder.layClick.setBackgroundResource(R.color.white_pure);
            holder.imgPlus.setImageResource(R.drawable.ic_plus);
        } else {
            if (matchModel.getTeamAXi().equalsIgnoreCase("yes")) {
                if (model.getPlayingXi().equals("Yes")) {
                    holder.layClick.setBackgroundResource(R.color.selectGreen);
                    holder.imgPlus.setImageResource(R.drawable.ic_minus);
                }else {
                   // holder.layClick.setBackgroundResource(R.color.selectRed);
                    holder.imgPlus.setImageResource(R.drawable.ic_minus);
                    if (model.getOther_text().equalsIgnoreCase("substitute")){
                        holder.layClick.setBackgroundResource(R.color.lighest_blue);
                    }else
                        holder.layClick.setBackgroundResource(R.color.selectRed);
                }
            }else {
                holder.imgPlus.setImageResource(R.drawable.ic_minus);
                holder.layClick.setBackgroundResource(R.color.selectGreen);
            }
        }

        holder.team_player_name.setText(model.getPlayerSname());
        if (model.getPlayingXi().equalsIgnoreCase("yes")){
            holder.txtCnt.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(model.getBatting_order_no()) && !model.getBatting_order_no().equalsIgnoreCase("0")){
                holder.txtCnt.setText(""+model.getBatting_order_no());
            }
        }else {
            holder.txtCnt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(model.getOther_text2())){
            if (isPoint)
                holder.team_player_point.setText(model.getPlayerAvgPoint()+" pts • "+ model.getPlayerType());//156 pts • ALL
            else
                holder.team_player_point.setText(model.getPlayer_percent()+"% • "+ model.getPlayerType());//156 pts • ALL
        }else {
            if (model.getOther_text2().contains("Unavailable")){
                holder.team_player_point.setText(model.getOther_text2());
                holder.team_player_point.setTextColor(context.getResources().getColor(R.color.red));
            }else {
                if (isPoint)
                    holder.team_player_point.setText(model.getPlayerAvgPoint()+" pts • "+ model.getPlayerType());//156 pts • ALL
                else
                    holder.team_player_point.setText(model.getPlayer_percent()+"% • "+ model.getPlayerType());//156 pts • ALL
                holder.team_player_point.setTextColor(context.getResources().getColor(R.color.btnGrey));
            }
        }

        /*if (isPoint)
            holder.team_player_point.setText(model.getPlayerAvgPoint()+" pts • "+ model.getPlayerType());//156 pts • ALL
        else
            holder.team_player_point.setText(model.getPlayer_percent()+"% • "+ model.getPlayerType());//156 pts • ALL*/
            //holder.team_player_point.setText(model.getPlayerType()+" • "+model.getPlayer_percent()+"% sel by");//ALL • 156 pts

        if (sportId.equalsIgnoreCase("1")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img, MyApp.imageBase + MyApp.document,
                        model.getPlayerImage(),R.drawable.ic_team1_tshirt);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img, MyApp.imageBase + MyApp.document,
                        model.getPlayerImage(),R.drawable.ic_team2_tshirt);
            }
        }else if (sportId.equalsIgnoreCase("2")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.football_player1);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.football_player2);
            }
        }else if (sportId.equalsIgnoreCase("4")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.basketball_team1);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.basketball_team2);
            }
        }else if (sportId.equalsIgnoreCase("3")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.baseball_player1);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.baseball_player2);
            }

        }else if (sportId.equalsIgnoreCase("6")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.handball_player1);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.handball_player2);
            }

        }else if (sportId.equalsIgnoreCase("7")){
            if(model.getTeamId().equals(matchModel.getTeam1())){
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.kabaddi_player1);
            }else {
                CustomUtil.loadImageWithGlide(context,holder.team_player_img,MyApp.imageBase + MyApp.document,  model.getPlayerImage(),R.drawable.kabaddi_player2);
            }
        }

        holder.team_player_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    /*new FragmentUtil().addFragment((FragmentActivity) context,
                            R.id.fragment_container,
                            new PlayerStatesFragment(model, matchModel.getLeagueId()),
                            ((HomeActivity) context).fragmentTag(40),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                }
            }
        });

        holder.layClick.setOnClickListener(view -> {
            if (matchModel.getSportId().equals("1")){
                if(((LineupsSelectionFragment)fragment).validateCricketClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (matchModel.getSportId().equals("2")){
                if(((LineupsSelectionFragment)fragment).validateFootballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (matchModel.getSportId().equals("4")){
                if(((LineupsSelectionFragment)fragment).validateBasketballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (matchModel.getSportId().equals("6")){
                if(((LineupsSelectionFragment)fragment).validateHandballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (matchModel.getSportId().equals("7")){
                if(((LineupsSelectionFragment)fragment).validateKabaddiClick(model)){
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }else if (matchModel.getSportId().equals("3")){
                if(((LineupsSelectionFragment)fragment).validateBaseballClick(model)){
                    //Log.e(TAG, "onClick: IF");
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    public interface Team2PlayerClick{
        public void onClick(PlayerModel playerModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView team_player_img,imgPlus;
        private TextView team_player_name,team_player_point,txtCnt;
        private LinearLayout layClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            team_player_img=itemView.findViewById(R.id.team_player_img);
            team_player_name=itemView.findViewById(R.id.team_player_name);
            team_player_point=itemView.findViewById(R.id.team_player_point);
            imgPlus=itemView.findViewById(R.id.imgPlus);
            txtCnt=itemView.findViewById(R.id.txtCnt);
            layClick=itemView.findViewById(R.id.layClick);

        }
    }

}
