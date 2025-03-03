package com.fantafeat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Activity.CricketSelectPlayerActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Adapter.LineupSelectMainAdapter;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.LineupMainModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.HeaderItemDecoration;
import com.fantafeat.util.LogUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;


public class LineupsSelectionFragment extends BaseFragment {

    private Fragment fragment;
    private List<PlayerModel> playerModelList;
    private String mParam2;
    private ImageView imgBack;
    private LinearLayout bottom_buttons;
    private TextView select_team_time,team1_count,team2_count,select_team_next,txtTitle;
    private long diff;
    private CountDownTimer countDownTimer;
    private Boolean isShow=false;
    public static BottomSheetDialog bottomSheetDialog = null;
    public Map<String, Integer> checkSelect;
    private LineupSelectMainAdapter adapterMain;

    public LineupsSelectionFragment(Fragment fragment) {
       this.fragment=fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_lineups_selection, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        playerModelList=new ArrayList<>(((CricketSelectPlayerFragment)fragment).playerModelList);
        checkSelect = new HashMap<>(((CricketSelectPlayerFragment)fragment).checkSelect);

        int blackColor=mContext.getResources().getColor(R.color.black_pure);
        int whiteColor=mContext.getResources().getColor(R.color.white_pure);
        MatchModel matchModel=preferences.getMatchModel();
        AtomicBoolean isPoint= new AtomicBoolean(true);

        imgBack=view.findViewById(R.id.imgBack);
        select_team_time=view.findViewById(R.id.select_team_time);
        bottom_buttons=view.findViewById(R.id.bottom_buttons);
        select_team_next=view.findViewById(R.id.select_team_next);
        txtTitle=view.findViewById(R.id.txtTitle);

        TextView txtPointBtn=view.findViewById(R.id.txtPointBtn);
        TextView txtSelByBtn=view.findViewById(R.id.txtSelByBtn);
        TextView team1_name=view.findViewById(R.id.team1_name);
        team1_name.setText(matchModel.getTeam1Sname());
        TextView team2_name=view.findViewById(R.id.team2_name);
        team2_name.setText(matchModel.getTeam2Sname());

        team1_count=view.findViewById(R.id.team1_count);
        team2_count=view.findViewById(R.id.team2_count);
        CircleImageView imgTeam1=view.findViewById(R.id.imgTeam1);
        CircleImageView imgTeam2=view.findViewById(R.id.imgTeam2);

        CustomUtil.loadImageWithGlide(mContext,imgTeam1, MyApp.imageBase+MyApp.document, matchModel.getTeam1Img(),R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(mContext,imgTeam2,MyApp.imageBase+MyApp.document, matchModel.getTeam2Img(),R.drawable.team_loading);

        team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
        team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

        txtTitle.setText(matchModel.getTeam1Sname()+" vs "+matchModel.getTeam2Sname());

        ArrayList<LineupMainModel> list=new ArrayList<>();

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
            List<PlayerModel> xiList1=new ArrayList<>();
            List<PlayerModel> notXiList1=new ArrayList<>();
            List<PlayerModel> substituteList1=new ArrayList<>();

            List<PlayerModel> xiList2=new ArrayList<>();
            List<PlayerModel> notXiList2=new ArrayList<>();
            List<PlayerModel> substituteList2=new ArrayList<>();

            for (PlayerModel model : CustomUtil.emptyIfNull(playerModelList)) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), model.getTeamId()) ){
                    if (model.getPlayingXi().equalsIgnoreCase("yes")){
                        xiList1.add(model);
                    } else {
                        if (model.getOther_text().equalsIgnoreCase("substitute")){
                            substituteList1.add(model);
                        }else
                            notXiList1.add(model);
                    }
                }
                else if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), model.getTeamId()) ){
                    if (model.getPlayingXi().equalsIgnoreCase("yes")){
                        xiList2.add(model);
                    } else {
                        if (model.getOther_text().equalsIgnoreCase("substitute")){
                            substituteList2.add(model);
                        }else
                            notXiList2.add(model);
                    }
                }
            }

            LineupMainModel model=new LineupMainModel();
            if (xiList1.size()>0 || xiList2.size()>0){
                model.setType(1);//Playing
                model.setXiType(1);
                model.setTitle("Announced");
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(1);
                Collections.sort(xiList1,new OrderDown());
                Collections.sort(xiList2,new OrderDown());
                model.setPlayerModelList1(xiList1);
                model.setPlayerModelList2(xiList2);
                list.add(model);
            }
            if (substituteList1.size()>0 || substituteList2.size()>0){
                model=new LineupMainModel();
                model.setType(2);//Substitute
                model.setXiType(2);
                model.setTitle("Substitute");
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(2);
                model.setPlayerModelList1(substituteList1);
                model.setPlayerModelList2(substituteList2);
                list.add(model);
            }
            if (notXiList1.size()>0 || notXiList2.size()>0){
                model=new LineupMainModel();
                model.setType(3);//not playing
                model.setXiType(3);
                model.setTitle("Unannounced");
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(3);
                model.setPlayerModelList1(notXiList1);
                model.setPlayerModelList2(notXiList2);
                list.add(model);
            }
        }
        else {
            //List<PlayerModel> xiList1=new ArrayList<>();
            List<PlayerModel> notXiList1=new ArrayList<>();
            List<PlayerModel> substituteList1=new ArrayList<>();

            //List<PlayerModel> xiList2=new ArrayList<>();
            List<PlayerModel> notXiList2=new ArrayList<>();
            List<PlayerModel> substituteList2=new ArrayList<>();

            for (PlayerModel model : CustomUtil.emptyIfNull(playerModelList)) {
                if (CustomUtil.stringEqualsIgnore(matchModel.getTeam1(), model.getTeamId()) ){
                    if (model.getOther_text().equalsIgnoreCase("")){
                        notXiList1.add(model);
                    }else
                        substituteList1.add(model);
                }
                else if (CustomUtil.stringEqualsIgnore(matchModel.getTeam2(), model.getTeamId()) ){
                    if (model.getOther_text().equalsIgnoreCase("")){
                        notXiList2.add(model);
                    }else
                        substituteList2.add(model);
                }
            }

            LineupMainModel model=new LineupMainModel();
          /*  if (xiList1.size()>0 || xiList2.size()>0){
                model.setType(1);//Playing
                model.setXiType(1);
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(1);
                Collections.sort(xiList1,new OrderDown());
                Collections.sort(xiList2,new OrderDown());
                model.setPlayerModelList1(xiList1);
                model.setPlayerModelList2(xiList2);
                list.add(model);
            }*/
            if (substituteList1.size()>0 || substituteList2.size()>0){
                model=new LineupMainModel();
                model.setType(2);//Substitute
                model.setXiType(2);
                model.setTitle("Played Last Match");
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(2);
                model.setPlayerModelList1(substituteList1);
                model.setPlayerModelList2(substituteList2);
                list.add(model);
            }
            if (notXiList1.size()>0 || notXiList2.size()>0){
                model=new LineupMainModel();
                model.setType(3);//not playing
                model.setXiType(3);
                model.setTitle("Other Players");
                list.add(model);
                model=new LineupMainModel();
                model.setType(4);
                model.setXiType(3);
                model.setPlayerModelList1(notXiList1);
                model.setPlayerModelList2(notXiList2);
                list.add(model);
            }
        }


        RecyclerView recyclerMainLineup=view.findViewById(R.id.recyclerMainLineup);
        recyclerMainLineup.setLayoutManager(new LinearLayoutManager(mContext));
        adapterMain = new LineupSelectMainAdapter(mContext,list,isPoint.get(), playerModel -> {
            //validateClick(playerModel);
        },this);
        recyclerMainLineup.setAdapter(adapterMain);

        recyclerMainLineup.addItemDecoration(new HeaderItemDecoration((HeaderItemDecoration.StickyHeaderInterface) adapterMain));

        txtSelByBtn.setOnClickListener(view1 -> {
            txtPointBtn.setBackgroundResource(R.drawable.transparent_view);
            txtPointBtn.setTextColor(blackColor);

            txtSelByBtn.setBackgroundResource(R.drawable.btn_primary);
            txtSelByBtn.setTextColor(whiteColor);

            isPoint.set(false);
            adapterMain.updatePoint(isPoint.get());
        });

        txtPointBtn.setOnClickListener(view1 -> {
            txtPointBtn.setBackgroundResource(R.drawable.btn_primary);
            txtPointBtn.setTextColor(whiteColor);

            txtSelByBtn.setBackgroundResource(R.drawable.transparent_view);
            txtSelByBtn.setTextColor(blackColor);
            isPoint.set(true);
            adapterMain.updatePoint(isPoint.get());
        });

        imgBack.setOnClickListener(view1 -> {
            RemoveFragment();
        });

        changeNextBg();

        setTimer();

        //setTimer();

        //changeNextBg();
    }

    public class OrderDown implements Comparator<PlayerModel> {
        @Override
        public int compare(PlayerModel o1, PlayerModel o2) {
            return Float.compare(CustomUtil.convertFloat(o1.getBatting_order_no()), CustomUtil.convertFloat(o2.getBatting_order_no()));
        }
    }

    @Override
    public void initClick() {
        bottom_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()){
                    if (((CricketSelectPlayerFragment)fragment).selected_list!=null && ((CricketSelectPlayerFragment)fragment).selected_list.size() > 0) {
                        BottomSheetTeam bottomSheetTeam = new BottomSheetTeam(mContext, "",((CricketSelectPlayerFragment)fragment).selected_list);
                        bottomSheetTeam.show(((CricketSelectPlayerActivity) mContext).getSupportFragmentManager(),
                                BottomSheetTeam.TAG);
                    }else {
                        CustomUtil.showTopSneakError(mContext,"Please Select Player First");
                    }
                }
            }
        });

        select_team_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (((CricketSelectPlayerFragment)fragment).selected_list!=null){
                        if (((CricketSelectPlayerFragment)fragment).selected_list.size() != ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                            CustomUtil.showTopSneakError(mContext, "Please Select "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" Players");
                        }
                        else {
                            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
                            for (PlayerModel playerModel : CustomUtil.emptyIfNull(playerModelList)) {
                                if (checkSelect.containsKey(playerModel.getPlayerId()) && checkSelect.get(playerModel.getPlayerId()).equals(1)) {
                                    ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                                }
                            }
                            if (((CricketSelectPlayerFragment)fragment).teamCreateType != 0) {
                                for (PlayerModel playerModel : CustomUtil.emptyIfNull(((CricketSelectPlayerFragment)fragment).playerSelectedList.getPlayerDetailList())) {
                                    for (PlayerModel model : CustomUtil.emptyIfNull(((CricketSelectPlayerFragment)fragment).selected_list)) {
                                        if (playerModel.getPlayerId().equals(model.getPlayerId())) {
                                            model.setIsCaptain(playerModel.getIsCaptain());
                                            model.setIsWiseCaptain(playerModel.getIsWiseCaptain());
                                        }
                                    }
                                }
                            }
                            Type menu = new TypeToken<List<PlayerModel>>() {
                            }.getType();
                            String json = gson.toJson(((CricketSelectPlayerFragment)fragment).selected_list, menu);

                            if (((CricketSelectPlayerFragment)fragment).teamCreateType == 0) {
                        /*AddNewFragment(ChoiceCVCFragment.newInstance(json, teamCreateType, "",contestData),
                                fragmentTag(10));*/
                                new FragmentUtil().addFragment((FragmentActivity) mContext,
                                        R.id.cricket_select_frame,
                                        SelectCVCFragment.newInstance(json,((CricketSelectPlayerFragment)fragment).teamCreateType,"",((CricketSelectPlayerFragment)fragment).contestData),
                                        fragmentTag(14),
                                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                            } else {
                                String oldPlayerSelected = gson.toJson(((CricketSelectPlayerFragment)fragment).selectPlayerListTemp);
                        /*AddNewFragment(ChoiceCVCFragment.newInstance(json, teamCreateType,oldPlayerSelected ,playerSelectedList.getId(),contestData),
                                fragmentTag(10));*/

                                new FragmentUtil().addFragment((FragmentActivity) mContext,
                                        R.id.cricket_select_frame,
                                        SelectCVCFragment.newInstance(json, ((CricketSelectPlayerFragment)fragment).teamCreateType,oldPlayerSelected ,
                                                ((CricketSelectPlayerFragment)fragment).playerSelectedList.getPlayerDetailList().get(0).getTempTeamId(),((CricketSelectPlayerFragment)fragment).contestData),
                                        fragmentTag(14),
                                        FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
                            }
                        }
                    }
                }
            }
        });
    }

    private void changeNextBg(){
        if (((CricketSelectPlayerFragment)fragment).selected_list!=null){
            if(((CricketSelectPlayerFragment)fragment).selected_list.size() != ((CricketSelectPlayerFragment)fragment).maxTeamPlayer){
                select_team_next.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
            }else{
                select_team_next.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_fill));
            }
        }else {
            select_team_next.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();

        ((CricketSelectPlayerFragment)fragment).reloadData();
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            Log.e(TAG, "onBindViewHolder: " + diff);
        } catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        /*if (matchModel.getMatchStatus().equals("Pending")) {*/
        countDownTimer = new CountDownTimer(diff, 1000) {
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
                    diff = CustomUtil.getFormater("0").format(elapsedDays) + "d " + CustomUtil.getFormater("0").format(elapsedHours) + "h";
                } else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("0").format(elapsedHours) + "h " + CustomUtil.getFormater("0").format(elapsedMinutes) + "m";
                } else {
                    diff = CustomUtil.getFormater("0").format(elapsedMinutes) + "m " + CustomUtil.getFormater("0").format(elapsedSeconds) + "s";
                }
                select_team_time.setText(diff);
            }

            public void onFinish() {

                timesOver();
                // Do something on finish
            }
        }.start();
        /*} else {
            holder.match_remain_time.setTextSize(14);
            holder.match_remain_time.setText(List.getMatchStatus());
        }*/
    }

    private void timesOver(){
        if (bottomSheetDialog==null){
            bottomSheetDialog = new BottomSheetDialog(mContext);
        }
        Button btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                ConstantUtil.isTimeOverShow=false;
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                        R.id.fragment_container,
                        new HomeFragment(),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);*/
            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow){
            ConstantUtil.isTimeOverShow=true;
            bottomSheetDialog.show();
        }

    }

    public boolean validateCricketClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");
                //team2_selected_players.setText("" + team2_selected);
                //show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "wk")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("WK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bat")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("BAT(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bowl")) {
                ((CricketSelectPlayerFragment)fragment).bowl -= 1;
                if (((CricketSelectPlayerFragment)fragment).bowl >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("BOWL(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("AR(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }

            /*if(((CricketSelectPlayerFragment)fragment).totalSelected==((CricketSelectPlayerFragment)fragment).maxTeamPlayer){
                ((CricketSelectPlayerFragment)fragment).next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_fill));
            }else{
                ((CricketSelectPlayerFragment)fragment).next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
            }*/
            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }

            changeNextBg();

            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "wk")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > ((CricketSelectPlayerFragment)fragment).bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) +
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > ((CricketSelectPlayerFragment)fragment).bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Wicket-keeper allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("WK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bat")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) +
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bat_max+" Batsmen are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("BAT(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) +
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Bowlers");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                } else if (((CricketSelectPlayerFragment)fragment).ar == ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" All-Rounders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("AR(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "bowl")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) +
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Batsmen");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Wicket-Keeper");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" All-Rounder");
                } else if (((CricketSelectPlayerFragment)fragment).bowl == ((CricketSelectPlayerFragment)fragment).bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bowl_max+" Bowlers are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bowl += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("BOWL(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                // total_player_selected.setText(totalSelected+"/((CricketSelectPlayerFragment)fragment).maxTeamPlayer");
                ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                /*if(((CricketSelectPlayerFragment)fragment).totalSelected==((CricketSelectPlayerFragment)fragment).maxTeamPlayer){
                    ((CricketSelectPlayerFragment)fragment).next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.green_bg_round_fill));
                }else{
                    ((CricketSelectPlayerFragment)fragment).next_for_select_captain.setBackground(mContext.getResources().getDrawable(R.drawable.gray_bg_round_fill));
                }*/

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }

                changeNextBg();

                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateFootballClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("GK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                ((CricketSelectPlayerFragment)fragment).bowl -= 1;
                if (((CricketSelectPlayerFragment)fragment).bowl >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("FOR(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("MID(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }

            changeNextBg();

            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if (( ((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (( ((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > ((CricketSelectPlayerFragment)fragment).bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > ((CricketSelectPlayerFragment)fragment).bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Goal-keepers are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("GK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min >= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Only "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper allowed");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bat_max+" Defenders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "mid")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                } else if (((CricketSelectPlayerFragment)fragment).ar == ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" Mid-Fielders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("MID(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "for")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min >= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Mid-Fielders");
                } else if (((CricketSelectPlayerFragment)fragment).bowl == ((CricketSelectPlayerFragment)fragment).bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bowl_max+" Forwards are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bowl += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("FOR(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment). team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                        ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                //total_player_selected.setText(totalSelected+"/11");
                ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                changeNextBg();

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBasketballClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (((CricketSelectPlayerFragment)fragment).checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("PG(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("SG(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("SF(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                ((CricketSelectPlayerFragment)fragment).bowl -= 1;
                if (((CricketSelectPlayerFragment)fragment).bowl >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("PF(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                ((CricketSelectPlayerFragment)fragment).cr -= 1;
                if (((CricketSelectPlayerFragment)fragment).cr >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(4).setText("CR(" + ((CricketSelectPlayerFragment)fragment).cr + ")");
                }
            }

            changeNextBg();

            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }

            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pg")) {
                //5-4-3
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)
                                + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat))
                        && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                    else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                    else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                } //2-5-3
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+
                                (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) +(((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)
                                + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)
                                + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected)
                        <= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                }else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Point Guards are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("PG(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sg")) {

                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                } //5-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)
                                + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                ( ((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)
                                + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+
                                (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) +(((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guards allowed");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+ ((CricketSelectPlayerFragment)fragment).bat_max+" Shooting Guards are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("SG(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "sf")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                } //5-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) +
                                (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+
                                (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min
                        && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +(((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)
                                + (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)
                                + (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min
                        && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guards");
                }else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                } else if (((CricketSelectPlayerFragment)fragment).ar ==  ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" Small Forward are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("SF(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pf")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)
                                + (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                } //5-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                                (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)
                                + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ ( ((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +(((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min
                        && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)+ (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr)) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min && ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).cr_min > (((CricketSelectPlayerFragment)fragment).cr)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min
                        && ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                }else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).cr_min - ((CricketSelectPlayerFragment)fragment).cr) &&
                        ((CricketSelectPlayerFragment)fragment).cr <= ((CricketSelectPlayerFragment)fragment).cr_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).cr_min+" Center");
                } else if (((CricketSelectPlayerFragment)fragment).bowl == ((CricketSelectPlayerFragment)fragment).bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bowl_max+" Forwards are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bowl += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("PF(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cr")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                                (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+
                                (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                } //5-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //3-4-2
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                                (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                } //2-5-3
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }

                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +(((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)+ (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }else if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+ (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)+ (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min && ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)+
                                (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guard");
                    }else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                    }
                }
                else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Shooting Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Point Guards");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Small Forward");
                }else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Power Forward");
                } else if (((CricketSelectPlayerFragment)fragment).cr == ((CricketSelectPlayerFragment)fragment).cr_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).cr_max+" Center are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).cr += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(4).setText("CR(" + ((CricketSelectPlayerFragment)fragment).cr + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                        ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                //total_player_selected.setText(((CricketSelectPlayerFragment)fragment).totalSelected+"/((CricketSelectPlayerFragment)fragment).maxTeamPlayer");
                ((CricketSelectPlayerFragment)fragment). team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment). team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                changeNextBg();

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateHandballClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (((CricketSelectPlayerFragment)fragment).checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("GK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("FWD(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }

            changeNextBg();

            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "gk")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Forward");
                    }
                }  else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Forward");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Goal-keeper are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("GK(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Forward");
                    }
                } /*else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <= (1 - bowl) && bowl <= 1) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 1 Forward");
                }*/ else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Forward");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bat_max+" Defenders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "fwd")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min
                        && ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Goal-keeper");
                } else if (((CricketSelectPlayerFragment)fragment).ar == ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" Forward are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("FWD(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                        ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                //total_player_selected.setText(((CricketSelectPlayerFragment)fragment).totalSelected+"/11");
                ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                changeNextBg();

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateKabaddiClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");
                //team2_selected_players.setText("" + team2_selected);
                // show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("AR(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("RAID(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }

            changeNextBg();

            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "def")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) +
                                (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" All-Rounder");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Raider");
                    }
                }  else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" All-Rounder");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Raider");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Defenders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("DEF(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "ar")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > (((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Raider");
                    }
                } /*else if ((maxTeamPlayer - totalSelected) <= (1 - bowl) && bowl <= 1) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least 1 Forward");
                }*/ else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Raider");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bat_max+" All-Rounders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("AR(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "raid")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Defenders");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" All-Rounder");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Defenders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" All-Rounder");
                } else if (((CricketSelectPlayerFragment)fragment).ar == ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" Raiders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("RAID(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }
            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                        ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                //total_player_selected.setText(totalSelected+"/11");
                ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                changeNextBg();

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment). txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

    public boolean validateBaseballClick(PlayerModel playerModel) {
        if (((CricketSelectPlayerFragment)fragment).selected_list == null) {
            ((CricketSelectPlayerFragment)fragment).selected_list = new ArrayList<>();
        }
        if (checkSelect.get(playerModel.getPlayerId()) == 1) {
//-----------------------------------------------------
            int i = -1,j=0;
            for( PlayerModel playerModel1 : ((CricketSelectPlayerFragment)fragment).selected_list){
                if(playerModel1.getPlayerId().equals(playerModel.getPlayerId())){
                    i = j;
                }
                j++;
            }
            ((CricketSelectPlayerFragment)fragment).selected_list.remove(i);
            playerModel.setSelected(false);
            ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
            //selected_list.remove(playerModel);
//--------------------------------------------------------
            checkSelect.put(playerModel.getPlayerId(), 0);
            ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 0);

            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team1_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team1_selected_dots -= 1;

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");

                //team1_selected_players.setText("" + team1_selected);
                //show_team1_dots(team1_selected_dots);
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                ((CricketSelectPlayerFragment)fragment).team2_selected -= 1;
                ((CricketSelectPlayerFragment)fragment).team2_selected_dots -= 1;

                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                //team2_selected_players.setText("" + team2_selected);
                //show_team2_dots(team2_selected_dots);
            }
            ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots);
            ((CricketSelectPlayerFragment)fragment).total_remain += 1;
            ((CricketSelectPlayerFragment)fragment).totalSelected -= 1;
            ((CricketSelectPlayerFragment)fragment).total_points += CustomUtil.convertFloat(playerModel.getPlayerRank());
            ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
            //total_player_selected.setText(totalSelected+"/11");
            ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
            ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                ((CricketSelectPlayerFragment)fragment).wk -= 1;
                if (((CricketSelectPlayerFragment)fragment).wk >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("OF(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                ((CricketSelectPlayerFragment)fragment).bat -= 1;
                if (((CricketSelectPlayerFragment)fragment).bat >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("IF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                ((CricketSelectPlayerFragment)fragment).bowl -= 1;
                if (((CricketSelectPlayerFragment)fragment).bowl >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("CAT(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                }
            }
            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                ((CricketSelectPlayerFragment)fragment).ar -= 1;
                if (((CricketSelectPlayerFragment)fragment).ar >= 0) {
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("PIT(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                }
            }

            changeNextBg();

            if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
            }else {
                ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        else {
            boolean isSelected = false;
            if (((CricketSelectPlayerFragment)fragment).totalSelected == ((CricketSelectPlayerFragment)fragment).maxTeamPlayer) {
                CustomUtil.showTopSneakError(mContext, "Maximum "+((CricketSelectPlayerFragment)fragment).maxTeamPlayer+" players are allowed, click NEXT to proceed");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team1_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }
            if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId()) &&
                    ((CricketSelectPlayerFragment)fragment).team2_selected >= ((CricketSelectPlayerFragment)fragment).eachTeamMax) {
                CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).eachTeamMax+" players are allowed from a team");
                return false;
            }

            if ((((CricketSelectPlayerFragment)fragment).total_points - Float.parseFloat(playerModel.getPlayerRank())) < 0) {
                CustomUtil.showTopSneakError(mContext, "You don't have enough credit to pick this player");
                return false;
            }

            if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "of")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) +
                                (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > ((CricketSelectPlayerFragment)fragment).bat) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min > ((CricketSelectPlayerFragment)fragment).ar) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > ((CricketSelectPlayerFragment)fragment).bowl) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                } else if (((CricketSelectPlayerFragment)fragment).wk == ((CricketSelectPlayerFragment)fragment).wk_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).wk_max+" Outfielders allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).wk += 1;
                    isSelected = true;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(0).setText("OF(" + ((CricketSelectPlayerFragment)fragment).wk + ")");
                    /// select_tab.getTabAt(0).setText("WK(" + wk + ")");
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "if")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment). bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                } else if (((CricketSelectPlayerFragment)fragment).bat == ((CricketSelectPlayerFragment)fragment).bat_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bat_max+" Infielders are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bat += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(1).setText("IF(" + ((CricketSelectPlayerFragment)fragment).bat + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "pit")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) +
                                (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > ( ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min >= (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) + (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bowl_min > (((CricketSelectPlayerFragment)fragment).bowl)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bowl_min - ((CricketSelectPlayerFragment)fragment).bowl) &&
                        ((CricketSelectPlayerFragment)fragment).bowl <= ((CricketSelectPlayerFragment)fragment).bowl_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bowl_min+" Catcher");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                } else if (((CricketSelectPlayerFragment)fragment).ar == ((CricketSelectPlayerFragment)fragment).ar_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).ar_max+" Pitchers are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).ar += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(2).setText("PIT(" + ((CricketSelectPlayerFragment)fragment).ar + ")");
                    isSelected = true;
                }
            }
            else if (CustomUtil.stringEqualsIgnore(playerModel.getPlayerType(), "cat")) {
                if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                                (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min <= (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min >= (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) + (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk)) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    if (((CricketSelectPlayerFragment)fragment).wk_min > (((CricketSelectPlayerFragment)fragment).wk)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                    } else if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        ((((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) +
                                (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min &&
                        ((CricketSelectPlayerFragment)fragment). ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    if (((CricketSelectPlayerFragment)fragment).ar_min <= (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                    } else if (((CricketSelectPlayerFragment)fragment).bat_min > (((CricketSelectPlayerFragment)fragment).bat)) {
                        CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                    }
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).bat_min - ((CricketSelectPlayerFragment)fragment).bat) &&
                        ((CricketSelectPlayerFragment)fragment).bat <= ((CricketSelectPlayerFragment)fragment).bat_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).bat_min+" Infielders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).wk_min - ((CricketSelectPlayerFragment)fragment).wk) &&
                        ((CricketSelectPlayerFragment)fragment).wk <= ((CricketSelectPlayerFragment)fragment).wk_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).wk_min+" Outfielders");
                } else if ((((CricketSelectPlayerFragment)fragment).maxTeamPlayer - ((CricketSelectPlayerFragment)fragment).totalSelected) <=
                        (((CricketSelectPlayerFragment)fragment).ar_min - ((CricketSelectPlayerFragment)fragment).ar) &&
                        ((CricketSelectPlayerFragment)fragment).ar <= ((CricketSelectPlayerFragment)fragment).ar_min) {
                    CustomUtil.showTopSneakError(mContext, "Pick at least "+((CricketSelectPlayerFragment)fragment).ar_min+" Pitcher");
                } else if (((CricketSelectPlayerFragment)fragment).bowl == ((CricketSelectPlayerFragment)fragment).bowl_max) {
                    CustomUtil.showTopSneakError(mContext, "Max "+((CricketSelectPlayerFragment)fragment).bowl_max+" Catcher are allowed");
                } else {
                    ((CricketSelectPlayerFragment)fragment).bowl += 1;
                    ((CricketSelectPlayerFragment)fragment).select_player_tab.getTabAt(3).setText("CAT(" + ((CricketSelectPlayerFragment)fragment).bowl + ")");
                    isSelected = true;
                }
            }

            if (isSelected) {
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam1(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team1_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team1_selected_dots += 1;
                    //show_team1_dots(team1_selected_dots);
                }
                if (CustomUtil.stringEqualsIgnore(preferences.getMatchModel().getTeam2(), playerModel.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).team2_selected += 1;
                    ((CricketSelectPlayerFragment)fragment).team2_selected_dots += 1;
                    // show_team2_dots(team2_selected_dots);
                }
                ((CricketSelectPlayerFragment)fragment).show_team1_dots(((CricketSelectPlayerFragment)fragment).team1_selected_dots+((CricketSelectPlayerFragment)fragment).team2_selected_dots);
                ((CricketSelectPlayerFragment)fragment).total_remain -= 1;
                //team1_selected_players.setText("" + team1_selected);
                //team2_selected_players.setText("" + team2_selected);
                ((CricketSelectPlayerFragment)fragment).selected_list.add(playerModel);
                playerModel.setSelected(true);
                ((CricketSelectPlayerActivity)mContext).updateSelectedList(((CricketSelectPlayerFragment)fragment).selected_list);
                checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).checkSelect.put(playerModel.getPlayerId(), 1);
                ((CricketSelectPlayerFragment)fragment).totalSelected += 1;
                ((CricketSelectPlayerFragment)fragment).total_points -= CustomUtil.convertFloat(playerModel.getPlayerRank());
                ((CricketSelectPlayerFragment)fragment).credit_left.setText("" + ((CricketSelectPlayerFragment)fragment).total_points);
                // total_player_selected.setText(totalSelected+"/11");
                ((CricketSelectPlayerFragment)fragment).team1_cnt.setText(((CricketSelectPlayerFragment)fragment).team1_selected_dots+"");
                ((CricketSelectPlayerFragment)fragment).team2_cnt.setText(((CricketSelectPlayerFragment)fragment).team2_selected_dots+"");

                team1_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team1_selected+")");
                team2_count.setText("(" + ((CricketSelectPlayerFragment)fragment).team2_selected+")");

                changeNextBg();

                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }

                return true;
            } else {
                if (((CricketSelectPlayerFragment)fragment).selected_list.size()>0){
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.VISIBLE);
                }else {
                    ((CricketSelectPlayerFragment)fragment).txtClear.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
    }

}