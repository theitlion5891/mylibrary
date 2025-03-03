package com.fantafeat.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Adapter.TeamBreakupAdapter;
import com.fantafeat.R;
import com.fantafeat.Session.MyPreferences;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;


public class TeamBreakup extends BottomSheetDialogFragment {
    private JSONObject teamData;
    private Context context;
    BottomSheetDialog bottomSheet;
    private RecyclerView recyclerTeams;
    private Button btnJoin;
    public View view;
    public static final String TAG = "BreakTeam";

    private TeamBreakupAdapter adapter;
    BottomSheetBehavior bottomSheetBehavior;
    public MyPreferences preferences;

    public TeamBreakup(Context context, JSONObject teamData, MyPreferences preferences) {
        this.teamData = teamData;
        this.context = context;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheet.setCancelable(false);
        view = View.inflate(getContext(), R.layout.team_breakup, null);
        bottomSheet.setContentView(view);
        CardView layMain = view.findViewById(R.id.layMain);
        ViewGroup.LayoutParams params = layMain.getLayoutParams();
        layMain.setLayoutParams(params);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(getWindowHeight());
        setViewCreated(view);
        return bottomSheet;
    }

    private int getWindowHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void setViewCreated(View view) {

        btnJoin=view.findViewById(R.id.btnJoin);
        btnJoin.setVisibility(View.GONE);
        recyclerTeams=view.findViewById(R.id.recyclerTeams);

        try {
            recyclerTeams.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
            adapter=new TeamBreakupAdapter(context,teamData.optJSONArray("team_array"));
            recyclerTeams.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  btnJoin.setOnClickListener(v->{
            if(MyApp.getClickStatus()) {
                if (bottomSheet.isShowing()){
                    bottomSheet.dismiss();
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date.before(MyApp.getCurrentDate())) {
                    Toast.makeText(context, "Time Up! Match Started", Toast.LENGTH_LONG).show();
                } else {
                    joinContest(teamData);
                       *//* if (!isDonation && join_donation_select.isChecked()) {
                            Toast.makeText(mContext, "Insufficient Balance For Donation", Toast.LENGTH_LONG).show();
                        } else {
                            //joinContest();
                            joinContest(mainObj);
                        }*//*
                }
            }
        });*/

    }



}
