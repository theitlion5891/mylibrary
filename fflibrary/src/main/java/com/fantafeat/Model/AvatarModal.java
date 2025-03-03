package com.fantafeat.Model;

public class AvatarModal {
    int avatar;
    String id;
    boolean isCheck=false;

    public AvatarModal(int avatar, String id) {
        this.avatar = avatar;
        this.id = id;
    }

    public AvatarModal(int avatar, String id, boolean isCheck) {
        this.avatar = avatar;
        this.id = id;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
