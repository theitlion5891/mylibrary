package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.UserRefLevelModel;
import com.fantafeat.R;

import java.util.ArrayList;


public class ReferLevelAdapter extends RecyclerView.Adapter<ReferLevelAdapter.ReferLevelHolder> {


    private Context mContext;
    private ArrayList<UserRefLevelModel> userRefLevels;

    public ReferLevelAdapter(Context mContext, ArrayList<UserRefLevelModel> userRefLevels) {
        this.mContext = mContext;
        this.userRefLevels = userRefLevels;
    }

    @NonNull
    @Override
    public ReferLevelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReferLevelHolder(LayoutInflater.from(mContext).inflate(R.layout.row_my_referrals, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReferLevelHolder holder, int position) {
        UserRefLevelModel List = userRefLevels.get(holder.getAdapterPosition());
        holder.refer_level_name.setText(List.getDisplayName());
        holder.refer_level_Tname.setText(List.getUser_team_name());
        holder.refer_level_no.setText("# "+(holder.getAdapterPosition()+1));

        if ((position%2)==0){
            //even
            holder.layMain.setBackgroundColor(mContext.getResources().getColor(R.color.white_pure));
        }else {
            //odd
            holder.layMain.setBackgroundColor(mContext.getResources().getColor(R.color.appBackGround));
        }

        /*if (CustomUtil.convertInt(List.getJoinedMatch()) > 0) {
            holder.refer_level_join.setImageResource(R.drawable.play);
        } else {
            holder.refer_level_join.setImageResource(R.drawable.nonplay);
        }*/
    }

    @Override
    public int getItemCount() {
        return userRefLevels.size();
    }

    protected class ReferLevelHolder extends RecyclerView.ViewHolder {
        TextView refer_level_name,refer_level_Tname,refer_level_no;
        ImageView refer_level_join;
        LinearLayout layMain;

        public ReferLevelHolder(@NonNull View itemView) {
            super(itemView);
            refer_level_name = (TextView) itemView.findViewById(R.id.refer_level_name);
            refer_level_join = (ImageView) itemView.findViewById(R.id.refer_level_join);
            refer_level_Tname = itemView.findViewById(R.id.refer_level_Tname);
            refer_level_no = itemView.findViewById(R.id.refer_level_no);
            layMain = itemView.findViewById(R.id.layMain);
        }
    }

    public void updateData(ArrayList<UserRefLevelModel> userRefLevels){
        this.userRefLevels = userRefLevels;
        notifyDataSetChanged();
    }
}
