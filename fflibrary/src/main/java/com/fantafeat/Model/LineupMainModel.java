package com.fantafeat.Model;

import java.util.List;

public class LineupMainModel {

    private int type;
    private int xiType;//1=xi, 2=substitute, 3=notxi, 0=notlineup
    private String title;
    private List<PlayerModel> playerModelList1;
    private List<PlayerModel> playerModelList2;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getXiType() {
        return xiType;
    }

    public void setXiType(int xiType) {
        this.xiType = xiType;
    }

    public List<PlayerModel> getPlayerModelList2() {
        return playerModelList2;
    }

    public void setPlayerModelList2(List<PlayerModel> playerModelList2) {
        this.playerModelList2 = playerModelList2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<PlayerModel> getPlayerModelList1() {
        return playerModelList1;
    }

    public void setPlayerModelList1(List<PlayerModel> playerModelList) {
        this.playerModelList1 = playerModelList;
    }
}
