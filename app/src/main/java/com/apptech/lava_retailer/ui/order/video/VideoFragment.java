package com.apptech.lava_retailer.ui.order.video;

import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.VideoFragmentBinding;

public class VideoFragment extends Fragment {

    private VideoViewModel mViewModel;
    VideoFragmentBinding binding;
    int mCurrentPosition = 0;


    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(getActivity() != null){
            getActivity().findViewById(R.id.toolbar_header).setVisibility(View.GONE);
        }

        binding = VideoFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        // TODO: Use the ViewModel



        MediaController controller = new MediaController(getContext());
        controller.setMediaPlayer(binding.VideoPlay);
        binding.VideoPlay.setMediaController(controller);




        String Stringuri = VideoFragmentArgs.fromBundle(getArguments()).getUrl();

        Uri uri = Uri.parse(Stringuri);
        binding.VideoPlay.setVideoURI(uri);
        binding.VideoPlay.setOnPreparedListener(mp -> {

            binding.progressBar.setVisibility(View.GONE);

            if (mCurrentPosition > 0) {
                binding.VideoPlay.seekTo(mCurrentPosition);
            } else {
                binding.VideoPlay.seekTo(1);
            }
            binding.VideoPlay.start();

        });

        binding.VideoPlay.setOnCompletionListener(mp -> {
            binding.VideoPlay.seekTo(0);
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity() != null){
            getActivity().findViewById(R.id.toolbar_header).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            getActivity().findViewById(R.id.toolbar_header).setVisibility(View.VISIBLE);
        }
    }
}




























