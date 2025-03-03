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
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.PollDetailsActivity;
import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder> {

    private ArrayList<EventModel> list;
    private Context context;
    private PredictionItemClick listener;

    public PollAdapter(ArrayList<EventModel> list, Context context, PredictionItemClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.poll_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel model=list.get(position);

        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date date1 = input.parse(model.getConStartTime());
            Date date2 = new Date();

            long different = date2.getTime() - date1.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;

            long elapsedHours = different / hoursInMilli;

            if (elapsedHours<=1){
                holder.txtNewBadge.setVisibility(View.VISIBLE);
                model.setNewBadge(true);
            }else {
                holder.txtNewBadge.setVisibility(View.GONE);
                model.setNewBadge(false);
            }
            // obj.printDifference(date1, date2);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtConEnd.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getConEndTime(),"MMM dd hh:mm aa"));
        holder.txtConTitle.setText(model.getConDesc());
        holder.txtConDesc.setText(model.getConSubDesc());

        if (TextUtils.isEmpty(model.getTotalTrades()) || model.getTotalTrades().equalsIgnoreCase("null"))
            holder.txtTrades.setText("0 Vote");
        else
            holder.txtTrades.setText(model.getTotalTrades()+" Votes");

        String rs=context.getResources().getString(R.string.rs);

        if (!TextUtils.isEmpty(model.getConImage())){
            CustomUtil.loadImageWithGlide(context,holder.imgConImage, MyApp.imageBase + MyApp.document,model.getConImage(),R.drawable.event_placeholder);
        }else
            holder.imgConImage.setImageResource(R.drawable.event_placeholder);

        if (TextUtils.isEmpty(model.getOption1())){
            holder.txtOp1.setVisibility(View.GONE);
        }else {
            holder.txtOp1.setVisibility(View.VISIBLE);
            holder.txtOp1.setText(model.getOption1());// + " | "+rs+model.getEntryFee()
        }

        if (TextUtils.isEmpty(model.getOption2())){
            holder.txtOp2.setVisibility(View.GONE);
        }else {
            holder.txtOp2.setVisibility(View.VISIBLE);
            holder.txtOp2.setText(model.getOption2());// + " | "+rs+model.getEntryFee()
        }

        if (TextUtils.isEmpty(model.getOption3())){
            holder.txtOp3.setVisibility(View.GONE);
        }else {
            holder.txtOp3.setVisibility(View.VISIBLE);
            holder.txtOp3.setText(model.getOption3());// + " | "+rs+model.getEntryFee()
        }

        if (TextUtils.isEmpty(model.getOption4())){
            holder.txtOp4.setVisibility(View.GONE);
        }else {
            holder.txtOp4.setVisibility(View.VISIBLE);
            holder.txtOp4.setText(model.getOption4());// + " | "+rs+model.getEntryFee()
        }

        if (TextUtils.isEmpty(model.getOption5())){
            holder.txtOp5.setVisibility(View.GONE);
        }else {
            holder.txtOp5.setVisibility(View.VISIBLE);
            holder.txtOp5.setText(model.getOption5());// + " | "+rs+model.getEntryFee()
        }

        holder.txtOp1.setOnClickListener(view -> {
            model.setOptionValue(model.getOption1());
            listener.onItemClick(model);
        });

        holder.txtOp2.setOnClickListener(view -> {
            model.setOptionValue(model.getOption2());
            listener.onItemClick(model);
        });

        holder.txtOp3.setOnClickListener(view -> {
            model.setOptionValue(model.getOption3());
            listener.onItemClick(model);
        });

        holder.txtOp4.setOnClickListener(view -> {
            model.setOptionValue(model.getOption4());
            listener.onItemClick(model);
        });

        holder.txtOp5.setOnClickListener(view -> {
            model.setOptionValue(model.getOption5());
            listener.onItemClick(model);
        });

        holder.layMain.setOnClickListener(view -> {
            //model.setMy_total_trades_count(model.getTotalTrades());
            context.startActivity(new Intent(context, PollDetailsActivity.class)
                    .putExtra("eventModel", model)
                    .putExtra("isLiveEvent", true)
            );
        });

    }

    public void updateData(ArrayList<EventModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface PredictionItemClick{
        public void onItemClick(EventModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtConEnd,txtConTitle,txtOp1,txtOp2,txtOp3,txtOp4,txtOp5,txtTrades,txtNewBadge,txtConDesc;
        private ImageView imgConImage;
        private LinearLayout layMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtConEnd=itemView.findViewById(R.id.txtConEnd);
            imgConImage=itemView.findViewById(R.id.imgConImage);
            txtConTitle=itemView.findViewById(R.id.txtConTitle);
            txtOp1=itemView.findViewById(R.id.txtOp1);
            txtOp2=itemView.findViewById(R.id.txtOp2);
            txtOp3=itemView.findViewById(R.id.txtOp3);
            txtOp4=itemView.findViewById(R.id.txtOp4);
            txtOp5=itemView.findViewById(R.id.txtOp5);
            txtTrades=itemView.findViewById(R.id.txtTrades);
            txtNewBadge=itemView.findViewById(R.id.txtNewBadge);
            txtConDesc=itemView.findViewById(R.id.txtConDesc);
            layMain=itemView.findViewById(R.id.layMain);

        }
    }
}
