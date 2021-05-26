package com.apptech.lava_retailer.ui.trade_program.trade_program_img;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.TradeProgramImgOpenFragmentBinding;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TradeProgramImgOpenFragment extends Fragment {

    private TradeProgramImgOpenViewModel mViewModel;
    TradeProgramImgOpenFragmentBinding binding;
    private static final String TAG = "TradeProgramImgOpenFrag";

    public static TradeProgramImgOpenFragment newInstance() {
        return new TradeProgramImgOpenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TradeProgramImgOpenFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TradeProgramImgOpenViewModel.class);
        // TODO: Use the ViewModel


        Log.e(TAG, "onActivityCreated: "  + ApiClient.Image_URL + "/uploads/1051e8590a9bb77577396b58292bce3a.jpg"  );
//        Bitmap bitmap = getBitmapfromUrl(ApiClient.Image_URL + "uploads/1051e8590a9bb77577396b58292bce3a.jpg");
//        Uri uri = getImageUri(getContext() , bitmap);
        binding.imageView.setImageResource(R.drawable.a1);
//        binding.imageView.setImageURI(uri);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Trade Program");
    }

}