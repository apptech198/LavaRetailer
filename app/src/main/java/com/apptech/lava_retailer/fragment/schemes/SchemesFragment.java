package com.apptech.lava_retailer.fragment.schemes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.SchemesFragmentBinding;
import com.apptech.lava_retailer.fragment.schemes_special.SchemesSpecialFragment;
import com.apptech.lava_retailer.fragment.schenes_normal.SchemesNormalFragment;


public class SchemesFragment extends Fragment {

    private SchemesViewModel mViewModel;
    SchemesFragmentBinding binding;

    public static SchemesFragment newInstance() {
        return new SchemesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SchemesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SchemesViewModel.class);
        // TODO: Use the ViewModel


        binding.Normal.setBackground(getResources().getDrawable(R.drawable.left_corner_color));
        binding.Normal.setTextColor(getResources().getColor(R.color.black));

        binding.Normal.setOnClickListener(v -> {

            loadfragment(new SchemesNormalFragment());
            binding.Normal.setBackground(getResources().getDrawable(R.drawable.left_corner_color));
            binding.Normal.setTextColor(getResources().getColor(R.color.black));

            binding.Special.setBackground(getResources().getDrawable(R.drawable.right_corner_round_black));
            binding.Special.setTextColor(getResources().getColor(R.color.white));

        });


        binding.Special.setOnClickListener(v -> {
            loadfragment(new SchemesSpecialFragment());

            binding.Special.setBackground(getResources().getDrawable(R.drawable.left_corner_color));
            binding.Special.setTextColor(getResources().getColor(R.color.black));


            binding.Normal.setBackground(getResources().getDrawable(R.drawable.right_corner_round_black));
            binding.Normal.setTextColor(getResources().getColor(R.color.white));

        });


    }


    @Override
    public void onStart() {
        super.onStart();
        loadfragment(new SchemesNormalFragment());
        binding.Normal.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
        binding.Normal.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.PurchRequestFrameLayout, fragment).addToBackStack(null).commit();
    }


}

























