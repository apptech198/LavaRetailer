package com.apptech.lava_retailer.ui.barcode_scanner;


import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.lava_retailer.databinding.BarCodeScannerFragmentBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;

public class BarCodeScannerFragment extends Fragment {

    private BarCodeScannerViewModel mViewModel;
    BarcodeDetector barcodeDetector;
    BarCodeScannerFragmentBinding binding;
    CameraSource cameraSource;
    private static final String TAG = "BarCodeScannerFragment";
    String intentData = "";
    BackPressBarCode backPressBarCode;


    public BarCodeScannerFragment(BackPressBarCode backPress) {
        this.backPressBarCode = backPress;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BarCodeScannerFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BarCodeScannerViewModel.class);
        // TODO: Use the ViewModel

         barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

         cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        binding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    cameraSource.start(binding.surfaceView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections detections) {


                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    binding.barcodeValue.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            binding.barcodeValue.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).email.address;
                            binding.barcodeValue.setText(intentData);
                        } else {
                            intentData = barcodes.valueAt(0).displayValue;
                            binding.barcodeValue.setText(intentData);
                            backPressBarCode.OnbackpressBarcode(intentData);
                            cameraSource.release();
                        }
                        getActivity().getSupportFragmentManager().popBackStack();

                    });
                }

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }

    public interface BackPressBarCode {
        void OnbackpressBarcode(String imei);
    }


}






































