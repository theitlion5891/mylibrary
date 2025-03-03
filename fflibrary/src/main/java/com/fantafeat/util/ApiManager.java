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
    public static native String getOpinioBaseUrl();
    public static native String getOpinioDashBaseUrl();
    public static String BASE=getBASEURL();//getDashBASEURL
    public static String V3BASE=getV3BASEURL();//getV3DASHBASEURL
    public static String IMAGEBASE=getImageBaseURL();
    public static String LUDOBASE=getLudoBaseURL();
    public static String OPINIOBASEURL = getOpinioBaseUrl(); //getOpinioDashBaseUrl


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

    public static String MENU_LIST = URL + "menuList.php";
    public static String JOINED_CONTEST_EDIT = URL + "joinedContestEdit.php";
    public static String LOGIN_OTP_RESEND = URL + "loginOtpResend.php";
    public static String LOGIN_OTP_WP_RESEND = URL + "loginOtpResendWp.php";
    public static String autoVerifyDecentroPanCard = URL + "autoVerifyDecentroPanCard.php";
    public static String autoVerifyDecentroBankAccount = URL + "autoVerifyDecentroBankAccount.php";

    public static String getTdsAmountAndPercentage =  OTHERSV3URL+ "getTdsAmountAndPercentageWithList.php";//getTdsAmountAndPercentage
    public static String getWinningTranferOfferList = OTHERSV3URL + "getWinningTranferOfferList.php";
    public static String winningTransferToDepositCash = OTHERSV3URL + "winningTransferToDepositCash.php";
    public static String getMyTicketList = OTHERSV3URL + "getMyTicketList.php";
    public static String createTicket = OTHERSV3URL + "createTicket.php";
 public static String getCallbackTopicFaqList = OTHERSV3URL + "getCallbackTopicFaqList.php";
    public static String getCallbackTopicList = OTHERSV3URL + "getCallbackTopicList.php";
    public static String getMyTicketListMessageList = OTHERSV3URL + "getMyTicketListMessageList.php";
    public static String getCallbackTopicFaqListDeafultDisplay = OTHERSV3URL + "getCallbackTopicFaqListDeafultDisplay.php";
    public static String updateUserRating = OTHERSV3URL + "updateUserRating.php";
    public static String infLeaderboardType = URL + "infLeaderboardType.php";
    public static String upload_user_callbackDoc = URL + "upload_user_callbackDoc.php";
    public static String userLeaderboardType = URL + "userLeaderboardType.php";
    public static String userLeaderboardDetailslist = URL + "userLeaderboardDetailslist.php";
    public static String userLeaderboardTypeSubList = URL + "userLeaderboardTypeSubList.php";
    public static String userLeaderboardlist = URL + "userLeaderboardlist.php";
    public static String infLeaderboardList = URL + "infLeaderboardList.php";
    public static String infLeaderboardListDtls = URL + "infLeaderboardListDtls.php";

    //public static String ludoContestListNew = LUDOBASE + "contestListNew.php"/*"contestList.php"*/;
    public static String ludoContestListNew = "http://43.204.158.171/ludo/contestListNewPass.php";
    public static String ludoContestList = LUDOBASE + "contestListNewPassFF.php"/*"contestList.php"*/;
    public static String verifyLudoContest = LUDOBASE + "verifyLudoContest.php"/*"contestList.php"*/;
    public static String createLudoContest = LUDOBASE + "createLudoContest.php"/*"contestList.php"*/;
    public static String gamesTncGetFF = LUDOBASE + "gamesTncGetFF.php";
    public static String RUMMY_CONTEST = LUDOBASE+"rummyContestList.php";
    public static String gameList = LUDOBASE + "game_list_ff.php";//game_details_ff
    public static String leaderboardList = LUDOBASE+"lb/leaderboardList.php";
    public static String leaderboardRankList = LUDOBASE+"lb/leaderboardRankList.php";

    public static String getTournamentList = LUDOBASE+"tournaments/getTournamentList.php";
    public static String getTournamentList_1 = LUDOBASE+"tournaments/getTournamentList_1.php";
    public static String getTournamentDetailsList = LUDOBASE+"tournaments/getTournamentDetailsList.php";
    public static String investmentLeaderboardRankList = OTHERSV3URL + "investmentLeaderboardRankList.php";
    public static String investmentLeaderBoardList = OTHERSV3URL + "investmentLeaderBoardList.php";
    public static String investmentLeaderBoardDetailList = OTHERSV3URL + "investmentLeaderBoardDetailList.php";
    public static String appDataList = URL + "appDataList.php";
    public static String LOGIN = URL + "login2.php";
    public static String REGISTER = AUTHV3URL + "register.php";
    public static String adminUserDetailsCheck = URL + "adminUserDetailsCheck.php";

    //public static String ADMIN_USERDETAIL_CHECK = TEMPBASE;
    public static String MATCH_LIST_COMPLETED = URL + "matchListCompleted.php";
    public static String OTP_VERIFY = URL + "loginOtpVerify.php";
    public static String AUTHV3OTP_VERIFY = AUTHV3URL + "otpVerify.php";
    public static String AUTHV3UpdateUserDetails = AUTHV3URL + "updateUserDetails.php";
    public static String AUTHV3GetUserDetails = AUTHV3URL + "getUserDetails.php";
    public static String getUserKycSetting = AUTHV3URL + "getUserKycSetting.php";


    public static String AUTHV3_Resend_OTP_VERIFY = AUTHV3URL + "resendOtp.php";
    public static String contestListWithTypeId =  MATCHV3URL  + "contestListWithTypeId.php";
    public static String contestListV3 =  MATCHV3URL  + "defaultMatchWiseContestList.php";

    public static String STORY_USER_LIST = OTHERSV3URL+"storyUserList.php";
    public static String STORY_VIEW_UPDATE = OTHERSV3URL+"storyViewUpdate.php";
    public static String PHONEPE_CHECKSUM = OTHERSV3URL+"phonepe_checksum.php";
    public static String PHONEPE_CHECK_STATUS = OTHERSV3URL+"phonepe_check_trans.php";
    public static String getDepositOfferList = OTHERSV3URL + "getDepositOfferList.php";
    public static String OTP = URL + "loginOtpVerify.php";
    public static String USER_DETAIL = URL + "getUserDetailByID.php";
    public static String updateUserVerifyDetails = URL + "updateUserVerifyDetails.php";
    public static String UPDATE_MASTER = URL + "checkUpdate.php";
    public static String SPORT_SUB_LIST = URL + "sportsListDetails.php";
    public static String BANK_INSTANT_WITHDRAW_REQUEST = URL + "add_instant_withdraw_cashfree_DualTransList.php";//add_instant_withdraw_cashfree
    public static String SPORTS_LIST = URL + "sportsList.php";
    public static String UPDATE_USER_DETAILS = URL + "updateUserDetails.php";
    public static String MATCH_LIST = URL + "matchList.php";
    public static String CONTEST_LIST = URL + "contestList.php";
    public static String MATCH_DETAIL_LIST = URL + "matchDetailsList.php";
    public static String TEMP_TEAM_DETAIL_LIST = URL + "tempTeamDetailsList.php";
    public static String CREATE_TEMP_TEAM = URL + "createTempTeam.php";
    public static String JOIN_CONTEST_LIST_BY_ID = URL + "joinedContestListByUserID.php";
    public static String TEMP_TEAM_LIST = URL + "tempTeamList.php";
    public static String MATCH_LIST_FIXTURE = URL + "matchListFixture.php";

    public static String MATCH_LIST_FIXTURE_TEST = URL + "matchFixtureListTest.php";
    public static String MATCH_WISE_JOIN_GROUP_LIST = URL + "matchWiseJoinGroupList.php";
    public static String MATCH_LIST_LIVE = URL + "matchListLive.php";
    public static String GET_MATCH_SCORE = URL + "getMatchScore.php";
    public static String PLAYER_LEADERBOARD = URL + "playerLeaderBoard.php";
    public static String CONTEST_LEADER_BOARD = URL + "contestLeaderBoard.php";
    public static String TEMP_TEMP_DETAIL_BY_ID = URL + "tempTeamDetailsListById.php";
    public static String AFFILIATION = URL + "user_affiliation.php";
    public static String MATCH_AFFILIATION = URL + "matchAffiliationHeader.php";
    public static String AFFILIATION_DETAILS = URL + "user_affiliation_list.php";
    public static String MATCH_WISE_AFFILIATION = URL + "matchWiseUserAffiliationList.php";
    public static String MATCH_LIST_AFFILIATION = URL + "matchAffiliationListSUbHead.php";
    public static String CALLBACK_SUB_TYPE = URL + "callback_subject_type.php";
    public static String CALL_BACK_SUBMIT = URL + "add_call.php";
    public static String STATE_LIST = URL + "state_list.php";
    public static String UPLOAD_PAN_IMAGE =URL + "upload_pan_image.php";
    public static String UPLOAD_BANK_IMAGE =URL + "upload_acc_image.php";
    public static String UPLOAD_ADDRESS_IMAGE =URL + "upload_addr_image.php";
    public static String REQUEST_STATEMENT = URL + "requestStatement.php";
    public static String BANK_WITHDRAW_REQUEST = URL + "addWithdraw_DualTransList.php";//addWithdraw
    public static String PAYTM_WITHDRAW_REQUEST = URL + "add_withdraw_paytm_wallet.php";
    public static String PAYTM_WITHDRAW_RESEND_OTP = URL + "withdraw_paytm_otp_resend.php";
    public static String PAYTM_WITHDRAW_SUBMIT = URL + "withdraw_paytm_wallet_verify.php";
    public static String leaderboardCategoryList = OTHERSV3URL + "leaderboardCategoryList.php";
    public static String GET_REF_COUNT = URL + "get_ref_user_count.php";
    public static String GET_REF_USER_DETAILS = URL + "get_ref_user_dtls.php";
    public static String BUZZ = URL + "buzz.php";

    public static String checkDepositStatus = URL + "checkDepositStatus.php";
    public static String TRANSFER_BAL = URL + "addTransfer.php";
    public static String TRANSACTION_LIST = URL + "transactionList.php";
    public static String withdrawListWithTds = URL + "withdrawListWithTds.php";
    public static String requestStatementnew = URL + "requestStatementnew.php";
    public static String HOME_BANNER = URL + "bannerList.php";

    public static String RAZOR_CHECKSUM = URL + "razor_checksum.php";
    public static String CHECK_CODE = URL + "checkCuponCode.php";
    public static String ADD_AMOUNT_RAZORPAY = URL + "add_cash_razorpay.php";
    public static String TRANSACTION_FAIL = URL + "transaction_failed.php";
    public static String CASHFREE_TOKEN = URL + "cashfree_token.php";
    public static String CASHFREE_TOKEN_2 = URL + "cashfree_token_2_new.php";
    public static String appPaymentSetting = URL + "appPaymentSetting.php";
    public static String ADD_CASH_CASHFREE = URL + "add_cash_cashfree.php";
    private static String PAYTM_URL = URL + "paytm_auth/";
    public static String GENERATE_CHECKSUM = PAYTM_URL + "generateChecksum.php";
    public static String GENERATE_CHECKSUM2 = PAYTM_URL + "generateChecksum2.php";
    public static String ADD_AMOUNT = URL + "add_cash.php";
    public static String PLAYER_STATS = URL + "playerStats.php";
    public static String UPLOAD_USER_IMAGE =URL + "upload_user_image.php";
    public static String NOTIFICATION_LIST = URL + "notification_list.php";
    public static String JOIN_CONTEST = URL + "joinedContest.php";
    public static String JOIN_CONTEST2 = URL + "joinedContest2.php";
    public static String JOIN_CONTEST2MultiJoin = URL + "joinedContest2.php";//joinedContest2MultiJoin
    public static String EDIT_TEMP_TEAM = URL + "editTempTeam2.php";
    public static String AFTER_MATCH_PLAYER_STATE = URL + "playerLeaderBoardPoints.php";
    public static String CHECK_APP = URL + "check_app.php";
    public static String app_promotion = URL + "app_promotion.php";
    public static String SCORE_CARD = URL + "scoreCardListInningWise.php";
    public static String POPUP_BANNER = URL + "popupBannerList.php";


 public static String tradesContestListDetailsCount = OPINIOBASEURL + "tradesContestListDetailsCount.php";
 public static String TRADING_CONTEST_LIST = OPINIOBASEURL + "tradesContestList.php";

 public static String liveMyJoinTradeBoardContestList = OPINIOBASEURL + "liveMyJoinTradeBoardContestList.php";
 public static String completedMyJoinTradeBoardContestList = OPINIOBASEURL + "completedMyJoinTradeBoardContestList.php";
 public static String joinTrdeBoardContest = OPINIOBASEURL + "joinTrdeBoardContest.php";

 public static String myJoinTradesContestListLive = OPINIOBASEURL + "myJoinTradesContestListLive.php";
 public static String myJoinOpinionContestList = OPINIOBASEURL + "myJoinOpinionContestList.php";
 public static String myJoinPollContestList = OPINIOBASEURL + "myJoinPollContestList.php";
 public static String MY_OPINION_CONTEST_LIST_EXIT = OPINIOBASEURL + "myJoinOpenionPendingContestExit.php";
 public static String opinionContestDetailLeaderboard = OPINIOBASEURL + "opinionContestDetailLeaderboard.php";
 public static String myJoinTradesContestListDetails = OPINIOBASEURL + "myJoinTradesContestListDetails.php";
 public static String myJoinTradesContestListClose = OPINIOBASEURL + "myJoinTradesContestListClose.php";
 public static String myJoinOpinionContestListClose = OPINIOBASEURL + "myJoinOpinionContestListClose.php";
 public static String myJoinPollContestListClose = OPINIOBASEURL + "myJoinPollContestListClose.php";
 public static String myJoinTradesPendingContestCancel = OPINIOBASEURL + "myJoinTradesPendingContestCancel.php";
 public static String myJoinTradesConfirmContestExitCancel = OPINIOBASEURL + "myJoinTradesConfirmContestExitCancel.php";
 public static String myJoinTradesConfirmContestExit = OPINIOBASEURL + "myJoinTradesConfirmContestExit.php";
 public static String myJoinPollContestListDetails = OPINIOBASEURL + "myJoinPollContestListDetails.php";
 public static String MY_PREDICTION_CONTEST_LIST = OPINIOBASEURL + "myJoinPredictContestList.php";
 public static String joinTradesContest = OPINIOBASEURL + "joinTradesContest.php";//{"user_id":"122","comp_id":"1","contest_id":"1","entry_fee":"6","option_value":"No","con_join_qty":"10"}
 public static String joinPollContest = OPINIOBASEURL + "joinPollContest.php";//{"user_id":"122","comp_id":"1","contest_id":"1","entry_fee":"6","option_value":"No","con_join_qty":"10"}
 //public static String tradesContestListDetailsCount = OPINIOBASEURL + "tradesContestListDetailsCount.php"; //{"user_id":"122","comp_id":"1","contest_id":"1"}
 public static String openionContestList = OPINIOBASEURL + "openionContestList.php";
 public static String pollContestList = OPINIOBASEURL + "pollContestList.php";
 public static String PREDICTION_CONTEST_JOIN = OPINIOBASEURL + "joinPredictContest.php";


    public static String easebuzz = URL + "easebuzz.php";
    public static String MATCH_WISE_GROUP_JOIN = /*URL*/ MATCHV3URL+ "matchWiseGroupContestJoin.php";
    public static String MATCH_WISE_GROUP_JOIN_EDIT = MATCHV3URL + "matchWiseGroupContestJoinEdit.php";

    public static String MATCH_WISE_GROUP_CONTEST_LIST = URL + "matchWiseGroupContestList.php";
    public static String MATCH_WISE_GROUP_CONTEST_LIST_v3 = MATCHV3URL + "matchWiseGroupContestList.php";
    public static String MATCH_WISE_GROUP_LIST = URL + "matchWiseGroupList.php";
    public static String MATCH_WISE_JOINED_CONTEST_LIST = URL + "matchWiseGroupContestJoinedList.php";
    public static String MATCH_WISE_JOINED_CONTEST_LIST_v3 = MATCHV3URL + "matchWiseGroupContestJoinedList.php";

    public static String payment_image_url = "https://www.fantafeat.com/images/fav-icon.png";
    public static String support_email = "support@fantafeat.com";
    public static String support_phone = "+919534229534";
    public static String support_username = "FantafeatSupport";

    public static String fb_page_id = "fb://page/113391220510092";//"fb://page/100486275280399";
    public static String fb_page_url = "https://www.facebook.com/fantafeatofficial";//"https://www.facebook.com/pg/fantafeat.fantasy";
    public static String telegram_id = "https://t.me/fantafeat";//"https://t.me/fantafeat_fantasy";
    public static String instagram_id = "http://instagram.com/";//"https://www.instagram.com/fantafeat.fantasy"fantafeat;
    public static String tweet_url = "https://twitter.com/";//fantafeat_offic
    public static String whatsapp_url = "https://api.whatsapp.com/send?phone="+support_phone;//+919625449625

    public static String point_system_url = "https://www.fantafeat.com/point-system-mob.html";//"https://www.fantafeat.com/point-system.html";//https://www.batball11.com/fantasy_point.html
    public static String about_url = "https://www.fantafeat.com/who-we-are-mob.html";
   // public static String social_respo_url = "https://www.batball11.com/social_responsibility.html";
    public static String how_to_play_url ="https://www.fantafeat.com/how-to-play-mob.html" ;
    public static String user_benifit_url ="https://www.fantafeat.com/our-features-mob.html" ;
    public static String app_feature_url ="" ;//https://www.batball11.com/application_feature-mob.html
    public static String app_point_url ="https://www.fantafeat.com/point-system-mob.html" ;
    public static String term_condition_url ="https://www.fantafeat.com/terms-mob.html" ;
    public static String privacy_url = "https://www.fantafeat.com/privacy-policy-mob.html" ;

    public static String app_download_url = "https://www.fantafeat.com/download_ff_apk.php?refcode=";//apk/download.html

    public static Boolean isPagerSwipe=true;
    public static Boolean isTabLoad=false;
    public static Boolean isInstant=true;
    public static Boolean isPayTm=true;
    public static boolean IS_MULTIJOIN = true;
    public static boolean IS_BREAKUP_SHOW = true;
    public static boolean IS_SQLITE_ENABLE = false;
    public static boolean is_team_clear = true;
    public static boolean ISCDN = false;
    public static boolean play_store_app = false;

    public static int MAX_SQLITE_SIZE = 10;

}
