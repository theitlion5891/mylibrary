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
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.CustomUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardListAdapter extends RecyclerView.Adapter<LeaderBoardListAdapter.ViewHolder> {

    Context mContext;
    List<UserLeaderboardList.Datum> list;
    MyPreferences preferences;
    onItemClick onItemClick;
    String winning_dec;

    public LeaderBoardListAdapter(Context mContext, List<UserLeaderboardList.Datum> list, onItemClick onItemClick, String winning_dec) {
        this.mContext = mContext;
        this.list = list;
        this.onItemClick = onItemClick;
        this.winning_dec=winning_dec;
        preferences = MyApp.getMyPreferences();
    }

    public void updateWin(String winning_dec){
        this.winning_dec=winning_dec;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_leaderboard_item,parent,false) );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserLeaderboardList.Datum item=list.get(position);

        if (winning_dec.equalsIgnoreCase("yes")){
            holder.txtWinAmt.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(item.getTotal_win_amount())){
                holder.txtWinAmt.setVisibility(View.GONE);
            }else {
                if (item.getTotal_win_amount().equalsIgnoreCase("0")){
                    holder.txtWinAmt.setVisibility(View.GONE);
                }else {
                    if (item.getUserId().equals(preferences.getUserModel().getId())) {
                        holder.txtWinAmt.setText("You Won: "+ CustomUtil.displayAmount( mContext, item.getTotal_win_amount()));
                    }else {
                        holder.txtWinAmt.setText("Win: "+CustomUtil.displayAmount( mContext, item.getTotal_win_amount()));
                    }
                }
            }

        }else {
            holder.txtWinAmt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(item.getTotalPoint())){
            holder.txtPoints.setText("-");
        }else {
            holder.txtPoints.setText(item.getTotalPoint());
        }

        if (TextUtils.isEmpty(item.getTotalRank())){
            holder.txtRank.setText("-");
        }else {
            holder.txtRank.setText("#"+item.getTotalRank());
        }

        switch (item.getTotalRank()) {
            case "1":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_one);
                holder.viewRank.setBackgroundResource(R.color.green_pure);
                break;
            case "2":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_two);

                holder.viewRank.setBackgroundResource(R.color.orange);
                break;
            case "3":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_three);
                holder.viewRank.setBackgroundResource(R.color.colorPrimary);

                break;
            default:
                holder.imgRank.setVisibility(View.GONE);
                holder.txtRank.setVisibility(View.VISIBLE);
                break;
        }

        switch (item.getTotalRank()) {
            case "1":
                holder.leader_layout.setBackgroundResource(R.drawable.gradiant_rank_one);
                break;
            case "2":
                holder.leader_layout.setBackgroundResource(R.drawable.gradiant_rank_two);
                break;
            case "3":
                holder.leader_layout.setBackgroundResource(R.drawable.gradiant_rank_three);
                break;
            default:
                holder.viewRank.setBackgroundResource(R.color.gradientGrey);
                if (item.getUserId().equals(preferences.getUserModel().getId())) {
                    holder.leader_layout.setBackgroundResource(R.drawable.gradiant_rank_user);
                }else {
                    holder.leader_layout.setBackgroundResource(R.color.white_pure);
                }
                break;
        }

        holder.txtUserName.setText(item.getDisplayName());
        holder.txtTeamName.setText("@"+item.getUserTeamName());

        CustomUtil.loadImageWithGlide(mContext,holder.imgUser,MyApp.imageBase + MyApp.user_img,item.getUserImg(),
                R.drawable.ic_team1_placeholder);


        /*if (!TextUtils.isEmpty(item.getUserImg())){
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG +item.getUserImg())
                    .placeholder(R.drawable.user_image_place)
                    .into(holder.imgUser);
        }else {
            holder.imgUser.setImageResource(R.drawable.user_image_place);
        }*/

    }

    public interface onItemClick{
        public void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtUserName,txtPoints,txtRank,txtTeamName,txtWinAmt;
        CircleImageView imgUser;
        ImageView imgRank;
        View viewRank;
        LinearLayout leader_layout/*,layRank*/;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtPoints=itemView.findViewById(R.id.txtPoints);
            txtRank=itemView.findViewById(R.id.txtRank);
            txtTeamName=itemView.findViewById(R.id.txtTeamName);
            imgUser=itemView.findViewById(R.id.imgUser);
            leader_layout=itemView.findViewById(R.id.leader_layout);
            //layRank=itemView.findViewById(R.id.layRank);
            imgRank=itemView.findViewById(R.id.imgRank);
            viewRank=itemView.findViewById(R.id.viewRank);
            txtWinAmt=itemView.findViewById(R.id.txtWinAmt);

            itemView.setOnClickListener(v->{
                onItemClick.onClick(getAdapterPosition());
            });
        }
    }
}
