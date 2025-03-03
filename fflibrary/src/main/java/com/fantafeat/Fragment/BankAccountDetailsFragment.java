package com.fantafeat.Fragment;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BankAccountDetailsFragment extends BaseFragment {

  //  Fragment fragment;
    Button bank_confirm;
    RelativeLayout bank_upload;
    TextView bank_upload_text,bankMsg,bankSubTitle;
    EditText bank_acc_number, bank_confirm_acc_num, bank_ifsc, bank_name,bank_holder_name;
    private int PICK_IMAGE_REQUEST = 1;
    private int RESULT_CROP = 2;
    File user_image_file;
    ImageView bank_photo;
    Cursor cursor;
    String reSizeImagePath = "/Android/data/"+ BuildConfig.APPLICATION_ID +"/cache/";
    ImageView bank_eye;
    String result_utl_to_pass = "";
    boolean isAdded=false;
    private static final int REQUEST_IMAGE = 200;
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
    public BankAccountDetailsFragment() {//Fragment fragment

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_account_details, container, false);
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
        bank_confirm = view.findViewById(R.id.bank_confirm);
        bank_upload = view.findViewById(R.id.bank_upload);
        bank_acc_number = view.findViewById(R.id.bank_acc_number);
        bank_acc_number.setLongClickable(false);
        bank_acc_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18),ConstantUtil.EMOJI_FILTER });
        bank_confirm_acc_num = view.findViewById(R.id.bank_confirm_acc_num);
        bank_confirm_acc_num.setLongClickable(false);
        bank_confirm_acc_num.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18), ConstantUtil.EMOJI_FILTER });
        bank_ifsc = view.findViewById(R.id.bank_ifsc);
        bank_ifsc.setLongClickable(false);
        bank_ifsc.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11),new InputFilter.AllCaps(), ConstantUtil.EMOJI_FILTER });
        bank_name = view.findViewById(R.id.bank_name);
        bank_name.setLongClickable(false);
        bank_photo = view.findViewById(R.id.bank_photo);
        bank_eye = view.findViewById(R.id.bank_eye);
        bank_upload_text = view.findViewById(R.id.bank_upload_text);
        bank_holder_name = view.findViewById(R.id.bank_holder_name);
        bank_holder_name.setLongClickable(false);
        bankMsg = view.findViewById(R.id.bankMsg);
        bankSubTitle = view.findViewById(R.id.bankSubTitle);

        if (preferences.getUserModel().getBankAccStatus().toLowerCase().equals("pending")) {
            bank_eye.setVisibility(View.INVISIBLE);

            bankMsg.setText("Bank Details Pending");
            bankMsg.setTextColor(getResources().getColor(R.color.textPrimary));
            bankSubTitle.setTextColor(getResources().getColor(R.color.textPrimary));
            bankSubTitle.setText("Upload and verify your Bank details");
            bank_upload.setBackgroundResource(R.drawable.btn_primary);

        }
        else if (preferences.getUserModel().getBankAccStatus().toLowerCase().equals("inreview")) {

            bank_confirm.setText("Under Review");
            bank_upload_text.setText("Uploaded");

            bankMsg.setText("Your Bank details are Under Review");
            bankMsg.setTextColor(getResources().getColor(R.color.orange));
            bankSubTitle.setTextColor(getResources().getColor(R.color.orange));
            bankSubTitle.setText("Your Bank documents are Under Review");
            bank_upload.setBackgroundResource(R.drawable.btn_orange);

            bank_eye.setVisibility(View.VISIBLE);
            bank_acc_number.setText(preferences.getUserModel().getBankAccNo());
            bank_confirm_acc_num.setText(preferences.getUserModel().getBankAccNo());
            bank_ifsc.setText(preferences.getUserModel().getBankIfscNo());
            bank_name.setText(preferences.getUserModel().getBankName());
            bank_holder_name.setText(preferences.getUserModel().getBank_acc_holder_name());
            bank_confirm.setEnabled(false);
            bank_upload.setEnabled(false);
            bank_acc_number.setEnabled(false);
            bank_confirm_acc_num.setEnabled(false);
            bank_ifsc.setEnabled(false);
            bank_name.setEnabled(false);
            bank_holder_name.setEnabled(false);
            result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getBankAccImg();

        }
        else if (preferences.getUserModel().getBankAccStatus().toLowerCase().equals("reject")) {

            bank_confirm.setText("Re-Upload BANK Proof");
            bank_eye.setVisibility(View.VISIBLE);

            bankMsg.setText(preferences.getUserModel().getBankMsg());
            bankMsg.setTextColor(getResources().getColor(R.color.colorPrimary));
            bankSubTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            bankSubTitle.setText("Your KYC is rejected");
            bank_upload.setBackgroundResource(R.drawable.btn_primary);

            bank_acc_number.setText(preferences.getUserModel().getBankAccNo());
            bank_confirm_acc_num.setText(preferences.getUserModel().getBankAccNo());
            bank_ifsc.setText(preferences.getUserModel().getBankIfscNo());
            bank_name.setText(preferences.getUserModel().getBankName());
            bank_holder_name.setText(preferences.getUserModel().getBank_acc_holder_name());
            bank_confirm.setEnabled(true);
            bank_upload.setEnabled(true);
            bank_acc_number.setEnabled(true);
            bank_confirm_acc_num.setEnabled(true);
            bank_ifsc.setEnabled(true);
            bank_name.setEnabled(true);
            bank_holder_name.setEnabled(true);
            result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getBankAccImg();

        }
        else if (preferences.getUserModel().getBankAccStatus().toLowerCase().equals("approve")) {

            bank_confirm.setText("BANK Verified");
            bank_upload_text.setText("Uploaded");

            bankMsg.setText("Your KYC documents are approved.");
            bankMsg.setTextColor(getResources().getColor(R.color.green_pure));
            bankSubTitle.setText("Your Bank details are approved.");
            bankSubTitle.setTextColor(getResources().getColor(R.color.green_pure));
            bank_upload.setBackgroundResource(R.drawable.btn_green);

            bank_acc_number.setText(preferences.getUserModel().getBankAccNo());
            bank_confirm_acc_num.setText(preferences.getUserModel().getBankAccNo());
            bank_ifsc.setText(preferences.getUserModel().getBankIfscNo());
            bank_name.setText(preferences.getUserModel().getBankName());
            bank_holder_name.setText(preferences.getUserModel().getBank_acc_holder_name());
            bank_confirm.setEnabled(false);
            bank_upload.setEnabled(false);
            bank_acc_number.setEnabled(false);
            bank_confirm_acc_num.setEnabled(false);
            bank_ifsc.setEnabled(false);
            bank_name.setEnabled(false);
            bank_holder_name.setEnabled(false);
            result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getBankAccImg();

        }

    }

    @Override
    public void initClick() {

        bank_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e(TAG, "onClick: ");
                if (MyApp.getClickStatus()) {
                    hideKeyboard((Activity) mContext);
                    startActivity(new Intent(mContext, FullImageActivity.class)
                        .putExtra(ConstantUtil.FULL_IMAGE_PATH,result_utl_to_pass));
                   /* new FragmentUtil().addFragment((FragmentActivity) mContext,
                            R.id.home_fragment_container,
                            new FullImageFragment(result_utl_to_pass),
                            ((HomeActivity) mContext).fragmentTag(21),
                            FragmentUtil.ANIMATION_TYPE.CUSTOM);*/
                }
            }
        });

        bank_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApp.getClickStatus()) {
                    if (isValidate()) {
                        submitData();
                    }
                }
            }
        });
        bank_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add permission code
                Dexter.withActivity((Activity) mContext)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    chooseImage();
                                   /* ImagePicker.Companion.with((Activity) mContext)
                                            .crop()
                                            //.compress(1024)
                                            .maxResultSize(900,900)
                                            .start();*/
                                    /*CropImage.activity()
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setMultiTouchEnabled(true)
                                            .start(mContext, BankAccountDetailsFragment.this);*/
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

    private void submitData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("bank_ifsc_no", getEditText(bank_ifsc).toUpperCase());
            jsonObject.put("bank_acc_no", getEditText(bank_acc_number));
            jsonObject.put("bank_name", getEditText(bank_name).toUpperCase());
            jsonObject.put("bank_acc_holder_name", getEditText(bank_holder_name).toUpperCase());
            jsonObject.put("bank_acc_status", "inreview");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.UPDATE_USER_DETAILS, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    UserModel userModel = preferences.getUserModel();
                    userModel.setBankIfscNo(getEditText(bank_ifsc).toUpperCase());
                    userModel.setBankAccNo(getEditText(bank_acc_number));
                    userModel.setBankName(getEditText(bank_name).toUpperCase());
                    userModel.setBank_acc_holder_name(getEditText(bank_holder_name).toUpperCase());
                    userModel.setBankAccStatus("inreview");
                    preferences.setUserModel(userModel);


                    bank_confirm.setText("Under Review");
                    bank_upload_text.setText("Uploaded");

                    bankMsg.setText("Your Bank details are Under Review");
                    bankMsg.setTextColor(getResources().getColor(R.color.orange));
                    bankSubTitle.setTextColor(getResources().getColor(R.color.orange));
                    bankSubTitle.setText("Your Bank documents are Under Review");
                    bank_upload.setBackgroundResource(R.drawable.btn_orange);

                    bank_eye.setVisibility(View.VISIBLE);
                    bank_acc_number.setText(preferences.getUserModel().getBankAccNo());
                    bank_confirm_acc_num.setText(preferences.getUserModel().getBankAccNo());
                    bank_ifsc.setText(preferences.getUserModel().getBankIfscNo());
                    bank_name.setText(preferences.getUserModel().getBankName());
                    bank_holder_name.setText(preferences.getUserModel().getBank_acc_holder_name());
                    bank_confirm.setEnabled(false);
                    bank_upload.setEnabled(false);
                    bank_acc_number.setEnabled(false);
                    bank_confirm_acc_num.setEnabled(false);
                    bank_ifsc.setEnabled(false);
                    bank_name.setEnabled(false);
                    bank_holder_name.setEnabled(false);
                    result_utl_to_pass = ApiManager.DOCUMENTS + preferences.getUserModel().getBankAccImg();

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
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", preferences.getUserModel().getId());

        LogUtil.e(TAG, "sendImage: " + params.toString());
        HttpRestClient.postDataFile(mContext, true, ApiManager.UPLOAD_BANK_IMAGE, params, user_image_file, "acc_image", new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                isAdded=true;
                if (responseBody.optBoolean("status")) {
                    LogUtil.e(TAG, "onSuccessResult: " + responseBody.toString());
                    UserModel userModel = preferences.getUserModel();
                    userModel.setBankAccImg(responseBody.optString("msg"));
                    result_utl_to_pass = ApiManager.DOCUMENTS + responseBody.optString("msg");
                    preferences.setUserModel(userModel);

                    bank_eye.setVisibility(View.VISIBLE);
                    CustomUtil.showTopSneakSuccess(mContext, "Image Successfully Uploaded.");
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {
                LogUtil.e(TAG, "onFailureResult: ");
            }
        });
    }

    private boolean isValidate() {
        if(user_image_file == null){
            CustomUtil.showTopSneakError(mContext, "Please upload image");
            return false;
        }
        if (getEditText(bank_holder_name).equalsIgnoreCase("")) {
            CustomUtil.showTopSneakError(mContext, "Please enter account holder name");
            return false;
        }
        if (getEditText(bank_acc_number).equalsIgnoreCase("")) {
            CustomUtil.showTopSneakError(mContext, "Please enter account number");
            return false;
        }
        if (getEditText(bank_confirm_acc_num).equalsIgnoreCase("")) {
            CustomUtil.showTopSneakError(mContext, "Please Re-enter account number");
            return false;
        }
        if (getEditText(bank_ifsc).length() != 11) {
            CustomUtil.showTopSneakError(mContext, "Please enter valid IFSC code");
            return false;
        }
        if (getEditText(bank_name).equals("")) {
            CustomUtil.showTopSneakError(mContext, "Please enter Bank name");
            return false;
        }
        if (!getEditText(bank_acc_number).equals(getEditText(bank_confirm_acc_num))) {
            CustomUtil.showTopSneakError(mContext, "Account number mismatch");
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

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 2);

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
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        //intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 2);
        //((Activity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
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

                            bank_eye.setVisibility(View.VISIBLE);

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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    user_image_file= FileUtil.from(mContext,uri);

                    result_utl_to_pass=uri.toString();

                    bank_eye.setVisibility(View.VISIBLE);

                    if (isAdded() && isVisible() && getUserVisibleHint()){
                        isAdded=true;
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

            //bank_photo.setVisibility(View.VISIBLE);
            //bank_photo.setImageBitmap(scaledBitmap);
            bank_eye.setVisibility(View.VISIBLE);


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

