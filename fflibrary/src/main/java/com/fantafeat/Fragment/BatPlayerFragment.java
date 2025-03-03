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

public class BatPlayerFragment extends BaseFragment {

    private RecyclerView bat_list;
    private Fragment fragment;
    private List<PlayerModel> batList;
    private SelectPlayerMainAdapter selectPlayerAdapter;
    private String sportId="";


    public BatPlayerFragment(Fragment fragment,String sportId) {
        // Required empty public constructor
        //this.fragment = fragment;
        this.sportId = sportId;
    }

    public BatPlayerFragment() {
        //this.fragment = fragment;
    }

    public static BatPlayerFragment newInstance(Fragment fragment, String sportId) {//, Fragment fragment
        BatPlayerFragment myFragment=new BatPlayerFragment();
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

        int min=((CricketSelectPlayerFragment)fragment).bat_min;
        int max=((CricketSelectPlayerFragment)fragment).bat_max;

        boolean isEqual = min == max;

        if (sportId.equals("1")){
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" Batsmen");//1
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" - "+max+" Batsmen");//1
        }else if (sportId.equals("2")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" Defender");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" - "+max+" Defender");
        }else if (sportId.equals("4")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" Shooting Guard");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" - "+max+" Shooting Guard");
        }else if (sportId.equals("3")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" Infielders");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+ min+" - "+max+" Infielders");
        }else if (sportId.equals("6")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" Defenders");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" - "+max+" Defenders");
        }else if (sportId.equals("7")) {
            if (isEqual)
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" All-Rounder");
            else
                ((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select "+min+" - "+max+" All-Rounder");
        }
        //((CricketSelectPlayerFragment)fragment).desc_select_player.setText("Select 3 - 6 Batsmen");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bat_player, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        bat_list = view.findViewById(R.id.bat_list);

        batList = new ArrayList<>();

        for(PlayerModel model : ((CricketSelectPlayerFragment)fragment).playerModelList){
            if (sportId.equals("1")) {
                if(model.getPlayerType().equalsIgnoreCase("bat")){
                    batList.add(model);
                }
            }else if (sportId.equals("2")) {
                if (model.getPlayerType().equalsIgnoreCase("def")) {
                    batList.add(model);
                }
            }else if (sportId.equals("4")) {
                if (model.getPlayerType().equalsIgnoreCase("sg")) {
                    batList.add(model);
                }
            }else if (sportId.equals("3")) {
                if (model.getPlayerType().equalsIgnoreCase("if")) {
                    batList.add(model);
                }
            }else if (sportId.equals("6")) {
                if (model.getPlayerType().equalsIgnoreCase("def")) {
                    batList.add(model);
                }
            }else if (sportId.equals("7")) {
                if (model.getPlayerType().equalsIgnoreCase("ar")) {
                    batList.add(model);
                }
            }
        }

        List<LineupMainModel> playerModelList = new ArrayList<>();

        ArrayList<PlayerModel> teamXi=new ArrayList<>(Collections2.filter(batList, input ->
                input.getPlayingXi().equalsIgnoreCase("yes")));
        ArrayList<PlayerModel> teamNoXi=new ArrayList<>(Collections2.filter(batList, input ->
                (input.getPlayingXi().equalsIgnoreCase("no")) &&
                        !input.getOther_text().equalsIgnoreCase("substitute")));
        ArrayList<PlayerModel> teamSub=new ArrayList<>(Collections2.filter(batList, input ->
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
        bat_list.setLayoutManager(new LinearLayoutManager(mContext));
        bat_list.setAdapter(selectPlayerAdapter);

        HeaderItemDecoration itemDecoration=new HeaderItemDecoration((HeaderItemDecoration.StickyHeaderInterface) selectPlayerAdapter);

        if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("yes") ||
                preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("yes")){
            bat_list.addItemDecoration(itemDecoration);
        }else {
            bat_list.removeItemDecoration(itemDecoration);
        }

        /*selectPlayerAdapter = new SelectPlayerAdapter(batList,mContext,fragment,preferences);
        bat_list.setLayoutManager(new LinearLayoutManager(mContext));
        bat_list.setAdapter(selectPlayerAdapter);*/
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
            if (sportId.equals("1") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "bat")) {
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
            else if (sportId.equals("2") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "def")){
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
            else if (sportId.equals("6") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "def")){
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
            else if (sportId.equals("4") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "sg")){
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
            else if (sportId.equals("3") && CustomUtil.stringEqualsIgnore(model.getPlayerType(), "if")){
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
            if (sportId.equals("7")) {
                if (CustomUtil.stringEqualsIgnore(model.getPlayerType(), "ar")){
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