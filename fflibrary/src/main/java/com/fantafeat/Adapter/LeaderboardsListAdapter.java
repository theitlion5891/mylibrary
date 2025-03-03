package com.fantafeat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantafeat.Model.LeaderboardListModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class LeaderboardsListAdapter extends RecyclerView.Adapter<LeaderboardsListAdapter.ViewHolder> {

    private ArrayList<LeaderboardListModel> list;
    private Context context;
    private LeaderboardClick listener;

    public LeaderboardsListAdapter(ArrayList<LeaderboardListModel> list, Context context, LeaderboardClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.leaderboard_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardListModel model=list.get(position);

        /*holder.txtTitle.setText(model.getLbListTitle());
        if (model.getId().equalsIgnoreCase("1")){
            holder.txtTitle.setBackgroundResource(R.drawable.ic_fantasy_leaderboard_lblbg);
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.layMain.setBackgroundResource(R.drawable.fantasy_leaderboard_bg);
        }else if (model.getId().equalsIgnoreCase("2")){
            holder.txtTitle.setBackgroundResource(R.drawable.ic_game_leaderboard_lblbg);
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.layMain.setBackgroundResource(R.drawable.game_leaderboard_bg);
        }else if (model.getId().equalsIgnoreCase("3")){
            holder.txtTitle.setBackgroundResource(R.drawable.ic_promoter_leaderboard_lblbg);
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.layMain.setBackgroundResource(R.drawable.promoter_leaderboard_bg);
        }*/

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthPixels = displayMetrics.widthPixels;
        float heightPixels = (float) (widthPixels * 0.54);
        holder.imgItem.setMinimumHeight((int) heightPixels);

        Glide.with(context).load(model.getLbListImage()).into(holder.imgItem);

        holder.imgItem.setOnClickListener(view -> {
            listener.onClick(model);
        });
    }

    public interface LeaderboardClick{
        public void onClick(LeaderboardListModel model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgItem;
        private CardView cardMain;
       /* private TextView txtTitle;
        private LinearLayout layMain;*/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem=itemView.findViewById(R.id.imgItem);
            cardMain=itemView.findViewById(R.id.cardMain);

           // txtTitle=itemView.findViewById(R.id.txtTitle);
           // layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
