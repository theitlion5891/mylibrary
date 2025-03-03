package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("menu_name")
    @Expose
    private String menuName;
    @SerializedName("menu_key")
    @Expose
    private String menuKey;
    @SerializedName("menu_url")
    @Expose
    private Object menuUrl;
    @SerializedName("display_ios")
    @Expose
    private String displayIos;
    @SerializedName("display_android")
    @Expose
    private String displayAndroid;
    @SerializedName("is_web")
    @Expose
    private String isWeb;
    @SerializedName("menu_img")
    @Expose
    private String menuImg;
    @SerializedName("menu_order")
    @Expose
    private String menuOrder;
    @SerializedName("menu_status")
    @Expose
    private String menuStatus;
    @SerializedName("display_separator")
    @Expose
    private String displaySeparator;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("update_at")
    @Expose
    private String updateAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public Object getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(Object menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getDisplayIos() {
        return displayIos;
    }

    public void setDisplayIos(String displayIos) {
        this.displayIos = displayIos;
    }

    public String getDisplayAndroid() {
        return displayAndroid;
    }

    public void setDisplayAndroid(String displayAndroid) {
        this.displayAndroid = displayAndroid;
    }

    public String getIsWeb() {
        return isWeb;
    }

    public void setIsWeb(String isWeb) {
        this.isWeb = isWeb;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    public String getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(String menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getDisplaySeparator() {
        return displaySeparator;
    }

    public void setDisplaySeparator(String displaySeparator) {
        this.displaySeparator = displaySeparator;
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
