package com.fantafeat.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ff_coins_bal")
    @Expose
    private String ff_coin;
    @SerializedName("old_ff_coins_bal")
    @Expose
    private String old_ff_coins_bal="0";
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("user_team_name")
    @Expose
    private String userTeamName;
    @SerializedName("user_img")
    @Expose
    private String userImg;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("ref_by")
    @Expose
    private String refBy;
    @SerializedName("level_1_id")
    @Expose
    private String level1Id;
    @SerializedName("level_2_id")
    @Expose
    private String level2Id;
    @SerializedName("level_3_id")
    @Expose
    private String level3Id;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("user_status")
    @Expose
    private String userStatus;
    @SerializedName("bank_acc_holder_name")
    @Expose
    private String bank_acc_holder_name;
    @SerializedName("user_level")
    @Expose
    private String userLevel;
    @SerializedName("dashboard_login")
    @Expose
    private String dashboardLogin;
    @SerializedName("dashboard_login_role")
    @Expose
    private String dashboardLoginRole;
    @SerializedName("is_promoter")
    @Expose
    private String isPromoter;
    @SerializedName("promoter_comm")
    @Expose
    private String promoterComm;
    @SerializedName("promoter_comm_1")
    @Expose
    private String promoterComm1;
    @SerializedName("promoter_comm_2")
    @Expose
    private String promoterComm2;
    @SerializedName("promoter_comm_1_id")
    @Expose
    private String promoterComm1Id;
    @SerializedName("promoter_comm_2_id")
    @Expose
    private String promoterComm2Id;
    @SerializedName("affiliation_start_date")
    @Expose
    private String affiliationStartDate;
    @SerializedName("token_no")
    @Expose
    private String tokenNo;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("phone_uid")
    @Expose
    private String phoneUid;
    @SerializedName("app_ver")
    @Expose
    private String appVer;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile_os")
    @Expose
    private String mobileOs;
    @SerializedName("mobile_type")
    @Expose
    private String mobileType;
    @SerializedName("mobile_name")
    @Expose
    private String mobileName;
    @SerializedName("mobile_hardware")
    @Expose
    private String mobileHardware;
    @SerializedName("page_display")
    @Expose
    private String pageDisplay;
    @SerializedName("visit")
    @Expose
    private String visit;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("pan_image")
    @Expose
    private String panImage;
    @SerializedName("pan_status")
    @Expose
    private String panStatus;
    @SerializedName("pan_update_date")
    @Expose
    private String panUpdateDate;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_acc_no")
    @Expose
    private String bankAccNo;
    @SerializedName("bank_ifsc_no")
    @Expose
    private String bankIfscNo;
    @SerializedName("bank_acc_img")
    @Expose
    private String bankAccImg;
    @SerializedName("bank_acc_status")
    @Expose
    private String bankAccStatus;
    @SerializedName("bank_update_date")
    @Expose
    private String bankUpdateDate;
    @SerializedName("pan_msg")
    @Expose
    private String panMsg;
    @SerializedName("bank_msg")
    @Expose
    private String bankMsg;
    @SerializedName("deposit_bal")
    @Expose
    private String depositBal;
    @SerializedName("bonus_bal")
    @Expose
    private String bonusBal;
    @SerializedName("win_bal")
    @Expose
    private String winBal;
    @SerializedName("transfer_bal")
    @Expose
    private String transferBal;
    @SerializedName("donation_bal")
    @Expose
    private String donationBal;
    @SerializedName("force_data_clear")
    @Expose
    private String forceDataClear;
    @SerializedName("force_logout")
    @Expose
    private String forceLogout;
    @SerializedName("deposit_msg")
    @Expose
    private String depositMsg;
    @SerializedName("deposit_amt")
    @Expose
    private String depositAmt;
    @SerializedName("depost_order_id")
    @Expose
    private String depostOrderId;
    @SerializedName("last_otp")
    @Expose
    private String lastOtp;
    @SerializedName("favorite_league")
    @Expose
    private String favoriteLeague;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("facebook_id")
    @Expose
    private String facebook_id;
    @SerializedName("insta_id")
    @Expose
    private String insta_id;
    @SerializedName("telegram_id")
    @Expose
    private String telegram_id;
    @SerializedName("twitter_id")
    @Expose
    private String twitter_id;
    @SerializedName("youtube_id")
    @Expose
    private String youtube_id;
    @SerializedName("team_name_change_allow")
    @Expose
    private String team_name_change_allow;

    @SerializedName("addr_prf_status")
    @Expose
    private String addr_prf_status;
    @SerializedName("addr_prf_front_img")
    @Expose
    private String addr_prf_front_img;
    @SerializedName("addr_prf_back_img")
    @Expose
    private String addr_prf_back_img;
    @SerializedName("addr_prf_type")
    @Expose
    private String addr_prf_type;

    public String getAddr_prf_status() {
        return addr_prf_status;
    }

    public void setAddr_prf_status(String addr_prf_status) {
        this.addr_prf_status = addr_prf_status;
    }

    public String getAddr_prf_front_img() {
        return addr_prf_front_img;
    }

    public void setAddr_prf_front_img(String addr_prf_front_img) {
        this.addr_prf_front_img = addr_prf_front_img;
    }

    public String getAddr_prf_back_img() {
        return addr_prf_back_img;
    }

    public void setAddr_prf_back_img(String addr_prf_back_img) {
        this.addr_prf_back_img = addr_prf_back_img;
    }

    public String getAddr_prf_type() {
        return addr_prf_type;
    }

    public void setAddr_prf_type(String addr_prf_type) {
        this.addr_prf_type = addr_prf_type;
    }

    public String getOld_ff_coins_bal() {
        return old_ff_coins_bal;
    }

    public void setOld_ff_coins_bal(String old_ff_coins_bal) {
        this.old_ff_coins_bal = old_ff_coins_bal;
    }

    public String getFf_coin() {
        return ff_coin;
    }

    public void setFf_coin(String ff_coin) {
        this.ff_coin = ff_coin;
    }

    public String getTeam_name_change_allow() {
        return team_name_change_allow;
    }

    public void setTeam_name_change_allow(String team_name_change_allow) {
        this.team_name_change_allow = team_name_change_allow;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getInsta_id() {
        return insta_id;
    }

    public void setInsta_id(String insta_id) {
        this.insta_id = insta_id;
    }

    public String getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(String telegram_id) {
        this.telegram_id = telegram_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    private float total_balance = 0;

    public String getBank_acc_holder_name() {
        return bank_acc_holder_name;
    }

    public void setBank_acc_holder_name(String bank_acc_holder_name) {
        this.bank_acc_holder_name = bank_acc_holder_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(float total_balance) {
        this.total_balance = total_balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserTeamName() {
        return userTeamName;
    }

    public void setUserTeamName(String userTeamName) {
        this.userTeamName = userTeamName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
    }

    public String getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(String level1Id) {
        this.level1Id = level1Id;
    }

    public String getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(String level2Id) {
        this.level2Id = level2Id;
    }

    public String getLevel3Id() {
        return level3Id;
    }

    public void setLevel3Id(String level3Id) {
        this.level3Id = level3Id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getDashboardLogin() {
        return dashboardLogin;
    }

    public void setDashboardLogin(String dashboardLogin) {
        this.dashboardLogin = dashboardLogin;
    }

    public String getDashboardLoginRole() {
        return dashboardLoginRole;
    }

    public void setDashboardLoginRole(String dashboardLoginRole) {
        this.dashboardLoginRole = dashboardLoginRole;
    }

    public String getIsPromoter() {
        return isPromoter;
    }

    public void setIsPromoter(String isPromoter) {
        this.isPromoter = isPromoter;
    }

    public String getPromoterComm() {
        return promoterComm;
    }

    public void setPromoterComm(String promoterComm) {
        this.promoterComm = promoterComm;
    }

    public String getPromoterComm1() {
        return promoterComm1;
    }

    public void setPromoterComm1(String promoterComm1) {
        this.promoterComm1 = promoterComm1;
    }

    public String getPromoterComm2() {
        return promoterComm2;
    }

    public void setPromoterComm2(String promoterComm2) {
        this.promoterComm2 = promoterComm2;
    }

    public String getPromoterComm1Id() {
        return promoterComm1Id;
    }

    public void setPromoterComm1Id(String promoterComm1Id) {
        this.promoterComm1Id = promoterComm1Id;
    }

    public String getPromoterComm2Id() {
        return promoterComm2Id;
    }

    public void setPromoterComm2Id(String promoterComm2Id) {
        this.promoterComm2Id = promoterComm2Id;
    }

    public String getAffiliationStartDate() {
        return affiliationStartDate;
    }

    public void setAffiliationStartDate(String affiliationStartDate) {
        this.affiliationStartDate = affiliationStartDate;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getPhoneUid() {
        return phoneUid;
    }

    public void setPhoneUid(String phoneUid) {
        this.phoneUid = phoneUid;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getMobileOs() {
        return mobileOs;
    }

    public void setMobileOs(String mobileOs) {
        this.mobileOs = mobileOs;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
    }

    public String getMobileHardware() {
        return mobileHardware;
    }

    public void setMobileHardware(String mobileHardware) {
        this.mobileHardware = mobileHardware;
    }

    public String getPageDisplay() {
        return pageDisplay;
    }

    public void setPageDisplay(String pageDisplay) {
        this.pageDisplay = pageDisplay;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanImage() {
        return panImage;
    }

    public void setPanImage(String panImage) {
        this.panImage = panImage;
    }

    public String getPanStatus() {
        return panStatus;
    }

    public void setPanStatus(String panStatus) {
        this.panStatus = panStatus;
    }

    public String getPanUpdateDate() {
        return panUpdateDate;
    }

    public void setPanUpdateDate(String panUpdateDate) {
        this.panUpdateDate = panUpdateDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankIfscNo() {
        return bankIfscNo;
    }

    public void setBankIfscNo(String bankIfscNo) {
        this.bankIfscNo = bankIfscNo;
    }

    public String getBankAccImg() {
        return bankAccImg;
    }

    public void setBankAccImg(String bankAccImg) {
        this.bankAccImg = bankAccImg;
    }

    public String getBankAccStatus() {
        return bankAccStatus;
    }

    public void setBankAccStatus(String bankAccStatus) {
        this.bankAccStatus = bankAccStatus;
    }

    public String getBankUpdateDate() {
        return bankUpdateDate;
    }

    public void setBankUpdateDate(String bankUpdateDate) {
        this.bankUpdateDate = bankUpdateDate;
    }

    public String getPanMsg() {
        return panMsg;
    }

    public void setPanMsg(String panMsg) {
        this.panMsg = panMsg;
    }

    public String getBankMsg() {
        return bankMsg;
    }

    public void setBankMsg(String bankMsg) {
        this.bankMsg = bankMsg;
    }

    public String getDepositBal() {
        return depositBal;
    }

    public void setDepositBal(String depositBal) {
        this.depositBal = depositBal;
    }

    public String getBonusBal() {
        return bonusBal;
    }

    public void setBonusBal(String bonusBal) {
        this.bonusBal = bonusBal;
    }

    public String getWinBal() {
        return winBal;
    }

    public void setWinBal(String winBal) {
        this.winBal = winBal;
    }

    public String getTransferBal() {
        return transferBal;
    }

    public void setTransferBal(String transferBal) {
        this.transferBal = transferBal;
    }

    public String getDonationBal() {
        return donationBal;
    }

    public void setDonationBal(String donationBal) {
        this.donationBal = donationBal;
    }

    public String getForceDataClear() {
        return forceDataClear;
    }

    public void setForceDataClear(String forceDataClear) {
        this.forceDataClear = forceDataClear;
    }

    public String getForceLogout() {
        return forceLogout;
    }

    public void setForceLogout(String forceLogout) {
        this.forceLogout = forceLogout;
    }

    public String getDepositMsg() {
        return depositMsg;
    }

    public void setDepositMsg(String depositMsg) {
        this.depositMsg = depositMsg;
    }

    public String getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }

    public String getDepostOrderId() {
        return depostOrderId;
    }

    public void setDepostOrderId(String depostOrderId) {
        this.depostOrderId = depostOrderId;
    }

    public String getLastOtp() {
        return lastOtp;
    }

    public void setLastOtp(String lastOtp) {
        this.lastOtp = lastOtp;
    }

    public String getFavoriteLeague() {
        return favoriteLeague;
    }

    public void setFavoriteLeague(String favoriteLeague) {
        this.favoriteLeague = favoriteLeague;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

}