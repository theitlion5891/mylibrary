package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentSettingModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("is_web_view")
    @Expose
    private String isWebView;
    @SerializedName("is_web_view_android")
    @Expose
    private String is_web_view_android;
    @SerializedName("is_enable_android")
    @Expose
    private String is_enable_android;
    @SerializedName("is_enable")
    @Expose
    private String isEnable;
    @SerializedName("gateway_type")
    @Expose
    private String gatewayType;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("payment_type")
    @Expose
    private List<PaymentType> paymentType;
    @SerializedName("token_generate_url")
    @Expose
    private String tokenGenerateUrl;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("order_no")
    @Expose
    private String orderNo;

    @SerializedName("trans_success_url")
    @Expose
    private String trans_success_url;
    @SerializedName("trans_fail_url")
    @Expose
    private String trans_fail_url;

    public String getTrans_success_url() {
        return trans_success_url;
    }

    public void setTrans_success_url(String trans_success_url) {
        this.trans_success_url = trans_success_url;
    }

    public String getTrans_fail_url() {
        return trans_fail_url;
    }

    public void setTrans_fail_url(String trans_fail_url) {
        this.trans_fail_url = trans_fail_url;
    }

    public String getIs_web_view_android() {
        return is_web_view_android;
    }

    public void setIs_web_view_android(String is_web_view_android) {
        this.is_web_view_android = is_web_view_android;
    }

    public String getIs_enable_android() {
        return is_enable_android;
    }

    public void setIs_enable_android(String is_enable_android) {
        this.is_enable_android = is_enable_android;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIsWebView() {
        return isWebView;
    }

    public void setIsWebView(String isWebView) {
        this.isWebView = isWebView;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<PaymentType> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<PaymentType> paymentType) {
        this.paymentType = paymentType;
    }

    public String getTokenGenerateUrl() {
        return tokenGenerateUrl;
    }

    public void setTokenGenerateUrl(String tokenGenerateUrl) {
        this.tokenGenerateUrl = tokenGenerateUrl;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public class PaymentType {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("sub_title")
        @Expose
        private String subTitle;
        @SerializedName("tag_android")
        @Expose
        private String tag;
        @SerializedName("is_web_view")
        @Expose
        private String isWebView;
        @SerializedName("is_web_view_android")
        @Expose
        private String is_web_view_android;
        @SerializedName("is_enable_android")
        @Expose
        private String is_enable_android;

        @SerializedName("is_enable")
        @Expose
        private String isEnable;
        @SerializedName("gateway_type")
        @Expose
        private String gatewayType;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("token_generate_url")
        @Expose
        private String tokenGenerateUrl;
        @SerializedName("ref_no")
        @Expose
        private String refNo;
        @SerializedName("order_no")
        @Expose
        private String orderNo;

        @SerializedName("trans_success_url")
        @Expose
        private String trans_success_url;
        @SerializedName("trans_fail_url")
        @Expose
        private String trans_fail_url;

        public String getTrans_success_url() {
            return trans_success_url;
        }

        public void setTrans_success_url(String trans_success_url) {
            this.trans_success_url = trans_success_url;
        }

        public String getTrans_fail_url() {
            return trans_fail_url;
        }

        public void setTrans_fail_url(String trans_fail_url) {
            this.trans_fail_url = trans_fail_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getIsWebView() {
            return isWebView;
        }

        public void setIsWebView(String isWebView) {
            this.isWebView = isWebView;
        }

        public String getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(String isEnable) {
            this.isEnable = isEnable;
        }

        public String getGatewayType() {
            return gatewayType;
        }

        public void setGatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getTokenGenerateUrl() {
            return tokenGenerateUrl;
        }

        public void setTokenGenerateUrl(String tokenGenerateUrl) {
            this.tokenGenerateUrl = tokenGenerateUrl;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getIs_web_view_android() {
            return is_web_view_android;
        }

        public void setIs_web_view_android(String is_web_view_android) {
            this.is_web_view_android = is_web_view_android;
        }

        public String getIs_enable_android() {
            return is_enable_android;
        }

        public void setIs_enable_android(String is_enable_android) {
            this.is_enable_android = is_enable_android;
        }
    }
}
