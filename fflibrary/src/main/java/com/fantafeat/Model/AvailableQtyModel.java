package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableQtyModel {

    private int id;
    @SerializedName("jn_amount")
    @Expose
    private String jnAmount;
    @SerializedName("total_join_cnt")
    @Expose
    private String totalJoinCnt;
    @SerializedName("jn_ef_amount")
    @Expose
    private String jnEfAmount;
    @SerializedName("total_join_cnt_sell")
    @Expose
    private String total_join_cnt_sell;
    private boolean isSelected=false;

    public String getTotal_join_cnt_sell() {
        return total_join_cnt_sell;
    }

    public void setTotal_join_cnt_sell(String total_join_cnt_sell) {
        this.total_join_cnt_sell = total_join_cnt_sell;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getJnAmount() {
        return jnAmount;
    }

    public void setJnAmount(String jnAmount) {
        this.jnAmount = jnAmount;
    }

    public String getTotalJoinCnt() {
        return totalJoinCnt;
    }

    public void setTotalJoinCnt(String totalJoinCnt) {
        this.totalJoinCnt = totalJoinCnt;
    }

    public String getJnEfAmount() {
        return jnEfAmount;
    }

    public void setJnEfAmount(String jnEfAmount) {
        this.jnEfAmount = jnEfAmount;
    }

}
