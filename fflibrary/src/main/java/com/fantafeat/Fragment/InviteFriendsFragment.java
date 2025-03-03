package com.fantafeat.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.Activity.InviteFriendListActivity;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.CLIPBOARD_SERVICE;

public class InviteFriendsFragment extends BaseFragment {

   // Button share_btn, my_invited_friends_btn;
    Button phoneContect, btnWP,btnMore;

    ImageView copy_icon;
    TextView code,my_invited_friends_btn;
    ClipboardManager clipboard;
    private String shareBody="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HideBottomNavigationBar();
        View view = inflater.inflate(R.layout.fragment_referal, container, false);
        initFragment(view);
        initToolBar(view, "Invite Friends", true);
        return view;
    }

    @Override
    public void initControl(View view) {
        code = view.findViewById(R.id.code);
        phoneContect = view.findViewById(R.id.phoneContect);
        btnWP = view.findViewById(R.id.btnWP);
        btnMore = view.findViewById(R.id.btnMore);
        my_invited_friends_btn = view.findViewById(R.id.my_invited_friends_btn);
        /*share_btn = view.findViewById(R.id.share_btn);
        my_invited_friends_btn = view.findViewById(R.id.my_invited_friends_btn);*/
        copy_icon = view.findViewById(R.id.copy_icon);

        code.setText(preferences.getUserModel().getRefNo());

        if (ConstantUtil.is_bonus_show){
            shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                    preferences.getUserModel().getRefNo() +
                    "\n\nDownload Link: " + ApiManager.app_download_url+
                    preferences.getUserModel().getRefNo();
        }else {
            shareBody = "Thanks for interest in Batball11.\nClick below link to download the app : " +
                    "\n\nDownload Link: " + ApiManager.app_download_url+
                    preferences.getUserModel().getRefNo();
        }


        getReferalCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShowBottomNavigationBar();
    }

    private void getReferalCount() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("referCode", preferences.getUserModel().getRefNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, false, ApiManager.GET_REF_COUNT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {

                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    int level1 = CustomUtil.convertInt(data.optString("level_1"));
                    my_invited_friends_btn.setText("Your invited friends ("+level1+")");
                   /* int level2 = CustomUtil.convertInt(data.optString("level_2"));
                    int level3 = CustomUtil.convertInt(data.optString("level_3"));
                    int totalLevel = level1 + level2 + level3;

                    if (!preferences.getUserModel().getIsPromoter().equalsIgnoreCase("Yes")) {

                        referrals_tab.getTabAt(0).setText("Level 1 (" + level1 + ")");
                        referrals_tab.getTabAt(1).setText("Level 2 (" + level2 + ")");
                        referrals_tab.getTabAt(2).setText("Level 3 (" + level3 + ")");
                        referral_total_points.setText("Referral : " + totalLevel);
                    } else {
                        referral_total_points.setText("Referral : " + level1);
                    }*/
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {}
        });

    }

    @Override
    public void initClick() {

        my_invited_friends_btn.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                startActivity(new Intent(mContext, InviteFriendListActivity.class));
            }
        });
        copy_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("referral code", preferences.getUserModel().getRefNo());
                clipboard.setPrimaryClip(clip);
                CustomUtil.showTopSneakSuccess(mContext, "Copied to Clipboard");
            }
        });

        phoneContect.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {

                Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
              //  sharingIntent.setType("text/plain");
                sharingIntent.setData(Uri.parse("smsto:"));
                /*String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                        preferences.getUserModel().getRefNo() +
                        "\n\nDownload Link: " + ApiManager.app_download_url+
                        preferences.getUserModel().getRefNo();*/
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Fantafeat");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        btnWP.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.setPackage("com.whatsapp");
                /*String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                        preferences.getUserModel().getRefNo() +
                        "\n\nDownload Link: " +ApiManager.app_download_url+
                        preferences.getUserModel().getRefNo();*/
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Fantafeat");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }

        });

        btnMore.setOnClickListener(view -> {
            if (MyApp.getClickStatus()) {

                /*String url= *//*ConstantUtil.LINK_URL+*//*"https://www.fantafeat.com/download_ff_apk.php?refcode=" + preferences.getUserModel().getRefNo();
                LogUtil.e("resp",url);
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse(url))
                        .setDomainUriPrefix(ConstantUtil.LINK_PREFIX_URL)
                        .setIosParameters(
                                new DynamicLink.IosParameters.Builder(ConstantUtil.FF_IOS_APP_BUNDLE).setAppStoreId(ConstantUtil.FF_IOS_APP_ID)
                                        .setFallbackUrl(Uri.parse(ConstantUtil.FALL_BACK_LINK)).build())
                        .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                        .addOnCompleteListener((Activity) mContext, task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().getShortLink()!=null)
                                    shareShortLink(task.getResult().getShortLink());
                                else {
                                    CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                                }
                            } else {
                                CustomUtil.showTopSneakWarning(mContext,"Can't share this");
                            }
                        });

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");*/

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                /*String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                        preferences.getUserModel().getRefNo() +
                        "\n\nDownload the Fantafeat app from here: " +ApiManager.app_download_url+preferences.getUserModel().getRefNo();*/
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Fantafeat");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

       /* my_invited_friends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApp.getClickStatus()) {
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new MyReferralsFragment(),
                            ((HomeActivity) mContext).fragmentTag(26),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");

                    String shareBody = "Gear up for the ultimate fantasy affair where fairplay " +
                            "and transparency will take a front seat! It’s a place for real users " +
                            "having real passion for cricket and experiencing the real fantasy " +
                            "cricket world.\n" + "\n" +
                            "To get 50 INR Rewards at Batball11 use my referral code : " +
                            preferences.getUserModel().getRefNo() +
                            "\n\nDownload Link: https://www.batball11.com/download_app.php?ref_code=" +
                            preferences.getUserModel().getRefNo();
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download BatBall11");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            }
        });*/
    }

    private void shareShortLink(Uri link){

        /*String sportsName="";

        for (SportsModel sportsModel:MyApp.getMyPreferences().getSports()) {
            if (sportsModel.getId().equalsIgnoreCase(MyApp.getMyPreferences().getMatchModel().getSportId())){
                sportsName=sportsModel.getSportName();
            }
        }*/

        String content="Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                preferences.getUserModel().getRefNo()+
                "\n" +
                "Download the Fantafeat app from here: "+link+"\n";
        //int spotsLeft = totalSpots - jointeam;

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        /*String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                preferences.getUserModel().getRefNo() +
                "\n\nDownload Link: " +ApiManager.app_download_url+
                preferences.getUserModel().getRefNo();*/
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Fantafeat");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

        /*try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }catch (ActivityNotFoundException e){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        }*/
    }

}