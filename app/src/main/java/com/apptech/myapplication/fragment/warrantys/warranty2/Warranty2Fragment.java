package com.apptech.myapplication.fragment.warrantys.warranty2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.Warranty2FragmentBinding;

public class Warranty2Fragment extends Fragment {

    private Warranty2ViewModel mViewModel;
    Warranty2FragmentBinding binding;
    View rowView;
    LinearLayout removeBtn;

    public static Warranty2Fragment newInstance() {
        return new Warranty2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = Warranty2FragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Warranty2ViewModel.class);
        // TODO: Use the ViewModel


        binding.scanBtn.setOnClickListener(v -> {
            if (!binding.ImeiEdittext.getText().toString().trim().isEmpty()) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.add_view, null);
                binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);
                TextView textView = rowView.findViewById(R.id.imei);
                textView.setText(binding.ImeiEdittext.getText().toString().trim());
                removeBtn = rowView.findViewById(R.id.remove);
                removeView();
                binding.ImeiEdittext.setText(null);
            } else
                Toast.makeText(requireContext(), getResources().getString(R.string.field_required), Toast.LENGTH_SHORT).show();

        });



    }



    private void removeView() {
        removeBtn.setOnClickListener(v -> {
            binding.addLayout.removeView((View) v.getParent().getParent());
        });
    }

}
































