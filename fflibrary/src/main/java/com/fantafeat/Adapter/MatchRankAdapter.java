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

public class MatchRankAdapter extends RecyclerView.Adapter<MatchRankAdapter.ApplicationViewHolder> {

    private Context mContext;
    private JSONArray matchRankArray;
    private static final String TAG = "MatchRankAdapter";

    public MatchRankAdapter(Context mContext, JSONArray matchRankArray) {
        this.mContext = mContext;
        this.matchRankArray = matchRankArray;
    }

    @NonNull
    @Override
    public MatchRankAdapter.ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_prize_pool, viewGroup, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationViewHolder holder, final int i) {

        JSONObject jsonObject = matchRankArray.optJSONObject(i);

        if ((i%2)==0){
            //even
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
        }else {
            //odd
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.lighetest_gray));
        }

        holder.match_rank_rang.setText("#  "+jsonObject.optString("rank_text"));
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
        LinearLayout main;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            match_rank_rang = (TextView) itemView.findViewById(R.id.txtRank);
            match_rank_price = (TextView) itemView.findViewById(R.id.txtPrize);
            main = itemView.findViewById(R.id.main);
            //rank_title = (TextView) itemView.findViewById(R.id.rank_title);

        }
    }
}