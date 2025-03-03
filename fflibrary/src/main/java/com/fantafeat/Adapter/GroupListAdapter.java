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

import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroupModel> list;
    LayoutInflater layoutInflater;
    private MatchModel matchModel;
    private onGroupClick onGroupClick;

    public GroupListAdapter(Context context, ArrayList<GroupModel> list,MatchModel matchModel,onGroupClick onGroupClick) {
        this.context = context;
        this.list = list;
        this.matchModel = matchModel;
        this.onGroupClick = onGroupClick;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupModel item=list.get(position);

        holder.txtGrpName.setText(item.getGrpName());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        holder.layPlayers.removeAllViews();

        if (item.getType().equalsIgnoreCase("2")){
            if (TextUtils.isEmpty(item.getFilling_fast_text())){
                holder.txtGrpLbl.setVisibility(View.GONE);
            }else {
                holder.txtGrpLbl.setVisibility(View.VISIBLE);
                holder.txtGrpLbl.setText("◉ "+item.getFilling_fast_text());
            }
        }else {
            if (TextUtils.isEmpty(item.getDisplay_amt())){
                holder.txtGrpLbl.setVisibility(View.GONE);
            }else {
                holder.txtGrpLbl.setVisibility(View.VISIBLE);
                holder.txtGrpLbl.setText("Entry Fee: "+context.getResources().getString(R.string.rs)+item.getDisplay_amt());
                holder.txtGrpLbl.setTextColor(context.getResources().getColor(R.color.blackPrimary));
            }
        }


        for (int i=0;i<item.getPlayersData().size();i++){
            GroupModel.PlayersDatum player=item.getPlayersData().get(i);
            final View addView = layoutInflater.inflate(R.layout.group_player_item, null);

            addView.setLayoutParams(param);

            TextView playerName =  addView.findViewById(R.id.player_name);
            TextView player_type = addView.findViewById(R.id.player_type);
            ImageView player_img = addView.findViewById(R.id.player_img);
            ImageView inPlaying = addView.findViewById(R.id.inPlaying);

            CustomUtil.loadImageWithGlide(context,player_img, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.ic_team1_tshirt);

            playerName.setText(player.getPlayerSname());/*.replace(" ","\n")*/
            player_type.setText(player.getPlayerType());
            if (matchModel.getTeamAXi().equalsIgnoreCase("Yes") || matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
                if (!player.getPlayingXi().equalsIgnoreCase("Yes")) {
                    inPlaying.setVisibility(View.VISIBLE);
                } else {
                    inPlaying.setVisibility(View.GONE);
                }
            }

            addView.setOnClickListener(view -> {
                player.setChecked(true);
                onGroupClick.onClick(item);

            });

            holder.layPlayers.addView(addView);
        }

        holder.txtGrpName.setOnClickListener(view -> {
            onGroupClick.onClick(item);
        });

    }

    public interface onGroupClick{
        public void onClick(GroupModel model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layPlayers;
        private TextView txtGrpName,txtGrpLbl;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layPlayers=itemView.findViewById(R.id.layPlayers);
            txtGrpName=itemView.findViewById(R.id.txtGrpName);
            txtGrpLbl=itemView.findViewById(R.id.txtGrpLbl);
        }
    }
}
