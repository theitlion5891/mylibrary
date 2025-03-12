package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Activity.LeaderBoardActivity;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.fantafeat.util.WinningTreeSheet;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinLeagueAdapter extends RecyclerView.Adapter<JoinLeagueAdapter.Holder> {

    private Context mContext;
    private List<ContestModel.ContestDatum> contestDatumList;
    private MyPreferences preferences;
    private onDownload listner;
    private Animation animation;

    public JoinLeagueAdapter(Context mContext, List<ContestModel.ContestDatum> contestDatumList,onDownload listner) {
        this.mContext = mContext;
        this.contestDatumList = contestDatumList;
        this.preferences = MyApp.getMyPreferences();
        this.listner=listner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.row_after_join, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ContestModel.ContestDatum list = contestDatumList.get(holder.getAdapterPosition());
        /*MatchModel matchModel=preferences.getMatchModel();
        matchModel.setMatchStatus("completed");
        preferences.setMatchModel(matchModel);
        list.setConEntryStatus("cancel");*/
        if (preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {

            holder.txtCanceled.setVisibility(View.VISIBLE);
            holder.viewline.setVisibility(View.VISIBLE);
            holder.txtCanceled.setText("Cancelled");
            /*if (list.getNoTeamJoin().equals(list.getJoinedTeams())){
                holder.txtCanceled.setText("Same Points");//This match is cancelled
            }else {
                holder.contest_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
            }*/
            holder.contest_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
            holder.winning.setVisibility(View.GONE);
            //holder.contest_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
        }

        if (list.getConEntryStatus().equalsIgnoreCase("cancel")) {

            holder.txtCanceled.setVisibility(View.VISIBLE);
            holder.viewline.setVisibility(View.VISIBLE);
            if (list.getNoTeamJoin().equalsIgnoreCase(list.getJoinedTeams())){
                holder.txtCanceled.setText("Same Points");
            }else {
                holder.txtCanceled.setText("Cancelled");
                holder.contest_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
            }
            holder.winning.setVisibility(View.GONE);
            //holder.match_sub_card.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
        } else {
            holder.contest_card.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
            holder.txtCanceled.setVisibility(View.GONE);
            holder.viewline.setVisibility(View.GONE);
            holder.winning.setVisibility(View.VISIBLE);
            //holder.match_sub_card.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
        }

        if (list.getEntryFee().equalsIgnoreCase("0")){
            holder.contest_entry.setText("FREE");
        }else {
            holder.contest_entry.setText(mContext.getResources().getString(R.string.rs) + list.getEntryFee());
        }

        holder.rank.setText(list.getTotalRank());

        holder.contest_card.setClickable(true);
        int totalSpots = CustomUtil.convertInt(list.getNoTeamJoin());
        int jointeam = CustomUtil.convertInt(list.getJoinedTeams());

        int progress = (jointeam * 100) / totalSpots;
        holder.contest_progress.setProgress(progress);
        holder.after_points.setText("P : "+list.getTotalPoint());

        if (list.getIs_flexi().equalsIgnoreCase("yes")){
            holder.imgConfirm.setVisibility(View.VISIBLE);
            holder.imgConfirm.setImageResource(R.drawable.ic_flext_join);
        }else {
            if (list.getConEntryStatus().equalsIgnoreCase("Unconfirmed")) {
                holder.imgConfirm.setVisibility(View.GONE);
            } else {
                if (preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
                    holder.imgConfirm.setVisibility(View.GONE);
                } else if (list.getConEntryStatus().equalsIgnoreCase("cancel")) {
                    holder.imgConfirm.setVisibility(View.GONE);
                } else {
                    holder.imgConfirm.setVisibility(View.VISIBLE);
                    holder.imgConfirm.setImageResource(R.drawable.ic_confirm_contest);
                }
            }
        }

        if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
          //  holder.contest_multiple.setText("S");
            holder.imgSingle.setImageResource(R.drawable.ic_single_join);
        } /*else  if (list.getConPlayerEntry().equalsIgnoreCase("flexi")) {
            holder.contest_multiple.setText("F");
        }*/
        else {
           // holder.contest_multiple.setText("M");
            holder.imgSingle.setImageResource(R.drawable.ic_multiple_join);
        }

        if (list.getMyJoinedTeam().equalsIgnoreCase("1")){
            holder.myTeamCnt.setText(list.getMyJoinedTeam() +" team");
        }else {
            holder.myTeamCnt.setText(list.getMyJoinedTeam() +" teams");
        }

        if (list.getIsUnlimited().equalsIgnoreCase("Yes")) {
            holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
            holder.teams_left_linear.setVisibility(View.VISIBLE);
            holder.contest_full_linear.setVisibility(View.GONE);
            holder.contest_left_team.setText(jointeam);
            holder.contest_left_spots_text.setText("Joined teams");
            holder.contest_total_team.setText("Unlimited");
            holder.contest_total_spots_text.setText("Spots");

            float cal=(CustomUtil.convertFloat(list.getJoinedTeams())*CustomUtil.convertFloat(list.getEntryFee()))
                    -((CustomUtil.convertFloat(list.getJoinedTeams())*CustomUtil.convertFloat(list.getEntryFee())*CustomUtil.convertFloat(list.getCommission()))/100);

            holder.contest_price_pool.setText(CustomUtil.displayAmount(mContext, (int)cal+""));
            holder.contest_total_winner.setText(list.getTotalWinner()+" %");
        }
        else {

            holder.contest_price_pool.setText(CustomUtil.displayAmount(mContext, list.getDistributeAmount()));
            holder.contest_total_winner.setText(list.getTotalWinner());

            int spotsLeft = totalSpots - jointeam;
            if(spotsLeft <= 0){
                //holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
               // holder.teams_left_linear.setVisibility(View.GONE);
                holder.teams_left_linear.setVisibility(View.VISIBLE);
                holder.contest_left_team.setText("" + jointeam);//spotsLeft
                //holder.contest_full_linear.setVisibility(View.VISIBLE);
                holder.contest_full_linear.setVisibility(View.GONE);
                holder.contest_total_team.setText(list.getNoTeamJoin());
            }else {
                //holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                holder.teams_left_linear.setVisibility(View.VISIBLE);
                holder.contest_full_linear.setVisibility(View.GONE);
                holder.contest_left_team.setText("" +jointeam );//spotsLeft
                holder.contest_left_spots_text.setText("Joined teams");
                holder.contest_total_team.setText(list.getNoTeamJoin());
                holder.contest_total_spots_text.setText("Spots");
            }
        }
       // holder.contest_bonus.setText(list.getUseBonus() + "%");
        if (list.getIsOffer() != null &&
                list.getIsOffer().equalsIgnoreCase("Yes")) {
            holder.contest_offer_price.setVisibility(View.VISIBLE);
            holder.contest_offer_price.setText(list.getOfferPrice());
            //holder.contest_offer_text.setText(list.getOfferText());
            holder.contest_offer_price.setPaintFlags(holder.contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.contest_offer_price.setVisibility(View.GONE);
        }

        if(preferences.getMatchModel().getMatchStatus().toLowerCase().equals("completed")){
            if(CustomUtil.convertFloat(list.getTotal_win_amount())>0) {
                holder.winning.setVisibility(View.VISIBLE);
                holder.winning.setTextColor(mContext.getResources().getColor(R.color.green_pure));
                holder.winning.setText("\nWon : " + list.getTotal_win_amount());
            }else {
                holder.winning.setVisibility(View.GONE);
            }
            /*if(CustomUtil.convertFloat(list.getTotal_win_amount())!=0) {
                holder.winning.setText("\nWon : " + list.getTotal_win_amount());
                holder.winning.setTextColor(mContext.getResources().getColor(R.color.green));
            }else{
                holder.winning.setText("\nWon : " + list.getTotal_win_amount());
            }*/
        }
        else if(preferences.getMatchModel().getMatchStatus().toLowerCase().equals("Cancelled") ){// || status.equalsIgnoreCase("In review")
            holder.winning.setText("");
        }
        else{
            if(CustomUtil.convertFloat(list.getTotal_win_amount()) > 0){
                holder.winning.setText("◉ In Winning");
                holder.winning.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            }else{
                holder.winning.setText("");
            }
        }

        if (CustomUtil.convertInt(list.getMyJoinedTeam()) < CustomUtil.convertInt(list.getMaxTeamBonusUse())) {
            /*if (CustomUtil.convertInt(list.getUseBonus()) > 0) {
               // holder.bonus_linear.setBackground(mContext.getResources().getDrawable(R.drawable.contest_type_border_red));
                holder.contest_bonus.setTextColor(mContext.getResources().getColor(R.color.white_font));
                holder.contest_bonus.setBackground(mContext.getResources().getDrawable(R.drawable.red_fill_curve));
            } else {
             //   holder.bonus_linear.setBackground(mContext.getResources().getDrawable(R.drawable.contest_type_border_red));
                holder.contest_bonus.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.contest_bonus.setBackground(mContext.getResources().getDrawable(R.drawable.light_grey_fill_curve));
            }*/
            if (!TextUtils.isEmpty(list.getUseBonus()) && CustomUtil.convertFloat(list.getUseBonus())>0){
                holder.imgBonus.setVisibility(View.VISIBLE);
            }else {
                holder.imgBonus.setVisibility(View.GONE);
            }
            //holder.contest_bonus.setText(list.getUseBonus() + "%");
        }
        else {
            /*if (CustomUtil.convertInt(list.getDefaultBonus()) > 0) {
             //   holder.bonus_linear.setBackground(mContext.getResources().getDrawable(R.drawable.contest_type_border_red));
                holder.contest_bonus.setTextColor(mContext.getResources().getColor(R.color.white_font));
                holder.contest_bonus.setBackground(mContext.getResources().getDrawable(R.drawable.red_fill_curve));
            } else {
             //   holder.bonus_linear.setBackground(mContext.getResources().getDrawable(R.drawable.contest_type_border_red));
                holder.contest_bonus.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.contest_bonus.setBackground(mContext.getResources().getDrawable(R.drawable.light_grey_fill_curve));
            }*/
            if (!TextUtils.isEmpty(list.getDefaultBonus()) && CustomUtil.convertFloat(list.getDefaultBonus())>0){
                holder.imgBonus.setVisibility(View.VISIBLE);
            }else {
                holder.imgBonus.setVisibility(View.GONE);
            }
           // holder.contest_bonus.setText(list.getDefaultBonus() + "%");
        }

        if (list.getIs_lb().equalsIgnoreCase("yes")){
            holder.txtLeaderboard.setVisibility(View.VISIBLE);
            holder.txtLeaderboard.setAnimation(animation);
        }else {
            holder.txtLeaderboard.setVisibility(View.GONE);
        }

        holder.txtLeaderboard.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
               /* mContext.startActivity(new Intent(mContext, UserLeaderboardActivity.class)
                        .putExtra("leaderboard_id",list.getLb_id()));*/
            }
        });

        holder.contest_tree_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (list.getIsUnlimited().equalsIgnoreCase("No")) {
                        LogUtil.d("resp",list.getWinningTree()+" ");
                        WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, list.getDistributeAmount(), list.getWinningTree(),holder.contest_price_pool.getText().toString());
                        bottomSheetTeam.show(((AfterMatchActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else {
                        CustomUtil.showTopSneakWarning(mContext,"Price Pool will appear after match starts.");
                    }
                }
            }
        });

        holder.contest_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("Cancelled")) {
                    if (!list.getConEntryStatus().equalsIgnoreCase("cancel")) {
                        Gson gson = new Gson();
                        Intent intent = new Intent(mContext, LeaderBoardActivity.class);
                        String model = gson.toJson(list);
                        intent.putExtra("model",model);
                        mContext.startActivity(intent);
                    }else {
                        if (list.getNoTeamJoin().equals(list.getJoinedTeams())){
                            //CustomUtil.showTopSneakError(mContext,"This contest is cancelled due to clashes");
                            Gson gson = new Gson();
                            Intent intent = new Intent(mContext, LeaderBoardActivity.class);
                            String model = gson.toJson(list);
                            intent.putExtra("model",model);
                            intent.putExtra("is_same_team_cancel",true);
                            mContext.startActivity(intent);
                        }else {
                            CustomUtil.showTopSneakError(mContext,"This contest is cancelled due to unfilled");
                        }
                    }
                }else {
                    CustomUtil.showTopSneakError(mContext,"This match is cancelled");
                }
            }
        });

        holder.excel_download.setOnClickListener(view -> {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            try {
                if (preferences.getPrefBoolean(PrefConstant.MATCH_TYPE)) {
                    date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
                } else {
                    date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date.after(MyApp.getCurrentDate())) {
                CustomUtil.showTopSneakError(mContext, "Match is not started yet!");
            } else {
                //downloadExcel(contestData.getId());
                listner.onClick(position);
            }
        });

        holder.imgConfirm.setOnClickListener(v->{
            /*if (list.getIs_flexi().equalsIgnoreCase("yes")){
                ViewTooltip
                        .on((Activity)  mContext, holder.imgConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("The event will take place even if only two spots are filled, Prize pool varies based on entries.")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }else {
                ViewTooltip
                        .on((Activity)  mContext, holder.imgConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Guaranteed winning even if contest remains unfilled")//Confirmed winning even if contest remains unfilled
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }*/

        });

        holder.imgSingle.setOnClickListener(v->{
            /*if (list.getConPlayerEntry().equalsIgnoreCase("Single")){
                ViewTooltip
                        .on((Activity)  mContext, holder.imgSingle)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Only one Team allowed")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }else {
                ViewTooltip
                        .on((Activity)  mContext, holder.imgSingle)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Join with multiple Teams")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }*/

        });

        holder.imgBonus.setOnClickListener(v->{
            String txt="Bonus Contest";
            if (CustomUtil.convertFloat(list.getDefaultBonus())>0){
                txt=list.getDefaultBonus()+"% Bonus Contest";
            }
            /*ViewTooltip
                    .on((Activity)  mContext, holder.imgBonus)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(RIGHT)
                    .text(txt)
                    .textColor(Color.WHITE)
                    .color( mContext.getResources().getColor(R.color.blackPrimary))
                    .show();*/
        });

    }

    public void updateData(List<ContestModel.ContestDatum> list){
        //this.contestDatumList.clear();
        this.contestDatumList=list;
        notifyDataSetChanged();
    }


    public interface onDownload{
        public void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return contestDatumList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView contest_price_pool, contest_total_winner, contest_entry, contest_left_team, contest_total_team,winning,/*contest_bonus,*/myTeamCnt,/*contest_bonus_b,*/
                contest_left_spots_text,txtLeaderboard, contest_offer_price, contest_total_spots_text,/*contest_confirm,contest_multiple,*/rank,after_points,txtCanceled;
        ProgressBar contest_progress;
        CardView contest_card;
        View viewline;
        LinearLayout contest_full_linear,teams_left_linear,join_btn,contest_tree_layout/*,layBonus*/;
        ImageView excel_download,imgBonus,imgConfirm,imgSingle;

        public Holder(@NonNull View itemView) {
            super(itemView);

            contest_price_pool = itemView.findViewById(R.id.contest_price_pool);
            contest_total_winner = itemView.findViewById(R.id.contest_winner);
            contest_entry = itemView.findViewById(R.id.contest_entry_fee);
            contest_left_team = itemView.findViewById(R.id.contest_left_team);
            contest_total_team = itemView.findViewById(R.id.contest_total_team);
            contest_progress = itemView.findViewById(R.id.contest_progress);
            contest_left_spots_text = itemView.findViewById(R.id.contest_left_text);
            contest_total_spots_text = itemView.findViewById(R.id.contest_total_text);
            contest_card = itemView.findViewById(R.id.main_card);
            contest_offer_price = itemView.findViewById(R.id.contest_offer_price);
            imgBonus = itemView.findViewById(R.id.imgBonus);
            imgConfirm = itemView.findViewById(R.id.imgConfirm);
            imgSingle = itemView.findViewById(R.id.imgSingle);
            //contest_confirm = itemView.findViewById(R.id.contest_confirm);
            //contest_multiple = itemView.findViewById(R.id.contest_multiple);
            contest_full_linear = itemView.findViewById(R.id.contest_full_linear);
            teams_left_linear = itemView.findViewById(R.id.teams_left_linear);
            excel_download = itemView.findViewById(R.id.excel_download);
            winning = itemView.findViewById(R.id.winning);
            join_btn = itemView.findViewById(R.id.join_btn);
            rank = itemView.findViewById(R.id.rank);
            after_points = itemView.findViewById(R.id.after_points);
            contest_tree_layout = itemView.findViewById(R.id.contest_tree_layout);
            //contest_bonus = itemView.findViewById(R.id.contest_bonus);
            myTeamCnt = itemView.findViewById(R.id.myTeamCnt);
           // contest_bonus_b = itemView.findViewById(R.id.contest_bonus_b);
            txtCanceled = itemView.findViewById(R.id.txtCanceled);
            viewline = itemView.findViewById(R.id.viewline);
            //layBonus = itemView.findViewById(R.id.layBonus);

            txtLeaderboard = itemView.findViewById(R.id.txtLeaderboard);
            animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_to_original);

        }
    }
}
