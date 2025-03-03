package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawListModel {

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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("update_by")
    @Expose
    private String updateBy;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("wd_final_amt")
    @Expose
    private String wdFinalAmt;
    @SerializedName("tds_amt")
    @Expose
    private String tdsAmt;
    @SerializedName("charg_amt")
    @Expose
    private String chargAmt;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_acc_no")
    @Expose
    private String bankAccNo;
    @SerializedName("bank_acc_holder_name")
    @Expose
    private String bankAccHolderName;
    @SerializedName("bank_ifsc_no")
    @Expose
    private String bankIfscNo;
    @SerializedName("entry_date")
    @Expose
    private String entryDate;
    @SerializedName("wd_bfr_tds_amt")
    @Expose
    private String wdBfrTdsAmt;
    @SerializedName("financial_year")
    @Expose
    private String financialYear;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public String getWdFinalAmt() {
        return wdFinalAmt;
    }

    public void setWdFinalAmt(String wdFinalAmt) {
        this.wdFinalAmt = wdFinalAmt;
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public String getChargAmt() {
        return chargAmt;
    }

    public void setChargAmt(String chargAmt) {
        this.chargAmt = chargAmt;
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

    public String getBankAccHolderName() {
        return bankAccHolderName;
    }

    public void setBankAccHolderName(String bankAccHolderName) {
        this.bankAccHolderName = bankAccHolderName;
    }

    public String getBankIfscNo() {
        return bankIfscNo;
    }

    public void setBankIfscNo(String bankIfscNo) {
        this.bankIfscNo = bankIfscNo;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getWdBfrTdsAmt() {
        return wdBfrTdsAmt;
    }

    public void setWdBfrTdsAmt(String wdBfrTdsAmt) {
        this.wdBfrTdsAmt = wdBfrTdsAmt;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

}
