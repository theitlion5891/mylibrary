package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TournamentMatchAdapter extends RecyclerView.Adapter<TournamentMatchAdapter.ViewHolder> {

    private Context mContext;
    private List<MatchModel> matchModelList;
    private TournamentListener listener;
    MyPreferences preferences;
    long diff;
    DecimalFormat entryFormat = CustomUtil.getFormater("00");
    //private Fragment fragment;

    public TournamentMatchAdapter(Context mContext, List<MatchModel> matchModelList, TournamentListener listener/*, Fragment fragment*/) {
        this.mContext = mContext;
        this.matchModelList = matchModelList;
        this.listener = listener;
       // this.fragment = fragment;
        preferences = MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.tournament_match_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel List = matchModelList.get(position);

        Date date = null;

       /* if (CustomUtil.convertInt(List.getMatchSquad()) == 0) {
            holder.main_card.setClickable(false);
            holder.main_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
        } else {
            holder.main_card.setClickable(true);
            holder.main_card.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
        }*/

        if (List.getTeamAXi().equalsIgnoreCase("Yes") || List.getTeamBXi().equalsIgnoreCase("Yes")) {
            //holder.imgXi.setImageResource(R.drawable.annoucement_green);
            Glide.with(mContext)
                    .load(R.drawable.go)
                    .into(holder.lineout);
            holder.layLine.setVisibility(View.VISIBLE);
        } else {
            holder.layLine.setVisibility(View.GONE);
        }

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = MyApp.changedFormat("dd MMM,hh:mm a");
        String str = "";
        try {
            if (preferences.getPrefBoolean(PrefConstant.MATCH_TYPE)) {
                date = format.parse(List.getRegularMatchStartTime());
            } else {
                date = format.parse(List.getSafeMatchStartTime());
            }

            str = outputFormat.format(date);

            if (isTomorrow(date)){
                str="Tomorrow";
            }
            else if ( DateUtils.isToday(date.getTime())){
                str=outputFormat.format(date);
            }
            else
                str="";

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

        } catch (ParseException e) {
            LogUtil.e("TAG", "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (holder.timer != null) {
            holder.timer.cancel();
        }

        if (List.getMatchStatus().equals("Pending")) {
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
                        diff = entryFormat.format(elapsedDays) +"d : " + entryFormat.format(elapsedHours) + "h ";
                    } else if (elapsedHours > 0) {
                        diff = entryFormat.format(elapsedHours) + "h : " + entryFormat.format(elapsedMinutes) + "m";
                    } else {
                        diff = entryFormat.format(elapsedMinutes) + "m : " + entryFormat.format(elapsedSeconds) + "s";
                    }
                    holder.match_time.setText(diff);
                }

                public void onFinish() {
                    holder.match_time.setText("Playing");
                }
            }.start();
        }

        holder.match_date.setText(str);
        holder.team1_sname.setText(List.getTeam1Sname());
        holder.team2_sname.setText(List.getTeam2Sname());
        holder.match_title.setText(List.getMatchTitle());

        CustomUtil.loadImageWithGlide(mContext, holder.team1_img, MyApp.imageBase + MyApp.document,
                List.getTeam1Img(),
                R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(mContext, holder.team2_img, MyApp.imageBase + MyApp.document,
                List.getTeam2Img(),
                R.drawable.team_loading);

        if (TextUtils.isEmpty(List.getMega_text())) {
            holder.txtGrandPrise.setText("Coming Soon");
            //holder.txtGrandPrise.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            holder.txtGrandPrise.setText(List.getMega_text());
           // holder.txtGrandPrise.setTextColor(mContext.getResources().getColor(R.color.yellow_golden));
        }

        /*if (position==0)
            holder.layGrand.setVisibility(View.VISIBLE);*/

        final Date finalDate = date;
        holder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (CustomUtil.convertInt(List.getMatchSquad()) == 0) {
                        CustomUtil.showTopSneakWarning(mContext, "Contest for this match will open soon. Stay tuned!");
                    } else if (finalDate.before(MyApp.getCurrentDate())) {
                        CustomUtil.showTopSneakWarning(mContext, "The match has already started");
                    } else {
                        List.setConDisplayType("1");
                        preferences.setMatchModel(List);
                        Intent intent = new Intent(mContext, ContestListActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            }
        });

        /*if (position<bgArr.length-1){
            holder.layTop.setBackgroundResource(bgArr[position]);
        }else {
            holder.layTop.setBackgroundResource(bgArr[0]);
        }*/

       /* ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setShader(new LinearGradient(0, 0, 0, holder.layTop.getHeight(), Color.parseColor("#330000FF"),
                Color.parseColor("#110000FF"), Shader.TileMode.REPEAT));
        holder.layTop.setBackgroundDrawable(mDrawable);*/

        GradientDrawable gd;
        if (TextUtils.isEmpty(List.getTeam1Color()) || List.getTeam1Color().equalsIgnoreCase("yes") ||
                List.getTeam1Color().equalsIgnoreCase("no")){

            gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor("#8798DA"), Color.parseColor("#DD706D")});
            //gd.setCornerRadius(20f);
            // gd.setStroke(5,mContext.getResources().getColor(R.color.white));

        }else {
            gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(List.getTeam1Color()), Color.parseColor(List.getTeam2Color())});
            //gd.setCornerRadius(20f);
            // gd.setStroke(5,mContext.getResources().getColor(R.color.white));

        }
        holder.layTop.setBackgroundDrawable(gd);

    }

    public boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public interface TournamentListener{
        public void onClick(MatchModel model);
    }

    @Override
    public int getItemCount() {
        return matchModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layGrand,layLine;
       // private CardView cardXi;
        private RelativeLayout layTop;
        private CardView mainCard;
       // private ImageView imgXi;
        //private CardView main_card;
        private TextView match_time,match_date,team1_sname,team2_sname,txtGrandPrise,match_title;
        private CircleImageView team1_img,team2_img/*,team2_img_dummy,team1_img_dummy*/;
        private ImageView lineout;

        CountDownTimer timer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layTop=itemView.findViewById(R.id.layTop);
            team2_img=itemView.findViewById(R.id.team2_img);
            team1_img=itemView.findViewById(R.id.team1_img);
            match_time=itemView.findViewById(R.id.match_time);
            match_date=itemView.findViewById(R.id.match_date);
            team1_sname=itemView.findViewById(R.id.team1_sname);
            team2_sname=itemView.findViewById(R.id.team2_sname);
            txtGrandPrise=itemView.findViewById(R.id.txtGrandPrise);
            txtGrandPrise.setSelected(true);
            layLine=itemView.findViewById(R.id.layLine);
            //cardXi=itemView.findViewById(R.id.cardXi);
            mainCard=itemView.findViewById(R.id.mainCard);
            layGrand=itemView.findViewById(R.id.layGrand);
            lineout=itemView.findViewById(R.id.lineout);
            match_title=itemView.findViewById(R.id.match_title);
            match_title.setSelected(true);

            /*team2_img_dummy=itemView.findViewById(R.id.team2_img_dummy);
            team1_img_dummy=itemView.findViewById(R.id.team1_img_dummy);
            match_Xi=itemView.findViewById(R.id.match_Xi);*/
        }
    }
}
