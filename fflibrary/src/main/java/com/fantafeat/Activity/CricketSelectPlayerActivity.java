package com.fantafeat.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.fantafeat.Fragment.CricketSelectPlayerFragment;
import com.fantafeat.Fragment.LineupsSelectionFragment;
import com.fantafeat.Fragment.SelectCVCFragment;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.PlayerModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CricketSelectPlayerActivity extends BaseActivity {

    FrameLayout frameLayout;
    CricketSelectPlayerFragment cricketSelectPlayerFragment=null;
    private List<PlayerModel> selected_list;
   // private Socket mSocket= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_select_player);
        Window window = CricketSelectPlayerActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        frameLayout = findViewById(R.id.cricket_select_frame);
        Intent intent = getIntent();
        int teamCreateType = intent.getIntExtra(PrefConstant.TEAMCREATETYPE,3);
        if(teamCreateType==0) {
            //cricketSelectPlayerFragment=new CricketSelectPlayerFragment();

            new FragmentUtil().addFragment(CricketSelectPlayerActivity.this,
                    R.id.cricket_select_frame,
                    CricketSelectPlayerFragment.newInstance(),
                    fragmentTag(13),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);
        }else if(teamCreateType==2){
            String json = intent.getStringExtra("data");
            //cricketSelectPlayerFragment=new CricketSelectPlayerFragment(json);
            new FragmentUtil().addFragment(CricketSelectPlayerActivity.this,
                    R.id.cricket_select_frame,
                    CricketSelectPlayerFragment.newInstance(json),
                    fragmentTag(13),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);
        }else if(teamCreateType==1){
            //edit
            PlayerListModel list = gson.fromJson(intent.getStringExtra("team"),PlayerListModel.class);
            //cricketSelectPlayerFragment=new CricketSelectPlayerFragment(list, true);

            new FragmentUtil().addFragment((FragmentActivity) mContext,
                    R.id.cricket_select_frame,
                    CricketSelectPlayerFragment.newInstance(list, true),
                    fragmentTag(13),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);
        }else if(teamCreateType==-1){
            //clone
            PlayerListModel list = gson.fromJson(intent.getStringExtra("team"),PlayerListModel.class);
            //cricketSelectPlayerFragment=new CricketSelectPlayerFragment(list, false);
            new FragmentUtil().addFragment((FragmentActivity) mContext,
                    R.id.cricket_select_frame,
                    CricketSelectPlayerFragment.newInstance(list, false),
                    fragmentTag(13),
                    FragmentUtil.ANIMATION_TYPE.CUSTOM);
        }
    }

    public void updateSelectedList(List<PlayerModel> selected_list){
        this.selected_list=selected_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow=false;

       /* mSocket = MyApp.getInstance().getSocketInstance();
        if (mSocket!=null){
            if (!mSocket.connected())
                mSocket.connect();
            try {
                JSONObject obj=new JSONObject();
                if (preferences.getUserModel()!=null){
                    obj.put("user_id",preferences.getUserModel().getId());
                }
                obj.put("title","createteam");
                mSocket.emit("connect_user",obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }*/
    }

    public void initClick() {

    }

    public String fragmentTag(int position) {
        String[] drawer_tag = getResources().getStringArray(R.array.stack);
        return drawer_tag[position];
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.cricket_select_frame);
        if (currentFrag == null) {
            return;
        }
        Log.e(TAG, "onBackPressed: " + currentFrag.getTag() + " " + fragmentTag(14));
        if (currentFrag.getTag().equals(fragmentTag(14))) {
            ((SelectCVCFragment) currentFrag).RemoveFragment();
        } else if (currentFrag.getTag().equals(fragmentTag(35))) {
            ((LineupsSelectionFragment) currentFrag).RemoveFragment();
        } else {
            Log.e(TAG, "onBackPressed: 1");
            //  finish();
            if (MyApp.getClickStatus()) {
                if (selected_list != null && selected_list.size() > 0) {
                    discardDialog();
                } else {
                    finish();
                }
            }
        }
    }

    private void discardDialog() {
        Button btn_ok,btn_no;
        View view = LayoutInflater.from(mContext).inflate(R.layout.discard_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        btn_ok = view.findViewById(R.id.btn_ok);
        btn_no = view.findViewById(R.id.btn_no);
        btn_ok.setOnClickListener(view1 -> {
            bottomSheetDialog.dismiss();
            finish();
        });
        btn_no.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /*public void setDataToList(RecyclerView view, List<PlayerModel> player_model) {

        selectPlayerAdapter = new SelectPlayerAdapter(player_model, mContext, CricketSelectPlayerActivity.this, preferences);
        view.setLayoutManager(new LinearLayoutManager(mContext));
        view.setAdapter(selectPlayerAdapter);
    }
*/

    /* private void createTablayout() {
        viewPagerAdapter = new ViewPagerAdapter(CricketSelectPlayerActivity.this);
        select_player_viewpager.setAdapter(viewPagerAdapter);

        final String[] title = {"WK", "BAT", "AR", "BOWL"};

        new TabLayoutMediator(select_player_tab, select_player_viewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(title[position] + "(0)");
                    }
                }).attach();
    }*/
}