package com.fantafeat.Session;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.fantafeat.util.PrefConstant;
import com.google.gson.Gson;

public abstract class BaseLoginFragment extends Fragment {

    public Context mContext;
    public static final String TAG = BaseFragment.class.getClass().getSimpleName();
    public MyPreferences preferences;
    public AlertDialog.Builder builder;
    public Gson gson;

    public abstract void initControl(View view);
    public abstract void initClick();

    protected void initFragment(View view) {
        mContext = getContext();
        assert mContext != null;
        builder = new AlertDialog.Builder(mContext);
        gson = new Gson();
        preferences = MyApp.getMyPreferences();

        view.setClickable(true);
        view.setFocusable(true);

        initControl(view);
        initClick();

        MyApp.imageBase=preferences.getPrefString(PrefConstant.IMAGE_BASE);
    }

    protected void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*protected void AddNewFragment(final Fragment frargment, final String id) {
        new FragmentUtil().addFragment(getActivity(),
                R.id.login_fragment,
                frargment,
                id,
                FragmentUtil.ANIMATION_TYPE.SLIDE_RIGHT_TO_LEFT);
    }

    public void RemoveFragment() {
        new FragmentUtil().removeFragment(getActivity(),
                R.id.login_fragment,
                this,
                FragmentUtil.ANIMATION_TYPE.SLIDE_LEFT_TO_RIGHT);
    }
*/
   // protected String fragmentTag(int pos){ return ((LoginActivity)getActivity()).fragmentTag(pos); }

}
