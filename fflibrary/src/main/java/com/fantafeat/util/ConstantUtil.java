package com.fantafeat.util;

import android.text.InputFilter;
import android.text.Spanned;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.fantafeat.Model.ContestQuantityModel;
import com.fantafeat.Model.ScoreModel;
import com.fantafeat.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConstantUtil {
    public static String HASH_TYPE = "SHA-256";
    public static int NUM_HASHED_BYTES = 9;
    public static int NUM_BASE64_CHAR = 11;
    public static int INTERNET_ERROR = 21;
    public static int NOT_FOUND = 404;
    public static int SERVER_ERROR = 501;
    public static int MAX_MEDIA_SIZE_MB = 25;

    //Glide  Image type
    public static int CENTER_CROP = 10000;
    public static int FIT_CENTER = 10000;
    public static int CENTER_INSIDE = 10000;
    public static String COMPANY_ID="101";
    public static boolean IS_POPUP_BANNER_SHOW = false;
    public static String FULL_IMAGE_PATH = "full_screen_image";
    public static String TRANS_ID = "trans_id";
    public static String AMOUNT = "amount";
    public static String WEB_TITLE = "webView_title";
    public static String WEB_URL = "webView_url";
    public static boolean is_game_test = true;
    public static boolean is_bonus_show = true;

    public static int Refresh_delay = 10000;
    public static boolean isTop=false;
    public static boolean isCustomMore=false;
    public static boolean play_store_app = false;
    public static boolean is_instant_kyc = true;
    public static final String GAME_ASSETS_NAME="/ff-android";
    public static final String WAITING_LUDO_USER="getWaitDatOnApp";
    public static final String GET_PLAYER_DETAIL="getPlayerDetails";
    public static final String CONTAST_PLAYER_COUNT="CONTAST_PLAYER_COUNT";
    public static final String SIGNUP="SIGNUP";
    public static final String REQ="req";
    public static final String RES="res";
    public static List<ScoreModel> score_list = new ArrayList<>();
    public static boolean isTimeOverShow=false;
    public static String Pattern_NAME = "[A-Za-z0-9]+";
    private static String blockCharacterSet = "~#^|$%&*!+=/.,-_;:<>?[]{}@()'\"×÷€£₹₩`\\°•○●□■♤♡◇♧☆▪¤《》¡¿";

    public static final String FALL_BACK_LINK="https://www.fantafeat.com/";
    public static final String FF_IOS_APP_ID="1551530502";
    public static final String FF_IOS_APP_BUNDLE="com.fantafeat.app";
    public static final String TEST_APP="com.fantafeat.test";
    public static final String LINK_PREFIX_FANTASY="fantasy";
    public static final String LINK_PREFIX_GAME="game";
    public static final String IMAGE_NAME="imageName";
    public static final String IMAGE_BASE="imageBase";
    public static final String LINK_PREFIX_DEPOSIT="deposit";
    public static final String LINK_PREFIX_TRADING="trading";
    public static final String LINK_PREFIX_POLL="poll";
    public static final String LINK_PREFIX_OPINION="opinion";
    public static final String LINK_PREFIX_HELPDESK="helpdesk";
    public static final String LINK_PREFIX_URL="https://fantafeatapp.page.link";
    public static final String LINK_URL="https://www.fantafeat.com/";
    public static final String LINK_FANTASY_URL=LINK_URL+LINK_PREFIX_FANTASY+"/";
    public static final String LINK_GAME_URL=LINK_URL+LINK_PREFIX_GAME+"/";
    public static final String LINK_DEPOSIT_URL=LINK_URL+LINK_PREFIX_DEPOSIT+"/";

    public static final String LINK_TRADING_URL=LINK_URL+LINK_PREFIX_TRADING+"/";
    public static final String LINK_POLL_URL=LINK_URL+LINK_PREFIX_POLL+"/";
    public static final String LINK_OPINION_URL=LINK_URL+LINK_PREFIX_OPINION+"/";



    public static final String PLAY_STORE="com.fantafeat.app";

    public static InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };

    public static void reduceDragSensitivity(ViewPager2 viewpager) {
        try {
            Field ff = ViewPager2.class.getDeclaredField("mRecyclerView") ;
            ff.setAccessible(true);
            RecyclerView recyclerView =  (RecyclerView) ff.get(viewpager);
            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop") ;
            touchSlopField.setAccessible(true);
            int touchSlop = (int) touchSlopField.get(recyclerView);
            touchSlopField.set(recyclerView,touchSlop*4);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static int getColorCode(String score){
        if (score.equalsIgnoreCase("4")){
            return R.drawable.circul_green;
        }else if (score.equalsIgnoreCase("6")){
            return R.drawable.circul_green;
        }else if (score.equalsIgnoreCase("0")){
            return R.drawable.circul_black;
        }else if (score.equalsIgnoreCase("1")){
            return R.drawable.gray_circul_fill;
        }else if (score.equalsIgnoreCase("2")){
            return R.drawable.dark_gray_circul_fill;
        }else if (score.equalsIgnoreCase("3")){
            return R.drawable.dark_gray_circul_fill;
        }else if (score.equalsIgnoreCase("w")){
            return R.drawable.circul_primary;
        }else {
            return R.drawable.gray_circul_fill;
        }
    }

    public static ArrayList<ContestQuantityModel> getContestEntryList(){
        ArrayList<ContestQuantityModel> contestQtyList=new ArrayList<>();

        ContestQuantityModel model = new ContestQuantityModel();
        model.setId("1");
        model.setNoOfQty("1X");
        model.setSelected(true);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("2");
        model.setNoOfQty("2X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("4");
        model.setNoOfQty("4X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("5");
        model.setNoOfQty("5X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("10");
        model.setNoOfQty("10X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("15");
        model.setNoOfQty("15X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("20");
        model.setNoOfQty("20X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("25");
        model.setNoOfQty("25X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("35");
        model.setNoOfQty("35X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("50");
        model.setNoOfQty("50X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("75");
        model.setNoOfQty("75X");
        model.setSelected(false);
        contestQtyList.add(model);
        model = new ContestQuantityModel();
        model.setId("100");
        model.setNoOfQty("100X");
        model.setSelected(false);
        contestQtyList.add(model);

        return contestQtyList;

    }

    public static InputFilter EMOJI_FILTER = (source, start, end, dest, dstart, dend) -> {
        for (int index = start; index < end; index++) {

            int type = Character.getType(source.charAt(index));

            if (type == Character.SURROGATE) {
                return "";
            }
        }
        return null;
    };
}
