package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("trans_id")
    @Expose
    private String transId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("trans_type")
    @Expose
    private String transType;
    @SerializedName("trans_name")
    @Expose
    private String transName;
    @SerializedName("total_bal")
    @Expose
    private String totalBal;
    @SerializedName("trans_desc")
    @Expose
    private String transDesc;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("close_bal_dtls")
    @Expose
    private String closeBalDtls;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("contest_id")
    @Expose
    private String contestId;
    @SerializedName("txn_id")
    @Expose
    private String txnId;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    private boolean isOpen = false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTotalBal() {
        return totalBal;
    }

    public void setTotalBal(String totalBal) {
        this.totalBal = totalBal;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getCloseBalDtls() {
        return closeBalDtls;
    }

    public void setCloseBalDtls(String closeBalDtls) {
        this.closeBalDtls = closeBalDtls;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
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
