package com.apptech.lava_retailer.ui.trade_program.trade_program_img;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.TradeProgramImgOpenFragmentBinding;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TradeProgramImgOpenFragment extends Fragment {

    private TradeProgramImgOpenViewModel mViewModel;
    TradeProgramImgOpenFragmentBinding binding;
    private static final String TAG = "TradeProgramImgOpenFrag";
    PhotoViewAttacher pAttacher;

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

/*
        String url= "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        binding.pdfOpen.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = binding.pdfOpen.getSettings();
        settings.setJavaScriptEnabled(true);
        binding.pdfOpen.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.pdfOpen.getSettings().setBuiltInZoomControls(true);
        binding.pdfOpen.getSettings().setUseWideViewPort(true);
        binding.pdfOpen.getSettings().setLoadWithOverviewMode(true);
        binding.pdfOpen.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        binding.pdfOpen.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                progressbar.setVisibility(View.GONE);
//                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(requireActivity() , "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
*/



        if(getArguments()!=null){
            binding.materialTextView.setText(getArguments().getString("name"));
            String mStringUrl = ApiClient.Image_URL +getArguments().getString("image");


            Glide.with(getActivity()).load(mStringUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(binding.imageView);

        }


        pAttacher = new PhotoViewAttacher(binding.imageView);
        pAttacher.update();
        pAttacher.isZoomable();

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
        title.setText(getActivity().getString(R.string.Trade_Program));
    }

}