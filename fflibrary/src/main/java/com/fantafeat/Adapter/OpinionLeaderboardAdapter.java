package com.fantafeat.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.OpinionLeaderboardModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class OpinionLeaderboardAdapter extends RecyclerView.Adapter<OpinionLeaderboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OpinionLeaderboardModel> list;
    private boolean isLiveEvent;

    public OpinionLeaderboardAdapter(Context context, ArrayList<OpinionLeaderboardModel> list, boolean isLiveEvent) {
        this.context = context;
        this.list = list;
        this.isLiveEvent = isLiveEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.opinion_leaderboard_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpinionLeaderboardModel model=list.get(position);

        String rs=context.getResources().getString(R.string.rs);

        if (TextUtils.isEmpty(model.getUser_team_name())){
            holder.txtTeam.setText("NA");
        }else {
            holder.txtTeam.setText(model.getUser_team_name());
        }
        if (isLiveEvent){
            if (model.isMyOpinion()){
                holder.txtForecast.setVisibility(View.VISIBLE);
                String forecast="<font>Forecast: <b>"+model.getOptionValue()+"</b></font>";
                holder.txtForecast.setText(Html.fromHtml(forecast));
            }else
                holder.txtForecast.setVisibility(View.GONE);

            holder.txtWinning.setText("-");
            holder.txtRank.setText("-");

        }else {
            holder.txtForecast.setVisibility(View.VISIBLE);
            String forecast="<font>Forecast: <b>"+model.getOptionValue()+"</b></font>";
            holder.txtForecast.setText(Html.fromHtml(forecast));

            holder.txtWinning.setText(rs+model.getWinAmount());
            holder.txtRank.setText("#"+model.getTotalRank());
        }

        if (model.isMyOpinion()){
            holder.layMain.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryTransparentRegular));//skyblue
        }else {
            holder.layMain.setBackgroundColor(context.getResources().getColor(R.color.white_pure));
        }

    }

    public void updateList(ArrayList<OpinionLeaderboardModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTeam,txtForecast,txtWinning,txtRank;
        private LinearLayout layMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTeam=itemView.findViewById(R.id.txtTeam);
            txtForecast=itemView.findViewById(R.id.txtForecast);
            txtWinning=itemView.findViewById(R.id.txtWinning);
            txtRank=itemView.findViewById(R.id.txtRank);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
