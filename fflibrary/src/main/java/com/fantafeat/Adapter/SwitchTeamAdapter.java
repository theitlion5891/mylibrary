package com.fantafeat.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.fantafeat.Activity.SwipeTeamActivity;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.CustomUtil;

import java.util.List;

public class SwitchTeamAdapter extends RecyclerView.Adapter<SwitchTeamAdapter.SwitchTeamHolder> {

    private static final String TAG = "SwitchTeamAdapter";
    private Context mContext;
    private List<PlayerListModel> playerListModelList;
    private MyPreferences preferences;
    private SwipeTeamActivity swipeTeamActivity;
    private String sport_id;

    public SwitchTeamAdapter(Context mContext, List<PlayerListModel> playerListModelList, SwipeTeamActivity swipeTeamActivity,
                             String sport_id){
        this.mContext =mContext;
        this.playerListModelList = playerListModelList;
        this.swipeTeamActivity = swipeTeamActivity;
        this.sport_id=sport_id;
        preferences = MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public SwitchTeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SwitchTeamHolder(LayoutInflater.from(mContext).inflate(R.layout.row_team_join_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SwitchTeamHolder holder, int position) {
        String PlayerTeamName = "";
        String PlayerCredit = "";
        boolean isSelected = false;
        int team1Count =0, team2Count =0,wk=0,ar=0,bat=0,ball=0;
        final PlayerListModel list = playerListModelList.get(holder.getAdapterPosition());


        if (sport_id.equals("1")){
            holder.txtWK.setText("Wicket Keeper");
            holder.txtBat.setText("Batsman");
            holder.txtAr.setText("All Rounder");
            holder.txtBowl.setText("Bowler");
        }else if (sport_id.equals("2")){
            holder.txtWK.setText("Goal Keeper");
            holder.txtBat.setText("Defender");
            holder.txtAr.setText("Mid Fielder");
            holder.txtBowl.setText("Forward");
        }
        else if (sport_id.equals("4")){
            holder.layCr.setVisibility(View.VISIBLE);
            holder.viewCr.setVisibility(View.VISIBLE);

            holder.txtWK.setText("PG");
            holder.txtBat.setText("SG");
            holder.txtAr.setText("SF");
            holder.txtBowl.setText("PF");
            holder.txtCr.setText("CR");
        }else if (sport_id.equals("6")){
            holder.layCr.setVisibility(View.GONE);
            holder.viewCr.setVisibility(View.GONE);
            holder.layBowl.setVisibility(View.GONE);
            holder.viewBowl.setVisibility(View.GONE);

            holder.txtWK.setText("Goal Keeper");
            holder.txtBat.setText("Defender");
            holder.txtAr.setText("Forward");
        }else if (sport_id.equals("7")){
            holder.layCr.setVisibility(View.GONE);
            holder.viewCr.setVisibility(View.GONE);
            holder.layBowl.setVisibility(View.GONE);
            holder.viewBowl.setVisibility(View.GONE);

            holder.txtWK.setText("Defender");
            holder.txtBat.setText("All Rounder");
            holder.txtAr.setText("Raider");
        }else if (sport_id.equals("3")){
            holder.txtWK.setText("OF");
            holder.txtBat.setText("IF");
            holder.txtAr.setText("PIT");
            holder.txtBowl.setText("CAT");
        }



        if (((SwipeTeamActivity) mContext).selectPosition == holder.getAdapterPosition()) {
            holder.team_select_img.setChecked(true);
        } else {
            holder.team_select_img.setChecked(false);
        }


      /*  if (list.getIsSelected().equalsIgnoreCase("Yes")) {

            holder.linear4.setEnabled(false);
            holder.linear4.setClickable(false);
            holder.team_select_img.setChecked(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.linear4.setForeground(mContext.getResources().getDrawable(R.drawable.disable_match));
            }

        } else {
            holder.linear4.setEnabled(true);
            holder.linear4.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.linear4.setForeground(mContext.getResources().getDrawable(R.drawable.transparent_view));
            }
        }*/


        holder.txtTeamname.setText(list.getTempTeamName());

        String c = list.getC_player_name();
       // String[] c_names = c.split(" ");
       // holder.c_first_name.setText(c_names[0]);
        holder.c_last_name.setText(c);

        String vc = list.getVc_player_name();
        //String[] vc_names = vc.split(" ");
        //holder.vc_first_name.setText(vc_names[0]);
        holder.vc_last_name.setText(vc);

        holder.txtTeam1Count.setText("  " + list.getTeam1_count());
        holder.txtTeam2Count.setText("  " + list.getTeam2_count());
        holder.txtTeam1Name.setText(preferences.getMatchModel().getTeam1Sname()+" :");
        holder.txtTeam2Name.setText(preferences.getMatchModel().getTeam2Sname()+" :");

        holder.txtBastman.setText("" + list.getBat_count());
        holder.txtWicketK.setText("" + list.getWk_count());
        holder.txtAllrounder.setText("" + list.getAr_count());
        holder.txtBowler.setText("" + list.getBowl_count());

        //CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.user_image);
        //CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.user_image);

        if (preferences.getMatchModel().getSportId().equals("1")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("2")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player1);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.football_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("4")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team1);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.basketball_team2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("3")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player1);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.baseball_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("6")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player1);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.handball_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("7")){
            if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam1,ApiManager.DOCUMENTS,list.getC_player_img(),R.drawable.kabaddi_player2);
            }
        }

        if (sport_id.equals("1")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team1_tshirt);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.ic_team2_tshirt);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("2")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player1);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.football_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("4")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team1);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.basketball_team2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("3")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player1);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.baseball_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("6")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player1);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.handball_player2);
            }
        }
        else if (preferences.getMatchModel().getSportId().equals("7")){
            if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam1())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player1);
            }else if (list.getVCTeam_id().equals(preferences.getMatchModel().getTeam2())){
                CustomUtil.loadImageWithGlide(mContext,holder.imgTeam2,ApiManager.DOCUMENTS,list.getVc_player_img(),R.drawable.kabaddi_player2);
            }
        }

        holder.linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApp.getClickStatus()) {
                    holder.team_select_img.setChecked(true);
                    notifyItemChanged(((SwipeTeamActivity) mContext).selectPosition);
                    ((SwipeTeamActivity) mContext).selectPosition = holder.getAdapterPosition();
                }
            }
        });

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, list.getTempTeamName(), list.getPlayerDetailList());
                    bottomSheetTeam.show(((SwipeTeamActivity) mContext).getSupportFragmentManager(),
                            BottomSheetTeam.TAG);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return playerListModelList.size();
    }

    public class SwitchTeamHolder extends RecyclerView.ViewHolder {
        public CardView matchCard;
        public TextView txtTeamname;

        public LinearLayout cardLinear;
        public LinearLayout linear4;
        public TextView txtCaption;
        public ImageView imgTeam1;
        public TextView txtTeam1Name;
        public TextView txtTeam1Count;
        public TextView txtTeam2Name;
        public TextView txtTeam2Count;
        public ImageView imgTeam2;
        public ImageView show;
        public TextView txtWicketK;
        public TextView txtBastman;
        public TextView txtAllrounder;
        public View viewCr,viewBowl;
        public CheckBox team_select_img;
        public TextView txtWK,txtBat,txtAr,txtBowl,txtCr,txtCenter;
        public TextView txtBowler,c_last_name,vc_last_name;//vc_first_name,c_first_name,
        public LinearLayout layCr,layBowl;

        public SwitchTeamHolder(@NonNull View itemView) {
            super(itemView);
            matchCard = (CardView)itemView.findViewById( R.id.match_card );
            txtTeamname = (TextView)itemView.findViewById( R.id.txtTeamname );

            viewCr = itemView.findViewById(R.id.viewCr);
            txtCenter = (TextView) itemView.findViewById(R.id.txtCenter);
            txtWK = (TextView) itemView.findViewById(R.id.txtWK);
            txtBat = (TextView) itemView.findViewById(R.id.txtBat);
            txtAr = (TextView) itemView.findViewById(R.id.txtAr);
            txtBowl = (TextView) itemView.findViewById(R.id.txtBowl);
            viewBowl = itemView.findViewById(R.id.viewBowl);
            layBowl = itemView.findViewById(R.id.layBowl);
            layCr = (LinearLayout) itemView.findViewById(R.id.layCr);
            txtCr = (TextView) itemView.findViewById(R.id.txtCr);

            cardLinear = (LinearLayout)itemView.findViewById( R.id.card_linear );
            linear4 = (LinearLayout)itemView.findViewById( R.id.linear4 );
            txtCaption = (TextView)itemView.findViewById( R.id.txtCaption );
            imgTeam1 = (ImageView)itemView.findViewById( R.id.img_team1 );
            txtTeam1Name = (TextView)itemView.findViewById( R.id.txt_team1_name );
            txtTeam1Count = (TextView)itemView.findViewById( R.id.txt_team1_count );
            txtTeam2Name = (TextView)itemView.findViewById( R.id.txt_team2_name );
            txtTeam2Count = (TextView)itemView.findViewById( R.id.txt_team2_count );
            imgTeam2 = (ImageView)itemView.findViewById( R.id.img_team2 );
            show = (ImageView)itemView.findViewById( R.id.show );
            txtWicketK = (TextView)itemView.findViewById( R.id.txtWicket_k );
            txtBastman = (TextView)itemView.findViewById( R.id.txtBastman );
            txtAllrounder = (TextView)itemView.findViewById( R.id.txtAllrounder );
            txtBowler = (TextView)itemView.findViewById( R.id.txtBowler );
//            c_first_name = (TextView)itemView.findViewById( R.id.c_first_name );
            c_last_name = (TextView)itemView.findViewById( R.id.c_last_name );
           // vc_first_name = (TextView)itemView.findViewById( R.id.vc_first_name );
            vc_last_name = (TextView)itemView.findViewById( R.id.vc_last_name );
            team_select_img = (CheckBox) itemView.findViewById( R.id.team_select_img );

        }
    }
}
