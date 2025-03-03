package com.fantafeat.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fantafeat.Session.BaseFragment.TAG;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchViewHolder> {

    private Context mContext;
    private List<MatchModel> matchModelList;
    long diff;
    MyPreferences preferences;

    public MatchListAdapter(Context mContext, List<MatchModel> matchModelList) {
        this.mContext = mContext;
        this.matchModelList = matchModelList;
        preferences = new MyPreferences(mContext);
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_match, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchViewHolder holder, int position) {
        final MatchModel matchModel = matchModelList.get(position);
        Date date = null;
        String matchDate = "";


        if (matchModel.getTeamAXi().equalsIgnoreCase("Yes") ||
                matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
            holder.layLine.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(R.drawable.go)
                    .into(holder.lineout);
        } else {
            holder.layLine.setVisibility( View.GONE);
        }

        //Match Squad
        if (CustomUtil.convertInt(matchModel.getMatchSquad()) == 0) {
            holder.cardView.setClickable(false);
            holder.cardView.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
        } else {
            holder.cardView.setClickable(true);
            holder.cardView.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
        }

        holder.team1_name.setText(matchModel.getTeam1Sname());
        holder.team2_name.setText(matchModel.getTeam2Sname());
        holder.team1_full_name.setText(matchModel.getTeam1Name());
        holder.team2_full_name.setText(matchModel.getTeam2Name());
        holder.match_title.setText(matchModel.getMatchTitle());

        CustomUtil.loadImageWithGlide(mContext,holder.team1_img,ApiManager.TEAM_IMG,matchModel.getTeam1Img(),R.drawable.ic_team1_placeholder);
        CustomUtil.loadImageWithGlide(mContext,holder.team2_img,ApiManager.TEAM_IMG,matchModel.getTeam2Img(),R.drawable.ic_team2_placeholder);

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("hh:mm a");
        try {
            date = format.parse(matchModel.getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            if (isTomorrow(date)){
                holder.txtMatchStartTime.setText("Tomorrow");
            }else if ( DateUtils.isToday(date.getTime())){
                holder.txtMatchStartTime.setText(matchDate);
            }else
                holder.txtMatchStartTime.setText("");


            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            //Log.e(TAG, "onBindViewHolder: " + diff);
        }
        catch (ParseException e) {
            //LogUtil.e(TAG, "error onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

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

                if (elapsedDays<=0 && elapsedHours<=0 && elapsedMinutes<=5){
                    //LogUtil.e("respDif"," "+elapsedMinutes+"  "+holder.isAnimationStarted);
                    if (!holder.isAnimationStarted){
                        holder.isAnimationStarted=true;
                        holder.anim.start();
                    }
                }else {
                    if (holder.isAnimationStarted){
                        holder.anim.cancel();
                    }
                }

                String diff = "";
                if (elapsedDays > 0) {
                    diff = CustomUtil.getFormater("0").format(elapsedDays) + "d " + CustomUtil.getFormater("0").format(elapsedHours) + "h";
                }
                else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("0").format(elapsedHours) + "h " + CustomUtil.getFormater("0").format(elapsedMinutes) + "m";
                }
                else {
                    diff = CustomUtil.getFormater("0").format(elapsedMinutes) + "m " + CustomUtil.getFormater("0").format(elapsedSeconds) + "s";
                }

                holder.match_timer.setText(diff);
            }

            public void onFinish() {
                //holder.cardView.setVisibility(View.GONE);
                holder.match_timer.setText("Playing");
            }

        }.start();

        if (TextUtils.isEmpty(matchModel.getIs_single())){
            matchModel.setIs_single("no");
        }

        if (TextUtils.isEmpty(matchModel.getIs_fifer())){
            matchModel.setIs_fifer("no");
        }

        /*if (!TextUtils.isEmpty(matchModel.getMatchDesc()) ||
                !TextUtils.isEmpty(matchModel.getMega_text()) ||
                matchModel.getTeam1Color().equalsIgnoreCase("yes") ||
                matchModel.getIs_single().equalsIgnoreCase("yes") ||
                matchModel.getIs_fifer().equalsIgnoreCase("yes")){
            holder.layDesc.setVisibility(View.VISIBLE);
        }else {
            holder.layDesc.setVisibility(View.GONE);
        }*/

        if (!TextUtils.isEmpty(matchModel.getMatchDesc())){
           // holder.layDesc.setVisibility(View.GONE);
            holder.txtMatchDesc.setText(matchModel.getMatchDesc());
        }else {
            //holder.layDesc.setVisibility(View.VISIBLE);
           // holder.txtMatchDesc.setText(matchModel.getMatchDesc());
            // setTickerAnimation(holder.txtMatchDesc);
        }

        if (TextUtils.isEmpty(matchModel.getMega_text())){
            holder.lblGrand.setVisibility(View.GONE);
            holder.txtGrandText.setVisibility(View.GONE);
        }else {
            //holder.layDesc.setVisibility(View.VISIBLE);

            holder.lblGrand.setVisibility(View.VISIBLE);
            holder.txtGrandText.setVisibility(View.VISIBLE);
            holder.txtGrandText.setText(matchModel.getMega_text());
        }

        if (matchModel.getTeam1Color().equalsIgnoreCase("yes")){
            //holder.layDesc.setVisibility(View.VISIBLE);
            holder.imgLeader.setVisibility(View.VISIBLE);
        }else {
            holder.imgLeader.setVisibility(View.GONE);
        }

        /*if (!TextUtils.isEmpty(matchModel.getIs_single()) && !TextUtils.isEmpty(matchModel.getIs_fifer())){
            if (matchModel.getIs_single().equalsIgnoreCase("yes") || matchModel.getIs_fifer().equalsIgnoreCase("yes")){
                holder.txtFifer.setVisibility(View.VISIBLE);
                holder.txtSingles.setVisibility(View.VISIBLE);
                //holder.layDesc.setVisibility(View.VISIBLE);
            }else {
                holder.txtFifer.setVisibility(View.GONE);
                holder.txtSingles.setVisibility(View.GONE);
                //holder.layDesc.setVisibility(View.GONE);
            }
        }else {
            holder.txtFifer.setVisibility(View.GONE);
            holder.txtSingles.setVisibility(View.GONE);
           // holder.layDesc.setVisibility(View.GONE);
        }*/


        /*if (holder.handler!=null){
                holder.handler.removeCallbacksAndMessages(null);
                holder.handler.postDelayed(new Runnable() {
                    public void run() {
                        if (holder.imgLeaderboard.getTag().equals("red")){
                            holder.imgLeaderboard.setTag("white");
                            holder.imgLeaderboard.setImageResource(R.drawable.ic_white_leaderboard);
                        }else {
                            holder.imgLeaderboard.setTag("red");
                            holder.imgLeaderboard.setImageResource(R.drawable.ic_red_leaderboard);
                        }
                        holder.handler.postDelayed(this, holder.delay);
                    }
                }, holder.delay);
            }*/
        //if (matchModel.is)

       /* holder.txtFifer.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                ViewTooltip
                        .on((Activity)  mContext, holder.txtFifer)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(TOP)
                        .text("Fifer Contest Available")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }
        });

        holder.txtSingles.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                ViewTooltip
                        .on((Activity)  mContext, holder.txtSingles)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(TOP)
                        .text("Singles Contest Available")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }
        });*/

        holder.imgLeader.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
               // mContext.startActivity(new Intent(mContext, UserLeaderboardActivity.class));
                /*ViewTooltip
                        .on((Activity)  mContext, holder.imgLeader)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(TOP)
                        .text("Leaderboard Match")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }
        });

        final Date finalDate = date;
        holder.cardView.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                if (CustomUtil.convertInt(matchModel.getMatchSquad()) == 0) {
                    CustomUtil.showTopSneakError(mContext, "Contest for this match will open soon. Stay tuned!");
                } else if (finalDate.before(MyApp.getCurrentDate())) {
                    CustomUtil.showTopSneakError(mContext, "The match has already started");
                } else {
                   // Log.d("selSport",matchModel.getSportId()+" ");
                    preferences.setMatchModel(matchModel);
                    Intent intent = new Intent(mContext, ContestListActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    @Override
    public int getItemCount() {
        return matchModelList.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView match_lineups_out, team1_name, match_title, match_timer, team2_name,team1_full_name,
                team2_full_name,txtMatchDesc,txtGrandText,lblGrand,/*txtFifer,txtSingles,*/txtMatchStartTime;

        ImageView lineout,imgLeader;
        CircleImageView team2_img, team1_img;
        LinearLayout card_linear,layLine,layDesc;//,layLeaderboard;
        CardView cardView;
        CountDownTimer timer;
        Handler handler = new Handler();
        boolean isAnimationStarted=false;
        private ObjectAnimator anim;
        private int delay=500;
        //ShimmerFrameLayout shimmer;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            card_linear = itemView.findViewById(R.id.card_linear);
            layLine = itemView.findViewById(R.id.layLine);
            layDesc = itemView.findViewById(R.id.layDesc);
            cardView = itemView.findViewById(R.id.match_card);
            match_lineups_out = itemView.findViewById(R.id.match_lineups_out);
            lineout = itemView.findViewById(R.id.lineout);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            imgLeader = itemView.findViewById(R.id.imgLeader);
            //txtSingles = itemView.findViewById(R.id.txtSingles);
            //txtFifer = itemView.findViewById(R.id.txtFifer);
            txtMatchStartTime = itemView.findViewById(R.id.txtMatchStartTime);
            txtMatchDesc = itemView.findViewById(R.id.txtMatchDesc);
            txtMatchDesc.setSelected(true);

            match_title = itemView.findViewById(R.id.match_title);
            match_title.setSelected(true);
            match_timer = itemView.findViewById(R.id.match_timer);

            lblGrand = itemView.findViewById(R.id.lblGrand);
            txtGrandText = itemView.findViewById(R.id.txtGrandText);

            anim = ObjectAnimator.ofFloat(match_timer, "alpha", 0.5f, 1.0f);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(300);
            team2_img = itemView.findViewById(R.id.team2_img);
            team1_img = itemView.findViewById(R.id.team1_img);
            team1_full_name = itemView.findViewById(R.id.team1_full_name);
            team2_full_name = itemView.findViewById(R.id.team2_full_name);
        }
    }

    public void updateList(List<MatchModel> matchModelList) {
        this.matchModelList = matchModelList;
        notifyDataSetChanged();
    }

}
