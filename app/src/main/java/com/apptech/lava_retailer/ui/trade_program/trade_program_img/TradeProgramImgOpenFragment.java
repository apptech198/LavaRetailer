package com.apptech.lava_retailer.ui.trade_program.trade_program_img;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
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
import android.view.MotionEvent;
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
    String FileType = "";
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        // TODO: Use the ViewModel



        if(getArguments()!=null){

            FileType = getArguments().getString("file_type");
            binding.materialTextView.setText(getArguments().getString("name"));

            if(FileType.equalsIgnoreCase("IMAGE")){
                binding.imageView.setVisibility(View.VISIBLE);
                binding.pdfOpen.setVisibility(View.GONE);
                Imagezoom();
            }else {
                progressDialog.show();
                binding.pdfOpen.setVisibility(View.VISIBLE);
                binding.imageView.setVisibility(View.GONE);
                PdfOpen();
            }


        }


        pAttacher = new PhotoViewAttacher(binding.imageView);
        pAttacher.update();
        pAttacher.isZoomable();

    }


    void PdfOpen(){

        String pdfurl= ApiClient.Image_URL + getArguments().getString("image");
//        String url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfurl;
        String url = "https://docs.google.com/gview?embedded=true&url=" + pdfurl;


        WebView webView = binding.pdfOpen;
        WebSettings settings = webView.getSettings();


        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (view.getTitle().equals("")){
                    view.reload();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(requireActivity() , "Error:" + description, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });




    }


    void Imagezoom(){

        String mStringUrl = ApiClient.Image_URL + getArguments().getString("image");

        Glide.with(requireActivity()).load(mStringUrl).listener(new RequestListener<Drawable>() {
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