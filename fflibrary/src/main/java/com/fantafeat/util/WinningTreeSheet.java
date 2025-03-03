package com.fantafeat.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Activity.ContestListActivity;
import com.fantafeat.Adapter.MatchRankAdapter;
import com.fantafeat.R;
import com.fantafeat.Session.MyApp;
import com.fantafeat.Session.MyPreferences;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class WinningTreeSheet extends BottomSheetDialogFragment {

    Context mContext;
    MyPreferences preferences;
    private static final String TAG = "WinningTreeSheet";
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheet;
    private String pool, winningTree,amount;

    public WinningTreeSheet(@NonNull Context mContext, String pool, String winningTree,String amount) {
        this.mContext = mContext;
        this.pool = pool;
        this.winningTree = winningTree;
        this.amount = amount;
        preferences = MyApp.getMyPreferences();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_match_rank, null);
        bottomSheet.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);

        setViewCreated(view);
        return bottomSheet;

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((ContestListActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void setViewCreated(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.match_rank);
        TextView winnin_tree_text = (TextView) view.findViewById(R.id.winnin_tree_text);
        TextView txtPrise = (TextView) view.findViewById(R.id.txtPrise);

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(winningTree);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtPrise.setText("PRIZE - "+amount);
        winnin_tree_text.setText(preferences.getUpdateMaster().getWinnig_tree_text());

        MatchRankAdapter matchRank = new MatchRankAdapter(mContext, jsonArray);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(matchRank);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }
}
