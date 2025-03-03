package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic_id")
    @Expose
    private String topicId;
    @SerializedName("faq_question")
    @Expose
    private String faqQuestion;
    @SerializedName("faq_answer")
    @Expose
    private String faqAnswer;
    @SerializedName("faq_status")
    @Expose
    private String faqStatus;
    @SerializedName("faq_order_no")
    @Expose
    private String faqOrderNo;
    @SerializedName("is_display_default")
    @Expose
    private String isDisplayDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    public String getFaqStatus() {
        return faqStatus;
    }

    public void setFaqStatus(String faqStatus) {
        this.faqStatus = faqStatus;
    }

    public String getFaqOrderNo() {
        return faqOrderNo;
    }

    public void setFaqOrderNo(String faqOrderNo) {
        this.faqOrderNo = faqOrderNo;
    }

    public String getIsDisplayDefault() {
        return isDisplayDefault;
    }

    public void setIsDisplayDefault(String isDisplayDefault) {
        this.isDisplayDefault = isDisplayDefault;
    }

}
