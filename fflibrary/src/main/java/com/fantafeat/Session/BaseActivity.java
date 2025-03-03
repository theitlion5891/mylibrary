package com.fantafeat.Session;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fantafeat.R;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.MCrypt;
import com.fantafeat.util.PrefConstant;
import com.google.gson.Gson;

public abstract class BaseActivity extends AppCompatActivity {

   public String TAG = BaseActivity.this.getClass().getSimpleName();

   public Context mContext;
   public AlertDialog.Builder builder;
   public Gson gson;
   //public InternetConnection internetConnection;
   public MyPreferences preferences;
   public MCrypt mCrypt;

   public abstract void initClick();

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      mContext = BaseActivity.this;
      preferences = MyApp.getMyPreferences();
      mCrypt = new MCrypt();
      gson = new Gson();

      MyApp.imageBase=preferences.getPrefString(PrefConstant.IMAGE_BASE);
   }

   protected void setStatusBarDark() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         Window window = getWindow();
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.setStatusBarColor(getResources().getColor(R.color.blackSecondary));
      }
   }

   protected void setStatusBarRed() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         Window window = getWindow();
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
      }
   }

   protected void setStatusBarTransparent() {
      Window window=getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
   }

   public static boolean isValidEmail(CharSequence target) {
      return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
   }

   public void hideKeyboard(Activity activity) {
      InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      //Find the currently focused view, so we can grab the correct window token from it.
      View view = activity.getCurrentFocus();
      //If no view currently has focus, create a new one, just so we can grab a window token from it
      if (view == null) {
         view = new View(activity);
      }
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

   }

   public void hideKeyboard(Activity activity,View view) {
      InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      //Find the currently focused view, so we can grab the correct window token from it.
      //View view = activity.getCurrentFocus();
      //If no view currently has focus, create a new one, just so we can grab a window token from it
      if (view == null) {
         view = new View(activity);
      }
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

   }

   @Override
   protected void onResume() {
      super.onResume();
      ConstantUtil.isTimeOverShow=false;
   }

   public String getEditText(EditText editText){
      if(editText!= null){
         return editText.getText().toString();
      }else{
         return "";
      }
   }

}
