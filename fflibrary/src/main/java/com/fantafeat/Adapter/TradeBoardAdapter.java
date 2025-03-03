package com.fantafeat.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.StatusUserListModel;
import com.fantafeat.Model.TradeBoard;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TradeBoardAdapter extends RecyclerView.Adapter<TradeBoardAdapter.ViewHolder> {
    private Context context;
    private List<EventModel> list;
    private long diff;
    private TradeBoardListener listener;
    private String eventType;


    public TradeBoardAdapter(Context context, List<EventModel> list, TradeBoardListener listener,String eventType) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.eventType=eventType;
    }

    public interface TradeBoardListener{
        public void onClick(EventModel model);
    }

    @NonNull
    @Override
    public TradeBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TradeBoardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tradeboard,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TradeBoardAdapter.ViewHolder holder, int position) {
        EventModel model=list.get(position);
        holder.conDesc.setText(model.getConDesc());
        holder.team1ShortName.setText(model.getOption1());
        holder.team2ShortName.setText(model.getOption2());
        holder.opt1_price.setText(model.getOption1Price());
        holder.opt2_price.setText(model.getOption2Price());
        holder.totalTrades.setText(model.getTotalTrades()+" joined");
        holder.entryFee.setText("Confirm Trade "+"("+context.getResources().getString(R.string.rs)+model.getEntryFee()+" = 1 Slot)");

        CustomUtil.loadImageWithGlide(context,holder.imgContest1, ApiManager.TEAM_IMG,model.getOptionImg1(),R.drawable.event_placeholder);
        CustomUtil.loadImageWithGlide(context,holder.imgContest2, ApiManager.TEAM_IMG,model.getOptionImg2(),R.drawable.event_placeholder);

        if (!TextUtils.isEmpty(model.getCon_status()) && model.getCon_status().equalsIgnoreCase("hold")){
            holder.viewDisable.setVisibility(View.VISIBLE);
            //holder.txtContestTime.getBackground().setAlpha(150);
        }else {
            holder.viewDisable.setVisibility(View.GONE);
            //holder.txtContestTime.getBackground().setAlpha(255);
        }

        holder.layTeam1.setOnClickListener(v -> {
            if (TextUtils.isEmpty(model.getCon_status()) || !model.getCon_status().equalsIgnoreCase("hold")) {
                model.setOptionPrice(model.getOption1Price());
                //model.setItemSelected(true);
                model.setOptionValue(model.getOption1());
                listener.onClick(model);
            }
        });
        holder.layTeam2.setOnClickListener(v -> {
            if (TextUtils.isEmpty(model.getCon_status()) || !model.getCon_status().equalsIgnoreCase("hold")) {
                //model.setItemSelected(false);
                model.setOptionPrice(model.getOption2Price());
                model.setOptionValue(model.getOption2());
                listener.onClick(model);
            }
        });

        Date date = null;
        String confirmTradeDate = "";
        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        try {
            date = format.parse(model.getConEndTime());
            confirmTradeDate = matchFormate.format(date);
            diff = date.getTime() - MyApp.getCurrentDate().getTime();
            //LogUtil.e("Difghgfff", String.valueOf(diff));
            //Log.e(TAG, "onBindViewHolder: " + diff);
        }
        catch (ParseException e) {
            //LogUtil.e(TAG, "error onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        holder.endTime.setText(model.getConSubDesc());//"ENDS ON "+confirmTradeDate
        holder.txtContestDate.setText(confirmTradeDate);

        if (holder.timer != null) {
            holder.timer.cancel();
        }
        holder.timer = new CountDownTimer(diff, 1000) {

            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = millisUntilFinished / daysInMilli;
                millisUntilFinished = millisUntilFinished % daysInMilli;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;



                String diff = "";
                if (elapsedDays > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedDays) + "D " +": "+ CustomUtil.getFormater("00").format(elapsedHours) + "H";
                }
                else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedHours) + "H "+": " + CustomUtil.getFormater("00").format(elapsedMinutes) + "M";
                }
                else {
                    diff = CustomUtil.getFormater("00").format(elapsedMinutes) + "M "+": " + CustomUtil.getFormater("00").format(elapsedSeconds) + "S";
                }

                holder.txtContestTime.setText(diff);
            }

            @Override
            public void onFinish() {
                holder.txtContestTime.setText("Event Ended");
            }

        }.start();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CountDownTimer timer;
        LinearLayout layTeam1,layTeam2;
        CircleImageView imgContest1,imgContest2;
        private View viewDisable;
        private TextView conDesc,team1ShortName,team2ShortName,entryFee,opt1_price,opt2_price,endTime,totalTrades,txtContestTime,txtContestDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conDesc=itemView.findViewById(R.id.conDesc);
            viewDisable=itemView.findViewById(R.id.viewDisable);
            team1ShortName=itemView.findViewById(R.id.team1ShortName);
            team2ShortName=itemView.findViewById(R.id.team2ShortName);
            entryFee=itemView.findViewById(R.id.entryFee);
            opt1_price=itemView.findViewById(R.id.opt1_price);
            opt2_price=itemView.findViewById(R.id.opt2_price);
            endTime=itemView.findViewById(R.id.endTime);
            totalTrades=itemView.findViewById(R.id.totalTrades);
            layTeam1=itemView.findViewById(R.id.layTeam1);
            layTeam2=itemView.findViewById(R.id.layTeam2);
            txtContestTime=itemView.findViewById(R.id.txtContestTime);
            txtContestDate=itemView.findViewById(R.id.txtContestDate);
            imgContest1=itemView.findViewById(R.id.imgContest1);
            imgContest2=itemView.findViewById(R.id.imgContest2);
        }
    }
}
