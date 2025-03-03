package com.fantafeat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;

import java.util.List;

public class StateTeamFilterAdapter extends RecyclerView.Adapter<StateTeamFilterAdapter.ViewHolder> {

    private Context context;
    private List<PlayerListModel> list;
    private FilterItemListener listener;

    public StateTeamFilterAdapter(Context context, List<PlayerListModel> list,FilterItemListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.state_team_filter_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerListModel model=list.get(position);

        holder.txtTitle.setText("T"+(position+1)+"\n"+model.getTotal_points());

        if (model.isChecked()){
            holder.txtTitle.setBackgroundResource(R.drawable.red_bg_fifer);
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.white_pure));
        }else {
            holder.txtTitle.setBackgroundResource(R.drawable.white_fill_curve);
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.black_pure));
        }

        holder.txtTitle.setOnClickListener(v -> {
            for (PlayerListModel playerListModel:list) {
                playerListModel.setChecked(false);
            }
            model.setChecked(true);
            notifyDataSetChanged();
            listener.onItemClick(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface FilterItemListener{
        public void onItemClick(PlayerListModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);
        }
    }
}
