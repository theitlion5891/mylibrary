package com.fantafeat.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Adapter.Team1AnnounceAdapter;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.MyPreferences;
import com.fantafeat.Adapter.Team2AnnounceAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamAnnounce extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheet;
    public View view;
    public Context context;
    private List<PlayerModel> playerModelList;
    public static final String TAG = "BottomSheetTeamAnnounce";
    private CircleImageView imgTeam1,imgTeam2;
    private ImageView imgClose;
    private TextView txtTeam1,txtTeam2;
    private RecyclerView recyclerTeam1,recyclerTeam2;
    private MyPreferences preferences;

    public TeamAnnounce(Context context,List<PlayerModel> playerModelList,MyPreferences preferences) {
        this.playerModelList = playerModelList;
        this.context = context;
        this.preferences = preferences;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.team_announce, null);
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(getWindowHeight());

        initView(view);

        return bottomSheet;
    }

    private void initView(View view) {
        imgTeam1=view.findViewById(R.id.imgTeam1);
        imgTeam2=view.findViewById(R.id.imgTeam2);
        txtTeam1=view.findViewById(R.id.txtTeam1);
        txtTeam2=view.findViewById(R.id.txtTeam2);
        recyclerTeam1=view.findViewById(R.id.recyclerTeam1);
        recyclerTeam2=view.findViewById(R.id.recyclerTeam2);
        imgClose=view.findViewById(R.id.imgClose);

        MatchModel matchModel=preferences.getMatchModel();

        List<PlayerModel> team1List=new ArrayList<>();
        List<PlayerModel> team2List=new ArrayList<>();

        for (PlayerModel model:playerModelList){
            if (model.getPlayingXi().equalsIgnoreCase("yes")){
                if (model.getTeamId().equalsIgnoreCase(matchModel.getTeam1())){
                    team1List.add(model);
                }
                if (model.getTeamId().equalsIgnoreCase(matchModel.getTeam2())){
                    team2List.add(model);
                }
            }

        }

        if (team1List.size()<11){
            int t1Size=team1List.size();
            for (int i=t1Size;i<11;i++){
                team1List.add(new PlayerModel());
            }
        }

        if (team2List.size()<11){
            int t2Size=team2List.size();
            for (int i=t2Size;i<11;i++){
                team2List.add(new PlayerModel());
            }
        }

        CustomUtil.loadImageWithGlide(context,imgTeam1, ApiManager.TEAM_IMG,matchModel.getTeam1Img(),R.drawable.team_loading);
        CustomUtil.loadImageWithGlide(context,imgTeam2, ApiManager.TEAM_IMG,matchModel.getTeam2Img(),R.drawable.team_loading);


        txtTeam1.setText(matchModel.getTeam1Sname());
        txtTeam2.setText(matchModel.getTeam2Sname());

        String sportId=preferences.getMatchModel().getSportId();

        Team1AnnounceAdapter team1AnnounceAdapter=new Team1AnnounceAdapter(team1List,context,sportId);
        recyclerTeam1.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerTeam1.setAdapter(team1AnnounceAdapter);

        Team2AnnounceAdapter team2AnnounceAdapter=new Team2AnnounceAdapter(team2List,context,sportId);
        recyclerTeam2.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerTeam2.setAdapter(team2AnnounceAdapter);

        imgClose.setOnClickListener(v -> {
            bottomSheet.dismiss();
        });


    }

    private int getWindowHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
