package com.apptech.myapplication.modal.message;

public class MessageList {
    String title;
    String data_time;
    String des;
    boolean expanble;


    public MessageList(String title, String data_time, String des, boolean expanble) {
        this.title = title;
        this.data_time = data_time;
        this.des = des;
        this.expanble = expanble;
    }


    public String getTitle() {
        return title;
    }

    public String getData_time() {
        return data_time;
    }

    public String getDes() {
        return des;
    }

    public boolean isExpanble() {
        return expanble;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setExpanble(boolean expanble) {
        this.expanble = expanble;
    }
}
