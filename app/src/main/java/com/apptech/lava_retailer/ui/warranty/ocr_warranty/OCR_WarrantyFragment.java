package com.apptech.lava_retailer.ui.warranty.ocr_warranty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.databinding.OCRWarrantyFragmentBinding;

import java.util.Calendar;

public class OCR_WarrantyFragment extends Fragment {

    private OCRWarrantyViewModel mViewModel;
    OCRWarrantyFragmentBinding binding;
    DatePickerDialog picker;
    private static final String TAG = "OCR_WarrantyFragment";
    Calendar cldr;
    int day, month, year;
    AlertDialog dialog;

    public static OCR_WarrantyFragment newInstance() {
        return new OCR_WarrantyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = OCRWarrantyFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OCRWarrantyViewModel.class);
        // TODO: Use the ViewModel
        cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        binding.WorkOrderDate.setText(day + "-" + (month + 1) + "-" + year);

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
//        alertDialog.setCancelable(false);
//        View view = LayoutInflater.from(requireContext()).inflate(R.layout.row_ocr_dialog_warranty , null);
//        alertDialog.setView(view);
//        dialog = alertDialog.create();
//        dialog.show();

        ActivitAction();
    }

    private void ActivitAction() {
        binding.WorkOrderDate.setOnClickListener(v -> {
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
//                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        binding.WorkOrderDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });


        binding.Next.setOnClickListener(v -> {

            if (binding.Layout1.getVisibility() != View.INVISIBLE) {
                //                type 1
//                Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
//                binding.Layout1.startAnimation(slideUp);

                //                type 2
//                Animation animation = new TranslateAnimation(0, -700, 0, 0);
//                animation.setDuration(2000);
//                binding.Layout1.startAnimation(animation);

                //                type 3
                binding.Layout1.animate().translationX(0).translationXBy(-700).setDuration(400).withEndAction(() -> {
                    binding.Layout1.setVisibility(View.GONE);
                    binding.Layout2.setVisibility(View.VISIBLE);
                }).setDuration(400);

            }

        });


        binding.Next2.setOnClickListener(v -> {
            if (binding.Layout2.getVisibility() != View.INVISIBLE) {
                {
                    binding.Layout2.animate().translationX(0).translationXBy(-700).setDuration(400).withEndAction(() -> {
                        binding.Layout2.setVisibility(View.GONE);
                        binding.Layout3.setVisibility(View.VISIBLE);
                    }).setDuration(400);
                }
            }
        });


        binding.Next3.setOnClickListener(v -> {
            if (binding.Layout3.getVisibility() != View.INVISIBLE) {
                {
                    binding.Layout3.animate().translationX(0).translationXBy(-700).setDuration(400).withEndAction(() -> {
                        binding.Layout3.setVisibility(View.GONE);
                        binding.Layout4.setVisibility(View.VISIBLE);
                    }).setDuration(400);
                }
            }
        });

        binding.Next4.setOnClickListener(v -> {
            if (binding.Layout4.getVisibility() != View.INVISIBLE) {
                {
                    binding.Layout4.animate().translationX(0).translationXBy(-700).setDuration(400).withEndAction(() -> {
                        binding.Layout4.setVisibility(View.GONE);
                        binding.Layout5.setVisibility(View.VISIBLE);
                    }).setDuration(400);
                }
            }
        });

        binding.pre2.setOnClickListener(v -> {
            binding.Layout1.setVisibility(View.VISIBLE);
            binding.Layout2.setVisibility(View.GONE);

//            if (binding.Layout2.getVisibility() != View.INVISIBLE) {
//                {
//                    binding.Layout2.animate().translationX(0).translationXBy(700).setDuration(400).withEndAction(() -> {
//                        binding.Layout2.setVisibility(View.GONE);
//                        binding.Layout1.setVisibility(View.VISIBLE);
//                    }).setDuration(400);
//                }
//            }
        });

        binding.pre3.setOnClickListener(v -> {
            binding.Layout3.setVisibility(View.GONE);
            binding.Layout2.setVisibility(View.VISIBLE);

        });

        binding.pre4.setOnClickListener(v -> {});

    }

}

























