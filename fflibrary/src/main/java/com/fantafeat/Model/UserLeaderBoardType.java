package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserLeaderBoardType {

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
        @SerializedName("lb_master_id")
        @Expose
        private String lbMasterId;
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
        @SerializedName("winning_tree")
        @Expose
        private String winning_tree;
        @SerializedName("distribute_amount")
        @Expose
        private String distribute_amount;

        public String getDistribute_amount() {
            return distribute_amount;
        }

        public void setDistribute_amount(String distribute_amount) {
            this.distribute_amount = distribute_amount;
        }

        public String getWinning_tree() {
            return winning_tree;
        }

        public void setWinning_tree(String winning_tree) {
            this.winning_tree = winning_tree;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLbMasterId() {
            return lbMasterId;
        }

        public void setLbMasterId(String lbMasterId) {
            this.lbMasterId = lbMasterId;
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
    }
}
