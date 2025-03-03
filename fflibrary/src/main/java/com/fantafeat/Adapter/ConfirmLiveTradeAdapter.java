package com.fantafeat.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.EventModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConfirmLiveTradeAdapter extends RecyclerView.Adapter<ConfirmLiveTradeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EventModel> list;
    private OpinionOptionClick listener;
    private boolean isLiveEvent;

    private long diff;
    public ConfirmLiveTradeAdapter(Context context, ArrayList<EventModel> list, OpinionOptionClick listener, boolean isLiveEvent) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.isLiveEvent = isLiveEvent;
    }

    public interface OpinionOptionClick{
        public void onItemClick(EventModel item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.confirm_trade_after_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel model=list.get(position);
        String rs=context.getResources().getString(R.string.rs);
        DecimalFormat format=new DecimalFormat("00.00");

        holder.txtTitle.setText(model.getConDesc());

        holder.txtAnswer.setText(model.getOpinion_value()+" @ "+model.getSell_bal());
        holder.txtSlotFee.setText(rs+model.getEntryFee()+"/Slot");
        holder.txtInvested.setText(rs+model.getJoin_ef());
        holder.txtOption1Text.setText(model.getOption1());
        holder.txtOption2Text.setText(model.getOption2());
        holder.endTime.setText(model.getConSubDesc());//"ENDS ON "+confirmTradeDate

        Date date = null;
        String confirmTradeDate = "";
        SimpleDateFormat format1 = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        try {
            date = format1.parse(model.getConEndTime());
            confirmTradeDate = matchFormate.format(date);
            diff = date.getTime() - MyApp.getCurrentDate().getTime();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtContestDate.setText(confirmTradeDate);

        if (isLiveEvent){
            holder.txtGet.setText(rs+format.format(Float.parseFloat(model.getJoin_ef())*Float.parseFloat(model.getSell_bal())));
            holder.txtAnswerLbl.setText("Your Answer");
            holder.txtAnswer.setBackgroundResource(R.drawable.gray_bg_light_circular);
            holder.txtAnswer.setTextColor(context.getResources().getColor(R.color.black_pure));
            holder.txtJoin.setText(model.getCon_join_qty()+"/"+ CustomUtil.coolFormat(context,model.getTotalTrades()));
            holder.txtLblGet.setText("You Get");

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
        else {
            holder.txtGet.setText(rs+model.getJoin_win_amt());
            holder.txtJoin.setText(model.getCon_join_qty()+"");
            holder.txtLblGet.setText("Winning");

            Date date1 = null;
            String confirmTradeDate1 = "";
            SimpleDateFormat format2 = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat matchFormate1 = MyApp.changedFormat("hh:mm a");
            try {
                date1 = format2.parse(model.getConEndTime());
                confirmTradeDate1 = matchFormate1.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.txtContestTime.setText(confirmTradeDate1);

            if (model.getConWinning().equalsIgnoreCase(model.getOpinion_value())){
                holder.txtAnswer.setBackgroundResource(R.drawable.opinion_yes_selected);
                holder.txtAnswer.setTextColor(context.getResources().getColor(R.color.green_pure));
                holder.txtAnswerLbl.setText("Your Answer is Correct");
            }
            else {
                holder.txtAnswer.setBackgroundResource(R.drawable.opinion_no_selected);
                holder.txtAnswer.setTextColor(context.getResources().getColor(R.color.red));
                holder.txtAnswerLbl.setText("Your Answer is not Correct");
            }
        }

        CustomUtil.loadImageWithGlide(context,holder.imgContest1, ApiManager.TEAM_IMG,model.getOptionImg1(),R.drawable.event_placeholder);
        CustomUtil.loadImageWithGlide(context,holder.imgContest2, ApiManager.TEAM_IMG,model.getOptionImg2(),R.drawable.event_placeholder);
        /*if (!TextUtils.isEmpty(model.getOptionImg1())) {
            CustomUtil.loadImageWithGlide(context,holder.imgContest1, ApiManager.TEAM_IMG,model.getOptionImg1(),R.drawable.event_placeholder);
        } else
            holder.imgContest1.setImageResource(R.drawable.event_placeholder);

        if (!TextUtils.isEmpty(model.getOptionImg2())) {
            CustomUtil.loadImageWithGlide(context,holder.imgContest2, ApiManager.TEAM_IMG,model.getOptionImg2(),R.drawable.event_placeholder);
        } else
            holder.imgContest2.setImageResource(R.drawable.event_placeholder);*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout layMain;
       // private LinearLayout layWaitCnt;
        private TextView txtTitle,txtContestTime,txtSlotFee,txtInvested,txtGet,txtAnswerLbl,txtLblGet,
        /*txtWaitCnt,*/txtAnswer/*,txtOption1,txtOption2*/,txtOption2Text,txtOption1Text,txtJoin,endTime,txtContestDate;
        private ImageView imgContest1,imgContest2;
        CountDownTimer timer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layMain=itemView.findViewById(R.id.layMain);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            imgContest1=itemView.findViewById(R.id.imgContest1);
            imgContest2=itemView.findViewById(R.id.imgContest2);
            txtContestTime=itemView.findViewById(R.id.txtContestTime);
            txtSlotFee=itemView.findViewById(R.id.txtSlotFee);
            txtInvested=itemView.findViewById(R.id.txtInvested);
            txtGet=itemView.findViewById(R.id.txtGet);
            txtAnswerLbl=itemView.findViewById(R.id.txtAnswerLbl);
            txtLblGet=itemView.findViewById(R.id.txtLblGet);
            txtOption2Text=itemView.findViewById(R.id.txtOption2Text);
            txtOption1Text=itemView.findViewById(R.id.txtOption1Text);
            /*txtWaitCnt=itemView.findViewById(R.id.txtWaitCnt);
            layWaitCnt=itemView.findViewById(R.id.layWaitCnt);*/
            txtAnswer=itemView.findViewById(R.id.txtAnswer);
            txtJoin=itemView.findViewById(R.id.txtJoin);
            endTime=itemView.findViewById(R.id.endTime);
            txtContestDate=itemView.findViewById(R.id.txtContestDate);
           // txtOption1=itemView.findViewById(R.id.txtOption1);
           // txtOption2=itemView.findViewById(R.id.txtOption2);

        }
    }
}
