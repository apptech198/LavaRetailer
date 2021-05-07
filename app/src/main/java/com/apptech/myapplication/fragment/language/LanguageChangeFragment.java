package com.apptech.myapplication.fragment.language;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.LoginActivity;
import com.apptech.myapplication.activity.MainActivity;
import com.apptech.myapplication.databinding.FragmentLanguageChangeBinding;
import com.apptech.myapplication.other.SessionManage;

import org.jetbrains.annotations.NotNull;


public class LanguageChangeFragment extends Fragment {


    FragmentLanguageChangeBinding binding;
    private static final String TAG = "LanguageChangeFragment";
    SessionManage sessionManage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLanguageChangeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sessionManage = SessionManage.getInstance(requireContext());


        binding.RadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = view.findViewById(checkedId);
            Log.e(TAG, "onCreate: " + radioButton.getTag());
            if (radioButton.getTag().equals("ENGLISH")) {
                sessionManage.setlanguage("en");
                startActivity(new Intent(requireContext(), MainActivity.class));
                return;
            }
            sessionManage.setlanguage("ar");
            startActivity(new Intent(requireContext(), MainActivity.class));

        });

    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Language");
    }


}