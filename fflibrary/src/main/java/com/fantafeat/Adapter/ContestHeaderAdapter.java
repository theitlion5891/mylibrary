package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.fantafeat.Activity.CustomMoreContestListActivity;
import com.fantafeat.Activity.MoreContestListActivity;
import com.fantafeat.Activity.TOPActivity;
import com.fantafeat.Fragment.ContestListInnerFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContestHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ContestModel> contestModelList;
    private ContestListAdapter contestListAdapter;
    private Fragment fragment;
    private Gson gson;

    public ContestHeaderAdapter(Context mContext, List<ContestModel> contestModelList,Fragment fragment, Gson gson){
        this.mContext = mContext;
        this.gson = gson;
        this.contestModelList = contestModelList;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return contestModelList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1){
            return new ViewAllHolder(LayoutInflater.from(mContext).inflate(R.layout.view_all_contest_item,parent,false));
        }else
            return new ContestHeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contest_header,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ContestModel List = contestModelList.get(position);

        if (holder1 instanceof ContestHeaderHolder){
            ContestHeaderHolder holder = (ContestHeaderHolder) holder1;

            holder.contest_title.setText(List.getTitle());
            holder.contest_sub_title.setText(List.getSubTitle());

            if (TextUtils.isEmpty(List.getImage())){
                holder.imgHeader.setVisibility(View.GONE);
            }
            else {
                if (List.getImage().equalsIgnoreCase("fav")){
                    holder.imgHeader.setImageResource(R.drawable.ic_favorite_contest);
                }else if (List.getImage().equalsIgnoreCase("top")){
                    holder.imgHeader.setImageResource(R.drawable.ic_trade_x);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,holder.imgHeader,ApiManager.DOCUMENTS,List.getImage(),R.drawable.ic_team1_placeholder);
                }
            }

            if (!TextUtils.isEmpty(List.getIs_view_all()) && List.getIs_view_all().equalsIgnoreCase("yes")){
                holder.txtViewAll.setVisibility(View.VISIBLE);
            }else {
                holder.txtViewAll.setVisibility(View.GONE);
            }

            holder.txtViewAll.setOnClickListener(view -> {
                if (List.getImage().equalsIgnoreCase("top")){
                    mContext.startActivity(new Intent(mContext, TOPActivity.class));
                }else {
                    if (ConstantUtil.isCustomMore){
                        mContext.startActivity(new Intent(mContext, CustomMoreContestListActivity.class)
                                .putExtra("league_id",List.getId()));
                    }else
                        mContext.startActivity(new Intent(mContext, MoreContestListActivity.class)
                            .putExtra("league_id",List.getId()));
                }
            });

            if (List.getImage().equalsIgnoreCase("top")){
                if (List.getListTop().size()<=0){
                    holder.layHeader.setVisibility(View.GONE);
                    holder.contest_sub_list.setVisibility(View.GONE);
                }else {
                    holder.layHeader.setVisibility(View.VISIBLE);
                    TradingContestAdapter tradingContestAdapter=new TradingContestAdapter(mContext, List.getListTop(), "Trading", item -> {
                        //listener.onTopClick(item);
                        ((ContestListInnerFragment)fragment).getOpinionCnt(item);
                    });
                    holder.contest_sub_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    holder.contest_sub_list.setAdapter(tradingContestAdapter);
                }

            }else {

                if(List.getTitle().equalsIgnoreCase("Grand Leagues") ||
                        List.getTitle().equalsIgnoreCase("Bonus League")){
                    Collections.sort(List.getContestData(), new prizePoolUp());
                }else{
                    Collections.sort(List.getContestData(), new EntryFeeDown());
                }

                contestListAdapter = new ContestListAdapter(mContext,List.getContestData(), gson,position,false);
                holder.contest_sub_list.setLayoutManager(new LinearLayoutManager(mContext));
                //holder.contest_sub_list.setItemAnimator(null);
                holder.contest_sub_list.setAdapter(contestListAdapter);
            }
        }
        else {
            ViewAllHolder holder= (ViewAllHolder) holder1;
            holder.btnViewAll.setOnClickListener(view -> {
                if (ConstantUtil.isCustomMore){
                    mContext.startActivity(new Intent(mContext, CustomMoreContestListActivity.class)
                            .putExtra("league_id","0"));
                }else
                    mContext.startActivity(new Intent(mContext, MoreContestListActivity.class)
                            .putExtra("league_id","0"));

            });
        }



    }

    public void updateChild(int mainPorition,ContestModel.ContestDatum contest){
        try {
            int childPos=0;
            if (mainPorition>=0){
                for (int i=0;i<contestModelList.get(mainPorition).getContestData().size();i++){
                    ContestModel.ContestDatum contestDatum=contestModelList.get(mainPorition).getContestData().get(i);
                    if (contest.getId().equalsIgnoreCase(contestDatum.getId())){
                        childPos=i;
                    }
                }
                notifyDataSetChanged();
                if (contestListAdapter!=null){
                    contestListAdapter.updateChild(childPos,contest);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

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
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(contest_sub_list);
            imgHeader = itemView.findViewById(R.id.imgHeader);
            txtViewAll = itemView.findViewById(R.id.txtViewAll);
            layHeader = itemView.findViewById(R.id.layHeader);
        }
    }

    public class ViewAllHolder extends RecyclerView.ViewHolder{
        TextView btnViewAll;
        public ViewAllHolder(@NonNull View itemView) {
            super(itemView);
            btnViewAll=itemView.findViewById(R.id.btnViewAll);
        }
    }

    public void updateList(List<ContestModel> contestModelList){
        this.contestModelList = contestModelList;
        notifyDataSetChanged();
    }

    public class prizePoolUp implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare(CustomUtil.convertFloat(o2.getDistributeAmount()), CustomUtil.convertFloat(o1.getDistributeAmount()));
        }
    }

    public class EntryFeeDown implements Comparator<ContestModel.ContestDatum> {
        @Override
        public int compare(ContestModel.ContestDatum o1, ContestModel.ContestDatum o2) {
            return Float.compare( CustomUtil.convertFloat(o1.getEntryFee()),CustomUtil.convertFloat(o2.getEntryFee()));
        }
    }

}
