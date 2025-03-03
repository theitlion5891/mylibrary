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

import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class PromotorPoolAdapter extends RecyclerView.Adapter<PromotorPoolAdapter.ViewHolder> {

    private Context mContext;
    private JSONArray matchRankArray;
    private static final String TAG = "MatchRankAdapter";

    public PromotorPoolAdapter(Context mContext, JSONArray matchRankArray) {
        this.mContext = mContext;
        this.matchRankArray = matchRankArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.promotors_prise_pool_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        JSONObject jsonObject = matchRankArray.optJSONObject(holder.getAdapterPosition());

        if ((i%2)==0){
            //even
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
           // holder.imgWinGadgets.setVisibility(View.GONE);
           // holder.match_rank_price.setVisibility(View.VISIBLE);
        }else {
            //odd
            holder.main.setBackgroundColor(mContext.getResources().getColor(R.color.lighetest_gray));
           // holder.imgWinGadgets.setVisibility(View.VISIBLE);
            //holder.match_rank_price.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(jsonObject.optString("asset_url")) && !jsonObject.optString("asset_url").equalsIgnoreCase("null")){
            holder.imgWinGadgets.setVisibility(View.VISIBLE);
            holder.match_rank_price.setVisibility(View.GONE);
            CustomUtil.loadImageWithGlide(mContext,holder.imgWinGadgets, ApiManager.PROFILE_IMG,jsonObject.optString("asset_url"),R.drawable.ic_team1_placeholder);
        }else {
            holder.imgWinGadgets.setVisibility(View.GONE);
            holder.match_rank_price.setVisibility(View.VISIBLE);
            holder.match_rank_price.setText(CustomUtil.displayAmount( mContext, jsonObject.optString("amount")));
        }

        holder.match_rank_rang.setText("#  "+jsonObject.optString("rank_text"));


    }

    @Override
    public int getItemCount() {
        return matchRankArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView match_rank_rang, match_rank_price, rank_title;
        LinearLayout main;
        ImageView imgWinGadgets;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            match_rank_rang = (TextView) itemView.findViewById(R.id.txtRank);
            match_rank_price = (TextView) itemView.findViewById(R.id.txtPrize);
            imgWinGadgets = itemView.findViewById(R.id.imgWinGadgets);
            main = itemView.findViewById(R.id.main);
            //rank_title = (TextView) itemView.findViewById(R.id.rank_title);

        }
    }
}
