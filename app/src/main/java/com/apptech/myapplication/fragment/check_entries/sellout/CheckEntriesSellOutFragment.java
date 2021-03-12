package com.apptech.myapplication.fragment.check_entries.sellout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.CheckEntriesSellOutFragmentBinding;
import com.apptech.myapplication.fragment.check_entries_sellout_invalid_imei.CheckEntriesSellOutInvalidImeiFragment;
import com.apptech.myapplication.fragment.check_entries_sellout_pending_verification.CheckEntriesSellOutPendingVerificationFragment;
import com.apptech.myapplication.fragment.check_entries_sellout_valid_imei.CheckEntriesSellOutValidImeiFragment;

public class CheckEntriesSellOutFragment extends Fragment implements View.OnClickListener {

    private CheckEntriesSellOutViewModel mViewModel;
    CheckEntriesSellOutFragmentBinding binding;

    public static CheckEntriesSellOutFragment newInstance() {
        return new CheckEntriesSellOutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CheckEntriesSellOutFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckEntriesSellOutViewModel.class);
        // TODO: Use the ViewModel


        binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
        binding.validImeiTextview.setTextColor(getResources().getColor(R.color.black));

        binding.validImei.setOnClickListener(this);
        binding.InvalidImei.setOnClickListener(this);
        binding.pendingverification.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        loadfragment(new CheckEntriesSellOutValidImeiFragment());
        binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
        binding.validImeiTextview.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.CheckEntriesFrameLayout2, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validImei:
                loadfragment(new CheckEntriesSellOutValidImeiFragment());
                // chnage white color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.black));
                // valid chnage black color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage black color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.InvalidImei:
                loadfragment(new CheckEntriesSellOutInvalidImeiFragment());
                // chnage white color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.black));
                // valid chnage black color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage black color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.pendingverification:
                loadfragment(new CheckEntriesSellOutPendingVerificationFragment());
                // chnage white black color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // valid chnage black color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage  color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }
}






































