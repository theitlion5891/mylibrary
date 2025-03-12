package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Adapter.ContestQuantityAdapter;
import com.fantafeat.Fragment.LeaderBoardFragment;
import com.fantafeat.Fragment.PrizePoolFragment;
import com.fantafeat.Model.ContestModel;
import com.fantafeat.Model.ContestQuantityModel;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.Model.PassModel;
import com.fantafeat.Model.PlayerListModel;
import com.fantafeat.Model.SportsModel;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.BottomSheetTeam;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.OfferListSheet;
import com.fantafeat.util.PrefConstant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.fantafeat.util.CustomUtil.cleanUpCommas;

public class LeaderBoardActivity extends BaseActivity {

    TextView contest_price_pool, contest_total_winner, contest_entry, contest_left_team, contest_total_team, txtBonus, contest_offer_text,contest_confirm,
            contest_left_spots_text, contest_offer_price, contest_total_spots_text, match_title, timer, txtSingleMultiple, txtFirstWin, txtWinPer;
    ProgressBar contest_progress;
    // CardView contest_card;
    ImageView mToolBarBack, toolbar_fav, imgOfferText, imgSingleMultiple, imgShare, imgConfirm;//excel_download
    LinearLayout contest_full_linear, teams_left_linear, join_btn, layConfirm, layBonus, laySingleMultiple, layFirstWin;

    private TabLayout tabs;

    private ViewPager2 viewpager;

    ContestModel.ContestDatum list;

    ArrayList<String> FragmentName = new ArrayList<>();

    private long diff;
    private CountDownTimer countDownTimer;
    private BottomSheetDialog bottomSheetDialog = null;
    private Boolean is_same_team_cancel = false;
    float use_deposit = 0;
    float use_transfer = 0;
    float use_winning = 0;
    float use_donation_deposit = 0;
    float useBonus = 0;
    float useCoin = 0;
    float amtToAdd = 0;

   // private Socket mSocket = null;

    private String selectedState = "";
    private Spinner spinState;
    private ArrayList<String> cityName, cityId;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private String selectedGender = "Select Gender";

    private static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    String[] p;

    public void initClick() {
        mToolBarBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (preferences.getPrefBoolean("join_contest")) {
            preferences.setPref("join_contest", false);
            Log.e(TAG, "onResume: ");
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            String model = gson.toJson(list);
            intent.putExtra("model", model);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        Window window = LeaderBoardActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.blackPrimary));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        }
        else {
            p = storge_permissions;
        }

        Intent intent = getIntent();
        String model1 = intent.getStringExtra("model");
        if (intent.hasExtra("is_same_team_cancel")) {
            is_same_team_cancel = intent.getBooleanExtra("is_same_team_cancel", false);
        }
        list = gson.fromJson(model1, ContestModel.ContestDatum.class);

        match_title = findViewById(R.id.match_title);
        match_title.setSelected(true);
        timer = findViewById(R.id.timer);
        mToolBarBack = findViewById(R.id.toolbar_back);

        contest_price_pool = findViewById(R.id.contest_price_pool);
        layFirstWin = findViewById(R.id.layFirstWin);
        txtFirstWin = findViewById(R.id.txtFirstWin);
        txtWinPer = findViewById(R.id.txtWinPer);
        imgConfirm = findViewById(R.id.imgConfirm);
//        excel_download = findViewById(R.id.excel_download);
        contest_total_winner = findViewById(R.id.contest_winner);
        contest_entry = findViewById(R.id.contest_entry_fee);
        contest_left_team = findViewById(R.id.contest_left_team);
        contest_total_team = findViewById(R.id.contest_total_team);
        contest_progress = findViewById(R.id.contest_progress);
        contest_left_spots_text = findViewById(R.id.contest_left_text);
        contest_total_spots_text = findViewById(R.id.contest_total_text);
        contest_offer_price = findViewById(R.id.contest_offer_price);
        contest_offer_text = findViewById(R.id.contest_offer_text);
        txtSingleMultiple = findViewById(R.id.txtSingleMultiple);
        layConfirm = findViewById(R.id.layConfirm);
        laySingleMultiple = findViewById(R.id.laySingleMultiple);
        contest_full_linear = findViewById(R.id.contest_full_linear);
        teams_left_linear = findViewById(R.id.teams_left_linear);
        imgSingleMultiple = findViewById(R.id.imgSingleMultiple);
        join_btn = findViewById(R.id.join_btn);
        layBonus = findViewById(R.id.layBonus);
        txtBonus = findViewById(R.id.txtBonus);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewpager = (ViewPager2) findViewById(R.id.viewpager);
        toolbar_fav = findViewById(R.id.toolbar_fav);
        imgOfferText = findViewById(R.id.imgOfferText);
        imgShare = findViewById(R.id.imgShare);
        contest_confirm = findViewById(R.id.contest_confirm);

        String entryFee = "";

        contest_offer_price.setVisibility(View.GONE);

        if (list.getNewOfferList() != null && list.getNewOfferList().size() > 0) {
            imgOfferText.setVisibility(View.VISIBLE);
            layBonus.setVisibility(View.GONE);

            NewOfferModal newOfferModal;
            // LogUtil.e("compa","list: "+list.getNewOfferRemovedList().size());
            if (list.getNewOfferRemovedList().size() > 0) {
                newOfferModal = list.getNewOfferRemovedList().get(0);
            } else {
                newOfferModal = list.getNewOfferList().get(0);
            }

            if (TextUtils.isEmpty(newOfferModal.getDiscount_entry_fee())) {
                //contest_entry.setText(context.getResources().getString(R.string.rs)+newOfferModal.getEntry_fee());
                entryFee = mContext.getResources().getString(R.string.rs) + newOfferModal.getEntry_fee();
                contest_offer_price.setVisibility(View.GONE);

            } else if (newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0") ||
                    newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0.0") ||
                    newOfferModal.getDiscount_entry_fee().equalsIgnoreCase("0.00")) {
                // contest_entry.setText("FREE");
                entryFee = "Free";
                contest_offer_price.setVisibility(View.VISIBLE);

                contest_offer_price.setText(mContext.getResources().getString(R.string.rs) + newOfferModal.getEntry_fee());
                contest_offer_price.setPaintFlags(contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //contest_entry.setText(context.getResources().getString(R.string.rs)+newOfferModal.getDiscount_entry_fee());
                entryFee = mContext.getResources().getString(R.string.rs) + newOfferModal.getDiscount_entry_fee();
                contest_offer_price.setVisibility(View.VISIBLE);

                contest_offer_price.setText(mContext.getResources().getString(R.string.rs) + newOfferModal.getEntry_fee());
                contest_offer_price.setPaintFlags(contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            txtBonus.setText(newOfferModal.getUsed_bonus() + "%");

            if (TextUtils.isEmpty(newOfferModal.getUsed_bonus()) || CustomUtil.convertFloat(newOfferModal.getUsed_bonus()) <= 0) {
                layBonus.setVisibility(View.GONE);
            } else {
                layBonus.setVisibility(View.VISIBLE);
            }
        } else {
            imgOfferText.setVisibility(View.GONE);

            //bonus_linear.setVisibility(View.VISIBLE);

            if (list.getEntryFee().equalsIgnoreCase("0") || list.getEntryFee().equalsIgnoreCase("0.0") ||
                    list.getEntryFee().equalsIgnoreCase("0.00")) {
                // contest_entry.setText("FREE");
                entryFee = "FREE";
            } else {
                entryFee = mContext.getResources().getString(R.string.rs) + list.getEntryFee();
                //contest_entry.setText( context.getResources().getString(R.string.rs) + list.getEntryFee());
            }
            if (TextUtils.isEmpty(list.getDefaultBonus()) || CustomUtil.convertFloat(list.getDefaultBonus()) <= 0) {
                layBonus.setVisibility(View.GONE);
            } else {
                layBonus.setVisibility(View.VISIBLE);
                //bonus_linear.setBackground(mContext.getResources().getDrawable(R.drawable.contest_type_border_red));
                //contest_bonus.setTextColor(mContext.getResources().getColor(R.color.white_font));
                //contest_bonus.setBackground(mContext.getResources().getDrawable(R.drawable.red_fill_curve));
            }

            txtBonus.setText(list.getDefaultBonus() + "%");
        }

        /*if (list.getEntryFee().equalsIgnoreCase("0")){
            contest_entry.setText("FREE");
        }else {
            contest_entry.setText(mContext.getResources().getString(R.string.rs) + list.getEntryFee());
        }*/

        //contest_offer_text.setText(list.getWinningTreeText());
        contest_entry.setText(entryFee);

        initClick();

        int totalSpots = CustomUtil.convertInt(list.getNoTeamJoin());
        int jointeam = CustomUtil.convertInt(list.getJoinedTeams());

        int progress = (jointeam * 100) / totalSpots;
        contest_progress.setProgress(progress);

        if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("pending")) {
            join_btn.setEnabled(false);
            match_title.setText("Leaderboard");
            timer.setVisibility(View.GONE);

            if (list.getIsUnlimited().equalsIgnoreCase("Yes")) {
                join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                teams_left_linear.setVisibility(View.VISIBLE);
                contest_full_linear.setVisibility(View.GONE);
                contest_left_team.setText("" + jointeam);
                contest_left_spots_text.setText("Joined teams");
                contest_total_team.setText("Unlimited");
                contest_total_spots_text.setText("Spots");

                float cal = (CustomUtil.convertFloat(list.getJoinedTeams()) * CustomUtil.convertFloat(list.getEntryFee()))
                        - ((CustomUtil.convertFloat(list.getJoinedTeams()) * CustomUtil.convertFloat(list.getEntryFee()) * CustomUtil.convertFloat(list.getCommission())) / 100);

                contest_price_pool.setText(CustomUtil.displayAmount(mContext, (int) cal + ""));
                contest_total_winner.setText(list.getTotalWinner() + " %");
            } else {
                contest_price_pool.setText(CustomUtil.displayAmountFloat(mContext, list.getDistributeAmount()));
                contest_total_winner.setText(list.getTotalWinner());

                int spotsLeft = totalSpots - jointeam;
                if (spotsLeft <= 0) {
                    //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
                    teams_left_linear.setVisibility(View.VISIBLE);
                    contest_full_linear.setVisibility(View.GONE);
                    //contest_full_linear.setVisibility(View.VISIBLE);
                } else {
                    //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                    teams_left_linear.setVisibility(View.VISIBLE);
                    contest_full_linear.setVisibility(View.GONE);
                }
                contest_left_team.setText("" + jointeam);
                contest_left_spots_text.setText("Joined teams");
                contest_total_team.setText(list.getNoTeamJoin());
                contest_total_spots_text.setText("Spots");
            }

        } else {
            join_btn.setEnabled(true);
            timer.setVisibility(View.VISIBLE);
            setTimer();
            match_title.setText(preferences.getMatchModel().getMatchTitle());//preferences.getMatchModel().getTeam1Sname() + " vs " + preferences.getMatchModel().getTeam2Sname()

            if (list.getIsUnlimited().equalsIgnoreCase("Yes")) {
                join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                teams_left_linear.setVisibility(View.VISIBLE);
                contest_full_linear.setVisibility(View.GONE);
                contest_left_team.setText("" + jointeam);
                contest_left_spots_text.setText("Spots");
                contest_total_team.setText("Unlimited");
                contest_total_spots_text.setText("Spots");

                float cal = (CustomUtil.convertFloat(list.getJoinedTeams()) * CustomUtil.convertFloat(list.getEntryFee()))
                        - ((CustomUtil.convertFloat(list.getJoinedTeams()) * CustomUtil.convertFloat(list.getEntryFee()) * CustomUtil.convertFloat(list.getCommission())) / 100);

                contest_price_pool.setText(CustomUtil.displayAmount(mContext, (int) cal + ""));
                contest_total_winner.setText(list.getTotalWinner() + " %");

            } else {
                contest_price_pool.setText(CustomUtil.displayAmountFloat(mContext, list.getDistributeAmount()));
                contest_total_winner.setText(list.getTotalWinner());

                int spotsLeft = totalSpots - jointeam;
                if (spotsLeft <= 0) {
                    //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
                    teams_left_linear.setVisibility(View.GONE);
                    contest_full_linear.setVisibility(View.VISIBLE);
                } else {
                    //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                    teams_left_linear.setVisibility(View.VISIBLE);
                    contest_full_linear.setVisibility(View.GONE);
                }
                contest_left_team.setText("" + spotsLeft);
                contest_left_spots_text.setText("Spots Left");
                contest_total_team.setText(list.getNoTeamJoin());
                contest_total_spots_text.setText("Spots");
            }
        }

        if (CustomUtil.convertInt(list.getMaxJoinTeam()) > CustomUtil.convertInt(list.getMyJoinedTeam())) {
            int spotsLeft = totalSpots - jointeam;
            if (spotsLeft <= 0) {
                join_btn.setEnabled(false);
                join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
            } else {
                join_btn.setEnabled(true);
                join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.green_pure));
            }

        } else {
            join_btn.setEnabled(false);
            join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
        }

        if (!preferences.getMatchModel().getMatchStatus().equalsIgnoreCase("pending")) {
            join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
        }

        /*if (list.getIsOffer() != null && list.getIsOffer().equalsIgnoreCase("Yes")) {
            contest_offer_price.setVisibility(View.VISIBLE);
            contest_offer_price.setText(mContext.getResources().getString(R.string.rs)+list.getOfferPrice());
            contest_offer_price.setPaintFlags(contest_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            contest_offer_price.setVisibility(View.GONE);
        }*/


        if (list.getIs_flexi().equalsIgnoreCase("yes")){
            layConfirm.setVisibility(View.VISIBLE);
            imgConfirm.setImageResource(R.drawable.ic_flext_join);
            contest_confirm.setText("Flexible");
        }else {
            if (list.getConEntryStatus().equalsIgnoreCase("Unconfirmed")) {
                layConfirm.setVisibility(View.GONE);
            } else {
                layConfirm.setVisibility(View.VISIBLE);
                imgConfirm.setImageResource(R.drawable.ic_confirm_contest);
                contest_confirm.setText("Guaranteed");
            }
        }

        if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
            imgSingleMultiple.setImageResource(R.drawable.ic_single_join);
            txtSingleMultiple.setText("Single entry");
        }
        /*else  if (list.getConPlayerEntry().equalsIgnoreCase("flexi")) {
            imgSingleMultiple.setImageResource(R.drawable.ic_flext_join);
            txtSingleMultiple.setText("Flexi entry");
        }*/
        else {
            imgSingleMultiple.setImageResource(R.drawable.ic_multiple_join);
            txtSingleMultiple.setText("Upto " + list.getMaxJoinTeam() + " entries");
        }

        try {
            JSONArray jsonArray = new JSONArray(list.getWinningTree());
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                if (!jsonObject.optString("amount").equalsIgnoreCase("glory")) {
                    txtFirstWin.setText(CustomUtil.displayAmountFloat(mContext, jsonObject.optString("amount")));
                } else {
                    txtFirstWin.setText(jsonObject.optString("amount"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            layFirstWin.setVisibility(View.GONE);
        }

        int totalWin = Integer.parseInt(list.getTotalWinner()) * 100 / Integer.parseInt(list.getNoTeamJoin());

        txtWinPer.setText(totalWin + "%");

        if (!TextUtils.isEmpty(list.getWinningTreeText())) {
            contest_offer_text.setVisibility(View.VISIBLE);
            contest_offer_text.setText(list.getWinningTreeText());
        } else {
            contest_offer_text.setVisibility(View.GONE);
        }

        if (list.getConTypeName().equalsIgnoreCase("Grand Leagues")) {
            toolbar_fav.setVisibility(View.GONE);
        } else {
            toolbar_fav.setVisibility(View.VISIBLE);
            if (list != null && list.getContestFavorite() != null && list.getContestFavorite()) {
                toolbar_fav.setImageResource(R.drawable.star_fille_yellow);
            } else {
                toolbar_fav.setImageResource(R.drawable.star_white_outline);
            }
        }

        toolbar_fav.setOnClickListener(view -> {
            /*Intent intent1 = new Intent(mContext,AddDepositActivity.class);
            intent1.putExtra("isJoin",true);
            intent1.putExtra("depositAmt","");
            startActivity(intent1);*/

            if (MyApp.getClickStatus()) {
                toolbar_fav.setEnabled(false);
                String msg = "";

                if (list.getContestFavorite()) {
                    msg = "Are you sure you want Unfavorite this Contest?";
                } else {
                    msg = "Are you sure you want favorite this Contest?";
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setMessage(msg);
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", (dialog, which) -> {
                    toolbar_fav.setEnabled(true);
                    try {
                        followUnFollowAction(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                alert.setNegativeButton("No", (dialog, which) -> {
                    toolbar_fav.setEnabled(true);
                    dialog.dismiss();
                });
                alert.show();
            }
        });

        String finalEntryFee = entryFee;
        imgShare.setOnClickListener(view -> {
            String url = ConstantUtil.LINK_FANTASY_URL + MyApp.getMyPreferences().getMatchModel().getSportId() + "/" +
                    MyApp.getMyPreferences().getMatchModel().getId() + "/" + list.getId();
            //LogUtil.e("resp","Share Link: "+url);
            /*FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(url))
                    .setDomainUriPrefix(ConstantUtil.LINK_PREFIX_URL)
                    .setIosParameters(new DynamicLink.IosParameters.Builder(ConstantUtil.FF_IOS_APP_BUNDLE).setAppStoreId(ConstantUtil.FF_IOS_APP_ID)
                            .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK)).build())
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                            .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK))
                            .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener((Activity) mContext, task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().getShortLink() != null)
                                shareShortLink(task.getResult().getShortLink(), list, finalEntryFee, totalSpots - jointeam);
                            else {
                                CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                            }
                        } else {
                            CustomUtil.showTopSneakWarning(mContext, "Can't share this");
                        }
                    });*/
        });

        layConfirm.setOnClickListener(v -> {
            /*if (list.getIs_flexi().equalsIgnoreCase("yes")){
                ViewTooltip
                        .on((Activity)  mContext, imgConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("The event will take place even if only two spots are filled, Prize pool varies based on entries.")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }else {
                ViewTooltip
                        .on((Activity) mContext, imgConfirm)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(LEFT)
                        .text("Guaranteed winning even if contest remains unfilled")
                        .textColor(Color.WHITE)
                        .color(mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }*/
        });

        laySingleMultiple.setOnClickListener(v -> {
            if (list.getConPlayerEntry().equalsIgnoreCase("Single")) {
               /* ViewTooltip
                        .on((Activity) mContext, txtSingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Only one Team allowed")
                        .textColor(Color.WHITE)
                        .color(mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }
           /* else if (list.getConPlayerEntry().equalsIgnoreCase("flexi")){
                ViewTooltip
                        .on((Activity)  mContext, txtSingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Prize pool may be vary")
                        .textColor(Color.WHITE)
                        .color( mContext.getResources().getColor(R.color.blackPrimary))
                        .show();
            }*/
            else {
                /*ViewTooltip
                        .on((Activity) mContext, txtSingleMultiple)
                        .autoHide(true, 1000)
                        .align(CENTER)
                        .position(RIGHT)
                        .text("Join with multiple Teams")
                        .textColor(Color.WHITE)
                        .color(mContext.getResources().getColor(R.color.blackPrimary))
                        .show();*/
            }

        });

        layBonus.setOnClickListener(v -> {
            /*ViewTooltip
                    .on((Activity) mContext, layBonus)
                    .autoHide(true, 1000)
                    .align(CENTER)
                    .position(RIGHT)
                    .text("Bonus Contest")
                    .textColor(Color.WHITE)
                    .color(mContext.getResources().getColor(R.color.blackPrimary))
                    .show();*/
        });

        imgOfferText.setOnClickListener(v -> {
            if (MyApp.getClickStatus()) {
                OfferListSheet bottomSheetTeam = new OfferListSheet(mContext, list.getNewOfferTempList());//getNewOfferList
                bottomSheetTeam.show(((LeaderBoardActivity) mContext).getSupportFragmentManager(),
                        BottomSheetTeam.TAG);
            }
        });


        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getMyPreferences().getPlayerModel() != null) {
                    if (CustomUtil.convertInt(list.getMaxJoinTeam()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                        CustomUtil.showTopSneakError(mContext, "Max " + list.getMaxJoinTeam() + " team(s) allowed");
                    } else if (CustomUtil.convertInt(MyApp.getMyPreferences().getUpdateMaster().getMaxTeamCount()) <= CustomUtil.convertInt(list.getMyJoinedTeam())) {
                        CustomUtil.showTopSneakError(mContext, "Max " + MyApp.getMyPreferences().getUpdateMaster().getMaxTeamCount() + " team(s) can be created");
                    } else if (CustomUtil.convertInt(list.getMyJoinedTeam()) >= MyApp.getMyPreferences().getPlayerModel().size()) {
                        //MyApp.getMyPreferences().setPref(PrefConstant.IS_CONTEST_JOIN,true);
                        Gson gson = new Gson();
                        Type menu = new TypeToken<ContestModel.ContestDatum>() {
                        }.getType();
                        String json = gson.toJson(list, menu);
                        /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new SelectPlayerFragment(json),
                                ((HomeActivity) mContext).fragmentTag(8),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                        Intent intent = new Intent(mContext, CricketSelectPlayerActivity.class);
                        intent.putExtra("data", json);
                        intent.putExtra(PrefConstant.TEAMCREATETYPE, 2);
                        mContext.startActivity(intent);
                    } else {
                       /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                                R.id.fragment_container,
                                new TeamSelectJoinFragment(list),
                                ((HomeActivity) mContext).fragmentTag(13),
                                FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                        // LogUtil.e("addreTeId",list.getJoinedTeamTempTeamId());
                        int teamCnt = 0;
                        if (preferences.getPlayerModel() != null && preferences.getPlayerModel().size() > 0) {
                            teamCnt = preferences.getPlayerModel().size();
                            LogUtil.d("colDara", list.getJoinedTeamTempTeamId() + "   " + teamCnt);
                            if ((TextUtils.isEmpty(list.getJoinedTeamTempTeamId()) || list.getJoinedTeamTempTeamId().equals("0")) &&
                                    teamCnt == 1) {
                                confirmTeam(list);
                            } else {
                                Gson gson = new Gson();
                                String model = gson.toJson(list);
                                Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                                intent.putExtra("model", model);
                                startActivityForResult(intent, 107);
                            }
                        } else {
                            Gson gson = new Gson();
                            String model = gson.toJson(list);
                            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
                            intent.putExtra("model", model);
                            startActivityForResult(intent, 107);
                        }
                    }
                }
            }
        });

       /* txtFirstWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (list.getIsUnlimited().equalsIgnoreCase("No")) {
                        WinningTreeSheet bottomSheetTeam = new WinningTreeSheet(mContext, list.getDistributeAmount(), list.getWinningTree(),
                                contest_price_pool.getText().toString());
                        if (mContext instanceof  ContestListActivity){
                            bottomSheetTeam.show(((ContestListActivity) mContext).getSupportFragmentManager(),
                                    BottomSheetTeam.TAG);
                        }else {
                            bottomSheetTeam.show(((MoreContestListActivity) mContext).getSupportFragmentManager(),
                                    BottomSheetTeam.TAG);
                        }

                    }else {
                        CustomUtil.showTopSneakWarning(mContext,"Price Pool will appear after match starts.");
                    }

                }
            }
        });*/

        FragmentName.add("Contest Details");
        FragmentName.add("LeaderBoard");

        viewpager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabs, viewpager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(FragmentName.get(position));
                    }
                }).attach();

        // Disable swipe changes and allow to change only by pressing on tab
        viewpager.setUserInputEnabled(ApiManager.isPagerSwipe);
        viewpager.setOffscreenPageLimit(2);
        ConstantUtil.reduceDragSensitivity(viewpager);
        //viewpager.setCurrentItem(1);
        //tabs.selectTab(tabs.getTabAt(1));
    }

    private void shareShortLink(Uri link, ContestModel.ContestDatum list, String finalEntryFee, int spotsLeft) {
        String sportsName = "";

        for (SportsModel sportsModel : MyApp.getMyPreferences().getSports()) {
            if (sportsModel.getId().equalsIgnoreCase(MyApp.getMyPreferences().getMatchModel().getSportId())) {
                sportsName = sportsModel.getSportName();
            }
        }

        String content =/*ConstantUtil.LINK_URL+"\n\n"+*/
                "I have challenged you to this *" + list.getConTypeName() + "* \uD83C\uDFC6 contest for the " +
                        MyApp.getMyPreferences().getMatchModel().getTeam1Sname() +
                        "\uD83E\uDD1Cvs\uD83E\uDD1B" + MyApp.getMyPreferences().getMatchModel().getTeam2Sname() + " " + sportsName + " match!\n\n" +
                        "*Deadline* ⏳ : " + CustomUtil.dateTimeConvert(MyApp.getMyPreferences().getMatchModel().getSafeMatchStartTime()) +
                        "\n\n*Prize Pool*\uD83D\uDCB0 : " + CustomUtil.displayAmountFloat(mContext, list.getDistributeAmount()) + "\n" +
                        "*Entry Fee*\uD83D\uDCB6 : " + finalEntryFee + "\n" +
                        "\n\nTap below link for join:\uD83D\uDCF2" +
                        "\n" + link.toString().trim();

        if (spotsLeft > 0) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/html");
                mContext.startActivity(sendIntent);
            } catch (ActivityNotFoundException e) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setType("text/html");
                mContext.startActivity(sendIntent);
            }
        } else {
            CustomUtil.showTopSneakWarning(mContext, "This contest is full, Please choose another!");
        }
    }

    public void followUnFollowAction(ContestModel.ContestDatum object) {


        ArrayList<String> favArr = new ArrayList<>(Arrays.asList(preferences.getUserModel().getFavoriteLeague().split(",")));

        if (favArr != null && favArr.size() > 0) {

            if (object.getContestFavorite()) {
                int pos = -1;
                for (int i = 0; i < favArr.size(); i++) {
                    if (object.getConTplId().trim().equals(favArr.get(i))) {
                        pos = i;
                    }
                }
                if (pos != -1) {
                    favArr.remove(pos);
                    object.setContestFavorite(!object.getContestFavorite());
                    updateUserFavContest(cleanUpCommas(TextUtils.join(",", favArr)), false);
                }
            } else {
                boolean isFavAdd = false;
                for (int i = 0; i < favArr.size(); i++) {
                    if (object.getConTplId().trim().equals(favArr.get(i))) {
                        isFavAdd = true;
                    }
                }
                if (!isFavAdd) {
                    favArr.add(object.getConTplId());
                    object.setContestFavorite(!object.getContestFavorite());
                    updateUserFavContest(cleanUpCommas(TextUtils.join(",", favArr)), true);
                } else {
                    CustomUtil.showTopSneakSuccess(mContext, "Contest is already favorite");
                }
            }
        } else {
            object.setContestFavorite(!object.getContestFavorite());
            updateUserFavContest(object.getConTplId(), true);
        }
    }

    private void updateUserFavContest(String favArray, boolean isFav) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("favorite_league", favArray);
            LogUtil.e("resp", "updateUserFavContest:" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    UserModel userModel = preferences.getUserModel();
                    userModel.setFavoriteLeague(favArray);
                    preferences.setUserModel(userModel);

                    if (isFav) {
                        CustomUtil.showTopSneakSuccess(mContext, "Contest favorite successfully");
                        toolbar_fav.setImageResource(R.drawable.star_fille_yellow);
                    } else {
                        CustomUtil.showTopSneakSuccess(mContext, "Contest Unfavorite successfully");
                        toolbar_fav.setImageResource(R.drawable.star_white_outline);
                    }

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantUtil.isTimeOverShow = false;

    }

    public void confirmTeam(ContestModel.ContestDatum contestData) {


        if (!isValidForJoin(contestData, 1)) {//((deposit +  winning) - Contest_fee[0]) < 0

            //float amt=Contest_fee[0]-(deposit + winning);
            double amt = Math.ceil(amtToAdd);

            if (amt < 1) {
                amt = 1;
            }

            String patableAmt = CustomUtil.getFormater("0.00").format(amt);
            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS, false);
            CustomUtil.showTopSneakError(mContext,mContext.getResources().getString(R.string.not_enough_balance));
            /*Intent intent = new Intent(mContext, AddDepositActivity.class);
            intent.putExtra("isJoin", true);
            intent.putExtra("depositAmt", patableAmt);
            intent.putExtra("contestData", gson.toJson(contestData));
            startActivity(intent);*/
        } else {
            TextView join_contest_fee, join_use_deposit, join_use_borrowed, join_use_rewards,
                    join_use_winning, join_user_pay;
            final CheckBox join_donation_select;
            TextView join_donation_text, join_contest;
            final EditText join_donation_price, edtConQty;
            final RecyclerView recyclerNoOfContest;

            ArrayList<ContestQuantityModel> contestQtyList = ConstantUtil.getContestEntryList();

            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_joining, null);

            join_contest = (TextView) view.findViewById(R.id.join_contest_btn);
            join_contest_fee = (TextView) view.findViewById(R.id.join_contest_fee);
            join_use_deposit = (TextView) view.findViewById(R.id.join_use_deposit);
            //join_use_borrowed = (TextView) view.findViewById(R.id.join_use_borrowed);
            join_use_rewards = (TextView) view.findViewById(R.id.join_use_rewards);
            join_use_winning = (TextView) view.findViewById(R.id.join_use_winning);
            join_user_pay = (TextView) view.findViewById(R.id.join_user_pay);
            recyclerNoOfContest = view.findViewById(R.id.recyclerNoOfContest);
            LinearLayout layMultiContest = view.findViewById(R.id.layMultiContest);
            edtConQty = view.findViewById(R.id.edtConQty);
            edtConQty.setText("1");

            LinearLayout layBonus = view.findViewById(R.id.layBonus);

            if (ConstantUtil.is_bonus_show) {
                layBonus.setVisibility(View.VISIBLE);
            } else {
                layBonus.setVisibility(View.GONE);
            }

            if (contestData.getConPlayerEntry().equalsIgnoreCase("Single")
                    && contestData.getAutoCreate().equalsIgnoreCase("yes")
                    && !(contestData.getEntryFee().equalsIgnoreCase("0") ||
                    contestData.getEntryFee().equalsIgnoreCase("0.0") ||
                    contestData.getEntryFee().equalsIgnoreCase("0.00"))) {
                layMultiContest.setVisibility(View.VISIBLE);

                recyclerNoOfContest.setLayoutManager(new GridLayoutManager(mContext, 6));
                ContestQuantityAdapter adapter = new ContestQuantityAdapter(mContext, contestQtyList, new ContestQuantityAdapter.onQtyListener() {
                    @Override
                    public void onClick(ContestQuantityModel model) {
                        edtConQty.setText(model.getId());
                        edtConQty.setSelection(model.getId().length());
                    }
                });
                recyclerNoOfContest.setAdapter(adapter);

                //float finalOrgContestEntry = orgContestEntry;
                edtConQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        for (int i = 0; i < contestQtyList.size(); i++) {
                            ContestQuantityModel model = contestQtyList.get(i);
                            if (s.toString().trim().equalsIgnoreCase(model.getId().trim())) {
                                model.setSelected(true);
                            } else {
                                model.setSelected(false);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (!TextUtils.isEmpty(s.toString())) {
                            int qty = Integer.parseInt(s.toString());
                            if (qty > 500) {
                                CustomUtil.showToast(mContext, "Maximum 500 Quantity allow");

                                edtConQty.setText("500");
                                edtConQty.setSelection(3);

                                qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                isValidForJoin(contestData, qty);

                                /*use_deposit=use_winning=useBonus=0;
                                Contest_fee[0]=CustomUtil.convertFloat(contestData.getEntryFee()) * qty;
                                // LogUtil.e(TAG, "onClick: total: "+ total);

                                float usableBonus = 0;
                                if (CustomUtil.convertInt(contestData.getMyJoinedTeam()) < CustomUtil.convertInt(contestData.getMaxTeamBonusUse())) {
                                    usableBonus = CustomUtil.convertFloat(contestData.getUseBonus());
                                } else {
                                    usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                                }
                                useBonus = ((Contest_fee[0] * usableBonus) / 100);

                                if (useBonus > bonus) {
                                    useBonus = bonus;
                                }

                                if (Contest_fee[0] - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
                                    Contest_fee[0] = Contest_fee[0] - useBonus;
                                }

                                if ((Contest_fee[0] - deposit) < 0) {
                                    use_deposit = Contest_fee[0];
                                }
                                else {
                                    use_deposit = deposit;
                                    //use_transfer = transfer_bal;
                                    use_winning = Contest_fee[0] - deposit;
                                }*/
                            } else {
                                qty = Integer.parseInt(edtConQty.getText().toString().trim());

                                isValidForJoin(contestData, qty);

                                /*use_deposit=use_winning=useBonus=0;
                                Contest_fee[0]=CustomUtil.convertFloat(contestData.getEntryFee()) * qty;
                                // LogUtil.e(TAG, "onClick: total: "+ total);

                                float usableBonus = 0;
                                if (CustomUtil.convertInt(contestData.getMyJoinedTeam()) < CustomUtil.convertInt(contestData.getMaxTeamBonusUse())) {
                                    usableBonus = CustomUtil.convertFloat(contestData.getUseBonus());
                                } else {
                                    usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                                }
                                useBonus = ((Contest_fee[0] * usableBonus) / 100);

                                if (useBonus > bonus) {
                                    useBonus = bonus;
                                }

                                if (Contest_fee[0] - useBonus >= 0) {// (Contest_fee - useBonus >= 0)
                                    Contest_fee[0] = Contest_fee[0] - useBonus;
                                }

                                if ((Contest_fee[0] - deposit) < 0) {
                                    use_deposit = Contest_fee[0];
                                }
                                else {
                                    use_deposit = deposit;
                                    //use_transfer = transfer_bal;
                                    use_winning = Contest_fee[0] - deposit;
                                }*/
                            }

                            LogUtil.e(TAG, "onClick: deposit_bal" + CustomUtil.getFormater("00.00").format(use_deposit) +
                                    "\n  Win_bal" + CustomUtil.getFormater("00.00").format(use_winning) +
                                    "\n  Donation useBonus" + CustomUtil.getFormater("00.00").format(useBonus));

                            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
                            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
                            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
                            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));
                        } else {
                            join_use_deposit.setText(getResources().getString(R.string.rs) + "00.00");
                            join_use_winning.setText(getResources().getString(R.string.rs) + "00.00");
                            join_use_rewards.setText(getResources().getString(R.string.rs) + "00.00");
                            join_user_pay.setText(getResources().getString(R.string.rs) + "00.00");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            } else {
                layMultiContest.setVisibility(View.GONE);
            }

            //join_donation_select = (CheckBox) view.findViewById(R.id.join_donation_select);
            //join_donation_text = (TextView) view.findViewById(R.id.join_donation_text);
            //join_donation_price = (EditText) view.findViewById(R.id.join_donation_number);

            /*join_donation_price.setEnabled(false);

            if (preferences.getMatchModel().getTeamAXi().equalsIgnoreCase("Yes") &&
                    preferences.getMatchModel().getTeamBXi().equalsIgnoreCase("Yes")){
                join_donation_select.setChecked(false);
            }else {
                join_donation_select.setChecked(true);
            }*/

            join_use_deposit.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + useCoin)));
            //join_use_borrowed.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_transfer));
            join_use_winning.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(use_winning));
            join_use_rewards.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(useBonus));
            join_contest_fee.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format(CustomUtil.convertFloat(contestData.getEntryFee())));// Contest_fee+ useBonus
            join_user_pay.setText(getResources().getString(R.string.rs) + CustomUtil.getFormater("00.00").format((use_deposit + use_winning + useBonus + useCoin)));// Contest_fee+ useBonus

            //final float finalUseBonus = useBonus;
            join_contest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //join_contest.setEnabled(false);
                    if (MyApp.getClickStatus()) {
                        /*Float donation;
                        Boolean isDonation = false;*/

                        int qty = 0;
                        if (!TextUtils.isEmpty(edtConQty.getText().toString().trim())) {
                            qty = Integer.parseInt(edtConQty.getText().toString().trim());
                        }

                        if (!isValidForJoin(contestData, qty)) {//((deposit +  winning) - (CustomUtil.convertFloat(contestData.getEntryFee()) * qty)) < 0
                            bottomSheetDialog.dismiss();
                          //  CustomUtil.showToast(mContext, "Insufficient Balance");
                            //float amt= (CustomUtil.convertFloat(contestData.getEntryFee()) * qty) - (deposit + winning);
                            double amt = Math.ceil(amtToAdd);
                            if (amt < 1) {
                                amt = 1;
                            }
                            contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                            String patableAmt = CustomUtil.getFormater("0.00").format(amt);
                            MyApp.getMyPreferences().setPref(PrefConstant.PAYMENT_SUCCESS, false);
                            CustomUtil.showTopSneakError(mContext,mContext.getResources().getString(R.string.not_enough_balance));
                        /*    Intent intent = new Intent(mContext, AddDepositActivity.class);
                            intent.putExtra("isJoin", true);
                            intent.putExtra("depositAmt", patableAmt);
                            intent.putExtra("contestData", gson.toJson(contestData));
                            startActivity(intent);*/
                            return;
                        }

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        Date date = null;
                        try {
                            date = format.parse(preferences.getMatchModel().getSafeMatchStartTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date.before(MyApp.getCurrentDate())) {
                            Toast.makeText(mContext, "Time Up! Match Started", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(edtConQty.getText().toString().trim())) {
                            CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                        } else if (qty <= 0) {
                            CustomUtil.showToast(mContext, "Select atleast 1 contest quantity");
                        } else if (qty > 500) {
                            CustomUtil.showToast(mContext, "Maximum 500 Quantity allow");
                        } else {
                            bottomSheetDialog.dismiss();
                            contestData.setJoin_con_qty(edtConQty.getText().toString().trim());
                            joinContest(contestData);
                        }
                    }
                }
            });

            bottomSheetDialog = new BottomSheetDialog(mContext);
            bottomSheetDialog.setContentView(view);
            //((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
            if (bottomSheetDialog != null && !bottomSheetDialog.isShowing())
                bottomSheetDialog.show();
        }
    }

    private boolean isValidForJoin(ContestModel.ContestDatum contestData, int qty) {
        DecimalFormat format = CustomUtil.getFormater("00.00");

        use_deposit = use_winning = use_donation_deposit = useBonus = useCoin = 0;

        float finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());

        final float deposit = CustomUtil.convertFloat(preferences.getUserModel().getDepositBal());
        float bonus = CustomUtil.convertFloat(preferences.getUserModel().getBonusBal());
        final float winning = CustomUtil.convertFloat(preferences.getUserModel().getWinBal());
        final float bb_coin = CustomUtil.convertFloat(preferences.getUserModel().getFf_coin());

        float usableBonus, usableBBCoins = 0;

        if (!TextUtils.isEmpty(contestData.getOffer_date_text())) {
            if (contestData.getNewOfferRemovedList().size() > 0) {
                NewOfferModal modal = contestData.getNewOfferRemovedList().get(0);
                if (modal.getDiscount_entry_fee().equalsIgnoreCase("")) {
                    finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
                } else {
                    finalEntryFee = CustomUtil.convertFloat(modal.getDiscount_entry_fee());
                }
                usableBonus = CustomUtil.convertFloat(modal.getUsed_bonus());
            } else {
                usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
                finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
            }
        } else {
            usableBonus = CustomUtil.convertFloat(contestData.getDefaultBonus());
            finalEntryFee = CustomUtil.convertFloat(contestData.getEntryFee());
        }

        usableBBCoins = CustomUtil.convertFloat(contestData.getDefault_bb_coins());

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

        if (useCoin >= useBonus)
            useCoin = useCoin - useBonus;

        if (finalEntryFee - useCoin >= 0) {
            finalEntryFee = finalEntryFee - useCoin;
        }

        if ((finalEntryFee - deposit) < 0) {
            use_deposit = finalEntryFee;
        } else {
            use_deposit = deposit;
            use_winning = finalEntryFee - deposit;
        }

        float calReward = 0, calCoin = 0;

        if (bonus > 0) {
            if (useBonus <= bonus) {
                calReward = useBonus;
            } else if (useBonus > bonus) {
                calReward = bonus;
            } else {
                calReward = 0;
            }
        } else {
            calReward = 0;
        }

        if (bb_coin > 0) {
            if (useCoin <= bb_coin) {
                calCoin = useCoin;
            } else if (useCoin > bb_coin) {
                calCoin = bb_coin;
            } else {
                calCoin = 0;
            }
        } else {
            calCoin = 0;
        }

        float calBalance1 = deposit + winning + calReward + calCoin;
        float totalCharge = useBonus + use_deposit + use_winning + useCoin;

        amtToAdd = totalCharge - calBalance1;

        LogUtil.e("resp", "deposit:" + deposit + "\nwinning:" + winning + "\nbb_coin:" + bb_coin + "\nuse_deposit:" + use_deposit
                + "\nuse_transfer:" + use_transfer + "\nuse_winning:" + use_winning + "\nuse_bb_coin:" + useCoin + "\nuseBonus:" + useBonus);

        if (calBalance1 < totalCharge) {
            return false;
        }
        return true;
    }

    public void joinContest(ContestModel.ContestDatum contestData) {

        if (preferences.getPlayerModel() != null && preferences.getPlayerModel().size() > 0) {
            PlayerListModel playerListModel = preferences.getPlayerModel().get(0);

            DecimalFormat format = CustomUtil.getFormater("00.00");
            format.setRoundingMode(RoundingMode.HALF_UP);
            //temp_team_id,match_id,con_display_type,user_id,contest_id,team_name,deposit_bal,bonus_bal,win_bal,transfer_bal,donation_bal
            final JSONObject jsonObject = new JSONObject();
            final JSONObject childObj = new JSONObject();
            final JSONArray array = new JSONArray();

            try {

                //jsonObject.put("temp_team_id", playerListModel.getId());
                jsonObject.put("match_id", preferences.getMatchModel().getId());
                jsonObject.put("con_display_type", preferences.getMatchModel().getConDisplayType());
                jsonObject.put("user_id", preferences.getUserModel().getId());
                jsonObject.put("contest_id", contestData.getId());
                if (TextUtils.isEmpty(contestData.getJoin_con_qty()) || contestData.getJoin_con_qty().equalsIgnoreCase("0"))
                    contestData.setJoin_con_qty("1");
                jsonObject.put("join_con_qty", contestData.getJoin_con_qty());
                // jsonObject.put("team_name", playerListModel.getTempTeamName());
                if (use_deposit > 0) {
                    use_deposit = use_deposit / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (useBonus > 0) {
                    useBonus = useBonus / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (use_winning > 0) {
                    use_winning = use_winning / Integer.parseInt(contestData.getJoin_con_qty());
                }
                if (useCoin > 0) {
                    useCoin = useCoin / Integer.parseInt(contestData.getJoin_con_qty());
                }
                // jsonObject.put("team_name", playerListModel.getTempTeamName());
                jsonObject.put("ff_coins_bal", format.format(useCoin));
                jsonObject.put("deposit_bal", format.format(use_deposit));//92.85
                jsonObject.put("bonus_bal", format.format(useBonus));//6
                jsonObject.put("win_bal", format.format(use_winning));//21.15
                jsonObject.put("transfer_bal", format.format(use_transfer));
                jsonObject.put("donation_bal", format.format(use_donation_deposit));

                jsonObject.put("pass_id", "");
                jsonObject.put("applied_pass_count", "0");
                childObj.put("team_no", "1");

                childObj.put("pass_id", "");
                childObj.put("deposit_bal", format.format(use_deposit));
                childObj.put("bonus_bal", format.format(useBonus));
                childObj.put("win_bal", format.format(use_winning));
                childObj.put("ff_coins_bal", format.format(useCoin));
                childObj.put("transfer_bal", format.format(use_transfer));
                childObj.put("donation_bal", format.format(use_donation_deposit));
                childObj.put("team_name", playerListModel.getTempTeamName());
                childObj.put("temp_team_id", playerListModel.getId());

                if (contestData.getIs_pass() != null && contestData.getIs_pass().equalsIgnoreCase("yes")) {
                    if (contestData.getPassModelArrayList().size() > 0) {
                        LogUtil.e(TAG, "teamSelectJoin=" + contestData.getPassModelArrayList().size());
                        PassModel model = contestData.getPassModelArrayList().get(0);
                        int count = Integer.parseInt(model.getNoOfEntry()) - Integer.parseInt(model.getTotalJoinSpot());
                        if (count >= 1) {
                            childObj.put("pass_id", model.getPassId());
                            jsonObject.put("pass_id", model.getPassId());
                            jsonObject.put("applied_pass_count", "1");

                            JSONObject passData = new JSONObject();
                            passData.put("id", model.getId());
                            passData.put("no_of_entry", model.getNoOfEntry());
                            passData.put("pass_id", model.getPassId());
                            passData.put("total_join_spot", model.getTotalJoinSpot());
                            LogUtil.e(TAG, "pass_data=>" + passData);
                            jsonObject.put("my_pass_data",passData);
                        }
                    }
                }
                array.put(childObj);
                jsonObject.put("team_array", array);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtil.d("resp", jsonObject.toString());

            HttpRestClient.postJSON(mContext, true, ApiManager.JOIN_CONTEST2MultiJoin, jsonObject, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    if (responseBody.optBoolean("status")) {

                        //refreshArray(playerListModel.getId());

                        UserModel user = preferences.getUserModel();

                        if (use_deposit > 0) {
                            use_deposit = use_deposit * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (useBonus > 0) {
                            useBonus = useBonus * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (use_winning > 0) {
                            use_winning = use_winning * Integer.parseInt(contestData.getJoin_con_qty());
                        }
                        if (useCoin > 0) {
                            useCoin = useCoin * Integer.parseInt(contestData.getJoin_con_qty());
                        }

                        float deposit_bal = CustomUtil.convertFloat(user.getDepositBal()) - use_deposit;
                        // float transfer_bal = CustomUtil.convertFloat(user.getTransferBal()) - use_transfer;
                        float bonus_bal = CustomUtil.convertFloat(user.getBonusBal()) - useBonus;
                        float winning_bal = CustomUtil.convertFloat(user.getWinBal()) - use_winning;
                        float coin_bal = CustomUtil.convertFloat(user.getFf_coin()) - useCoin;

                        user.setDepositBal(String.valueOf(deposit_bal));
                        //  user.setTransferBal(String.valueOf(transfer_bal));
                        user.setBonusBal(String.valueOf(bonus_bal));
                        user.setWinBal(String.valueOf(winning_bal));
                        user.setFf_coin(String.valueOf(coin_bal));

                        float total;//CustomUtil.convertFloat(user.getTransferBal()) +
/*+
                                    CustomUtil.convertFloat(user.getBonusBal())*/
                        if (ConstantUtil.is_bonus_show) {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin()) +
                                    CustomUtil.convertFloat(user.getBonusBal());

                        } else {
                            total = CustomUtil.convertFloat(user.getDepositBal()) +
                                    CustomUtil.convertFloat(user.getWinBal()) +
                                    //CustomUtil.convertFloat(user.getTransferBal()) +
                                    CustomUtil.convertFloat(user.getFf_coin());

                        }

                        user.setTotal_balance(total);

                        preferences.setUserModel(user);
                        CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                        LogUtil.e(TAG, "JOIN_CONTEST2MultiJoinLeaderBoard contest join");

                        int myJoin = CustomUtil.convertInt(contestData.getMyJoinedTeam()) + 1;
                        contestData.setMyJoinedTeam(myJoin + "");

                        int totalSpots = CustomUtil.convertInt(contestData.getNoTeamJoin());
                        int jointeam = CustomUtil.convertInt(contestData.getJoinedTeams()) + 1;

                        contestData.setJoinedTeams(jointeam + "");

                        int progress = (jointeam * 100) / totalSpots;
                        contest_progress.setProgress(progress);

                        int spotsLeft = totalSpots - jointeam;
                        if (spotsLeft <= 0) {
                            //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.grey_filled_btn));
                            teams_left_linear.setVisibility(View.VISIBLE);
                            contest_full_linear.setVisibility(View.GONE);
                            //contest_full_linear.setVisibility(View.VISIBLE);
                        } else {
                            //join_btn.setBackground(mContext.getResources().getDrawable(R.drawable.green_filled_btn));
                            teams_left_linear.setVisibility(View.VISIBLE);
                            contest_full_linear.setVisibility(View.GONE);
                        }
                        //contest_left_team.setText("" + jointeam);
                        //contest_left_spots_text.setText("Joined teams");
                        contest_left_team.setText("" + spotsLeft);
                        contest_left_spots_text.setText("Spots Left");
                        contest_total_team.setText(list.getNoTeamJoin());
                        contest_total_spots_text.setText("Spots");

                        if (CustomUtil.convertInt(list.getMaxJoinTeam()) > CustomUtil.convertInt(list.getMyJoinedTeam())) {

                            if (spotsLeft <= 0) {
                                join_btn.setEnabled(false);
                                join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
                            } else {
                                join_btn.setEnabled(true);
                                join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.green_pure));
                            }

                        }
                        else {
                            join_btn.setEnabled(false);
                            join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
                        }
                        finish();
                        // bottomSheetDialog.dismiss();

                       /* Intent intent=new Intent();
                        intent.putExtra("tempTeamId",playerListModel.getId());
                        setResult(Activity.RESULT_OK,intent);
                        finish();*/
                        //onBackPressed();
                    } else {
                        String message = responseBody.optString("msg");
                        CustomUtil.showTopSneakWarning(mContext, message);
                        LogUtil.e(TAG, "JOIN_CONTEST2MultiJoinWArning contest join");
                        if (responseBody.has("new_con_id")) {
                            final String new_con_id = responseBody.optString("new_con_id");
                            if (new_con_id != null && !new_con_id.equals("null") && !new_con_id.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LeaderBoardActivity.this);
                                builder.setTitle("The Contest is already full!!");//Almost there! That League filled up
                                builder.setMessage("Don't worry, we have same contest for you! join this contest.");//No worries, join this League instead! it is exactly the same type
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        contestData.setId(new_con_id);
                                        joinContest(contestData);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                // AlertDialog alertDialog = builder.create();
                                builder.setCancelable(false);
                                builder.show();
                            } else {
                                CustomUtil.showTopSneakError(mContext, "Please join another contest.");//Almost there! The League already filled Join any other League.
                            }
                        } else {
                            CustomUtil.showTopSneakError(mContext, message);
                        }
                    }
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });

        } else {
            Gson gson = new Gson();
            String model = gson.toJson(contestData);
            Intent intent = new Intent(mContext, TeamSelectJoinActivity.class);
            intent.putExtra("model", model);
            startActivity(intent);
        }

    }

    private void showBasicDetailDialog(ContestModel.ContestDatum contestData) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        ((View) view.getParent()).setBackgroundResource(android.R.color.white);

        UserModel user = preferences.getUserModel();

        RelativeLayout layImage = view.findViewById(R.id.layImage);
        layImage.setVisibility(View.GONE);
        LinearLayout layName = view.findViewById(R.id.layName);
        layName.setVisibility(View.GONE);
        ImageView toolbar_back = view.findViewById(R.id.toolbar_back);
        //toolbar_back.setVisibility(View.GONE);
        toolbar_back.setImageResource(R.drawable.ic_close_otp);
        toolbar_back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");

        EditText name_as_aadhar = view.findViewById(R.id.name_as_aadhar);
        EditText team_name = view.findViewById(R.id.edtTeamName);
        EditText mobile_number = view.findViewById(R.id.mobile_number);
        EditText email = view.findViewById(R.id.email);
        EditText dob = view.findViewById(R.id.dob);
        Spinner spinGender = view.findViewById(R.id.spinGender);
        Button confirm = view.findViewById(R.id.confirm);
        spinState = view.findViewById(R.id.spinState);

        name_as_aadhar.setText(user.getDisplayName());
        team_name.setText(user.getUserTeamName());
        mobile_number.setText(user.getPhoneNo());
        email.setText(user.getEmailId());
        dob.setText(user.getDob());

        selectedState = user.getStateId();

        /*if (TextUtils.isEmpty(user.getEmailId())){
            email.setEnabled(true);
        }else {
            email.setEnabled(false);
        }*/

        team_name.setEnabled(user.getTeam_name_change_allow().equalsIgnoreCase("yes"));

        ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter(mContext, R.layout.spinner_text, genderList);
        spinGender.setAdapter(genderAdapter);
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedGender = genderList.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.YEAR, -18);

        date = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dob);
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        date.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

        dob.setOnClickListener(view1 -> date.show());

        confirm.setOnClickListener(view1 -> {
            String db = dob.getText().toString().trim();
            String strEmail = getEditText(email);
            String strName = getEditText(name_as_aadhar);
            String strTeam = getEditText(team_name);

            if (TextUtils.isEmpty(strName)) {
                CustomUtil.showToast(mContext, "Please Enter Name as on Aadhar Card.");
            } else if (TextUtils.isEmpty(strTeam)) {
                CustomUtil.showToast(mContext, "Please Enter Team Name.");
            } else if (strTeam.length() > 11) {
                CustomUtil.showToast(mContext, "Team name should be less than or equals to 11 characters.");
            } else if (TextUtils.isEmpty(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Email.");
            } else if (!isValidEmail(strEmail)) {
                CustomUtil.showToast(mContext, "Please Enter Valid Email.");
            } else if (selectedState.equalsIgnoreCase("0")) {
                CustomUtil.showToast(mContext, "Please Select State.");
            } else if (TextUtils.isEmpty(db)) {
                CustomUtil.showToast(mContext, "Please Enter Date of Birth.");
            } else {
                callFirstApi(strName, strEmail, strTeam, db, bottomSheetDialog, contestData);
            }

        });

        switch (user.getGender()) {
            case "Male":
                spinGender.setSelection(1);
                break;
            case "Female":
                spinGender.setSelection(2);
                break;
            case "Other":
                spinGender.setSelection(3);
                break;
            default:
                spinGender.setSelection(0);
                break;
        }

        getStateData();

        bottomSheetDialog.show();
    }

    private void callFirstApi(String name, String email, String team, String dob, BottomSheetDialog bottomSheetDialog,
                              ContestModel.ContestDatum contestData) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email);
            jsonObject.put("display_name", name);
            jsonObject.put("state_id", selectedState);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("dob", dob);
            jsonObject.put("user_team_name", team);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(email);
                    userModel.setDisplayName(name);
                    userModel.setStateId(selectedState);
                    userModel.setGender(selectedGender);
                    userModel.setDob(dob);
                    userModel.setUserTeamName(team);
                    preferences.setUserModel(userModel);
                    MyApp.getMyPreferences().setUserModel(userModel);

                    bottomSheetDialog.dismiss();

                    confirmTeam(contestData);

                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                    LogUtil.e(TAG, "AUTHV3UpdateUserDetailsLeaderboardwarnig contest join");
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateLabel(EditText dob) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void getStateData() {
        HttpRestClient.postData(mContext, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                    cityName = new ArrayList<String>();
                    cityName.add("Select state");
                    cityId = new ArrayList<String>();
                    cityId.add("0");
                    ArrayList<StateModal> stateModals = new ArrayList<>();
                    int pos = 0;
                    if (responseBody.optBoolean("status")) {
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            String id = obj.optString("id");
                            if (selectedState.equals(id)) {
                                pos = i + 1;
                            }
                            cityId.add(id);
                            cityName.add(name);
                            StateModal stateModal = gson.fromJson(obj.toString(), StateModal.class);
                            stateModals.add(stateModal);
                        }
                        preferences.setStateList(stateModals);
                    }
                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text, cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState = cityId.get(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spinState.setSelection(pos);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 107) {
            if (data != null && data.hasExtra("tempTeamId")) {
                String tempTeamId = data.getStringExtra("tempTeamId");
                list.setJoinedTeamTempTeamId(list.getJoinedTeamTempTeamId() + "," + tempTeamId);
                int cnt = CustomUtil.convertInt(list.getMyJoinedTeam()) + 1;
                list.setMyJoinedTeam(cnt + "");
                //LogUtil.e("addreTeId",list.getJoinedTeamTempTeamId());
                if (CustomUtil.convertInt(list.getMaxJoinTeam()) > CustomUtil.convertInt(list.getMyJoinedTeam())) {
                    join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.green_pure));
                } else {
                    join_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btnGrey));
                }
            }
        }
    }

    private void downloadExcel(String excel_name) {
        final String excelFile = excel_name + ".xlsx";
        final String csvFile = excel_name + ".csv";

        Dexter.withActivity((Activity) mContext)
                .withPermissions(p)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        LogUtil.e(TAG, "onPermissionsChecked: " + ApiManager.EXCEL_DOWNLOAD + excelFile);
                        LogUtil.e(TAG, "onPermissionsChecked: " + ApiManager.EXCEL_DOWNLOAD + csvFile);
                        if (!exists(ApiManager.EXCEL_DOWNLOAD + excelFile)) {
                            if (!exists(ApiManager.EXCEL_DOWNLOAD + csvFile)) {
                                CustomUtil.showTopSneakError(mContext, "File not Available Currently try after Some time");
                            } else {
                                CustomUtil.showTopSneakSuccess(mContext, "Download Start in your Download Manager");
                                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                                Uri Download_Uri = Uri.parse(ApiManager.EXCEL_DOWNLOAD + csvFile);

                                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setAllowedOverRoaming(false);
                                request.setTitle("League Report Downloading " + csvFile);
                                request.setDescription("Downloading " + csvFile);
                                request.setVisibleInDownloadsUi(true);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Fantafeat/" + "/" + csvFile);

                                Long refid = downloadManager.enqueue(request);
                            }
                        } else {
                            Toast.makeText(mContext, "Download Start in your Download Manager", Toast.LENGTH_LONG).show();
                            DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                            Uri Download_Uri = Uri.parse(ApiManager.EXCEL_DOWNLOAD + excelFile);

                            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setAllowedOverRoaming(false);
                            request.setTitle("League Report Downloading " + excelFile);
                            request.setDescription("Downloading " + excelFile);
                            request.setVisibleInDownloadsUi(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Fantafeat/" + "/" + excelFile);

                            Long refid = downloadManager.enqueue(request);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public static boolean exists(String URLName) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull LeaderBoardActivity leaderBoardActivity) {
            super(leaderBoardActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return PrizePoolFragment.newInstance(list);
            } else {
                return LeaderBoardFragment.newInstance(list, is_same_team_cancel);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private void setTimer() {
        Date date = null;
        String matchDate = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat matchFormate = new SimpleDateFormat("dd MMM yyyy", Locale.US);
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
        /*} else {
            match_remain_time.setTextSize(14);
            match_remain_time.setText(List.getMatchStatus());
        }*/
    }

    private void timesOver() {
        TextView btn_ok;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_events, null);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                ConstantUtil.isTimeOverShow = false;
                Intent intent = new Intent(mContext, HomeActivity.class);
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
        if (!bottomSheetDialog.isShowing() && !ConstantUtil.isTimeOverShow) {
            ConstantUtil.isTimeOverShow = true;
            bottomSheetDialog.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}