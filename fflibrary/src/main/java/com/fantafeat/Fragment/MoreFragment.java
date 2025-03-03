package com.fantafeat.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.fantafeat.Activity.CallbackSelectQueryActivity;
import com.fantafeat.Activity.HelpDeskActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.PromoteAppActivity;
import com.fantafeat.Activity.WebViewActivity;
import com.fantafeat.BuildConfig;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FragmentUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class MoreFragment extends BaseFragment {

    TextView about_bb11, social_respo, how_to_use, user_benefit, app_features, point_system,
            terms_condition, privacy_policy, affiliation,call_back,call_promote,txt_social,txtPromoteLink;
   // private ImageView toolbar_menu;
    private ImageView drawer_image;
    public ImageView imgWp, imgFb, imgInsta,imgTele,imgTweet,imgTeleMsg;
    View affiliation_line,viewLink;
    TextView app_version;
    BottomSheetDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initFragment(view);

        return view;
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public void initControl(View view) {
        about_bb11 = view.findViewById(R.id.about_bb11);
        social_respo = view.findViewById(R.id.social_respo);
        how_to_use = view.findViewById(R.id.how_to_use);
        user_benefit = view.findViewById(R.id.user_benefit);
        app_features = view.findViewById(R.id.app_features);
        point_system = view.findViewById(R.id.point_system);
        terms_condition = view.findViewById(R.id.terms_condition);
        privacy_policy = view.findViewById(R.id.privacy_policy);
        drawer_image = view.findViewById(R.id.drawer_image);
        affiliation = view.findViewById(R.id.affiliation);
        call_back = view.findViewById(R.id.call_back);
        call_promote = view.findViewById(R.id.call_promote);
        affiliation_line = view.findViewById(R.id.affiliation_line);
        app_version = view.findViewById(R.id.app_version);
        txt_social = view.findViewById(R.id.txt_social);
        txtPromoteLink = view.findViewById(R.id.txtPromoteLink);
        viewLink = view.findViewById(R.id.viewLink);

        //UserModel userModel=preferences.getUserModel();

        /*if (userModel.getIsPromoter().equalsIgnoreCase("yes")){
            txtPromoteLink.setVisibility(View.VISIBLE);
            viewLink.setVisibility(View.VISIBLE);
        }else {
            txtPromoteLink.setVisibility(View.GONE);
            viewLink.setVisibility(View.GONE);
        }*/

      /*  imgWp = view.findViewById(R.id.imgWp);
        imgFb = view.findViewById(R.id.imgFb);
        imgInsta = view.findViewById(R.id.imgInsta);
        imgTele = view.findViewById(R.id.imgTele);*/

        String versionName = BuildConfig.VERSION_NAME;
        app_version.setText("Version  "+versionName);

       /* if(preferences.getUserModel().getUserImg().equals("")){
            Glide.with(mContext)
                    .load(R.drawable.user_image)
                    .placeholder(R.drawable.user_image)
                    .error(R.drawable.user_image)
                    .into(toolbar_menu);
        }else{
            LogUtil.e(TAG, "onCreate: "+ ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg());
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                    .error(R.drawable.user_image)
                    .placeholder(R.drawable.user_image)
                    .into(toolbar_menu);
        }*/

      /*  if(preferences.getUserModel().getIsPromoter().equalsIgnoreCase("yes")){
            affiliation.setVisibility(View.VISIBLE);
            affiliation_line.setVisibility(View.VISIBLE);
        }else{
            affiliation.setVisibility(View.GONE);
            affiliation_line.setVisibility(View.GONE);
        }*/

       /* if (preferences.getUserModel()!=null && !TextUtils.isEmpty(preferences.getUserModel().getUserImg())){
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG +preferences.getUserModel().getUserImg())
                    .placeholder(R.drawable.user_image)
                    .into(drawer_image);
        }*/

    }

    private void initSocialClick(BottomSheetDialog bottomSheetDialog) {
        imgWp.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                String url = ApiManager.whatsapp_url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        });
        imgFb.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_id));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.fb_page_url)));
                }
            }

        });
        imgTele.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                String url = ApiManager.telegram_id;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        imgInsta.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                String url = ApiManager.instagram_id;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        });
        imgTweet.setOnClickListener(view -> {
            if (MyApp.getClickStatus()){
                if (bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    bottomSheetDialog.dismiss();
                }
                String url = ApiManager.tweet_url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        imgTeleMsg.setOnClickListener(view -> {
            try {
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/"+ApiManager.support_username));
                startActivity(telegram);
               /* String url = "https://t.me/"+ApiManager.support_username;//2122548406
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("org.telegram.messenger");
                i.setData(Uri.parse(url));
                startActivity(i);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setStatusBarDark();
    }

    /*private void initSocialClick() {
        imgWp.setOnClickListener(view -> {
            //9625449625
            String url = "https://api.whatsapp.com/send?phone=+919625449625";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        imgFb.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/2216938415251012"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/batball11")));
            }
         */
    /*   String url = "https://www.facebook.com/batball11/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);*/
    /*
        });
        imgTele.setOnClickListener(view -> {
            String url = "https://t.me/batballeleven";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        imgInsta.setOnClickListener(view -> {
            String url = "https://www.instagram.com/batball11official/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }*/

    @Override
    public void initClick() {

        drawer_image.setOnClickListener(view -> OpenNavDrawer());

        txtPromoteLink.setOnClickListener(v -> {
            if (MyApp.getClickStatus()){
                showPromotorsLinkDialog();
            }
        });

        about_bb11.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,about_bb11.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.about_url));
               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(, ),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        how_to_use.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,how_to_use.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.how_to_play_url));
            }
        });

        user_benefit.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,user_benefit.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.user_benifit_url));
                /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(, ),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        app_features.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,app_features.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.app_feature_url));
           /*     new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(, ),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        point_system.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,point_system.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL, ApiManager.app_point_url));
               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(,),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        terms_condition.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,terms_condition.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.term_condition_url));
               /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(, ),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        privacy_policy.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra(ConstantUtil.WEB_TITLE,privacy_policy.getText().toString())
                        .putExtra(ConstantUtil.WEB_URL,ApiManager.privacy_url));
                /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new WebViewFragment(, ),
                        ((HomeActivity) mContext).fragmentTag(17),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        affiliation.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new AffiliationFragment(),
                        ((HomeActivity) mContext).fragmentTag(18),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);
            }
        });

        call_back.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(mContext, HelpDeskActivity.class));
                //startActivity(new Intent(mContext, CallbackSelectQueryActivity.class));
                /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new CallBackFragment(),
                        ((HomeActivity) mContext).fragmentTag(20),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        call_promote.setOnClickListener(view -> {
            if(MyApp.getClickStatus()) {
                startActivity(new Intent(getActivity(), PromoteAppActivity.class));
            }
        });

        txt_social.setOnClickListener(v->{
            if (MyApp.getClickStatus()){
                showSheet();
            }
        });
    }

    private void showPromotorsLinkDialog() {
        dialog = new BottomSheetDialog(mContext);
        View bottomSheet = getLayoutInflater().inflate(R.layout.promotors_link_detail, null);
        //dialog.setCancelable(false);

        UserModel userModel=preferences.getUserModel();

        EditText edtInstagram=bottomSheet.findViewById(R.id.edtInstagram);
        edtInstagram.setText(userModel.getInsta_id());
        EditText edtFacebook=bottomSheet.findViewById(R.id.edtFacebook);
        edtFacebook.setText(userModel.getFacebook_id());
        EditText edtTelegram=bottomSheet.findViewById(R.id.edtTelegram);
        edtTelegram.setText(userModel.getTelegram_id());
        EditText edtYoutube=bottomSheet.findViewById(R.id.edtYoutube);
        edtYoutube.setText(userModel.getYoutube_id());
        EditText edtOther=bottomSheet.findViewById(R.id.edtOther);
        edtOther.setText(userModel.getTwitter_id());
        TextView btnSubmit=bottomSheet.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String insta=edtInstagram.getText().toString().trim();
            String fb=edtFacebook.getText().toString().trim();
            String tele=edtTelegram.getText().toString().trim();
            String yt=edtYoutube.getText().toString().trim();
            String other=edtOther.getText().toString().trim();

            boolean isDataAvailable = false;

            if (!TextUtils.isEmpty(fb) ||
                    !TextUtils.isEmpty(insta) ||
                    !TextUtils.isEmpty(tele) ||
                    !TextUtils.isEmpty(other) ||
                    !TextUtils.isEmpty(yt)
            ){
                isDataAvailable = true;
            }

            if (!isDataAvailable){
                Toast.makeText(mContext,"We need at least one of your social details",Toast.LENGTH_LONG).show();
                return;
            }

            getUpdatePromotorDetails(insta,fb,tele,yt,other);

        });

        dialog.setContentView(bottomSheet);
        dialog.show();
    }

    private void getUpdatePromotorDetails(String insta,String fb,String tele,String yt,String other) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("insta_id", insta);
            jsonObject.put("facebook_id", fb);
            jsonObject.put("telegram_id", tele);
            jsonObject.put("youtube_id", yt);
            jsonObject.put("twitter_id", other);
            Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.updateUserVerifyDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                Log.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {

                        CustomUtil.showTopSneakSuccess(mContext,"Detail updated successfully");

                        UserModel userModel = preferences.getUserModel();
                        userModel.setInsta_id(insta);
                        userModel.setFacebook_id(fb);
                        userModel.setTelegram_id(tele);
                        userModel.setYoutube_id(yt);
                        userModel.setTwitter_id(other);
                        preferences.setUserModel(userModel);

                        if (dialog!=null && dialog.isShowing()){
                            dialog.dismiss();
                        }

                    }
                } else {
                    CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                }
                // mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: " );
                // mUserDetail = true;
            }
        });
    }

    private void showSheet() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_social_connection, null);

        imgWp = view.findViewById(R.id.imgWp);
        imgFb = view.findViewById(R.id.imgFb);
        imgInsta = view.findViewById(R.id.imgInsta);
        imgTele = view.findViewById(R.id.imgTele);
        imgTweet = view.findViewById(R.id.imgTweet);
        imgTeleMsg = view.findViewById(R.id.imgTeleMsg);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(view);
        ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialog.show();

        initSocialClick(bottomSheetDialog);
    }

}