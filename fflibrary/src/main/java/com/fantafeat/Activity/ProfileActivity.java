package com.fantafeat.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.FileUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    EditText name_as_aadhar,  mobile_number, email,team_name, dob;//
    Button confirm;
    TextView profile_name, profile_tag;//complete_now,followers, following,completed_your_profile_per
    //ProgressBar profile_progress;
    ImageView edit_btn,toolbar_back;//share,
    CircleImageView profile_img;
    private int PICK_IMAGE_REQUEST = 1;
    private int RESULT_CROP = 2;
    File user_image_file;
    private ArrayList<String> cityName;
    private ArrayList<String> cityId;
    private String selectedState="0";
    private String selectedGender="Select Gender";
    private Spinner spinState,spinGender;
    Calendar myCalendar;
    DatePickerDialog date;
    // LinearLayout profile_kyc;
    Cursor cursor;
    // LinearLayout following_follower;
    String reSizeImagePath = "/Android/data/com.fantafeat/cache/", result_utl_to_pass;
    private static final int REQUEST_IMAGE = 99;

    private static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
    };

    String[] p;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        initData();
        getStateData();
        initClic();

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String userEmail = intent.getStringExtra("email");

            if (name != null) {
                name_as_aadhar.setText(name);
            }
            if (userEmail != null) {
                email.setText( userEmail);
            }
        }
    }

    private void initClic() {
        toolbar_back.setOnClickListener(view->{
            finish();
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(dob.getText().toString())){
                    date.show();
                }
            }
        });
        /*following_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* if (MyApp.getClickStatus()) {
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new FollowFollowersFragment(),
                            fragmentTag(30),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }*//*
            }
        });*/


        profile_img.setOnClickListener(view -> {
            LogUtil.e(TAG, "onClick: ");
            if (MyApp.getClickStatus()) {
                hideKeyboard((Activity) mContext);
                startActivity(new Intent(mContext, FullImageActivity.class)
                        .putExtra(ConstantUtil.FULL_IMAGE_PATH,ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                        .putExtra("pageFrom","profile")
                );
            /*    new FragmentUtil().addFragment((FragmentActivity) mContext,
                        R.id.home_fragment_container,
                        new FullImageFragment(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg()),
                        ((HomeActivity) mContext).fragmentTag(21),
                        FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
            }
        });

        /*share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    shareNow();
                }
            }
        });*/

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if (TextUtils.isEmpty(preferences.getUserModel().getUserImg())){
                                        chooseImage();
                                        /*ImagePicker.Companion.with((Activity) mContext)
                                                .cropSquare()
                                                .start();*/
                                    }else {
                                        showUpdateDialog();
                                    }

                                    /*CropImage.activity()
                                            .setAspectRatio(1, 1)
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setMultiTouchEnabled(true)
                                            .start(mContext, ProfileFragment.this);*/
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    LogUtil.e(TAG, "onPermissionsChecked: ");
                                    showSettingsDialog();
                                    // permission is denied permenantly, navigate user to app settings
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

    /*    complete_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new UpdateKYCFragment(),
                            ((HomeActivity) mContext).fragmentTag(22),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);
                }
            }
        });*/

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (validate()) {
                        callFirstApi();
                    }
                }
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

       // ((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
       // ((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    if (data!=null){
                        try {
                            Uri uri = data.getParcelableExtra("path");

                            user_image_file= FileUtil.from(mContext,uri);

                            profile_img.setImageURI(uri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        CustomUtil.showTopSneakWarning(mContext,"Please try after some time");
                    }
                }
            });

    private void chooseImage(){
        showImagePickerOptions();
         /* ImagePicker.Companion.with((UpdateKYCActivity) context)
                    .maxResultSize(900,900)
                    .crop()
                    .start();*/
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void showUpdateDialog() {
        String[] arr=new String[]{"Update","Remove"};
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setItems(arr, (dialogInterface, i) -> {
            if (i==0){
                chooseImage();
                /*ImagePicker.Companion.with((Activity) mContext)
                        .cropSquare()
                        .start();*/
            }else {
                dialogInterface.dismiss();
                confirmRemove();
            }
        });
        alert.show();
    }

    private void confirmRemove() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to remove profile?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", preferences.getUserModel().getId());
                    jsonObject.put("email_id", email.getText().toString());
                    jsonObject.put("display_name", name_as_aadhar.getText().toString());
                    jsonObject.put("dob", dob.getText().toString());
                    jsonObject.put("state_id", selectedState);
                    jsonObject.put("gender", selectedGender);
                    jsonObject.put("user_img", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpRestClient.postJSON(mContext, true, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
                    @Override
                    public void onSuccessResult(JSONObject responseBody, int code) {
                        if (responseBody.optBoolean("status")) {
                            LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                            UserModel userModel = preferences.getUserModel();
                            userModel.setEmailId(Objects.requireNonNull(email.getText().toString()));
                            userModel.setDisplayName(name_as_aadhar.getText().toString());
                            userModel.setDob(dob.getText().toString());
                            userModel.setStateId(selectedState);
                            userModel.setGender(selectedGender);
                            userModel.setUserImg("");
                            preferences.setUserModel(userModel);

                            setData();
                            CustomUtil.showTopSneakSuccess(mContext,"Profile remove success");
                        } else {
                            CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                        }
                    }

                    @Override
                    public void onFailureResult(String responseBody, int code) {

                    }
                });
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    private void shareNow() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                preferences.getUserModel().getRefNo() +
                "\n\nDownload Link : " +ApiManager.app_download_url+
                preferences.getUserModel().getRefNo();

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Fantafeat");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void callFirstApi() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("email_id", email.getText().toString().trim());
            jsonObject.put("display_name", name_as_aadhar.getText().toString().trim());
            jsonObject.put("dob", dob.getText().toString().trim());
            jsonObject.put("state_id", selectedState);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("user_team_name", team_name.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3UpdateUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());

                    /*UserModel userModel = preferences.getUserModel();
                    userModel.setEmailId(Objects.requireNonNull(email.getText().toString().trim()));
                    userModel.setDisplayName(name_as_aadhar.getText().toString().trim());
                    userModel.setDob(dob.getText().toString().trim());
                    userModel.setStateId(selectedState);
                    userModel.setGender(selectedGender);
                    userModel.setUserTeamName(team_name.getText().toString().trim());
                    preferences.setUserModel(userModel);*/

                    if (user_image_file == null) {
                        CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));
                        getUserDetails();
                    } else {
                        callApi();
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

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());

            Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, false, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    if (responseBody.optString("code").equals("200")) {
                        JSONObject data = responseBody.optJSONObject("data");
                        UserModel userModel = gson.fromJson(data.toString(), UserModel.class);

                        result_utl_to_pass = ApiManager.PROFILE_IMG + userModel.getUserImg();

                        preferences.setUserModel(userModel);
                        setData();
                    }
                } else {
                    preferences.setUserModel(new UserModel());
                }
                // mUserDetail = true;
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                Log.e(TAG, "onFailureResult: ");
                // mUserDetail = true;
                preferences.setUserModel(new UserModel());

            }
        });
    }

    private void callApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", preferences.getUserModel().getId());

        LogUtil.e(TAG, "sendImage: " + params.toString());
        HttpRestClient.postDataFile(mContext, true, ApiManager.UPLOAD_USER_IMAGE, params, user_image_file, "user_image", new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {

                    /*UserModel userModel = preferences.getUserModel();
                    userModel.setUserImg(responseBody.optString("msg"));
                    preferences.setUserModel(userModel);
                    result_utl_to_pass = ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg();

                    setData();*/
                    CustomUtil.showTopSneakSuccess(mContext, "Details Updated Successfully.");
                    getUserDetails();
                    //HomeActivity.profile_name.setText(name_as_aadhar.getText().toString());
                }else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e(TAG, "onFailureResult: ");
            }
        });
    }

    private boolean validate() {
        if (getEditText(name_as_aadhar).equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Name as on Aadhar Card.");
            return false;
        }
        else if (getEditText(email).equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Email.");
            return false;
        }
        else if (!isValidEmail(getEditText(email))) {
            CustomUtil.showTopSneakError(mContext, "Please Enter Valid Email.");
            return false;
        }
        return true;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    user_image_file= FileUtil.from(mContext,uri);

                    profile_img.setImageURI(uri);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void getStateData() {
        HttpRestClient.postData(mContext, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                try {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                    cityName=new ArrayList<String>();
                    cityName.add("Select state");
                    cityId=new ArrayList<String>();
                    cityId.add("0");
                    int pos=0;
                    if(responseBody.optBoolean("status")){
                        JSONArray jsonArray = responseBody.getJSONArray("data");

                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject obj=jsonArray.getJSONObject(i);
                            String name = obj.optString("name");
                            String id = obj.optString("id");
                            if (selectedState.equals(id)){
                                pos=i+1;
                            }
                            cityId.add(id);
                            cityName.add(name);
                        }
                    }
                    ArrayAdapter<String> stateAdapter=new ArrayAdapter<String>(mContext,R.layout.spinner_text,cityName);
                    spinState.setAdapter(stateAdapter);
                    spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            selectedState=cityId.get(pos);
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

    private void initData() {
        name_as_aadhar = findViewById(R.id.name_as_aadhar);
        team_name = findViewById(R.id.edtTeamName);
        mobile_number = findViewById(R.id.mobile_number);
        toolbar_back = findViewById(R.id.toolbar_back);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        confirm = findViewById(R.id.confirm);
        spinState = findViewById(R.id.spinState);
        spinGender = findViewById(R.id.spinGender);
        //complete_now = findViewById(R.id.complete_now);
        profile_name = findViewById(R.id.profile_name);
        profile_tag = findViewById(R.id.profile_tag);
        /*followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        completed_your_profile_per = findViewById(R.id.completed_your_profile_per);
        profile_progress = findViewById(R.id.profile_progress);
        share = findViewById(R.id.share);*/
        edit_btn = findViewById(R.id.edit_btn);
        profile_img = findViewById(R.id.profile_img);
        /// profile_kyc = findViewById(R.id.profile_kyc);
        // following_follower = findViewById(R.id.following_follower);

        //Log.d("modal",preferences.getUserModel().getUserImg());



        /*if (preferences.getUserModel().getUserImg().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.user_image)
                    .placeholder(R.drawable.user_image)
                    .error(R.drawable.user_image)
                    .into(profile_img);
        } else {
            Glide.with(mContext)
                    .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                    .error(R.drawable.user_image)
                    .placeholder(R.drawable.user_image)
                    .into(profile_img);
        }*/

        /* int progress = 40;
        if (preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
            LogUtil.e(TAG, "initControl: bank");
            progress += 25;
        }
        if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve")) {
            LogUtil.e(TAG, "initControl: pan");
            progress += 25;
        }
        if (!preferences.getUserModel().getUserImg().toLowerCase().equals("")) {
            LogUtil.e(TAG, "initControl: img");
            progress += 10;
        }
       profile_progress.setProgress(progress);
        LogUtil.e(TAG, "initControl: " + progress);
        completed_your_profile_per.setText("Your profile is (" + progress + "%) completed");*/

        // team_name.setEnabled(false);
        mobile_number.setEnabled(false);
        if (TextUtils.isEmpty(dob.getText().toString())){
            dob.setEnabled(true);
        }else {
            dob.setEnabled(false);
        }

        ArrayList<String> genderList=new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter=new ArrayAdapter<>(this,R.layout.spinner_text,genderList);
        spinGender.setAdapter(genderAdapter);
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedGender=genderList.get(pos);
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
                updateLabel();
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        date.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);

        setData();
        //KYC  -  check :)
      /*  if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
            profile_kyc.setVisibility(View.GONE);
        }*/
    }

    private void setData(){

        UserModel userModel=preferences.getUserModel();

        name_as_aadhar.setText(userModel.getDisplayName());
        team_name.setText(userModel.getUserTeamName());
        mobile_number.setText(userModel.getPhoneNo());
        email.setText(userModel.getEmailId());
        dob.setText(userModel.getDob());

        profile_name.setText(userModel.getDisplayName());
        profile_tag.setText("@" + userModel.getUserTeamName());

        team_name.setEnabled(userModel.getTeam_name_change_allow().equalsIgnoreCase("yes"));

        selectedState=userModel.getStateId();

        switch (userModel.getGender()) {
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

        int age=18;
        if (!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equals("0000-00-00")){
            age=CustomUtil.getAge(userModel.getDob());
        }
        LogUtil.d("agereap",age+" ");
        if (TextUtils.isEmpty(userModel.getUserImg())) {
            if (userModel.getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    profile_img.setImageResource(R.drawable.male_18_below);
                }else if (age>25 && age<=40){
                    profile_img.setImageResource(R.drawable.male_25_above);
                }else {
                    profile_img.setImageResource(R.drawable.male_40_above);
                }
            }else {
                if (age>=18 && age <= 25){
                    profile_img.setImageResource(R.drawable.female_18_below);
                }else if (age>25 && age<=40){
                    profile_img.setImageResource(R.drawable.female_25_above);
                }else {
                    profile_img.setImageResource(R.drawable.female_40_above);
                }
            }
        }
        else {
            //LogUtil.e(TAG, "onCreate: " + ApiManager.PROFILE_IMG + userModel.getUserImg());
            if (userModel.getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_18_below);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.male_18_below)
                            .placeholder(R.drawable.male_18_below)
                            .into(profile_img);*/
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_25_above);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.male_25_above)
                            .placeholder(R.drawable.male_25_above)
                            .into(profile_img);*/
                }else {
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.male_40_above);
                    /*Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.male_40_above)
                            .placeholder(R.drawable.male_40_above)
                            .into(profile_img);*/
                }
            }else {
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_18_below);
                    /*Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.female_18_below)
                            .placeholder(R.drawable.female_18_below)
                            .into(profile_img);*/
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_25_above);
              /*      Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.female_25_above)
                            .placeholder(R.drawable.female_25_above)
                            .into(profile_img);*/
                }else {
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.female_40_above);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + userModel.getUserImg())
                            .error(R.drawable.female_40_above)
                            .placeholder(R.drawable.female_40_above)
                            .into(profile_img);*/
                }
            }

        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

}