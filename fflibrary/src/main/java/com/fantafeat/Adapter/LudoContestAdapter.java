package com.fantafeat.Adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.GameLeaderboardActivity;
import com.fantafeat.Model.GameContestModel;
import com.fantafeat.Model.LudoContestModal;
import com.fantafeat.R;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class LudoContestAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<GameContestModel> mDataList;
    private Context mContext;
    private ItemClickListener mListener;

    public LudoContestAdapter(List<GameContestModel> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        this.mListener = (ItemClickListener) mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_header,
                    parent, false));
            case 1: return new ItemRowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_item,
                    parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameContestModel contest = mDataList.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                HeaderHolder holder2 = (HeaderHolder)holder;

                if (TextUtils.isEmpty(contest.getTitle()) && TextUtils.isEmpty(contest.getSubTitle())){
                    holder2.layMain.setVisibility(View.GONE);
                    holder2.txtArrow.setVisibility(View.GONE);
                }else {
                    holder2.layMain.setVisibility(View.VISIBLE);
                    holder2.txtArrow.setVisibility(View.VISIBLE);
                    holder2.txtTitle.setText(contest.getTitle());
                    holder2.txtSubTitle.setText(contest.getSubTitle());
                }

                if (!TextUtils.isEmpty(contest.getLb_id())) {
                    holder2.txtArrow.setVisibility(View.VISIBLE);
                }else {
                    holder2.txtArrow.setVisibility(View.GONE);
                }

                holder2.layMain.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(contest.getLb_id())) {
                        mContext.startActivity(new Intent(mContext, GameLeaderboardActivity.class)
                                .putExtra("leaderboard_title", "Leaderboard")
                                .putExtra("lb_cat_id", "2")
                                .putExtra("leaderboard_id", contest.getLb_id())
                        );
                    }
                });

                holder2.txtArrow.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(contest.getLb_id())) {
                        mContext.startActivity(new Intent(mContext, GameLeaderboardActivity.class)
                                .putExtra("leaderboard_title", "Leaderboard")
                                .putExtra("lb_cat_id", "2")
                                .putExtra("leaderboard_id", contest.getLb_id())
                        );
                    }
                });

                break;

            case 1:
                ItemRowHolder holder1 = (ItemRowHolder)holder;
                String pool="";
                if (contest.getAsset_type().equalsIgnoreCase("poker/") || contest.getAsset_type().equalsIgnoreCase("Win3Patti/")){
                    pool=contest.getNo_spot()+"/"+contest.getGame_round();
                }else {
                   /* if (contest.getDisAmt().contains(".")){
                        pool=mContext.getResources().getString(R.string.rs) + contest.getDisAmt();
                    }else*/
                    pool=CustomUtil.displayAmountFloat( mContext, contest.getDisAmt());//mContext.getResources().getString(R.string.rs)+contest.getDisAmt()
                }
                //Log.e("resp","Pool: "+pool);
                holder1.txtPool.setText(pool);
              //  holder1.txtPool.setText(CustomUtil.displayAmount(mContext, contest.getDisAmt()));//"₹"+contest.getDisAmt()

                if (!TextUtils.isEmpty(contest.getGameMode())){
                    if (contest.getGameMode().equalsIgnoreCase("point") || contest.getGameMode().equalsIgnoreCase("raise")){
                        holder1.txtLblPool.setText("Point / Value");
                    }
                    else {
                        if (contest.getAsset_type().equalsIgnoreCase("poker/") || contest.getAsset_type().equalsIgnoreCase("Win3Patti/")){
                            holder1.txtLblPool.setText("Blinds");
                            holder1.txtEntryLbl.setText("Min Buy-In");
                        }else
                            holder1.txtLblPool.setText("Winner Takes");
                    }
                }
                else {
                    if (contest.getAsset_type().equalsIgnoreCase("poker/") || contest.getAsset_type().equalsIgnoreCase("Win3Patti/")){
                        holder1.txtLblPool.setText("Blinds");
                        holder1.txtEntryLbl.setText("Min Buy-In");
                    }else
                        holder1.txtLblPool.setText("Winner Takes");
                }

                if (contest.getEntryFee().equalsIgnoreCase("0")){
                    holder1.txtEntry.setText("FREE");
                }else {
                    holder1.txtEntry.setText("₹"+contest.getEntryFee());
                }

                /*if (!TextUtils.isEmpty(contest.getGame_use_coin()) && CustomUtil.convertFloat(contest.getGame_use_coin())>0){
                    holder1.layCoin.setVisibility(View.VISIBLE);
                    holder1.txtCoin.setText(contest.getGame_use_coin()+" %");
                }else {
                    holder1.layCoin.setVisibility(View.GONE);
                }*/

                int inPixelsHeight= (int) mContext.getResources().getDimension(R.dimen.two_player_height);

                if (contest.getAsset_type().equalsIgnoreCase("poker/") || contest.getAsset_type().equalsIgnoreCase("Win3Patti/")) {
                    holder1.layUser1.getLayoutParams().height = inPixelsHeight;
                    holder1.layUser2.getLayoutParams().height = inPixelsHeight;

                    holder1.layP1Waiting.setBackgroundResource(R.drawable.transparent_view);
                    holder1.layP2Waiting.setBackgroundResource(R.drawable.transparent_view);
                    holder1.layP3Waiting.setBackgroundResource(R.drawable.transparent_view);
                    holder1.layP4Waiting.setBackgroundResource(R.drawable.transparent_view);

                    holder1.txtP1.setVisibility(View.INVISIBLE);
                    holder1.layWaitLabelP1.setVisibility(View.INVISIBLE);
                    holder1.txtP2.setVisibility(View.INVISIBLE);
                    holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);
                    holder1.txtP3.setVisibility(View.INVISIBLE);
                    holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                    holder1.txtP4.setVisibility(View.INVISIBLE);
                    holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                    holder1.anim1.cancel();
                    holder1.anim2.cancel();
                    holder1.anim3.cancel();
                    holder1.anim4.cancel();

                    holder1.layOnlineText.setVisibility(View.VISIBLE);
                    String nospot="0";
                    if(!TextUtils.isEmpty(contest.getPokerNoOfPlaying()))
                        nospot=contest.getPokerNoOfPlaying();
                    if (CustomUtil.convertInt(nospot)>0) {
                        holder1.txtOnlineCnt.setText(nospot + " Playing");
                        holder1.layOnlineText.setVisibility(View.VISIBLE);
                    }else {
                        holder1.layOnlineText.setVisibility(View.GONE);
                    }
                }
                else {

                    holder1.layOnlineText.setVisibility(View.GONE);

                    if (contest.getNo_spot().equals("2")) {

                        holder1.layP1Waiting.setBackgroundResource(R.drawable.transparent_view);
                        holder1.layP2Waiting.setBackgroundResource(R.drawable.transparent_view);

                        inPixelsHeight = (int) mContext.getResources().getDimension(R.dimen.two_player_height);

                        if (contest.getWaitCount() == 1) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1" /*+ contest.getWaitingModals().get(0).getUser_team_name()*/);
                                holder1.anim1.start();
                            }
                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);

                            holder1.layP3Waiting.setVisibility(View.GONE);
                            holder1.layP4Waiting.setVisibility(View.GONE);

                        }
                        else if (contest.getWaitCount() == 2) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1" /*+ contest.getWaitingModals().get(0).getUser_team_name()*/);
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("# Player2"/**/);
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.layP3Waiting.setVisibility(View.GONE);
                            holder1.layP4Waiting.setVisibility(View.GONE);

                        }
                        else {

                            holder1.txtP1.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.INVISIBLE);

                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);

                            holder1.layP3Waiting.setVisibility(View.GONE);
                            holder1.layP4Waiting.setVisibility(View.GONE);

                            holder1.anim1.cancel();
                            holder1.anim2.cancel();

                        }
                    }
                    else if (contest.getNo_spot().equals("3")) {

                        inPixelsHeight = (int) mContext.getResources().getDimension(R.dimen.four_player_height);

                        if (contest.getWaitCount() == 1) {

                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1"/**/);
                                holder1.anim1.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);
                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                        } else if (contest.getWaitCount() == 2) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1" /*+ contest.getWaitingModals().get(0).getUser_team_name()*/);
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("#  Player2" /*+ contest.getWaitingModals().get(1).getUser_team_name()*/);
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                        } else if (contest.getWaitCount() == 3) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1" );
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("# Player2");
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }
                            if (contest.getWaitingModals().size() > 2) {
                                holder1.txtP3.setText("# Player3");
                                holder1.anim3.cancel();
                                holder1.anim3.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.txtP3.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.VISIBLE);

                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);
                        } else {

                            holder1.txtP1.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.INVISIBLE);
                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);
                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                            holder1.anim1.cancel();
                            holder1.anim2.cancel();
                            holder1.anim3.cancel();
                            holder1.anim4.cancel();

                        }
                    }
                    else if (contest.getNo_spot().equals("4")) {

                        inPixelsHeight = (int) mContext.getResources().getDimension(R.dimen.four_player_height);

                        holder1.layP1Waiting.setBackgroundResource(R.drawable.bg_black_transe);
                        holder1.layP2Waiting.setBackgroundResource(R.drawable.bg_black_transe);

                        holder1.layP3Waiting.setVisibility(View.VISIBLE);
                        holder1.layP4Waiting.setVisibility(View.VISIBLE);

                        if (contest.getWaitCount() == 1) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1");
                                holder1.anim1.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);
                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                        }
                        else if (contest.getWaitCount() == 2) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1" );
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("# Player2");
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                        }
                        else if (contest.getWaitCount() == 3) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1");
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("# Player2" );
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }
                            if (contest.getWaitingModals().size() > 2) {
                                holder1.txtP3.setText("# Player3");
                                holder1.anim3.cancel();
                                holder1.anim3.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.txtP3.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.VISIBLE);

                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                        }
                        else if (contest.getWaitCount() == 4) {
                            if (contest.getWaitingModals().size() > 0) {
                                holder1.txtP1.setText("# Player1");
                                holder1.anim1.cancel();
                                holder1.anim1.start();
                            }
                            if (contest.getWaitingModals().size() > 1) {
                                holder1.txtP2.setText("# Player2");
                                holder1.anim2.cancel();
                                holder1.anim2.start();
                            }
                            if (contest.getWaitingModals().size() > 2) {
                                holder1.txtP3.setText("# Player3");
                                holder1.anim3.cancel();
                                holder1.anim3.start();
                            }
                            if (contest.getWaitingModals().size() > 3) {
                                holder1.txtP4.setText("# Player4");
                                holder1.anim4.cancel();
                                holder1.anim4.start();
                            }

                            holder1.txtP1.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.VISIBLE);

                            holder1.txtP2.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.VISIBLE);

                            holder1.txtP3.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.VISIBLE);

                            holder1.txtP4.setVisibility(View.VISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.VISIBLE);

                        }
                        else {

                            holder1.txtP1.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP1.setVisibility(View.INVISIBLE);
                            holder1.txtP2.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP2.setVisibility(View.INVISIBLE);
                            holder1.txtP3.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP3.setVisibility(View.INVISIBLE);
                            holder1.txtP4.setVisibility(View.INVISIBLE);
                            holder1.layWaitLabelP4.setVisibility(View.INVISIBLE);

                            holder1.anim1.cancel();
                            holder1.anim2.cancel();
                            holder1.anim3.cancel();
                            holder1.anim4.cancel();

                        }
                    }

                    holder1.layUser1.getLayoutParams().height = inPixelsHeight;
                    holder1.layUser2.getLayoutParams().height = inPixelsHeight;
                }

                holder1.layUser1.setOnClickListener(v->{
                    v.setEnabled(false);
                    mListener.onItemClick(holder1.layUser1,contest);
                });

                holder1.layUser2.setOnClickListener(v->{
                    v.setEnabled(false);
                    mListener.onItemClick(holder1.layUser2,contest);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView txtPool,txtEntry,txtP1,txtP2,txtLabelWaitP1,txtP3,txtP4,txtOnlineCnt,txtEntryLbl,txtLblPool;
        private LinearLayout layUser1,layUser2,layP1Waiting,layP2Waiting,layWaitLabelP2,layWaitLabelP1,
                layP3Waiting,layP4Waiting,layWaitLabelP3,layWaitLabelP4,layOnlineText;
        private ObjectAnimator anim2,anim1,anim3,anim4;



        @SuppressLint("WrongConstant")
        ItemRowHolder(View itemView) {
            super(itemView);
            txtPool=itemView.findViewById(R.id.txtPool);
            txtEntry=itemView.findViewById(R.id.txtEntry);
            layOnlineText=itemView.findViewById(R.id.layOnlineText);
            txtOnlineCnt=itemView.findViewById(R.id.txtOnlineCnt);
            txtEntryLbl=itemView.findViewById(R.id.txtEntryLbl);
            txtLblPool=itemView.findViewById(R.id.txtLblPool);

            layUser1=itemView.findViewById(R.id.layUser1);
            layUser2=itemView.findViewById(R.id.layUser2);

            txtP3=itemView.findViewById(R.id.txtP3);
            txtP4=itemView.findViewById(R.id.txtP4);
            layP3Waiting=itemView.findViewById(R.id.layP3Waiting);
            layP4Waiting=itemView.findViewById(R.id.layP4Waiting);
            layWaitLabelP3=itemView.findViewById(R.id.layWaitLabelP3);
            layWaitLabelP4=itemView.findViewById(R.id.layWaitLabelP4);

            txtP1=itemView.findViewById(R.id.txtP1);
            txtP1.setSelected(true);
            txtP2=itemView.findViewById(R.id.txtP2);
            txtP2.setSelected(true);
            txtLabelWaitP1=itemView.findViewById(R.id.txtLabelWaitP1);
            txtLabelWaitP1.setSelected(true);

            layP1Waiting=itemView.findViewById(R.id.layP1Waiting);

            layP2Waiting=itemView.findViewById(R.id.layP2Waiting);

            layWaitLabelP2=itemView.findViewById(R.id.layWaitLabelP2);
            layWaitLabelP1=itemView.findViewById(R.id.layWaitLabelP1);

         /*   anim1 = ObjectAnimator.ofInt(layP1Waiting, "backgroundColor", Color.WHITE, Color.GRAY);
            anim1.setDuration(3000);
            anim1.setEvaluator(new ArgbEvaluator());
            anim1.setRepeatCount(ValueAnimator.INFINITE);
            anim1.setRepeatMode(ValueAnimator.REVERSE);*/

            anim1 = ObjectAnimator.ofFloat(layWaitLabelP1, "alpha", 0.5f, 1.0f);
            anim1.setRepeatMode(Animation.REVERSE);
            anim1.setRepeatCount(Animation.INFINITE);
            anim1.setDuration(300);

            anim2 = ObjectAnimator.ofFloat(layWaitLabelP2, "alpha", 0.5f, 1.0f);
            anim2.setRepeatMode(Animation.REVERSE);
            anim2.setRepeatCount(Animation.INFINITE);
            anim2.setDuration(300);

            anim3 = ObjectAnimator.ofFloat(layWaitLabelP3, "alpha", 0.5f, 1.0f);
            anim3.setRepeatMode(Animation.REVERSE);
            anim3.setRepeatCount(Animation.INFINITE);
            anim3.setDuration(300);

            anim4 = ObjectAnimator.ofFloat(layWaitLabelP4, "alpha", 0.5f, 1.0f);
            anim4.setRepeatMode(Animation.REVERSE);
            anim4.setRepeatCount(Animation.INFINITE);
            anim4.setDuration(300);



        }

        private int getScreenWidth(Context context) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle,txtSubTitle,txtArrow;
        private LinearLayout layMain;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtSubTitle=itemView.findViewById(R.id.txtSubTitle);
            layMain=itemView.findViewById(R.id.layMain);
            txtArrow=itemView.findViewById(R.id.txtArrow);

        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, GameContestModel contest);
    }

}
