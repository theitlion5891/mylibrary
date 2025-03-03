package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserLeaderboardList {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public class Datum implements Serializable {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("total_win_amount")
        @Expose
        private String total_win_amount;
        @SerializedName("total_point")
        @Expose
        private String totalPoint;
        @SerializedName("total_rank")
        @Expose
        private String totalRank;

        @SerializedName("display_name")
        @Expose
        private String displayName;
        @SerializedName("user_team_name")
        @Expose
        private String userTeamName;
        @SerializedName("phone_no")
        @Expose
        private String phoneNo;
        @SerializedName("user_img")
        @Expose
        private String userImg;
        @SerializedName("ref_no")
        @Expose
        private String refNo;

        public String getTotal_win_amount() {
            return total_win_amount;
        }

        public void setTotal_win_amount(String total_win_amount) {
            this.total_win_amount = total_win_amount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTotalPoint() {
            return totalPoint;
        }

        public void setTotalPoint(String totalPoint) {
            this.totalPoint = totalPoint;
        }

        public String getTotalRank() {
            return totalRank;
        }

        public void setTotalRank(String totalRank) {
            this.totalRank = totalRank;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getUserTeamName() {
            return userTeamName;
        }

        public void setUserTeamName(String userTeamName) {
            this.userTeamName = userTeamName;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

    }
}
