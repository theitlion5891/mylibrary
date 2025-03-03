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
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.HeaderItemDecoration;

import java.util.ArrayList;

public class LineupSelectMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface {

    private Context context;
    private ArrayList<LineupMainModel> list;
    private String spId;
    private MyPreferences preferences;
    private boolean isPoint;
    private PlayerClick listener;
    private Fragment fragment;
    private MatchModel matchModel;

    public LineupSelectMainAdapter(Context context, ArrayList<LineupMainModel> list,boolean isPoint,PlayerClick listener, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.isPoint = isPoint;
        this.listener = listener;
        this.fragment = fragment;
        preferences = MyApp.getMyPreferences();
        matchModel=preferences.getMatchModel();
        spId = preferences.getMatchModel().getSportId();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public void updatePoint(boolean isPoint){
        this.isPoint=isPoint;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.lineup_xi_header_item, parent, false));
        else if (viewType==2)
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.lineup_sub_header_item, parent, false));
        else if (viewType==3) {
            if (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes")) {
                return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.lineup_notxi_header_item, parent, false));
            }else {
                return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.lineup_other_player_header_item, parent, false));
            }
        }
       /* else if (viewType==3)
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.lineup_notxi_header_item, parent, false));*/
        else
            return new ChildViewHolder(LayoutInflater.from(context).inflate(R.layout.lineup_select_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holdr, int position) {

        LineupMainModel model=list.get(position);

        switch (holdr.getItemViewType()) {
            case 1:
                HeaderHolder holder = (HeaderHolder) holdr;

                if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                        preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
                    holder.layAnnounce.setVisibility(View.VISIBLE);
                }else {
                    holder.layAnnounce.setVisibility(View.GONE);
                }

            break;
            case 4:
                ChildViewHolder holder1 = (ChildViewHolder) holdr;

                holder1.recyclerTeam1.setLayoutManager(new LinearLayoutManager(context));
                holder1.recyclerTeam2.setLayoutManager(new LinearLayoutManager(context));

                LineupTeam1Adapter adapter1=new LineupTeam1Adapter(context, model.getPlayerModelList1(), spId, isPoint, playerModel -> {
                    //listener.onClick(playerModel);
                },fragment);
                holder1.recyclerTeam1.setAdapter(adapter1);

                LineupTeam2Adapter adapter2=new LineupTeam2Adapter(context,model.getPlayerModelList2(),spId,isPoint,playerModel -> {
                    //listener.onClick(playerModel);
                },fragment);
                holder1.recyclerTeam2.setAdapter(adapter2);
            break;
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
        if (list.get(headerPosition).getType() == 1)
            return R.layout.lineup_xi_header_item;
        else if (list.get(headerPosition).getType() == 2)
            return R.layout.lineup_sub_header_item;
        else{
            if (matchModel.getTeamAXi().equalsIgnoreCase("yes") ||
                    matchModel.getTeamBXi().equalsIgnoreCase("yes")) {
                return R.layout.lineup_notxi_header_item;
            }else {
                return R.layout.lineup_other_player_header_item;
            }
        }
            //return R.layout.lineup_notxi_header_item;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        TextView txtAnnounce=header.findViewById(R.id.txtAnnounce);
        txtAnnounce.setText(list.get(headerPosition).getTitle());
    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (list.get(itemPosition).getType() == 1 || list.get(itemPosition).getType() == 2 || list.get(itemPosition).getType() == 3)
            return true;
        else
            return false;
    }

    public interface PlayerClick{
        public void onClick(PlayerModel playerModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
        private RecyclerView recyclerTeam1,recyclerTeam2;


        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerTeam1=itemView.findViewById(R.id.recyclerTeam1);
            recyclerTeam2=itemView.findViewById(R.id.recyclerTeam2);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
