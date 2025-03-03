package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.LineupMainModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.HeaderItemDecoration;

import java.util.List;

public class SelectPlayerMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface {

    private Context mContext;
    private List<LineupMainModel> playerModelList;
    private Fragment fragment;
    private String sportId;
    private MyPreferences prefreance;

    public SelectPlayerMainAdapter(Context mContext, List<LineupMainModel> playerModelList, Fragment fragment, String sportId) {
        this.mContext = mContext;
        this.playerModelList = playerModelList;
        this.fragment = fragment;
        this.sportId = sportId;
        prefreance= MyApp.getMyPreferences();
    }

    public void updateList(List<LineupMainModel> playerModelList) {
        this.playerModelList = playerModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return playerModelList.get(position).getType();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.lineup_xi_header_item, parent, false));
        else if (viewType==2)
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.lineup_sub_header_item, parent, false));
        else if (viewType==3)
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.lineup_notxi_header_item, parent, false));
        else
            return new ChildViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_player_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holdr, int position) {
        LineupMainModel model=playerModelList.get(position);

        if (holdr instanceof HeaderHolder){
            HeaderHolder holder = (HeaderHolder) holdr;

            if (prefreance.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                    prefreance.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
                if (model.getXiType()==0){
                    holder.layAnnounce.setVisibility(View.GONE);
                }else {
                    holder.layAnnounce.setVisibility(View.VISIBLE);
                }
            }
            else {
                holder.layAnnounce.setVisibility(View.GONE);
            }
        }else {
            ChildViewHolder holder1 = (ChildViewHolder) holdr;

            SelectPlayerAdapter selectPlayerAdapter = new SelectPlayerAdapter(model.getPlayerModelList1(),mContext,fragment,prefreance);
            holder1.recyclerTeam.setLayoutManager(new LinearLayoutManager(mContext));
            holder1.recyclerTeam.setAdapter(selectPlayerAdapter);
        }

    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        if (playerModelList.get(headerPosition).getType() == 1)
            if (playerModelList.get(headerPosition).getXiType()==0)
                return -1;
            else
                return R.layout.lineup_xi_header_item;

        else if (playerModelList.get(headerPosition).getType() == 2)
            if (playerModelList.get(headerPosition).getXiType()==0)
                return -1;
            else
                return R.layout.lineup_sub_header_item;

        else if (playerModelList.get(headerPosition).getType() == 3)
            if (playerModelList.get(headerPosition).getXiType()==0)
                return -1;
            else
                return R.layout.lineup_notxi_header_item;

        else
            return -1;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {

    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (prefreance.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                prefreance.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
            return playerModelList.get(itemPosition).getType() == 1 ||
                    playerModelList.get(itemPosition).getType() == 2 ||
                    playerModelList.get(itemPosition).getType() == 3;
        }else
            return false;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return playerModelList.size();
    }

    static class HeaderHolder extends RecyclerView.ViewHolder{

        private RelativeLayout layAnnounce;
        private ImageView imgAnnounceLine,imgAnnounceBottom;
        private TextView txtAnnounce;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            layAnnounce=itemView.findViewById(R.id.layAnnounce);
            imgAnnounceLine=itemView.findViewById(R.id.imgAnnounceLine);
            imgAnnounceBottom=itemView.findViewById(R.id.imgAnnounceBottom);
            txtAnnounce=itemView.findViewById(R.id.txtAnnounce);
        }

    }
    static class ChildViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout layMain;
        private RecyclerView recyclerTeam;


        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerTeam=itemView.findViewById(R.id.recyclerTeam);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }

}

