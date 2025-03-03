package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinningTransferOfferModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("start_amt")
    @Expose
    private String startAmt;
    @SerializedName("end_amt")
    @Expose
    private String endAmt;
    @SerializedName("credit_percentage")
    @Expose
    private String creditPercentage;

    private boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartAmt() {
        return startAmt;
    }

    public void setStartAmt(String startAmt) {
        this.startAmt = startAmt;
    }

    public String getEndAmt() {
        return endAmt;
    }

    public void setEndAmt(String endAmt) {
        this.endAmt = endAmt;
    }

    public String getCreditPercentage() {
        return creditPercentage;
    }

    public void setCreditPercentage(String creditPercentage) {
        this.creditPercentage = creditPercentage;
    }
}
