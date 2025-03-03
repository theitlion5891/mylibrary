package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.MVPlayerAdapter;
import com.fantafeat.Fragment.GroupContestListFragment;
import com.fantafeat.Fragment.GroupContestMyListFragment;
import com.fantafeat.Model.GroupContestModel;
import com.fantafeat.Model.GroupModel;
import com.fantafeat.Model.MatchModel;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SinglesContestActivity extends BaseActivity {

    private Toolbar toolbar;
    private GroupModel groupModel;
    public ImageView mToolBarBack,toolbar_wallet;
    private TextView match_title, timer,txtGrpName;
    private MatchModel selected_match;
   // private LinearLayout layPlayers;
    private long diff;
    private CountDownTimer countDownTimer;
    private BottomSheetDialog bottomSheetDialog = null;
    private static ViewPager2 viewPager;
    public TabLayout tabLayout;
    public JSONObject join_count;
    private GroupContestListFragment groupContestListFragment;
    private GroupContestMyListFragment groupContestMyListFragment;
    private final List<String> mFragmentTitleList = new ArrayList<>();
    GroupContestModel.ContestDatum contestData;
    int mainPosition=-1;
    float  use_deposit = 0;
    float  use_transfer =  0;
    float  use_winning =  0;
    float  use_donation_deposit = 0;
    float  useBonus = 0;
    float  useCoin = 0;
    float  amtToAdd = 0;
    private boolean isContest=true;
    public boolean is_match_after=false;
    private MVPlayerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singles_contest);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));
        toolbar = findViewById(R.id.contest_list_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("group_model")){
            groupModel= (GroupModel) getIntent().getSerializableExtra("group_model");
        }

        if (getIntent().hasExtra("is_match_after")){
            is_match_after= getIntent().getBooleanExtra("is_match_after",false);
        }

        if(preferences.getMatchModel()!=null){
            selected_match = preferences.getMatchModel();
        }

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        mToolBarBack = findViewById(R.id.toolbar_back);
        toolbar_wallet = findViewById(R.id.toolbar_wallet);
        match_title = findViewById(R.id.match_title);
        timer = findViewById(R.id.timer);

        if (is_match_after){
            mFragmentTitleList.add("My Contest");
            tabLayout.setVisibility(View.GONE);
        }else {
            mFragmentTitleList.add("Contests");
            mFragmentTitleList.add("My Contest");
            tabLayout.setVisibility(View.VISIBLE);
        }

        viewPager.setAdapter(createContestAdapter());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(mFragmentTitleList.get(position));
                    viewPager.setCurrentItem(tab.getPosition(),false);
                }).attach();

        // Disable swipe changes and allow to change only by pressing on tab
        viewPager.setUserInputEnabled(ApiManager.isPagerSwipe);
        ConstantUtil.reduceDragSensitivity(viewPager);
        if (is_match_after){
            viewPager.setOffscreenPageLimit(1);
        }else {
            viewPager.setOffscreenPageLimit(2);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    isContest=true;
                }else if(tab.getPosition() == 1){
                    isContest=false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(selected_match.getMatchTitle()!=null){
            match_title.setText(selected_match.getTeam1Sname()+" vs "+selected_match.getTeam2Sname());
        }

        initClick();

        if (!is_match_after){
            setTimer();
        }else {
            String matchDate = "";
            String matchTime = "";
            Date date = null;

            SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat matchFormate = MyApp.changedFormat("dd-MMM-yy");
            SimpleDateFormat matchTimeFormate = MyApp.changedFormat("hh:mm a");
            try {
                date = format.parse(preferences.getMatchModel().getRegularMatchStartTime());

                matchTime = matchTimeFormate.format(date);
                matchDate = matchFormate.format(date);

                diff = date.getTime() - MyApp.getCurrentDate().getTime();

            }
            catch (ParseException e) {
                LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
                e.printStackTrace();
            }
            String strDate=matchDate + " " + matchTime+"";
            timer.setText(strDate);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!is_match_after)
            countDownTimer.cancel();
    }

    @Override
    public void initClick() {
        mToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AddDepositActivity.class);
                intent.putExtra("isJoin",true);
                intent.putExtra("depositAmt","");
                startActivity(intent);
            }
        });
    }

    public void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat matchFormate = MyApp.changedFormat("dd MMM yyyy");
        try {
            selected_match = preferences.getMatchModel();

            date = format.parse(selected_match.getRegularMatchStartTime());
            matchDate = matchFormate.format(date);

            diff = date.getTime() - MyApp.getCurrentDate().getTime();

            Log.e(TAG, "onBindViewHolder: " + diff);
        } catch (ParseException e) {
            LogUtil.e(TAG, "onBindViewHolder: " + e.toString());
            e.printStackTrace();
        }

        if (countDownTimer != null){
            countDownTimer.cancel();
        }

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
                    diff = CustomUtil.getFormater("00").format(elapsedDays) + "d " + CustomUtil.getFormater("00").format(elapsedHours) + "h Left";
                } else if (elapsedHours > 0) {
                    diff = CustomUtil.getFormater("00").format(elapsedHours) + "h " + CustomUtil.getFormater("00").format(elapsedMinutes) + "m";
                } else {
                    diff = CustomUtil.getFormater("00").format(elapsedMinutes) + "m " + CustomUtil.getFormater("00").format(elapsedSeconds) + "s";
                }
                timer.setText(diff);
            }

            public void onFinish() {

                timesOver();
                // Do something on finish
            }
        }.start();

    }

    public void timesOver(){
        Button btn_ok;

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok =  view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.isTimeOverShow=false;
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(mContext,HomeActivity.class);
                startActivity(intent);
                /*new FragmentUtil().resumeFragment((FragmentActivity) mContext,
                        R.id.fragment_container,
                        new HomeFragment(),
                        ((HomeActivity) mContext).fragmentTag(0),
                        FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);*/
            }
        });
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow){
            ConstantUtil.isTimeOverShow=true;
            bottomSheetDialog.show();
        }
    }

    private RecyclerView.Adapter createContestAdapter() {
        return new ContestListViewPagerAdapter(this,mContext);
    }

    public void confirmTeam(GroupContestModel.ContestDatum contestData, int mainPosition,boolean isEdit) {
        if (is_match_after){
            return;
        }

        this.contestData=null;
        this.mainPosition=-1;
        /*use_deposit = use_transfer = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float Contest_fee = CustomUtil.convertFloat(contestData.getEntryFee());
        float orgContestEntry = CustomUtil.convertFloat(contestData.getEntryFee());
        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        //final float transfer_bal = CustomUtil.convertFloat(preferences.getUserModel().getTransferBal());
        float usableBonus = 0,usableCoin=0;

        usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
        usableCoin = CustomUtil.convertFloat(contestData.getDefault_ff_coins());

        useBonus = ((Contest_fee * usableBonus) / 100);

        useCoin = ((Contest_fee * usableCoin) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (useCoin > coin) {
            useCoin = coin;
        }

        if (Contest_fee - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
            Contest_fee = Contest_fee - useBonus;
        }

        if (Contest_fee - useCoin >= 0) {
            Contest_fee = Contest_fee - useCoin;
        }

        if ((Contest_fee - deposit) < 0) {
            use_deposit = Contest_fee;
        }
        else {
            use_deposit = deposit;
            //use_transfer = transfer_bal;
            use_winning = Contest_fee - deposit;
        }

        LogUtil.e(TAG, "onClick: deposit: " + deposit +"\n useBonus:" + useBonus + "\n Winning:" + winning);
        LogUtil.e(TAG, "onClick: Total: " + (deposit + useBonus + winning)+"\n Contest_fee:" + Contest_fee);*/

        if (!isEdit){
            if (!isValidForJoin(contestData,1)) {//((deposit +  winning) - Contest_fee) < 0

                this.contestData=contestData;
                this.mainPosition=mainPosition;

                double amt=Math.ceil(amtToAdd);//Contest_fee-(deposit + winning);

                if (amt<1){
                    amt=1;
                }

                String patableAmt=CustomUtil.getFormater("0.00").format(amt);
                MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS,false);
                Intent intent = new Intent(mContext,AddDepositActivity.class);
                intent.putExtra("isJoin",true);
                intent.putExtra("depositAmt",patableAmt);
                intent.putExtra("contestData",gson.toJson(contestData));
                startActivity(intent);
            }
            else {


                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_conirm_grp_joining, null);

                bottomSheetDialog = new BottomSheetDialog(mContext);

                bottomSheetDialog.setContentView(view);
                //((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
                if (bottomSheetDialog!=null && !bottomSheetDialog.isShowing())
                    bottomSheetDialog.show();

                ProgressBar progress = view.findViewById(R.id.progress);
                progress.setVisibility(View.VISIBLE);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() ->   progress.setVisibility(View.GONE), 800);

                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> initConfirmDialog(view, contestData,isEdit), 100);
            }
        } else {


            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_conirm_grp_joining, null);

            bottomSheetDialog = new BottomSheetDialog(mContext);

            bottomSheetDialog.setContentView(view);
            //((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
            if (bottomSheetDialog!=null && !bottomSheetDialog.isShowing())
                bottomSheetDialog.show();

            LinearLayout layAmtDetails = view.findViewById(R.id.layAmtDetails);
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

            layAmtDetails.setVisibility(View.GONE);
            txtTitle.setText("UPDATE CONTEST");

            ProgressBar progress = view.findViewById(R.id.progress);
            progress.setVisibility(View.VISIBLE);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() ->   progress.setVisibility(View.GONE), 800);

            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> initConfirmDialog(view, contestData,isEdit), 100);
        }

    }

    private void initConfirmDialog(View view,GroupContestModel.ContestDatum contestData,boolean isEdit){
        TextView join_contest_fee, join_use_deposit, join_use_borrowed, join_use_rewards,
                join_use_winning, join_user_pay;
        final CheckBox join_donation_select;
        TextView join_donation_text,join_contest;
        final EditText edtQty;
        final LinearLayout layQty;

        LayoutInflater  layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        join_contest = (TextView) view.findViewById(R.id.join_contest_btn);
        join_contest_fee = (TextView) view.findViewById(R.id.join_contest_fee);
        join_use_deposit = (TextView) view.findViewById(R.id.join_use_deposit);
        //join_use_borrowed = (TextView) view.findViewById(R.id.join_use_borrowed);
        join_use_rewards = (TextView) view.findViewById(R.id.join_use_rewards);
        join_use_winning = (TextView) view.findViewById(R.id.join_use_winning);
        join_user_pay = (TextView) view.findViewById(R.id.join_user_pay);
        edtQty = view.findViewById(R.id.edtQty);
       // layPlayers = view.findViewById(R.id.layPlayers);
        layQty = view.findViewById(R.id.layQty);
        SeekBar seekQty = view.findViewById(R.id.seekQty);
        RecyclerView recyclerList = view.findViewById(R.id.recyclerList);
        LinearLayout layBonus = view.findViewById(R.id.layBonus);

        if (ConstantUtil.is_bonus_show){
            layBonus.setVisibility(View.VISIBLE);
        }else {
            layBonus.setVisibility(View.GONE);
        }

        layQty.setVisibility(View.GONE);
        seekQty.setVisibility(View.GONE);

        join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+useCoin)));
        //join_use_borrowed.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_transfer));
        join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
        join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
        // LogUtil.e("resval",useBonus+"   "+CustomUtil.getFormater("00.000").format(useBonus));
        join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contestData.getEntryFee())));// Contest_fee+ useBonus
        join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit+use_winning+useBonus+useCoin)));// Contest_fee+ useBonus

       // layPlayers.removeAllViews();

        List<GroupModel.PlayersDatum> playersData=new ArrayList<>(groupModel.getPlayersData());

        String userId=preferences.getUserModel().getId();
        for (int i=0;i<playersData.size();i++) {
            GroupModel.PlayersDatum player = playersData.get(i);

            player.setDisable(false);
            //player.setJoinMe(false);
            player.setChecked(false);

            if (!TextUtils.isEmpty(contestData.getPlayerSpotWiseData())) {
                try {
                    JSONArray selectedArray = new JSONArray(contestData.getPlayerSpotWiseData());
                    for (int j = 0; j < selectedArray.length(); j++) {
                        JSONObject obj = selectedArray.optJSONObject(j);
                        if (obj.optString("player_id").equalsIgnoreCase(player.getPlayerId())) {
                            player.setDisable(true);
                            if (userId.equalsIgnoreCase(obj.optString("user_id"))) {
                                player.setJoinMe(true);
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }

        adapter=new MVPlayerAdapter(mContext, playersData, player -> {
            if (!player.isDisable()){
                if (!player.isDisable()){
                    for (int j=0;j< playersData.size();j++){
                        GroupModel.PlayersDatum player1= playersData.get(j);
                        player1.setChecked(false);
                    }
                    player.setChecked(true);
                    //confirmTeam(contestData,mainPosition,false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        recyclerList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerList.setAdapter(adapter);

        /*for (GroupModel.PlayersDatum player : contestData.getPlayers()) {
            View addView;
            if (player.isDisable()){
                addView = layoutInflater.inflate(R.layout.selected_player_group, null);
            }else {
                addView = layoutInflater.inflate(R.layout.group_player_item, null);
            }

            addView.setLayoutParams(param);

            TextView playerName =  addView.findViewById(R.id.player_name);
            TextView player_type = addView.findViewById(R.id.player_type);
            ImageView player_img = addView.findViewById(R.id.player_img);
            ImageView inPlaying = addView.findViewById(R.id.inPlaying);
            LinearLayout mainLay = addView.findViewById(R.id.linearLayout3);

            CustomUtil.loadImageWithGlide(mContext,player_img, ApiManager.TEAM_IMG,player.getPlayerImage(),R.drawable.ic_team1_tshirt);

            if (player.isChecked()){
                mainLay.setBackgroundResource(R.drawable.selected_green_player);
            }else {
                if (player.isDisable()){
                    mainLay.setBackgroundResource(R.drawable.gray_border_selected);
                }else {
                    mainLay.setBackgroundResource(R.drawable.gray_border);
                }
            }

            playerName.setText(player.getPlayerName().replace(" ","\n"));
            player_type.setText(player.getPlayerType());

            if (MyApp.getMyPreferences().getMatchModel().getTeamAXi().equalsIgnoreCase("Yes") ||
                    MyApp.getMyPreferences().getMatchModel().getTeamBXi().equalsIgnoreCase("Yes")) {
                if (!player.getPlayingXi().equalsIgnoreCase("Yes")) {
                    inPlaying.setVisibility(View.VISIBLE);
                } else {
                    inPlaying.setVisibility(View.GONE);
                }
            }

            addView.setOnClickListener(v -> {
                if (!player.isDisable()){
                    for (int j=0;j<contestData.getPlayers().size();j++){
                        GroupModel.PlayersDatum player1=contestData.getPlayers().get(j);
                        if (!player1.isDisable()){
                            LinearLayout mainLay1 = layPlayers.getChildAt(j).findViewById(R.id.linearLayout3);
                            mainLay1.setBackgroundResource(R.drawable.gray_border);
                            player1.setChecked(false);
                        }
                    }
                    player.setChecked(true);
                    mainLay.setBackgroundResource(R.drawable.selected_green_player);
                    layPlayers.invalidate();
                }else {
                    CustomUtil.showTopSneakWarning(mContext,"Please select other player, This Player is selected by other user.");
                }
            });

            layPlayers.addView(addView);
        }*/


        final float finalUseBonus = useBonus;
        join_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApp.getClickStatus()) {

                    SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date.before(MyApp.getCurrentDate())) {
                        Toast.makeText(mContext, "Time Up! Match Started", Toast.LENGTH_LONG).show();
                    } else {
                        GroupModel.PlayersDatum checkedModel=null;
                        for (int i=0;i<playersData.size();i++){
                            GroupModel.PlayersDatum player=playersData.get(i);
                            if (player.isChecked()){
                                checkedModel=player;
                            }
                        }

                        int qty=Integer.parseInt(edtQty.getText().toString().trim());

                        if (checkedModel==null){
                            CustomUtil.showTopSneakWarning(mContext,"Please select Player");
                            // Toast.makeText(mContext, "Please select Player", Toast.LENGTH_LONG).show();
                        }
                        else if (qty<=0){
                            CustomUtil.showTopSneakWarning(mContext,"Please enter atlease 1 quantity");
                            //Toast.makeText(mContext, "Please enter atlease 1 quantity", Toast.LENGTH_LONG).show();
                        }
                        else {
                            bottomSheetDialog.dismiss();
                            joinContest(contestData,qty,checkedModel,isEdit);
                        }
                    }
                }
            }
        });

    }

    private boolean isValidForJoin(GroupContestModel.ContestDatum contestData, int qty){
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit  = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
        float usableBBCoins = CustomUtil.convertFloat(contestData.getDefault_ff_coins());

        float usableBonus1 = 0;

        usableBonus1 = usableBonus;
        finalEntryFee = finalEntryFee * qty;

        useBonus = ((finalEntryFee * usableBonus1) / 100);
        useCoin = ((finalEntryFee * usableBBCoins) / 100);

        if (useBonus > bonus) {
            useBonus = bonus;
        }

        if (useCoin > bb_coin) {
            useCoin = bb_coin;
        }

        if (finalEntryFee - useBonus >= 0) {
            finalEntryFee = finalEntryFee - useBonus;
        }

        if (finalEntryFee - useCoin >= 0) {
            finalEntryFee = finalEntryFee - useCoin;
        }

        if ((finalEntryFee - deposit) < 0) {
            use_deposit = finalEntryFee;
        }
        else {
            use_deposit = deposit;
            use_winning = finalEntryFee - deposit;
        }

        float calReward=0,calCoin=0;

        if (bonus>0){
            if (useBonus<=bonus){
                calReward=useBonus;
            }else if (useBonus>bonus){
                calReward=bonus;
            }else {
                calReward=0;
            }
        }
        else {
            calReward=0;
        }

        if (bb_coin>0){
            if (useCoin<=bb_coin){
                calCoin=useCoin;
            }else if (useCoin>bb_coin){
                calCoin=bb_coin;
            }else {
                calCoin=0;
            }
        }
        else {
            calCoin=0;
        }

        float calBalance1 = deposit +  winning + calReward + calCoin;
        float totalCharge=useBonus+use_deposit+use_winning+useCoin;

        amtToAdd = totalCharge-calBalance1;

        LogUtil.e("resp","deposit:"+deposit+"\nwinning:"+winning+"\nbb_coin:"+bb_coin+"\nuse_deposit:"+use_deposit
                +"\nuse_transfer:"+use_transfer+"\nuse_winning:"+use_winning+"\nuse_bb_coin:"+useCoin+"\nuseBonus:"+useBonus);

        if (calBalance1 < totalCharge) {
            return false;
        }
        return true;
    }

    private void joinContest(GroupContestModel.ContestDatum contestData, int qty, GroupModel.PlayersDatum selectedPlayer,boolean isEdit) {
        //{"user_id":"122", "match_id":"17115", "display_type":"3", "grp_id":"1", "con_id":"1", "ref_by_no":"",
        // "player_details":[{"player_id":"101", "use_deposit_amt":"0", "use_bonus_amt":"0", "use_win_amt":"0","joined_spot":"10","ref_by_no":""},
        // {"player_id":"101", "use_deposit_amt":"0", "use_bonus_amt":"0", "use_win_amt":"0","joined_spot":"10","ref_by_no":""}]}



        DecimalFormat format=CustomUtil.getFormater("00.0000");

        JSONObject mainObj=new JSONObject();
        JSONObject playerObj=new JSONObject();
        JSONArray player_details=new JSONArray();

        try {
            playerObj.put("player_id",selectedPlayer.getPlayerId());
            playerObj.put("use_deposit_amt",  format.format(use_deposit));
            playerObj.put("use_bonus_amt", format.format(useBonus));
            playerObj.put("use_win_amt", format.format(use_winning));
            playerObj.put("use_ff_coins_amt", format.format(useCoin));
            playerObj.put("ref_by_no", preferences.getUserModel().getRefBy());
            playerObj.put("joined_spot", qty);
            player_details.put(playerObj);
            mainObj.put("player_details",player_details);
            mainObj.put("user_id",preferences.getUserModel().getId());
            mainObj.put("match_id",preferences.getMatchModel().getId());
            mainObj.put("display_type",groupModel.getDisplayType());
            mainObj.put("grp_id",groupModel.getId());
            mainObj.put("con_id",contestData.getId());
            mainObj.put("ref_by_no",preferences.getUserModel().getRefBy());
        }catch (Exception e){
            e.printStackTrace();
        }

        LogUtil.e("resp",mainObj.toString());

        String url="";
        if (isEdit){
            url=ApiManager.MATCH_WISE_GROUP_JOIN_EDIT;
        }else {
            url=ApiManager.MATCH_WISE_GROUP_JOIN;
        }

        HttpRestClient.postJSON(mContext, true, url, mainObj, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                if (responseBody.optBoolean("status")) {

                    if (!isEdit){
                        UserModel user = preferences.getUserModel();

                        float deposit_bal = CustomUtil.convertFloat(user.getDepositBal()) - use_deposit;
                        float coin_bal = CustomUtil.convertFloat(user.getFf_coin()) - useCoin;
                        //float transfer_bal = CustomUtil.convertFloat(user.getTransferBal()) - use_transfer;
                        float bonus_bal = CustomUtil.convertFloat(user.getBonusBal()) - useBonus;
                        float winning_bal = CustomUtil.convertFloat(user.getWinBal()) - use_winning;

                        user.setDepositBal(String.valueOf(deposit_bal));
                        //user.setTransferBal(String.valueOf(transfer_bal));
                        user.setBonusBal(String.valueOf(bonus_bal));
                        user.setWinBal(String.valueOf(winning_bal));
                        user.setFf_coin(String.valueOf(coin_bal));

                        float total;
                        if (ConstantUtil.is_bonus_show){
                            total =  CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) +
                                    CustomUtil.convertFloat(user.getBonusBal());

                        }else {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }

                      /*  float total = CustomUtil.convertFloat(user.getDepositBal()) +
                                CustomUtil.convertFloat(user.getWinBal()) +
                                //CustomUtil.convertFloat(user.getTransferBal()) +
                                CustomUtil.convertFloat(user.getFf_coin()) +
                                CustomUtil.convertFloat(user.getBonusBal());*/

                        user.setTotal_balance(total);

                        preferences.setUserModel(user);
                    }

                    CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));
                    if (isContest){
                        if (groupContestListFragment!=null){
                            groupContestListFragment.getContests();
                        }
                    }else {
                        if (groupContestMyListFragment!=null){
                            groupContestMyListFragment.getContests();
                        }
                    }

                }
                else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    public class ContestListViewPagerAdapter extends FragmentStateAdapter {

        private MatchModel current_match;
        Fragment fragment;
        Context mContext;

        public ContestListViewPagerAdapter(@NonNull FragmentActivity fragmentActivity,Context mContext) {
            super(fragmentActivity);
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (is_match_after){
                 groupContestMyListFragment= GroupContestMyListFragment.newInstance(groupModel,"my");
                return  groupContestMyListFragment;
            }else {
                if(position == 0) {
                    groupContestListFragment= GroupContestListFragment.newInstance(groupModel,"");
                    return  groupContestListFragment;
                }else if(position == 1) {
                    groupContestMyListFragment= GroupContestMyListFragment.newInstance(groupModel,"my");
                    return  groupContestMyListFragment;
                }
            }

            return null;
        }

        @Override
        public int getItemCount() {
            if (is_match_after){
                return 1;
            }else
                return 2;

        }

    }

}