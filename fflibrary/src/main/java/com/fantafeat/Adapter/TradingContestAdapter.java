package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Model.EventModel;
import com.fantafeat.R;

import java.util.ArrayList;

public class TradingContestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<EventModel> list;
    private String tag;
    private OpinionOptionClick listener;

    public TradingContestAdapter(Context context, ArrayList<EventModel> list, String tag, OpinionOptionClick listener) {
        this.context = context;
        this.list = list;
        this.tag = tag;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TradingViewHolder(LayoutInflater.from(context).inflate(R.layout.trading_contest_item,parent,false));
    }

    public interface OpinionOptionClick{
        public void onItemClick(EventModel item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder ho, int position) {
        EventModel model=list.get(position);

        if (tag.equalsIgnoreCase("Trading")){
            TradingViewHolder holder=(TradingViewHolder)ho;

            holder.txtConTitle.setText(model.getConDesc());

            String rs=context.getResources().getString(R.string.rs);

            holder.txtYes.setText(model.getOption1() + " | "+rs+model.getOption1Price());
            holder.txtNo.setText(model.getOption2() + " | "+rs+model.getOption2Price());
            holder.txtTrades.setText(model.getTotalTrades()+" Trades");

            holder.txtYes.setOnClickListener(view -> {
                model.setOptionValue(model.getOption1());
                model.setOptionEntryFee(model.getOption1Price());

                listener.onItemClick(model);
            });

            holder.txtNo.setOnClickListener(view -> {
                model.setOptionValue(model.getOption2());
                model.setOptionEntryFee(model.getOption2Price());
                listener.onItemClick(model);
            });
        /*holder.layMain.setOnClickListener(view -> {
            context.startActivity(new Intent(context, UpcomingEventDetailsActivity.class)
                    .putExtra("eventModel",model)
                    .putExtra("selectedTag","1"));
        });*/
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TradingViewHolder extends RecyclerView.ViewHolder{
        private TextView txtYes,txtNo,txtConTitle,txtTrades;
        private LinearLayout layMain;
        public TradingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtYes=itemView.findViewById(R.id.txtYes);
            txtNo=itemView.findViewById(R.id.txtNo);
            txtConTitle=itemView.findViewById(R.id.txtConTitle);
            txtTrades=itemView.findViewById(R.id.txtTrades);
            layMain=itemView.findViewById(R.id.layMain);
        }
    }
}
