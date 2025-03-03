package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinningTreeModel {
    @SerializedName("rank_text")
    @Expose
    private String rank_text;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("asset_url")
    @Expose
    private String asset_url;

    public String getRank_text() {
        return rank_text;
    }

    public void setRank_text(String rank_text) {
        this.rank_text = rank_text;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getAsset_url() {
        return asset_url;
    }

    public void setAsset_url(String asset_url) {
        this.asset_url = asset_url;
    }
}
