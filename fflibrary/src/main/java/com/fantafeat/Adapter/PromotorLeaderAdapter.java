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

import com.bumptech.glide.Glide;
import com.fantafeat.Model.PromotorsUserModel;
import com.fantafeat.Model.UserLeaderboardList;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PromotorLeaderAdapter extends RecyclerView.Adapter<PromotorLeaderAdapter.ViewHolder>{

    Context mContext;
    List<PromotorsUserModel> list;
    MyPreferences preferences;
    LeaderBoardListAdapter.onItemClick onItemClick;
    String winning_dec;

    public PromotorLeaderAdapter(Context mContext, List<PromotorsUserModel> list, LeaderBoardListAdapter.onItemClick onItemClick, String winning_dec) {
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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.promotors_leaderboard_item,parent,false) );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromotorsUserModel item=list.get(position);



        if (winning_dec.equalsIgnoreCase("yes")){

            if (TextUtils.isEmpty(item.getAsset_url())){
                holder.txtWinAmt.setVisibility(View.VISIBLE);
                holder.txtWinAmt.setText(CustomUtil.displayAmount( mContext, item.getTotalWinAmount()));
                holder.imgWinGadgets.setVisibility(View.GONE);

            }else {
                holder.txtWinAmt.setVisibility(View.GONE);
                holder.imgWinGadgets.setVisibility(View.VISIBLE);
                CustomUtil.loadImageWithGlide(mContext,holder.imgWinGadgets,ApiManager.PROFILE_IMG,item.getAsset_url(),R.drawable.ic_team1_placeholder);
            }
        }else {
            holder.txtWinAmt.setVisibility(View.VISIBLE);
            holder.txtWinAmt.setText("-");
            holder.imgWinGadgets.setVisibility(View.GONE);

        }

        if (TextUtils.isEmpty(item.getTotalAmt())){
            holder.txtPoints.setText("-");
        }
        else {
            if (item.getlType().equalsIgnoreCase("Contest"))
                holder.txtPoints.setText("Investment : "+CustomUtil.displayAmountFloat( mContext, item.getTotalAmt()));
            else{
                if (item.getTotalAmt().equalsIgnoreCase("0") || item.getTotalAmt().equalsIgnoreCase("0.0") ||
                        item.getTotalAmt().equalsIgnoreCase("0.00")){
                    holder.txtPoints.setText("Users : 0");
                }else {
                    holder.txtPoints.setText("Users : "+item.getTotalAmt().replaceAll(".00",""));
                }
            }

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
                //holder.txtWinAmt.setVisibility(View.GONE);
                //holder.imgWinGadgets.setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_two);
                holder.viewRank.setBackgroundResource(R.color.orange);
                //holder.txtWinAmt.setVisibility(View.GONE);
               // holder.imgWinGadgets.setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.txtRank.setVisibility(View.GONE);
                holder.imgRank.setVisibility(View.VISIBLE);
                holder.imgRank.setImageResource(R.drawable.ic_rank_three);
                holder.viewRank.setBackgroundResource(R.color.colorPrimary);
               // holder.txtWinAmt.setVisibility(View.GONE);
               // holder.imgWinGadgets.setVisibility(View.VISIBLE);

                break;
            default:
                holder.imgRank.setVisibility(View.GONE);
                holder.txtRank.setVisibility(View.VISIBLE);
               // holder.txtWinAmt.setVisibility(View.VISIBLE);
               // holder.imgWinGadgets.setVisibility(View.GONE);
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

        //holder.txtUserName.setText(item.getTotalAmt());
        holder.txtTeamName.setText(item.getRefBy());
        CustomUtil.loadImageWithGlide(mContext,holder.imgUser,ApiManager.PROFILE_IMG,item.getUser_img(),R.drawable.ic_team1_placeholder);

  /*      if (!TextUtils.isEmpty(item.getUser_img())){

            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG +item.getUser_img())
                    .placeholder(R.drawable.ic_team1_placeholder)
                    .into(holder.imgUser);
        }else {
            holder.imgUser.setImageResource(R.drawable.ic_team1_placeholder);
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
        //CircleImageView imgUser;
        ImageView imgRank,imgWinGadgets;
        View viewRank;
        CircleImageView imgUser;
        LinearLayout leader_layout/*,layRank*/;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtPoints=itemView.findViewById(R.id.txtPoints);
            txtRank=itemView.findViewById(R.id.txtRank);
            txtTeamName=itemView.findViewById(R.id.txtTeamName);
            //imgUser=itemView.findViewById(R.id.imgUser);
            leader_layout=itemView.findViewById(R.id.leader_layout);
            //layRank=itemView.findViewById(R.id.layRank);
            imgRank=itemView.findViewById(R.id.imgRank);
            viewRank=itemView.findViewById(R.id.viewRank);
            txtWinAmt=itemView.findViewById(R.id.txtWinAmt);
            imgWinGadgets=itemView.findViewById(R.id.imgWinGadgets);
            imgUser=itemView.findViewById(R.id.imgUser);

            itemView.setOnClickListener(v->{
                onItemClick.onClick(getAdapterPosition());
            });
        }
    }
}
