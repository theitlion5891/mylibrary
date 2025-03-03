package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOfferModal {
    @SerializedName("team_no")
    @Expose
    private String team_no;
    @SerializedName("offer_text")
    @Expose
    private String offer_text;
    @SerializedName("used_bonus")
    @Expose
    private String used_bonus;
    @SerializedName("discount_entry_fee")
    @Expose
    private String discount_entry_fee;
    @SerializedName("entry_fee")
    @Expose
    private String entry_fee;

    private String passId;

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getTeam_no() {
        return team_no;
    }

    public void setTeam_no(String team_no) {
        this.team_no = team_no;
    }

    public String getOffer_text() {
        return offer_text;
    }

    public void setOffer_text(String offer_text) {
        this.offer_text = offer_text;
    }

    public String getUsed_bonus() {
        return used_bonus;
    }

    public void setUsed_bonus(String used_bonus) {
        this.used_bonus = used_bonus;
    }

    public String getDiscount_entry_fee() {
        return discount_entry_fee;
    }

    public void setDiscount_entry_fee(String discount_entry_fee) {
        this.discount_entry_fee = discount_entry_fee;
    }

    public String getEntry_fee() {
        return entry_fee;
    }

    public void setEntry_fee(String entry_fee) {
        this.entry_fee = entry_fee;
    }
}
