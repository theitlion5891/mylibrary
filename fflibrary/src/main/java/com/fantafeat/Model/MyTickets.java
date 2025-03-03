package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyTickets implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("topic_id")
    @Expose
    private String topicId;
    @SerializedName("trans_id")
    @Expose
    private String transId;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("ticket_status")
    @Expose
    private String ticketStatus;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("chat_enable")
    @Expose
    private String chatEnable;
    @SerializedName("user_rating")
    @Expose
    private String userRating;
    @SerializedName("rating_msg")
    @Expose
    private String ratingMsg;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;
    @SerializedName("user_agent_assign_time")
    @Expose
    private String userAgentAssignTime;
    @SerializedName("topic_name")
    @Expose
    private String topicName;
    @SerializedName("is_kyc_bank_reject")
    @Expose
    private String is_kyc_bank_reject;
    @SerializedName("is_team_edit")
    @Expose
    private String is_team_edit;

    public String getIs_kyc_bank_reject() {
        return is_kyc_bank_reject;
    }

    public void setIs_kyc_bank_reject(String is_kyc_bank_reject) {
        this.is_kyc_bank_reject = is_kyc_bank_reject;
    }

    public String getIs_team_edit() {
        return is_team_edit;
    }

    public void setIs_team_edit(String is_team_edit) {
        this.is_team_edit = is_team_edit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getChatEnable() {
        return chatEnable;
    }

    public void setChatEnable(String chatEnable) {
        this.chatEnable = chatEnable;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getRatingMsg() {
        return ratingMsg;
    }

    public void setRatingMsg(String ratingMsg) {
        this.ratingMsg = ratingMsg;
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

    public String getUserAgentAssignTime() {
        return userAgentAssignTime;
    }

    public void setUserAgentAssignTime(String userAgentAssignTime) {
        this.userAgentAssignTime = userAgentAssignTime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}
