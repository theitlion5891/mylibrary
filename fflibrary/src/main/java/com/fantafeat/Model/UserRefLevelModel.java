package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRefLevelModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("user_team_name")
    @Expose
    private String user_team_name;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("joined_match")
    @Expose
    private String joinedMatch;

    public String getUser_team_name() {
        return user_team_name;
    }

    public void setUser_team_name(String user_team_name) {
        this.user_team_name = user_team_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getJoinedMatch() {
        return joinedMatch;
    }

    public void setJoinedMatch(String joinedMatch) {
        this.joinedMatch = joinedMatch;
    }

}