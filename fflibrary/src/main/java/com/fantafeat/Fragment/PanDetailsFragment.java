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
import android.text.InputFilter;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.fantafeat.Activity.FullImageActivity;
import com.fantafeat.Activity.ImagePickerActivity;
import com.fantafeat.BuildConfig;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class PanDetailsFragment extends BaseFragment {

    //Fragment fragment;
    ImageView pan_photo;
    EditText pan_name, pan_number;
    TextView pan_dob,pan_upload_text,panMsg,txtSubTitle;
    RelativeLayout pan_upload;
    Button pan_confirm;
    ImageView pan_eye;
    //LinearLayout under_review_pan,rejected,approve;
    private int PICK_IMAGE_REQUEST = 1;
    private int RESULT_CROP = 2;
    File user_image_file;
    Cursor cursor;
    String reSizeImagePath = "/Android/data/"+ BuildConfig.APPLICATION_ID +"/cache/";
    Calendar myCalendar;
    DatePickerDialog date;
    Spinner pan_state;
    String[] state_id;
    String stateID;
    Uri resultUri;
    String result_utl_to_pass="";
    boolean isAdded=true;
    private static final int REQUEST_IMAGE = 300;
   // private Activity activity;

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

    public PanDetailsFragment() {//Fragment fragment
        //super();
        //this.fragment = fragment;
      //  this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pan_details, container, false);
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

        stateID = "0";
        getStateData();
        pan_photo = view.findViewById(R.id.pan_photo);
        pan_name = view.findViewById(R.id.pan_name);
        pan_name.setLongClickable(false);
        pan_number = view.findViewById(R.id.pan_number);
        pan_number.setLongClickable(false);
        pan_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10), new InputFilter.AllCaps(),ConstantUtil.EMOJI_FILTER });
        pan_dob = view.findViewById(R.id.pan_dob);
        pan_state = view.findViewById(R.id.pan_state);
        pan_upload = view.findViewById(R.id.pan_upload);
        pan_confirm = view.findViewById(R.id.pan_confirm);
        pan_eye = view.findViewById(R.id.pan_eye);
       // under_review_pan = view.findViewById(R.id.under_review_pan);
        //rejected = view.findViewById(R.id.rejected);
        //approve = view.findViewById(R.id.approve);
        pan_upload_text = view.findViewById(R.id.pan_upload_text);
        panMsg = view.findViewById(R.id.panMsg);
        txtSubTitle = view.findViewById(R.id.txtSubTitle);

        if (preferences.getUserModel()!=null){
            if(preferences.getUserModel().getPanStatus().toLowerCase().equals("pending")) {

                pan_eye.setVisibility(View.INVISIBLE);
               // under_review_pan.setVisibility(View.GONE);

                pan_upload.setBackgroundResource(R.drawable.btn_primary);
                txtSubTitle.setText("Upload and verify your PAN details");
                panMsg.setText("PAN Details Pending");
                panMsg.setTextColor(getResources().getColor(R.color.textPrimary));
                txtSubTitle.setTextColor(getResources().getColor(R.color.textPrimary));

            }
            else if(preferences.getUserModel().getPanStatus().toLowerCase().equals("inreview")){
                pan_upload_text.setText("Uploaded");
                //under_review_pan.setVisibility(View.VISIBLE);

                txtSubTitle.setText("Your KYC documents are Under Review");
                panMsg.setText("Your PAN details are under review");
                panMsg.setTextColor(getResources().getColor(R.color.orange));
                txtSubTitle.setTextColor(getResources().getColor(R.color.orange));
                pan_upload.setBackgroundResource(R.drawable.btn_orange);

                pan_confirm.setText("Under Review");
                pan_name.setText(preferences.getUserModel().getPanName());
                pan_number.setText(preferences.getUserModel().getPanNo());
                pan_dob.setText(preferences.getUserModel().getDob());
                pan_confirm.setEnabled(false);
                pan_name.setEnabled(false);
                pan_number.setEnabled(false);
                pan_dob.setEnabled(false);
                pan_state.setEnabled(false);
                pan_eye.setVisibility(View.VISIBLE);
                pan_upload.setEnabled(false);
                result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getPanImage();

            } else if(preferences.getUserModel().getPanStatus().toLowerCase().equals("reject")){
                //rejected.setVisibility(View.VISIBLE);
                pan_confirm.setText("Re-Upload PAN Card");
                pan_eye.setVisibility(View.VISIBLE);

                txtSubTitle.setText("Your PAN Details are rejected");
                panMsg.setText(preferences.getUserModel().getPanMsg());
                panMsg.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtSubTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                pan_upload.setBackgroundResource(R.drawable.btn_primary);

                pan_name.setText(preferences.getUserModel().getPanName());
                pan_number.setText(preferences.getUserModel().getPanNo());
                pan_dob.setText(preferences.getUserModel().getDob());
                pan_confirm.setEnabled(true);
                pan_name.setEnabled(true);
                pan_number.setEnabled(true);
                pan_dob.setEnabled(true);
                pan_state.setEnabled(true);
                pan_upload.setEnabled(true);
                result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getPanImage();

            }else if(preferences.getUserModel().getPanStatus().toLowerCase().equals("approve")){
                //approve.setVisibility(View.VISIBLE);
                pan_confirm.setText("PAN Verified");
                pan_upload_text.setText("Uploaded");
                pan_upload.setBackgroundResource(R.drawable.btn_green);

                txtSubTitle.setText("Your PAN details are approved.");
                panMsg.setText("Your KYC documents are approved.");
                panMsg.setTextColor(getResources().getColor(R.color.green_pure));
                txtSubTitle.setTextColor(getResources().getColor(R.color.green_pure));

                pan_name.setText(preferences.getUserModel().getPanName());
                pan_number.setText(preferences.getUserModel().getPanNo());
                pan_dob.setText(preferences.getUserModel().getDob());
                pan_upload.setEnabled(false);
                pan_confirm.setEnabled(false);
                pan_name.setEnabled(false);
                pan_number.setEnabled(false);
                pan_dob.setEnabled(false);
                pan_state.setEnabled(false);
                result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getPanImage();
            }
        }


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

    }

    private void getStateData() {

        HttpRestClient.postJSON(mContext, true, ApiManager.STATE_LIST, new JSONObject(), new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: " + responseBody);
                if (responseBody.optBoolean("status")) {
                    try {
                        JSONArray jsonArray = responseBody.getJSONArray("data");
                        int len = jsonArray.length();
                        String[] stateItems = new String[len + 1];
                        state_id = new String[len + 1];
                        if (len > 0) {
                            stateItems[0] = "Select State";
                            state_id[0] = "0";
                            int selected = 0;
                            for (int i = 1; i < len + 1; i++) {
                                JSONObject jo = jsonArray.getJSONObject(i - 1);
                                stateItems[i] = jo.optString("name");
                                state_id[i] = jo.optString("id");
                                if (preferences.getUserModel().getStateId() != null &&
                                        preferences.getUserModel().getStateId().equals(jo.optString("id"))) {
                                    LogUtil.e(TAG, "onSuccessResult: " + i);
                                    selected = i;
                                }
                            }
                            ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, stateItems);
                            pan_state.setAdapter(stateAdapter);
                            if (selected != 0) {
                                pan_state.setSelection(selected);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        pan_dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void initClick() {

        pan_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e(TAG, "onClick: " );
                if (MyApp.getClickStatus()) {
                    hideKeyboard((Activity)mContext);
                    startActivity(new Intent(mContext, FullImageActivity.class)
                            .putExtra(ConstantUtil.FULL_IMAGE_PATH,result_utl_to_pass));
                    /*new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new FullImageFragment(result_utl_to_pass),
                            ((HomeActivity) mContext).fragmentTag(21),
                            FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);*/
                }
            }
        });

        pan_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateID = state_id[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pan_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.show();
            }
        });

        pan_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (isValid()) {
                        UpdatePanDetails();
                    }
                }
            }
        });

        pan_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add permission code
                /*String[] p;
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
                                chooseImage();
                                /*ImagePicker.Companion.with((Activity) mContext)
                                        .crop()
                                        //.compress(1024)
                                        .maxResultSize(900,900)
                                        .start();*/
                               /* CropImage.activity()
                                        .setAspectRatio(1, 1)
                                      //  .setGuidelines(CropImageView.Guidelines.ON)
                                       // .setMultiTouchEnabled(true)
                                        .start(mContext, PanDetailsFragment.this);*/
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

    }

    private void UpdatePanDetails() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("pan_no", getEditText(pan_number).toUpperCase());
            jsonObject.put("pan_name", getEditText(pan_name).toUpperCase());
            jsonObject.put("state_id", stateID);
            jsonObject.put("pan_status", "inreview");
            jsonObject.put("dob", pan_dob.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    UserModel userModel = preferences.getUserModel();
                    userModel.setPanNo(getEditText(pan_number).toUpperCase());
                    userModel.setPanName(getEditText(pan_name).toUpperCase());
                    userModel.setStateId(stateID);
                    userModel.setDob(pan_dob.getText().toString());
                    userModel.setPanStatus("inreview");
                    preferences.setUserModel(userModel);

                    pan_upload_text.setText("Uploaded");
                    //under_review_pan.setVisibility(View.VISIBLE);

                    txtSubTitle.setText("Your KYC documents are Under Review");
                    panMsg.setText("Your PAN details are under review");
                    panMsg.setTextColor(getResources().getColor(R.color.orange));
                    txtSubTitle.setTextColor(getResources().getColor(R.color.orange));
                    pan_upload.setBackgroundResource(R.drawable.btn_orange);

                    pan_confirm.setText("Under Review");
                    pan_name.setText(preferences.getUserModel().getPanName());
                    pan_number.setText(preferences.getUserModel().getPanNo());
                    pan_dob.setText(preferences.getUserModel().getDob());
                    pan_confirm.setEnabled(false);
                    pan_name.setEnabled(false);
                    pan_number.setEnabled(false);
                    pan_dob.setEnabled(false);
                    pan_state.setEnabled(false);
                    pan_eye.setVisibility(View.VISIBLE);
                    pan_upload.setEnabled(false);
                    result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getPanImage();


                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private void sendImage() {
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id", preferences.getUserModel().getId());

        LogUtil.e(TAG, "sendImage: "+params.toString() );
        HttpRestClient.postDataFile(mContext, true, ApiManager.UPLOAD_PAN_IMAGE, params, user_image_file, "pan_image", new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                if(responseBody.optBoolean("status")){

                    UserModel userModel = preferences.getUserModel();
                    userModel.setPanImage(responseBody.optString("msg"));
                    result_utl_to_pass = ApiManager.DOCUMENTS + responseBody.optString("msg");
                    preferences.setUserModel(userModel);

                    pan_eye.setVisibility(View.VISIBLE);
                    CustomUtil.showTopSneakSuccess(mContext,"Image Successfully Uploaded.");

                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e(TAG, "onFailureResult: " );
            }
        });
    }

    private boolean isValid() {
        if(user_image_file==null){
            CustomUtil.showTopSneakError(mContext, "Please upload image");
            return false;
        }
        else if (getEditText(pan_name).equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please enter PAN name");
            return false;
        }
        else if (getEditText(pan_name).length()<3) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid PAN name");
            return false;
        }
        else if (getEditText(pan_name).length()>54) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid PAN name");
            return false;
        }
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(getEditText(pan_number));
        if (!matcher.matches()) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid PAN Number");
            return false;
        }
        if (pan_dob.getText().toString().trim().equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please select DOB");
            return false;
        }
        if (stateID.equals("0")) {
            CustomUtil.showTopSneakError(mContext, "Please select state");
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

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 2);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 900);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);

       // ((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
        imageLauncher.launch(intent);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
       // intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 2);
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

                            result_utl_to_pass=uri.toString();

                            pan_eye.setVisibility(View.VISIBLE);

                            sendImage();

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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isAdded=!hidden;
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    user_image_file= FileUtil.from(mContext,uri);
                    pan_eye.setVisibility(View.VISIBLE);

                    if (isAdded() && isVisible() && getUserVisibleHint()){
                        sendImage();
                    }

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

            pan_eye.setVisibility(View.VISIBLE);
            //pan_photo.setImageBitmap(scaledBitmap);

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