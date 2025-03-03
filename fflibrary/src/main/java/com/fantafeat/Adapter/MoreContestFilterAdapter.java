package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.ContestModel;
import com.fantafeat.R;

import java.util.List;

public class MoreContestFilterAdapter extends RecyclerView.Adapter<MoreContestFilterAdapter.ViewHolder>  {
    List<ContestModel> contestModelList;
    Context context;
    onItemClick listener;

    public MoreContestFilterAdapter(List<ContestModel> contestModelList, Context context, onItemClick listener) {
        this.contestModelList = contestModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chip_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContestModel model=contestModelList.get(position);
        if (!TextUtils.isEmpty(model.getTitle())){
            holder.txtTitle.setText(model.getTitle());

            holder.txtTitle.setOnClickListener(v->{
                listener.onClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return contestModelList.size();
    }

    public interface onItemClick{
       public void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.txtTitle);
        }
    }
}
