package com.apptech.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.apptech.myapplication.R;
import com.apptech.myapplication.modal.productgallery.List;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.bumptech.glide.Glide;


public class ProductGalleryAdapter extends PagerAdapter {

    Context context;
    java.util.List<List> lists;
    private LayoutInflater layoutInflater;
    private static final String TAG = "ProductGalleryAdapter";
    SessionManage sessionManage;


    public ProductGalleryAdapter(java.util.List<List> list , Context context) {
        this.lists = list;
        this.context = context;
        sessionManage = SessionManage.getInstance(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        List list = lists.get(position);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.row_product_gallery, container, false);

        Log.e(TAG, "instantiateItem: " + list.getImgUrl() );

        ImageView imageView = item_view.findViewById(R.id.img);

        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            Glide.with(context).load(ApiClient.Image_URL + list.getImgUrlAr()).placeholder(R.drawable.check_icon).into(imageView);
        } else {
            Glide.with(context).load(ApiClient.Image_URL + list.getImgUrl()).placeholder(R.drawable.check_icon).into(imageView);
        }



        container.addView(item_view);
        return item_view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);

        //        super.destroyItem(container, position, object);
    }

}






























