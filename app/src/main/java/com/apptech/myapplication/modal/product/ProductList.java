package com.apptech.myapplication.modal.product;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ProductList {
    String name;
    String img;


    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public ProductList(String name, String img) {
        this.name = name;
        this.img = img;
    }



    @BindingAdapter("android:productImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .fitCenter()
                .into(imageView);
    }

}
