package com.fantafeat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.AffilationMatchList;
import com.fantafeat.Model.AffiliationDetailModel;
import com.fantafeat.Model.AffiliationMatchModal;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AffiliationMatchAdapter extends RecyclerView.Adapter<AffiliationMatchAdapter.ViewHolder> {

    private List<AffiliationMatchModal> list;
    private Context context;
    long diff;

    public AffiliationMatchAdapter(Context context,List<AffiliationMatchModal> affiliationDetailModelList) {
        this.list = affiliationDetailModelList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.affiliation_match_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        AffiliationMatchModal modal = list.get(position);

        holder.team1_name.setText(modal.getTeam1Sname());
        holder.team2_name.setText(modal.getTeam2Sname());
        holder.team1_full_name.setText(modal.getTeam1Name());
        holder.team2_full_name.setText(modal.getTeam2Name());
        holder.match_title.setText(modal.getMatchTitle());

        if (modal.getSportId().equals("1")){
            holder.txtSports.setText("Cricket");
        }else if (modal.getSportId().equals("2")){
            holder.txtSports.setText("Football");
        }else if (modal.getSportId().equals("4")){
            holder.txtSports.setText("Baseball");
        }else if (modal.getSportId().equals("6")){
            holder.txtSports.setText("Handball");
        }else if (modal.getSportId().equals("3")){
            holder.txtSports.setText("Basketball");
        }else if (modal.getSportId().equals("7")){
            holder.txtSports.setText("Kabaddi");
        }


        CustomUtil.loadImageWithGlide(context,holder.team1_img, ApiManager.TEAM_IMG,modal.getTeam1Img(),R.drawable.ic_team1_placeholder);
        CustomUtil.loadImageWithGlide(context,holder.team2_img,ApiManager.TEAM_IMG,modal.getTeam2Img(),R.drawable.ic_team2_placeholder);

        Date date = null;
        String matchDate = "";
        String matchTime = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
        SimpleDateFormat matchTimeFormate = MyApp.changedFormat("hh:mm a");

        try {
            date = format.parse(modal.getRegularMatchStartTime());

            matchTime = matchTimeFormate.format(date);
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

        }
        catch (ParseException e) {

            e.printStackTrace();
        }
        String strDate=matchDate + " <b>" + matchTime+"</b>";
        holder.match_timer.setText(Html.fromHtml(strDate));

        if (TextUtils.isEmpty(modal.getTotalWinAmount())){
            String invest="Total affiliation: <b>" + context.getResources().getString(R.string.rs)+"0";
            holder.txtMarchInvest.setText(Html.fromHtml(invest));
        }else {
            String invest="Total affiliation: <b>" + context.getResources().getString(R.string.rs)+modal.getTotalWinAmount()+"</b>";
            holder.txtMarchInvest.setText(Html.fromHtml(invest));
        }

        holder.match_card.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                ((AffilationMatchList)context).matchItemClick(modal);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<AffiliationMatchModal> affiliationDetailModelList) {
        this.list = affiliationDetailModelList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView match_title,txtSports,team1_name,match_timer,team2_name,team1_full_name,team2_full_name,txtMarchInvest;
        private CircleImageView team1_img,team2_img;
        private CardView match_card;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            match_title=itemView.findViewById(R.id.match_title);
            txtSports=itemView.findViewById(R.id.txtSports);
            team1_name=itemView.findViewById(R.id.team1_name);
            team2_name=itemView.findViewById(R.id.team2_name);
            match_timer=itemView.findViewById(R.id.match_timer);
            team1_full_name=itemView.findViewById(R.id.team1_full_name);
            team2_full_name=itemView.findViewById(R.id.team2_full_name);
            txtMarchInvest=itemView.findViewById(R.id.txtMarchInvest);
            team1_img=itemView.findViewById(R.id.team1_img);
            team2_img=itemView.findViewById(R.id.team2_img);
            match_card=itemView.findViewById(R.id.match_card);
        }
    }
}
