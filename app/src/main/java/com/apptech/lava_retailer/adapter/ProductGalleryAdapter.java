package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import com.apptech.lava_retailer.MobileNavigationDirections;
import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.list.product_gallery.ProductGalleryLists;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.ui.order.product_details.ProductDetailsFragmentDirections;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


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
        LinearLayout GipLoader = item_view.findViewById(R.id.GipLoader);


        if (list.getType().equalsIgnoreCase("IMAGE")){

            play.setVisibility(View.GONE);

            if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                Glide.with(context).load(ApiClient.Image_URL + list.get_img_url_ar()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        GipLoader.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        GipLoader.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
            } else {
                Glide.with(context).load(ApiClient.Image_URL + list.getImg_url()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        GipLoader.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        GipLoader.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
            }
        }else {

            play.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(ApiClient.Image_URL + list.getVideo_ar())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            GipLoader.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            GipLoader.setVisibility(View.GONE);
                            return false;
                        }
                    })
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






























