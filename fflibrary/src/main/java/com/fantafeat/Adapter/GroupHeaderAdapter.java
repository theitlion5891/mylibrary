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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Fragment.FiferContestListFragment;
import com.fantafeat.Fragment.GroupContestListFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupHeaderAdapter extends RecyclerView.Adapter<GroupHeaderAdapter.ContestHeaderHolder> {

    private Context mContext;
    private List<GroupContestModel> contestModelList;
    private Fragment fragment;
    private Gson gson;
    private boolean is_match_after;

    public GroupHeaderAdapter(Context mContext, List<GroupContestModel> contestModelList, Fragment fragment, Gson gson,boolean is_match_after) {
        this.mContext = mContext;
        this.contestModelList = contestModelList;
        this.fragment = fragment;
        this.gson = gson;
        this.is_match_after = is_match_after;
    }

    @NonNull
    @Override
    public ContestHeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestHeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contest_header,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContestHeaderHolder holder, int position) {
        GroupContestModel List = contestModelList.get(holder.getAdapterPosition());

        holder.contest_title.setText(List.getTitle());
        holder.contest_sub_title.setText(List.getSubTitle());

        if(List.getTitle().equalsIgnoreCase("Grand Leagues")){
            Collections.sort(List.getContestData(), new prizePoolUp());
        }else if(List.getTitle().equalsIgnoreCase("Bonus League")){
            Collections.sort(List.getContestData(), new prizePoolUp());
        }else{
            Collections.sort(List.getContestData(), new EntryFeeDown());
        }

        if (TextUtils.isEmpty(List.getImage())){
            holder.imgHeader.setVisibility(View.GONE);
        }
        else {
            if (List.getImage().equalsIgnoreCase("fav")){
                holder.imgHeader.setImageResource(R.drawable.ic_favorite_contest);
            }else {
                CustomUtil.loadImageWithGlide(mContext,holder.imgHeader, ApiManager.DOCUMENTS,List.getImage(),R.drawable.ic_team1_placeholder);
            }

           /* Glide.with(mContext)
                    .load(ApiManager.DOCUMENTS + List.getImage())
                    .placeholder(R.drawable.ic_team1_placeholder)
                    .into(holder.imgHeader);*/
        }

        //if (fragment instanceof GroupContestListFragment){

            GroupContestListAdapter contestListAdapter = new GroupContestListAdapter(mContext,List.getContestData(), gson,position,false,is_match_after);
            holder.contest_sub_list.setLayoutManager(new LinearLayoutManager(mContext));
            holder.contest_sub_list.setItemAnimator(null);
            holder.contest_sub_list.setAdapter(contestListAdapter);
       /* }else {

            FiferContestAdapter contestListAdapter = new FiferContestAdapter(mContext,List.getContestData(), gson,position,false);
            holder.contest_sub_list.setLayoutManager(new LinearLayoutManager(mContext));
            holder.contest_sub_list.setItemAnimator(null);
            holder.contest_sub_list.setAdapter(contestListAdapter);
        }*/


    }



    @Override
    public int getItemCount() {
        return contestModelList.size();
    }


    public class ContestHeaderHolder extends RecyclerView.ViewHolder {
        TextView contest_title,contest_sub_title,txtViewAll;
        RecyclerView contest_sub_list;
        ImageView imgHeader;
        LinearLayout layHeader;

        public ContestHeaderHolder(@NonNull View itemView) {
            super(itemView);

            contest_title = itemView.findViewById(R.id.contest_title);
            contest_sub_title = itemView.findViewById(R.id.contest_sub_title);
            contest_sub_list = itemView.findViewById(R.id.contest_sub_list);
            imgHeader = itemView.findViewById(R.id.imgHeader);
            layHeader = itemView.findViewById(R.id.layHeader);
            txtViewAll = itemView.findViewById(R.id.txtViewAll);
            txtViewAll.setVisibility(View.GONE);

            if (fragment instanceof GroupContestListFragment){
                layHeader.setVisibility(View.VISIBLE);
            }else {
                layHeader.setVisibility(View.GONE);
            }
        }
    }

    public void updateList(List<GroupContestModel> contestModelList){
        this.contestModelList = contestModelList;
        notifyDataSetChanged();
    }

    public class prizePoolUp implements Comparator<GroupContestModel.ContestDatum> {
        @Override
        public int compare(GroupContestModel.ContestDatum o1, GroupContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getDistributeAmount()), CustomUtil.convertFloat(o1.getDistributeAmount()));
        }
    }

    public class EntryFeeDown implements Comparator<GroupContestModel.ContestDatum> {
        @Override
        public int compare(GroupContestModel.ContestDatum o1, GroupContestModel.ContestDatum o2) {
            return Float.compare( CustomUtil.convertFloat(o1.getEntryFee()),CustomUtil.convertFloat(o2.getEntryFee()));
        }
    }
}
