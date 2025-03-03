package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeamBreakupAdapter extends RecyclerView.Adapter<TeamBreakupAdapter.ViewHolder> {

    private Context context;
    private JSONArray teamArray;

    public TeamBreakupAdapter(Context context, JSONArray teamArray) {
        this.context = context;
        this.teamArray = teamArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.team_breakup_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            JSONObject obj=teamArray.optJSONObject(position);

            float total= (CustomUtil.convertFloat(obj.optString("win_bal"))+
                    CustomUtil.convertFloat(obj.optString("deposit_bal"))+
                    CustomUtil.convertFloat(obj.optString("bonus_bal"))+
                    CustomUtil.convertFloat(obj.optString("transfer_bal")));

            String rs=context.getResources().getString(R.string.rs);

            if (CustomUtil.convertFloat(obj.optString("win_bal"))>0){
                holder.join_use_winning.setTextColor(context.getResources().getColor(R.color.green_pure));
            }else {
                holder.join_use_winning.setTextColor(context.getResources().getColor(R.color.textPrimary));
            }

            if (CustomUtil.convertFloat(obj.optString("deposit_bal"))>0){
                holder.join_use_deposit.setTextColor(context.getResources().getColor(R.color.green_pure));
            }else {
                holder.join_use_deposit.setTextColor(context.getResources().getColor(R.color.textPrimary));
            }

            if (CustomUtil.convertFloat(obj.optString("bonus_bal"))>0){
                holder.join_use_rewards.setTextColor(context.getResources().getColor(R.color.green_pure));
            }else {
                holder.join_use_rewards.setTextColor(context.getResources().getColor(R.color.textPrimary));
            }

           /* if (CustomUtil.convertFloat(obj.optString("transfer_bal"))>0){
                holder.join_use_borrowed.setTextColor(context.getResources().getColor(R.color.green));
            }else {
                holder.join_use_borrowed.setTextColor(context.getResources().getColor(R.color.textPrimary));
            }*/

            holder.join_use_winning.setText(rs+obj.optString("win_bal"));
            holder.join_use_deposit.setText(rs+obj.optString("deposit_bal"));
            holder.join_use_rewards.setText(rs+obj.optString("bonus_bal"));
            //holder.join_use_borrowed.setText(rs+obj.optString("transfer_bal"));
            holder.join_contest_fee.setText(rs+total);
            holder.txtTeamName.setText("#"+obj.optString("team_name"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return teamArray.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView join_contest_fee,join_use_deposit,join_use_borrowed,join_use_rewards,join_use_winning,txtTeamName;

        public ViewHolder(@NonNull View view) {
            super(view);

            join_contest_fee = (TextView) view.findViewById(R.id.join_contest_fee);
            join_use_deposit = (TextView) view.findViewById(R.id.join_use_deposit);
            join_use_borrowed = (TextView) view.findViewById(R.id.join_use_borrowed);
            join_use_rewards = (TextView) view.findViewById(R.id.join_use_rewards);
            join_use_winning = (TextView) view.findViewById(R.id.join_use_winning);
            txtTeamName = (TextView) view.findViewById(R.id.txtTeamName);
        }
    }

}
