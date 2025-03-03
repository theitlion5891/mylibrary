package com.fantafeat.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.BannerModel;
import com.fantafeat.Model.EventModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.StatusUserListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchFullListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MatchModel> matchModelList;
    private long diff;
    private MyPreferences preferences;
    private int pageTopAds=0;
    private MatchListener matchListener;

    public MatchFullListAdapter(Context mContext, List<MatchModel> matchModelList,MatchListener matchListener) {
        this.mContext = mContext;
        this.matchModelList = matchModelList;
        this.matchListener = matchListener;
        preferences = new MyPreferences(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return matchModelList.get(position).getMatchDisplayType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1){
            return new BannerHolder(LayoutInflater.from(mContext).inflate(R.layout.banner_pager_item,parent,false));
        }else if (viewType==2){
            return new TitleHolder(LayoutInflater.from(mContext).inflate(R.layout.match_title_item,parent,false));
        }else if (viewType==3){
            return new NoMatchHolder(LayoutInflater.from(mContext).inflate(R.layout.no_match_found,parent,false));
        }else if (viewType==4){
            return new TradingHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_match_item,parent,false));
        }else if (viewType==5){
            return new TradingHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_match_item,parent,false));
        }else if (viewType==6){
            return new TradingHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_match_item,parent,false));
        }else {
            return new MatchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_match,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final MatchModel matchModel = matchModelList.get(position);

        if (getItemViewType(position)==1){
            BannerHolder holder=(BannerHolder)viewHolder;

            OfferBannerAdapter adapter = new OfferBannerAdapter(mContext, matchModel.getBannerModels(),model -> {
                matchListener.onBannerClick(model);
            });
            holder.home_offer.setAdapter(adapter);
            holder.home_offer.setClipChildren(false);
            holder.home_offer.setOffscreenPageLimit(2);
            holder.home_offer.setPageTransformer(new MarginPageTransformer(20));

            if (holder.timerTopAds!=null){
                holder.timerTopAds.cancel();
            }
            holder.timerTopAds = new Timer();
            holder.timerTopAds.scheduleAtFixedRate(new TopAdsAutoScroll(holder.home_offer,matchModel.getBannerModels().size()),
                    0, 5 * 1000);

            holder.home_offer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    pageTopAds = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
        }
        else if (getItemViewType(position)==2){
            TitleHolder holder=(TitleHolder)viewHolder;
            holder.txtLbl.setText(matchModel.getMatchTitle());
        }
        else if (getItemViewType(position)==3){
            NoMatchHolder holder=(NoMatchHolder)viewHolder;

        }
        else if (getItemViewType(position)==4){
            TradingHolder holder=(TradingHolder) viewHolder;

            if (TextUtils.isEmpty(matchModel.getMatchTitle())){
                holder.txtLbl.setVisibility(View.GONE);
            }
            else {
                holder.txtLbl.setVisibility(View.VISIBLE);
                holder.txtLbl.setText(matchModel.getMatchTitle());
            }

            holder.recyclerOthers.setVisibility(View.VISIBLE);
            TradingContestAdapter tradingAdapter=new TradingContestAdapter(mContext, matchModel.getTradingList(), "Trading", item -> {
                matchListener.onTradeClick(item);
            });
            holder.recyclerOthers.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
            holder.recyclerOthers.setAdapter(tradingAdapter);
        }
        else if (getItemViewType(position)==5){
            TradingHolder holder=(TradingHolder) viewHolder;

            if (TextUtils.isEmpty(matchModel.getMatchTitle())){
                holder.txtLbl.setVisibility(View.GONE);
            }
            else {
                holder.txtLbl.setVisibility(View.VISIBLE);
                holder.txtLbl.setText(matchModel.getMatchTitle());
            }

                holder.recyclerOthers.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                holder.recyclerOthers.setAdapter(new StoryViewAdapter(matchModel.getStatusUserListModels(),mContext,(list,pos) -> {
                    matchListener.onStatusClick(list,pos);
                }));
        }
        else if (getItemViewType(position)==6){
            TradingHolder holder=(TradingHolder) viewHolder;
            if (TextUtils.isEmpty(matchModel.getMatchTitle())){
                holder.txtLbl.setVisibility(View.GONE);
            }
            else {
                holder.txtLbl.setVisibility(View.VISIBLE);
                holder.txtLbl.setText(matchModel.getMatchTitle());
            }

            holder.recyclerOthers.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
            TournamentMatchAdapter tournamentMatchAdapter=new TournamentMatchAdapter(mContext,matchModel.getOtherMatchList(),model -> {
                matchListener.onMatchClick(model);
            });
            holder.recyclerOthers.setAdapter(tournamentMatchAdapter);
        }
        else {

            MatchViewHolder holder=(MatchViewHolder)viewHolder;

            Date date = null;
            String matchDate = "";

            if (!TextUtils.isEmpty(matchModel.getTeamAXi()) && !TextUtils.isEmpty(matchModel.getTeamBXi())){
                if ( matchModel.getTeamAXi().equalsIgnoreCase("Yes") ||
                        matchModel.getTeamBXi().equalsIgnoreCase("Yes")) {
                    holder.layLine.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(R.drawable.go)
                            .into(holder.lineout);
                } else {
                    holder.layLine.setVisibility( View.GONE);
                }
            }else {
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

            CustomUtil.loadImageWithGlide(mContext,holder.team1_img, ApiManager.TEAM_IMG,matchModel.getTeam1Img(),R.drawable.ic_team1_placeholder);
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

    }

    public interface MatchListener{
        public void onStatusClick(ArrayList<StatusUserListModel> list, int position);
        public void onBannerClick(BannerModel bannerModel);
        public void onMatchClick(MatchModel matchModel);
        public void onTradeClick(EventModel eventModel);
    }

    private class TopAdsAutoScroll extends TimerTask {

        private ViewPager2 home_offer;
        private int size;

        public TopAdsAutoScroll(ViewPager2 home_offer,int size) {
            this.home_offer = home_offer;
            this.size = size;
        }

        @Override
        public void run() {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    if (pageTopAds > size - 1) {
                        pageTopAds = 0;
                    } else {
                        home_offer.setCurrentItem(pageTopAds++);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return matchModelList.size();
    }

    public boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
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
           // txtSingles = itemView.findViewById(R.id.txtSingles);
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

    protected class TradingHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerOthers;
        private TextView txtLbl;
        public TradingHolder(@NonNull View itemView) {
            super(itemView);
            txtLbl=itemView.findViewById(R.id.txtLbl);
            txtLbl.setVisibility(View.VISIBLE);

            recyclerOthers=itemView.findViewById(R.id.recyclerOthers);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerOthers);
        }
    }

    protected class NoMatchHolder extends RecyclerView.ViewHolder{

        public NoMatchHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected class TitleHolder extends RecyclerView.ViewHolder{
        private TextView txtLbl;
        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            txtLbl=itemView.findViewById(R.id.txtLbl);
        }
    }

    protected class BannerHolder extends RecyclerView.ViewHolder{

        private ViewPager2 home_offer;
        private Timer timerTopAds;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            home_offer=itemView.findViewById(R.id.home_offer);
        }
    }
}
