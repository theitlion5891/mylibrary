package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("menu")
    @Expose
    private String menu;
    @SerializedName("is_score_card_refresh")
    @Expose
    private String is_score_card_refresh;

    @SerializedName("sports")
    @Expose
    private String sports;
    @SerializedName("sport_details")
    @Expose
    private String sportDetails;
    @SerializedName("apk_ver_android")
    @Expose
    private String apkVerAndroid;
    @SerializedName("apk_ver_ios")
    @Expose
    private String apkVerIos;
    @SerializedName("display_sport")
    @Expose
    private String displaySport;
    @SerializedName("display_sub_sports")
    @Expose
    private String displaySubSports;
    @SerializedName("max_team_count")
    @Expose
    private String maxTeamCount;
    @SerializedName("min_deposit_amount")
    @Expose
    private String minDepositAmount;
    @SerializedName("max_deposit_amount")
    @Expose
    private String maxDepositAmount;
    @SerializedName("display_deposit_paytm")
    @Expose
    private String displayDepositPaytm;
    @SerializedName("display_deposit_razor")
    @Expose
    private String displayDepositRazor;
    @SerializedName("display_deposit_paytm_instant")
    @Expose
    private String displayDepositPaytmInstant;
    @SerializedName("display_deposit_easebuzz")
    @Expose
    private String display_deposit_easebuzz;
    @SerializedName("display_deposit_cashfree")
    @Expose
    private String display_deposit_cashfree;
    private String is_enable_selc_per="yes";
    @SerializedName("is_play_store")
    @Expose
    private String is_play_store;
    @SerializedName("min_withdraw_amount")
    @Expose
    private String minWithdrawAmount;
    @SerializedName("max_withdraw_amount")
    @Expose
    private String maxWithdrawAmount;
    @SerializedName("min_withdraw_amount_instant")
    @Expose
    private String minWithdrawAmountInstant;
    @SerializedName("max_withdraw_amount_instant")
    @Expose
    private String maxWithdrawAmountInstant;
    @SerializedName("donation_amount")
    @Expose
    private String donationAmount;
    @SerializedName("donation_text")
    @Expose
    private String donationText;
    @SerializedName("home_banner")
    @Expose
    private String homeBanner;
    @SerializedName("ads_banner")
    @Expose
    private String adsBanner;
    @SerializedName("app_ver")
    @Expose
    private String appVer;
    @SerializedName("encashment_rate")
    @Expose
    private String encashmentRate;
    @SerializedName("encashment_min_amount")
    @Expose
    private String encashmentMinAmount;
    @SerializedName("paytm_wallet_withdraw_limit")
    @Expose
    private String paytmWalletWithdrawLimit;
    @SerializedName("paytm_wallet_withdraw_amount_max")
    @Expose
    private String paytmWalletWithdrawAmountMax;
    @SerializedName("paytm_charges")
    @Expose
    private String paytmCharges;
    @SerializedName("paytm_charges_gst")
    @Expose
    private String paytmChargesGst;
    @SerializedName("paytm_instant_tnc")
    @Expose
    private String paytmInstantTnc;
    @SerializedName("bank_withdraw_tnc")
    @Expose
    private String bankWithdrawTnc;
    @SerializedName("winnig_tree_text")
    @Expose
    private String winnig_tree_text;
    @SerializedName("inning_display_all")
    @Expose
    private String inning_display_all;
    @SerializedName("cf_instant_withdraw_limit")
    @Expose
    private String cf_instant_withdraw_limit;
    @SerializedName("cf_instant_withdraw_maxlimit")
    @Expose
    private String cf_instant_withdraw_maxlimit;
    @SerializedName("cf_instant_withdraw_tnc")
    @Expose
    private String cf_instant_withdraw_tnc;
    @SerializedName("is_multijoin")
    @Expose
    private String is_multijoin;
    @SerializedName("ludo_tnc")
    @Expose
    private String ludo_tnc;
    @SerializedName("is_cf_instant_withdraw")
    @Expose
    private String is_cf_instant_withdraw;
    @SerializedName("is_top_enable")
    @Expose
    private String is_top_enable;
    @SerializedName("sql_lite_enable")
    @Expose
    private String sql_lite_enable;

    @SerializedName("image_base_path_url")
    @Expose
    private String image_base_path_url;

    @SerializedName("user_header_key")
    @Expose
    private String user_header_key;
    @SerializedName("lb_tnc")
    @Expose
    private String lb_tnc;
    @SerializedName("home_christmas_image")
    @Expose
    private String home_christmas_image;
    @SerializedName("is_whatsapp_enable")
    @Expose
    private String is_whatsapp_enable;
    @SerializedName("is_story_avaialble")
    @Expose
    private String is_story_avaialble;

    @SerializedName("is_rectangle_banner_avaialble")
    @Expose
    private String is_rectangle_banner_avaialble;
    @SerializedName("is_trade_available")
    @Expose
    private String is_trade_available;
    @SerializedName("is_category_available")
    @Expose
    private String is_category_available;
    @SerializedName("series_id_banner")
    @Expose
    private String series_id_banner;
    @SerializedName("series_title_banner")
    @Expose
    private String series_title_banner;
    @SerializedName("is_wd_expected_date")
    @Expose
    private String is_wd_expected_date;
  /*  @SerializedName("is_display_gst")
    @Expose
    private String is_display_gst;
    @SerializedName("gst_per")
    @Expose
    private String gst_per;*/

    public String getIs_wd_expected_date() {
        return is_wd_expected_date;
    }

    public void setIs_wd_expected_date(String is_wd_expected_date) {
        this.is_wd_expected_date = is_wd_expected_date;
    }

    public String getIs_play_store() {
        return is_play_store;
    }

    public void setIs_play_store(String is_play_store) {
        this.is_play_store = is_play_store;
    }

    public String getSeries_title_banner() {
        return series_title_banner;
    }

    public void setSeries_title_banner(String series_title_banner) {
        this.series_title_banner = series_title_banner;
    }

    public String getSeries_id_banner() {
        return series_id_banner;
    }

    public void setSeries_id_banner(String series_id_banner) {
        this.series_id_banner = series_id_banner;
    }

    public String getIs_rectangle_banner_avaialble() {
        return is_rectangle_banner_avaialble;
    }

    public void setIs_rectangle_banner_avaialble(String is_rectangle_banner_avaialble) {
        this.is_rectangle_banner_avaialble = is_rectangle_banner_avaialble;
    }

    public String getIs_trade_available() {
        return is_trade_available;
    }

    public void setIs_trade_available(String is_trade_available) {
        this.is_trade_available = is_trade_available;
    }

    public String getIs_category_available() {
        return is_category_available;
    }

    public void setIs_category_available(String is_category_available) {
        this.is_category_available = is_category_available;
    }

    public String getIs_story_avaialble() {
        return is_story_avaialble;
    }

    public void setIs_story_avaialble(String is_story_avaialble) {
        this.is_story_avaialble = is_story_avaialble;
    }

    public String getIs_top_enable() {
        return is_top_enable;
    }

    public void setIs_top_enable(String is_top_enable) {
        this.is_top_enable = is_top_enable;
    }

   /* public String getIs_display_gst() {
        return is_display_gst;
    }

    public void setIs_display_gst(String is_display_gst) {
        this.is_display_gst = is_display_gst;
    }

    public String getGst_per() {
        return gst_per;
    }

    public void setGst_per(String gst_per) {
        this.gst_per = gst_per;
    }*/

    public String getIs_whatsapp_enable() {
        return is_whatsapp_enable;
    }

    public void setIs_whatsapp_enable(String is_whatsapp_enable) {
        this.is_whatsapp_enable = is_whatsapp_enable;
    }

    public String getHome_christmas_image() {
        return home_christmas_image;
    }

    public void setHome_christmas_image(String home_christmas_image) {
        this.home_christmas_image = home_christmas_image;
    }

    public String getDisplay_deposit_cashfree() {
        return display_deposit_cashfree;
    }

    public void setDisplay_deposit_cashfree(String display_deposit_cashfree) {
        this.display_deposit_cashfree = display_deposit_cashfree;
    }

    public String getLb_tnc() {
        return lb_tnc;
    }

    public void setLb_tnc(String lb_tnc) {
        this.lb_tnc = lb_tnc;
    }

    public String getDisplay_deposit_easebuzz() {
        return display_deposit_easebuzz;
    }

    public void setDisplay_deposit_easebuzz(String display_deposit_easebuzz) {
        this.display_deposit_easebuzz = display_deposit_easebuzz;
    }

    public String getLudo_tnc() {
        return ludo_tnc;
    }

    public void setLudo_tnc(String ludo_tnc) {
        this.ludo_tnc = ludo_tnc;
    }

    public void setIs_score_card_refresh(String is_score_card_refresh) {
        this.is_score_card_refresh = is_score_card_refresh;
    }

    public String getIs_score_card_refresh() {
        return is_score_card_refresh;
    }

    public String getImage_base_path_url() {
        return image_base_path_url;
    }

    public void setImage_base_path_url(String image_base_path_url) {
        this.image_base_path_url = image_base_path_url;
    }

    public String getUser_header_key() {
        return user_header_key;
    }

    public void setUser_header_key(String user_header_key) {
        this.user_header_key = user_header_key;
    }

    public String getSql_lite_enable() {
        return sql_lite_enable;
    }

    public void setSql_lite_enable(String sql_lite_enable) {
        this.sql_lite_enable = sql_lite_enable;
    }

    public String getIs_multijoin() {
        return is_multijoin;
    }

    public void setIs_multijoin(String is_multijoin) {
        this.is_multijoin = is_multijoin;
    }

    public String getIs_cf_instant_withdraw() {
        return is_cf_instant_withdraw;
    }

    public void setIs_cf_instant_withdraw(String is_cf_instant_withdraw) {
        this.is_cf_instant_withdraw = is_cf_instant_withdraw;
    }

    public String getCf_instant_withdraw_limit() {
        return cf_instant_withdraw_limit;
    }

    public void setCf_instant_withdraw_limit(String cf_instant_withdraw_limit) {
        this.cf_instant_withdraw_limit = cf_instant_withdraw_limit;
    }

    public String getCf_instant_withdraw_maxlimit() {
        return cf_instant_withdraw_maxlimit;
    }

    public void setCf_instant_withdraw_maxlimit(String cf_instant_withdraw_maxlimit) {
        this.cf_instant_withdraw_maxlimit = cf_instant_withdraw_maxlimit;
    }

    public String getCf_instant_withdraw_tnc() {
        return cf_instant_withdraw_tnc;
    }

    public void setCf_instant_withdraw_tnc(String cf_instant_withdraw_tnc) {
        this.cf_instant_withdraw_tnc = cf_instant_withdraw_tnc;
    }

    public String getWinnig_tree_text() {
        return winnig_tree_text;
    }

    public void setWinnig_tree_text(String winnig_tree_text) {
        this.winnig_tree_text = winnig_tree_text;
    }

    public String getInning_display_all() {
        return inning_display_all;
    }

    public void setInning_display_all(String inning_display_all) {
        this.inning_display_all = inning_display_all;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getSportDetails() {
        return sportDetails;
    }

    public void setSportDetails(String sportDetails) {
        this.sportDetails = sportDetails;
    }

    public String getApkVerAndroid() {
        return apkVerAndroid;
    }

    public void setApkVerAndroid(String apkVerAndroid) {
        this.apkVerAndroid = apkVerAndroid;
    }

    public String getApkVerIos() {
        return apkVerIos;
    }

    public void setApkVerIos(String apkVerIos) {
        this.apkVerIos = apkVerIos;
    }

    public String getDisplaySport() {
        return displaySport;
    }

    public void setDisplaySport(String displaySport) {
        this.displaySport = displaySport;
    }

    public String getDisplaySubSports() {
        return displaySubSports;
    }

    public void setDisplaySubSports(String displaySubSports) {
        this.displaySubSports = displaySubSports;
    }

    public String getMaxTeamCount() {
        return maxTeamCount;
    }

    public void setMaxTeamCount(String maxTeamCount) {
        this.maxTeamCount = maxTeamCount;
    }

    public String getMinDepositAmount() {
        return minDepositAmount;
    }

    public void setMinDepositAmount(String minDepositAmount) {
        this.minDepositAmount = minDepositAmount;
    }

    public String getMaxDepositAmount() {
        return maxDepositAmount;
    }

    public void setMaxDepositAmount(String maxDepositAmount) {
        this.maxDepositAmount = maxDepositAmount;
    }

    public String getDisplayDepositPaytm() {
        return displayDepositPaytm;
    }

    public void setDisplayDepositPaytm(String displayDepositPaytm) {
        this.displayDepositPaytm = displayDepositPaytm;
    }

    public String getDisplayDepositRazor() {
        return displayDepositRazor;
    }

    public void setDisplayDepositRazor(String displayDepositRazor) {
        this.displayDepositRazor = displayDepositRazor;
    }

    public String getDisplayDepositPaytmInstant() {
        return displayDepositPaytmInstant;
    }

    public void setDisplayDepositPaytmInstant(String displayDepositPaytmInstant) {
        this.displayDepositPaytmInstant = displayDepositPaytmInstant;
    }

    public String getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(String minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public String getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(String maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public String getMinWithdrawAmountInstant() {
        return minWithdrawAmountInstant;
    }

    public void setMinWithdrawAmountInstant(String minWithdrawAmountInstant) {
        this.minWithdrawAmountInstant = minWithdrawAmountInstant;
    }

    public String getMaxWithdrawAmountInstant() {
        return maxWithdrawAmountInstant;
    }

    public void setMaxWithdrawAmountInstant(String maxWithdrawAmountInstant) {
        this.maxWithdrawAmountInstant = maxWithdrawAmountInstant;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getDonationText() {
        return donationText;
    }

    public void setDonationText(String donationText) {
        this.donationText = donationText;
    }

    public String getIs_enable_selc_per() {
        return is_enable_selc_per;
    }

    public void setIs_enable_selc_per(String is_enable_selc_per) {
        this.is_enable_selc_per = is_enable_selc_per;
    }

    public String getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(String homeBanner) {
        this.homeBanner = homeBanner;
    }

    public String getAdsBanner() {
        return adsBanner;
    }

    public void setAdsBanner(String adsBanner) {
        this.adsBanner = adsBanner;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getEncashmentRate() {
        return encashmentRate;
    }

    public void setEncashmentRate(String encashmentRate) {
        this.encashmentRate = encashmentRate;
    }

    public String getEncashmentMinAmount() {
        return encashmentMinAmount;
    }

    public void setEncashmentMinAmount(String encashmentMinAmount) {
        this.encashmentMinAmount = encashmentMinAmount;
    }

    public String getPaytmWalletWithdrawLimit() {
        return paytmWalletWithdrawLimit;
    }

    public void setPaytmWalletWithdrawLimit(String paytmWalletWithdrawLimit) {
        this.paytmWalletWithdrawLimit = paytmWalletWithdrawLimit;
    }

    public String getPaytmWalletWithdrawAmountMax() {
        return paytmWalletWithdrawAmountMax;
    }

    public void setPaytmWalletWithdrawAmountMax(String paytmWalletWithdrawAmountMax) {
        this.paytmWalletWithdrawAmountMax = paytmWalletWithdrawAmountMax;
    }

    public String getPaytmCharges() {
        return paytmCharges;
    }

    public void setPaytmCharges(String paytmCharges) {
        this.paytmCharges = paytmCharges;
    }

    public String getPaytmChargesGst() {
        return paytmChargesGst;
    }

    public void setPaytmChargesGst(String paytmChargesGst) {
        this.paytmChargesGst = paytmChargesGst;
    }

    public String getPaytmInstantTnc() {
        return paytmInstantTnc;
    }

    public void setPaytmInstantTnc(String paytmInstantTnc) {
        this.paytmInstantTnc = paytmInstantTnc;
    }

    public String getBankWithdrawTnc() {
        return bankWithdrawTnc;
    }

    public void setBankWithdrawTnc(String bankWithdrawTnc) {
        this.bankWithdrawTnc = bankWithdrawTnc;
    }

}