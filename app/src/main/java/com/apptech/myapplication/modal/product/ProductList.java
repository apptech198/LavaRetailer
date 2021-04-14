package com.apptech.myapplication.modal.product;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ProductList {

    String id;
    String name;
    String img;
    String qty;


    public ProductList(String id, String name, String img, String qty) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.qty = qty;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @BindingAdapter("android:productImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .fitCenter()
                .into(imageView);
    }

}
