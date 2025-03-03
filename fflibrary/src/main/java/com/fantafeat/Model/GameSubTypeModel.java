package com.fantafeat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GameSubTypeModel implements Serializable {
    @SerializedName("mode_name")
    @Expose
    private String modeName;
    @SerializedName("mode_image")
    @Expose
    private String modeImage;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_default")
    @Expose
    private String is_default;
    @SerializedName("order_no")
    @Expose
    private String order_no;
    private boolean isSelected=false;

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeImage() {
        return modeImage;
    }

    public void setModeImage(String modeImage) {
        this.modeImage = modeImage;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
