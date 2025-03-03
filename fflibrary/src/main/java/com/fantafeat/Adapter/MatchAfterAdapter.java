package com.fantafeat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.AfterMatchActivity;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MatchAfterAdapter extends RecyclerView.Adapter<MatchAfterAdapter.MatchAfterHolder> {

    private Context mContext;
    private List<MatchModel> matchAfterModelList;
    private MyPreferences preferences;
    private static final String TAG = "MatchAfterAdapter";
    long diff;

    public MatchAfterAdapter(Context mContext, List<MatchModel> matchAfterModelList) {
        this.mContext = mContext;
        this.matchAfterModelList = matchAfterModelList;
        preferences = MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public MatchAfterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchAfterHolder(LayoutInflater.from(mContext).inflate(R.layout.row_match, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchAfterHolder holder, int position) {
        final MatchModel matchModel = matchAfterModelList.get(holder.getAdapterPosition());

        if (CustomUtil.convertInt(matchModel.getMatchSquad()) == 0) {
            holder.cardView.setClickable(false);
            holder.cardView.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
        } else {
            holder.cardView.setClickable(true);
            holder.cardView.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
        }

        String matchDate = "";
        String matchTime = "";
        Date date = null;

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        SimpleDateFormat matchTimeFormate = MyApp.changedFormat("hh:mm a");

        DecimalFormat numFormat=new DecimalFormat("00.00");

        String totalEntryFee=matchModel.getTotalEntryFees();
        String totalWinAmt=matchModel.getTotalWinAmount();

        if(matchModel.getMatchStatus().equalsIgnoreCase("live")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_red_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#b20017"));
            holder.match_timer.setText("Live");
            String invst="",winAmt="";
            if (TextUtils.isEmpty(totalEntryFee)){
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalEntryFee))+
                        "</b></font>";
            }

            if (TextUtils.isEmpty(totalWinAmt)){
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalWinAmt))+
                        "</b></font>";
            }

            holder.txtInvest.setText(Html.fromHtml(invst));
            holder.txtEarn.setText(Html.fromHtml(winAmt));

            holder.layDate.setVisibility(View.VISIBLE);

        }
        else if(matchModel.getMatchStatus().equalsIgnoreCase("in review")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_orange_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#FFFE8620"));
            holder.match_timer.setText("In Review");

            holder.layDate.setVisibility(View.VISIBLE);

            String invst="",winAmt="";
            if (TextUtils.isEmpty(totalEntryFee)){
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalEntryFee))+
                        "</b></font>";
            }

            if (TextUtils.isEmpty(totalWinAmt)){
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalWinAmt))+
                        "</b></font>";
            }
            holder.txtInvest.setText(Html.fromHtml(invst));
            holder.txtEarn.setText(Html.fromHtml(winAmt));
        }
        else if(matchModel.getMatchStatus().equalsIgnoreCase("completed")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_green_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#39ad5e"));
            holder.match_timer.setText("Completed");

            holder.layDate.setVisibility(View.VISIBLE);

            String invst="",winAmt="";
            if (TextUtils.isEmpty(totalEntryFee)){
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalEntryFee))+
                        "</b></font>";
            }

            if (TextUtils.isEmpty(totalWinAmt)){
                winAmt="Won : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                winAmt="Won : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalWinAmt))+
                        "</b></font>";
            }
            holder.txtInvest.setText(Html.fromHtml(invst));
            holder.txtEarn.setText(Html.fromHtml(winAmt));
        }
        else if(matchModel.getMatchStatus().equalsIgnoreCase("Stopped")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_red_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#b20017"));
            holder.match_timer.setText("Stopped");
            holder.layInvest.setVisibility(View.GONE);

            holder.layDate.setVisibility(View.VISIBLE);
        }
        else if(matchModel.getMatchStatus().equalsIgnoreCase("cancelled")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_red_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#b20017"));
            holder.match_timer.setText("Cancelled");
            holder.layInvest.setVisibility(View.GONE);

            holder.layDate.setVisibility(View.VISIBLE);
        }
        else if(matchModel.getMatchStatus().equalsIgnoreCase("playing")){
            holder.match_timer.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(R.drawable.ic_red_dot), null,null , null);
            holder.match_timer.setTextColor(Color.parseColor("#b20017"));
            holder.match_timer.setText("Playing");


            holder.layDate.setVisibility(View.VISIBLE);

            String invst="",winAmt="";
            if (TextUtils.isEmpty(totalEntryFee)){
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                invst="Investment : <font color=#B20017><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalEntryFee))+
                        "</b></font>";
            }

            if (TextUtils.isEmpty(totalWinAmt)){
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+"0"+
                        "</b></font>";
            }else {
                winAmt="Approx Earning : <font color=#39AD5E><b>"+
                        mContext.getResources().getString(R.string.rs)+numFormat.format(CustomUtil.convertFloat(totalWinAmt))+
                        "</b></font>";
            }
            holder.txtInvest.setText(Html.fromHtml(invst));
            holder.txtEarn.setText(Html.fromHtml(winAmt));
        }
        holder.match_timer.setCompoundDrawablePadding(5);

        try {
            date = format.parse(matchModel.getRegularMatchStartTime());

            matchTime = matchTimeFormate.format(date);
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

        }
        catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }
        String strDate=matchDate + " <b>" + matchTime+"</b>";
        holder.match_dates.setText(Html.fromHtml(strDate));

        holder.team1_name.setText(matchModel.getTeam1Sname());
        holder.team2_name.setText(matchModel.getTeam2Sname());
        holder.team1_full_name.setText(matchModel.getTeam1Name());
        holder.team2_full_name.setText(matchModel.getTeam2Name());
        holder.match_title.setText(matchModel.getMatchTitle());



        CustomUtil.loadImageWithGlide(mContext,holder.team1_img,ApiManager.TEAM_IMG,matchModel.getTeam1Img(),R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(mContext,holder.team2_img,ApiManager.TEAM_IMG,matchModel.getTeam2Img(),R.drawable.team_loading);

       /* if (matchModel.getTeam1Img().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.team_loading)
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team1_img);
        } else if (URLUtil.isValidUrl(matchModel.getTeam1Img())) {
            Glide.with(mContext)
                    .load(matchModel.getTeam1Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team1_img);
        } else {
            Glide.with(mContext)
                    .load(ApiManager.TEAM_IMG + matchModel.getTeam1Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team1_img);
        }

        if (matchModel.getTeam2Img().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.team_loading)
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team2_img);
        } else if (URLUtil.isValidUrl(matchModel.getTeam2Img())) {
            Glide.with(mContext)
                    .load(matchModel.getTeam2Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team2_img);
        } else {
            Glide.with(mContext)
                    .load(ApiManager.TEAM_IMG + matchModel.getTeam2Img())
                    .placeholder(R.drawable.team_loading)
                    .error(R.drawable.team_loading)
                    .into(holder.team2_img);
        }*/

        if (TextUtils.isEmpty(matchModel.getIs_single())){
            matchModel.setIs_single("no");
        }

        if (TextUtils.isEmpty(matchModel.getIs_fifer())){
            matchModel.setIs_fifer("no");
        }

        if (!TextUtils.isEmpty(matchModel.getMatchDesc()) ||
                !TextUtils.isEmpty(matchModel.getMega_text()) ||
                matchModel.getTeam1Color().equalsIgnoreCase("yes") ||
                matchModel.getIs_single().equalsIgnoreCase("yes") ||
                matchModel.getIs_fifer().equalsIgnoreCase("yes")
        ){
            holder.layDesc.setVisibility(View.VISIBLE);
        }else {
            holder.layDesc.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(matchModel.getMatchDesc())){
            //holder.layDesc.setVisibility(View.GONE);
            holder.txtMatchDesc.setVisibility(View.INVISIBLE);
        }else {
            //holder.layDesc.setVisibility(View.VISIBLE);
            holder.txtMatchDesc.setVisibility(View.VISIBLE);
            holder.txtMatchDesc.setText(matchModel.getMatchDesc());
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

        }*/

        /*holder.txtFifer.setOnClickListener(view -> {
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
                //mContext.startActivity(new Intent(mContext, UserLeaderboardActivity.class));
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

        holder.card_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMatchModel(matchModel);
                Intent intent = new Intent(mContext, AfterMatchActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return matchAfterModelList.size();
    }

    protected class MatchAfterHolder extends RecyclerView.ViewHolder {
        TextView match_lineups_out, team1_name, match_title, match_timer, team2_name,team1_full_name,team2_full_name,
                txtMatchDesc,txtInvest,txtEarn,txtGrandText,lblGrand,match_dates/*,txtSingles,txtFifer*/;
        ImageView team2_img, team1_img,imgLeader;
        LinearLayout card_linear,layLine,layDate,layInvest,layDesc;
        CardView cardView;
        CountDownTimer timer;

        public MatchAfterHolder(@NonNull View itemView) {
            super(itemView);
            card_linear = itemView.findViewById(R.id.card_linear);
            cardView = itemView.findViewById(R.id.match_card);
            match_lineups_out = itemView.findViewById(R.id.match_lineups_out);
            txtEarn = itemView.findViewById(R.id.txtEarn);
            txtInvest = itemView.findViewById(R.id.txtInvest);
            layDate = itemView.findViewById(R.id.layDate);
            layLine = itemView.findViewById(R.id.layLine);
            layLine.setVisibility(View.GONE);
            layInvest = itemView.findViewById(R.id.layInvest);
            layInvest.setVisibility(View.VISIBLE);
            match_dates = itemView.findViewById(R.id.match_dates);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            match_title = itemView.findViewById(R.id.match_title);
            match_timer = itemView.findViewById(R.id.match_timer);
            team2_img = itemView.findViewById(R.id.team2_img);
            team1_img = itemView.findViewById(R.id.team1_img);
            lblGrand = itemView.findViewById(R.id.lblGrand);
            lblGrand.setVisibility(View.GONE);
            txtGrandText = itemView.findViewById(R.id.txtGrandText);
            txtGrandText.setVisibility(View.GONE);
            team1_full_name = itemView.findViewById(R.id.team1_full_name);
            team2_full_name = itemView.findViewById(R.id.team2_full_name);
            layDesc = itemView.findViewById(R.id.layDesc);
            txtMatchDesc = itemView.findViewById(R.id.txtMatchDesc);
            txtMatchDesc.setSelected(true);
            imgLeader = itemView.findViewById(R.id.imgLeader);
            //txtSingles = itemView.findViewById(R.id.txtSingles);
            //txtFifer = itemView.findViewById(R.id.txtFifer);
          //  layLeaderboard = itemView.findViewById(R.id.layLeaderboard);
        }
    }

    public void updateData(List<MatchModel> matchAfterModelList) {
        this.matchAfterModelList = matchAfterModelList;
        notifyDataSetChanged();
    }
}
