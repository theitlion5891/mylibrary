package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class TournamentMatchRankAdapter extends RecyclerView.Adapter<TournamentMatchRankAdapter.ApplicationViewHolder> {

    private Context mContext;
    private JSONArray matchRankArray;
    private static final String TAG = "MatchRankAdapter";

    public TournamentMatchRankAdapter(Context mContext, JSONArray matchRankArray) {
        this.mContext = mContext;
        this.matchRankArray = matchRankArray;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_prize_pool, viewGroup, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationViewHolder holder, final int i) {

        JSONObject jsonObject = matchRankArray.optJSONObject(i);

        if ((i%2)==0){
            //even
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.normal_bg));
        }else {
            //odd
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.dark_red));
        }

        holder.match_rank_rang.setText("#  "+jsonObject.optString("rank_text"));
        holder.match_rank_rang.setTextColor(mContext.getResources().getColor(R.color.white_pure));
        holder.match_rank_price.setTextColor(mContext.getResources().getColor(R.color.white_pure));
        holder.match_rank_price.setText(CustomUtil.displayAmountFloat( mContext, jsonObject.optString("amount")));

    }

    public void updateArray(JSONArray arr){
        this.matchRankArray=arr;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return matchRankArray.length();
    }

    public class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView match_rank_rang, match_rank_price, rank_title;
        LinearLayout main,layBorder;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            match_rank_rang = (TextView) itemView.findViewById(R.id.txtRank);
            match_rank_price = (TextView) itemView.findViewById(R.id.txtPrize);
            main = itemView.findViewById(R.id.main);
            layBorder = itemView.findViewById(R.id.layBorder);
            layBorder.setVisibility(View.GONE);
            //rank_title = (TextView) itemView.findViewById(R.id.rank_title);

        }
    }
}