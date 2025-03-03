package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserLeaderboardSubType {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("code")
    @Expose
    private String code;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class Datum {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("inf_lb_master_id")
        @Expose
        private String infLbMasterId;
        @SerializedName("lb_master_id")
        @Expose
        private String lb_master_id;
        @SerializedName("master_type_desc")
        @Expose
        private String masterTypeDesc;
        @SerializedName("from_dt")
        @Expose
        private String fromDt;
        @SerializedName("to_dt")
        @Expose
        private String toDt;
        @SerializedName("winning_dec")
        @Expose
        private String winningDec;
        @SerializedName("ref_no")
        @Expose
        private String refNo;
        @SerializedName("display_cont")
        @Expose
        private String displayCont;
        @SerializedName("create_at")
        @Expose
        private String createAt;
        @SerializedName("update_at")
        @Expose
        private String updateAt;
        @SerializedName("distribute_amount")
        @Expose
        private String distributeAmount;
        @SerializedName("total_win_user")
        @Expose
        private String totalWinUser;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("is_display")
        @Expose
        private String isDisplay;
        @SerializedName("league_id")
        @Expose
        private String leagueId;
        @SerializedName("winning_tree")
        @Expose
        private String winningTree;
        @SerializedName("asset_url")
        @Expose
        private String asset_url;

        public String getLb_master_id() {
            return lb_master_id;
        }

        public void setLb_master_id(String lb_master_id) {
            this.lb_master_id = lb_master_id;
        }

        public String getAsset_url() {
            return asset_url;
        }

        public void setAsset_url(String asset_url) {
            this.asset_url = asset_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInfLbMasterId() {
            return infLbMasterId;
        }

        public void setInfLbMasterId(String infLbMasterId) {
            this.infLbMasterId = infLbMasterId;
        }

        public String getMasterTypeDesc() {
            return masterTypeDesc;
        }

        public void setMasterTypeDesc(String masterTypeDesc) {
            this.masterTypeDesc = masterTypeDesc;
        }

        public String getFromDt() {
            return fromDt;
        }

        public void setFromDt(String fromDt) {
            this.fromDt = fromDt;
        }

        public String getToDt() {
            return toDt;
        }

        public void setToDt(String toDt) {
            this.toDt = toDt;
        }

        public String getWinningDec() {
            return winningDec;
        }

        public void setWinningDec(String winningDec) {
            this.winningDec = winningDec;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getDisplayCont() {
            return displayCont;
        }

        public void setDisplayCont(String displayCont) {
            this.displayCont = displayCont;
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

        public String getDistributeAmount() {
            return distributeAmount;
        }

        public void setDistributeAmount(String distributeAmount) {
            this.distributeAmount = distributeAmount;
        }

        public String getTotalWinUser() {
            return totalWinUser;
        }

        public void setTotalWinUser(String totalWinUser) {
            this.totalWinUser = totalWinUser;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getIsDisplay() {
            return isDisplay;
        }

        public void setIsDisplay(String isDisplay) {
            this.isDisplay = isDisplay;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getWinningTree() {
            return winningTree;
        }

        public void setWinningTree(String winningTree) {
            this.winningTree = winningTree;
        }

    }
}
