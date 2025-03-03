package com.fantafeat.Model;

import java.util.ArrayList;

public class TopicCatModel {

    String title;
    ArrayList<TopicModel> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TopicModel> getList() {
        return list;
    }

    public void setList(ArrayList<TopicModel> list) {
        this.list = list;
    }
}
