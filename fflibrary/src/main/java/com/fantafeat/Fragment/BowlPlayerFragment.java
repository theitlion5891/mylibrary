package com.fantafeat.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class BowlPlayerFragment extends BaseFragment {

    private RecyclerView bowl_list;
    private Fragment fragment;
    private List<PlayerModel> bowlList;
    private SelectPlayerMainAdapter selectPlayerAdapter;
    private String sportId="";


    public BowlPlayerFragment(Fragment fragment,String sportId) {
        // Required empty public constructor
        this.fragment = fragment;
        this.sportId = sportId;
    }

    public BowlPlayerFragment() {
        //this.fragment = fragment;
    }

    public static BowlPlayerFragment newInstance(Fragment fragment, String sportId) {//, Fragment fragment
        BowlPlayerFragment myFragment=new BowlPlayerFragment();
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

    @Override
    public void onResume() {
        super.onResume();
        int min=((CricketSelectPlayerFragment)fragment).bowl_min;
        int max=((CricketSelectPlayerFragment)fragment).bowl_max;
        boolean isEqual = min == max;

        if (sportId.equals("1")){
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" Bowlers");//3
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" - "+max+" Bowlers");//3
        }else if (sportId.equals("2")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" Forward");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" - "+max+" Forward");
        } else if (sportId.equals("4")) {
            if (isEqual)
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select "+ min+" Power Forward");
            else
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select "+ min+" - "+max+" Power Forward");
        }else if (sportId.equals("3")) {
            if (isEqual)
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select "+ min+" Catcher");
            else
                ((CricketSelectPlayerFragment) fragment).desc_select_player.setText("Select "+ min+" - "+max+" Catcher");
        }
        //((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select 3 - 6 Bowlers");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bowl_player, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        bowl_list = view.findViewById(R.id.bowl_list);

        bowlList = new ArrayList<>();
        for(PlayerModel model : ((CricketSelectPlayerFragment)fragment).playerModelList){
            if (sportId.equals("1")){
                if(model.getPlayerType().equalsIgnoreCase("bowl")){
                    bowlList.add(model);
                }
            }else if (sportId.equals("2")) {
                if (model.getPlayerType().equalsIgnoreCase("for")) {
                    bowlList.add(model);
                }
            }else if (sportId.equals("4")) {
                if (model.getPlayerType().equalsIgnoreCase("pf")) {
                    bowlList.add(model);
                }
            }else if (sportId.equals("3")) {
                if (model.getPlayerType().equalsIgnoreCase("cat")) {
                    bowlList.add(model);
                }
            }
        }

        List<LineupMainModel> playerModelList = new ArrayList<>();
        ArrayList<PlayerModel> teamXi=new ArrayList<>(Collections2.filter(bowlList, input ->
                input.getPlayingXi().equalsIgnoreCase("yes")));
        ArrayList<PlayerModel> teamNoXi=new ArrayList<>(Collections2.filter(bowlList, input ->
                (input.getPlayingXi().equalsIgnoreCase("no")) &&
                        !input.getOther_text().equalsIgnoreCase("substitute")));
        ArrayList<PlayerModel> teamSub=new ArrayList<>(Collections2.filter(bowlList, input ->
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
        bowl_list.setLayoutManager(new LinearLayoutManager(mContext));
        bowl_list.setAdapter(selectPlayerAdapter);

        HeaderItemDecoration itemDecoration=new HeaderItemDecoration((HeaderItemDecoration.StickyHeaderInterface) selectPlayerAdapter);

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
            bowl_list.addItemDecoration(itemDecoration);
        }else {
            bowl_list.removeItemDecoration(itemDecoration);
        }

        /*cricketSelectPlayerActivity.setDataToList(bowl_list,bowlList);*/
        /*selectPlayerAdapter = new SelectPlayerAdapter(bowlList,mContext,fragment,preferences);
        bowl_list.setLayoutManager(new LinearLayoutManager(mContext));
        bowl_list.setAdapter(selectPlayerAdapter);*/
    }

    public void adapterNotify(){
        if(selectPlayerAdapter != null){
            selectPlayerAdapter.notifyDataSetChanged();
        }
    }

    public void updateData(MyPreferences preferences,int teamFilter) {
        ((CricketSelectPlayerFragment)fragment).playerModelListTemp = new ArrayList<>();

        String team1=preferences.getMatchModel().getTeam1();
        String team2=preferences.getMatchModel().getTeam2();

        for (PlayerModel model : CustomUtil.emptyIfNull(((CricketSelectPlayerFragment)fragment).playerModelList)) {
            if (sportId.equals("1") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "bowl")) {
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
               /* if (CustomUtil.stringEqualsIgnore(team1, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                } else if (CustomUtil.stringEqualsIgnore(team2, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                }*/
            }
            else if (sportId.equals("2") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "for")) {
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
               /* if (CustomUtil.stringEqualsIgnore(team1, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                } else if (CustomUtil.stringEqualsIgnore(team2, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                }*/
            }
            else if (sportId.equals("4") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "pf")) {
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
               /* if (CustomUtil.stringEqualsIgnore(team1, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                } else if (CustomUtil.stringEqualsIgnore(team2, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                }*/
            }
            else if (sportId.equals("3") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "cat")) {
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
               /* if (CustomUtil.stringEqualsIgnore(team1, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                } else if (CustomUtil.stringEqualsIgnore(team2, model.getTeamId())) {
                    ((CricketSelectPlayerFragment)fragment).playerModelListTemp.add(model);
                }*/
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

        /*if (selectPlayerAdapter != null) {
            selectPlayerAdapter.updateList(((CricketSelectPlayerFragment)fragment).playerModelListTemp);
        }*/
    }

    @Override
    public void initClick() {

    }
}