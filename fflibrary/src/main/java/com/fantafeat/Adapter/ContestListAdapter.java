package com.fantafeat.Adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
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

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.CustomMoreContestListActivity;
import com.fantafeat.Activity.LeaderBoardActivity;
import com.fantafeat.Activity.MoreContestListActivity;
import com.fantafeat.Activity.TeamSelectJoinActivity;
import com.fantafeat.Activity.UserLeaderboardActivity;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.PassModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.OfferListSheet;
import com.fantafeat.util.PrefConstant;
import com.fantafeat.util.WinningTreeSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContestListAdapter extends RecyclerView.Adapter<ContestListAdapter.ContestListHolder> {

    private Context mContext;
    private List<ContestModel.ContestDatum> contestDatumList;
    private Gson gson;
    //private ObjectAnimator anim;
    private Animation animation;
    private int mainPosition = 0;
    private boolean isMyJoined = false;

    public ContestListAdapter(Context mContext, List<ContestModel.ContestDatum> contestDatumList, Gson gson,int mainPosition,boolean isMyJoined) {
        this.mContext = mContext;
        this.contestDatumList = contestDatumList;
        this.gson = gson;
        this.mainPosition = mainPosition;
        this.isMyJoined = isMyJoined;
    }

    @NonNull
    @Override
    public ContestListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestListHolder(LayoutInflater.from(mContext).inflate(R.layout.row_contest, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ContestListHolder holder, int position) {
        final ContestModel.ContestDatum list = contestDatumList.get(holder.getAdapterPosition());

        String entryFee="";

        holder.contest_offer_price.setVisibility(View.GONE);

        if (list.getNewOfferList().size()>0){
            holder.imgOfferText.setVisibility(View.VISIBLE);
            holder.layBonus.setVisibility(View.GONE);

            NewOfferModal newOfferModal;
            // LogUtil.e("compa","list: "+list.getNewOfferRemovedList().size());
            if (list.getNewOfferRemovedList().size()>0){
                newOfferModal=list.getNewOfferRemovedList().get(0);
            }else {
                newOfferModal=list.getNewOfferList().get(0);
            }

            if (TextUtils.isEmpty(newOfferModal.getDiscount_entry_fee())){
                //holder.contest_entry.setText(context.getResources().getString(R.string.rs)+newOfferModal.getEntry_fee());
                entryFee=mContext.getResources().getString(R.string.rs)+newOfferModal.getEntry_fee();
                holder.contest_offer_price.setVisibility(View.GONE);

            }
            else if (newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0") ||
                    newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0.0") ||
                    newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0.00")){
                // holder.contest_entry.setText("FREE");
                entryFee="Free";
                holder.contest_offer_price.setVisibility(View.VISIBLE);

                holder.contest_offer_price.setText( mContext.getResources().getString(R.string.rs) + newOfferModal.getEntry_fee());
                holder.contest_offer_price.setPaintFlags(holder.contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //holder.contest_entry.setText(context.getResources().getString(R.string.rs)+newOfferModal.getDiscount_entry_fee());
                entryFee=mContext.getResources().getString(R.string.rs)+newOfferModal.getDiscount_entry_fee();
                holder.contest_offer_price.setVisibility(View.VISIBLE);

                holder.contest_offer_price.setText( mContext.getResources().getString(R.string.rs) + newOfferModal.getEntry_fee());
                holder.contest_offer_price.setPaintFlags(holder.contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
           /* holder.contest_bonus.setText(newOfferModal.getUsed_bonus() + "%");

            if (newOfferModal.getUsed_bonus().equalsIgnoreCase("0")){
                holder.contest_bonus.setVisibility(View.GONE);
            }else {
                holder.contest_bonus.setVisibility(View.VISIBLE);
            }*/

        }
        else {
            holder.imgOfferText.setVisibility(View.GONE);
            holder.layBonus.setVisibility(View.VISIBLE);

            if (list.getEntryFee().equalsIgnoreCase("0") || list.getEntryFee().equalsIgnoreCase("0.0")||
                    list.getEntryFee().equalsIgnoreCase("0.00")){
                // holder.contest_entry.setText("FREE");
                entryFee="FREE";
            }else {
                entryFee=mContext.getResources().getString(R.string.rs)+list.getEntryFee();
                //holder.contest_entry.setText( context.getResources().getString(R.string.rs) + list.getEntryFee());
            }

            if (list.getDefaultBonus().equalsIgnoreCase("0")){
                holder.layBonus.setVisibility(View.GONE);
            }else {
                holder.layBonus.setVisibility(View.VISIBLE);
            }

            holder.txtBonus.setText(list.getDefaultBonus() + "%");

        }
        if(!TextUtils.isEmpty(list.getWinningTreeText())){
            holder.contest_offer_text.setVisibility(View.VISIBLE);
            holder.contest_offer_text.setText(list.getWinningTreeText());
        }else{
            holder.contest_offer_text.setVisibility(View.GONE);
        }

       // holder.contest_offer_text.setVisibility(View.GONE);

        holder.contest_entry.setText(entryFee);

        holder.contest_card.setClickable(true);
        int totalSpots = CustomUtil.convertInt(list.getNoTeamJoin());
        int jointeam = CustomUtil.convertInt(list.getJoinedTeams());

        int progress = (jointeam * 100) / totalSpots;
        holder.contest_progress.setProgress(progress);

       /* if (!TextUtils.isEmpty(list.getDefault_bb_coins()) && CustomUtil.convertInt(list.getDefault_bb_coins())>0){
            holder.layCoin.setVisibility(View.VISIBLE);
            holder.txtCoin.setText(list.getDefault_bb_coins()+"%");
        }else {
            holder.layCoin.setVisibility(View.GONE);
        }*/

        if (list.getIsUnlimited().equalsIgnoreCase("Yes")) {
            //holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
            holder.teams_left_linear.setVisibility(View.VISIBLE);
            holder.contest_full_linear.setVisibility(View.GONE);
            holder.contest_left_team.setText("" + jointeam);
            holder.contest_left_spots_text.setText("Spots left");
            holder.contest_total_team.setText("Unlimited");
            holder.contest_total_spots_text.setText("Spots");

            float cal=(CustomUtil.convertFloat(list.getJoinedTeams())*CustomUtil.convertFloat(list.getEntryFee()))
                    -((CustomUtil.convertFloat(list.getJoinedTeams())*CustomUtil.convertFloat(list.getEntryFee())*CustomUtil.convertFloat(list.getCommission()))/100);

            holder.contest_price_pool.setText(CustomUtil.displayAmount(mContext, (int)cal+""));
           // holder.contest_total_winner.setText(list.getTotalWinner()+" %");
        }
        else {

            holder.contest_price_pool.setText(CustomUtil.displayAmount(mContext, list.getDistributeAmount()));
            //holder.contest_total_winner.setText(list.getTotalWinner());

            int spotsLeft = totalSpots - jointeam;
            if(spotsLeft <= 0){
                holder.teams_left_linear.setVisibility(View.GONE);
                holder.contest_full_linear.setVisibility(View.VISIBLE);
              //  holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
            }else {
               /* if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
                    holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
                }else {
                    holder.join_btn.setBackground(mContext.getResources().ge0tDrawable(R.drawable.green_filled_btn));
                }*/
                holder.teams_left_linear.setVisibility(View.VISIBLE);
                holder.contest_full_linear.setVisibility(View.GONE);
            }

            holder.contest_left_team.setText("" + spotsLeft);
            holder.contest_left_spots_text.setText("Spots left");
            holder.contest_total_team.setText(list.getNoTeamJoin());
            holder.contest_total_spots_text.setText("Spots");
        }

       /* if (CustomUtil.convertInt(list.getMaxJoinTeam()) > CustomUtil.convertInt(list.getMyJoinedTeam())) {
            int spotsLeft = totalSpots - jointeam;
            if(spotsLeft <= 0){
                holder.join_btn.setEnabled(false);
                holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
            }else {
                holder.join_btn.setEnabled(true);
                holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
            }

        } else {
            holder.join_btn.setEnabled(false);
            holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
        }*/

        if (CustomUtil.convertInt(list.getMyJoinedTeam()) > 0){
            if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
                holder.contest_entry.setEnabled(false);
                holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
            }else {
                if (CustomUtil.convertInt(list.getMaxJoinTeam()) == CustomUtil.convertInt(list.getMyJoinedTeam())){
                    holder.contest_entry.setEnabled(false);
                    holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
                }
                else {
                    int spotsLeft = totalSpots - jointeam;
                    if(spotsLeft <= 0){
                        holder.contest_entry.setEnabled(false);
                        holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
                    }else {
                        holder.contest_entry.setEnabled(true);
                        holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
                    }
                }
            }
        }
        else {
            int spotsLeft = totalSpots - jointeam;
            if(spotsLeft <= 0){
                holder.contest_entry.setEnabled(false);
                holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_grey));
            }else {
                holder.contest_entry.setEnabled(true);
                holder.contest_entry.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
            }
            //holder.join_btn.setEnabled(true);
            //holder.join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
        }

        if (list.getIs_lb().equalsIgnoreCase("yes")){
            holder.txtLeaderboard.setVisibility(View.VISIBLE);
            holder.txtLeaderboard.setAnimation(animation);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.contest_card.getLayoutParams();
            cardViewMarginParams.setMargins(30, 0, 30, 30);
            holder.contest_card.requestLayout();
        }
        else {
            holder.txtLeaderboard.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.contest_card.getLayoutParams();
            cardViewMarginParams.setMargins(30, 15, 30, 30);
            holder.contest_card.requestLayout();
        }

        if (list.getIs_flexi().equalsIgnoreCase("yes")){
            holder.layConfirm.setVisibility(View.VISIBLE);
            holder.imgConfirm.setImageResource(R.drawable.ic_flext_join);
            holder.contest_confirm.setText("Flexible");
        }
        else {
            if (list.getConEntryStatus().equalsIgnoreCase("Unconfirmed")) {
                holder.layConfirm.setVisibility(View.INVISIBLE);
            }
            else {
                holder.layConfirm.setVisibility(View.VISIBLE);
                holder.imgConfirm.setImageResource(R.drawable.ic_confirm_contest);
                holder.contest_confirm.setText("Guaranteed");
            }
        }


        if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
            holder.imgSingleMultiple.setImageResource(R.drawable.ic_single_join);
            holder.txtSingleMultiple.setText("Single");
        }
       /* else  if (list.getConPlayerEntry().equalsIgnoreCase("flexi")) {
            holder.imgSingleMultiple.setImageResource(R.drawable.ic_flext_join);
            holder.txtSingleMultiple.setText("Flexi");
        }*/
        else {
            holder.imgSingleMultiple.setImageResource(R.drawable.ic_multiple_join);
            holder.txtSingleMultiple.setText("Upto "+list.getMaxJoinTeam());
        }

        //anim.start();

        try {
            JSONArray jsonArray = new JSONArray(list.getWinningTree());
            if (jsonArray.length()>0){
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                if (!jsonObject.optString("amount").equalsIgnoreCase("glory")){
                    holder.txtFirstWin.setText(CustomUtil.displayAmountFloat( mContext, jsonObject.optString("amount")));
                }else {
                    holder.txtFirstWin.setText(jsonObject.optString("amount"));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            holder.layFirstWin.setVisibility(View.GONE);
        }

        int totalWin=Integer.parseInt(list.getTotalWinner())*100/Integer.parseInt(list.getNoTeamJoin());

        holder.txtWinPer.setText(totalWin+"%");

        /*if (list.getContestFavorite()){
            holder.imgFavBtn.setImageResource(R.drawable.ic_unfavorite_contest);
        }
        else {
            holder.imgFavBtn.setImageResource(R.drawable.ic_favorite_contest);
        }

        if (isMyJoined){
            holder.imgFavBtn.setVisibility(View.GONE);
        }
        else {
            if (list.getConTypeName().equalsIgnoreCase("Grand Leagues")){
                holder.imgFavBtn.setVisibility(View.GONE);
            }else {
                holder.imgFavBtn.setVisibility(View.VISIBLE);
            }
        }*/

        String finalEntryFee = entryFee;
        holder.imgShare.setOnClickListener(view -> {
            String url= ConstantUtil.LINK_FANTASY_URL+MyApp.getMyPreferences().getMatchModel().getSportId()+"/"+
                    MyApp.getMyPreferences().getMatchModel().getId()+ "/"+list.getId();
            //LogUtil.e("resp","Share Link: "+url);
            /*FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(url))
                    .setDomainUriPrefix(ConstantUtil.LINK_PREFIX_URL)
                    .setIosParameters(new DynamicLink.IosParameters.Builder(ConstantUtil.FF_IOS_APP_BUNDLE).setAppStoreId(ConstantUtil.FF_IOS_APP_ID)
                            .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK)).build())
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                            .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK))
                            .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener((Activity) mContext, task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().getShortLink()!=null)
                                shareShortLink(task.getResult().getShortLink(),list, finalEntryFee,totalSpots - jointeam);
                            else {
                                CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                            }
                        } else {
                            CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                        }
                    });*/
        });

        holder.imgOfferText.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
                OfferListSheet bottomSheetTeam = new OfferListSheet( mContext,list.getNewOfferTempList());//getNewOfferList
                if (mContext instanceof  ContestListActivity){
                    bottomSheetTeam.show(((ContestListActivity)  mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);
                }else {
                    if (ConstantUtil.isCustomMore){
                        bottomSheetTeam.show(((CustomMoreContestListActivity)  mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else
                        bottomSheetTeam.show(((MoreContestListActivity)  mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);

                }

            }
        });

        /*holder.imgFavBtn.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
                holder.imgFavBtn.setEnabled(false);
                String msg="";
                if (list.getContestFavorite()){
                    msg="Are you sure you want Unfavorite this Contest?";
                }else {
                    msg="Are you sure you want favorite this Contest?";
                }

                AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
                alert.setMessage(msg);
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", (dialog, which) -> {
                    holder.imgFavBtn.setEnabled(true);
                    try {
                        if (mContext instanceof  ContestListActivity){
                            ((ContestListActivity)mContext).followUnFollowAction(list);
                        }else {
                            ((MoreContestListActivity)mContext).followUnFollowAction(list);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                alert.setNegativeButton("No", (dialog, which) -> {
                    holder.imgFavBtn.setEnabled(true);
                    dialog.dismiss();
                });
                alert.show();
            }
        });*/

        holder.linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // LogUtil.d("selSports",list.getSportId());
                if (MyApp.getClickStatus()) {
                    Intent intent = new Intent(mContext, LeaderBoardActivity.class);
                    String model = gson.toJson(list);
                    intent.putExtra("model",model);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.txtLeaderboard.setOnClickListener(v->{
            if (MyApp.getClickStatus()) {
                mContext.startActivity(new Intent(mContext, UserLeaderboardActivity.class)
                        .putExtra("leaderboard_id",list.getLb_id()));
            }
        });

        holder.contest_tree_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (list.getIsUnlimited().equalsIgnoreCase("No")) {
                        WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, list.getDistributeAmount(),
                                list.getWinningTree(),holder.contest_price_pool.getText().toString());
                        if (mContext instanceof  ContestListActivity){
                            bottomSheetTeam.show(((ContestListActivity) mContext).getSupportFragmentManager(),
                                    BottomSheetTeam.TAG);
                        }else {
                            if (ConstantUtil.isCustomMore){
                                bottomSheetTeam.show(((CustomMoreContestListActivity)  mContext).getSupportFragmentManager(),
                                        BottomSheetTeam.TAG);
                            }else
                                bottomSheetTeam.show(((MoreContestListActivity)  mContext).getSupportFragmentManager(),
                                        BottomSheetTeam.TAG);

                        }

                    }else {
                        CustomUtil.showTopSneakWarning(mContext,"Price Pool will appear after match starts.");
                    }

                }
            }
        });

        holder.txtFirstWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.contest_tree_layout.performClick();
            }
        });


        /*if (list.getIs_pass() != null && list.getIs_pass().equalsIgnoreCase("yes")) {
            if (list.getPassModelArrayList().size()>0){
                holder.imgPass.setVisibility(View.VISIBLE);
                holder.ll_pass.setVisibility(View.VISIBLE);
                LogUtil.e("Size","SIZEE="+list.getPassModelArrayList().size());
            }
            else {
                LogUtil.e("Sizeno","SIZEEno="+list.getPassModelArrayList().size());
                holder.imgPass.setVisibility(View.GONE);
                holder.ll_pass.setVisibility(View.GONE);
            }

        }else{
            LogUtil.e("Sizenrtro","SIZEEnortr="+list.getPassModelArrayList().size());
            holder.imgPass.setVisibility(View.GONE);
            holder.ll_pass.setVisibility(View.GONE);
        }*/

        holder.contest_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof  ContestListActivity){
                    ((ContestListActivity) mContext).list=list;
                }else {
                    if (ConstantUtil.isCustomMore){
                        ((CustomMoreContestListActivity) mContext).list=list;
                    }else
                        ((MoreContestListActivity) mContext).list=list;

                }

                if (MyApp.getMyPreferences().getPlayerModel() != null) {
                    if (CustomUtil.convertInt(list.getMaxJoinTeam()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                        CustomUtil.showTopSneakError(mContext, "Max " + list.getMaxJoinTeam() + " team(s) allowed");
                    }
                    else if (CustomUtil.convertInt(MyApp.getMyPreferences().getUpdateMaster().getMaxTeamCount()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                        CustomUtil.showTopSneakError(mContext, "Max " + MyApp.getMyPreferences().getUpdateMaster().getMaxTeamCount() + " team(s) can be created");
                    }
                    else if (CustomUtil.convertInt(list.getMyJoinedTeam()) >= MyApp.getMyPreferences().getPlayerModel().size()) {
                        //MyApp.getMyPreferences().setPref(PrefConstant.IS_CONTEST_JOIN,true);
                        Gson gson = new Gson();
                        Type menu = new TypeToken<ContestModel.ContestDatum>() {
                        }.getType();
                        String json = gson.toJson(list, menu);
                        Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                        intent.putExtra("data", json);
                        intent.putExtra(PrefConstant.TEAMCREATETYPE, 2);
                        mContext.startActivity(intent);
                    } else {
                        int teamCnt=0;
                        if (mContext instanceof  ContestListActivity){
                            if (((ContestListActivity)mContext).team_count!=null){
                                teamCnt=CustomUtil.convertInt(((ContestListActivity)mContext).team_count.optString("total_team"));
                            }
                            LogUtil.d("colDara",list.getJoinedTeamTempTeamId()+"   "+teamCnt);
                            if ((TextUtils.isEmpty(list.getJoinedTeamTempTeamId()) || list.getJoinedTeamTempTeamId().equals("0")) &&
                                    teamCnt == 1){
                                ((ContestListActivity)mContext).confirmTeam(list,mainPosition);
                            }else {
                                Gson gson = new Gson();
                                String model = gson.toJson(list);
                                Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                                intent.putExtra("model", model);
                                mContext.startActivity(intent);
                            }
                        }else {
                            if (ConstantUtil.isCustomMore){
                                if (((CustomMoreContestListActivity)mContext).team_count!=null){
                                    teamCnt=CustomUtil.convertInt(((CustomMoreContestListActivity)mContext).team_count.optString("total_team"));
                                }
                                LogUtil.d("colDara",list.getJoinedTeamTempTeamId()+"   "+teamCnt);
                                if ((TextUtils.isEmpty(list.getJoinedTeamTempTeamId()) || list.getJoinedTeamTempTeamId().equals("0")) &&
                                        teamCnt == 1){
                                    ((CustomMoreContestListActivity)mContext).confirmTeam(list);
                                }else {
                                    Gson gson = new Gson();
                                    String model = gson.toJson(list);
                                    Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                                    intent.putExtra("model", model);
                                    mContext.startActivity(intent);
                                }
                            }else{
                                if (((MoreContestListActivity)mContext).team_count!=null){
                                    teamCnt=CustomUtil.convertInt(((MoreContestListActivity)mContext).team_count.optString("total_team"));
                                }
                                LogUtil.d("colDara",list.getJoinedTeamTempTeamId()+"   "+teamCnt);
                                if ((TextUtils.isEmpty(list.getJoinedTeamTempTeamId()) || list.getJoinedTeamTempTeamId().equals("0")) &&
                                        teamCnt == 1){
                                    ((MoreContestListActivity)mContext).confirmTeam(list);
                                }else {
                                    Gson gson = new Gson();
                                    String model = gson.toJson(list);
                                    Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                                    intent.putExtra("model", model);
                                    mContext.startActivity(intent);
                                }
                            }


                        }

                    }
                }
            }
        });

        holder.layConfirm.setOnClickListener(v->{
            if (list.getIs_flexi().equalsIgnoreCase("yes")){
               /* ViewTooltip
                        .on((Activity)  mContext, holder.layConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("The event will take place even if only two spots are filled, Prize pool varies based on entries.")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }else {
               /* ViewTooltip
                        .on((Activity)  mContext, holder.imgConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Guaranteed winning even if contest remains unfilled")//Confirmed winning even if contest remains unfilled
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }

        });

       /* holder.layCoin.setOnClickListener(v->{
            ViewTooltip
                    .on((Activity)  mContext, holder.layCoin)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(LEFT)
                    .text("Use "+mContext.getResources().getString(R.string.fanta_gems)+" in this Contest")
                    .textColor(Color.WHITE)
                    .color( mContext.getResources().getColor(R.color.blackPrimary))
                    .show();
        });*/

        holder.laySingleMultiple.setOnClickListener(v->{
            if (list.getConPlayerEntry().equalsIgnoreCase("Single")){
               /* ViewTooltip
                        .on((Activity)  mContext, holder.laySingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Only one Team allowed")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }
          /*  else if (list.getConPlayerEntry().equalsIgnoreCase("flexi")){
                ViewTooltip
                        .on((Activity)  mContext, holder.laySingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Prize pool may be vary")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }*/
            else {
               /* ViewTooltip
                        .on((Activity)  mContext, holder.laySingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Join with multiple Teams")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }

        });

        holder.layBonus.setOnClickListener(v->{
           /* ViewTooltip
                    .on((Activity)  mContext, holder.layBonus)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(RIGHT)
                    .text("Bonus Contest")
                    .textColor(Color.WHITE)
                    .color( mContext.getResources().getColor(R.color.blackPrimary))
                    .show();*/
        });

    }

    private void shareShortLink(Uri link,ContestModel.ContestDatum list,String finalEntryFee,int spotsLeft){
        String sportsName="";

        for (SportsModel sportsModel:MyApp.getMyPreferences().getSports()) {
            if (sportsModel.getId().equalsIgnoreCase(MyApp.getMyPreferences().getMatchModel().getSportId())){
                sportsName=sportsModel.getSportName();
            }
        }

        String content=/*ConstantUtil.LINK_URL+"\n\n"+*/
                "I have challenged you to this *"+list.getConTypeName()+"* \uD83C\uDFC6 contest for the "+
                MyApp.getMyPreferences().getMatchModel().getTeam1Sname()+
                "\uD83E\uDD1Cvs\uD83E\uDD1B"+MyApp.getMyPreferences().getMatchModel().getTeam2Sname()+" "+sportsName+" match!\n\n"+
                "*Deadline* ⏳ : "+CustomUtil.dateTimeConvert(MyApp.getMyPreferences().getMatchModel().getSafeMatchStartTime())+
                "\n\n*Prize Pool*\uD83D\uDCB0 : "+CustomUtil.displayAmountFloat( mContext, list.getDistributeAmount())+"\n"+
                "*Entry Fee*\uD83D\uDCB6 : "+ finalEntryFee+"\n"+
                "\n\nTap below link for join:\uD83D\uDCF2" +
                "\n"+link.toString().trim();

        if (spotsLeft>0) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/html");
                mContext.startActivity(sendIntent);
            }catch (ActivityNotFoundException e){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setType("text/html");
                mContext.startActivity(sendIntent);
            }
        }
        else {
            CustomUtil.showTopSneakWarning(mContext,"This contest is full, Please choose another!");
        }
    }

    public void updateChild(int childPosition,ContestModel.ContestDatum contest){

        if (contestDatumList.size()!=childPosition){
            contestDatumList.get(childPosition).setMyJoinedTeam(contest.getMyJoinedTeam());
            contestDatumList.get(childPosition).setJoinedTeams(contest.getJoinedTeams());
            notifyDataSetChanged();
        }
    }

    public void updateList(List<ContestModel.ContestDatum> contestDatumList){
        this.contestDatumList = contestDatumList;
        notifyDataSetChanged();
    }

    public void updateData(List<ContestModel.ContestDatum> list){
        //this.contestDatumList.clear();
        this.contestDatumList=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contestDatumList.size();
    }

    public class ContestListHolder extends RecyclerView.ViewHolder {
        TextView contest_price_pool, /*contest_total_winner,*/ contest_entry, contest_left_team, contest_total_team,/*contest_bonus,
                contest_bonus_b,*/txtLeaderboard,txtSingleMultiple,txtFirstWin,txtWinPer,txtBonus,
                contest_left_spots_text, contest_offer_price, contest_total_spots_text,contest_confirm,/*contest_confirm,contest_multiple,txtSingleEntry,*/contest_offer_text;
        ProgressBar contest_progress;
        CardView contest_card;
        ImageView /*imgFavBtn,*/imgOfferText,imgSingleMultiple,imgShare,imgConfirm;
       // View vofr;

        LinearLayout contest_full_linear,teams_left_linear,linear2,/*join_btn,bonus_linear,*/contest_tree_layout,layConfirm,laySingleMultiple,
                layFirstWin,layWinPer,layBonus;

        public ContestListHolder(@NonNull View itemView) {
            super(itemView);

            contest_price_pool = itemView.findViewById(R.id.contest_price_pool);
            contest_tree_layout = itemView.findViewById(R.id.contest_tree_layout);
            //contest_total_winner = itemView.findViewById(R.id.contest_winner);
            contest_entry = itemView.findViewById(R.id.contest_entry_fee);
            contest_left_team = itemView.findViewById(R.id.contest_left_team);
            contest_total_team = itemView.findViewById(R.id.contest_total_team);
            contest_progress = itemView.findViewById(R.id.contest_progress);
            contest_left_spots_text = itemView.findViewById(R.id.contest_left_text);
            contest_total_spots_text = itemView.findViewById(R.id.contest_total_text);
            contest_card = itemView.findViewById(R.id.main_card);
            contest_offer_price = itemView.findViewById(R.id.contest_offer_price);
            contest_confirm = itemView.findViewById(R.id.contest_confirm);
            //contest_multiple = itemView.findViewById(R.id.contest_multiple);
            contest_full_linear = itemView.findViewById(R.id.contest_full_linear);
            teams_left_linear = itemView.findViewById(R.id.teams_left_linear);
            txtLeaderboard = itemView.findViewById(R.id.txtLeaderboard);
            imgSingleMultiple = itemView.findViewById(R.id.imgSingleMultiple);
            txtSingleMultiple = itemView.findViewById(R.id.txtSingleMultiple);
            laySingleMultiple = itemView.findViewById(R.id.laySingleMultiple);
            imgConfirm = itemView.findViewById(R.id.imgConfirm);
            layFirstWin = itemView.findViewById(R.id.layFirstWin);
            txtFirstWin = itemView.findViewById(R.id.txtFirstWin);
            layWinPer = itemView.findViewById(R.id.layWinPer);
            txtWinPer = itemView.findViewById(R.id.txtWinPer);
            layConfirm = itemView.findViewById(R.id.layConfirm);
            txtBonus = itemView.findViewById(R.id.txtBonus);
            layBonus = itemView.findViewById(R.id.layBonus);
            imgShare = itemView.findViewById(R.id.imgShare);
            //join_btn = itemView.findViewById(R.id.join_btn);
            linear2 = itemView.findViewById(R.id.linear2);
            //bonus_linear = itemView.findViewById(R.id.bonus_linear);
           // contest_bonus = itemView.findViewById(R.id.contest_bonus);
            //txtSingleEntry = itemView.findViewById(R.id.txtSingleEntry);
            imgOfferText = itemView.findViewById(R.id.imgOfferText);
            //imgFavBtn = itemView.findViewById(R.id.imgFavBtn);
            contest_offer_text = itemView.findViewById(R.id.contest_offer_text);
            /*anim = ObjectAnimator.ofFloat(contest_offer_text, "alpha", 0.5f, 1.0f);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(300);*/

            animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_to_original);

            //vofr = itemView.findViewById(R.id.vofr);
           // contest_bonus_b = itemView.findViewById(R.id.contest_bonus_b);

        }
    }
}
