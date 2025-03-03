package com.fantafeat.Adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.LudoTournamentModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LudoTournamentAdapter extends RecyclerView.Adapter<LudoTournamentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LudoTournamentModel> list;
    private TournamentListener listener;
   // private MyPreferences preferences;

    public LudoTournamentAdapter(Context context, ArrayList<LudoTournamentModel> list, TournamentListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        //preferences=MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ludo_tournament_contest_iitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LudoTournamentModel model=list.get(position);

        String rs=context.getResources().getString(R.string.rs);

        holder.contest_price_pool.setText(CustomUtil.displayAmountFloat(context,model.getDistAmt()));
        String entry="<font><b>"+rs+model.getEntryFee()+"</b></font>";
        holder.txtGiveOpinion.setText(Html.fromHtml(entry));

        holder.txtLblTournament.setText(model.getTitle());
        if (model.getMyJoinCnt().equalsIgnoreCase("0")){
            holder.imgPlayed.setVisibility(View.GONE);
            holder.txtPlayedCnt.setVisibility(View.GONE);
        }else {
            holder.imgPlayed.setVisibility(View.VISIBLE);
            holder.txtPlayedCnt.setVisibility(View.VISIBLE);
            holder.txtPlayedCnt.setText(model.getMyJoinCnt()+" Played");
        }


        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(model.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int totalSpots = CustomUtil.convertInt(model.getNoOfSpot());
        int jointeam = CustomUtil.convertInt(model.getTotalJoinSpot());
        int left=totalSpots - jointeam;

        if (model.getMaxJoinSpot().equalsIgnoreCase(model.getMyJoinCnt())){
            holder.txtGiveOpinion .setBackgroundResource(R.drawable.black_round_curve);
        }else if (left<=0){
            holder.txtGiveOpinion .setBackgroundResource(R.drawable.black_round_curve);
        }else {
            if (date.before(MyApp.getCurrentDate())) {
                holder.txtGiveOpinion .setBackgroundResource(R.drawable.black_round_curve);
            }else {
                holder.txtGiveOpinion.setBackgroundResource(R.drawable.btn_green_curve);
            }
        }

        if (holder.timer != null) {
            holder.timer.cancel();
        }

        if (date.before(MyApp.getCurrentDate())) {
            holder.txtConEnd.setText("Tournament Ended");
        }else {
            DecimalFormat entryFormat = CustomUtil.getFormater("00");
            long diff = 0;
            try {
                date = format.parse(model.getEndTime());
                diff = date.getTime() - MyApp.getCurrentDate().getTime();
                //System.out.println("timzne :" + MyApp.getCurrentDate().getTime()+"  "+diff);
            }
            catch (ParseException e) {
                LogUtil.e("TAG", "onBindViewHolder: " + e.toString());
                e.printStackTrace();
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
                        diff =entryFormat.format(elapsedDays) + "d : " +entryFormat.format(elapsedHours) + "h ";
                    } else if (elapsedHours > 0) {
                        diff =entryFormat.format(elapsedHours) + "h : " +entryFormat.format(elapsedMinutes) + "m";
                    } else {
                        diff =entryFormat.format(elapsedMinutes) + "m : " +entryFormat.format(elapsedSeconds) + "s";
                    }
                    holder.txtConEnd.setText("Ends on "+diff);
                }

                public void onFinish() {
                    holder.txtConEnd.setText("Tournament Ended");
                    holder.txtGiveOpinion .setBackgroundResource(R.drawable.black_round_curve);
                    /*MyApp.getRealmInstance().executeTransaction(realm -> {
                        MatchModel results= realm.where(MatchModel.class).equalTo("id",List.getId()).findFirst();
                        results.deleteFromRealm();
                    });*/

                }
            }.start();

          //  holder.txtConEnd.setText("Ends on "+ CustomUtil.dateConvertWithFormat(model.getEndTime(),"dd MMM, yy hh:mm aa"));
        }

        holder.contest_total_team.setText(model.getNoOfSpot());

        if (left<0){
            left=0;
        }
        holder.contest_left_team.setText(left+"");

        int progress = (jointeam * 100) / totalSpots;
        if (progress==0 && jointeam>0)
            progress+=1;

        holder.contest_progress.setProgress(progress);

        int totalWin=Integer.parseInt(model.getTotalWinners())*100/totalSpots;

        holder.txtWinPer.setText(totalWin+"%");
        holder.txtUpto.setText("Up to "+model.getMaxJoinSpot());

        //float firstPrise=CustomUtil.convertFloat(model.getDistAmt())/CustomUtil.convertInt(model.getTotalWinners());
        if (!TextUtils.isEmpty(model.getWinning_tree())){
            try {
                JSONArray array=new JSONArray(model.getWinning_tree());
                if (array.length()>0){
                    holder.txtFirstPrice.setText(rs+array.optJSONObject(0).optString("amount"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        holder.layMain.setOnClickListener(v -> {
            listener.onItemClick(model);
        });

        holder.contest_price_pool.setOnClickListener(v -> {
            listener.onTreeShow(model);
        });

        holder.txtFirstPrice.setOnClickListener(v -> {
            listener.onTreeShow(model);
        });

        Date finalDate = date;
        int finalLeft = left;
        holder.txtGiveOpinion.setOnClickListener(v -> {
            if (!model.getMaxJoinSpot().equalsIgnoreCase(model.getMyJoinCnt())){
                if (!finalDate.before(MyApp.getCurrentDate()) && finalLeft >0) {
                    listener.onJoin(model);
                }
            }
        });

        holder.txtWinPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ViewTooltip
                        .on((Activity) context, holder.txtWinPer)
                        .autoHide(true, 2000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Winning Percentage")
                        .textColor(Color.WHITE)
                        .color(context.getResources().getColor(R.color.colorPrimary))
                        .show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface TournamentListener{
        public void onItemClick(LudoTournamentModel model);
        public void onTreeShow(LudoTournamentModel model);
        public void onJoin(LudoTournamentModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layMain;
        private ProgressBar contest_progress;
        private TextView contest_price_pool,txtGiveOpinion,txtConEnd,contest_left_team,contest_total_team,txtFirstPrice,txtWinPer,txtUpto,
                txtLblTournament,txtPlayedCnt;
        CountDownTimer timer;
        private ImageView imgPlayed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contest_price_pool=itemView.findViewById(R.id.contest_price_pool);
            txtGiveOpinion=itemView.findViewById(R.id.txtGiveOpinion);
            txtConEnd=itemView.findViewById(R.id.txtConEnd);
            contest_left_team=itemView.findViewById(R.id.contest_left_team);
            contest_total_team=itemView.findViewById(R.id.contest_total_team);
            contest_progress=itemView.findViewById(R.id.contest_progress);
            txtFirstPrice=itemView.findViewById(R.id.txtFirstPrice);
            txtWinPer=itemView.findViewById(R.id.txtWinPer);
            txtUpto=itemView.findViewById(R.id.txtUpto);
            layMain=itemView.findViewById(R.id.layMain);
            txtLblTournament=itemView.findViewById(R.id.txtLblTournament);
            txtPlayedCnt=itemView.findViewById(R.id.txtPlayedCnt);
            imgPlayed=itemView.findViewById(R.id.imgPlayed);

        }
    }
}
