package com.apptech.myapplication.modal.card;

public class CardList {

    String id;
    String name;
    String img;
    String qty;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getQty() {
        return qty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public CardList(String id, String name, String img, String qty) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.qty = qty;
    }
}
