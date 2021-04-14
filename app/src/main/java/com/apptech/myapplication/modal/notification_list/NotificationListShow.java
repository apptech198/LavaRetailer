package com.apptech.myapplication.modal.notification_list;

public class NotificationListShow implements Comparable, Cloneable {
    private String id;
    private String heading;
    private String des;
    private String img;
    private String time;
    private String brand_id;
    private String brand_name;

    public NotificationListShow(String id, String heading, String des, String img, String time, String brand_id, String brand_name) {
        this.id = id;
        this.heading = heading;
        this.des = des;
        this.img = img;
        this.time = time;
        this.brand_id = brand_id;
        this.brand_name = brand_name;
    }

    public String getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getDes() {
        return des;
    }

    public String getImg() {
        return img;
    }

    public String getTime() {
        return time;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }


    @Override
    public int compareTo(Object o) {
        NotificationListShow compare = (NotificationListShow) o;
        if (compare.getHeading().equals(this.heading) && compare.getDes().equals(this.des)) {
            return 0;
        }
        return 1;
    }

    @Override
    public NotificationListShow clone() {

        NotificationListShow clone;
        try {
            clone = (NotificationListShow) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return clone;
    }

}
