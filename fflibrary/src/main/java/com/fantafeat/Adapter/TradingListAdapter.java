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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.fantafeat.Activity.UpcomingEventDetailsActivity;
import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.google.android.material.transition.Hold;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TradingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EventModel> list;
    private Context context;
    private OpinionOptionClick listener;

    public TradingListAdapter(ArrayList<EventModel> list, Context context, OpinionOptionClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0)
            return new Headerholder(LayoutInflater.from(context).inflate(R.layout.recyclerview_tradeboard_item,parent,false));
        else
            return new ItemTradeHolder(LayoutInflater.from(context).inflate(R.layout.trading_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventModel model=list.get(position);
        if (holder.getItemViewType()==0){
            Headerholder holder2 = (Headerholder) holder;

            LogUtil.e("resp","confirmTradeList onBindViewHolder : "+model.getConfirmTradeList().size());
            TradeBoardAdapter tradeBoardAdapter = new TradeBoardAdapter(context,model.getConfirmTradeList(),model1 -> {
                listener.onItemClick(model1);
            },"upcoming");
            holder2.recyclerTradeBoard.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            holder2.recyclerTradeBoard.setAdapter(tradeBoardAdapter);
        }
        else {
            ItemTradeHolder holder1 = (ItemTradeHolder) holder;
            try {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date1 = input.parse(model.getConStartTime());
                Date date2 = new Date();

                long different = date2.getTime() - date1.getTime();
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = different / hoursInMilli;

                if (elapsedHours<=1){
                    holder1.txtNewBadge.setVisibility(View.VISIBLE);
                    model.setNewBadge(true);
                }else {
                    holder1.txtNewBadge.setVisibility(View.GONE);
                    model.setNewBadge(false);
                }
                // obj.printDifference(date1, date2);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder1.txtEndDate.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));

            holder1.txtConTitle.setText(model.getConDesc());
            if (TextUtils.isEmpty(model.getConSubDesc())){
                holder1.layDesc.setVisibility(View.GONE);
            }else {
                holder1.layDesc.setVisibility(View.VISIBLE);
                holder1.txtDesc.setText(model.getConSubDesc());
            }

            String rs=context.getResources().getString(R.string.rs);

            if (!TextUtils.isEmpty(model.getConImage())){
                CustomUtil.loadImageWithGlide(context,holder1.imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
            }else
                holder1.imgConImage.setImageResource(R.drawable.event_placeholder);

            holder1.txtYes.setText(model.getOption1() + " | "+rs+model.getOption1Price());
            holder1.txtNo.setText(model.getOption2() + " | "+rs+model.getOption2Price());
            holder1.txtTrades.setText(model.getTotalTrades()+" Trades");

            holder1.txtYes.setOnClickListener(view -> {
                model.setOptionValue(model.getOption1());
                model.setOptionEntryFee(model.getOption1Price());
                listener.onItemClick(model);
            });

            holder1.txtNo.setOnClickListener(view -> {
                model.setOptionValue(model.getOption2());
                model.setOptionEntryFee(model.getOption2Price());
                listener.onItemClick(model);
            });

            holder1.layMain.setOnClickListener(view -> {
                context.startActivity(new Intent(context, UpcomingEventDetailsActivity.class)
                        .putExtra("eventModel",model)
                        .putExtra("selectedTag","1"));
            });
        }

    }

    public void updateData(ArrayList<EventModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public ArrayList<EventModel> getList(){
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OpinionOptionClick{
        public void onItemClick(EventModel item);
    }

    class Headerholder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerTradeBoard;
        public Headerholder(@NonNull View itemView) {
            super(itemView);
            recyclerTradeBoard = itemView.findViewById(R.id.recyclerTradeBoard);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerTradeBoard);
        }
    }
    class ItemTradeHolder extends RecyclerView.ViewHolder{

        private TextView txtYes,txtNo,txtEndDate,txtConTitle,txtDesc,txtTrades,txtNewBadge;
        private ImageView imgConImage;
        private LinearLayout layDesc,layMain;

        public ItemTradeHolder(@NonNull View itemView) {
            super(itemView);
            txtYes=itemView.findViewById(R.id.txtYes);
            txtNo=itemView.findViewById(R.id.txtNo);
            txtEndDate=itemView.findViewById(R.id.txtEndDate);
            imgConImage=itemView.findViewById(R.id.imgConImage);
            txtConTitle=itemView.findViewById(R.id.txtConTitle);
            txtDesc=itemView.findViewById(R.id.txtDesc);
            txtDesc.setSelected(true);
            layDesc=itemView.findViewById(R.id.layDesc);
            txtTrades=itemView.findViewById(R.id.txtTrades);
            layMain=itemView.findViewById(R.id.layMain);
            txtNewBadge=itemView.findViewById(R.id.txtNewBadge);
        }
    }
}
