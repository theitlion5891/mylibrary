package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pass_id")
    @Expose
    private String passId;
    @SerializedName("total_join_spot")
    @Expose
    private String totalJoinSpot;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("no_of_entry")
    @Expose
    private String noOfEntry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getTotalJoinSpot() {
        return totalJoinSpot;
    }

    public void setTotalJoinSpot(String totalJoinSpot) {
        this.totalJoinSpot = totalJoinSpot;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getNoOfEntry() {
        return noOfEntry;
    }

    public void setNoOfEntry(String noOfEntry) {
        this.noOfEntry = noOfEntry;
    }
}
