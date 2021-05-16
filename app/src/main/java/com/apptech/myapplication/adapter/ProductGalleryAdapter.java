package com.apptech.myapplication.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.apptech.myapplication.MobileNavigationDirections;
import com.apptech.myapplication.R;
import com.apptech.myapplication.list.product_gallery.ProductGalleryLists;
import com.apptech.myapplication.modal.productgallery.List;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.ui.order.product_details.ProductDetailsFragmentDirections;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;

import javax.xml.transform.Result;


public class ProductGalleryAdapter extends PagerAdapter {

    Context context;
    private LayoutInflater layoutInflater;
    private static final String TAG = "ProductGalleryAdapter";
    SessionManage sessionManage;
    java.util.List<ProductGalleryLists> lists;
    private int mCurrentPosition = 0;



    public ProductGalleryAdapter(java.util.List<ProductGalleryLists> galleryLists, Context context) {
        this.lists = galleryLists;
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

        ProductGalleryLists list = lists.get(position);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.row_product_gallery, container, false);


        ImageView imageView = item_view.findViewById(R.id.img);
        ImageView play = item_view.findViewById(R.id.play);


        if (list.getType().equalsIgnoreCase("IMAGE")){

            play.setVisibility(View.GONE);

            if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                Glide.with(context).load(ApiClient.Image_URL + list.get_img_url_ar()).placeholder(R.drawable.image_place_holder).into(imageView);
            } else {
                Glide.with(context).load(ApiClient.Image_URL + list.getImg_url()).placeholder(R.drawable.image_place_holder).into(imageView);
            }
        }else {

            play.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(ApiClient.Image_URL + list.getVideo_ar())
                    .into(imageView);

            play.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);

                if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                    MobileNavigationDirections.ActionGlobalVideoFragment action = ProductDetailsFragmentDirections.actionGlobalVideoFragment(ApiClient.Image_URL + list.getVideo_ar());
                    navController.navigate(action);
                } else {
                    MobileNavigationDirections.ActionGlobalVideoFragment action = ProductDetailsFragmentDirections.actionGlobalVideoFragment(ApiClient.Image_URL + list.getVideo());
                    navController.navigate(action);
                }

            });


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






























