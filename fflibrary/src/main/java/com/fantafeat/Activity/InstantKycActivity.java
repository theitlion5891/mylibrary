package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantafeat.Model.UserModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CameraUtils;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.PanProcessing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstantKycActivity extends BaseActivity {

    private TextView toolbar_title,txtKycStepLbl,txtPanVerification,txtPanNumber,txtBankVerification,txtBankDetails;
    private ImageView toolbar_back,imgArrowPan,imgArrowBank;
    private CardView cardPAN,cardBank;
    private boolean is_auto_verify_pan=false,is_auto_verify_bank=false;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    private AppCompatEditText edtPanNo;
    private String pan_pattern = "(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))";
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

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.activity_instant_kyc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }


        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getKycStatusData();
    }

    private void initData() {
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);
        cardPAN = findViewById(R.id.cardPAN);
        cardBank = findViewById(R.id.cardBank);
        txtKycStepLbl = findViewById(R.id.txtKycStepLbl);
        txtPanVerification = findViewById(R.id.txtPanVerification);
        txtPanNumber = findViewById(R.id.txtPanNumber);
        txtBankVerification = findViewById(R.id.txtBankVerification);
        txtBankDetails = findViewById(R.id.txtBankDetails);
        imgArrowPan = findViewById(R.id.imgArrowPan);
        imgArrowBank = findViewById(R.id.imgArrowBank);

        toolbar_title.setText("KYC Details");

        /*isPanVerify= userModel.getPanStatus().equalsIgnoreCase("approve");
        isBankVerify= userModel.getBankAccStatus().equalsIgnoreCase("approve");*/

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        cardPAN.setOnClickListener(v -> {

            if (is_auto_verify_pan)
                showDialogSheet(true);
            else
                startActivity(new Intent(mContext, UpdateKYCActivity.class)
                        .putExtra("verify_tag","pan"));

            /*UserModel userModel=preferences.getUserModel();
            if (userModel.getPanStatus().toLowerCase().equals("approve")){
                CustomUtil.showTopSneakSuccess(mContext,"Your PAN number is verified");
            }
            else if (userModel.getPanStatus().toLowerCase().equals("inreview")){
                //CustomUtil.showTopSneakWarning(mContext,"Your PAN is in-review");
            }
            else {
                if (is_auto_verify_pan)
                    showDialogSheet(true);
                else
                    startActivity(new Intent(mContext, UpdateKYCActivity.class)
                            .putExtra("verify_tag","pan"));
            }*/
        });

        cardBank.setOnClickListener(v -> {
            UserModel userModel=preferences.getUserModel();
            if (userModel.getPanStatus().toLowerCase().equals("inreview") || userModel.getPanStatus().toLowerCase().equals("approve")){
                if (is_auto_verify_bank)
                    showDialogSheet(false);
                else
                    startActivity(new Intent(mContext, UpdateKYCActivity.class)
                            .putExtra("verify_tag","bank"));
                /*if (userModel.getBankAccStatus().toLowerCase().equals("approve")){
                    CustomUtil.showTopSneakSuccess(mContext,"Your BANK Details are verified");
                }
                else if (userModel.getBankAccStatus().toLowerCase().equals("inreview")){
                   // CustomUtil.showTopSneakWarning(mContext,"Your BANK Details are in-review");
                }
                else {
                    if (is_auto_verify_bank)
                        showDialogSheet(false);
                    else
                        startActivity(new Intent(mContext, UpdateKYCActivity.class)
                                .putExtra("verify_tag","bank"));
                }*/
            }
            else {
                CustomUtil.showTopSneakWarning(mContext,"Please verify your PAN number first");
            }
        });

        //setDefaultData();
    }

    private void showDialogSheet(boolean isPan) {
        BottomSheetDialog dialog=new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.instant_kyc_sheet);

        LinearLayout layPan=dialog.findViewById(R.id.layPan);
        LinearLayout layBank=dialog.findViewById(R.id.layBank);

        edtPanNo=dialog.findViewById(R.id.edtPanNo);
        AppCompatEditText edtBankNo=dialog.findViewById(R.id.edtBankNo);
        AppCompatEditText edtBankIfsc=dialog.findViewById(R.id.edtBankIfsc);
        TextView btnBankSubmit=dialog.findViewById(R.id.btnBankSubmit);
        TextView btnPanSubmit=dialog.findViewById(R.id.btnPanSubmit);
        ImageView imgScanPan=dialog.findViewById(R.id.imgScanPan);

        edtPanNo.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        edtBankIfsc.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        imgScanPan.setOnClickListener(v -> {
            Dexter.withActivity((Activity) mContext)
                    .withPermissions(p)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            if (report.areAllPermissionsGranted()) {
                                takePicture();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                LogUtil.e(TAG, "onPermissionsChecked: ");
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

        btnBankSubmit.setOnClickListener(v -> {
            String bankNumber=edtBankNo.getText().toString().trim();
            String ifscCode=edtBankIfsc.getText().toString().trim();
            if (TextUtils.isEmpty(bankNumber)){
                CustomUtil.showTopSneakWarning(mContext,"Enter Account Number");
            }
            else if (TextUtils.isEmpty(ifscCode)){
                CustomUtil.showTopSneakWarning(mContext,"Enter IFSC Code");
            }else {
                dialog.dismiss();
                submitBankData(bankNumber,ifscCode);
            }
        });

        btnPanSubmit.setOnClickListener(v -> {
            String panNumber=edtPanNo.getText().toString().trim();
            if (!TextUtils.isEmpty(panNumber)){
                Pattern r = Pattern.compile(pan_pattern);
                if (regex_matcher(r, panNumber)) {
                    dialog.dismiss();
                    submitPanData(panNumber);
                }else {
                    CustomUtil.showTopSneakWarning(mContext,"Enter Valid PAN Number");
                }
            }else {
                Toast.makeText(mContext,"Enter Pan Number",Toast.LENGTH_SHORT).show();
            }
        });

        if (isPan){
            layPan.setVisibility(View.VISIBLE);
            layBank.setVisibility(View.GONE);
        }else {
            layPan.setVisibility(View.GONE);
            layBank.setVisibility(View.VISIBLE);
        }

        dialog.show();
    }

    private void submitBankData(String bankNumber, String ifscCode) {
        try {
            JSONObject param=new JSONObject();
            param.put("account_number",bankNumber);
            param.put("ifsc",ifscCode);
            param.put("user_id",preferences.getUserModel().getId());

            LogUtil.e("resp","submitPanData param : "+param);

            HttpRestClient.postJSONNormal(mContext, true, ApiManager.autoVerifyDecentroBankAccount, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("resp","submitPanData: "+responseBody);
                    if (responseBody.optBoolean("status")){
                        CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));

                        //isBankVerify=true;
                    }else {
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }

                    getUserDetails();
                }

                @Override
                public void onFailureResult(String responseBody, int code) {}
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void submitPanData(String panNumber) {
        try {
            JSONObject param=new JSONObject();
            param.put("pan_no",panNumber);
            param.put("user_id",preferences.getUserModel().getId());

            LogUtil.e("resp","submitPanData param : "+param);

            HttpRestClient.postJSONNormal(mContext, true, ApiManager.autoVerifyDecentroPanCard, param, new GetApiResult() {
                @Override
                public void onSuccessResult(JSONObject responseBody, int code) {
                    LogUtil.e("resp","submitPanData: "+responseBody);
                    if (responseBody.optBoolean("status")){
                        CustomUtil.showTopSneakSuccess(mContext,responseBody.optString("msg"));

                    }else {
                        CustomUtil.showTopSneakError(mContext,responseBody.optString("msg"));
                    }
                    getUserDetails();
                }

                @Override
                public void onFailureResult(String responseBody, int code) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUserDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("token_no", preferences.getUserModel().getTokenNo());
            //Log.e(TAG, "getUserDetails: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.AUTHV3GetUserDetails, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "USER onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    UserModel userModel = gson.fromJson(data.toString(), UserModel.class);
                    preferences.setUserModel(userModel);
                }

                getKycStatusData();
            }
            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void getKycStatusData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRestClient.postJSON(mContext, true, ApiManager.getUserKycSetting, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "getKycStatusData: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONObject data = responseBody.optJSONObject("data");
                    String bank=data.optString("is_auto_verify_bank");
                    String pan=data.optString("is_auto_verify_pan");

                    is_auto_verify_pan = pan.equalsIgnoreCase("yes");
                    is_auto_verify_bank = bank.equalsIgnoreCase("yes");

                    setDefaultData();
                }
            }
            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = CameraUtils.createImageFile(this);
                mCurrentPhotoPath=photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this,"Error creating file",Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext, getApplication().getPackageName()+ ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            mImageBitmap = CameraUtils.getBitmap(mCurrentPhotoPath);
            detectText();
        }
    }

    public void detectText() {

        if(mImageBitmap!=null){

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mImageBitmap);

            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

            detector.detectInImage(image).addOnSuccessListener(firebaseVisionText -> {
                HashMap<String,String> dataMap=new PanProcessing().processText(firebaseVisionText,getApplicationContext());
                if (edtPanNo!=null && dataMap!=null) {
                    Pattern r = Pattern.compile(pan_pattern);
                    //String panNumber=dataMap.get("pan");
                    if (dataMap.containsKey("pan") && !TextUtils.isEmpty(dataMap.get("pan")) && regex_matcher(r, dataMap.get("pan"))) {
                        edtPanNo.setText(dataMap.get("pan"));
                    }
                    else {
                        Toast.makeText(mContext,"Error to detect PAN number, Please enter manually",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> Toast.makeText(mContext,"Error to detect PAN number, Please enter manually",Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(this,"Please Take a Picture first",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean regex_matcher(Pattern pattern, String string) {
        Matcher m = pattern.matcher(string);
        return m.find() && (m.group(0) != null);
    }

    private void setDefaultData(){
        UserModel userModel=preferences.getUserModel();

        if(userModel.getPanStatus().toLowerCase().equals("pending")) {
            txtPanVerification.setText(" "+userModel.getPanStatus());
            txtPanNumber.setText("Mandatory for withdraw");
            txtPanNumber.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            txtPanVerification.setTextColor(getResources().getColor(R.color.green_pure));
            imgArrowPan.setVisibility(View.VISIBLE);
        }
        else if(userModel.getPanStatus().toLowerCase().equals("inreview")){
            txtPanVerification.setText(" in-review");
            txtPanNumber.setText("Mandatory for withdraw");
            txtPanNumber.setText(userModel.getPanNo()+"\n"+userModel.getPanName());
            txtPanNumber.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            txtPanVerification.setTextColor(getResources().getColor(R.color.green_pure));
            imgArrowPan.setVisibility(View.GONE);
        }
        else if(userModel.getPanStatus().toLowerCase().equals("reject")){
            txtPanVerification.setText(" rejected");
            txtPanVerification.setTextColor(getResources().getColor(R.color.red));
            imgArrowPan.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(userModel.getPanMsg())) {
                txtPanNumber.setText(userModel.getPanMsg());
                txtPanNumber.setTextColor(getResources().getColor(R.color.red));
            }
            else {
                txtPanNumber.setText("Mandatory for withdraw");
                txtPanNumber.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            }
        }
        else if(userModel.getPanStatus().toLowerCase().equals("approve")){
            txtPanVerification.setText(" VERIFIED");
            txtPanVerification.setTextColor(getResources().getColor(R.color.green_pure));
            txtPanNumber.setText(userModel.getPanNo()+"\n"+userModel.getPanName());
            txtPanNumber.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            imgArrowPan.setVisibility(View.GONE);
        }



        if(userModel.getBankAccStatus().toLowerCase().equals("pending")) {
            txtBankVerification.setText(" "+userModel.getBankAccStatus());
            txtBankDetails.setText("Mandatory for withdraw");
            txtBankDetails.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            txtBankVerification.setTextColor(getResources().getColor(R.color.green_pure));
            imgArrowBank.setVisibility(View.VISIBLE);
        }
        else if(userModel.getBankAccStatus().toLowerCase().equals("inreview")){
            txtBankVerification.setText(" in-review");
            txtBankDetails.setText("Mandatory for withdraw");
            txtBankDetails.setText(userModel.getBankAccNo()+"\n"+userModel.getBankName());
            txtBankDetails.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            txtBankVerification.setTextColor(getResources().getColor(R.color.green_pure));
            imgArrowBank.setVisibility(View.GONE);
        }
        else if(userModel.getBankAccStatus().toLowerCase().equals("reject")){
            txtBankVerification.setText(" rejected");
            txtBankVerification.setTextColor(getResources().getColor(R.color.red));
            imgArrowBank.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(userModel.getBankMsg())) {
                txtBankDetails.setText(userModel.getBankMsg());
                txtBankDetails.setTextColor(getResources().getColor(R.color.red));
            }else {
                txtBankDetails.setText("Mandatory for withdraw");
                txtBankDetails.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            }
        }
        else if(userModel.getBankAccStatus().toLowerCase().equals("approve")){
            txtBankVerification.setText(" VERIFIED");
            txtBankVerification.setTextColor(getResources().getColor(R.color.green_pure));
            txtBankDetails.setText(userModel.getBankAccNo()+"\n"+userModel.getBankName());
            txtBankDetails.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
            imgArrowBank.setVisibility(View.GONE);
        }

        txtKycStepLbl.setVisibility(View.VISIBLE);

        if(userModel.getPanStatus().equalsIgnoreCase("approve") &&
                userModel.getBankAccStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("Your KYC process is successfully verified");
        }
        else if(userModel.getPanStatus().equalsIgnoreCase("pending") &&
                userModel.getBankAccStatus().equalsIgnoreCase("pending")) {
            txtKycStepLbl.setText("1/2 | Steps remaining to complete KYC Process");
        }
        else if(userModel.getPanStatus().equalsIgnoreCase("inreview") &&
                userModel.getBankAccStatus().equalsIgnoreCase("inreview")) {
            txtKycStepLbl.setText("Your KYC documents are under review");
        }
        else if(!userModel.getPanStatus().equalsIgnoreCase("approve") &&
                userModel.getBankAccStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("2/2 | Steps remaining to complete KYC Process");
        }
        else if(userModel.getPanStatus().equalsIgnoreCase("approve") &&
                !userModel.getBankAccStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("2/2 | Steps remaining to complete KYC Process");
        }else {
            txtKycStepLbl.setVisibility(View.GONE);
        }

        /*if(userModel.getPanStatus().equalsIgnoreCase("approve") && userModel.getBankAccStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("Your KYC process is successfully verified");
            txtPanVerification.setText(" VERIFIED");
            txtBankVerification.setText(" VERIFIED");
            txtPanNumber.setText(userModel.getPanNo()+"\n"+userModel.getPanName());
            txtBankDetails.setText(userModel.getBankAccNo()+"\n"+userModel.getBankName());
        }
        else if(userModel.getPanStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("2/2 | Steps remaining to complete KYC Process");
            txtPanVerification.setText(" VERIFIED");
            txtBankVerification.setText(" INSTANT VERIFICATION");
            txtPanNumber.setText(userModel.getPanNo()+"\n"+userModel.getPanName());
            txtBankDetails.setText("Mandatory for withdraw");
        }
        else if(userModel.getBankAccStatus().equalsIgnoreCase("approve")) {
            txtKycStepLbl.setText("2/2 | Steps remaining to complete KYC Process");
            txtPanVerification.setText(" INSTANT VERIFICATION");
            txtBankVerification.setText(" VERIFIED");
            txtPanNumber.setText("Mandatory for withdraw");
            txtBankDetails.setText(userModel.getBankAccNo()+"\n"+userModel.getBankName());
        }
        else {
            txtKycStepLbl.setText("1/2 | Steps remaining to complete KYC Process");
            txtPanVerification.setText(" INSTANT VERIFICATION");
            txtBankVerification.setText(" INSTANT VERIFICATION");
            txtPanNumber.setText("Mandatory for withdraw");
            txtBankDetails.setText("Mandatory for withdraw");
        }*/

    }

    @Override
    public void initClick() {

    }

}