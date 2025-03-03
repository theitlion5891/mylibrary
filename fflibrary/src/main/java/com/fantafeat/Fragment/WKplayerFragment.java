package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantafeat.Adapter.SelectPlayerMainAdapter;
import com.fantafeat.Model.LineupMainModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.HeaderItemDecoration;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

public class WKplayerFragment extends BaseFragment {

    private RecyclerView wk_list;
    private List<PlayerModel> wkList;
    private SelectPlayerMainAdapter selectPlayerAdapter;
    private String sportId="";
    Fragment fragment;

    public WKplayerFragment() {
    }

    public WKplayerFragment(Fragment fragment, String sportId) {
        this.fragment = fragment;
        this.sportId = sportId;
    }

    public static WKplayerFragment newInstance(Fragment fragment, String sportId) {//, Fragment fragment
        WKplayerFragment myFragment=new WKplayerFragment();
        myFragment.fragment = fragment;
        Bundle args = new Bundle();
        args.putString("sportId", sportId);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("sportId")){
                this.sportId=getArguments().getString("sportId");
            }
        }
    }

    /*public WKplayerFragment( Fragment fragment) {
        // Required empty public constructor
        this.fragment = fragment;
    }*/


    @Override
    public void onResume() {
        super.onResume();
        int min=((CricketSelectPlayerFragment)fragment).wk_min;
        int max=((CricketSelectPlayerFragment)fragment).wk_max;
        boolean isEqual = min == max;

        if (sportId.equals("1")){
            if (isEqual) {
               /* if (mListener!=null){
                    mListener.onUpdateErrorMessage("Select " + min + " Wicket-Keepers");
                }*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Wicket-Keepers");
            }
            else {
                /*if (mListener!=null){
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Wicket-Keepers");
                }*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Wicket-Keepers");
            }
        }else if (sportId.equals("2")) {
            if (isEqual) {
              /*  if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " Goal Keeper");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Goal Keeper");
            }
            else {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Goal Keeper");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Goal Keeper");
            }
        }else if (sportId.equals("4")) {
            if (isEqual) {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " Point Guard");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Point Guard");
            }
            else {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Point Guard");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Point Guard");
            }
        }else if (sportId.equals("3")) {
            if (isEqual) {
               /* if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " Outfielders");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Outfielders");
            }
            else {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Outfielders");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Outfielders");
            }
        }else if (sportId.equals("6")) {
            if (isEqual) {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " Goal Keeper");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Goal Keeper");
            }
            else {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Goal Keeper");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Goal Keeper");
            }
        }else if (sportId.equals("7")) {
            if (isEqual) {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " Defenders");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " Defenders");
            }
            else {
                /*if (mListener!=null)
                    mListener.onUpdateErrorMessage("Select " + min + " - " + max + " Defenders");*/
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select " + min + " - " + max + " Defenders");
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w_kplayer, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        wk_list = view.findViewById(R.id.wk_list);
        Log.e("plyrname", "updateData: " +sportId);
        wkList = new ArrayList<>();


        for (PlayerModel model : ((CricketSelectPlayerFragment)fragment).playerModelList) {
            if (sportId.equals("1") && model.getPlayerType().equalsIgnoreCase("wk")){
                wkList.add(model);
            }else if (sportId.equals("2") && model.getPlayerType().equalsIgnoreCase("gk")) {
                wkList.add(model);
            }else if (sportId.equals("4") && model.getPlayerType().equalsIgnoreCase("pg")) {
                wkList.add(model);
            }else if (sportId.equals("3") && model.getPlayerType().equalsIgnoreCase("of")) {
                wkList.add(model);
            }else if (sportId.equals("6") && model.getPlayerType().equalsIgnoreCase("gk")) {
                wkList.add(model);
            }else if (sportId.equals("7") && model.getPlayerType().equalsIgnoreCase("def")) {
                wkList.add(model);
            }
        }

        List<LineupMainModel> playerModelList = new ArrayList<>();
        ArrayList<PlayerModel> teamXi=new ArrayList<>(Collections2.filter(wkList, input ->
                input.getPlayingXi().equalsIgnoreCase("yes")));
        ArrayList<PlayerModel> teamNoXi=new ArrayList<>(Collections2.filter(wkList, input ->
                (input.getPlayingXi().equalsIgnoreCase("no")) &&
                        !input.getOther_text().equalsIgnoreCase("substitute")));
        ArrayList<PlayerModel> teamSub=new ArrayList<>(Collections2.filter(wkList, input ->
                input.getOther_text().equalsIgnoreCase("substitute")));

        LineupMainModel model=new LineupMainModel();

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){

            model.setType(1);//Playing

            if (teamXi.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }

            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(1);
            model.setPlayerModelList1(teamXi);
            playerModelList.add(model);

            model=new LineupMainModel();
            if (teamSub.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }
            model.setType(2);//Substitute
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(2);
            model.setPlayerModelList1(teamSub);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(3);//not playing
            if (teamNoXi.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(3);
            model.setPlayerModelList1(teamNoXi);
            playerModelList.add(model);
        }
        else {
            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(1);
            model.setPlayerModelList1(teamXi);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(2);
            model.setPlayerModelList1(teamSub);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(3);
            model.setPlayerModelList1(teamNoXi);
            playerModelList.add(model);

        }

        selectPlayerAdapter=new SelectPlayerMainAdapter(mContext,playerModelList,fragment,sportId);
        wk_list.setLayoutManager(new LinearLayoutManager(mContext));
        wk_list.setAdapter(selectPlayerAdapter);

        HeaderItemDecoration itemDecoration=new HeaderItemDecoration((HeaderItemDecoration.StickyHeaderInterface) selectPlayerAdapter);

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
            wk_list.addItemDecoration(itemDecoration);
        }else {
            wk_list.removeItemDecoration(itemDecoration);
        }

       /* selectPlayerAdapter = new SelectPlayerAdapter(wkList,mContext,fragment,preferences);
        wk_list.setLayoutManager(new LinearLayoutManager(mContext));
        wk_list.setAdapter(selectPlayerAdapter);*/
        //cricketSelectPlayerActivity.setDataToList(wk_list, wkList);
    }

    public void adapterNotify(){
        if(selectPlayerAdapter != null){
            selectPlayerAdapter.notifyDataSetChanged();
        }
    }

    public void updateData(MyPreferences preferences,int teamFilter) {
        Log.e("plyrname", "updateData: "+sportId );
        ((CricketSelectPlayerFragment)fragment).playerModelListTemp = new ArrayList<>();
        //((CricketSelectPlayerFragment)fragment).playerModelListTemp.clear();

        String team1=preferences.getMatchModel().getTeam1();
        String team2=preferences.getMatchModel().getTeam2();

        for (PlayerModel model : CustomUtil.emptyIfNull(((CricketSelectPlayerFragment)fragment).playerModelList)) {

            if (sportId.equals("1") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "wk")) {
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
            else if (sportId.equals("4") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "pg")){
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
            else if (sportId.equals("3") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "of")){
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
            else if (sportId.equals("2") && model.getPlayerType().equalsIgnoreCase("gk")){
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
            else if (sportId.equals("6") && model.getPlayerType().equalsIgnoreCase("gk")){
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
            else if (sportId.equals("7") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "def")){
                switch (teamFilter){
                    case 1:{
                        if (team1.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 2:{
                        if (team2.equals(model.getTeamId())) {
                            ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        }
                        break;
                    }
                    case 3:{
                        ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                        break;
                    }
                }
            }
        }

        ArrayList<PlayerModel> teamXi=new ArrayList<>(Collections2.filter(((CricketSelectPlayerFragment)fragment).playerModelListTemp, input ->
                input.getPlayingXi().equalsIgnoreCase("yes")));
        ArrayList<PlayerModel> teamNoXi=new ArrayList<>(Collections2.filter(((CricketSelectPlayerFragment)fragment).playerModelListTemp, input ->
                (input.getPlayingXi().equalsIgnoreCase("no")) &&
                        !input.getOther_text().equalsIgnoreCase("substitute")));
        ArrayList<PlayerModel> teamSub=new ArrayList<>(Collections2.filter(((CricketSelectPlayerFragment)fragment).playerModelListTemp, input ->
                input.getOther_text().equalsIgnoreCase("substitute")));

        List<LineupMainModel> playerModelList = new ArrayList<>();
        LineupMainModel model=new LineupMainModel();

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){

            model.setType(1);//Playing

            if (teamXi.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }

            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(1);
            model.setPlayerModelList1(teamXi);
            playerModelList.add(model);

            model=new LineupMainModel();
            if (teamSub.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }
            model.setType(2);//Substitute
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(2);
            model.setPlayerModelList1(teamSub);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(3);//not playing
            if (teamNoXi.size()>0){
                model.setXiType(1);
            }else {
                model.setXiType(0);
            }
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(3);
            model.setPlayerModelList1(teamNoXi);
            playerModelList.add(model);
        }
        else {
            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(1);
            model.setPlayerModelList1(teamXi);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(2);
            model.setPlayerModelList1(teamSub);
            playerModelList.add(model);

            model=new LineupMainModel();
            model.setType(4);
            model.setXiType(3);
            model.setPlayerModelList1(teamNoXi);
            playerModelList.add(model);

        }

        if (selectPlayerAdapter != null) {
            selectPlayerAdapter.updateList(playerModelList);
        }
    }

    @Override
    public void initClick() {}

}