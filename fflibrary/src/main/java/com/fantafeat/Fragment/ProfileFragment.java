package com.fantafeat.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.fantafeat.Activity.FullImageActivity;
import com.fantafeat.Activity.HomeActivity;
import com.fantafeat.Activity.ImagePickerActivity;
import com.fantafeat.Model.StateModal;
import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    EditText name_as_aadhar,  mobile_number, email,team_name, dob;//
    Button confirm;
    TextView profile_name, profile_tag;
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
    private static final int REQUEST_IMAGE = 100;

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

    /*@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {

            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
    };*/

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStatusBarDark();
        HideBottomNavigationBar();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initFragment(view);

        selectedState=preferences.getUserModel().getStateId();

        if (preferences.getStateList()==null){
            getStateData();
        }else {
            cityName=new ArrayList<String>();
            cityName.add("Select state");
            cityId=new ArrayList<String>();
            cityId.add("0");
            ArrayList<StateModal> stateModals=preferences.getStateList();
            int pos=0;
            for (int i=0;i<stateModals.size();i++){
                StateModal modal=stateModals.get(i);
                String name =modal.getName();
                String id = modal.getId();
                if (selectedState.equals(id)){
                    pos=i+1;
                }
                cityId.add(id);
                cityName.add(name);
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
        }

        initClic();
        initToolBar(view, "Profile", true);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyboard((Activity) mContext);
        ShowBottomNavigationBar();
    }

    private void initClic() {

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.show();
                /*if (TextUtils.isEmpty(dob.getText().toString())){
                    date.show();
                }*/
            }
        });

      /*  drawer_image.setOnClickListener(view -> OpenNavDrawer());*/

        profile_img.setOnClickListener(view -> {
            LogUtil.e(TAG, "onClick: ");
            if (MyApp.getClickStatus()) {
                hideKeyboard((Activity) mContext);
                startActivity(new Intent(mContext, FullImageActivity.class)
                        .putExtra(ConstantUtil.FULL_IMAGE_PATH,ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                        .putExtra("pageFrom","profile")
                );
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String[] p;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    p = storge_permissions_33;
                } else {
                    p = storge_permissions;
                }*/
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if (TextUtils.isEmpty(preferences.getUserModel().getUserImg())){
                                        chooseImage();
                                       /* ImagePicker.Companion.with((Activity) mContext)
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

    private void showUpdateDialog() {
        String[] arr=new String[]{"Update","Remove"};
        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
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
        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
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
                            userModel.setDob(dob.getText().toString());
                            userModel.setUserImg("");
                            preferences.setUserModel(userModel);


                            HomeActivity home = (HomeActivity) getActivity();
                          /*  Glide.with(mContext)
                                    .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                                    .error(R.drawable.team_loading)
                                    .placeholder(R.drawable.team_loading)
                                    .into(home.profile_image);*/
                            home.profile_name.setText(name_as_aadhar.getText().toString());
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

    private void setData(){
        name_as_aadhar.setText(preferences.getUserModel().getDisplayName());
        team_name.setText(preferences.getUserModel().getUserTeamName());
        mobile_number.setText(preferences.getUserModel().getPhoneNo());
        email.setText(preferences.getUserModel().getEmailId());
        dob.setText(preferences.getUserModel().getDob());

        profile_name.setText(preferences.getUserModel().getDisplayName());
        profile_tag.setText("@" + preferences.getUserModel().getUserTeamName());



        team_name.setEnabled(preferences.getUserModel().getTeam_name_change_allow().equalsIgnoreCase("yes"));

        /*if (TextUtils.isEmpty(preferences.getUserModel().getEmailId())){
            email.setEnabled(true);
        }else {
            email.setEnabled(false);
        }*/

        switch (preferences.getUserModel().getGender()) {
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
        if (!TextUtils.isEmpty(preferences.getUserModel().getDob()) && !preferences.getUserModel().getDob().equals("0000-00-00")){
            age=CustomUtil.getAge(preferences.getUserModel().getDob());
        }
        LogUtil.d("agereap",age+" ");
        if (TextUtils.isEmpty(preferences.getUserModel().getUserImg())) {
            if (preferences.getUserModel().getGender().equals("Male")){
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
            LogUtil.e(TAG, "onCreate: " + ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg());
            if (preferences.getUserModel().getGender().equals("Male")){
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.male_18_below);
                    /*Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.male_18_below)
                            .placeholder(R.drawable.male_18_below)
                            .into(profile_img);*/
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.male_25_above);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.male_25_above)
                            .placeholder(R.drawable.male_25_above)
                            .into(profile_img);*/
                }else {
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.male_40_above);
                    /*Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.male_40_above)
                            .placeholder(R.drawable.male_40_above)
                            .into(profile_img);*/
                }
            }else {
                if (age>=18 && age <= 25){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.female_18_below);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.female_18_below)
                            .placeholder(R.drawable.female_18_below)
                            .into(profile_img);*/
                }else if (age>25 && age<=40){
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.female_25_above);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.female_25_above)
                            .placeholder(R.drawable.female_25_above)
                            .into(profile_img);*/
                }else {
                    CustomUtil.loadImageWithGlide(mContext,profile_img,ApiManager.PROFILE_IMG,preferences.getUserModel().getUserImg(),R.drawable.female_40_above);
                   /* Glide.with(mContext)
                            .load(ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg())
                            .error(R.drawable.female_40_above)
                            .placeholder(R.drawable.female_40_above)
                            .into(profile_img);*/
                }
            }
        }

        //getStateData();
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
                    ArrayList<StateModal> stateModals=new ArrayList<>();
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
                            StateModal stateModal = gson.fromJson(obj.toString(), StateModal.class);
                            stateModals.add(stateModal);
                        }
                        preferences.setStateList(stateModals);
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
            public void onFailureResult(String responseBody, int code) {}

        });
    }

    @Override
    public void initControl(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        name_as_aadhar = view.findViewById(R.id.name_as_aadhar);
        name_as_aadhar.setLongClickable(false);
        team_name = view.findViewById(R.id.edtTeamName);
        team_name.setLongClickable(false);
        mobile_number = view.findViewById(R.id.mobile_number);
        mobile_number.setLongClickable(false);
        toolbar_back = view.findViewById(R.id.toolbar_back);
        email = view.findViewById(R.id.email);
        email.setLongClickable(false);
        dob = view.findViewById(R.id.dob);
        confirm = view.findViewById(R.id.confirm);
        spinState = view.findViewById(R.id.spinState);
      //  spinState.setEnabled(false);
        spinGender = view.findViewById(R.id.spinGender);

        //complete_now = findViewById(R.id.complete_now);
        profile_name = view.findViewById(R.id.profile_name);
        profile_tag = view.findViewById(R.id.profile_tag);
        //drawer_image = view.findViewById(R.id.drawer_image);
        /*followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        completed_your_profile_per = findViewById(R.id.completed_your_profile_per);
        profile_progress = findViewById(R.id.profile_progress);
        share = findViewById(R.id.share);*/
        edit_btn = view.findViewById(R.id.edit_btn);
        profile_img = view.findViewById(R.id.profile_img);
       /// profile_kyc = view.findViewById(R.id.profile_kyc);
       // following_follower = view.findViewById(R.id.following_follower);

        //Log.d("modal",preferences.getUserModel().getUserImg());

       /* mobile_number.setEnabled(false);
        if (TextUtils.isEmpty(dob.getText().toString())){
            dob.setEnabled(true);
        }else {
            dob.setEnabled(false);
        }*/

        ArrayList<String> genderList=new ArrayList<String>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter<String> genderAdapter=new ArrayAdapter(mContext,R.layout.spinner_text,genderList);
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

        getUserDetails();

        //setData();
        //KYC  -  check :)
      /*  if (preferences.getUserModel().getPanStatus().toLowerCase().equals("approve") &&
                preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {
            profile_kyc.setVisibility(View.GONE);
        }*/
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 900);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);

      //  ((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
       // intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

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
                            long fileSizeInBytes = user_image_file.length();
                            long fileSizeInKB = fileSizeInBytes / 1024;
                            long fileSizeInMB = fileSizeInKB / 1024;
                            LogUtil.e("filse","fileSizeInMB: "+fileSizeInMB+"   fileSizeInKB:"+fileSizeInKB);
                            if (fileSizeInKB <= 500) {
                                //  sendImage();

                                profile_img.setImageURI(uri);
                                callApi();
                            }else {
                                user_image_file=null;
                                CustomUtil.showTopSneakWarning(mContext,"Choose Max 500 KB file size");
                            }
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


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void initClick() {

      /*  toolbar_back.setOnClickListener(view->{
            RemoveFragment();
        });

        profile_img.setOnClickListener(view -> {
            LogUtil.e(TAG, "onClick: ");
            if (MyApp.getClickStatus()) {
                hideKeyboard((Activity) mContext);
                startActivity(new Intent(mContext, FullImageActivity.class)
                        .putExtra(ConstantUtil.FULL_IMAGE_PATH,ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg()));
            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    ImagePicker.Companion.with((Activity) mContext)
                                            .crop()
                                            .start();
                                    */
        /*CropImage.activity()
                                            .setAspectRatio(1, 1)
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setMultiTouchEnabled(true)
                                            .start(mContext, ProfileFragment.this);*/
        /*
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


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (validate()) {
                        callFirstApi();
                    }
                }
            }
        });*/
    }

    private void shareNow() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareBody = "Invite your friend and earn ₹100 each (once your refer friend add ₹20 or more and play).\nJoin with invite code : " +
                preferences.getUserModel().getRefNo() +
                "\n\nDownload Link :" +ApiManager.app_download_url+
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
            jsonObject.put("state_id", selectedState);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("dob", dob.getText().toString().trim());
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
                    userModel.setEmailId(Objects.requireNonNull(email.getText().toString()));
                    userModel.setDisplayName(name_as_aadhar.getText().toString());
                    userModel.setStateId(selectedState);
                    userModel.setGender(selectedGender);
                    userModel.setDob(dob.getText().toString().trim());
                    preferences.setUserModel(userModel);*/
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));

                    getUserDetails();

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

                        //result_utl_to_pass = ApiManager.PROFILE_IMG + userModel.getUserImg();
                        result_utl_to_pass = ApiManager.PROFILE_IMG + userModel.getUserImg();

                        HomeActivity home = (HomeActivity) getActivity();
                        CustomUtil.loadImageWithGlide(mContext,home.profile_image,ApiManager.PROFILE_IMG,userModel.getUserImg(),R.drawable.team_loading);

                        home.profile_name.setText(userModel.getDisplayName());
                        home.profile_tag.setText(userModel.getUserTeamName());

                        preferences.setUserModel(userModel);
                        MyApp.getMyPreferences().setUserModel(userModel);

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

                    UserModel userModel = preferences.getUserModel();
                    userModel.setUserImg(responseBody.optString("msg"));
                    preferences.setUserModel(userModel);

                    getUserDetails();

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
        String db=dob.getText().toString().trim();
        if (getEditText(name_as_aadhar).equals("")) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Name as on Aadhar Card.");
            return false;
        }
        else if (getEditText(email).equals("")) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Email.");
            return false;
        }
        else if (!isValidEmail(getEditText(email))) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Valid Email.");
            return false;
        }
        else if (TextUtils.isEmpty(db)) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Date of Birth.");
            return false;
        }
        else if (TextUtils.isEmpty(getEditText(team_name))) {
            CustomUtil.showTopSneakWarning(mContext, "Please Enter Team Name.");
            return false;
        }
        else if (getEditText(team_name).length()>11) {
            CustomUtil.showTopSneakWarning(mContext, "Team name should be less than or equals to 11 characters.");
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    user_image_file= FileUtil.from(mContext,uri);

                    profile_img.setImageURI(uri);

                    callApi();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }*/

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(Uri.parse(imageUri));
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = 0;
        int actualWidth = 0;

        actualHeight = options.outHeight;
        actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1024.0f;
        float maxWidth = 820.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        assert scaledBitmap != null;
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            //bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);

            //pan_photo.setVisibility(View.VISIBLE);
            profile_img.setImageBitmap(scaledBitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "0000000000/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        LogUtil.e("NEW FILE PATH ", "=> " + uriSting);
        reSizeImagePath = uriSting;
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        cursor = Objects.requireNonNull(mContext).getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            return contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String path = "";
                try {
                    path = cursor.getString(idx);
                } catch (Exception e) {
                    LogUtil.e(TAG, "" + e.toString());
                }
                cursor.close();
                return path;
            } else {
                return null;
            }
        }
    }

}