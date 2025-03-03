package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Fragment.LeaderBoardFragment;
import com.fantafeat.Model.LeaderBoardModel;
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

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder> {

    private Context mContext;
    private List<LeaderBoardModel> leaderBoardModelList;
    MyPreferences preferences;
    private Fragment fragment;
    private Boolean is_same_team_cancel;
    private static final String TAG = "LeaderBoardAdapter";
    int joined_teams;

    public LeaderBoardAdapter(Context mContext, List<LeaderBoardModel> leaderBoardModelList, Fragment fragment,Boolean is_same_team_cancel) {
        this.mContext = mContext;
        this.leaderBoardModelList = leaderBoardModelList;
        this.is_same_team_cancel = is_same_team_cancel;
        this.fragment = fragment;
        preferences = MyApp.getMyPreferences();
        joined_teams = CustomUtil.convertInt(((LeaderBoardFragment) fragment).contestData.getMyJoinedTeam());
    }

    @NonNull
    @Override
    public LeaderBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeaderBoardHolder(LayoutInflater.from(mContext).inflate(R.layout.row_match_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaderBoardHolder holder, int position) {
        final LeaderBoardModel list = leaderBoardModelList.get(holder.getAdapterPosition());

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        Date MatchDate = null;
        try {
            MatchDate = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "onBindViewHolder: 3"+ preferences.getMatchModel().getMatchStatus());

        int age=18;
        if (!TextUtils.isEmpty(list.getDob()) && !list.getDob().equals("0000-00-00")){
            age=CustomUtil.getAge(list.getDob());
        }
        //Log.d("agereap",age+" ");
        if (TextUtils.isEmpty(list.getUser_img())) {
            if (list.getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    holder.leader_img.setImageResource(R.drawable.male_18_below);
                }else if (age>25 && age<=40){
                    holder.leader_img.setImageResource(R.drawable.male_25_above);
                }else {
                    holder.leader_img.setImageResource(R.drawable.male_40_above);
                }
            }else {
                if (age>=18 && age <= 25){
                    holder.leader_img.setImageResource(R.drawable.female_18_below);
                }else if (age>25 && age<=40){
                    holder.leader_img.setImageResource(R.drawable.female_25_above);
                }else {
                    holder.leader_img.setImageResource(R.drawable.female_40_above);
                }
            }
        }
        else {
            if (list.getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.male_18_below);
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.male_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.male_40_above);
                }
            }else {
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.female_18_below);
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.female_25_above);
                }else {
                    CustomUtil.loadImageWithGlide(mContext,holder.leader_img,ApiManager.PROFILE_IMG,list.getUser_img(),R.drawable.female_40_above);
                }
            }
        }

         if (preferences.getMatchModel().getMatchStatus().equals("Completed")) {
            holder.leader_point.setVisibility(View.VISIBLE);
        } else {

             holder.leader_point.setVisibility(View.GONE);
        }

        if(((LeaderBoardFragment) fragment).compare_on == false) {
            if (list.getUserId().equals(preferences.getUserModel().getId())) {
                holder.leader_layout.setBackgroundResource(R.color.selectGreen);
                holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));//colorPrimary
                holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
            } else {
                holder.leader_layout.setBackgroundResource(R.color.pureWhite);
                holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));
                holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
            }
            holder.leader_name.setText(list.getTempTeamName());
        }
        else{
            holder.leader_layout.setBackgroundResource(R.color.pureWhite);
            holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
            holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));
            holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
            holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
            if (list.isSelected()){
                LogUtil.e(TAG, "onBindViewHolder: 3" );
                holder.leader_layout.setBackgroundResource(R.color.selectGreen);
                holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));
                holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
            /*holder.leader_img.setBorderWidth(4);
            holder.leader_img.setBorderColor(mContext.getResources().getColor(R.color.colorPrimary));*/
            }else {
                LogUtil.e(TAG, "onBindViewHolder: 2" );
                holder.leader_layout.setBackgroundResource(R.color.pureWhite);
                holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));
                holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
                //holder.leader_img.setBorderWidth(0);
            }
            holder.leader_name.setText(list.getTempTeamName());
        }



        if (preferences.getMatchModel().getMatchStatus().equals("Pending")) {
            holder.leader_winning.setText("-");
        }else {
            holder.leader_winning.setText(list.getTotalPoint());//(CustomUtil.getFormater("00").format(CustomUtil.convertFloat(list.getTotalPoint())));
        }
        if (MatchDate.after(MyApp.getCurrentDate())) {
            //if (list.getUserId().equals(preferences.getUserModel().getId()) && no_Join != MatchData.teamLists.size()) {


            if (list.getUserId().equals(preferences.getUserModel().getId()) &&
                    preferences.getPlayerModel()!=null && joined_teams < preferences.getPlayerModel().size()) {
                holder.switchTeam.setVisibility(View.VISIBLE);
                holder.leader_rank.setVisibility(View.GONE);
            } else {
                holder.switchTeam.setVisibility(View.GONE);
                holder.leader_rank.setVisibility(View.VISIBLE);
            }
            holder.leader_rank.setText("-");
           // holder.leader_point.setText("Points: 0");
           // holder.leader_point.setText("Won: "+mContext.getResources().getString(R.string.rs)+"0");
            holder.leader_point.setVisibility(View.GONE);
        }
        else {
            //holder.leader_point.setText("Points: " + list.getTotalPoint());
            if (Double.parseDouble(list.getTotalWinAmount())>0){
                if (preferences.getMatchModel().getMatchStatus().equals("Completed")) {
                    holder.leader_point.setVisibility(View.VISIBLE);
                    holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.green_pure));
                    holder.leader_point.setText("Won: "+mContext.getResources().getString(R.string.rs) + list.getTotalWinAmount());
                }else {
                    holder.leader_point.setVisibility(View.GONE);
                }

            }else {
                holder.leader_point.setVisibility(View.GONE);
            }

            if (is_same_team_cancel){
                holder.leader_point.setVisibility(View.VISIBLE);
                holder.leader_point.setText("Entry Fee Refunded");//+mContext.getResources().getString(R.string.rs) + preferences.getMatchModel().getTotalEntryFees()
            }

            holder.leader_rank.setText("#" + list.getTotalRank());
        }

       /* holder.leader_point.setVisibility(View.VISIBLE);
        holder.leader_point.setText(position);*/

        final Date finalMatchDate = MatchDate;
        holder.switchTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.getClickStatus()) {
                    ((LeaderBoardFragment)fragment).switchTeam(position,finalMatchDate);
                   /* if (finalMatchDate.after(MyApp.getCurrentDate())) {
                        Intent intent = new Intent(mContext, SwipeTeamActivity.class);
                        intent.putExtra("team_id",list.getTempTeamId());
                        intent.putExtra("contest_id",list.getContestId());
                        intent.putExtra("user_join_team_id",list.getId());
                        intent.putExtra("joined_temp_team_id",((LeaderBoardFragment) fragment).contestData.getJoinedTeamTempTeamId());
                        mContext.startActivity(intent);
                    } else {
                        CustomUtil.showSnackBarLong(mContext, "Match Started!");
                    }*/
                }
            }
        });

        holder.leader_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((LeaderBoardFragment) fragment).compare_on == false) {

                    SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (date.after(MyApp.getCurrentDate()) && !list.getUserId().equals(preferences.getUserModel().getId())) {
                        CustomUtil.showTopSneakError(mContext, "The match has not been started yet");
                    } else {
                        if (MyApp.getClickStatus()) {
                            ((LeaderBoardFragment) fragment).getTeamDetail(list.getTempTeamId(), list.getTempTeamName());
                        }
                    }
                } else {

                    if (list.isSelected() == true) {
                        ((LeaderBoardFragment) fragment).compare_count -= 1;
                        //holder.leader_img.setBorderWidth(0);
                        holder.leader_layout.setBackgroundResource(R.color.pureWhite);
                        holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                        holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.font_color));
                        holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                        holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));
                        ((LeaderBoardFragment) fragment).compare_list.remove(list);
                        list.setSelected(false);
                    } else {
                        if(((LeaderBoardFragment) fragment).compare_count==2){
                            CustomUtil.showTopSneakError(mContext,"You can't compare more than 2 teams");
                        }else{
                            ((LeaderBoardFragment) fragment).compare_count += 1;

                            holder.leader_layout.setBackgroundResource(R.color.selectGreen);
                            holder.leader_name.setTextColor(mContext.getResources().getColor(R.color.font_color));
                            holder.leader_point.setTextColor(mContext.getResources().getColor(R.color.font_color));
                            holder.leader_rank.setTextColor(mContext.getResources().getColor(R.color.font_color));
                            holder.leader_winning.setTextColor(mContext.getResources().getColor(R.color.font_color));

                            ((LeaderBoardFragment) fragment).compare_list.add(list);
                            LogUtil.e(TAG, "onClick: " );
                            list.setSelected(true);
                            if(((LeaderBoardFragment) fragment).compare_count==2){
                                ((LeaderBoardFragment) fragment).onCompare();
                            }
                        }

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return leaderBoardModelList.size();
    }

    protected class LeaderBoardHolder extends RecyclerView.ViewHolder {
        TextView leader_point, leader_rank, leader_name, leader_winning;
        CircleImageView leader_img;
        LinearLayout leader_layout;
        ImageView switchTeam;

        public LeaderBoardHolder(@NonNull View itemView) {
            super(itemView);
            leader_name = (TextView) itemView.findViewById(R.id.leader_name);
            leader_point = (TextView) itemView.findViewById(R.id.leader_point);
            leader_rank = (TextView) itemView.findViewById(R.id.leader_rank);
            leader_img = (CircleImageView) itemView.findViewById(R.id.leader_img);
            leader_layout = (LinearLayout) itemView.findViewById(R.id.leader_layout);
            leader_winning = (TextView) itemView.findViewById(R.id.leader_winning);
            switchTeam = (ImageView) itemView.findViewById(R.id.leader_exchange);
        }
    }

    public void updateData(List<LeaderBoardModel> leaderBoardModelList) {
        this.leaderBoardModelList = leaderBoardModelList;
        notifyDataSetChanged();
    }


}
