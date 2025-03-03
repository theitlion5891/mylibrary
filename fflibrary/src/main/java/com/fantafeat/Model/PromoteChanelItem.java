package com.fantafeat.Model;

public class PromoteChanelItem {

    int id=0;
    String ch_type="";
    String ch_name="";
    String ch_url="";

    public PromoteChanelItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCh_type() {
        return ch_type;
    }

    public void setCh_type(String ch_type) {
        this.ch_type = ch_type;
    }

    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

    public String getCh_url() {
        return ch_url;
    }

    public void setCh_url(String ch_url) {
        this.ch_url = ch_url;
    }
}
