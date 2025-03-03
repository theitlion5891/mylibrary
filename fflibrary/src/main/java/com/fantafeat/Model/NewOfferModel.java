package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOfferModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("offer_title")
    @Expose
    private String offerTitle;
    @SerializedName("offer_desc")
    @Expose
    private String offerDesc;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("discount_types")
    @Expose
    private String discountTypes;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("deposit_val")
    @Expose
    private String depositVal;
    @SerializedName("trans_val")
    @Expose
    private String transVal;
    @SerializedName("bonus_val")
    @Expose
    private String bonusVal;
    @SerializedName("win_val")
    @Expose
    private String winVal;
    @SerializedName("max_count")
    @Expose
    private String maxCount;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("min_amount")
    @Expose
    private String minAmount;
    @SerializedName("max_amount")
    @Expose
    private String maxAmount;
    @SerializedName("assign_user_id")
    @Expose
    private String assignUserId;
    @SerializedName("assign_ref_codes")
    @Expose
    private String assignRefCodes;
    @SerializedName("offer_img")
    @Expose
    private String offerImg;
    @SerializedName("offer_tnc")
    @Expose
    private String offerTnc;
    private boolean isApply=false;

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountTypes() {
        return discountTypes;
    }

    public void setDiscountTypes(String discountTypes) {
        this.discountTypes = discountTypes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDepositVal() {
        return depositVal;
    }

    public void setDepositVal(String depositVal) {
        this.depositVal = depositVal;
    }

    public String getTransVal() {
        return transVal;
    }

    public void setTransVal(String transVal) {
        this.transVal = transVal;
    }

    public String getBonusVal() {
        return bonusVal;
    }

    public void setBonusVal(String bonusVal) {
        this.bonusVal = bonusVal;
    }

    public String getWinVal() {
        return winVal;
    }

    public void setWinVal(String winVal) {
        this.winVal = winVal;
    }

    public String getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(String assignUserId) {
        this.assignUserId = assignUserId;
    }

    public String getAssignRefCodes() {
        return assignRefCodes;
    }

    public void setAssignRefCodes(String assignRefCodes) {
        this.assignRefCodes = assignRefCodes;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }

    public String getOfferTnc() {
        return offerTnc;
    }

    public void setOfferTnc(String offerTnc) {
        this.offerTnc = offerTnc;
    }

}
