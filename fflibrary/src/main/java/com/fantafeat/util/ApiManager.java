package com.fantafeat.util;


import com.fantafeat.Session.MyApp;

public class ApiManager {
    //public static native String stringFromJNI();

    public static native String getBASEURL();
    public static native String getDashBASEURL();
    public static native String getV3BASEURL();
    public static native String getV3DASHBASEURL();
    //public static native String getTempBASEURL();
    public static native String getUpdateURL();
    public static native String getImageBaseURL();
    public static native String getLudoBaseURL();
    public static String BASE=getBASEURL();//getDashBASEURL
    public static String V3BASE=getV3BASEURL();//getV3DASHBASEURL
    public static String IMAGEBASE=getImageBaseURL();


   // private static String TEMPBASE=getTempBASEURL();
    private static String URL = BASE+"API/";
    private static String AUTHV3URL = V3BASE+"auth/";
    private static String MATCHV3URL = V3BASE+"matchList/";
    private static String OTHERSV3URL = V3BASE+"others/";

    public static String CHECK_APP_SERVER = getUpdateURL();

   // public static String imgBase=BASE;

    //public static String IMGURL = BASE+"myAppImages/";
    public static String TEAM_IMG =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "documents/";
    public static String BANNER_IMAGES =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "banner/";
    public static String PROFILE_IMG =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "user_img/";
    public static String DOCUMENTS =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "documents/";
    public static String NOTIFICATION =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "notification_img/";
    public static String EXCEL_DOWNLOAD =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "Excel/";
    public static String POPUP_BANNER_IMG =  MyApp.getMyPreferences().getPrefString(PrefConstant.IMAGE_BASE) + "documents/";

    public static String JOINED_CONTEST_EDIT = URL + "joinedContestEdit.php";

    public static String appDataList = URL + "appDataList.php";
    public static String MATCH_LIST_COMPLETED = URL + "matchListCompleted.php";
    public static String AUTHV3UpdateUserDetails = AUTHV3URL + "updateUserDetails.php";
    public static String getUserDetailsOtherCompanyLogin = AUTHV3URL + "getUserDetailsOtherCompanyLogin.php";

    public static String contestListWithTypeId =  MATCHV3URL  + "contestListWithTypeId.php";
    public static String contestListV3 =  MATCHV3URL  + "defaultMatchWiseContestList.php";
    public static String MATCH_LIST = URL + "matchList.php";
    public static String CONTEST_LIST = URL + "contestList.php";
    public static String MATCH_DETAIL_LIST = URL + "matchDetailsList.php";
    public static String TEMP_TEAM_DETAIL_LIST = URL + "tempTeamDetailsList.php";
    public static String CREATE_TEMP_TEAM = URL + "createTempTeam.php";
    public static String JOIN_CONTEST_LIST_BY_ID = URL + "joinedContestListByUserID.php";
    public static String TEMP_TEAM_LIST = URL + "tempTeamList.php";
    public static String MATCH_LIST_FIXTURE = URL + "matchListFixture.php";
    public static String MATCH_LIST_LIVE = URL + "matchListLive.php";
    public static String GET_MATCH_SCORE = URL + "getMatchScore.php";
    public static String PLAYER_LEADERBOARD = URL + "playerLeaderBoard.php";
    public static String CONTEST_LEADER_BOARD = URL + "contestLeaderBoard.php";
    public static String TEMP_TEMP_DETAIL_BY_ID = URL + "tempTeamDetailsListById.php";
    public static String STATE_LIST = URL + "state_list.php";
    public static String PLAYER_STATS = URL + "playerStats.php";
    public static String JOIN_CONTEST2MultiJoin = URL + "joinedContest_other_comp.php";
    public static String EDIT_TEMP_TEAM = URL + "editTempTeam2.php";
    public static String AFTER_MATCH_PLAYER_STATE = URL + "playerLeaderBoardPoints.php";
    public static String SCORE_CARD = URL + "scoreCardListInningWise.php";


    public static Boolean isPagerSwipe=true;
    public static Boolean isTabLoad=false;
    public static Boolean isInstant=true;
    public static Boolean isPayTm=true;
    public static boolean IS_MULTIJOIN = true;
    public static boolean IS_BREAKUP_SHOW = true;
    public static boolean IS_SQLITE_ENABLE = false;
    public static boolean play_store_app = false;


}
