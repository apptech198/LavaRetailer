package com.apptech.lava_retailer.ui.trade_program.price_list;

import androidx.lifecycle.ViewModelProvider;


import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.PricelistFragmentBinding;
import com.bumptech.glide.Glide;

public class PricelistFragment extends Fragment {

    private PricelistViewModel mViewModel;
    PricelistFragmentBinding binding;

    //These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int DRAW =3;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    // Limit zoomable/pannable image
    private float[] matrixValues = new float[9];
    private float maxZoom;
    private float minZoom;
    private float height;
    private float width;
    private RectF viewRect;

    private static final String TAG = "PricelistFragment";
    
    public static PricelistFragment newInstance() {
        return new PricelistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PricelistFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PricelistViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/fede1d85f86de8482853afd2de64d23b.jpeg").into(binding.imageView);

//        app:zoomage_restrictBounds="false"
//        app:zoomage_animateOnReset="true"
//        app:zoomage_autoResetMode="UNDER"
//        app:zoomage_autoCenter="true"
//        app:zoomage_zoomable="true"
//        app:zoomage_translatable="true"
//        app:zoomage_minScale="0.6"
//        app:zoomage_maxScale="8"

//        binding.imageView.setRestrictBounds(false);
//        binding.imageView.setAnimateOnReset(true);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.setOnTouchListener((v, event) -> false);



    }



    
    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Price List");
    }


    public interface IOnFocusListenable {
        public void onWindowFocusChanged(boolean hasFocus);
    }




}































