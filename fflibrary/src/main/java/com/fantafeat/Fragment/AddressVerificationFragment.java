package com.fantafeat.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fantafeat.Activity.FullImageActivity;
import com.fantafeat.Activity.ImagePickerActivity;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FileUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class AddressVerificationFragment extends BaseFragment {
    private RadioButton rBtnAadhaar,rBtnLicence,rBtnVoter;
    private ImageView imgCloseFront,imgFront,imgCloseBack,imgBack;
    private Button btnUpload;
    private LinearLayout layAadhaar,layLicence,layVoter,layMsg;
    private File frontFile=null,backFile=null;
    private TextView btnFileChoose,txtMsgTitle,txtMsgSubTitle;
    private String imageSideTag="Front View";
    private String proofType="";

    UserModel userModel;

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
    };
    String[] p;
    public AddressVerificationFragment() {
        // Required empty public constructor
    }

    public static AddressVerificationFragment newInstance(String param1, String param2) {
        AddressVerificationFragment fragment = new AddressVerificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_address_verification, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;

        }

        rBtnAadhaar=view.findViewById(R.id.rBtnAadhaar);
        rBtnAadhaar.setClickable(false);
        rBtnLicence=view.findViewById(R.id.rBtnLicence);
        rBtnLicence.setClickable(false);
        rBtnVoter=view.findViewById(R.id.rBtnVoter);
        rBtnVoter.setClickable(false);
        imgCloseFront=view.findViewById(R.id.imgCloseFront);
        imgFront=view.findViewById(R.id.imgFront);
        imgCloseBack=view.findViewById(R.id.imgCloseBack);
        imgBack=view.findViewById(R.id.imgBack);
        btnFileChoose=view.findViewById(R.id.btnFileChoose);
        btnUpload=view.findViewById(R.id.btnUpload);
        layAadhaar=view.findViewById(R.id.layAadhaar);
        layLicence=view.findViewById(R.id.layLicence);
        layVoter=view.findViewById(R.id.layVoter);
        layMsg=view.findViewById(R.id.layMsg);
        txtMsgTitle=view.findViewById(R.id.txtMsgTitle);
        txtMsgSubTitle=view.findViewById(R.id.txtMsgSubTitle);

        userModel=preferences.getUserModel();

        if (userModel.getAddr_prf_status().toLowerCase().equals("inreview")) {
            layVoter.setEnabled(false);
            layLicence.setEnabled(false);
            layAadhaar.setEnabled(false);
            imgCloseFront.setVisibility(View.GONE);
            imgCloseBack.setVisibility(View.GONE);

            btnUpload.setVisibility(View.GONE);

            if (userModel.getAddr_prf_type().equalsIgnoreCase("voterid")){
                layVoter.performClick();
                layVoter.setVisibility(View.VISIBLE);
                layLicence.setVisibility(View.GONE);
                layAadhaar.setVisibility(View.GONE);
            }else if (userModel.getAddr_prf_type().equalsIgnoreCase("licence")){
                layLicence.performClick();
                layVoter.setVisibility(View.GONE);
                layLicence.setVisibility(View.VISIBLE);
                layAadhaar.setVisibility(View.GONE);
            }else if (userModel.getAddr_prf_type().equalsIgnoreCase("aadhaar")){
                layAadhaar.performClick();
                layVoter.setVisibility(View.GONE);
                layLicence.setVisibility(View.GONE);
                layAadhaar.setVisibility(View.VISIBLE);
            }

            layMsg.setVisibility(View.VISIBLE);

            txtMsgTitle.setText("Verification is In-Review");
            txtMsgSubTitle.setText("You have already submitted a documents. Please allow sometime to verify.");

            CustomUtil.loadImageWithGlide(mContext,imgFront,MyApp.imageBase + MyApp.document,userModel.getAddr_prf_front_img(), R.drawable.add_image);
            CustomUtil.loadImageWithGlide(mContext,imgBack,MyApp.imageBase + MyApp.document,userModel.getAddr_prf_back_img(), R.drawable.add_image);

        }
        else if (userModel.getAddr_prf_status().toLowerCase().equals("reject")) {
            layVoter.setEnabled(true);
            layLicence.setEnabled(true);
            layAadhaar.setEnabled(true);
            imgCloseFront.setEnabled(true);
            imgCloseBack.setEnabled(true);
            btnUpload.setVisibility(View.VISIBLE);

            layMsg.setVisibility(View.GONE);

            imgFront.setImageResource(R.drawable.add_image);
            imgBack.setImageResource(R.drawable.add_image);

        }
        else if (userModel.getAddr_prf_status().toLowerCase().equals("approve")) {
            layVoter.setEnabled(false);
            layLicence.setEnabled(false);
            layAadhaar.setEnabled(false);
            imgCloseFront.setVisibility(View.GONE);
            imgCloseBack.setVisibility(View.GONE);

            btnUpload.setVisibility(View.GONE);

            layMsg.setVisibility(View.VISIBLE);

            txtMsgTitle.setText("Your address is verified");
            txtMsgSubTitle.setText("");

            if (userModel.getAddr_prf_type().equalsIgnoreCase("voterid")){
                layVoter.performClick();
                layVoter.setVisibility(View.VISIBLE);
                layLicence.setVisibility(View.GONE);
                layAadhaar.setVisibility(View.GONE);
                rBtnVoter.setChecked(true);
            }else if (userModel.getAddr_prf_type().equalsIgnoreCase("licence")){
                layLicence.performClick();
                layVoter.setVisibility(View.GONE);
                layLicence.setVisibility(View.VISIBLE);
                layAadhaar.setVisibility(View.GONE);
                rBtnLicence.setChecked(true);
            }else if (userModel.getAddr_prf_type().equalsIgnoreCase("aadhaar")){
                layAadhaar.performClick();
                layVoter.setVisibility(View.GONE);
                layLicence.setVisibility(View.GONE);
                layAadhaar.setVisibility(View.VISIBLE);
                rBtnAadhaar.setChecked(true);
            }

            CustomUtil.loadImageWithGlide(mContext,imgFront,MyApp.imageBase + MyApp.document,userModel.getAddr_prf_front_img(), R.drawable.add_image);
            CustomUtil.loadImageWithGlide(mContext,imgBack,MyApp.imageBase + MyApp.document,userModel.getAddr_prf_back_img(), R.drawable.add_image);
        }
    }

    @Override
    public void initClick() {
        layAadhaar.setOnClickListener(v -> {
            frontFile=null;
            imgFront.setImageResource(R.drawable.add_image);
            backFile=null;
            imgBack.setImageResource(R.drawable.add_image);

            rBtnAadhaar.setChecked(true);
            rBtnLicence.setChecked(false);
            rBtnVoter.setChecked(false);

            proofType="aadhaar";
        });

        layLicence.setOnClickListener(v -> {
            frontFile=null;
            imgFront.setImageResource(R.drawable.add_image);
            backFile=null;
            imgBack.setImageResource(R.drawable.add_image);

            rBtnVoter.setChecked(false);
            rBtnLicence.setChecked(true);
            rBtnAadhaar.setChecked(false);

            proofType="licence";
        });

        layVoter.setOnClickListener(v -> {
            frontFile=null;
            imgFront.setImageResource(R.drawable.add_image);
            backFile=null;
            imgBack.setImageResource(R.drawable.add_image);

            rBtnVoter.setChecked(true);
            rBtnLicence.setChecked(false);
            rBtnAadhaar.setChecked(false);

            proofType="voterid";
        });
        imgCloseFront.setOnClickListener(v -> {
            if (frontFile==null){
                CustomUtil.showToast(mContext,"Choose Image first");
                return;
            }

            frontFile=null;
            imgFront.setImageResource(R.drawable.add_image);
        });

        imgCloseBack.setOnClickListener(v -> {
            if (backFile==null){
                CustomUtil.showToast(mContext,"Choose Image first");
                return;
            }

            backFile=null;
            imgBack.setImageResource(R.drawable.add_image);
        });

        btnUpload.setOnClickListener(v -> {
            if (frontFile==null){
                CustomUtil.showToast(mContext,"Choose Front Image");
                return;
            }
            if (backFile==null){
                CustomUtil.showToast(mContext,"Choose Back Image");
                return;
            }

            sendImage(frontFile,"addr_prf_front_img",true);

        });

        imgFront.setOnClickListener(v -> {
            if (userModel.getAddr_prf_status().toLowerCase().equals("inreview")) {
                if (MyApp.getClickStatus()) {
                    Intent i=new Intent(mContext, FullImageActivity.class);
                    i.putExtra("imageBase",MyApp.imageBase + MyApp.document);
                    i.putExtra("imageName",userModel.getAddr_prf_front_img());
                    startActivity(i);
                }
            }else if (userModel.getAddr_prf_status().toLowerCase().equals("approve")) {
                if (MyApp.getClickStatus()) {
                    Intent i=new Intent(mContext, FullImageActivity.class);
                    i.putExtra("imageBase",MyApp.imageBase + MyApp.document);
                    i.putExtra("imageName",userModel.getAddr_prf_front_img());
                    startActivity(i);
                }
            }else {

                if (TextUtils.isEmpty(proofType)){
                    CustomUtil.showToast(mContext,"Choose Proof type");
                    return;
                }

                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {

                                    imageSideTag="Front View";

                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    LogUtil.e("TAG", "onPermissionsChecked: ");
                                    showSettingsDialog();
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

        });

        imgBack.setOnClickListener(v -> {
            if (userModel.getAddr_prf_status().toLowerCase().equals("inreview")) {
                if (MyApp.getClickStatus()) {
                    Intent i=new Intent(mContext, FullImageActivity.class);
                    i.putExtra("imageBase",MyApp.imageBase + MyApp.document);
                    i.putExtra("imageName",userModel.getAddr_prf_back_img());
                    startActivity(i);
                }
            }else if (userModel.getAddr_prf_status().toLowerCase().equals("approve")) {
                if (MyApp.getClickStatus()) {
                    Intent i=new Intent(mContext, FullImageActivity.class);
                    i.putExtra("imageBase",MyApp.imageBase + MyApp.document);
                    i.putExtra("imageName",userModel.getAddr_prf_back_img());
                    startActivity(i);
                }
            }else {
                if (TextUtils.isEmpty(proofType)) {
                    CustomUtil.showToast(mContext, "Choose Proof type");
                    return;
                }
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted

                                if (report.areAllPermissionsGranted()) {

                                    imageSideTag = "Back View";

                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    LogUtil.e("TAG", "onPermissionsChecked: ");
                                    showSettingsDialog();
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
        });

        btnFileChoose.setOnClickListener(v -> {
            if (TextUtils.isEmpty(proofType)){
                CustomUtil.showToast(mContext,"Choose Proof type");
                return;
            }
            Dexter.withActivity((Activity) mContext)
                    .withPermissions(p)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted

                            if (report.areAllPermissionsGranted()) {

                                if (btnFileChoose.getText().toString().equalsIgnoreCase("Upload Front Image")){
                                    imageSideTag="Front View";
                                }else {
                                    imageSideTag="Back View";
                                }

                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                LogUtil.e("TAG", "onPermissionsChecked: ");
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        });
    }

    private void showImagePickerOptions(){
        ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                System.out.println("capture12");
                launchCameraIntent();
                System.out.println("capture");
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 2);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        // ((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    private void launchGalleryIntent(){
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        //((Activity)context).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null){
                            Uri uri = data.getParcelableExtra("path");
                            try {
                                long fileSizeInBytes = FileUtil.from(mContext,uri).length();
                                long fileSizeInKB = fileSizeInBytes / 1024;
                                long fileSizeInMB = fileSizeInKB / 1024;
                                LogUtil.e("filse","fileSizeInMB: "+fileSizeInMB+"   fileSizeInKB:"+fileSizeInKB);
                                if (fileSizeInKB >= 1000) {
                                    CustomUtil.showTopSneakWarning(mContext,"Choose Max 1 MB file size");
                                    return;
                                }

                                if (imageSideTag.equalsIgnoreCase("Front View")){
                                    imageSideTag="Back View";
                                    frontFile=FileUtil.from(mContext,uri);
                                    imgFront.setImageURI(uri);
                                }else {
                                    backFile=FileUtil.from(mContext,uri);
                                    imgBack.setImageURI(uri);
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    private void sendImage(File image_file,String name,boolean isCall) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", preferences.getUserModel().getId());
        params.put("addr_prf_type", proofType);

        LogUtil.e("TAG", "sendImage: " + params.toString());
        HttpRestClient.postDataFile(mContext, true, ApiManager.UPLOAD_ADDRESS_IMAGE, params, image_file, name, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e("TAG", "onSuccessResultaddress: " + responseBody.toString());
                    if (isCall) {
                        sendImage(backFile, "addr_prf_back_img", false);
                    }else {
                        getUserDetail();
                    }
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("TAG", responseBody);
            }
        });
    }

    private void getUserDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.USER_DETAIL, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "USER USER_DETAIL: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject object = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(object.toString(), UserModel.class);
                    float total = CustomUtil.convertFloat(userModel.getDepositBal()) + CustomUtil.convertFloat(userModel.getWinBal())
                            + CustomUtil.convertFloat(userModel.getTransferBal()) + CustomUtil.convertFloat(userModel.getBonusBal());
                    userModel.setTotal_balance(total);
                    preferences.setUserModel(userModel);

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e("AddressVerificationFail: ",responseBody);
            }
        });
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}