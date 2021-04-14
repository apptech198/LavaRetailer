package com.apptech.myapplication.modal.message;

public class MessageList  implements Comparable, Cloneable {
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

    @Override
    public int compareTo(Object o) {
        MessageList compare = (MessageList) o;
        if (compare.title.equals(this.title) && compare.expanble == this.expanble) {
            return 0;
        }
        return 1;
    }

    @Override
    public MessageList clone() {

        MessageList clone;
        try {
            clone = (MessageList) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }
        return clone;
    }

}




























